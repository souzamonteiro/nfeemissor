# ROTEIRO DE HOMOLOGAÇÃO DO nfe-emissor-web

* Data: <dd/mm/aaaa>
* Sistema: nfe-emissor-web
* Versão: <Versão do sistema>
* Realizador: <Nome da pessoa que realizou a homologação>

## 1. OBJETIVO

Validar o fluxo completo de operação do sistema de emissão de NF-e, cobrindo:
- autenticação e acesso;
- configuração do ambiente e da empresa;
- cadastro de clientes e produtos;
- cálculo de tributos por CST e alíquota;
- emissão da NF-e em homologação;
- consulta e reimpressão do DANFE;
- verificação de valores e compatibilidade com o guia de tributação.

Este roteiro foi criado com base no fluxo do PDV Aurora e adaptado para o nfe-emissor-web, mantendo dados válidos e cenários fiscais reais.

---

## 2. LIMPEZA DO BANCO DE DADOS

1. Acesse o sistema e realize o login com usuário administrador.
2. Se houver opção de reset do banco ou limpeza de dados, execute a limpeza conforme a aplicação.
3. Confirme que os dados iniciais foram removidos.

- [ ] Funcionou
- [ ] Não funcionou

---

## 3. ENTRADA NO SISTEMA

1. Abra a aplicação.
2. Acesse a tela de login.
3. Informe:
   - Usuário: admin
   - Senha: admin123
4. Confirme que o sistema entra corretamente.

- [ ] Funcionou
- [ ] Não funcionou

---

## 4. CONFIGURAÇÕES DO SISTEMA

1. Acesse a opção [Configurações].
2. Configure os seguintes campos:
   - Ambiente: Homologação (2)
   - UF Emitente: BA
   - Webservice UF: BA
   - Série NF-e: 1
   - Número inicial da NF-e: 100000 (valor seguro para não conflitar com notas do Aurora POS em homologação)
   - Natureza da Operação: VENDA
   - Finalidade da Emissão: 1
   - Consumidor Final: 1
   - Presença do Comprador: 1
   - Porta do Servidor: 3435
   - CNPJ autorizado: 13937073000156
   - Caminho dos schemas: /home/schemas/
   - Caminho do certificado: /home/certificados/certificado.pfx
   - Senha do certificado: 123456
   - Caminho de XML: /home/xml
3. Salve a configuração.
4. Reabra a tela de Configurações e valide se as informações ficaram corretas.

- [ ] Funcionou
- [ ] Não funcionou

---

## 5. CADASTRO DA EMPRESA

1. Acesse a opção [Empresa].
2. Configure os dados abaixo:
   - Razão Social: Editora Roberto Luiz Souza Monteiro
   - Nome Fantasia: Editora Science Books
   - CNPJ: 33630582000149
   - Inscrição Estadual: 158039716
   - CRT: 3
   - Logradouro: Rua Chile
   - Número: s/n
   - Complemento: Edifício Eduardo De Moraes, sala 606
   - Bairro: Centro
   - Cidade: Salvador
   - Código da Cidade: 2927408
   - UF: BA
   - CEP: 40020000
   - Telefone: 71991721874
   - E-mail: roberto@souzamonteiro.com
3. Salve.
4. Reabra a tela e confirme que os dados permaneceram corretos.

- [ ] Funcionou
- [ ] Não funcionou

---

## 6. CADASTRO DE CLIENTES

### 6.1 Cliente pessoa física

1. Acesse a opção [Clientes].
2. Clique em [Novo].
3. Preencha:
   - Nome: Andréia Rita da Silva
   - CPF: 40325635862
   - Logradouro: Rua Tanhaçu
   - Número: 11
   - Complemento: Quadra 5
   - Bairro: Ipitanga
   - Cidade: Lauro de Freitas
   - Código da Cidade: 2919207
   - UF: BA
   - CEP: 42706730
   - Telefone: 71991965292
   - E-mail: andreiarita2@gmail.com
4. Salve.
5. Reabra o cadastro e confirme.

- [ ] Funcionou
- [ ] Não funcionou

---

## 7. CADASTRO DE PRODUTOS

> Observação: o roteiro do Aurora POS cobre mais de um regime tributário. Para o nfe-emissor-web, a validação deve ser feita em ciclos de tributação, e não apenas em um único cenário. A mesma base operacional deve ser repetida com diferentes combinações de CST/CRT para verificar a consistência do cálculo.

### 7.1 Produto 1: Óleo de câmbio

1. Acesse a opção [Produtos].
2. Clique em [Novo].
3. Preencha:
   - Código: 7898480650104
   - EAN: 7898480650104
   - Descrição: Óleo de cambio manual SAE 80W Flex Oil 1Ltl
   - NCM: 27101932
   - CEST: 0600500
   - CFOP: 5102
   - Unidade: UN
   - Valor Unitário: 13.00
   - Origem: 0
   - CST ICMS: 00
   - Modalidade de BC: 3
   - Alíquota ICMS: 7.00
   - CST PIS: 01
   - Alíquota PIS: 1.65
   - CST COFINS: 01
   - Alíquota COFINS: 7.60
   - CST RTC: 000
   - Classificação RTC: 000001
   - IBS UF: 0.10
   - IBS Município: 0.00
   - CBS: 0.90
4. Salve.
5. Reabra o produto e valide.

- [ ] Funcionou
- [ ] Não funcionou

### 7.2 Produto 2: Caneta esferográfica azul

1. Clique em [Novo].
2. Preencha:
   - Código: 7890647097098
   - EAN: 7890647097098
   - Descrição: Caneta esferográfica azul
   - NCM: 96081000
   - CEST: 1902700
   - CFOP: 5102
   - Unidade: UN
   - Valor Unitário: 1.49
   - Origem: 0
   - CST ICMS: 00
   - Modalidade de BC: 3
   - Alíquota ICMS: 18.50
   - CST PIS: 01
   - Alíquota PIS: 1.65
   - CST COFINS: 01
   - Alíquota COFINS: 7.60
   - CST RTC: 000
   - Classificação RTC: 000001
   - IBS UF: 0.10
   - IBS Município: 0.00
   - CBS: 0.90
3. Salve.
4. Valide a persistência.

- [ ] Funcionou
- [ ] Não funcionou

### 7.3 Produto 3: Banqueta plástica dobrável (CST 20 com redução)

1. Clique em [Novo].
2. Preencha:
   - Código: 2
   - EAN: 7893946378271
   - Descrição: Banqueta plástica dobrável, branca, altura 220 mm
   - NCM: 94018000
   - CEST: 0108500
   - CFOP: 5102
   - Unidade: UN
   - Valor Unitário: 56.08
   - Origem: 0
   - CST ICMS: 20
   - Modalidade BC: 3
   - Percentual de Redução da Base: 41.1760
   - Alíquota ICMS: 18.00
   - CST PIS: 01
   - Alíquota PIS: 1.65
   - CST COFINS: 01
   - Alíquota COFINS: 7.60
3. Salve.
4. Reabra e valide.

- [ ] Funcionou
- [ ] Não funcionou

### 7.4 Produto 4: Jogo de cinta para amarração de carga (CST 20 com redução)

1. Clique em [Novo].
2. Preencha:
   - Código: 3
   - EAN: 7893946169442
   - Descrição: Jogo de cinta com catraca para amarração de carga 0,8 tf/0,4 tf, CC 080
   - NCM: 56079090
   - CEST: 2804000
   - CFOP: 5102
   - Unidade: UN
   - Valor Unitário: 37.82
   - Origem: 0
   - CST ICMS: 20
   - Modalidade BC: 3
   - Percentual de Redução da Base: 41.1760
   - Alíquota ICMS: 18.00
   - CST PIS: 01
   - Alíquota PIS: 1.65
   - CST COFINS: 01
   - Alíquota COFINS: 7.60
3. Salve.
4. Reabra e valide.

- [ ] Funcionou
- [ ] Não funcionou

---

## 8. TESTE DE VENDA 1: SIMPLES NACIONAL / CST PADRÃO

1. Acesse a opção [Vendas] ou a tela principal de venda.
2. Selecione o cliente: Andréia Rita da Silva (CPF 40325635862).
3. Adicione os produtos:
   - 7898480650104, quantidade 1
   - 7890647097098, quantidade 2
4. Valide o valor total antes do pagamento.
5. Clique em [Finalizar Venda e Emitir NF-e].
6. Verifique se a NF-e foi emitida em homologação.
7. Confirme que a resposta do servidor foi aceita e que a chave da NF-e foi retornada.

Valores esperados no cálculo:
- Produto 7898480650104: ICMS = 13,00 × 7% = 0,91
- Produto 7890647097098: ICMS = 1,49 × 18,50% = 0,28 (aprox.)
- PIS/COFINS calculados sobre o valor do item conforme alíquota informada

- [ ] Funcionou
- [ ] Não funcionou

---

## 9. TESTE DE VENDA 2: TRIBUTAÇÃO NORMAL COM REDUÇÃO DE BASE (CST 20)

1. Crie uma nova venda.
2. Selecione o mesmo cliente.
3. Adicione:
   - Banqueta plástica dobrável, quantidade 1
   - Jogo de cinta com catraca, quantidade 1
4. Valide o total antes da emissão.
5. Confirme que o sistema calcula a base reduzida corretamente:
   - Base reduzida = valor × (1 - 41.1760 / 100)
   - ICMS = base reduzida × 18%
6. Conclua a venda e confirme a emissão em homologação.

- [ ] Funcionou
- [ ] Não funcionou

---

## 10. TESTE DE VENDA 3: DESCONTO E MULTIPLOS ITENS

1. Crie nova venda.
2. Selecione o cliente Andréia Rita da Silva.
3. Adicione produtos:
   - 2
   - 3
   - 7898480650104
4. Aplique desconto de 3% ou 10% conforme o fluxo do sistema.
5. Confirme que o valor total final é calculado corretamente.
6. Finalize a venda.
7. Confirme que a NF-e foi emitida corretamente em homologação.

- [ ] Funcionou
- [ ] Não funcionou

---

## 11. TESTE DE CRT / REGIME TRIBUTÁRIO (CENÁRIO AURORA POS)

Este cenário tem como objetivo reproduzir o que o roteiro do Aurora POS valida: a mesma operação deve ser testada com regime e alíquotas diferentes.

### 11.1 Cenário A - Simples Nacional

1. Acesse a configuração da empresa.
2. Ajuste o CRT para regime de Simples Nacional (valor 3, conforme documento base do Aurora).
3. Em seguida, use produtos com CST ICMS 00 e alíquotas simples.
4. Emita uma NF-e e confirme a regra fiscal aplicada.
5. Revise a chave emitida e o total gravado na venda.

- [ ] Funcionou
- [ ] Não funcionou

### 11.2 Cenário B - Tributação Normal

1. Ajuste a empresa para CRT de tributação normal, caso o sistema permita mudança.
2. Use produtos com CST 20 ou CST 60 conforme a necessidade do teste.
3. Recalcule a base, a alíquota e a tributação final.
4. Emita uma nova NF-e.
5. Compare o resultado com os cálculos esperados de ICMS e PIS/COFINS.

- [ ] Funcionou
- [ ] Não funcionou

### 11.3 Cenário C - Mistura de regimes em mesma operação

1. Crie uma venda com produtos de diferentes CSTs.
2. Misture no mesmo documento:
   - produto tributado integralmente;
   - produto com redução de base;
   - produto de isenção ou sem tributação.
3. Finalize a venda.
4. Verifique se o sistema mantém a tributação por item e gerencia o total geral corretamente.

- [ ] Funcionou
- [ ] Não funcionou

---

## 12. VALIDAÇÃO DO JSON DE ENVIO / NF-E

1. Verifique o JSON emitido pelo sistema na camada web.
2. Confirme que está sendo enviado:
   - dados da empresa;
   - dados do cliente;
   - itens com quantidade, valor unitário e valor total;
   - ICMS, PIS, COFINS e RTC/IBS-CBS;
   - valor total da nota;
   - dados de pagamento e transportes.
3. Compare os valores calculados com o guia de tributação.
4. Valide se o campo de CST, percentual, base, modo de cálculo e valores finais estão corretos.
5. Anote divergências entre o cálculo esperado e o JSON gerado.

- [ ] Funcionou
- [ ] Não funcionou

---

## 13. REIMPRESSÃO E CONSULTA

1. Acesse a opção de consulta de vendas emitidas.
2. Selecione uma NF-e emitida.
3. Verifique se a chave da NF-e e o protocolo foram gravados corretamente.
4. Use a opção de reimpressão e confirme que o DANFE foi exibido/impresso.
5. Valide que os dados da nota e os valores tributários permanecem consistentes.

- [ ] Funcionou
- [ ] Não funcionou

---

## 14. CHECKLIST FINAL DE HOMOLOGAÇÃO

- [ ] Login foi validado
- [ ] Empresa salva corretamente
- [ ] Configuração do ambiente salva corretamente
- [ ] Cliente cadastrado corretamente
- [ ] Produtos cadastrados corretamente
- [ ] Simples Nacional validado em CFOP/CST/aliquota
- [ ] Tributação normal validada com CST 20 e reduções
- [ ] Misto de regimes validado no mesmo documento
- [ ] JSON/estrutura da NF-e validada
- [ ] NF-e enviada em ambiente de homologação
- [ ] XML/JSON compatível com os campos fiscais esperados
- [ ] Reimpressão funcionando
- [ ] Dados finais consistentes com o guia de tributação

---

## 15. OBSERVAÇÕES FINAIS

- O Aurora POS valida a mudança de CRT e o comportamento em regimes distintos. O nfe-emissor-web deve seguir a mesma lógica de homologação, cobrindo ao menos Simples Nacional e tributação normal em cenários separados e em operação mista.
- O sistema web deve calcular os valores tributários a partir de percentual, base e modalidade de cálculo, e não exigir que o usuário informe valores derivados diretamente;
- O emissor final deve receber o JSON pronto para serialização da NF-e;
- Caso algum campo fiscal não seja coberto, deve ser registrado no relatório de homologação para correção posterior.

---

## 16. ASSINATURA

*Responsável pela homologação:* ________________________________

*Data:* ________________________________

---

## 9. TESTE DE VENDA 2: ICMS COM REDUÇÃO DE BASE

1. Crie uma nova venda.
2. Selecione o mesmo cliente.
3. Adicione:
   - Banqueta plástica dobrável, quantidade 1
   - Jogo de cinta com catraca, quantidade 1
4. Valide o total antes do pagamento.
5. Confirme que o sistema calcula a base reduzida corretamente:
   - Base reduzida = valor × (1 - 41.1760 / 100)
   - ICMS = base reduzida × 18%
6. Conclua a venda e confirme a emissão em homologação.

- [ ] Funcionou
- [ ] Não funcionou

---

## 10. TESTE DE VENDA 3: DESCONTO E MULTIPLOS ITENS

1. Crie nova venda.
2. Selecione o cliente Andréia Rita da Silva.
3. Adicione produtos:
   - 2
   - 3
   - 7898480650104
4. Aplique desconto de 3% ou 10% conforme o fluxo do sistema.
5. Confirme que o valor total final é calculado corretamente.
6. Finalize a venda.
7. Confirme que a NF-e foi emitida corretamente em homologação.

- [ ] Funcionou
- [ ] Não funcionou

---

## 11. VALIDAÇÃO DO JSON DE ENVIO / NF-E

1. Verifique o JSON emitido pelo sistema na camada web.
2. Confirme que está sendo enviado:
   - dados da empresa;
   - dados do cliente;
   - itens com quantidade, valor unitário e valor total;
   - ICMS, PIS, COFINS e RTC/IBS-CBS;
   - valor total da nota;
   - dados de pagamento e transportes.
3. Compare os valores calculados com o guia de tributação.
4. Valide se o campo de CST, percentual, base, modo de cálculo e valores finais estão corretos.

- [ ] Funcionou
- [ ] Não funcionou

---

## 12. REIMPRESSÃO E CONSULTA

1. Acesse a opção de consulta de vendas emitidas.
2. Selecione uma NF-e emitida.
3. Verifique se a chave da NF-e e o protocolo foram gravados corretamente.
4. Use a opção de reimpressão e confirme que o DANFE foi exibido/impresso.
5. Valide que os dados da nota e os valores tributários permanecem consistentes.

- [ ] Funcionou
- [ ] Não funcionou

---

## 13. CHECKLIST FINAL DE HOMOLOGAÇÃO

- [ ] Login foi validado
- [ ] Empresa salva corretamente
- [ ] Configuração do ambiente salva corretamente
- [ ] Cliente cadastrado corretamente
- [ ] Produtos cadastrados corretamente
- [ ] ICMS calculado corretamente
- [ ] PIS/COFINS calculados corretamente
- [ ] Redução de base funcionando em CST 20
- [ ] NF-e enviada em ambiente de homologação
- [ ] XML/JSON compatível com os campos fiscais esperados
- [ ] Reimpressão funcionando
- [ ] Dados finais consistentes com o guia de tributação

---

## 14. OBSERVAÇÕES FINAIS

- O sistema web deve calcular os valores tributários a partir de percentual, base e modalidade de cálculo, e não exigir que o usuário informe valores derivados diretamente;
- O emissor final deve receber o JSON pronto para serialização da NF-e;
- Caso algum campo fiscal não seja coberto, deve ser registrado no relatório de homologação para correção posterior.

---

## 15. ASSINATURA

*Responsável pela homologação:* ________________________________

*Data:* ________________________________
