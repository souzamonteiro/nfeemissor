# NF-e Emissor - Sistema de Emissão de Nota Fiscal Eletrônica (NF-e)

## 📋 Sobre o Projeto

Sistema web completo para emissão de Nota Fiscal Eletrônica (NF-e) desenvolvido para empresas do Simples Nacional. Interface moderna com gestão completa de produtos, clientes, usuários e processo integrado de vendas com emissão automática de NF-e.

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 11** - Linguagem de programação
- **Jakarta EE 9** - Especificações enterprise
- **JSF 4.0** (Jakarta Server Faces) - Framework web
- **CDI** (Contexts and Dependency Injection) - Injeção de dependências
- **Bean Validation** - Validação de dados
- **JPA 3.0** - API de persistência
- **Hibernate 6.2.7** - ORM

### Frontend
- **PrimeFaces 12.0.0** - Componentes UI
- **XHTML** - Templates de página
- **CSS3** - Estilos e layout
- **JavaScript** - Interatividade

### Banco de Dados
- **PostgreSQL 12+** - Banco de dados relacional

### Servidor
- **Apache Tomcat 9** - Servlet container
- **Maven 3.6+** - Gerenciamento de dependências

## 📦 Módulos do Sistema

### ✅ Cadastro de Produtos
- Gestão completa de produtos
- Configuração de impostos (ICMS, PIS, COFINS)
- Validação de campos obrigatórios
- Controle de duplicidade

### ✅ Cadastro de Clientes
- Suporte a pessoa física e jurídica
- Endereçamento completo
- Validação de CPF/CNPJ
- Indicador de IE

### ✅ Dados da Empresa
- Configuração única do emitente
- Dados fiscais completos
- Endereço da empresa

### ✅ Configurações do Sistema
- Parâmetros de ambiente (Produção/Homologação)
- Configurações de certificado digital
- Caminhos de arquivos e schemas
- Parâmetros de numeração NF-e

### ✅ Gestão de Usuários
- Perfis ADMIN e USER
- Controle de acesso
- Validação de email e login único

### ✅ Módulo de Vendas
- Processo completo de venda
- Cálculo automático de impostos
- Emissão integrada de NF-e
- Geração de DANFE em PDF

## 🏗️ Arquitetura

### Padrão MVC
```
Model (Entities) ←→ Controller (Managed Beans) ←→ View (XHTML)
         ↓
Data Access (DAOs) ←→ Database (PostgreSQL)
```

### Estrutura de Pacotes
```
com.souzamonteiro.nfe/
├── controller/     # Managed Beans
├── dao/           # Data Access Objects  
├── model/         # Entidades JPA
└── service/       # Lógica de negócio
```

## ⚙️ Instalação e Configuração

### Pré-requisitos
- JDK 11
- Apache Tomcat 9+
- PostgreSQL 12+
- Maven 3.6+

### 1. Configuração do Banco de Dados
```sql
CREATE DATABASE nfedb;
CREATE USER nfe_user WITH PASSWORD 'nfe123';
GRANT ALL PRIVILEGES ON DATABASE nfedb TO nfe_user;
```

### 2. Configuração do Data Source (Tomcat)
Adicione no `conf/server.xml` do Tomcat:
```xml
<Resource name="jdbc/nfeDS" 
          auth="Container"
          type="javax.sql.DataSource"
          driverClassName="org.postgresql.Driver"
          url="jdbc:postgresql://localhost:5432/nfedb"
          username="nfe_user"
          password="nfe123"
          maxTotal="20"
          maxIdle="10"
          maxWaitMillis="-1"/>
```

### 3. Build e Deploy
```bash
# Compilar o projeto
mvn clean package

# O arquivo WAR será gerado em: target/nfe-emissor.war
# Copiar para: tomcat/webapps/
```

### 4. Acesso Inicial
- **URL**: http://localhost:8080/nfe-emissor
- **Usuário**: `admin`
- **Senha**: `admin123`

## 🔧 Configuração de Ambiente

### Certificado Digital
- Formato: A1 (PKCS12)
- Extensão: .pfx
- Configurar caminho no módulo de configurações

### Ambiente SEFAZ
- **Homologação**: Ambiente de testes
- **Produção**: Ambiente real (após testes)
- Configurar UF correspondente

### Estrutura de Diretórios
```
/
├── schemas/           # Arquivos XSD da NF-e
├── certificados/      # Certificado digital
└── xml/              # XMLs gerados e DANFEs
```

## 📊 Fluxo de Emissão NF-e

1. **Cadastrar** → Empresa, Produtos, Clientes
2. **Configurar** → Parâmetros do sistema
3. **Vender** → Processar venda com produtos
4. **Emitir** → Envio automático para SEFAZ
5. **Gerar** → XML e DANFE PDF

## 🔒 Segurança

### Autenticação
- Login com usuário/email e senha
- Sessão com timeout de 30 minutos
- Logout seguro

### Autorização
- **ADMIN**: Acesso completo
- **USER**: Apenas vendas e consultas

### Validações
- Campos obrigatórios
- Formato CPF/CNPJ
- Email válido
- Duplicidade de registros

## 🐛 Solução de Problemas

### Erros Comuns
1. **Certificado inválido** → Verificar caminho e senha
2. **Conexão banco** → Verificar data source
3. **Erro JSF** → Verificar escopos dos managed beans
4. **Problema deploy** → Verificar versão do Tomcat

### Logs
- Verificar logs do Tomcat: `logs/catalina.out`
- Logs da aplicação: `logs/nfe-emissor.log`

## 📈 Monitoramento

### Métricas
- Tempo de resposta: < 2 segundos
- Usuários concorrentes: 50+
- NF-e/minuto: 10+

### Backup
- Backup diário do banco PostgreSQL
- Backup dos XMLs emitidos
- Logs de auditoria

## 🤝 Contribuição

### Desenvolvimento
1. Clone o repositório
2. Importe no NetBeans/Eclipse
3. Configure Tomcat 9
4. Desenvolva e teste

### Padrões de Código
- Seguir convenções Java
- Usar injeção de dependência (CDI)
- Manter separação MVC
- Documentar código complexo

## 📄 Licença

Este projeto está disponível sob licença Apache 2.0.

## 🆕 Versões

### v1.0.0 (Atual)
- ✅ Todos os módulos implementados
- ✅ Emissão de NF-e funcional
- ✅ Interface completa
- ✅ Validações integradas

---

## 📞 Suporte

Para issues e dúvidas:
1. Verificar logs de aplicação
2. Testar em ambiente de homologação
3. Validar configurações de certificado

**Desenvolvido com ❤️ para automatizar processos fiscais**

---

*Última atualização: 2025*  
*Versão do documento: 1.0*