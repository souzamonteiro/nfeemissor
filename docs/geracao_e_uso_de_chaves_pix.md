# Guia completo: Geração e uso de chaves PIX

---

## 1) Tipos de chave PIX (resumo)

Existem cinco tipos de chave que o sistema PIX aceita como identificadores:

* **CPF** (11 dígitos numéricos)
* **CNPJ** (14 dígitos numéricos)
* **Telefone celular** no formato internacional (ex.: `+5511999998888`) — geralmente 11–14 dígitos sem espaços (com o “+” quando apresentado).
* **Email** (endereço de e-mail válido)
* **Chave aleatória (EVP / EVP/EVP — "endereço virtual de pagamento")** gerada pelo PSP, normalmente 32 bytes codificados (ou base64/hex dependente da API).

Esses tipos, suas regras e formatos são definidos nos documentos do Pix/Bacen. ([Banco Central do Brasil][1])

---

## 2) Regras práticas de formato e validação (por tipo)

### CPF

* Exatamente 11 dígitos numéricos; validação com dígitos verificadores (algoritmo padrão de CPF).
* Não aceitar pontuação (remover `.` e `-` antes de validar). ([Banco Central do Brasil][1])

### CNPJ

* Exatamente 14 dígitos numéricos; validar com dígitos verificadores de CNPJ. Remover `./-`. ([Banco Central do Brasil][1])

### Telefone

* Preferir E.164 (`+55` + DDD + número) — ex.: `+5511999998888`.
* No payload EMV, deve ser gravado tal qual (com o `+` se usado); os PSPs podem exigir apenas dígitos internamente. ([Banco Central do Brasil][1])

### Email

* Sintaxe conforme RFC (validação básica: `local@domain` com domínio válido). Tamanho máximo permitido pelo arranjo: verificar normas do PSP/Bacen (tipicamente até 77 caracteres). ([Banco Central do Brasil][1])

### Chave aleatória (EVP)

* Gerada pelo PSP/participant; formato interno varia (geralmente 32 bytes). Não deve ser previsível; armazenar com segurança. ([Banco Central do Brasil][2])

---

## 3) Fluxo de criação e associação (boas práticas)

1. **Solicitação do usuário**: usuário pede cadastro/associação da chave no seu PSP.
2. **Validações iniciais**: checar formato (ex.: CPF = 11 dígitos) e verificação semântica (algoritmo de dígito verificador).
3. **Résolução/registro no DICT**: o PSP registra a chave no DICT do Bacen (ou consulta no fluxo de vinculação), o que torna a chave pesquisável. Use as APIs do DICT para confirmar. ([Banco Central do Brasil][2])
4. **Confirmação para usuário**: envio de confirmação por e-mail/sms quando aplicável.
5. **Armazenamento**: guardar meta-dados (ID, tipo, data, status) cifrados; nunca armazenar credenciais sensíveis desnecessárias.

---

## 4) Segurança e requisitos mínimos (práticas obrigatórias / recomendadas)

* Autenticação e autorização forte para operações de cadastro/remoção.
* **Armazenamento cifrado** de chaves aleatórias e metadados sensíveis (AES-GCM, KMS).
* Logging e rastreabilidade (quem, quando, como) para auditoria.
* Rate limiting e proteção contra enumeração de chaves.
* Proteção de dados pessoais (LGPD): peça consentimento, mantenha prazos de retenção e minimize dados.
  Considere o **Manual de Segurança do Pix** do Bacen para obrigações técnicas atualizadas. ([Banco Central do Brasil][3])

---

## 5) Regras do TXID e Additional Data (campo 62)

* TXID (subcampo 05 dentro do campo 62) deve ter até 25 caracteres; define identificador local da transação. Se vazio, usar valor padrão "***" ou similar. (Ver regras no BR Code e no Manual de Padrões de Iniciação do Pix). ([Banco Central do Brasil][4])

---

## 6) Como montar o Merchant Account Information (campo 26) para PIX

* Subcampo `00` = GUI `br.gov.bcb.pix` (ex.: `00 14 br.gov.bcb.pix`)
* Subcampo `01` = chave Pix (por ex. `01 14 +5511999998888`)
* Concatene: `26` + `<lenTotal>` + `00` + `<lenGui>` + `br.gov.bcb.pix` + `01` + `<lenChave>` + `<chave>`
  Siga TLV (Tag-Length-Value) conforme padrão EMV. ([mvallim.github.io][5])

---

## 7) CRC16: exatamente como calcular (ponto crítico)

* **Polinômio**: `0x1021` (CRC-CCITT)
* **Valor inicial**: `0xFFFF`
* **Input**: BYTES ASCII (US-ASCII) do payload **até** incluir `63` e `04` (ou seja, acrescente a string literal `"6304"` ao final do buffer para cálculo), **mas sem** os 4 hex do CRC.
* **Output**: 4 caracteres hex em maiúsculo (ex.: `5097`) adicionados após `6304`.
* **ATENÇÃO**: usar bytes ASCII — NÃO use `char` UTF-16 diretamente; use `getBytes(StandardCharsets.US_ASCII)` (Java). Implementações devem corresponder ao padrão EMV (não refletir input/output, sem XOR de saída). ([srecord.sourceforge.net][6])

---

## 8) Exemplo de construção de payload (passo a passo)

Suponha: chave `+5511999998888`, nome `MARIA SILVA`, cidade `BELO HORIZONTE`, valor `150.00`, txid `SERVICO #123`.

1. 00 (Payload Format): `00 02 01` → `000201`
2. 26 (Merchant Account): `26 36 00 14 br.gov.bcb.pix 01 14 +5511999998888` → `26360014br.gov.bcb.pix0114+5511999998888`
3. 52: `52040000`
4. 53: `5303986`
5. 54: `5406150.00` (se houver valor)
6. 58: `5802BR`
7. 59: `5911MARIA SILVA` (nome sem acentos, até 25 chars)
8. 60: `6014BELO HORIZONTE` (cidade sem acentos, até 15 chars)
9. 62: `62 16 05 12 SERVICO #123` → `62160512SERVICO #123`
10. Antes de CRC: concatene tudo e acrescente `6304` para cálculo do CRC
11. Calcule CRC16 sobre bytes ASCII do passo 10 → ex.: `5097`
12. Anexe `6304` + CRC → `63045097`

Resultado final (concatenado, sem espaços):
`00020126360014br.gov.bcb.pix0114+55119999988885204000053039865406150.005802BR5911MARIA SILVA6014BELO HORIZONTE62160512SERVICO #12363045097`
(O validador do site mostra estrutura e `5097` como CRC correto). ([mvallim.github.io][5])

---

## 9) Exemplo mínimo de código (Java) — cálculo correto do CRC

**Apenas** o método CRC (use `StandardCharsets.US_ASCII`):

```java
import java.nio.charset.StandardCharsets;

private static String calcularCRC16(String data) {
    int crc = 0xFFFF;
    byte[] bytes = data.getBytes(StandardCharsets.US_ASCII);

    for (byte b : bytes) {
        crc ^= (b & 0xFF) << 8;
        for (int j = 0; j < 8; j++) {
            if ((crc & 0x8000) != 0) {
                crc = (crc << 1) ^ 0x1021;
            } else {
                crc <<= 1;
            }
            crc &= 0xFFFF;
        }
    }
    return String.format("%04X", crc);
}
```

Lembre: para calcular CRC, passe `payloadSemCRC + "6304"` como `data`. ([srecord.sourceforge.net][6])

---

## 10) Validação completa (lista de checagem antes de aceitar/emitir payload)

* [ ] Chave Pix formatada e validada (CPF/CNPJ/telefone/email/EVP).
* [ ] Campos nome/cidade submetidos em MAIÚSCULAS e sem acentos (remover caracteres inválidos).
* [ ] LL (comprimentos) calculados corretamente em cada TLV.
* [ ] Subcampo 26 com GUI `br.gov.bcb.pix` e subcampo 01 com chave.
* [ ] Se valor presente, usar formato `0.00` (ponto decimal).
* [ ] Acrescentar `6304` e calcular CRC sobre BYTES ASCII.
* [ ] Gerar CRC em HEX (4 chars, maiúsculo) e anexar.
* [ ] Testar no validador BR Code / ferramentas de bancos. ([Banco Central do Brasil][4])

---

## 11) Integração com DICT e API do PSP

* Ao cadastrar chave no seu sistema, use as APIs do DICT (ou do PSP) para registrar/vincular e consultar informações (identificador da conta, favorecido, etc.). O DICT é o diretório do Bacen para resolver chaves. ([Banco Central do Brasil][2])

---

## 12) Referências oficiais (leitura obrigatória)

* **BR Code – QR Codes para iniciação de pagamentos no SPB** (Manual BR Code). ([Banco Central do Brasil][4])
* **Manual de Padrões para Iniciação do Pix** (Bacen) — regras de iniciação via QR Code. ([Banco Central do Brasil][1])
* **EMV® QR Code Specification for Payment Systems (Merchant-Presented)** — padrão EMV usado como base. ([mvallim.github.io][5])
* **DICT / APIs Pix (Banco Central)** — regras de registro e consulta de chaves. ([Banco Central do Brasil][2])
* **CRC16 CCITT (polinômio 0x1021, seed 0xFFFF)** — comportamento exigido pelo EMV / BR Code. ([srecord.sourceforge.net][6])

## 13) Referências

[1]: https://www.bcb.gov.br/content/estabilidadefinanceira/pix/Regulamento_Pix/II_ManualdePadroesparaIniciacaodoPix.pdf?utm_source=chatgpt.com "Manual de Padrões para Iniciação do Pix"
[2]: https://www.bcb.gov.br/content/estabilidadefinanceira/pix/API-DICT.html?utm_source=chatgpt.com "DICT API"
[3]: https://www.bcb.gov.br/content/estabilidadefinanceira/cedsfn/Manual_de_Seguranca_PIX.pdf?utm_source=chatgpt.com "Manual de Segurança do Pix Versão 3.7"
[4]: https://www.bcb.gov.br/content/estabilidadefinanceira/spb_docs/ManualBRCode.pdf?utm_source=chatgpt.com "QR Codes para iniciação de pagamentos no SPB"
[5]: https://mvallim.github.io/emv-qrcode/docs/EMVCo-Merchant-Presented-QR-Specification-v1-1.pdf?utm_source=chatgpt.com "QR Code Specification for Payment Systems (EMV QRCPS)"
[6]: https://srecord.sourceforge.net/crc16-ccitt.html?utm_source=chatgpt.com "CRC-CCITT -- 16-bit"
