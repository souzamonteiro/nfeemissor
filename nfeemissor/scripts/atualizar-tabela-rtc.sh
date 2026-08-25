#!/usr/bin/env bash
set -euo pipefail

BASE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

pushd "$BASE_DIR" >/dev/null
mvn -q -DskipTests compile
mvn -q -DskipTests -Dexec.mainClass=com.souzamonteiro.nfeemissor.AtualizarTabelaRtc -Dexec.classpathScope=runtime org.codehaus.mojo:exec-maven-plugin:3.5.0:java
popd >/dev/null

echo "Tabela RTC obtida da SEFAZ e salva em $BASE_DIR/tabelas/consulta-tributacao-rtc.json"
