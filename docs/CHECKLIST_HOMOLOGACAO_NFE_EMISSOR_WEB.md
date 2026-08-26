# CHECKLIST DE HOMOLOGAÇÃO - nfe-emissor-web

## 1. PREPARAÇÃO

- [ ] Verificar que o servidor Tomcat da aplicação está online
- [ ] Verificar que o emissor NF-e está online na porta 3435
- [ ] Confirmar o endpoint /NFeStatusServico retornando "Serviço em Operação"
- [ ] Definir sequência de NF-e inicial em 100000 para evitar conflito com Aurora POS
- [ ] Confirmar ambiente em homologação

## 2. ACESSO E AUTENTICAÇÃO

- [ ] Acessar /nfeemissor/login.xhtml
- [ ] Logar com usuário admin
- [ ] Informar senha admin123
- [ ] Confirmar redirecionamento para dashboard
- [ ] Validar que menu de cadastros e vendas está disponível

## 3. CONFIGURAÇÃO INICIAL

- [ ] Acessar Configurações
- [ ] Definir UF emitente = BA
- [ ] Definir UF webservice = BA
- [ ] Definir ambiente = Homologação
- [ ] Definir série = 1
- [ ] Definir número inicial da NF-e = 100000
- [ ] Definir natureza da operação = VENDA
- [ ] Definir finalidade = 1
- [ ] Definir consumidor final = 1
- [ ] Definir presença do comprador = 1
- [ ] Definir porta do servidor = 3435
- [ ] Definir caminho do certificado
- [ ] Definir senha do certificado
- [ ] Definir caminho dos schemas
- [ ] Definir caminho XML
- [ ] Salvar e reabrir configuração

## 4. EMPRESA

- [ ] Cadastrar empresa com dados válidos
- [ ] Confirmar CNPJ da empresa
- [ ] Confirmar IE, endereço, município e UF
- [ ] Confirmar CRT conforme regime do teste
- [ ] Salvar e reabrir cadastro

## 5. CLIENTE

- [ ] Cadastrar cliente Andréia Rita da Silva
- [ ] Confirmar CPF válido
- [ ] Confirmar endereço e município
- [ ] Confirmar UF BA
- [ ] Salvar cliente

## 6. PRODUTOS

### 6.1 Produto simples / tributação normal

- [ ] Cadastrar produto 7890647097098 - Caneta esferográfica azul
- [ ] Confirmar NCM, CFOP, unidade e valor unitário
- [ ] Confirmar ICMS 18,50%
- [ ] Confirmar PIS 1,65%
- [ ] Confirmar COFINS 7,60%
- [ ] Salvar produto

### 6.2 Produto com ICMS 7%

- [ ] Cadastrar produto 7898480650104 - Óleo de cambio manual SAE 80W Flex Oil 1Ltl
- [ ] Confirmar NCM, CFOP, unidade e valor unitário
- [ ] Confirmar ICMS 7%
- [ ] Confirmar PIS e COFINS conforme regra
- [ ] Salvar produto

### 6.3 Produto com redução de base

- [ ] Cadastrar produto de CST 20
- [ ] Informar percentual de redução (41,1760)
- [ ] Informar ICMS 18%
- [ ] Confirmar cálculo de base reduzida
- [ ] Salvar produto

## 7. FLUXO DE VENDA - REGIME SIMPLES

- [ ] Abrir tela de vendas
- [ ] Criar nova venda
- [ ] Selecionar cliente Andréia Rita
- [ ] Adicionar produto de ICMS simples
- [ ] Confirmar quantidade e total
- [ ] Finalizar venda
- [ ] Validar envio da NF-e
- [ ] Confirmar chave de acesso e status EMITIDA
- [ ] Verificar DANFE/PDF

## 8. FLUXO DE VENDA - REGIME NORMAL / REDUÇÃO DE BASE

- [ ] Criar nova venda
- [ ] Selecionar mesmo cliente
- [ ] Adicionar produto com CST 20
- [ ] Confirmar base reduzida e ICMS calculado
- [ ] Finalizar venda
- [ ] Validar emissão com sucesso
- [ ] Confirmar chave e status

## 9. FLUXO DE VENDA - MISTO EM UM MESMO DOCUMENTO

- [ ] Criar nova venda
- [ ] Selecionar cliente
- [ ] Incluir produtos com CSTs diferentes
- [ ] Confirmar total geral
- [ ] Finalizar venda
- [ ] Verificar diferentes alíquotas item a item
- [ ] Validar NF-e emitida corretamente

## 10. VALIDAÇÃO DE TRIBUTAÇÃO E CRT

- [ ] Validar Simples Nacional em operação normal
- [ ] Validar Tributação normal com redução de base
- [ ] Validar mistura de CSTs na mesma nota
- [ ] Validar JSON enviado ao emissor
- [ ] Validar valores finais em cada item
- [ ] Validar valores totais da nota

## 11. REIMPRESSÃO / CONSULTA

- [ ] Acessar listagem de vendas
- [ ] Selecionar NF-e emitida
- [ ] Confirmar chave e protocolo
- [ ] Abrir DANFE / PDF
- [ ] Validar dados da nota

## 12. RESULTADO FINAL

- [ ] Login validado
- [ ] Configuração validada
- [ ] Empresa validada
- [ ] Cliente validado
- [ ] Produtos validados
- [ ] Simples Nacional validado
- [ ] Tributação normal validada
- [ ] Operação mista validada
- [ ] Emissão em homologação validada
- [ ] Reimpressão validada
- [ ] Resultado final aprovado / reprovado

## 13. OBSERVAÇÕES

- [ ] Registrar qualquer divergência entre cálculo esperado e valor gerado
- [ ] Registrar necessidade de ajuste de calendário tributário / CRT / CST
- [ ] Registrar número da NF-e usada em cada cenário
- [ ] Registrar data e responsável pela homologação
