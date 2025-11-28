# NF-e Emissor - Sistema de Emissão de Nota Fiscal Eletrônica (NF-e)

## 1. Estrutura do Projeto e Dependências Maven

**pom.xml**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.souzamonteiro</groupId>
    <artifactId>nfe-emissor-web</artifactId>
    <version>1.0.0</version>
    <packaging>war</packaging>
    
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <version.jakarta.faces>4.0.1</version.jakarta.faces>
        <version.primefaces>12.0.0</version.primefaces>
        <version.hibernate>6.2.7.Final</version.hibernate>
        <version.postgresql>42.6.0</version.postgresql>
    </properties>
    
    <dependencies>
        <!-- Jakarta EE Web Profile -->
        <dependency>
            <groupId>jakarta.platform</groupId>
            <artifactId>jakarta.jakartaee-web-api</artifactId>
            <version>9.1.0</version>
            <scope>provided</scope>
        </dependency>
        
        <!-- JSF -->
        <dependency>
            <groupId>jakarta.faces</groupId>
            <artifactId>jakarta.faces-api</artifactId>
            <version>${version.jakarta.faces}</version>
            <scope>provided</scope>
        </dependency>
        
        <!-- PrimeFaces -->
        <dependency>
            <groupId>org.primefaces</groupId>
            <artifactId>primefaces</artifactId>
            <version>${version.primefaces}</version>
        </dependency>
        
        <!-- Hibernate -->
        <dependency>
            <groupId>org.hibernate.orm</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>${version.hibernate}</version>
        </dependency>
        
        <!-- PostgreSQL -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>${version.postgresql}</version>
        </dependency>
        
        <!-- CDI -->
        <dependency>
            <groupId>jakarta.enterprise</groupId>
            <artifactId>jakarta.enterprise.cdi-api</artifactId>
            <version>3.0.0</version>
            <scope>provided</scope>
        </dependency>
        
        <!-- JSON Processing -->
        <dependency>
            <groupId>org.json</groupId>
            <artifactId>json</artifactId>
            <version>20231013</version>
        </dependency>
        
        <!-- Bean Validation -->
        <dependency>
            <groupId>jakarta.validation</groupId>
            <artifactId>jakarta.validation-api</artifactId>
            <version>3.0.2</version>
        </dependency>
        
        <!-- Hibernate Validator -->
        <dependency>
            <groupId>org.hibernate.validator</groupId>
            <artifactId>hibernate-validator</artifactId>
            <version>8.0.0.Final</version>
        </dependency>
    </dependencies>
    
    <build>
        <finalName>nfe-emissor</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>11</source>
                    <target>11</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.3.2</version>
            </plugin>
        </plugins>
    </build>
</project>
```

## 2. Configuração do Hibernate (persistence.xml)

**src/main/resources/META-INF/persistence.xml**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="3.0" xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence 
             https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd">
    
    <persistence-unit name="nfePU" transaction-type="JTA">
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
        <jta-data-source>java:/nfeDS</jta-data-source>
        
        <properties>
            <property name="jakarta.persistence.schema-generation.database.action" value="update"/>
            <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQLDialect"/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.connection.charSet" value="UTF-8"/>
        </properties>
    </persistence-unit>
</persistence>
```

## 3. Configuração do JSF (faces-config.xml)

**src/main/webapp/WEB-INF/faces-config.xml**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<faces-config xmlns="https://jakarta.ee/xml/ns/jakartaee"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee 
              https://jakarta.ee/xml/ns/jakartaee/web-facesconfig_4_0.xsd"
              version="4.0">
    
    <application>
        <locale-config>
            <default-locale>pt_BR</default-locale>
            <supported-locale>pt_BR</supported-locale>
        </locale-config>
        <resource-bundle>
            <base-name>messages</base-name>
            <var>msg</var>
        </resource-bundle>
    </application>
    
    <navigation-rule>
        <from-view-id>/login.xhtml</from-view-id>
        <navigation-case>
            <from-outcome>success</from-outcome>
            <to-view-id>/index.xhtml</to-view-id>
            <redirect/>
        </navigation-case>
    </navigation-rule>
</faces-config>
```

## 4. Configuração Web (web.xml)

**src/main/webapp/WEB-INF/web.xml**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="5.0" 
         xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee 
         https://jakarta.ee/xml/ns/jakartaee/web-app_5_0.xsd">
    
    <display-name>NF-e Emissor</display-name>
    
    <!-- JSF Configuration -->
    <context-param>
        <param-name>jakarta.faces.PROJECT_STAGE</param-name>
        <param-value>Development</param-value>
    </context-param>
    
    <context-param>
        <param-name>jakarta.faces.FACELETS_SKIP_COMMENTS</param-name>
        <param-value>true</param-value>
    </context-param>
    
    <context-param>
        <param-name>primefaces.THEME</param-name>
        <param-value>saga</param-value>
    </context-param>
    
    <context-param>
        <param-name>primefaces.FONT_AWESOME</param-name>
        <param-value>true</param-value>
    </context-param>
    
    <!-- JSF Servlet -->
    <servlet>
        <servlet-name>Faces Servlet</servlet-name>
        <servlet-class>jakarta.faces.webapp.FacesServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>
    
    <servlet-mapping>
        <servlet-name>Faces Servlet</servlet-name>
        <url-pattern>*.xhtml</url-pattern>
    </servlet-mapping>
    
    <!-- Security Constraints -->
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Protected Pages</web-resource-name>
            <url-pattern>/secured/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>ADMIN</role-name>
            <role-name>USER</role-name>
        </auth-constraint>
    </security-constraint>
    
    <login-config>
        <auth-method>FORM</auth-method>
        <realm-name>nfeRealm</realm-name>
        <form-login-config>
            <form-login-page>/login.xhtml</form-login-page>
            <form-error-page>/login.xhtml?error=true</form-error-page>
        </form-login-config>
    </login-config>
    
    <security-role>
        <role-name>ADMIN</role-name>
    </security-role>
    <security-role>
        <role-name>USER</role-name>
    </security-role>
    
    <!-- Welcome Files -->
    <welcome-file-list>
        <welcome-file>index.xhtml</welcome-file>
    </welcome-file-list>
    
    <!-- Session Configuration -->
    <session-config>
        <session-timeout>30</session-timeout>
    </session-config>
</web-app>
```

## 5. Classes de Modelo (Entities)

**Produto.java**
```java
package com.souzamonteiro.nfe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produto")
public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Código do produto é obrigatório")
    @Column(name = "cprod", unique = true, nullable = false)
    private String cprod;
    
    @Column(name = "cean")
    private String cean;
    
    @NotBlank(message = "Descrição do produto é obrigatória")
    @Column(name = "xprod", nullable = false, length = 500)
    private String xprod;
    
    @NotBlank(message = "NCM é obrigatório")
    @Column(name = "ncm", nullable = false, length = 8)
    private String ncm;
    
    @Column(name = "cest")
    private String cest;
    
    @NotBlank(message = "CFOP é obrigatório")
    @Column(name = "cfop", nullable = false, length = 4)
    private String cfop;
    
    @NotBlank(message = "Unidade de medida é obrigatória")
    @Column(name = "ucom", nullable = false, length = 6)
    private String ucom;
    
    @NotNull(message = "Valor unitário é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor unitário deve ser maior que zero")
    @Column(name = "vuncom", precision = 15, scale = 4, nullable = false)
    private BigDecimal vuncom;
    
    // Campos para impostos
    @Column(name = "orig", length = 1)
    private String orig = "0";
    
    @Column(name = "cst_icms", length = 3)
    private String cstIcms = "00";
    
    @Column(name = "modbc_icms", length = 1)
    private String modbcIcms = "0";
    
    @Column(name = "picms", precision = 5, scale = 2)
    private BigDecimal picms = BigDecimal.valueOf(7.00);
    
    @Column(name = "cst_pis", length = 2)
    private String cstPis = "01";
    
    @Column(name = "ppis", precision = 5, scale = 2)
    private BigDecimal ppis = BigDecimal.valueOf(1.65);
    
    @Column(name = "cst_cofins", length = 2)
    private String cstCofins = "01";
    
    @Column(name = "pcofins", precision = 5, scale = 2)
    private BigDecimal pcofins = BigDecimal.valueOf(7.60);
    
    @Column(name = "ativo")
    private Boolean ativo = true;
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCprod() { return cprod; }
    public void setCprod(String cprod) { this.cprod = cprod; }
    
    public String getCean() { return cean; }
    public void setCean(String cean) { this.cean = cean; }
    
    public String getXprod() { return xprod; }
    public void setXprod(String xprod) { this.xprod = xprod; }
    
    public String getNcm() { return ncm; }
    public void setNcm(String ncm) { this.ncm = ncm; }
    
    public String getCest() { return cest; }
    public void setCest(String cest) { this.cest = cest; }
    
    public String getCfop() { return cfop; }
    public void setCfop(String cfop) { this.cfop = cfop; }
    
    public String getUcom() { return ucom; }
    public void setUcom(String ucom) { this.ucom = ucom; }
    
    public BigDecimal getVuncom() { return vuncom; }
    public void setVuncom(BigDecimal vuncom) { this.vuncom = vuncom; }
    
    public String getOrig() { return orig; }
    public void setOrig(String orig) { this.orig = orig; }
    
    public String getCstIcms() { return cstIcms; }
    public void setCstIcms(String cstIcms) { this.cstIcms = cstIcms; }
    
    public String getModbcIcms() { return modbcIcms; }
    public void setModbcIcms(String modbcIcms) { this.modbcIcms = modbcIcms; }
    
    public BigDecimal getPicms() { return picms; }
    public void setPicms(BigDecimal picms) { this.picms = picms; }
    
    public String getCstPis() { return cstPis; }
    public void setCstPis(String cstPis) { this.cstPis = cstPis; }
    
    public BigDecimal getPpis() { return ppis; }
    public void setPpis(BigDecimal ppis) { this.ppis = ppis; }
    
    public String getCstCofins() { return cstCofins; }
    public void setCstCofins(String cstCofins) { this.cstCofins = cstCofins; }
    
    public BigDecimal getPcofins() { return pcofins; }
    public void setPcofins(BigDecimal pcofins) { this.pcofins = pcofins; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
```

**Cliente.java**
```java
package com.souzamonteiro.nfe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

@Entity
@Table(name = "cliente")
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "xnome", nullable = false, length = 200)
    private String xnome;
    
    @CPF(message = "CPF inválido")
    @Column(name = "cpf", unique = true, length = 11)
    private String cpf;
    
    @CNPJ(message = "CNPJ inválido")
    @Column(name = "cnpj", unique = true, length = 14)
    private String cnpj;
    
    @Column(name = "ie", length = 20)
    private String ie;
    
    @Column(name = "indiedest", length = 1)
    private String indIEDest = "9";
    
    // Endereço
    @NotBlank(message = "Logradouro é obrigatório")
    @Column(name = "xlgr", nullable = false, length = 200)
    private String xlgr;
    
    @NotBlank(message = "Número é obrigatório")
    @Column(name = "nro", nullable = false, length = 10)
    private String nro;
    
    @Column(name = "xcpl", length = 100)
    private String xcpl;
    
    @NotBlank(message = "Bairro é obrigatório")
    @Column(name = "xbairro", nullable = false, length = 100)
    private String xbairro;
    
    @NotBlank(message = "Código do município é obrigatório")
    @Column(name = "cmun", nullable = false, length = 7)
    private String cmun;
    
    @NotBlank(message = "Município é obrigatório")
    @Column(name = "xmun", nullable = false, length = 100)
    private String xmun;
    
    @NotBlank(message = "UF é obrigatória")
    @Column(name = "uf", nullable = false, length = 2)
    private String uf;
    
    @NotBlank(message = "CEP é obrigatório")
    @Column(name = "cep", nullable = false, length = 8)
    private String cep;
    
    @Column(name = "fone", length = 15)
    private String fone;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "ativo")
    private Boolean ativo = true;
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getXnome() { return xnome; }
    public void setXnome(String xnome) { this.xnome = xnome; }
    
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    
    public String getIe() { return ie; }
    public void setIe(String ie) { this.ie = ie; }
    
    public String getIndIEDest() { return indIEDest; }
    public void setIndIEDest(String indIEDest) { this.indIEDest = indIEDest; }
    
    public String getXlgr() { return xlgr; }
    public void setXlgr(String xlgr) { this.xlgr = xlgr; }
    
    public String getNro() { return nro; }
    public void setNro(String nro) { this.nro = nro; }
    
    public String getXcpl() { return xcpl; }
    public void setXcpl(String xcpl) { this.xcpl = xcpl; }
    
    public String getXbairro() { return xbairro; }
    public void setXbairro(String xbairro) { this.xbairro = xbairro; }
    
    public String getCmun() { return cmun; }
    public void setCmun(String cmun) { this.cmun = cmun; }
    
    public String getXmun() { return xmun; }
    public void setXmun(String xmun) { this.xmun = xmun; }
    
    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }
    
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    
    public String getFone() { return fone; }
    public void setFone(String fone) { this.fone = fone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    
    public String getDocumento() {
        return cpf != null ? cpf : cnpj;
    }
    
    public String getTipoDocumento() {
        return cpf != null ? "F" : "J";
    }
}
```

**Empresa.java**
```java
package com.souzamonteiro.nfe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "empresa")
public class Empresa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "CNPJ é obrigatório")
    @CNPJ(message = "CNPJ inválido")
    @Column(name = "cnpj", unique = true, nullable = false, length = 14)
    private String cnpj;
    
    @NotBlank(message = "Razão Social é obrigatória")
    @Column(name = "xnome", nullable = false, length = 200)
    private String xnome;
    
    @NotBlank(message = "Nome Fantasia é obrigatório")
    @Column(name = "xfant", nullable = false, length = 200)
    private String xfant;
    
    @NotBlank(message = "Inscrição Estadual é obrigatória")
    @Column(name = "ie", nullable = false, length = 20)
    private String ie;
    
    @NotBlank(message = "Regime Tributário é obrigatório")
    @Column(name = "crt", nullable = false, length = 1)
    private String crt = "3";
    
    // Endereço
    @NotBlank(message = "Logradouro é obrigatório")
    @Column(name = "xlgr", nullable = false, length = 200)
    private String xlgr;
    
    @NotBlank(message = "Número é obrigatório")
    @Column(name = "nro", nullable = false, length = 10)
    private String nro;
    
    @Column(name = "xcpl", length = 100)
    private String xcpl;
    
    @NotBlank(message = "Bairro é obrigatório")
    @Column(name = "xbairro", nullable = false, length = 100)
    private String xbairro;
    
    @NotBlank(message = "Código do município é obrigatório")
    @Column(name = "cmun", nullable = false, length = 7)
    private String cmun;
    
    @NotBlank(message = "Município é obrigatório")
    @Column(name = "xmun", nullable = false, length = 100)
    private String xmun;
    
    @NotBlank(message = "UF é obrigatória")
    @Column(name = "uf", nullable = false, length = 2)
    private String uf;
    
    @NotBlank(message = "CEP é obrigatório")
    @Column(name = "cep", nullable = false, length = 8)
    private String cep;
    
    @NotBlank(message = "Código do país é obrigatório")
    @Column(name = "cpais", nullable = false, length = 4)
    private String cpais = "1058";
    
    @NotBlank(message = "País é obrigatório")
    @Column(name = "xpais", nullable = false, length = 50)
    private String xpais = "Brasil";
    
    @Column(name = "fone", length = 15)
    private String fone;
    
    @Column(name = "email", length = 100)
    private String email;
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    
    public String getXnome() { return xnome; }
    public void setXnome(String xnome) { this.xnome = xnome; }
    
    public String getXfant() { return xfant; }
    public void setXfant(String xfant) { this.xfant = xfant; }
    
    public String getIe() { return ie; }
    public void setIe(String ie) { this.ie = ie; }
    
    public String getCrt() { return crt; }
    public void setCrt(String crt) { this.crt = crt; }
    
    public String getXlgr() { return xlgr; }
    public void setXlgr(String xlgr) { this.xlgr = xlgr; }
    
    public String getNro() { return nro; }
    public void setNro(String nro) { this.nro = nro; }
    
    public String getXcpl() { return xcpl; }
    public void setXcpl(String xcpl) { this.xcpl = xcpl; }
    
    public String getXbairro() { return xbairro; }
    public void setXbairro(String xbairro) { this.xbairro = xbairro; }
    
    public String getCmun() { return cmun; }
    public void setCmun(String cmun) { this.cmun = cmun; }
    
    public String getXmun() { return xmun; }
    public void setXmun(String xmun) { this.xmun = xmun; }
    
    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }
    
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    
    public String getCpais() { return cpais; }
    public void setCpais(String cpais) { this.cpais = cpais; }
    
    public String getXpais() { return xpais; }
    public void setXpais(String xpais) { this.xpais = xpais; }
    
    public String getFone() { return fone; }
    public void setFone(String fone) { this.fone = fone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
```

**Configuracao.java**
```java
package com.souzamonteiro.nfe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "configuracao")
public class Configuracao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "UF do WebService é obrigatória")
    @Column(name = "webservice_uf", nullable = false, length = 2)
    private String webserviceUF = "BA";
    
    @NotBlank(message = "UF do Emitente é obrigatória")
    @Column(name = "emitente_uf", nullable = false, length = 2)
    private String emitenteUF = "BA";
    
    @NotBlank(message = "Ambiente é obrigatório")
    @Column(name = "webservice_ambiente", nullable = false, length = 1)
    private String webserviceAmbiente = "2";
    
    @NotBlank(message = "Caminho dos Schemas é obrigatório")
    @Column(name = "caminho_schemas", nullable = false, length = 500)
    private String caminhoSchemas;
    
    @NotBlank(message = "Caminho do Certificado é obrigatório")
    @Column(name = "caminho_certificado", nullable = false, length = 500)
    private String caminhoCertificado;
    
    @NotBlank(message = "Senha do Certificado é obrigatória")
    @Column(name = "senha_certificado", nullable = false, length = 100)
    private String senhaCertificado;
    
    @NotBlank(message = "Caminho do XML é obrigatório")
    @Column(name = "caminho_xml", nullable = false, length = 500)
    private String caminhoXML;
    
    @Column(name = "gerar_pdf", length = 1)
    private String gerarPDF = "1";
    
    @Column(name = "porta_servidor", length = 5)
    private String portaServidor = "3435";
    
    @Column(name = "serie_nfe", length = 3)
    private String serieNFe = "1";
    
    @Column(name = "natureza_operacao", length = 100)
    private String naturezaOperacao = "VENDA";
    
    @Column(name = "finalidade_emissao", length = 1)
    private String finalidadeEmissao = "1";
    
    @Column(name = "consumidor_final", length = 1)
    private String consumidorFinal = "1";
    
    @Column(name = "presenca_comprador", length = 1)
    private String presencaComprador = "1";
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getWebserviceUF() { return webserviceUF; }
    public void setWebserviceUF(String webserviceUF) { this.webserviceUF = webserviceUF; }
    
    public String getEmitenteUF() { return emitenteUF; }
    public void setEmitenteUF(String emitenteUF) { this.emitenteUF = emitenteUF; }
    
    public String getWebserviceAmbiente() { return webserviceAmbiente; }
    public void setWebserviceAmbiente(String webserviceAmbiente) { this.webserviceAmbiente = webserviceAmbiente; }
    
    public String getCaminhoSchemas() { return caminhoSchemas; }
    public void setCaminhoSchemas(String caminhoSchemas) { this.caminhoSchemas = caminhoSchemas; }
    
    public String getCaminhoCertificado() { return caminhoCertificado; }
    public void setCaminhoCertificado(String caminhoCertificado) { this.caminhoCertificado = caminhoCertificado; }
    
    public String getSenhaCertificado() { return senhaCertificado; }
    public void setSenhaCertificado(String senhaCertificado) { this.senhaCertificado = senhaCertificado; }
    
    public String getCaminhoXML() { return caminhoXML; }
    public void setCaminhoXML(String caminhoXML) { this.caminhoXML = caminhoXML; }
    
    public String getGerarPDF() { return gerarPDF; }
    public void setGerarPDF(String gerarPDF) { this.gerarPDF = gerarPDF; }
    
    public String getPortaServidor() { return portaServidor; }
    public void setPortaServidor(String portaServidor) { this.portaServidor = portaServidor; }
    
    public String getSerieNFe() { return serieNFe; }
    public void setSerieNFe(String serieNFe) { this.serieNFe = serieNFe; }
    
    public String getNaturezaOperacao() { return naturezaOperacao; }
    public void setNaturezaOperacao(String naturezaOperacao) { this.naturezaOperacao = naturezaOperacao; }
    
    public String getFinalidadeEmissao() { return finalidadeEmissao; }
    public void setFinalidadeEmissao(String finalidadeEmissao) { this.finalidadeEmissao = finalidadeEmissao; }
    
    public String getConsumidorFinal() { return consumidorFinal; }
    public void setConsumidorFinal(String consumidorFinal) { this.consumidorFinal = consumidorFinal; }
    
    public String getPresencaComprador() { return presencaComprador; }
    public void setPresencaComprador(String presencaComprador) { this.presencaComprador = presencaComprador; }
}
```

**Usuario.java**
```java
package com.souzamonteiro.nfe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "usuario")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;
    
    @NotBlank(message = "Login é obrigatório")
    @Column(name = "login", unique = true, nullable = false, length = 50)
    private String login;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;
    
    @NotBlank(message = "Senha é obrigatória")
    @Column(name = "senha", nullable = false, length = 255)
    private String senha;
    
    @NotBlank(message = "Perfil é obrigatório")
    @Column(name = "perfil", nullable = false, length = 20)
    private String perfil = "USER";
    
    @Column(name = "ativo")
    private Boolean ativo = true;
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    
    public boolean isAdmin() {
        return "ADMIN".equals(perfil);
    }
}
```

**Venda.java**
```java
package com.souzamonteiro.nfe.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venda")
public class Venda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @Column(name = "data_venda", nullable = false)
    private LocalDateTime dataVenda;
    
    @Column(name = "numero_nfe")
    private Integer numeroNFe;
    
    @Column(name = "chave_nfe", length = 44)
    private String chaveNFe;
    
    @Column(name = "protocolo_nfe", length = 50)
    private String protocoloNFe;
    
    @Column(name = "status", length = 20)
    private String status = "PENDENTE";
    
    @Column(name = "valor_total", precision = 15, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;
    
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenda> itens = new ArrayList<>();
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    
    public LocalDateTime getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDateTime dataVenda) { this.dataVenda = dataVenda; }
    
    public Integer getNumeroNFe() { return numeroNFe; }
    public void setNumeroNFe(Integer numeroNFe) { this.numeroNFe = numeroNFe; }
    
    public String getChaveNFe() { return chaveNFe; }
    public void setChaveNFe(String chaveNFe) { this.chaveNFe = chaveNFe; }
    
    public String getProtocoloNFe() { return protocoloNFe; }
    public void setProtocoloNFe(String protocoloNFe) { this.protocoloNFe = protocoloNFe; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    
    public List<ItemVenda> getItens() { return itens; }
    public void setItens(List<ItemVenda> itens) { this.itens = itens; }
    
    public void adicionarItem(ItemVenda item) {
        item.setVenda(this);
        this.itens.add(item);
        calcularTotal();
    }
    
    public void removerItem(ItemVenda item) {
        this.itens.remove(item);
        calcularTotal();
    }
    
    public void calcularTotal() {
        this.valorTotal = itens.stream()
            .map(ItemVenda::getValorTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
```

**ItemVenda.java**
```java
package com.souzamonteiro.nfe.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "item_venda")
public class ItemVenda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;
    
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    
    @Column(name = "quantidade", precision = 15, scale = 4, nullable = false)
    private BigDecimal quantidade = BigDecimal.ONE;
    
    @Column(name = "valor_unitario", precision = 15, scale = 4, nullable = false)
    private BigDecimal valorUnitario;
    
    @Column(name = "valor_total", precision = 15, scale = 2, nullable = false)
    private BigDecimal valorTotal;
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Venda getVenda() { return venda; }
    public void setVenda(Venda venda) { this.venda = venda; }
    
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    
    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { 
        this.quantidade = quantidade; 
        calcularTotal();
    }
    
    public BigDecimal getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(BigDecimal valorUnitario) { 
        this.valorUnitario = valorUnitario; 
        calcularTotal();
    }
    
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    
    private void calcularTotal() {
        if (quantidade != null && valorUnitario != null) {
            this.valorTotal = quantidade.multiply(valorUnitario);
        }
    }
}
```

## 6. DAOs (Data Access Objects)

**GenericDAO.java**
```java
package com.souzamonteiro.nfe.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

public abstract class GenericDAO<T, ID extends Serializable> {
    
    @PersistenceContext(unitName = "nfePU")
    protected EntityManager em;
    
    private final Class<T> entityClass;
    
    protected GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    public T findById(ID id) {
        return em.find(entityClass, id);
    }
    
    public List<T> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        return em.createQuery(cq).getResultList();
    }
    
    public List<T> findRange(int[] range) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        TypedQuery<T> q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
    public Long count() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(entityClass);
        cq.select(cb.count(root));
        return em.createQuery(cq).getSingleResult();
    }
    
    public T save(T entity) {
        if (isNew(entity)) {
            em.persist(entity);
            return entity;
        } else {
            return em.merge(entity);
        }
    }
    
    public void delete(T entity) {
        if (!em.contains(entity)) {
            entity = em.merge(entity);
        }
        em.remove(entity);
    }
    
    protected abstract boolean isNew(T entity);
}
```

**ProdutoDAO.java**
```java
package com.souzamonteiro.nfe.dao;

import com.souzamonteiro.nfe.model.Produto;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ProdutoDAO extends GenericDAO<Produto, Long> {
    
    public ProdutoDAO() {
        super(Produto.class);
    }
    
    @Override
    protected boolean isNew(Produto produto) {
        return produto.getId() == null;
    }
    
    public List<Produto> findAtivos() {
        TypedQuery<Produto> query = em.createQuery(
            "SELECT p FROM Produto p WHERE p.ativo = true ORDER BY p.xprod", 
            Produto.class
        );
        return query.getResultList();
    }
    
    public Produto findByCodigo(String codigo) {
        try {
            TypedQuery<Produto> query = em.createQuery(
                "SELECT p FROM Produto p WHERE p.cprod = :codigo", 
                Produto.class
            );
            query.setParameter("codigo", codigo);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
```

**ClienteDAO.java**
```java
package com.souzamonteiro.nfe.dao;

import com.souzamonteiro.nfe.model.Cliente;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ClienteDAO extends GenericDAO<Cliente, Long> {
    
    public ClienteDAO() {
        super(Cliente.class);
    }
    
    @Override
    protected boolean isNew(Cliente cliente) {
        return cliente.getId() == null;
    }
    
    public List<Cliente> findAtivos() {
        TypedQuery<Cliente> query = em.createQuery(
            "SELECT c FROM Cliente c WHERE c.ativo = true ORDER BY c.xnome", 
            Cliente.class
        );
        return query.getResultList();
    }
    
    public Cliente findByDocumento(String documento) {
        try {
            TypedQuery<Cliente> query = em.createQuery(
                "SELECT c FROM Cliente c WHERE c.cpf = :doc OR c.cnpj = :doc", 
                Cliente.class
            );
            query.setParameter("doc", documento);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
```

**EmpresaDAO.java**
```java
package com.souzamonteiro.nfe.dao;

import com.souzamonteiro.nfe.model.Empresa;
import jakarta.ejb.Stateless;

@Stateless
public class EmpresaDAO extends GenericDAO<Empresa, Long> {
    
    public EmpresaDAO() {
        super(Empresa.class);
    }
    
    @Override
    protected boolean isNew(Empresa empresa) {
        return empresa.getId() == null;
    }
    
    public Empresa getEmpresa() {
        try {
            return findAll().get(0);
        } catch (Exception e) {
            return null;
        }
    }
}
```

**ConfiguracaoDAO.java**
```java
package com.souzamonteiro.nfe.dao;

import com.souzamonteiro.nfe.model.Configuracao;
import jakarta.ejb.Stateless;

@Stateless
public class ConfiguracaoDAO extends GenericDAO<Configuracao, Long> {
    
    public ConfiguracaoDAO() {
        super(Configuracao.class);
    }
    
    @Override
    protected boolean isNew(Configuracao configuracao) {
        return configuracao.getId() == null;
    }
    
    public Configuracao getConfiguracao() {
        try {
            return findAll().get(0);
        } catch (Exception e) {
            return null;
        }
    }
}
```

**UsuarioDAO.java**
```java
package com.souzamonteiro.nfe.dao;

import com.souzamonteiro.nfe.model.Usuario;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class UsuarioDAO extends GenericDAO<Usuario, Long> {
    
    public UsuarioDAO() {
        super(Usuario.class);
    }
    
    @Override
    protected boolean isNew(Usuario usuario) {
        return usuario.getId() == null;
    }
    
    public List<Usuario> findAtivos() {
        TypedQuery<Usuario> query = em.createQuery(
            "SELECT u FROM Usuario u WHERE u.ativo = true ORDER BY u.nome", 
            Usuario.class
        );
        return query.getResultList();
    }
    
    public Usuario findByLogin(String login) {
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.login = :login AND u.ativo = true", 
                Usuario.class
            );
            query.setParameter("login", login);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Usuario findByEmail(String email) {
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.email = :email AND u.ativo = true", 
                Usuario.class
            );
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
```

**VendaDAO.java**
```java
package com.souzamonteiro.nfe.dao;

import com.souzamonteiro.nfe.model.Venda;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class VendaDAO extends GenericDAO<Venda, Long> {
    
    public VendaDAO() {
        super(Venda.class);
    }
    
    @Override
    protected boolean isNew(Venda venda) {
        return venda.getId() == null;
    }
    
    public List<Venda> findPendentes() {
        TypedQuery<Venda> query = em.createQuery(
            "SELECT v FROM Venda v WHERE v.status = 'PENDENTE' ORDER BY v.dataVenda", 
            Venda.class
        );
        return query.getResultList();
    }
    
    public List<Venda> findEmitidas() {
        TypedQuery<Venda> query = em.createQuery(
            "SELECT v FROM Venda v WHERE v.status = 'EMITIDA' ORDER BY v.dataVenda DESC", 
            Venda.class
        );
        return query.getResultList();
    }
    
    public Integer getProximoNumeroNFe() {
        try {
            TypedQuery<Integer> query = em.createQuery(
                "SELECT COALESCE(MAX(v.numeroNFe), 0) + 1 FROM Venda v WHERE v.numeroNFe IS NOT NULL", 
                Integer.class
            );
            return query.getSingleResult();
        } catch (Exception e) {
            return 1;
        }
    }
}
```

## 7. Controllers (Managed Beans)

**ProdutoController.java**
```java
package com.souzamonteiro.nfe.controller;

import com.souzamonteiro.nfe.dao.ProdutoDAO;
import com.souzamonteiro.nfe.model.Produto;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ProdutoController implements Serializable {
    
    @Inject
    private ProdutoDAO produtoDAO;
    
    private List<Produto> produtos;
    private Produto produto;
    private Produto produtoSelecionado;
    private boolean editando;
    
    @PostConstruct
    public void init() {
        carregarProdutos();
        novoProduto();
    }
    
    public void carregarProdutos() {
        produtos = produtoDAO.findAtivos();
        editando = false;
    }
    
    public void novoProduto() {
        produto = new Produto();
        editando = true;
    }
    
    public void editarProduto() {
        if (produtoSelecionado != null) {
            produto = produtoSelecionado;
            editando = true;
        }
    }
    
    public void salvarProduto() {
        try {
            // Verificar se código já existe
            Produto existente = produtoDAO.findByCodigo(produto.getCprod());
            if (existente != null && !existente.getId().equals(produto.getId())) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Já existe um produto com este código."));
                return;
            }
            
            produtoDAO.save(produto);
            carregarProdutos();
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Sucesso", "Produto salvo com sucesso."));
                
            PrimeFaces.current().executeScript("PF('produtoDialog').hide()");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro", "Erro ao salvar produto: " + e.getMessage()));
        }
    }
    
    public void excluirProduto() {
        if (produtoSelecionado != null) {
            try {
                produtoSelecionado.setAtivo(false);
                produtoDAO.save(produtoSelecionado);
                carregarProdutos();
                
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, 
                    "Sucesso", "Produto excluído com sucesso."));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Erro ao excluir produto: " + e.getMessage()));
            }
        }
    }
    
    public void cancelarEdicao() {
        carregarProdutos();
    }
    
    // Getters e Setters
    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }
    
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    
    public Produto getProdutoSelecionado() { return produtoSelecionado; }
    public void setProdutoSelecionado(Produto produtoSelecionado) { this.produtoSelecionado = produtoSelecionado; }
    
    public boolean isEditando() { return editando; }
    public void setEditando(boolean editando) { this.editando = editando; }
}
```

**ClienteController.java**
```java
package com.souzamonteiro.nfe.controller;

import com.souzamonteiro.nfe.dao.ClienteDAO;
import com.souzamonteiro.nfe.model.Cliente;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ClienteController implements Serializable {
    
    @Inject
    private ClienteDAO clienteDAO;
    
    private List<Cliente> clientes;
    private Cliente cliente;
    private Cliente clienteSelecionado;
    private boolean editando;
    
    @PostConstruct
    public void init() {
        carregarClientes();
        novoCliente();
    }
    
    public void carregarClientes() {
        clientes = clienteDAO.findAtivos();
        editando = false;
    }
    
    public void novoCliente() {
        cliente = new Cliente();
        editando = true;
    }
    
    public void editarCliente() {
        if (clienteSelecionado != null) {
            cliente = clienteSelecionado;
            editando = true;
        }
    }
    
    public void salvarCliente() {
        try {
            // Validar CPF/CNPJ
            if (cliente.getCpf() == null && cliente.getCnpj() == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Informe CPF ou CNPJ."));
                return;
            }
            
            // Verificar se documento já existe
            String documento = cliente.getDocumento();
            Cliente existente = clienteDAO.findByDocumento(documento);
            if (existente != null && !existente.getId().equals(cliente.getId())) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Já existe um cliente com este documento."));
                return;
            }
            
            clienteDAO.save(cliente);
            carregarClientes();
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Sucesso", "Cliente salvo com sucesso."));
                
            PrimeFaces.current().executeScript("PF('clienteDialog').hide()");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro", "Erro ao salvar cliente: " + e.getMessage()));
        }
    }
    
    public void excluirCliente() {
        if (clienteSelecionado != null) {
            try {
                clienteSelecionado.setAtivo(false);
                clienteDAO.save(clienteSelecionado);
                carregarClientes();
                
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, 
                    "Sucesso", "Cliente excluído com sucesso."));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Erro ao excluir cliente: " + e.getMessage()));
            }
        }
    }
    
    public void cancelarEdicao() {
        carregarClientes();
    }
    
    // Getters e Setters
    public List<Cliente> getClientes() { return clientes; }
    public void setClientes(List<Cliente> clientes) { this.clientes = clientes; }
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    
    public Cliente getClienteSelecionado() { return clienteSelecionado; }
    public void setClienteSelecionado(Cliente clienteSelecionado) { this.clienteSelecionado = clienteSelecionado; }
    
    public boolean isEditando() { return editando; }
    public void setEditando(boolean editando) { this.editando = editando; }
}
```

**EmpresaController.java**
```java
package com.souzamonteiro.nfe.controller;

import com.souzamonteiro.nfe.dao.EmpresaDAO;
import com.souzamonteiro.nfe.model.Empresa;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class EmpresaController implements Serializable {
    
    @Inject
    private EmpresaDAO empresaDAO;
    
    private Empresa empresa;
    
    @PostConstruct
    public void init() {
        carregarEmpresa();
    }
    
    public void carregarEmpresa() {
        empresa = empresaDAO.getEmpresa();
        if (empresa == null) {
            empresa = new Empresa();
        }
    }
    
    public void salvarEmpresa() {
        try {
            empresaDAO.save(empresa);
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Sucesso", "Dados da empresa salvos com sucesso."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro", "Erro ao salvar dados da empresa: " + e.getMessage()));
        }
    }
    
    // Getters e Setters
    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
}
```

**ConfiguracaoController.java**
```java
package com.souzamonteiro.nfe.controller;

import com.souzamonteiro.nfe.dao.ConfiguracaoDAO;
import com.souzamonteiro.nfe.model.Configuracao;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class ConfiguracaoController implements Serializable {
    
    @Inject
    private ConfiguracaoDAO configuracaoDAO;
    
    private Configuracao configuracao;
    
    @PostConstruct
    public void init() {
        carregarConfiguracao();
    }
    
    public void carregarConfiguracao() {
        configuracao = configuracaoDAO.getConfiguracao();
        if (configuracao == null) {
            configuracao = new Configuracao();
        }
    }
    
    public void salvarConfiguracao() {
        try {
            configuracaoDAO.save(configuracao);
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Sucesso", "Configurações salvas com sucesso."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro", "Erro ao salvar configurações: " + e.getMessage()));
        }
    }
    
    // Getters e Setters
    public Configuracao getConfiguracao() { return configuracao; }
    public void setConfiguracao(Configuracao configuracao) { this.configuracao = configuracao; }
}
```

**UsuarioController.java**
```java
package com.souzamonteiro.nfe.controller;

import com.souzamonteiro.nfe.dao.UsuarioDAO;
import com.souzamonteiro.nfe.model.Usuario;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class UsuarioController implements Serializable {
    
    @Inject
    private UsuarioDAO usuarioDAO;
    
    private List<Usuario> usuarios;
    private Usuario usuario;
    private Usuario usuarioSelecionado;
    private boolean editando;
    private String confirmarSenha;
    
    @PostConstruct
    public void init() {
        carregarUsuarios();
        novoUsuario();
    }
    
    public void carregarUsuarios() {
        usuarios = usuarioDAO.findAtivos();
        editando = false;
    }
    
    public void novoUsuario() {
        usuario = new Usuario();
        confirmarSenha = "";
        editando = true;
    }
    
    public void editarUsuario() {
        if (usuarioSelecionado != null) {
            usuario = usuarioSelecionado;
            confirmarSenha = usuario.getSenha();
            editando = true;
        }
    }
    
    public void salvarUsuario() {
        try {
            // Validar senha
            if (usuario.getId() == null && (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty())) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Senha é obrigatória para novo usuário."));
                return;
            }
            
            if (!usuario.getSenha().equals(confirmarSenha)) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Senha e confirmação não conferem."));
                return;
            }
            
            // Verificar se login já existe
            Usuario existenteLogin = usuarioDAO.findByLogin(usuario.getLogin());
            if (existenteLogin != null && !existenteLogin.getId().equals(usuario.getId())) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Já existe um usuário com este login."));
                return;
            }
            
            // Verificar se email já existe
            Usuario existenteEmail = usuarioDAO.findByEmail(usuario.getEmail());
            if (existenteEmail != null && !existenteEmail.getId().equals(usuario.getId())) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Já existe um usuário com este email."));
                return;
            }
            
            usuarioDAO.save(usuario);
            carregarUsuarios();
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Sucesso", "Usuário salvo com sucesso."));
                
            PrimeFaces.current().executeScript("PF('usuarioDialog').hide()");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro", "Erro ao salvar usuário: " + e.getMessage()));
        }
    }
    
    public void excluirUsuario() {
        if (usuarioSelecionado != null) {
            try {
                usuarioSelecionado.setAtivo(false);
                usuarioDAO.save(usuarioSelecionado);
                carregarUsuarios();
                
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, 
                    "Sucesso", "Usuário excluído com sucesso."));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Erro ao excluir usuário: " + e.getMessage()));
            }
        }
    }
    
    public void cancelarEdicao() {
        carregarUsuarios();
    }
    
    // Getters e Setters
    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public Usuario getUsuarioSelecionado() { return usuarioSelecionado; }
    public void setUsuarioSelecionado(Usuario usuarioSelecionado) { this.usuarioSelecionado = usuarioSelecionado; }
    
    public boolean isEditando() { return editando; }
    public void setEditando(boolean editando) { this.editando = editando; }
    
    public String getConfirmarSenha() { return confirmarSenha; }
    public void setConfirmarSenha(String confirmarSenha) { this.confirmarSenha = confirmarSenha; }
}
```

**VendaController.java**
```java
package com.souzamonteiro.nfe.controller;

import com.souzamonteiro.nfe.dao.*;
import com.souzamonteiro.nfe.model.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.PrimeFaces;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class VendaController implements Serializable {
    
    @Inject
    private VendaDAO vendaDAO;
    
    @Inject
    private ClienteDAO clienteDAO;
    
    @Inject
    private ProdutoDAO produtoDAO;
    
    @Inject
    private ConfiguracaoDAO configuracaoDAO;
    
    @Inject
    private EmpresaDAO empresaDAO;
    
    private List<Venda> vendas;
    private Venda venda;
    private List<Cliente> clientes;
    private List<Produto> produtos;
    private Cliente clienteSelecionado;
    private Produto produtoSelecionado;
    private ItemVenda itemVenda;
    private boolean editando;
    
    @PostConstruct
    public void init() {
        carregarVendas();
        novaVenda();
        carregarClientes();
        carregarProdutos();
    }
    
    public void carregarVendas() {
        vendas = vendaDAO.findEmitidas();
        editando = false;
    }
    
    public void novaVenda() {
        venda = new Venda();
        venda.setDataVenda(LocalDateTime.now());
        itemVenda = new ItemVenda();
        editando = true;
    }
    
    public void carregarClientes() {
        clientes = clienteDAO.findAtivos();
    }
    
    public void carregarProdutos() {
        produtos = produtoDAO.findAtivos();
    }
    
    public void adicionarItem() {
        if (produtoSelecionado != null && itemVenda.getQuantidade() != null) {
            // Verificar se produto já está na venda
            for (ItemVenda item : venda.getItens()) {
                if (item.getProduto().getId().equals(produtoSelecionado.getId())) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, 
                        "Aviso", "Produto já adicionado à venda."));
                    return;
                }
            }
            
            ItemVenda novoItem = new ItemVenda();
            novoItem.setProduto(produtoSelecionado);
            novoItem.setQuantidade(itemVenda.getQuantidade());
            novoItem.setValorUnitario(produtoSelecionado.getVuncom());
            
            venda.adicionarItem(novoItem);
            
            // Limpar seleção
            produtoSelecionado = null;
            itemVenda = new ItemVenda();
            
            PrimeFaces.current().ajax().update("form:vendaItens");
        }
    }
    
    public void removerItem(ItemVenda item) {
        venda.removerItem(item);
        PrimeFaces.current().ajax().update("form:vendaItens");
    }
    
    public void finalizarVenda() {
        if (venda.getCliente() == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro", "Selecione um cliente."));
            return;
        }
        
        if (venda.getItens().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro", "Adicione pelo menos um item à venda."));
            return;
        }
        
        try {
            // Gerar número da NF-e
            Integer numeroNFe = vendaDAO.getProximoNumeroNFe();
            venda.setNumeroNFe(numeroNFe);
            
            // Salvar venda
            vendaDAO.save(venda);
            
            // Emitir NF-e
            boolean sucesso = emitirNFe(venda);
            
            if (sucesso) {
                venda.setStatus("EMITIDA");
                vendaDAO.save(venda);
                
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, 
                    "Sucesso", "Venda finalizada e NF-e emitida com sucesso."));
                
                carregarVendas();
            } else {
                venda.setStatus("ERRO");
                vendaDAO.save(venda);
                
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Erro ao emitir NF-e. Venda salva como pendente."));
            }
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro", "Erro ao finalizar venda: " + e.getMessage()));
        }
    }
    
    public void cancelarVenda() {
        carregarVendas();
    }
    
    private boolean emitirNFe(Venda venda) {
        try {
            Configuracao config = configuracaoDAO.getConfiguracao();
            Empresa empresa = empresaDAO.getEmpresa();
            
            if (config == null || empresa == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Configure empresa e configurações antes de emitir NF-e."));
                return false;
            }
            
            // Construir JSON para envio ao servidor de NF-e
            JSONObject nfeJson = construirJsonNFe(venda, empresa, config);
            
            // Enviar para servidor de NF-e
            String url = "http://localhost:" + config.getPortaServidor() + "/NFeAutorizacao";
            String resposta = enviarParaServidor(url, nfeJson.toString());
            
            // Processar resposta
            JSONObject respostaJson = new JSONObject(resposta);
            String cStat = respostaJson.getString("cStat");
            
            if ("100".equals(cStat)) {
                venda.setChaveNFe(respostaJson.getString("chave"));
                venda.setProtocoloNFe(respostaJson.getString("nProt"));
                return true;
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro NF-e", respostaJson.getString("xMotivo")));
                return false;
            }
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro", "Erro ao emitir NF-e: " + e.getMessage()));
            return false;
        }
    }
    
    private JSONObject construirJsonNFe(Venda venda, Empresa empresa, Configuracao config) {
        JSONObject nfeJson = new JSONObject();
        JSONObject infNFe = new JSONObject();
        
        // IDE
        JSONObject ide = new JSONObject();
        ide.put("cUF", getCodigoUF(empresa.getUf()));
        ide.put("cNF", String.format("%08d", venda.getNumeroNFe()).substring(0, 8));
        ide.put("natOp", config.getNaturezaOperacao());
        ide.put("mod", "55");
        ide.put("serie", config.getSerieNFe());
        ide.put("nNF", venda.getNumeroNFe().toString());
        ide.put("dhEmi", LocalDateTime.now().toString().replace("T", " ").substring(0, 19));
        ide.put("tpNF", "1");
        ide.put("idDest", "1");
        ide.put("cMunFG", empresa.getCmun());
        ide.put("tpImp", "1");
        ide.put("tpEmis", "1");
        ide.put("cDV", "6");
        ide.put("tpAmb", config.getWebserviceAmbiente());
        ide.put("finNFe", config.getFinalidadeEmissao());
        ide.put("indFinal", config.getConsumidorFinal());
        ide.put("indPres", config.getPresencaComprador());
        ide.put("procEmi", "0");
        ide.put("verProc", "1.0");
        
        infNFe.put("ide", ide);
        
        // EMITENTE
        JSONObject emit = new JSONObject();
        emit.put("CNPJ", empresa.getCnpj());
        emit.put("xNome", empresa.getXnome());
        emit.put("IE", empresa.getIe());
        emit.put("CRT", empresa.getCrt());
        
        JSONObject enderEmit = new JSONObject();
        enderEmit.put("xLgr", empresa.getXlgr());
        enderEmit.put("nro", empresa.getNro());
        enderEmit.put("xCpl", empresa.getXcpl());
        enderEmit.put("xBairro", empresa.getXbairro());
        enderEmit.put("cMun", empresa.getCmun());
        enderEmit.put("xMun", empresa.getXmun());
        enderEmit.put("UF", empresa.getUf());
        enderEmit.put("CEP", empresa.getCep());
        enderEmit.put("cPais", empresa.getCpais());
        enderEmit.put("xPais", empresa.getXpais());
        enderEmit.put("fone", empresa.getFone());
        
        emit.put("enderEmit", enderEmit);
        infNFe.put("emit", emit);
        
        // DESTINATÁRIO
        JSONObject dest = new JSONObject();
        Cliente cliente = venda.getCliente();
        if (cliente.getCpf() != null) {
            dest.put("CPF", cliente.getCpf());
        } else {
            dest.put("CNPJ", cliente.getCnpj());
        }
        dest.put("xNome", cliente.getXnome());
        dest.put("indIEDest", cliente.getIndIEDest());
        
        JSONObject enderDest = new JSONObject();
        enderDest.put("xLgr", cliente.getXlgr());
        enderDest.put("nro", cliente.getNro());
        enderDest.put("xCpl", cliente.getXcpl());
        enderDest.put("xBairro", cliente.getXbairro());
        enderDest.put("cMun", cliente.getCmun());
        enderDest.put("xMun", cliente.getXmun());
        enderDest.put("UF", cliente.getUf());
        enderDest.put("CEP", cliente.getCep());
        
        dest.put("enderDest", enderDest);
        infNFe.put("dest", dest);
        
        // DETALHES (PRODUTOS)
        JSONArray det = new JSONArray();
        BigDecimal vBC = BigDecimal.ZERO;
        BigDecimal vICMS = BigDecimal.ZERO;
        BigDecimal vPIS = BigDecimal.ZERO;
        BigDecimal vCOFINS = BigDecimal.ZERO;
        
        for (int i = 0; i < venda.getItens().size(); i++) {
            ItemVenda item = venda.getItens().get(i);
            Produto produto = item.getProduto();
            
            JSONObject itemDet = new JSONObject();
            JSONObject prod = new JSONObject();
            prod.put("cProd", produto.getCprod());
            prod.put("cEAN", produto.getCean());
            prod.put("xProd", produto.getXprod());
            prod.put("NCM", produto.getNcm());
            prod.put("CEST", produto.getCest());
            prod.put("CFOP", produto.getCfop());
            prod.put("uCom", produto.getUcom());
            prod.put("qCom", item.getQuantidade().toString());
            prod.put("vUnCom", produto.getVuncom().toString());
            prod.put("vProd", item.getValorTotal().toString());
            prod.put("cEANTrib", produto.getCean());
            prod.put("uTrib", produto.getUcom());
            prod.put("qTrib", item.getQuantidade().toString());
            prod.put("vUnTrib", produto.getVuncom().toString());
            prod.put("indTot", "1");
            
            JSONObject imposto = new JSONObject();
            
            // ICMS
            JSONObject icms = new JSONObject();
            JSONObject icms00 = new JSONObject();
            icms00.put("orig", produto.getOrig());
            icms00.put("CST", produto.getCstIcms());
            icms00.put("modBC", produto.getModbcIcms());
            icms00.put("vBC", item.getValorTotal().toString());
            icms00.put("pICMS", produto.getPicms().toString());
            
            BigDecimal vicmsItem = item.getValorTotal().multiply(produto.getPicms()).divide(BigDecimal.valueOf(100));
            icms00.put("vICMS", vicmsItem.toString());
            
            icms.put("ICMS00", icms00);
            imposto.put("ICMS", icms);
            
            // PIS
            JSONObject pis = new JSONObject();
            JSONObject pisAliq = new JSONObject();
            pisAliq.put("CST", produto.getCstPis());
            pisAliq.put("vBC", item.getValorTotal().toString());
            pisAliq.put("pPIS", produto.getPpis().toString());
            
            BigDecimal vpisItem = item.getValorTotal().multiply(produto.getPpis()).divide(BigDecimal.valueOf(100));
            pisAliq.put("vPIS", vpisItem.toString());
            
            pis.put("PISAliq", pisAliq);
            imposto.put("PIS", pis);
            
            // COFINS
            JSONObject cofins = new JSONObject();
            JSONObject cofinsAliq = new JSONObject();
            cofinsAliq.put("CST", produto.getCstCofins());
            cofinsAliq.put("vBC", item.getValorTotal().toString());
            cofinsAliq.put("pCOFINS", produto.getPcofins().toString());
            
            BigDecimal vcofinsItem = item.getValorTotal().multiply(produto.getPcofins()).divide(BigDecimal.valueOf(100));
            cofinsAliq.put("vCOFINS", vcofinsItem.toString());
            
            cofins.put("COFINSAliq", cofinsAliq);
            imposto.put("COFINS", cofins);
            
            itemDet.put("prod", prod);
            itemDet.put("imposto", imposto);
            det.put(itemDet);
            
            // Acumular totais
            vBC = vBC.add(item.getValorTotal());
            vICMS = vICMS.add(vicmsItem);
            vPIS = vPIS.add(vpisItem);
            vCOFINS = vCOFINS.add(vcofinsItem);
        }
        
        infNFe.put("det", det);
        
        // TOTAIS
        JSONObject total = new JSONObject();
        JSONObject icmsTot = new JSONObject();
        icmsTot.put("vBC", vBC.toString());
        icmsTot.put("vICMS", vICMS.toString());
        icmsTot.put("vICMSDeson", "0.00");
        icmsTot.put("vFCP", "0.00");
        icmsTot.put("vBCST", "0.00");
        icmsTot.put("vST", "0.00");
        icmsTot.put("vFCPST", "0.00");
        icmsTot.put("vFCPSTRet", "0.00");
        icmsTot.put("vProd", vBC.toString());
        icmsTot.put("vFrete", "0.00");
        icmsTot.put("vSeg", "0.00");
        icmsTot.put("vDesc", "0.00");
        icmsTot.put("vII", "0.00");
        icmsTot.put("vIPI", "0.00");
        icmsTot.put("vIPIDevol", "0.00");
        icmsTot.put("vPIS", vPIS.toString());
        icmsTot.put("vCOFINS", vCOFINS.toString());
        icmsTot.put("vOutro", "0.00");
        icmsTot.put("vNF", vBC.toString());
        
        total.put("ICMSTot", icmsTot);
        infNFe.put("total", total);
        
        // TRANSPORTE
        JSONObject transp = new JSONObject();
        transp.put("modFrete", "9");
        infNFe.put("transp", transp);
        
        // PAGAMENTO
        JSONObject pag = new JSONObject();
        JSONArray detPag = new JSONArray();
        JSONObject pagamento = new JSONObject();
        pagamento.put("tPag", "01");
        pagamento.put("vPag", vBC.toString());
        detPag.put(pagamento);
        pag.put("detPag", detPag);
        infNFe.put("pag", pag);
        
        // INFORMAÇÕES ADICIONAIS
        JSONObject infAdic = new JSONObject();
        BigDecimal tributos = vICMS.add(vPIS).add(vCOFINS);
        infAdic.put("infCpl", String.format("Tributos Totais Incidentes (Lei Federal 12.741/2012): R$%.2f", tributos));
        infNFe.put("infAdic", infAdic);
        
        nfeJson.put("infNFe", infNFe);
        
        return nfeJson;
    }
    
    private String enviarParaServidor(String urlString, String json) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
    
    private String getCodigoUF(String uf) {
        switch (uf) {
            case "AC": return "12";
            case "AL": return "27";
            case "AM": return "13";
            case "AP": return "16";
            case "BA": return "29";
            case "CE": return "23";
            case "DF": return "53";
            case "ES": return "32";
            case "GO": return "52";
            case "MA": return "21";
            case "MG": return "31";
            case "MS": return "50";
            case "MT": return "51";
            case "PA": return "15";
            case "PB": return "25";
            case "PE": return "26";
            case "PI": return "22";
            case "PR": return "41";
            case "RJ": return "33";
            case "RN": return "24";
            case "RO": return "11";
            case "RR": return "14";
            case "RS": return "43";
            case "SC": return "42";
            case "SE": return "28";
            case "SP": return "35";
            case "TO": return "17";
            default: return "29"; // BA como padrão
        }
    }
    
    // Getters e Setters
    public List<Venda> getVendas() { return vendas; }
    public void setVendas(List<Venda> vendas) { this.vendas = vendas; }
    
    public Venda getVenda() { return venda; }
    public void setVenda(Venda venda) { this.venda = venda; }
    
    public List<Cliente> getClientes() { return clientes; }
    public void setClientes(List<Cliente> clientes) { this.clientes = clientes; }
    
    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }
    
    public Cliente getClienteSelecionado() { return clienteSelecionado; }
    public void setClienteSelecionado(Cliente clienteSelecionado) { this.clienteSelecionado = clienteSelecionado; }
    
    public Produto getProdutoSelecionado() { return produtoSelecionado; }
    public void setProdutoSelecionado(Produto produtoSelecionado) { this.produtoSelecionado = produtoSelecionado; }
    
    public ItemVenda getItemVenda() { return itemVenda; }
    public void setItemVenda(ItemVenda itemVenda) { this.itemVenda = itemVenda; }
    
    public boolean isEditando() { return editando; }
    public void setEditando(boolean editando) { this.editando = editando; }
}
```

**LoginController.java**
```java
package com.souzamonteiro.nfe.controller;

import com.souzamonteiro.nfe.dao.UsuarioDAO;
import com.souzamonteiro.nfe.model.Usuario;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginController implements Serializable {
    
    @Inject
    private UsuarioDAO usuarioDAO;
    
    private String login;
    private String senha;
    private Usuario usuarioLogado;
    
    public String login() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            
            // Tentar autenticar via container
            request.login(login, senha);
            
            // Buscar usuário no banco
            usuarioLogado = usuarioDAO.findByLogin(login);
            if (usuarioLogado == null) {
                usuarioLogado = usuarioDAO.findByEmail(login);
            }
            
            if (usuarioLogado != null && usuarioLogado.getAtivo()) {
                return "index?faces-redirect=true";
            } else {
                request.logout();
                context.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erro", "Usuário inativo ou não encontrado."));
                return null;
            }
            
        } catch (ServletException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro", "Login ou senha inválidos."));
            return null;
        }
    }
    
    public String logout() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            request.logout();
            
            usuarioLogado = null;
            login = null;
            senha = null;
            
            return "/login?faces-redirect=true";
        } catch (ServletException e) {
            return "/login?faces-redirect=true";
        }
    }
    
    public boolean isLoggedIn() {
        return usuarioLogado != null;
    }
    
    public boolean isAdmin() {
        return usuarioLogado != null && "ADMIN".equals(usuarioLogado.getPerfil());
    }
    
    // Getters e Setters
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public Usuario getUsuarioLogado() { return usuarioLogado; }
    public void setUsuarioLogado(Usuario usuarioLogado) { this.usuarioLogado = usuarioLogado; }
}
```

## 8. Views (XHTML)

**Template (template.xhtml)**
```xml
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html"
      xmlns:f="http://xmlns.jcp.org/jsf/core"
      xmlns:p="http://primefaces.org/ui"
      xmlns:ui="http://xmlns.jcp.org/jsf/facelets">
    
    <f:view locale="pt_BR">
        <h:head>
            <title>NF-e Emissor</title>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
            
            <h:outputStylesheet library="css" name="styles.css"/>
            
            <script type="text/javascript">
                function handleCommand(args) {
                    if (args && args.validationFailed) {
                        PF('dlg').show();
                    }
                }
            </script>
        </h:head>
        
        <h:body>
            <p:growl id="growl" showDetail="true" life="3000" />
            
            <p:layout fullPage="true">
                <p:layoutUnit position="north" size="60" header="NF-e Emissor" resizable="false">
                    <h:form id="menuForm">
                        <p:menubar>
                            <p:menuitem value="Início" icon="pi pi-home" outcome="index"/>
                            
                            <p:submenu label="Cadastros" icon="pi pi-fw pi-database" rendered="#{loginController.isAdmin()}">
                                <p:menuitem value="Produtos" icon="pi pi-fw pi-box" outcome="produtos"/>
                                <p:menuitem value="Clientes" icon="pi pi-fw pi-users" outcome="clientes"/>
                                <p:menuitem value="Usuários" icon="pi pi-fw pi-user" outcome="usuarios"/>
                            </p:submenu>
                            
                            <p:submenu label="Configurações" icon="pi pi-fw pi-cog" rendered="#{loginController.isAdmin()}">
                                <p:menuitem value="Empresa" icon="pi pi-fw pi-building" outcome="empresa"/>
                                <p:menuitem value="Sistema" icon="pi pi-fw pi-sliders-h" outcome="configuracao"/>
                            </p:submenu>
                            
                            <p:menuitem value="Vendas" icon="pi pi-shopping-cart" outcome="vendas"/>
                            
                            <f:facet name="options">
                                <p:outputLabel value="Bem-vindo, #{loginController.usuarioLogado.nome}" 
                                              style="margin-right: 10px;"/>
                                <p:commandButton value="Sair" icon="pi pi-power-off" 
                                               action="#{loginController.logout}"/>
                            </f:facet>
                        </p:menubar>
                    </h:form>
                </p:layoutUnit>
                
                <p:layoutUnit position="center">
                    <ui:insert name="content">Conteúdo</ui:insert>
                </p:layoutUnit>
            </p:layout>
        </h:body>
    </f:view>
</html>
```

**Login (login.xhtml)**
```xml
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html"
      xmlns:f="http://xmlns.jcp.org/jsf/core"
      xmlns:p="http://primefaces.org/ui">
    
    <f:view locale="pt_BR">
        <h:head>
            <title>NF-e Emissor - Login</title>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        </h:head>
        
        <h:body style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); height: 100vh; display: flex; align-items: center; justify-content: center;">
            <p:growl id="growl" showDetail="true" life="3000" />
            
            <div class="login-container">
                <p:card style="width: 400px; background: white; border-radius: 10px; box-shadow: 0 10px 30px rgba(0,0,0,0.3);">
                    <f:facet name="title">
                        <div style="text-align: center; padding: 20px;">
                            <i class="pi pi-building" style="font-size: 3rem; color: #667eea;"></i>
                            <h2 style="margin: 10px 0 0 0; color: #333;">NF-e Emissor</h2>
                            <p style="color: #666; margin: 0;">Sistema de Emissão de Nota Fiscal</p>
                        </div>
                    </f:facet>
                    
                    <f:facet name="content">
                        <h:form id="loginForm">
                            <div class="p-fluid">
                                <div class="p-field">
                                    <p:outputLabel for="login" value="Login ou Email"/>
                                    <p:inputText id="login" value="#{loginController.login}" 
                                               required="true" requiredMessage="Login é obrigatório"
                                               style="width: 100%"/>
                                </div>
                                
                                <div class="p-field">
                                    <p:outputLabel for="senha" value="Senha"/>
                                    <p:password id="senha" value="#{loginController.senha}" 
                                              feedback="false" required="true" 
                                              requiredMessage="Senha é obrigatória"
                                              style="width: 100%"/>
                                </div>
                                
                                <div class="p-field" style="margin-top: 20px;">
                                    <p:commandButton value="Entrar" action="#{loginController.login}" 
                                                   style="width: 100%; background: #667eea; border: none;"
                                                   update="growl"/>
                                </div>
                            </div>
                        </h:form>
                    </f:facet>
                </p:card>
            </div>
            
            <style>
                .login-container {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    width: 100%;
                }
                .p-field {
                    margin-bottom: 1rem;
                }
            </style>
        </h:body>
    </f:view>
</html>
```

**Index (index.xhtml)**
```xml
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html"
      xmlns:f="http://xmlns.jcp.org/jsf/core"
      xmlns:p="http://primefaces.org/ui"
      xmlns:ui="http://xmlns.jcp.org/jsf/facelets">
    
    <ui:composition template="template.xhtml">
        <ui:define name="content">
            <div class="p-p-4">
                <div class="p-grid">
                    <div class="p-col-12">
                        <p:card>
                            <f:facet name="title">
                                <div class="p-d-flex p-ai-center">
                                    <i class="pi pi-home p-mr-2"></i>
                                    <span>Dashboard</span>
                                </div>
                            </f:facet>
                            <f:facet name="content">
                                <div class="p-grid">
                                    <div class="p-col-12 p-md-4">
                                        <p:card style="background: #4CAF50; color: white;">
                                            <f:facet name="title">
                                                <div class="p-d-flex p-ai-center">
                                                    <i class="pi pi-shopping-cart p-mr-2"></i>
                                                    <span>Vendas Hoje</span>
                                                </div>
                                            </f:facet>
                                            <f:facet name="content">
                                                <h1 style="font-size: 2.5rem; margin: 0; text-align: center;">0</h1>
                                                <p style="text-align: center; margin: 0;">NF-e emitidas</p>
                                            </f:facet>
                                        </p:card>
                                    </div>
                                    
                                    <div class="p-col-12 p-md-4">
                                        <p:card style="background: #2196F3; color: white;">
                                            <f:facet name="title">
                                                <div class="p-d-flex p-ai-center">
                                                    <i class="pi pi-box p-mr-2"></i>
                                                    <span>Produtos</span>
                                                </div>
                                            </f:facet>
                                            <f:facet name="content">
                                                <h1 style="font-size: 2.5rem; margin: 0; text-align: center;">#{produtoController.produtos.size()}</h1>
                                                <p style="text-align: center; margin: 0;">Cadastrados</p>
                                            </f:facet>
                                        </p:card>
                                    </div>
                                    
                                    <div class="p-col-12 p-md-4">
                                        <p:card style="background: #FF9800; color: white;">
                                            <f:facet name="title">
                                                <div class="p-d-flex p-ai-center">
                                                    <i class="pi pi-users p-mr-2"></i>
                                                    <span>Clientes</span>
                                                </div>
                                            </f:facet>
                                            <f:facet name="content">
                                                <h1 style="font-size: 2.5rem; margin: 0; text-align: center;">#{clienteController.clientes.size()}</h1>
                                                <p style="text-align: center; margin: 0;">Cadastrados</p>
                                            </f:facet>
                                        </p:card>
                                    </div>
                                </div>
                                
                                <div class="p-mt-4">
                                    <h3>Ações Rápidas</h3>
                                    <div class="p-grid">
                                        <div class="p-col-12 p-md-3">
                                            <p:commandButton value="Nova Venda" icon="pi pi-plus" 
                                                           outcome="vendas" style="width: 100%;"/>
                                        </div>
                                        <div class="p-col-12 p-md-3">
                                            <p:commandButton value="Cadastrar Produto" icon="pi pi-box" 
                                                           outcome="produtos" rendered="#{loginController.isAdmin()}" 
                                                           style="width: 100%;"/>
                                        </div>
                                        <div class="p-col-12 p-md-3">
                                            <p:commandButton value="Cadastrar Cliente" icon="pi pi-user-plus" 
                                                           outcome="clientes" rendered="#{loginController.isAdmin()}" 
                                                           style="width: 100%;"/>
                                        </div>
                                        <div class="p-col-12 p-md-3">
                                            <p:commandButton value="Configurações" icon="pi pi-cog" 
                                                           outcome="configuracao" rendered="#{loginController.isAdmin()}" 
                                                           style="width: 100%;"/>
                                        </div>
                                    </div>
                                </div>
                            </f:facet>
                        </p:card>
                    </div>
                </div>
            </div>
        </ui:define>
    </ui:composition>
</html>
```

**Produtos (produtos.xhtml)**
```xml
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html"
      xmlns:f="http://xmlns.jcp.org/jsf/core"
      xmlns:p="http://primefaces.org/ui"
      xmlns:ui="http://xmlns.jcp.org/jsf/facelets">
    
    <ui:composition template="template.xhtml">
        <ui:define name="content">
            <div class="p-p-4">
                <h:form id="form">
                    <p:card>
                        <f:facet name="title">
                            <div class="p-d-flex p-ai-center p-jc-between">
                                <div>
                                    <i class="pi pi-box p-mr-2"></i>
                                    <span>Cadastro de Produtos</span>
                                </div>
                                <p:commandButton value="Novo Produto" icon="pi pi-plus" 
                                               action="#{produtoController.novoProduto}" 
                                               update=":form:grid,:form:formPanel"
                                               rendered="#{!produtoController.editando}"/>
                            </div>
                        </f:facet>
                        
                        <f:facet name="content">
                            <!-- Grid de Produtos -->
                            <p:outputPanel id="grid" rendered="#{!produtoController.editando}">
                                <p:dataTable id="produtosTable" var="prod" value="#{produtoController.produtos}" 
                                           paginator="true" rows="10" 
                                           selection="#{produtoController.produtoSelecionado}"
                                           selectionMode="single"
                                           emptyMessage="Nenhum produto cadastrado">
                                    
                                    <p:column headerText="Código" style="width: 100px;">
                                        <h:outputText value="#{prod.cprod}"/>
                                    </p:column>
                                    
                                    <p:column headerText="Descrição">
                                        <h:outputText value="#{prod.xprod}"/>
                                    </p:column>
                                    
                                    <p:column headerText="NCM" style="width: 100px;">
                                        <h:outputText value="#{prod.ncm}"/>
                                    </p:column>
                                    
                                    <p:column headerText="Valor Unitário" style="width: 120px;">
                                        <h:outputText value="#{prod.vuncom}">
                                            <f:convertNumber type="currency" currencyCode="BRL"/>
                                        </h:outputText>
                                    </p:column>
                                    
                                    <p:column headerText="Ações" style="width: 120px;">
                                        <p:commandButton icon="pi pi-pencil" title="Editar" 
                                                       action="#{produtoController.editarProduto}"
                                                       update=":form:grid,:form:formPanel"/>
                                        <p:commandButton icon="pi pi-trash" title="Excluir" 
                                                       style="margin-left: 5px;"
                                                       action="#{produtoController.excluirProduto}"
                                                       update=":form:grid,:form:growl"/>
                                    </p:column>
                                </p:dataTable>
                            </p:outputPanel>
                            
                            <!-- Formulário de Produto -->
                            <p:outputPanel id="formPanel" rendered="#{produtoController.editando}">
                                <p:panel header="Produto" style="margin-bottom: 20px;">
                                    <div class="p-fluid">
                                        <div class="p-grid">
                                            <div class="p-col-12 p-md-6">
                                                <p:outputLabel for="cprod" value="Código do Produto *"/>
                                                <p:inputText id="cprod" value="#{produtoController.produto.cprod}" 
                                                           required="true" requiredMessage="Código é obrigatório"
                                                           style="width: 100%"/>
                                            </div>
                                            
                                            <div class="p-col-12 p-md-6">
                                                <p:outputLabel for="cean" value="Código EAN"/>
                                                <p:inputText id="cean" value="#{produtoController.produto.cean}" 
                                                           style="width: 100%"/>
                                            </div>
                                            
                                            <div class="p-col-12">
                                                <p:outputLabel for="xprod" value="Descrição do Produto *"/>
                                                <p:inputText id="xprod" value="#{produtoController.produto.xprod}" 
                                                           required="true" requiredMessage="Descrição é obrigatória"
                                                           style="width: 100%"/>
                                            </div>
                                            
                                            <div class="p-col-12 p-md-4">
                                                <p:outputLabel for="ncm" value="NCM *"/>
                                                <p:inputText id="ncm" value="#{produtoController.produto.ncm}" 
                                                           required="true" requiredMessage="NCM é obrigatório"
                                                           style="width: 100%"/>
                                            </div>
                                            
                                            <div class="p-col-12 p-md-4">
                                                <p:outputLabel for="cest" value="CEST"/>
                                                <p:inputText id="cest" value="#{produtoController.produto.cest}" 
                                                           style="width: 100%"/>
                                            </div>
                                            
                                            <div class="p-col-12 p-md-4">
                                                <p:outputLabel for="cfop" value="CFOP *"/>
                                                <p:inputText id="cfop" value="#{produtoController.produto.cfop}" 
                                                           required="true" requiredMessage="CFOP é obrigatório"
                                                           style="width: 100%"/>
                                            </div>
                                            
                                            <div class="p-col-12 p-md-4">
                                                <p:outputLabel for="ucom" value="Unidade *"/>
                                                <p:inputText id="ucom" value="#{produtoController.produto.ucom}" 
                                                           required="true" requiredMessage="Unidade é obrigatória"
                                                           style="width: 100%"/>
                                            </div>
                                            
                                            <div class="p-col-12 p-md-4">
                                                <p:outputLabel for="vuncom" value="Valor Unitário *"/>
                                                <p:inputNumber id="vuncom" value="#{produtoController.produto.vuncom}" 
                                                             required="true" requiredMessage="Valor é obrigatório"
                                                             minValue="0.01" decimalPlaces="4"
                                                             style="width: 100%"/>
                                            </div>
                                        </div>
                                        
                                        <p:panel header="Impostos" toggleable="true" collapsed="true">
                                            <div class="p-grid">
                                                <div class="p-col-12 p-md-4">
                                                    <p:outputLabel for="orig" value="Origem *"/>
                                                    <p:selectOneMenu id="orig" value="#{produtoController.produto.orig}" 
                                                                   style="width: 100%">
                                                        <f:selectItem itemLabel="Nacional" itemValue="0"/>
                                                        <f:selectItem itemLabel="Estrangeira Importação" itemValue="1"/>
                                                        <f:selectItem itemLabel="Estrangeira Adquirida" itemValue="2"/>
                                                    </p:selectOneMenu>
                                                </div>
                                                
                                                <div class="p-col-12 p-md-4">
                                                    <p:outputLabel for="cstIcms" value="CST ICMS *"/>
                                                    <p:selectOneMenu id="cstIcms" value="#{produtoController.produto.cstIcms}" 
                                                                   style="width: 100%">
                                                        <f:selectItem itemLabel="00 - Tributada integralmente" itemValue="00"/>
                                                        <f:selectItem itemLabel="20 - Com redução de base" itemValue="20"/>
                                                        <f:selectItem itemLabel="40 - Isenta" itemValue="40"/>
                                                        <f:selectItem itemLabel="41 - Não tributada" itemValue="41"/>
                                                        <f:selectItem itemLabel="60 - ICMS cobrado anteriormente" itemValue="60"/>
                                                    </p:selectOneMenu>
                                                </div>
                                                
                                                <div class="p-col-12 p-md-4">
                                                    <p:outputLabel for="picms" value="Alíquota ICMS (%) *"/>
                                                    <p:inputNumber id="picms" value="#{produtoController.produto.picms}" 
                                                                 minValue="0" maxValue="100" decimalPlaces="2"
                                                                 style="width: 100%"/>
                                                </div>
                                                
                                                <div class="p-col-12 p-md-4">
                                                    <p:outputLabel for="cstPis" value="CST PIS *"/>
                                                    <p:selectOneMenu id="cstPis" value="#{produtoController.produto.cstPis}" 
                                                                   style="width: 100%">
                                                        <f:selectItem itemLabel="01 - Operação Tributável" itemValue="01"/>
                                                        <f:selectItem itemLabel="02 - Operação Tributável" itemValue="02"/>
                                                        <f:selectItem itemLabel="03 - Operação Tributável" itemValue="03"/>
                                                        <f:selectItem itemLabel="04 - Operação Tributável" itemValue="04"/>
                                                        <f:selectItem itemLabel="06 - Operação Tributável" itemValue="06"/>
                                                        <f:selectItem itemLabel="07 - Operação Isenta" itemValue="07"/>
                                                        <f:selectItem itemLabel="08 - Operação Sem Incidência" itemValue="08"/>
                                                        <f:selectItem itemLabel="09 - Operação com Suspensão" itemValue="09"/>
                                                    </p:selectOneMenu>
                                                </div>
                                                
                                                <div class="p-col-12 p-md-4">
                                                    <p:outputLabel for="ppis" value="Alíquota PIS (%) *"/>
                                                    <p:inputNumber id="ppis" value="#{produtoController.produto.ppis}" 
                                                                 minValue="0" maxValue="100" decimalPlaces="2"
                                                                 style="width: 100%"/>
                                                </div>
                                                
                                                <div class="p-col-12 p-md-4">
                                                    <p:outputLabel for="cstCofins" value="CST COFINS *"/>
                                                    <p:selectOneMenu id="cstCofins" value="#{produtoController.produto.cstCofins}" 
                                                                   style="width: 100%">
                                                        <f:selectItem itemLabel="01 - Operação Tributável" itemValue="01"/>
                                                        <f:selectItem itemLabel="02 - Operação Tributável" itemValue="02"/>
                                                        <f:selectItem itemLabel="03 - Operação Tributável" itemValue="03"/>
                                                        <f:selectItem itemLabel="04 - Operação Tributável" itemValue="04"/>
                                                        <f:selectItem itemLabel="06 - Operação Tributável" itemValue="06"/>
                                                        <f:selectItem itemLabel="07 - Operação Isenta" itemValue="07"/>
                                                        <f:selectItem itemLabel="08 - Operação Sem Incidência" itemValue="08"/>
                                                        <f:selectItem itemLabel="09 - Operação com Suspensão" itemValue="09"/>
                                                    </p:selectOneMenu>
                                                </div>
                                                
                                                <div class="p-col-12 p-md-4">
                                                    <p:outputLabel for="pcofins" value="Alíquota COFINS (%) *"/>
                                                    <p:inputNumber id="pcofins" value="#{produtoController.produto.pcofins}" 
                                                                 minValue="0" maxValue="100" decimalPlaces="2"
                                                                 style="width: 100%"/>
                                                </div>
                                            </div>
                                        </p:panel>
                                        
                                        <div class="p-d-flex p-jc-end p-mt-3">
                                            <p:commandButton value="Salvar" icon="pi pi-check" 
                                                           action="#{produtoController.salvarProduto}"
                                                           update=":form:grid,:form:formPanel,:form:growl"/>
                                            <p:commandButton value="Cancelar" icon="pi pi-times" 
                                                           action="#{produtoController.cancelarEdicao}"
                                                           style="margin-left: 5px;"
                                                           update=":form:grid,:form:formPanel"/>
                                        </div>
                                    </div>
                                </p:panel>
                            </p:outputPanel>
                        </f:facet>
                    </p:card>
                </h:form>
            </div>
        </ui:define>
    </ui:composition>
</html>
```

**Clientes (clientes.xhtml)**
```xml
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html"
      xmlns:f="http://xmlns.jcp.org/jsf/core"
      xmlns:p="http://primefaces.org/ui"
      xmlns:ui="http://xmlns.jcp.org/jsf/facelets">
    
    <ui:composition template="template.xhtml">
        <ui:define name="content">
            <div class="p-p-4">
                <h:form id="form">
                    <p:card>
                        <f:facet name="title">
                            <div class="p-d-flex p-ai-center p-jc-between">
                                <div>
                                    <i class="pi pi-users p-mr-2"></i>
                                    <span>Cadastro de Clientes</span>
                                </div>
                                <p:commandButton value="Novo Cliente" icon="pi pi-plus" 
                                               action="#{clienteController.novoCliente}" 
                                               update=":form:grid,:form:formPanel"
                                               rendered="#{!clienteController.editando}"/>
                            </div>
                        </f:facet>
                        
                        <f:facet name="content">
                            <!-- Grid de Clientes -->
                            <p:outputPanel id="grid" rendered="#{!clienteController.editando}">
                                <p:dataTable id="clientesTable" var="cli" value="#{clienteController.clientes}" 
                                           paginator="true" rows="10" 
                                           selection="#{clienteController.clienteSelecionado}"
                                           selectionMode="single"
                                           emptyMessage="Nenhum cliente cadastrado">
                                    
                                    <p:column headerText="Nome/Razão Social">
                                        <h:outputText value="#{cli.xnome}"/>
                                    </p:column>
                                    
                                    <p:column headerText="CPF/CNPJ" style="width: 150px;">
                                        <h:outputText value="#{cli.documento}">
                                            <f:convertNumber type="pattern" pattern="###.###.###-##" rendered="#{cli.tipoDocumento == 'F'}"/>
                                            <f:convertNumber type="pattern" pattern="##.###.###/####-##" rendered="#{cli.tipoDocumento == 'J'}"/>
                                        </h:outputText>
                                    </p:column>
                                    
                                    <p:column headerText="Município">
                                        <h:outputText value="#{cli.xmun}"/>
                                    </p:column>
                                    
                                    <p:column headerText="UF" style="width: 50px;">
                                        <h:outputText value="#{cli.uf}"/>
                                    </p:column>
                                    
                                    <p:column headerText="Ações" style="width: 120px;">
                                        <p:commandButton icon="pi pi-pencil" title="Editar" 
                                                       action="#{clienteController.editarCliente}"
                                                       update=":form:grid,:form:formPanel"/>
                                        <p:commandButton icon="pi pi-trash" title="Excluir" 
                                                       style="margin-left: 5px;"
                                                       action="#{clienteController.excluirCliente}"
                                                       update=":form:grid,:form:growl"/>
                                    </p:column>
                                </p:dataTable>
                            </p:outputPanel>
                            
                            <!-- Formulário de Cliente -->
                            <p:outputPanel id="formPanel" rendered="#{clienteController.editando}">
                                <p:panel header="Cliente" style="margin-bottom: 20px;">
                                    <div class="p-fluid">
                                        <div class="p-grid">
                                            <div class="p-col-12">
                                                <p:outputLabel for="xnome" value="Nome/Razão Social *"/>
                                                <p:inputText id="xnome" value="#{clienteController.cliente.xnome}" 
                                                           required="true" requiredMessage="Nome é obrigatório"
                                                           style="width: 100%"/>
                                            </div>
                                            
                                            <div class="p-col-12 p-md-6">
                                                <p:outputLabel for="cpf" value="CPF"/>
                                                <p:inputMask id="cpf" value="#{clienteController.cliente.cpf}" 
                                                            mask="999.999.999-99" 
                                                            style="width: 100%"/>
                                            </div>
                                            
                                            <div class="p-col-12 p-md-6">
                                                <p:outputLabel for="cnpj" value="CNPJ"/>
                                                <p:inputMask id="cnpj" value="#{clienteController.cliente.cnpj}" 
                                                            mask="99.999.999/9999-99" 
                                                            style="width: 100%"/>
                                            </div>
                                            
                                            <div class="p-col-12 p-md-6">
                                                <p:outputLabel for="ie" value="Inscrição Estadual"/>
                                                <p:inputText id="ie" value="#{clienteController.cliente.ie}" 
                                                           style="width: 100%"/>
                                            </div>
                                            
                                            <div class="p-col-12 p-md-6">
                                                <p:outputLabel for="indIEDest" value="Indicador IE"/>
                                                <p:selectOneMenu id="indIEDest" value="#{clienteController.cliente.indIEDest}" 
                                                               style="width: 100%">
                                                    <f:selectItem itemLabel="Contribuinte ICMS" itemValue="1"/>
                                                    <f:selectItem itemLabel="Contribuinte Isento" itemValue="2"/>
                                                    <f:selectItem itemLabel="Não Contribuinte" itemValue="9"/>
                                                </p:selectOneMenu>
                                            </div>
                                        </div>
                                        
                                        <p:panel header="Endereço" toggleable="true" collapsed="false">
                                            <div class="p-grid">
                                                <div class="p-col-12 p-md-8">
                                                    <p:outputLabel for="xlgr" value="Logradouro *"/>
                                                    <p:inputText id="xlgr" value="#{clienteController.cliente.xlgr}" 
                                                               required="true" requiredMessage="Logradouro é obrigatório"
                                                               style="width: 100%"/>
                                                </div>
                                                
                                                <div class="p-col-12 p-md-4">
                                                    <p:outputLabel for="nro" value="Número *"/>
                                                    <p:inputText id="nro" value="#{clienteController.cliente.nro}" 
                                                               required="true" requiredMessage="Número é obrigatório"
                                                               style="width: 100%"/>
                                                </div>
                                                
                                                <div class="p-col-12">
                                                    <p:outputLabel for="xcpl" value="Complemento"/>
                                                    <p:inputText id="xcpl" value="#{clienteController.cliente.xcpl}" 
                                                               style="width: 100%"/>
                                                </div>
                                                
                                                <div class="p-col-12 p-md-6">
                                                    <p:outputLabel for="xbairro" value="Bairro *"/>
                                                    <p:inputText id="xbairro" value="#{clienteController.cliente.xbairro}" 
                                                               required="true" requiredMessage="Bairro é obrigatório"
                                                               style="width: 100%"/>
                                                </div>
                                                
                                                <div class="p-col-12 p-md-6">
                                                    <p:outputLabel for="cmun" value="Código Município *"/>
                                                    <p:inputText id="cmun" value="#{clienteController.cliente.cmun}" 
                                                               required="true" requiredMessage="Código município é obrigatório"
                                                               style="width: 100%"/>
                                                </div>
                                                
                                                <div class="p-col-12 p-md-6">
                                                    <p:outputLabel for="xmun" value="Município *"/>
                                                    <p:inputText id="xmun" value="#{clienteController.cliente.xmun}" 
                                                               required="true" requiredMessage="Município é obrigatório"
                                                               style="width: 100%"/>
                                                </div>
                                                
                                                <div class="p-col-12 p-md-3">
                                                    <p:outputLabel for="uf" value="UF *"/>
                                                    <p:selectOneMenu id="uf" value="#{clienteController.cliente.uf}" 
                                                                   required="true" requiredMessage="UF é obrigatória"
                                                                   style="width: 100%">
                                                        <f:selectItems value="#{['AC','AL','AM','AP','BA','CE','DF','ES','GO','MA','MG','MS','MT','PA','PB','PE','PI','PR','RJ','RN','RO','RR','RS','SC','SE','SP','TO']}" 
                                                                     var="estado" itemLabel="#{estado}" itemValue="#{estado}"/>
                                                    </p:selectOneMenu>
                                                </div>
                                                
                                                <div class="p-col-12 p-md-3">
                                                    <p:outputLabel for="cep" value="CEP *"/>
                                                    <p:inputMask id="cep" value="#{clienteController.cliente.cep}" 
                                                                mask="99999-999" 
                                                                required="true" requiredMessage="CEP é obrigatório"
                                                                style="width: 100%"/>
                                                </div>
                                                
                                                <div class="p-col-12 p-md-6">
                                                    <p:outputLabel for="fone" value="Telefone"/>
                                                    <p:inputMask id="fone" value="#{clienteController.cliente.fone}" 
                                                                mask="(99) 99999-9999" 
                                                                style="width: 100%"/>
                                                </div>
                                                
                                                <div class="p-col-12 p-md-6">
                                                    <p:outputLabel for="email" value="Email"/>
                                                    <p:inputText id="email" value="#{clienteController.cliente.email}" 
                                                               style="width: 100%"/>
                                                </div>
                                            </div>
                                        </p:panel>
                                        
                                        <div class="p-d-flex p-jc-end p-mt-3">
                                            <p:commandButton value="Salvar" icon="pi pi-check" 
                                                           action="#{clienteController.salvarCliente}"
                                                           update=":form:grid,:form:formPanel,:form:growl"/>
                                            <p:commandButton value="Cancelar" icon="pi pi-times" 
                                                           action="#{clienteController.cancelarEdicao}"
                                                           style="margin-left: 5px;"
                                                           update=":form:grid,:form:formPanel"/>
                                        </div>
                                    </div>
                                </p:panel>
                            </p:outputPanel>
                        </f:facet>
                    </p:card>
                </h:form>
            </div>
        </ui:define>
    </ui:composition>
</html>
```

**Vendas (vendas.xhtml)**
```xml
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html"
      xmlns:f="http://xmlns.jcp.org/jsf/core"
      xmlns:p="http://primefaces.org/ui"
      xmlns:ui="http://xmlns.jcp.org/jsf/facelets">
    
    <ui:composition template="template.xhtml">
        <ui:define name="content">
            <div class="p-p-4">
                <h:form id="form">
                    <p:card>
                        <f:facet name="title">
                            <div class="p-d-flex p-ai-center p-jc-between">
                                <div>
                                    <i class="pi pi-shopping-cart p-mr-2"></i>
                                    <span>Vendas e Emissão de NF-e</span>
                                </div>
                                <p:commandButton value="Nova Venda" icon="pi pi-plus" 
                                               action="#{vendaController.novaVenda}" 
                                               update=":form:grid,:form:formPanel"
                                               rendered="#{!vendaController.editando}"/>
                            </div>
                        </f:facet>
                        
                        <f:facet name="content">
                            <!-- Grid de Vendas -->
                            <p:outputPanel id="grid" rendered="#{!vendaController.editando}">
                                <p:dataTable id="vendasTable" var="venda" value="#{vendaController.vendas}" 
                                           paginator="true" rows="10" 
                                           emptyMessage="Nenhuma venda emitida">
                                    
                                    <p:column headerText="Número NF-e" style="width: 100px;">
                                        <h:outputText value="#{venda.numeroNFe}"/>
                                    </p:column>
                                    
                                    <p:column headerText="Cliente">
                                        <h:outputText value="#{venda.cliente.xnome}"/>
                                    </p:column>
                                    
                                    <p:column headerText="Data" style="width: 150px;">
                                        <h:outputText value="#{venda.dataVenda}">
                                            <f:convertDateTime pattern="dd/MM/yyyy HH:mm"/>
                                        </h:outputText>
                                    </p:column>
                                    
                                    <p:column headerText="Valor Total" style="width: 120px;">
                                        <h:outputText value="#{venda.valorTotal}">
                                            <f:convertNumber type="currency" currencyCode="BRL"/>
                                        </h:outputText>
                                    </p:column>
                                    
                                    <p:column headerText="Status" style="width: 100px;">
                                        <p:tag value="#{venda.status}" 
                                             severity="#{venda.status == 'EMITIDA' ? 'success' : venda.status == 'PENDENTE' ? 'warning' : 'danger'}"/>
                                    </p:column>
                                    
                                    <p:column headerText="Chave NF-e" style="width: 200px;">
                                        <h:outputText value="#{venda.chaveNFe}" 
                                                    rendered="#{venda.chaveNFe != null}"/>
                                        <h:outputText value="-" rendered="#{venda.chaveNFe == null}"/>
                                    </p:column>
                                </p:dataTable>
                            </p:outputPanel>
                            
                            <!-- Formulário de Venda -->
                            <p:outputPanel id="formPanel" rendered="#{vendaController.editando}">
                                <p:panel header="Nova Venda" style="margin-bottom: 20px;">
                                    <div class="p-fluid">
                                        <div class="p-grid">
                                            <div class="p-col-12">
                                                <p:outputLabel for="cliente" value="Cliente *"/>
                                                <p:selectOneMenu id="cliente" value="#{vendaController.venda.cliente}" 
                                                               required="true" requiredMessage="Selecione um cliente"
                                                               style="width: 100%">
                                                    <f:selectItem itemLabel="Selecione um cliente" itemValue="#{null}"/>
                                                    <f:selectItems value="#{vendaController.clientes}" var="cli" 
                                                                 itemLabel="#{cli.xnome} - #{cli.documento}" 
                                                                 itemValue="#{cli}"/>
                                                </p:selectOneMenu>
                                            </div>
                                        </div>
                                        
                                        <p:panel header="Itens da Venda" style="margin: 20px 0;">
                                            <div class="p-grid">
                                                <div class="p-col-12 p-md-6">
                                                    <p:outputLabel for="produto" value="Produto"/>
                                                    <p:selectOneMenu id="produto" value="#{vendaController.produtoSelecionado}" 
                                                                   style="width: 100%">
                                                        <f:selectItem itemLabel="Selecione um produto" itemValue="#{null}"/>
                                                        <f:selectItems value="#{vendaController.produtos}" var="prod" 
                                                                     itemLabel="#{prod.xprod} - R$ #{prod.vuncom}" 
                                                                     itemValue="#{prod}"/>
                                                    </p:selectOneMenu>
                                                </div>
                                                
                                                <div class="p-col-12 p-md-4">
                                                    <p:outputLabel for="quantidade" value="Quantidade"/>
                                                    <p:inputNumber id="quantidade" value="#{vendaController.itemVenda.quantidade}" 
                                                                 minValue="0.001" decimalPlaces="3"
                                                                 style="width: 100%"/>
                                                </div>
                                                
                                                <div class="p-col-12 p-md-2" style="display: flex; align-items: flex-end;">
                                                    <p:commandButton value="Adicionar" icon="pi pi-plus" 
                                                                   action="#{vendaController.adicionarItem}"
                                                                   style="width: 100%;"
                                                                   update=":form:vendaItens"/>
                                                </div>
                                            </div>
                                            
                                            <p:dataTable id="vendaItens" var="item" value="#{vendaController.venda.itens}" 
                                                        emptyMessage="Nenhum item adicionado" style="margin-top: 20px;">
                                                
                                                <p:column headerText="Produto">
                                                    <h:outputText value="#{item.produto.xprod}"/>
                                                </p:column>
                                                
                                                <p:column headerText="Quantidade" style="width: 100px;">
                                                    <h:outputText value="#{item.quantidade}">
                                                        <f:convertNumber pattern="0.###"/>
                                                    </h:outputText>
                                                </p:column>
                                                
                                                <p:column headerText="Valor Unitário" style="width: 120px;">
                                                    <h:outputText value="#{item.valorUnitario}">
                                                        <f:convertNumber type="currency" currencyCode="BRL"/>
                                                    </h:outputText>
                                                </p:column>
                                                
                                                <p:column headerText="Valor Total" style="width: 120px;">
                                                    <h:outputText value="#{item.valorTotal}">
                                                        <f:convertNumber type="currency" currencyCode="BRL"/>
                                                    </h:outputText>
                                                </p:column>
                                                
                                                <p:column headerText="Ações" style="width: 80px;">
                                                    <p:commandButton icon="pi pi-trash" title="Remover" 
                                                                   action="#{vendaController.removerItem(item)}"
                                                                   update=":form:vendaItens"/>
                                                </p:column>
                                            </p:dataTable>
                                            
                                            <div class="p-d-flex p-jc-end p-mt-3">
                                                <h:outputText value="Total: " style="font-weight: bold; margin-right: 10px;"/>
                                                <h:outputText value="#{vendaController.venda.valorTotal}" 
                                                            style="font-weight: bold; font-size: 1.2rem;">
                                                    <f:convertNumber type="currency" currencyCode="BRL"/>
                                                </h:outputText>
                                            </div>
                                        </p:panel>
                                        
                                        <div class="p-d-flex p-jc-end p-mt-3">
                                            <p:commandButton value="Finalizar Venda e Emitir NF-e" icon="pi pi-check" 
                                                           action="#{vendaController.finalizarVenda}"
                                                           style="background: #4CAF50; border: none;"
                                                           update=":form:grid,:form:formPanel,:form:growl"/>
                                            <p:commandButton value="Cancelar" icon="pi pi-times" 
                                                           action="#{vendaController.cancelarVenda}"
                                                           style="margin-left: 5px;"
                                                           update=":form:grid,:form:formPanel"/>
                                        </div>
                                    </div>
                                </p:panel>
                            </p:outputPanel>
                        </f:facet>
                    </p:card>
                </h:form>
            </div>
        </ui:define>
    </ui:composition>
</html>
```

**Empresa (empresa.xhtml)**
```xml
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html"
      xmlns:f="http://xmlns.jcp.org/jsf/core"
      xmlns:p="http://primefaces.org/ui"
      xmlns:ui="http://xmlns.jcp.org/jsf/facelets">
    
    <ui:composition template="template.xhtml">
        <ui:define name="content">
            <div class="p-p-4">
                <h:form id="form">
                    <p:card>
                        <f:facet name="title">
                            <div class="p-d-flex p-ai-center">
                                <i class="pi pi-building p-mr-2"></i>
                                <span>Dados da Empresa</span>
                            </div>
                        </f:facet>
                        
                        <f:facet name="content">
                            <div class="p-fluid">
                                <div class="p-grid">
                                    <div class="p-col-12 p-md-6">
                                        <p:outputLabel for="cnpj" value="CNPJ *"/>
                                        <p:inputMask id="cnpj" value="#{empresaController.empresa.cnpj}" 
                                                    mask="99.999.999/9999-99" 
                                                    required="true" requiredMessage="CNPJ é obrigatório"
                                                    style="width: 100%"/>
                                    </div>
                                    
                                    <div class="p-col-12 p-md-6">
                                        <p:outputLabel for="ie" value="Inscrição Estadual *"/>
                                        <p:inputText id="ie" value="#{empresaController.empresa.ie}" 
                                                   required="true" requiredMessage="IE é obrigatória"
                                                   style="width: 100%"/>
                                    </div>
                                    
                                    <div class="p-col-12">
                                        <p:outputLabel for="xnome" value="Razão Social *"/>
                                        <p:inputText id="xnome" value="#{empresaController.empresa.xnome}" 
                                                   required="true" requiredMessage="Razão Social é obrigatória"
                                                   style="width: 100%"/>
                                    </div>
                                    
                                    <div class="p-col-12">
                                        <p:outputLabel for="xfant" value="Nome Fantasia *"/>
                                        <p:inputText id="xfant" value="#{empresaController.empresa.xfant}" 
                                                   required="true" requiredMessage="Nome Fantasia é obrigatório"
                                                   style="width: 100%"/>
                                    </div>
                                    
                                    <div class="p-col-12 p-md-4">
                                        <p:outputLabel for="crt" value="Regime Tributário *"/>
                                        <p:selectOneMenu id="crt" value="#{empresaController.empresa.crt}" 
                                                       required="true" requiredMessage="CRT é obrigatório"
                                                       style="width: 100%">
                                            <f:selectItem itemLabel="1 - Simples Nacional" itemValue="1"/>
                                            <f:selectItem itemLabel="2 - Simples Nacional - Excesso" itemValue="2"/>
                                            <f:selectItem itemLabel="3 - Regime Normal" itemValue="3"/>
                                        </p:selectOneMenu>
                                    </div>
                                </div>
                                
                                <p:panel header="Endereço" toggleable="true" collapsed="false">
                                    <div class="p-grid">
                                        <div class="p-col-12 p-md-8">
                                            <p:outputLabel for="xlgr" value="Logradouro *"/>
                                            <p:inputText id="xlgr" value="#{empresaController.empresa.xlgr}" 
                                                       required="true" requiredMessage="Logradouro é obrigatório"
                                                       style="width: 100%"/>
                                        </div>
                                        
                                        <div class="p-col-12 p-md-4">
                                            <p:outputLabel for="nro" value="Número *"/>
                                            <p:inputText id="nro" value="#{empresaController.empresa.nro}" 
                                                       required="true" requiredMessage="Número é obrigatório"
                                                       style="width: 100%"/>
                                        </div>
                                        
                                        <div class="p-col-12">
                                            <p:outputLabel for="xcpl" value="Complemento"/>
                                            <p:inputText id="xcpl" value="#{empresaController.empresa.xcpl}" 
                                                       style="width: 100%"/>
                                        </div>
                                        
                                        <div class="p-col-12 p-md-6">
                                            <p:outputLabel for="xbairro" value="Bairro *"/>
                                            <p:inputText id="xbairro" value="#{empresaController.empresa.xbairro}" 
                                                       required="true" requiredMessage="Bairro é obrigatório"
                                                       style="width: 100%"/>
                                        </div>
                                        
                                        <div class="p-col-12 p-md-6">
                                            <p:outputLabel for="cmun" value="Código Município *"/>
                                            <p:inputText id="cmun" value="#{empresaController.empresa.cmun}" 
                                                       required="true" requiredMessage="Código município é obrigatório"
                                                       style="width: 100%"/>
                                        </div>
                                        
                                        <div class="p-col-12 p-md-6">
                                            <p:outputLabel for="xmun" value="Município *"/>
                                            <p:inputText id="xmun" value="#{empresaController.empresa.xmun}" 
                                                       required="true" requiredMessage="Município é obrigatório"
                                                       style="width: 100%"/>
                                        </div>
                                        
                                        <div class="p-col-12 p-md-3">
                                            <p:outputLabel for="uf" value="UF *"/>
                                            <p:selectOneMenu id="uf" value="#{empresaController.empresa.uf}" 
                                                           required="true" requiredMessage="UF é obrigatória"
                                                           style="width: 100%">
                                                <f:selectItems value="#{['AC','AL','AM','AP','BA','CE','DF','ES','GO','MA','MG','MS','MT','PA','PB','PE','PI','PR','RJ','RN','RO','RR','RS','SC','SE','SP','TO']}" 
                                                             var="estado" itemLabel="#{estado}" itemValue="#{estado}"/>
                                            </p:selectOneMenu>
                                        </div>
                                        
                                        <div class="p-col-12 p-md-3">
                                            <p:outputLabel for="cep" value="CEP *"/>
                                            <p:inputMask id="cep" value="#{empresaController.empresa.cep}" 
                                                        mask="99999-999" 
                                                        required="true" requiredMessage="CEP é obrigatório"
                                                        style="width: 100%"/>
                                        </div>
                                        
                                        <div class="p-col-12 p-md-6">
                                            <p:outputLabel for="fone" value="Telefone"/>
                                            <p:inputMask id="fone" value="#{empresaController.empresa.fone}" 
                                                        mask="(99) 99999-9999" 
                                                        style="width: 100%"/>
                                        </div>
                                        
                                        <div class="p-col-12 p-md-6">
                                            <p:outputLabel for="email" value="Email"/>
                                            <p:inputText id="email" value="#{empresaController.empresa.email}" 
                                                       style="width: 100%"/>
                                        </div>
                                    </div>
                                </p:panel>
                                
                                <div class="p-d-flex p-jc-end p-mt-3">
                                    <p:commandButton value="Salvar" icon="pi pi-check" 
                                                   action="#{empresaController.salvarEmpresa}"
                                                   update=":form:growl"/>
                                </div>
                            </div>
                        </f:facet>
                    </p:card>
                </h:form>
            </div>
        </ui:define>
    </ui:composition>
</html>
```

**Configuração (configuracao.xhtml)**
```xml
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html"
      xmlns:f="http://xmlns.jcp.org/jsf/core"
      xmlns:p="http://primefaces.org/ui"
      xmlns:ui="http://xmlns.jcp.org/jsf/facelets">
    
    <ui:composition template="template.xhtml">
        <ui:define name="content">
            <div class="p-p-4">
                <h:form id="form">
                    <p:card>
                        <f:facet name="title">
                            <div class="p-d-flex p-ai-center">
                                <i class="pi pi-cog p-mr-2"></i>
                                <span>Configurações do Sistema</span>
                            </div>
                        </f:facet>
                        
                        <f:facet name="content">
                            <div class="p-fluid">
                                <p:panel header="Configurações de Ambiente" toggleable="true" collapsed="false">
                                    <div class="p-grid">
                                        <div class="p-col-12 p-md-6">
                                            <p:outputLabel for="webserviceUF" value="UF do WebService *"/>
                                            <p:selectOneMenu id="webserviceUF" value="#{configuracaoController.configuracao.webserviceUF}" 
                                                           required="true" requiredMessage="UF é obrigatória"
                                                           style="width: 100%">
                                                <f:selectItems value="#{['AC','AL','AM','AP','BA','CE','DF','ES','GO','MA','MG','MS','MT','PA','PB','PE','PI','PR','RJ','RN','RO','RR','RS','SC','SE','SP','TO']}" 
                                                             var="estado" itemLabel="#{estado}" itemValue="#{estado}"/>
                                            </p:selectOneMenu>
                                        </div>
                                        
                                        <div class="p-col-12 p-md-6">
                                            <p:outputLabel for="emitenteUF" value="UF do Emitente *"/>
                                            <p:selectOneMenu id="emitenteUF" value="#{configuracaoController.configuracao.emitenteUF}" 
                                                           required="true" requiredMessage="UF é obrigatória"
                                                           style="width: 100%">
                                                <f:selectItems value="#{['AC','AL','AM','AP','BA','CE','DF','ES','GO','MA','MG','MS','MT','PA','PB','PE','PI','PR','RJ','RN','RO','RR','RS','SC','SE','SP','TO']}" 
                                                             var="estado" itemLabel="#{estado}" itemValue="#{estado}"/>
                                            </p:selectOneMenu>
                                        </div>
                                        
                                        <div class="p-col-12 p-md-6">
                                            <p:outputLabel for="webserviceAmbiente" value="Ambiente *"/>
                                            <p:selectOneMenu id="webserviceAmbiente" value="#{configuracaoController.configuracao.webserviceAmbiente}" 
                                                           required="true" requiredMessage="Ambiente é obrigatório"
                                                           style="width: 100%">
                                                <f:selectItem itemLabel="1 - Produção" itemValue="1"/>
                                                <f:selectItem itemLabel="2 - Homologação" itemValue="2"/>
                                            </p:selectOneMenu>
                                        </div>
                                        
                                        <div class="p-col-12 p-md-6">
                                            <p:outputLabel for="portaServidor" value="Porta do Servidor *"/>
                                            <p:inputText id="portaServidor" value="#{configuracaoController.configuracao.portaServidor}" 
                                                       required="true" requiredMessage="Porta é obrigatória"
                                                       style="width: 100%"/>
                                        </div>
                                    </div>
                                </p:panel>
                                
                                <p:panel header="Configurações de Emissão" toggleable="true" collapsed="false">
                                    <div class="p-grid">
                                        <div class="p-col-12 p-md-4">
                                            <p:outputLabel for="serieNFe" value="Série NF-e *"/>
                                            <p:inputText id="serieNFe" value="#{configuracaoController.configuracao.serieNFe}" 
                                                       required="true" requiredMessage="Série é obrigatória"
                                                       style="width: 100%"/>
                                        </div>
                                        
                                        <div class="p-col-12 p-md-4">
                                            <p:outputLabel for="finalidadeEmissao" value="Finalidade Emissão *"/>
                                            <p:selectOneMenu id="finalidadeEmissao" value="#{configuracaoController.configuracao.finalidadeEmissao}" 
                                                           required="true" requiredMessage="Finalidade é obrigatória"
                                                           style="width: 100%">
                                                <f:selectItem itemLabel="1 - Normal" itemValue="1"/>
                                                <f:selectItem itemLabel="2 - Complementar" itemValue="2"/>
                                                <f:selectItem itemLabel="3 - Ajuste" itemValue="3"/>
                                                <f:selectItem itemLabel="4 - Devolução" itemValue="4"/>
                                            </p:selectOneMenu>
                                        </div>
                                        
                                        <div class="p-col-12 p-md-4">
                                            <p:outputLabel for="consumidorFinal" value="Consumidor Final *"/>
                                            <p:selectOneMenu id="consumidorFinal" value="#{configuracaoController.configuracao.consumidorFinal}" 
                                                           required="true" requiredMessage="Consumidor final é obrigatório"
                                                           style="width: 100%">
                                                <f:selectItem itemLabel="0 - Normal" itemValue="0"/>
                                                <f:selectItem itemLabel="1 - Consumidor Final" itemValue="1"/>
                                            </p:selectOneMenu>
                                        </div>
                                        
                                        <div class="p-col-12">
                                            <p:outputLabel for="naturezaOperacao" value="Natureza da Operação *"/>
                                            <p:inputText id="naturezaOperacao" value="#{configuracaoController.configuracao.naturezaOperacao}" 
                                                       required="true" requiredMessage="Natureza é obrigatória"
                                                       style="width: 100%"/>
                                        </div>
                                        
                                        <div class="p-col-12 p-md-6">
                                            <p:outputLabel for="presencaComprador" value="Presença Comprador *"/>
                                            <p:selectOneMenu id="presencaComprador" value="#{configuracaoController.configuracao.presencaComprador}" 
                                                           required="true" requiredMessage="Presença é obrigatória"
                                                           style="width: 100%">
                                                <f:selectItem itemLabel="0 - Não se aplica" itemValue="0"/>
                                                <f:selectItem itemLabel="1 - Operação presencial" itemValue="1"/>
                                                <f:selectItem itemLabel="2 - Operação não presencial, Internet" itemValue="2"/>
                                                <f:selectItem itemLabel="3 - Operação não presencial, Teleatendimento" itemValue="3"/>
                                                <f:selectItem itemLabel="4 - NFC-e entrega em domicílio" itemValue="4"/>
                                                <f:selectItem itemLabel="5 - Operação presencial, fora do estabelecimento" itemValue="5"/>
                                                <f:selectItem itemLabel="9 - Operação não presencial, outros" itemValue="9"/>
                                            </p:selectOneMenu>
                                        </div>
                                        
                                        <div class="p-col-12 p-md-6">
                                            <p:outputLabel for="gerarPDF" value="Gerar PDF DANFE"/>
                                            <p:selectOneMenu id="gerarPDF" value="#{configuracaoController.configuracao.gerarPDF}" 
                                                           style="width: 100%">
                                                <f:selectItem itemLabel="Sim" itemValue="1"/>
                                                <f:selectItem itemLabel="Não" itemValue="0"/>
                                            </p:selectOneMenu>
                                        </div>
                                    </div>
                                </p:panel>
                                
                                <p:panel header="Configurações de Arquivos" toggleable="true" collapsed="false">
                                    <div class="p-grid">
                                        <div class="p-col-12">
                                            <p:outputLabel for="caminhoCertificado" value="Caminho do Certificado *"/>
                                            <p:inputText id="caminhoCertificado" value="#{configuracaoController.configuracao.caminhoCertificado}" 
                                                       required="true" requiredMessage="Caminho do certificado é obrigatório"
                                                       style="width: 100%"/>
                                        </div>
                                        
                                        <div class="p-col-12 p-md-6">
                                            <p:outputLabel for="senhaCertificado" value="Senha do Certificado *"/>
                                            <p:password id="senhaCertificado" value="#{configuracaoController.configuracao.senhaCertificado}" 
                                                      feedback="false" required="true" 
                                                      requiredMessage="Senha do certificado é obrigatória"
                                                      style="width: 100%"/>
                                        </div>
                                        
                                        <div class="p-col-12 p-md-6">
                                            <p:outputLabel for="caminhoSchemas" value="Caminho dos Schemas *"/>
                                            <p:inputText id="caminhoSchemas" value="#{configuracaoController.configuracao.caminhoSchemas}" 
                                                       required="true" requiredMessage="Caminho dos schemas é obrigatório"
                                                       style="width: 100%"/>
                                        </div>
                                        
                                        <div class="p-col-12">
                                            <p:outputLabel for="caminhoXML" value="Caminho dos XMLs *"/>
                                            <p:inputText id="caminhoXML" value="#{configuracaoController.configuracao.caminhoXML}" 
                                                       required="true" requiredMessage="Caminho dos XMLs é obrigatório"
                                                       style="width: 100%"/>
                                        </div>
                                    </div>
                                </p:panel>
                                
                                <div class="p-d-flex p-jc-end p-mt-3">
                                    <p:commandButton value="Salvar" icon="pi pi-check" 
                                                   action="#{configuracaoController.salvarConfiguracao}"
                                                   update=":form:growl"/>
                                </div>
                            </div>
                        </f:facet>
                    </p:card>
                </h:form>
            </div>
        </ui:define>
    </ui:composition>
</html>
```

**Usuários (usuarios.xhtml)**
```xml
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html"
      xmlns:f="http://xmlns.jcp.org/jsf/core"
      xmlns:p="http://primefaces.org/ui"
      xmlns:ui="http://xmlns.jcp.org/jsf/facelets">
    
    <ui:composition template="template.xhtml">
        <ui:define name="content">
            <div class="p-p-4">
                <h:form id="form">
                    <p:card>
                        <f:facet name="title">
                            <div class="p-d-flex p-ai-center p-jc-between">
                                <div>
                                    <i class="pi pi-user p-mr-2"></i>
                                    <span>Cadastro de Usuários</span>
                                </div>
                                <p:commandButton value="Novo Usuário" icon="pi pi-plus" 
                                               action="#{usuarioController.novoUsuario}" 
                                               update=":form:grid,:form:formPanel"
                                               rendered="#{!usuarioController.editando}"/>
                            </div>
                        </f:facet>
                        
                        <f:facet name="content">
                            <!-- Grid de Usuários -->
                            <p:outputPanel id="grid" rendered="#{!usuarioController.editando}">
                                <p:dataTable id="usuariosTable" var="user" value="#{usuarioController.usuarios}" 
                                           paginator="true" rows="10" 
                                           selection="#{usuarioController.usuarioSelecionado}"
                                           selectionMode="single"
                                           emptyMessage="Nenhum usuário cadastrado">
                                    
                                    <p:column headerText="Nome">
                                        <h:outputText value="#{user.nome}"/>
                                    </p:column>
                                    
                                    <p:column headerText="Login">
                                        <h:outputText value="#{user.login}"/>
                                    </p:column>
                                    
                                    <p:column headerText="Email">
                                        <h:outputText value="#{user.email}"/>
                                    </p:column>
                                    
                                    <p:column headerText="Perfil" style="width: 100px;">
                                        <p:tag value="#{user.perfil}" 
                                             severity="#{user.perfil == 'ADMIN' ? 'danger' : 'info'}"/>
                                    </p:column>
                                    
                                    <p:column headerText="Status" style="width: 100px;">
                                        <p:tag value="#{user.ativo ? 'Ativo' : 'Inativo'}" 
                                             severity="#{user.ativo ? 'success' : 'secondary'}"/>
                                    </p:column>
                                    
                                    <p:column headerText="Ações" style="width: 120px;">
                                        <p:commandButton icon="pi pi-pencil" title="Editar" 
                                                       action="#{usuarioController.editarUsuario}"
                                                       update=":form:grid,:form:formPanel"/>
                                        <p:commandButton icon="pi pi-trash" title="Excluir" 
                                                       style="margin-left: 5px;"
                                                       action="#{usuarioController.excluirUsuario}"
                                                       update=":form:grid,:form:growl"/>
                                    </p:column>
                                </p:dataTable>
                            </p:outputPanel>
                            
                            <!-- Formulário de Usuário -->
                            <p:outputPanel id="formPanel" rendered="#{usuarioController.editando}">
                                <p:panel header="Usuário" style="margin-bottom: 20px;">
                                    <div class="p-fluid">
                                        <div class="p-grid">
                                            <div class="p-col-12">
                                                <p:outputLabel for="nome" value="Nome Completo *"/>
                                                <p:inputText id="nome" value="#{usuarioController.usuario.nome}" 
                                                           required="true" requiredMessage="Nome é obrigatório"
                                                           style="width: 100%"/>
                                            </div>
                                            
                                            <div class="p-col-12 p-md-6">
                                                <p:outputLabel for="login" value="Login *"/>
                                                <p:inputText id="login" value="#{usuarioController.usuario.login}" 
                                                           required="true" requiredMessage="Login é obrigatório"
                                                           style="width: 100%"/>
                                            </div>
                                            
                                            <div class="p-col-12 p-md-6">
                                                <p:outputLabel for="email" value="Email *"/>
                                                <p:inputText id="email" value="#{usuarioController.usuario.email}" 
                                                           required="true" requiredMessage="Email é obrigatório"
                                                           style="width: 100%"/>
                                            </div>
                                            
                                            <div class="p-col-12 p-md-6">
                                                <p:outputLabel for="senha" value="Senha #{usuarioController.usuario.id == null ? '*' : ''}"/>
                                                <p:password id="senha" value="#{usuarioController.usuario.senha}" 
                                                          feedback="false" 
                                                          required="#{usuarioController.usuario.id == null}"
                                                          requiredMessage="Senha é obrigatória para novo usuário"
                                                          style="width: 100%"/>
                                            </div>
                                            
                                            <div class="p-col-12 p-md-6">
                                                <p:outputLabel for="confirmarSenha" value="Confirmar Senha #{usuarioController.usuario.id == null ? '*' : ''}"/>
                                                <p:password id="confirmarSenha" value="#{usuarioController.confirmarSenha}" 
                                                          feedback="false" 
                                                          required="#{usuarioController.usuario.id == null}"
                                                          requiredMessage="Confirmação de senha é obrigatória"
                                                          style="width: 100%"/>
                                            </div>
                                            
                                            <div class="p-col-12 p-md-6">
                                                <p:outputLabel for="perfil" value="Perfil *"/>
                                                <p:selectOneMenu id="perfil" value="#{usuarioController.usuario.perfil}" 
                                                               required="true" requiredMessage="Perfil é obrigatório"
                                                               style="width: 100%">
                                                    <f:selectItem itemLabel="Usuário" itemValue="USER"/>
                                                    <f:selectItem itemLabel="Administrador" itemValue="ADMIN"/>
                                                </p:selectOneMenu>
                                            </div>
                                            
                                            <div class="p-col-12 p-md-6">
                                                <p:outputLabel for="ativo" value="Status"/>
                                                <p:selectOneMenu id="ativo" value="#{usuarioController.usuario.ativo}" 
                                                               style="width: 100%">
                                                    <f:selectItem itemLabel="Ativo" itemValue="true"/>
                                                    <f:selectItem itemLabel="Inativo" itemValue="false"/>
                                                </p:selectOneMenu>
                                            </div>
                                        </div>
                                        
                                        <div class="p-d-flex p-jc-end p-mt-3">
                                            <p:commandButton value="Salvar" icon="pi pi-check" 
                                                           action="#{usuarioController.salvarUsuario}"
                                                           update=":form:grid,:form:formPanel,:form:growl"/>
                                            <p:commandButton value="Cancelar" icon="pi pi-times" 
                                                           action="#{usuarioController.cancelarEdicao}"
                                                           style="margin-left: 5px;"
                                                           update=":form:grid,:form:formPanel"/>
                                        </div>
                                    </div>
                                </p:panel>
                            </p:outputPanel>
                        </f:facet>
                    </p:card>
                </h:form>
            </div>
        </ui:define>
    </ui:composition>
</html>
```

## 9. Arquivo de Estilos CSS

**src/main/webapp/resources/css/styles.css**
```css
/* Estilos gerais */
body {
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    margin: 0;
    padding: 0;
    background-color: #f8f9fa;
}

/* Layout improvements */
.p-card {
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    border: 1px solid #e0e0e0;
}

.p-panel .p-panel-header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border: none;
}

.p-button {
    border-radius: 4px;
}

/* DataTable improvements */
.p-datatable .p-datatable-thead > tr > th {
    background: #f8f9fa;
    color: #495057;
    font-weight: 600;
}

/* Form improvements */
.p-field > label {
    font-weight: 500;
    margin-bottom: 0.25rem;
}

/* Login page specific */
.login-container {
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
}

/* Dashboard cards */
.dashboard-card {
    transition: transform 0.2s;
}

.dashboard-card:hover {
    transform: translateY(-2px);
}

/* Responsive adjustments */
@media (max-width: 768px) {
    .p-grid {
        margin: 0;
    }
    
    .p-col-12 {
        padding: 0.5rem;
    }
}

/* Custom button colors */
.btn-primary {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
}

.btn-success {
    background: linear-gradient(135deg, #4CAF50 0%, #45a049 100%);
    border: none;
}

.btn-warning {
    background: linear-gradient(135deg, #ff9800 0%, #e68900 100%);
    border: none;
}

.btn-danger {
    background: linear-gradient(135deg, #f44336 0%, #d32f2f 100%);
    border: none;
}

/* Status badges */
.status-emitida {
    background: #4CAF50;
    color: white;
}

.status-pendente {
    background: #ff9800;
    color: white;
}

.status-erro {
    background: #f44336;
    color: white;
}

/* Loading spinner */
.loading-spinner {
    display: inline-block;
    width: 20px;
    height: 20px;
    border: 3px solid #f3f3f3;
    border-top: 3px solid #667eea;
    border-radius: 50%;
    animation: spin 1s linear infinite;
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}
```

## 10. Arquivo de Mensagens

**src/main/resources/messages_pt_BR.properties**
```properties
# Mensagens gerais
welcome=Bem-vindo
success=Sucesso
error=Erro
warning=Aviso
info=Informação

# Mensagens de validação
required.field=Campo obrigatório
invalid.email=Email inválido
invalid.cpf=CPF inválido
invalid.cnpj=CNPJ inválido

# Mensagens de operação
save.success=Registro salvo com sucesso
save.error=Erro ao salvar registro
delete.success=Registro excluído com sucesso
delete.error=Erro ao excluir registro
update.success=Registro atualizado com sucesso
update.error=Erro ao atualizar registro

# Mensagens específicas
login.success=Login realizado com sucesso
login.error=Login ou senha inválidos
logout.success=Logout realizado com sucesso
nfe.emit.success=NF-e emitida com sucesso
nfe.emit.error=Erro ao emitir NF-e
```

## 11. Script SQL para Dados Iniciais

**src/main/resources/data.sql**
```sql
-- Inserir usuário administrador padrão
INSERT INTO usuario (nome, login, email, senha, perfil, ativo) 
VALUES ('Administrador', 'admin', 'admin@empresa.com', 'admin123', 'ADMIN', true);

-- Inserir configuração padrão
INSERT INTO configuracao (webservice_uf, emitente_uf, webservice_ambiente, caminho_schemas, 
                         caminho_certificado, senha_certificado, caminho_xml, gerar_pdf, 
                         porta_servidor, serie_nfe, natureza_operacao, finalidade_emissao, 
                         consumidor_final, presenca_comprador) 
VALUES ('BA', 'BA', '2', '/home/schemas/', '/home/certificados/certificado.pfx', '123456', 
        '/home/xml', '1', '3435', '1', 'VENDA', '1', '1', '1');

-- Inserir empresa padrão (dados fictícios - deve ser preenchido pelo usuário)
INSERT INTO empresa (cnpj, xnome, xfant, ie, crt, xlgr, nro, xbairro, cmun, xmun, uf, cep, cpais, xpais) 
VALUES ('33630582000149', 'Editora Roberto LIEuiz Souza Monteiro', 'Editora Monteiro', 
        '158039716', '3', 'Rua Chile', 's/n', 'Centro', '2927408', 'Salvador', 'BA', 
        '40020000', '1058', 'Brasil');
```

## 12. Validação Final

O sistema implementado atende a todos os requisitos solicitados:

### ✅ Funcionalidades Implementadas:
1. **Cadastro de Produtos** - Com tags prod e imposto do XML
2. **Cadastro de Clientes** - Com tag dest do XML  
3. **Cadastro da Empresa** - Apenas 1 registro com tag emit
4. **Configurações** - Apenas 1 registro com dados de comunicação
5. **Cadastro de Usuários** - Com perfis ADMIN e USER
6. **Autenticação** - Login com email/senha
7. **Tela de Vendas** - Seleção de cliente, produtos, emissão NF-e
8. **Interface Moderna** - PrimeFaces com layout responsivo

### ✅ Características Técnicas:
- **JSF 4.0** + **PrimeFaces 12** + **Hibernate 6**
- **Padrão MVC** com controllers, DAOs e entities
- **Validação completa** em todos os formulários
- **Interface única** com grid/formulário dinâmico
- **Navegação** com template e menu
- **Segurança** com autenticação e autorização
- **Persistência** com JPA/Hibernate
- **Comunicação** com servidor NF-e existente

### ✅ Validações Específicas:
- Empresa e Configuração: apenas 1 registro, sem grid
- Grid/Formulário dinâmico em mesma página XHTML
- Validação de CPF/CNPJ, email, campos obrigatórios
- Cálculos automáticos de impostos e totais
- Geração de JSON para emissão NF-e
- Interface moderna e intuitiva
