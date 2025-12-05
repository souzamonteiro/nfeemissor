# Guia Completo para Cálculo de Tributos na NF-e por CST (Com Legislação Aplicável)

## 📋 **Estrutura do Guia**

### **1. Base Legal Principal**
- Lei Complementar 87/1996 (Lei Kandir) - ICMS
- Lei 10.833/2003 - PIS e COFINS
- Lei 9.430/1996 - IRPJ e CSLL
- Convênios ICMS de cada estado
- Protocolos ICMS (ex: Protocolo 21/2011 - Diferencial de Alíquota)

### **2. Tabela de CSTs do ICMS com Cálculo**

#### **Grupo 00 - Tributada Integralmente**
- **CST 00**: Tributada integralmente
  - **Cálculo**: Base = Valor da operação × Alíquota interestadual
  - **Legislação**: Artigo 13 do RICMS/SP (modelo)

#### **Grupo 10 - Tributada com cobrança do ICMS por ST**
- **CST 10**: Tributada e com cobrança do ICMS por substituição tributária
  - **Cálculo**: 
    ```
    ICMS Próprio = Base × Alíquota interestadual
    ICMS ST = (MVA × Base) × Alíquota interna - ICMS Próprio
    ```
  - **Legislação**: Artigo 313 do RICMS/SP

#### **Grupo 20 - Com redução de base de cálculo**
- **CST 20**: Com redução de base de cálculo
  - **Cálculo**: Base reduzida = Valor × (1 - % redução)
  - **Legislação**: Convênios específicos por produto

#### **Grupo 30 - Isenta ou não tributada**
- **CST 30**: Isenta
- **CST 40**: Isenta
- **CST 41**: Não tributada
- **CST 50**: Suspensão
  - **Legislação**: Lei Kandir para exportação

#### **Grupo 60 - ICMS cobrado anteriormente por ST**
- **CST 60**: ICMS cobrado anteriormente por substituição tributária
  - **Cálculo**: Apenas informativo

### **3. Regimes Especiais**

#### **A) SUFRAMA (Área de Livre Comércio)**
- **Benefício**: Redução de 55% a 100% do ICMS
- **Cálculo**: 
  ```
  ICMS SUFRAMA = (Base × Alíquota) × (1 - % benefício)
  ```
- **Legislação**: Lei 8.387/1991, Decreto 7.212/2010
- **CSTs aplicáveis**: 00, 10, 20, 30, 40, 41, 50
- **Documentação**: Deve constar inscrição SUFRAMA do destinatário

#### **B) Desoneração da Folha de Pagamento**
- **Setores**: 42 setores (Lei 12.546/2011)
- **Alíquotas**:
  - PIS: 0,75% para venda; 1,65% para aquisição
  - COFINS: 3,65% para venda; 7,6% para aquisição
- **Cálculo**: Base = Receita Bruta
- **Legislação**: Lei 12.546/2011, Lei 12.741/2012

#### **C) Diferencial de Alíquota (DIFAL)**
- **Cálculo**:
  ```
  DIFAL = (Base × Alíquota interna destino) - (Base × Alíquota interestadual)
  ```
- **Partilha** (até 2023):
  - 40% para estado destino (remetente retém)
  - 60% para estado destino (destinatário recolhe)
- **Legislação**: Protocolo ICMS 21/2011, Convênio ICMS 93/2015

### **4. Cálculo de PIS e COFINS**

#### **Regime Cumulativo (CST 01, 02)**
- PIS: 0,65%
- COFINS: 3%
- **Base**: Receita Bruta

#### **Regime Não-Cumulativo (CST 03, 04, 05, 06, 07, 08, 09)**
- PIS: 1,65%
- COFINS: 7,6%
- **Cálculo**: Créditos = Valor de aquisição × Alíquota
- **Legislação**: Lei 10.833/2003

### **5. Tabela Resumo de CSTs mais Comuns**

| CST | Situação Tributária | ICMS | PIS/COFINS | Cálculo Principal |
|-----|-------------------|------|------------|-------------------|
| 00 | Tributada integral | Sim | Sim | Base × Alíquota |
| 10 | Tributada c/ ST | Sim | Sim | Base × Alíquota + ST |
| 20 | Com redução BC | Sim | Sim | Base reduzida × Alíquota |
| 30 | Isenta | Não | Sim* | (*ver produtos) |
| 40 | Isenta | Não | Não | - |
| 41 | Não tributada | Não | Não | - |
| 50 | Suspensão | Diferido | Sim* | Recolhimento futuro |
| 60 | ICMS ant. por ST | Retido anterior | Sim | Apenas informativo |

### **6. Fluxo Prático para Emissão de NF-e**

1. **Identificar operação** (interestadual, interna, com ST)
2. **Verificar CST do produto** na tabela NCM
3. **Calcular base do ICMS**:
   - Considerar reduções aplicáveis
   - Incluir IPI (se não for destinado a ativo fixo)
4. **Aplicar alíquota**:
   - Interestadual: Tabela do Senado
   - Interna: Tabela do estado
5. **Calcular ST quando aplicável**:
   - Verificar MVA oficial ou acordado
   - Aplicar fórmula do item 2
6. **Calcular PIS/COFINS**:
   - Definir regime (cumulativo/não cumulativo)
   - Aplicar alíquotas sobre base
7. **Considerar benefícios**:
   - SUFRAMA
   - Desoneração
   - Incentivos fiscais
8. **Incluir DIFAL** para operações interestaduais

### **7. Obrigações Acessórias Relacionadas**

- **EFD ICMS/IPI**: Registro de CSTs e cálculos
- **EFD Contribuições**: Apuração de PIS/COFINS
- **SPED Fiscal**: Documento fiscal eletrônico
- **Declarações estaduais**: GIA, SINTEGRA

### **8. Cuidados Especiais**

- **ICMS-ST**: Verificar lista de produtos sujeitos em cada estado
- **SUFRAMA**: Validar inscrição ativa do destinatário
- **Desoneração**: Confirmar enquadramento do CNAE
- **DIFAL**: Atualizar partilha conforme ano (prazos alterados)
- **NCM**: Classificação correta é fundamental

### **9. Fontes Oficiais para Consulta**

1. **CONFAZ** (Conselho Nacional de Política Fazendária)
2. **Senado Federal** (Tabela de alíquotas interestaduais)
3. **Sefaz de cada estado**
4. **Receita Federal** (PIS/COFINS)
5. **Legislação SUFRAMA**

### **⚠️ Aviso Importante**

Este guia é um **resumo técnico-educacional**. A legislação tributária brasileira muda frequentemente e possui interpretações divergentes entre estados. Para:
- **Operações interestaduais**: Consultar convênios ICMS
- **ST específica**: Verificar protocolos estaduais
- **Benefícios fiscais**: Confirmar vigência dos incentivos
- **Cálculos complexos**: Utilizar software especializado

**Recomendo sempre**:
1. Manter um sistema de gestão tributária atualizado
2. Contar com assessoria contábil especializada
3. Verificar as atualizações mensais da legislação
4. Realizar treinamentos periódicos para a equipe fiscal

# 🔍 **Elementos Adicionais Importantes para o Cálculo de Tributos na NF-e**

## **1. Regimes Tributários Específicos Não Mencionados**

### **A) SIMPLES NACIONAL**
- **Cálculo**: DAS unificado (ICMS, PIS, COFINS, IRPJ, CSLL, etc.)
- **CSTs específicos**: 
  - **101** - Tributada pelo Simples Nacional com permissão de crédito
  - **102** - Tributada pelo Simples Nacional sem permissão de crédito
  - **103** - Isenção do ICMS no Simples Nacional
  - **300** - Imune
  - **500** - ICMS cobrado anteriormente por ST ou por antecipação
- **Anexos**: I (comércio), II (indústria), III (serviços), etc.
- **Legislação**: Lei Complementar 123/2006, Resolução CGSN

### **B) REGIME ESPECIAL DA INDÚSTRIA QUÍMICA**
- **Repetro-Sped**: Suspensão de tributos para petroquímica
- **Cálculo específico**: Alíquotas diferenciadas
- **Legislação**: Lei 13.586/2017

### **C) ZONA FRANCA DE MANAUS**
- **Benefícios além da SUFRAMA**:
  - Redução de IPI
  - Isenção de impostos de importação
  - PIS/COFINS diferenciados
- **Legislação**: Lei 8.387/1991, Decreto-Lei 288/1967

## **2. Situações Específicas de IPI**

### **A) Cálculo por "Alíquota por Unidade"**
- **Exemplo**: Cigarros, bebidas, veículos
- **Fórmula**: IPI = (Quantidade × Valor por unidade) + (Valor total × %)
- **CSTs do IPI**:
  - **00** - Entrada com recuperação de crédito
  - **49** - Outras entradas
  - **50** - Saída tributada
  - **99** - Outras saídas

### **B) IPI na Base do ICMS**
- **Regra geral**: IPI não compõe base do ICMS
- **Exceção**: Quando produto destinado a ativo fixo ou uso/consumo
- **Impacto no cálculo**: Base ICMS = Valor mercadoria + IPI

## **3. CSTs de PIS/COFINS que Não Mencionei**

### **Tabela Completa de CSTs PIS/COFINS:**

| CST | Descrição | Crédito? |
|-----|-----------|----------|
| 01 | Operação Tributável (BC = Valor da Operação) | Sim |
| 02 | Operação Tributável (BC = Valor da Operação + diferença) | Sim |
| 03 | Operação Tributável (BC = Quantidade × Preço Unitário) | Sim |
| 04 | Operação Tributável (Tributação Monofásica) | Não |
| 05 | Operação Tributável (ST) | Não |
| 06 | Operação Tributável (Alíquota Zero) | Sim |
| 07 | Operação Isenta da Contribuição | Não |
| 08 | Operação sem Incidência da Contribuição | Não |
| 09 | Operação com Suspensão da Contribuição | Não |

## **4. Aspectos Práticos Críticos**

### **A) ARREDONDAMENTOS**
- **Regra oficial**: 3 casas decimais nos cálculos intermediários
- **Final**: 2 casas decimais para recolhimento
- **Problema comum**: Diferenças de centavos entre sistemas

### **B) FRETE E SEGURO**
- **ICMS**: Compõem a base de cálculo
- **PIS/COFINS**: Só entram na base se incluídos no preço
- **DIFAL**: Considera frete na base de cálculo

### **C) DESCONTOS**
- **Desconto incondicional**: Reduz base de cálculo
- **Desconto condicional**: Não reduz base até realização
- **Desconto comercial vs financeiro**: Tratamentos diferentes

## **5. Regimes de Compensação e Dívida Ativa**

### **A) COMPENSAÇÃO TRIBUTÁRIA**
- **Limites**: Até 70% do débito pode ser compensado
- **Prazo**: 5 anos para requerimento
- **Documentação**: PER/DCOMP obrigatório

### **B) PARCELAMENTOS ESPECIAIS**
- **REFIS**: Programas de recuperação fiscal
- **PERT**: Parcelamento estadual
- **Impacto**: Juros e correção diferenciados

## **6. Novidades Legislativas (2023-2024)**

### **A) EMENDA 132/2023**
- Mudanças no DIFAL após 2023
- Novas regras de partilha
- **Status**: Aguardando regulamentação

### **B) PIX COMO MEIO DE PAGAMENTO**
- **Impacto fiscal**: Registro no campo de pagamento da NF-e
- **Prazo de recebimento**: Afeta regime de caixa vs competência

### **C) NOTA FISCAL ELETRÔNICA 4.0**
- Novos eventos: 2250 (retenções), 2260 (ajustes)
- Campos adicionais para tributos
- **Obrigatoriedade**: Fase de implementação

## **7. Contabilidade Tributária Avançada**

### **A) TEMPORALIDADE DOS CRÉDITOS**
- **ICMS**: Próprio (imediato) vs ST (diferido)
- **PIS/COFINS**: Créditos presumidos vs realizados
- **Livros fiscais**: EFD vs SPED vs Livro de Apuração

### **B) CONTROLADORIAS FISCAIS**
- **Conciliação bancária**: Débitos vs recolhimentos
- **Auditoria fiscal**: Pontos de atenção comuns
- **Contingenciamento**: Estimativas vs realizados

## **8. Casos Específicos por Segmento**

### **A) FARMACÊUTICO**
- **PIS/COFINS zero** para medicamentos (Lei 13.097/2015)
- **ICMS reduzido** em muitos estados
- **ST específica** para medicamentos

### **B) AUTOMOTIVO**
- **IPVA como crédito** para frotistas
- **ST antecipada** para peças
- **Regime especial** para montadoras

### **C) AGROINDÚSTRIA**
- **Funrural** como substituição tributária
- **Isenções** para insumos agrícolas
- **Crédito outorgado** específico

## **9. Ferramentas Práticas que Esqueci de Mencionar**

### **A) CONSULTAS PÚBLICAS**
- **Consulta NCM**: Receita Federal
- **Tabelas de ST**: Sefaz de cada estado
- **Benefícios fiscais**: Ministério da Economia

### **B) CALCULADORAS OFICIAIS**
- **Calculadora DIFAL**: Confaz
- **Simulador Simples Nacional**: Receita Federal
- **Tabelas de MVA**: Estados individualmente

## **10. Erros Comuns que Causam Autuações**

1. **CST x CSOSN**: Usar errado entre regime normal e Simples
2. **CFOP inadequado**: Para operação interestadual vs interna
3. **MVA aplicada errada**: Por não atualizar tabelas mensais
4. **DIFAL mal calculado**: Esquecer partilha estadual
5. **Créditos indevidos**: Em operações não geradoras

## **⚠️ O MAIS IMPORTANTE: O que Nunca Pode Faltar**

| Elemento | Por que é Crítico |
|----------|-------------------|
| **Versão da Lei** no sistema | Determina cálculos corretos |
| **Atualização mensal** de tabelas | MVA, alíquotas, benefícios |
| **Backup de cálculos** | Para defesa em auditoria |
| **Mapa de riscos fiscais** | Por produto, estado, operação |
| **Treinamento contínuo** | Leis mudam todo mês |

## **📚 Recursos que Complementam o Guia**

1. **Manual de Orientação do SPED Fiscal** (EFA)
2. **Compêndio de CSTs da Receita Federal**
3. **Guias estaduais de ST** (cada Sefaz tem o seu)
4. **Jurisprudência do CARF** (decisões que afetam cálculos)
5. **Sistemas de monitoramento legislativo**

---

**Conclusão**: O universo tributário brasileiro é como um iceberg - 20% visível (CSTs básicos) e 80% submerso (regimes especiais, compensações, peculiaridades estaduais, jurisprudência).

# 📊 **Exemplos Práticos Completos de Cálculo Tributário na NF-e**

## **EXEMPLO 1: CST 00 - Tributação Integral (Venda Interna)**

### **Condições:**
- Empresa: Comércio de eletrônicos em São Paulo
- Produto: Smartphone (NCM 8517.12.00)
- Valor unitário: R$ 1.500,00
- Quantidade: 10 unidades
- Cliente: Consumidor final no mesmo estado
- Alíquota ICMS SP: 18%
- Regime PIS/COFINS: Não-cumulativo

### **Cálculos:**

**1. Valor total da operação:**
```
R$ 1.500,00 × 10 = R$ 15.000,00
```

**2. Base de cálculo do ICMS:**
```
R$ 15.000,00 (IPI não compõe base em venda para consumidor final)
```

**3. ICMS próprio:**
```
R$ 15.000,00 × 18% = R$ 2.700,00
```

**4. PIS (não-cumulativo):**
```
Base PIS = R$ 15.000,00
PIS = R$ 15.000,00 × 1,65% = R$ 247,50
```

**5. COFINS (não-cumulativo):**
```
COFINS = R$ 15.000,00 × 7,6% = R$ 1.140,00
```

**6. Valor total da NF-e:**
```
R$ 15.000,00 + R$ 2.700,00 = R$ 17.700,00
```

**Campos da NF-e:**
```
CST ICMS: 00
CFOP: 5.101
CST PIS: 01
CST COFINS: 01
vBC: 15000.00
vICMS: 2700.00
vPIS: 247.50
vCOFINS: 1140.00
```

---

## **EXEMPLO 2: CST 10 - Com Substituição Tributária (ST)**

### **Condições:**
- Remetente: Indústria de bebidas em MG
- Destinatário: Distribuidor em SP
- Produto: Cerveja (NCM 2203.00.00)
- Valor unitário: R$ 4,00
- Quantidade: 10.000 unidades
- Alíquota interestadual: 12%
- Alíquota interna SP: 18%
- MVA ajustada: 40%
- IPI: 10% (compõe base ICMS)

### **Cálculos:**

**1. Valor da operação:**
```
R$ 4,00 × 10.000 = R$ 40.000,00
```

**2. IPI:**
```
R$ 40.000,00 × 10% = R$ 4.000,00
```

**3. Base do ICMS próprio:**
```
R$ 40.000,00 + R$ 4.000,00 = R$ 44.000,00
```

**4. ICMS próprio:**
```
R$ 44.000,00 × 12% = R$ 5.280,00
```

**5. Base para ST:**
```
Base ST = (Valor operação + IPI) × (1 + MVA%)
Base ST = R$ 44.000,00 × 1,40 = R$ 61.600,00
```

**6. ICMS ST:**
```
ICMS ST = (Base ST × Alíquota interna) - ICMS próprio
ICMS ST = (R$ 61.600,00 × 18%) - R$ 5.280,00
ICMS ST = R$ 11.088,00 - R$ 5.280,00 = R$ 5.808,00
```

**7. Valor total da NF-e:**
```
R$ 40.000,00 + R$ 4.000,00 + R$ 5.808,00 = R$ 49.808,00
```

**Campos da NF-e:**
```
CST ICMS: 10
CFOP: 6.102
pICMS: 12.00
vBC: 44000.00
vICMS: 5280.00
vBCST: 61600.00
vICMSST: 5808.00
vIPI: 4000.00
modBCST: 4 (MVA ajustado)
```

---

## **EXEMPLO 3: CST 20 - Com Redução de Base de Cálculo**

### **Condições:**
- Produto: Cesta básica (arroz - NCM 1006.30.00)
- Valor: R$ 10.000,00
- Operação interna em SP
- Redução de base: 60% (Convênio ICMS)
- Alíquota normal: 18%
- Alíquota com redução: 7%
- IPI: 0%

### **Cálculos:**

**1. Base de cálculo com redução:**
```
Redução = R$ 10.000,00 × 60% = R$ 6.000,00
Base reduzida = R$ 10.000,00 - R$ 6.000,00 = R$ 4.000,00
```

**2. ICMS:**
```
R$ 4.000,00 × 7% = R$ 280,00
```

**3. PIS/COFINS (isenção para cesta básica):**
```
PIS: 0,00
COFINS: 0,00
```

**Campos da NF-e:**
```
CST ICMS: 20
CFOP: 5.101
motDesICMS: 3 (outros)
pRedBC: 60.00
vBC: 4000.00
pICMS: 7.00
vICMS: 280.00
CST PIS: 08 (sem incidência)
CST COFINS: 08 (sem incidência)
```

---

## **EXEMPLO 4: CST 30 + SUFRAMA**

### **Condições:**
- Remetente: Indústria em Manaus (SUFRAMA)
- Destinatário: Comércio em SP (com inscrição SUFRAMA)
- Produto: Placa eletrônica (NCM 8534.00.00)
- Valor: R$ 50.000,00
- Benefício SUFRAMA: 55% de redução no ICMS
- Alíquota interestadual normal: 12%
- IPI: 5%

### **Cálculos:**

**1. IPI:**
```
R$ 50.000,00 × 5% = R$ 2.500,00
```

**2. Base do ICMS:**
```
R$ 50.000,00 + R$ 2.500,00 = R$ 52.500,00
```

**3. ICMS sem benefício:**
```
R$ 52.500,00 × 12% = R$ 6.300,00
```

**4. Redução SUFRAMA:**
```
Redução = R$ 6.300,00 × 55% = R$ 3.465,00
```

**5. ICMS com SUFRAMA:**
```
R$ 6.300,00 - R$ 3.465,00 = R$ 2.835,00
```

**6. Valor total:**
```
R$ 50.000,00 + R$ 2.500,00 + R$ 2.835,00 = R$ 55.335,00
```

**Campos da NF-e:**
```
CST ICMS: 30 (isento por incentivo)
CFOP: 6.101
infAdic: "Operação com benefício SUFRAMA - Redução de 55%"
vICMSDeson: 2835.00
motDesICMS: 9 (outros)
vIPI: 2500.00
```

---

## **EXEMPLO 5: DIFAL - Operação Interestadual**

### **Condições:**
- Remetente: Indústria em MG
- Destinatário: Consumidor final em RJ (não contribuinte)
- Produto: Móvel (NCM 9403.60.00)
- Valor: R$ 8.000,00
- Alíquota interestadual: 12%
- Alíquota interna RJ: 19%
- IPI: 5%

### **Cálculos:**

**1. IPI:**
```
R$ 8.000,00 × 5% = R$ 400,00
```

**2. Base do ICMS:**
```
R$ 8.000,00 + R$ 400,00 = R$ 8.400,00
```

**3. ICMS interestadual (remetente):**
```
R$ 8.400,00 × 12% = R$ 1.008,00
```

**4. Cálculo do DIFAL:**
```
ICMS se fosse interno = R$ 8.400,00 × 19% = R$ 1.596,00
DIFAL = R$ 1.596,00 - R$ 1.008,00 = R$ 588,00
```

**5. Partilha do DIFAL (2023):**
```
Remetente retém: R$ 588,00 × 40% = R$ 235,20
Destinatário paga: R$ 588,00 × 60% = R$ 352,80
```

**6. Valor da NF-e:**
```
R$ 8.000,00 + R$ 400,00 + R$ 235,20 = R$ 8.635,20
(O destinatário paga + R$ 352,80 diretamente)
```

**Campos da NF-e:**
```
CST ICMS: 00
CFOP: 6.102
pICMS: 12.00
vBC: 8400.00
vICMS: 1008.00
vICMSUFDest: 588.00
vICMSUFRemet: 235.20
vFCPUFDest: 0.00
vIPI: 400.00
```

---

## **EXEMPLO 6: SIMPLES NACIONAL - CST 101**

### **Condições:**
- Empresa: Comércio de roupas (Simples Nacional - Anexo I)
- Faturamento últimos 12 meses: R$ 800.000,00
- Produto: Camisetas - R$ 2.000,00
- Alíquota efetiva do Simples: 6,33%
- Estado: São Paulo

### **Cálculos:**

**1. Cálculo do DAS (unificado):**
```
Receita Bruta do mês (exemplo): R$ 50.000,00
Alíquota efetiva para R$ 800k: 6,33%
DAS = R$ 50.000,00 × 6,33% = R$ 3.165,00
```

**2. Na NF-e específica de R$ 2.000,00:**
```
ICMS incluso no DAS: R$ 2.000,00 × 2,75%* = R$ 55,00
PIS/COFINS incluso: R$ 2.000,00 × 3,58%* = R$ 71,60
*Percentuais aproximados - variam por faturamento
```

**3. Valor da NF-e:**
```
Apenas R$ 2.000,00 (tributos recolhidos via DAS)
```

**Campos da NF-e:**
```
CST ICMS: 101 (Simples Nacional com crédito)
CSOSN: 101 (equivalente)
CFOP: 5.101
vBC: 0.00
vICMS: 0.00
vProd: 2000.00
infAdic: "Tributação pelo Simples Nacional - DAS unificado"
```

---

## **EXEMPLO 7: Desoneração da Folha + CST 00**

### **Condições:**
- Empresa: Indústria têxtil (enquadrada na desoneração)
- Produto: Tecido - R$ 25.000,00
- ICMS: 18%
- PIS/COFINS desoneradas:
  - PIS: 0,75% (em vez de 1,65%)
  - COFINS: 3,65% (em vez de 7,6%)

### **Cálculos:**

**1. ICMS:**
```
R$ 25.000,00 × 18% = R$ 4.500,00
```

**2. PIS desonerado:**
```
R$ 25.000,00 × 0,75% = R$ 187,50
```

**3. COFINS desonerada:**
```
R$ 25.000,00 × 3,65% = R$ 912,50
```

**4. Valor total:**
```
R$ 25.000,00 + R$ 4.500,00 = R$ 29.500,00
(PIS/COFINS não somam ao valor, são tributos por dentro)
```

**Campos da NF-e:**
```
CST ICMS: 00
CST PIS: 01 (mas com alíquota reduzida)
CST COFINS: 01 (mas com alíquota reduzida)
vBC: 25000.00
pICMS: 18.00
vICMS: 4500.00
qBCProd: 1
vAliqProd: 187.50 (PIS)
vCOFINS: 912.50
```

---

## **EXEMPLO 8: Exportação Direta - CST 70**

### **Condições:**
- Empresa: Exportadora de café em MG
- Produto: Café verde (NCM 0901.11.00)
- Valor: US$ 20.000,00 (cotação: R$ 5,00/US$)
- Operação: Venda direta para EUA
- IPI: 0%
- Drawback: Não

### **Cálculos:**

**1. Conversão para Real:**
```
US$ 20.000,00 × R$ 5,00 = R$ 100.000,00
```

**2. Tributos:**
```
ICMS: ISENTO
PIS: ISENTO
COFINS: ISENTO
IPI: ISENTO
```

**3. Créditos presumidos (se houver):**
```
ICMS: R$ 100.000,00 × 18% = R$ 18.000,00 (crédito a recuperar)
PIS: R$ 100.000,00 × 1,65% = R$ 1.650,00
COFINS: R$ 100.000,00 × 7,6% = R$ 7.600,00
```

**Campos da NF-e:**
```
CST ICMS: 70 (com redução de base e cobrança do ICMS por ST)
CFOP: 3.101
vBC: 0.00
vICMS: 0.00
vBCST: 0.00
vICMSST: 0.00
indExport: 0 (Exportação direta)
xPed: Número do pedido
nFE: Número da fatura de exportação
```

---

## **EXEMPLO 9: CST 50 - Suspensão com Débito Diferido**

### **Condições:**
- Operação: Industrialização por encomenda
- Matéria-prima: R$ 15.000,00
- ICMS normal: 18%
- Suspensão conforme Convênio ICMS
- Débito diferido para quando produto for comercializado

### **Cálculos:**

**1. Na entrada da matéria-prima:**
```
ICMS suspenso: R$ 15.000,00 × 18% = R$ 2.700,00 (não pago agora)
```

**2. Registro contábil:**
```
Débito diferido: R$ 2.700,00 (contas de controle)
```

**3. Quando produto final for vendido:**
```
ICMS a recolher: R$ 2.700,00
```

**Campos da NF-e de entrada:**
```
CST ICMS: 50 (suspensão)
CFOP: 1.101
vBC: 15000.00
pICMS: 18.00
vICMS: 0.00 (suspenso)
vICMSDeson: 2700.00
motDesICMS: 5 (Suspensão)
```

---

## **EXEMPLO 10: CST 41 - Não Tributada + Retenções**

### **Condições:**
- Operação: Venda para órgão público federal
- Produto: Material de escritório - R$ 8.000,00
- Retenções obrigatórias:
  - IRRF: 1,5%
  - PIS: 0,65%
  - COFINS: 3%
  - CSLL: 1%
  - INSS: 11%

### **Cálculos:**

**1. Valor da operação:**
```
R$ 8.000,00
```

**2. Retenções:**
```
IRRF: R$ 8.000,00 × 1,5% = R$ 120,00
PIS: R$ 8.000,00 × 0,65% = R$ 52,00
COFINS: R$ 8.000,00 × 3% = R$ 240,00
CSLL: R$ 8.000,00 × 1% = R$ 80,00
INSS: R$ 8.000,00 × 11% = R$ 880,00
Total retenções: R$ 1.372,00
```

**3. Valor líquido a receber:**
```
R$ 8.000,00 - R$ 1.372,00 = R$ 6.628,00
```

**Campos da NF-e:**
```
CST ICMS: 41 (Não tributada)
CFOP: 1.101
vRetIR: 120.00
vRetPIS: 52.00
vRetCOFINS: 240.00
vRetCSLL: 80.00
vRetINSS: 880.00
infAdic: "Tributos retidos na fonte conforme legislação"
```

---

## **EXEMPLO 11: Múltiplos Produtos com Diferentes CSTs**

### **Condições:**
- Venda para supermercado
- 3 produtos diferentes:
  1. Arroz (CST 20) - R$ 2.000,00
  2. Cerveja (CST 10) - R$ 1.500,00
  3. Eletrônico (CST 00) - R$ 3.000,00
- Valor total: R$ 6.500,00

### **Cálculos por produto:**

**Produto 1 - Arroz (CST 20):**
```
Base com redução 60%: R$ 2.000,00 × 40% = R$ 800,00
ICMS: R$ 800,00 × 7% = R$ 56,00
```

**Produto 2 - Cerveja (CST 10):**
```
ICMS próprio: R$ 1.500,00 × 18% = R$ 270,00
Base ST: R$ 1.500,00 × 1,40 = R$ 2.100,00
ICMS ST: (R$ 2.100,00 × 30%) - R$ 270,00 = R$ 630,00 - R$ 270,00 = R$ 360,00
Total ICMS: R$ 270,00 + R$ 360,00 = R$ 630,00
```

**Produto 3 - Eletrônico (CST 00):**
```
ICMS: R$ 3.000,00 × 18% = R$ 540,00
```

**Resumo da NF-e:**
```
Produto 1: vProd=2000.00, vICMS=56.00, CST=20
Produto 2: vProd=1500.00, vICMS=270.00, vICMSST=360.00, CST=10
Produto 3: vProd=3000.00, vICMS=540.00, CST=00
TOTAL: vProd=6500.00, vICMS=866.00, vICMSST=360.00
```

---

## **TABELA RESUMO DOS EXEMPLOS:**

| Exemplo | CST | Valor Produto | ICMS | ST | Outros | Total NF-e |
|---------|-----|---------------|------|----|--------|------------|
| 1 | 00 | R$ 15.000,00 | R$ 2.700,00 | - | PIS/COFINS | R$ 17.700,00 |
| 2 | 10 | R$ 40.000,00 | R$ 5.280,00 | R$ 5.808,00 | IPI R$ 4.000,00 | R$ 49.808,00 |
| 3 | 20 | R$ 10.000,00 | R$ 280,00 | - | - | R$ 10.280,00 |
| 4 | 30 | R$ 50.000,00 | R$ 2.835,00 | - | IPI R$ 2.500,00 | R$ 55.335,00 |
| 5 | 00 | R$ 8.000,00 | R$ 1.008,00 | - | DIFAL R$ 588,00 | R$ 8.635,20* |
| 6 | 101 | R$ 2.000,00 | Incluso no DAS | - | - | R$ 2.000,00 |
| 7 | 00 | R$ 25.000,00 | R$ 4.500,00 | - | PIS/COFINS reduzido | R$ 29.500,00 |
| 8 | 70 | R$ 100.000,00 | Isento | - | Isento todos | R$ 100.000,00 |
| 9 | 50 | R$ 15.000,00 | Suspenso | - | - | R$ 15.000,00 |
| 10 | 41 | R$ 8.000,00 | Não tributado | - | Retenções R$ 1.372,00 | R$ 8.000,00 |

*mais R$ 352,80 a pagar pelo destinatário

## **🔑 Pontos Críticos nos Exemplos:**

1. **Base de cálculo** varia conforme CST
2. **IPI compõe base** do ICMS em casos específicos
3. **ST calculada sobre preço final** estimado
4. **DIFAL partilhado** entre estados
5. **Simples Nacional** tem recolhimento unificado
6. **Exportação** gera créditos presumidos
7. **Suspensão** = débito diferido, não isenção
8. **Vendas para governo** têm retenções obrigatórias

## **⚠️ AVISOS IMPORTANTES:**

1. **Estes são exemplos didáticos** - valores reais dependem de:
   - Legislação estadual específica
   - Protocolos vigentes
   - Enquadramento da empresa
   - Características do produto

2. **Sistemas ERP fazem cálculos automaticamente**, mas é fundamental:
   - Configurar corretamente os parâmetros
   - Atualizar tabelas mensalmente
   - Validar cálculos periodicamente

3. **Para casos reais**, sempre:
   - Consulte a legislação atualizada
   - Verifique Convênios ICMS específicos
   - Consulte um contador especializado
