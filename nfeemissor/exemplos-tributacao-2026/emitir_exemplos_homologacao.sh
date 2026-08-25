#!/usr/bin/env bash
set -euo pipefail

# Envia todos os exemplos de tributação para a instância local em homologação.
# Pré-requisitos: jq, curl e o emissor já iniciado em ../ (mvn package; java -jar ...).
# A numeração é sempre recriada a partir de 6000, sem alterar os arquivos de exemplo.

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
EMISSOR_DIR="$(cd "$DIR/.." && pwd)"
CONFIG="$EMISSOR_DIR/NFeEmissor.json"
URL="${1:-http://localhost:3435/NFeAutorizacao}"
PRIMEIRA_NFE="${PRIMEIRA_NFE:-6000}"

command -v jq >/dev/null || { echo "jq é obrigatório." >&2; exit 1; }
command -v curl >/dev/null || { echo "curl é obrigatório." >&2; exit 1; }

[[ "$(jq -r '.webserviceAmbiente' "$CONFIG")" == "2" ]] || {
  echo "Recusado: este script só pode emitir em homologação (webserviceAmbiente = 2)." >&2
  exit 1
}

curl --fail --silent --max-time 3 "${URL%/NFeAutorizacao}/NFeStatusServico" >/dev/null || {
  echo "Emissor indisponível em $URL. Inicie-o em $EMISSOR_DIR antes de executar." >&2
  exit 1
}

numero="$PRIMEIRA_NFE"
for exemplo in "$DIR"/*.json; do
  temporario="$(mktemp)"
  cNF="$(printf '%08d' "$numero")"
  jq --arg nNF "$numero" --arg cNF "$cNF" \
    '.infNFe.ide.nNF = $nNF | .infNFe.ide.cNF = $cNF' "$exemplo" > "$temporario"

  echo "Emitindo $(basename "$exemplo") com NF-e $numero..."
  curl --fail-with-body --silent --show-error -X POST \
    -H 'Content-Type: application/json' --data @"$temporario" "$URL"
  printf '\n'
  rm -f "$temporario"
  numero=$((numero + 1))
done
