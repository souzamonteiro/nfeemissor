# Documento de Projeto - Sistema Emissor de NF-e

## 1. Visão Geral do Projeto

### 1.1. Objetivo
Sistema web completo para emissão de Nota Fiscal Eletrônica (NF-e) com interface moderna, desenvolvido para operações do Simples Nacional.

### 1.2. Escopo
- Cadastro e gestão de produtos
- Cadastro e gestão de clientes  
- Configuração de empresa e parâmetros do sistema
- Gestão de usuários e perfis de acesso
- Processo completo de vendas com emissão de NF-e
- Geração de DANFE em PDF

### 1.3. Público-Alvo
- Empresas do Simples Nacional
- Comércio varejista
- Pequenas e médias empresas

## 2. Especificações Técnicas

### 2.1. Stack Tecnológica

**Frontend:**
- **JSF 4.0** (Jakarta Server Faces)
- **PrimeFaces 12.0.0** - Componentes UI
- **HTML5 + CSS3** - Estrutura e estilos
- **JavaScript** - Interatividade

**Backend:**
- **Java 11** - Linguagem de programação
- **Jakarta EE 9** - Especificações enterprise
- **CDI (Contexts and Dependency Injection)** - Injeção de dependências
- **Bean Validation** - Validação de dados

**Persistência:**
- **Hibernate 6.2.7** - ORM
- **JPA 3.0** - API de persistência
- **PostgreSQL 12+** - Banco de dados

---

**Infraestrutura:**
- **Apache Tomcat 9** - Servlet container
- **Maven 3.6+** - Gerenciamento de dependências

### 2.2. Arquitetura do Sistema

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Backend        │    │   Persistência  │
│                 │    │                  │    │                 │
│  JSF 4.0        │◄──►│  Controllers     │◄──►│  Hibernate 6    │
│  PrimeFaces 12  │    │  Managed Beans   │    │  JPA 3.0        │
│  XHTML          │    │  CDI             │    │                 │
│  CSS3           │    │  Bean Validation │    │  PostgreSQL     │
└─────────────────┘    └──────────────────┘    └─────────────────┘
         │                        │                        │
         │                        │                        │
         └────────────────────────┼────────────────────────┘
                                  │
                         ┌────────▼────────┐
                         │  Serviço NF-e   │
                         │  (HTTP/REST)    │
                         └─────────────────┘
```

### 2.3. Padrões de Projeto Utilizados

**MVC (Model-View-Controller)**
- **Model**: Entidades JPA (Produto, Cliente, Venda, etc.)
- **View**: Páginas XHTML com PrimeFaces
- **Controller**: Managed Beans com escopo definido

**DAO (Data Access Object)**
- Abstração de acesso a dados
- Operações CRUD padronizadas
- Injeção de EntityManager

**Dependency Injection**
- CDI para injeção de dependências
- Escopos (@ViewScoped, @SessionScoped)
- Qualifiers para resolução

## 3. Especificações Funcionais

### 3.1. Módulo de Cadastros

#### 3.1.1. Produtos
**Campos:**
- Código do produto (cProd)
- Código EAN (cEAN)
- Descrição (xProd)
- NCM/SH (ncm)
- CEST (cest)
- CFOP (cfop)
- Unidade de medida (uCom)
- Valor unitário (vUnCom)
- Configurações de impostos (ICMS, PIS, COFINS)

**Funcionalidades:**
- Inclusão, alteração, exclusão lógica
- Validação de campos obrigatórios
- Verificação de duplicidade de código

#### 3.1.2. Clientes
**Campos:**
- Nome/Razão Social (xNome)
- CPF/CNPJ
- Inscrição Estadual (IE)
- Endereço completo
- Indicador de IE (indIEDest)

**Funcionalidades:**
- Suporte a pessoa física e jurídica
- Validação de CPF/CNPJ
- Endereçamento completo

#### 3.1.3. Empresa
**Características:**
- Registro único
- Dados do emitente da NF-e
- Endereço completo
- Configurações tributárias

#### 3.1.4. Configurações
**Parâmetros:**
- Ambiente (Produção/Homologação)
- UF do webservice e emitente
- Caminhos de certificados e schemas
- Configurações de emissão
- Parâmetros de numeração

### 3.2. Módulo de Segurança

#### 3.2.1. Usuários
**Perfis:**
- **ADMIN**: Acesso completo ao sistema
- **USER**: Acesso apenas a vendas e consultas

**Funcionalidades:**
- Cadastro com login, email e senha
- Validação de email único
- Controle de acesso por perfis
- Ativação/desativação de usuários

#### 3.2.2. Autenticação
- Login com usuário ou email
- Controle de sessão
- Logout seguro
- Redirecionamento automático

### 3.3. Módulo de Vendas

#### 3.3.1. Processo de Venda
1. Seleção do cliente
2. Adição de produtos à venda
3. Cálculo automático de totais
4. Finalização com emissão de NF-e
5. Geração de DANFE

#### 3.3.2. Cálculos Automáticos
- Valor total dos produtos
- Cálculo de ICMS, PIS, COFINS
- Total geral da NF-e

### 3.4. Integração com Serviço NF-e

#### 3.4.1. Comunicação
- **Protocolo**: HTTP/REST
- **Formato**: JSON
- **Endpoints**:
  - `/NFeAutorizacao` - Autorização de NF-e
  - `/NFeStatusServico` - Status do serviço
  - `/NFeGerarPDF` - Geração de DANFE

#### 3.4.2. Fluxo de Emissão

![alt text](venda_nfe_process.png)

## 4. Modelo de Dados

### 4.1. Entidades Principais

#### 4.1.1. Produto
```java
@Entity
@Table(name = "produto")
public class Produto {
    private Long id;
    private String cprod;
    private String xprod;
    private String ncm;
    private String cfop;
    private String ucom;
    private BigDecimal vuncom;
    // Campos de impostos...
    private Boolean ativo;
}
```

#### 4.1.2. Cliente
```java
@Entity
@Table(name = "cliente")
public class Cliente {
    private Long id;
    private String xnome;
    private String cpf;
    private String cnpj;
    private String ie;
    // Endereço...
    private Boolean ativo;
}
```

#### 4.1.3. Venda
```java
@Entity
@Table(name = "venda")
public class Venda {
    private Long id;
    private Cliente cliente;
    private LocalDateTime dataVenda;
    private Integer numeroNFe;
    private String chaveNFe;
    private String status;
    private BigDecimal valorTotal;
    private List<ItemVenda> itens;
}
```

### 4.2. Relacionamentos

```
Produto (1) ←---→ (N) ItemVenda (N) ←---→ (1) Venda (1) ←---→ (1) Cliente
                                                              ↑
Empresa (1) --- Configuracoes (1) --- Usuario (N) ------------┘
```

## 5. Especificações de Segurança

### 5.1. Autenticação e Autorização
- **Autenticação**: Form-based authentication
- **Autorização**: Controle por perfis (ADMIN/USER)
- **Sessão**: Timeout configurável (30 minutos)
- **Senhas**: Armazenamento seguro no banco

### 5.2. Validações
- Validação de campos obrigatórios
- Validação de formato (CPF, CNPJ, Email)
- Validação de duplicidade
- Validação de negócio

## 6. Especificações de Performance

### 6.1. Otimizações Implementadas
- **Lazy Loading**: Em relacionamentos de coleções
- **Pagination**: Em listagens de dados
- **Cache**: Nível de sessão para dados estáticos
- **Connection Pool**: Configurado no Tomcat

### 6.2. Métricas Esperadas
- Tempo de resposta: < 2 segundos
- Usuários concorrentes: 50+
- NF-e emitidas por minuto: 10+

## 7. Configuração de Ambiente

### 7.1. Requisitos Mínimos
- **Java**: JDK 11
- **Servidor**: Tomcat 9+
- **Banco**: PostgreSQL 12+
- **Memória**: 2GB RAM
- **Storage**: 10GB livre

### 7.2. Configurações Recomendadas
- **Java**: JDK 11
- **Tomcat**: 9.0.x com connection pool
- **PostgreSQL**: 13+ com configurações otimizadas
- **Memória**: 4GB RAM
- **CPU**: 2 cores

## 8. Estrutura de Projeto

```
nfe-emissor-web/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/souzamonteiro/nfe/
│   │   │       ├── controller/     # Managed Beans
│   │   │       ├── dao/           # Data Access Objects
│   │   │       ├── model/         # Entidades JPA
│   │   │       └── service/       # Lógica de negócio
│   │   ├── resources/
│   │   │   ├── META-INF/
│   │   │   │   └── persistence.xml
│   │   │   └── messages_pt_BR.properties
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── web.xml
│   │       │   └── faces-config.xml
│   │       ├── resources/
│   │       │   └── css/
│   │       │       └── styles.css
│   │       └── *.xhtml            # Páginas JSF
│   └── test/
│       └── java/                  # Testes unitários
├── target/                        # Artefatos gerados
└── pom.xml                        # Configuração Maven
```

## 9. Fluxos de Trabalho

### 9.1. Fluxo de Emissão de NF-e
1. **Preparação**: Configurar empresa e certificados
2. **Cadastro**: Registrar produtos e clientes
3. **Venda**: Selecionar cliente e adicionar produtos
4. **Cálculo**: Sistema calcula impostos automaticamente
5. **Emissão**: Envio para SEFAZ via serviço HTTP
6. **Retorno**: Processamento do protocolo de autorização
7. **Arquivo**: Armazenamento do XML e geração de PDF

### 9.2. Fluxo de Manutenção
1. **Backup**: Diário do banco de dados
2. **Logs**: Monitoramento de erros e auditoria
3. **Atualização**: Deploy de novas versões
4. **Certificado**: Renovação anual do certificado digital

## 10. Considerações de Implantação

### 10.1. Pré-requisitos
- Certificado digital A1 válido
- Acesso à internet para comunicação com SEFAZ
- Banco de dados PostgreSQL configurado
- Servidor Tomcat com Java 11

### 10.2. Configurações Específicas
- **Ambiente**: Homologação inicialmente
- **Certificado**: Configurar caminho e senha
- **Schemas**: Estrutura de pastas para arquivos XSD
- **Logs**: Configurar nível de log para debug inicial

### 10.3. Validação Pós-Implantação
- Teste de conexão com banco de dados
- Teste de autenticação de usuários
- Teste de emissão de NF-e em homologação
- Validação de cálculos tributários
- Verificação de geração de PDF

## 11. Manutenção e Suporte

### 11.1. Monitoramento
- Logs de aplicação
- Monitoramento de banco de dados
- Verificação de espaço em disco
- Backup automático

### 11.2. Atualizações
- Atualização de bibliotecas via Maven
- Deploy incremental
- Rollback planejado

---

**Documento criado em:** 2025
**Versão:** 1.0
**Status:** Implementado e Validado