-- =============================================
-- SISTEMA NF-E EMISSOR - MYSQL SCRIPT
-- Banco: MySQL 8.0+
-- Versão: 1.1
-- =============================================

CREATE DATABASE IF NOT EXISTS nfedb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE nfedb;

-- =============================================
-- LIMPEZA PARA REEXECUÇÃO SEGURA
-- =============================================

DROP VIEW IF EXISTS vw_itens_venda_detalhados;
DROP VIEW IF EXISTS vw_vendas_completas;
DROP VIEW IF EXISTS vw_clientes_ativos;
DROP VIEW IF EXISTS vw_produtos_ativos;

DROP TRIGGER IF EXISTS trigger_calcular_valor_item_venda;
DROP TRIGGER IF EXISTS trigger_calcular_valor_produto;

DROP TABLE IF EXISTS item_venda;
DROP TABLE IF EXISTS venda;
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS produto;
DROP TABLE IF EXISTS empresa;
DROP TABLE IF EXISTS configuracao;
DROP TABLE IF EXISTS usuario;

-- =============================================
-- TABELAS PRINCIPAIS
-- =============================================

CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(20) NOT NULL DEFAULT 'USER',
    ativo TINYINT(1) DEFAULT 1,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE empresa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cnpj VARCHAR(14) UNIQUE NOT NULL,
    xnome VARCHAR(200) NOT NULL,
    xfant VARCHAR(200) NOT NULL,
    ie VARCHAR(20) NOT NULL,
    crt VARCHAR(1) NOT NULL DEFAULT '3',
    xlgr VARCHAR(200) NOT NULL,
    nro VARCHAR(10) NOT NULL,
    xcpl VARCHAR(100),
    xbairro VARCHAR(100) NOT NULL,
    cmun VARCHAR(7) NOT NULL,
    xmun VARCHAR(100) NOT NULL,
    uf VARCHAR(2) NOT NULL,
    cep VARCHAR(8) NOT NULL,
    cpais VARCHAR(4) DEFAULT '1058',
    xpais VARCHAR(50) DEFAULT 'Brasil',
    fone VARCHAR(15),
    email VARCHAR(100),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE configuracao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    webservice_uf VARCHAR(2) NOT NULL DEFAULT 'BA',
    emitente_uf VARCHAR(2) NOT NULL DEFAULT 'BA',
    webservice_ambiente VARCHAR(1) NOT NULL DEFAULT '2',
    caminho_schemas VARCHAR(500) NOT NULL,
    caminho_certificado VARCHAR(500) NOT NULL,
    senha_certificado VARCHAR(100) NOT NULL,
    caminho_xml VARCHAR(500) NOT NULL,
    gerar_pdf VARCHAR(1) DEFAULT '1',
    porta_servidor VARCHAR(5) DEFAULT '3435',
    serie_nfe INT NOT NULL,
    numero_nfe INT NOT NULL DEFAULT 0,
    natureza_operacao VARCHAR(100) DEFAULT 'VENDA',
    finalidade_emissao VARCHAR(1) DEFAULT '1',
    consumidor_final VARCHAR(1) DEFAULT '1',
    presenca_comprador VARCHAR(1) DEFAULT '1',
    cst_rtc_padrao VARCHAR(3) DEFAULT '000',
    cclasstrib_rtc_padrao VARCHAR(6) DEFAULT '000001',
    pibs_uf_padrao DECIMAL(5,2) DEFAULT 0.10,
    pibs_mun_padrao DECIMAL(5,2) DEFAULT 0.00,
    pcbs_padrao DECIMAL(5,2) DEFAULT 0.90,
    tpag_padrao VARCHAR(2) DEFAULT '01',
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cprod VARCHAR(50) UNIQUE NOT NULL,
    cean VARCHAR(14),
    xprod VARCHAR(500) NOT NULL,
    ncm VARCHAR(8) NOT NULL,
    cest VARCHAR(7),
    cfop VARCHAR(4) NOT NULL,
    ucom VARCHAR(6) NOT NULL,
    qcom DECIMAL(15,4) DEFAULT 1.0000,
    vuncom DECIMAL(15,4) NOT NULL,
    vprod DECIMAL(15,2),
    ceantrib VARCHAR(14),
    utrib VARCHAR(6),
    qtrib DECIMAL(15,4) DEFAULT 1.0000,
    vuntrib DECIMAL(15,4),
    indtot VARCHAR(1) DEFAULT '1',
    orig VARCHAR(1) DEFAULT '0',
    cst_icms VARCHAR(3) DEFAULT '00',
    modbc_icms VARCHAR(1) DEFAULT '3',
    predbc_icms DECIMAL(5,2) DEFAULT 0.00,
    picms DECIMAL(5,2) DEFAULT 18.00,
    vbcst DECIMAL(15,2) DEFAULT 0.00,
    pst DECIMAL(5,2) DEFAULT 0.00,
    vicmsst DECIMAL(15,2) DEFAULT 0.00,
    vicmsdeson DECIMAL(15,2) DEFAULT 0.00,
    mot_des_icms VARCHAR(2) DEFAULT '9',
    vbcstret DECIMAL(15,2) DEFAULT 0.00,
    vicmssubstituto DECIMAL(15,2) DEFAULT 0.00,
    vicmsstret DECIMAL(15,2) DEFAULT 0.00,
    cst_pis VARCHAR(2) DEFAULT '01',
    ppis DECIMAL(5,2) DEFAULT 1.65,
    cst_cofins VARCHAR(2) DEFAULT '01',
    pcofins DECIMAL(5,2) DEFAULT 7.60,
    cst_rtc VARCHAR(3) DEFAULT '000',
    cclasstrib_rtc VARCHAR(6) DEFAULT '000001',
    pibs_uf DECIMAL(5,2) DEFAULT 0.10,
    pibs_mun DECIMAL(5,2) DEFAULT 0.00,
    pcbs DECIMAL(5,2) DEFAULT 0.90,
    ativo TINYINT(1) DEFAULT 1,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    xnome VARCHAR(200) NOT NULL,
    cpf VARCHAR(11),
    cnpj VARCHAR(14),
    ie VARCHAR(20),
    indiedest VARCHAR(1) DEFAULT '9',
    xlgr VARCHAR(200) NOT NULL,
    nro VARCHAR(10) NOT NULL,
    xcpl VARCHAR(100),
    xbairro VARCHAR(100) NOT NULL,
    cmun VARCHAR(7) NOT NULL,
    xmun VARCHAR(100) NOT NULL,
    uf VARCHAR(2) NOT NULL,
    cep VARCHAR(8) NOT NULL,
    fone VARCHAR(15),
    email VARCHAR(100),
    ativo TINYINT(1) DEFAULT 1,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE venda (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    data_venda TIMESTAMP NOT NULL,
    numero_nfe INT,
    chave_nfe VARCHAR(47),
    protocolo_nfe VARCHAR(50),
    status VARCHAR(20) DEFAULT 'PENDENTE',
    valor_total DECIMAL(15,2) DEFAULT 0.00,
    chave_pix VARCHAR(512),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_venda_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE item_venda (
    id INT AUTO_INCREMENT PRIMARY KEY,
    venda_id INT NOT NULL,
    produto_id INT NOT NULL,
    quantidade DECIMAL(15,4) NOT NULL DEFAULT 1.0000,
    valor_unitario DECIMAL(15,4) NOT NULL,
    valor_total DECIMAL(15,2) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_item_venda_venda FOREIGN KEY (venda_id) REFERENCES venda(id),
    CONSTRAINT fk_item_venda_produto FOREIGN KEY (produto_id) REFERENCES produto(id)
);

-- =============================================
-- ÍNDICES PARA PERFORMANCE
-- =============================================

CREATE INDEX idx_produto_cprod ON produto(cprod);
CREATE INDEX idx_produto_ativo ON produto(ativo);
CREATE INDEX idx_produto_ncm ON produto(ncm);

CREATE INDEX idx_cliente_documento ON cliente(cpf, cnpj);
CREATE INDEX idx_cliente_nome ON cliente(xnome);
CREATE INDEX idx_cliente_ativo ON cliente(ativo);

CREATE INDEX idx_venda_cliente ON venda(cliente_id);
CREATE INDEX idx_venda_data ON venda(data_venda);
CREATE INDEX idx_venda_status ON venda(status);
CREATE INDEX idx_venda_numero_nfe ON venda(numero_nfe);
CREATE INDEX idx_venda_chave_nfe ON venda(chave_nfe);

CREATE INDEX idx_item_venda_venda ON item_venda(venda_id);
CREATE INDEX idx_item_venda_produto ON item_venda(produto_id);

CREATE INDEX idx_usuario_login ON usuario(login);
CREATE INDEX idx_usuario_email ON usuario(email);
CREATE INDEX idx_usuario_ativo ON usuario(ativo);

-- =============================================
-- DADOS INICIAIS
-- =============================================

INSERT INTO usuario (nome, login, email, senha, perfil, ativo)
VALUES ('Administrador', 'admin', 'admin@empresa.com', 'admin123', 'ADMIN', 1);

INSERT INTO configuracao (
    webservice_uf, emitente_uf, webservice_ambiente,
    caminho_schemas, caminho_certificado, senha_certificado,
    caminho_xml, gerar_pdf, porta_servidor, serie_nfe, numero_nfe,
    natureza_operacao, finalidade_emissao, consumidor_final, presenca_comprador,
    cst_rtc_padrao, cclasstrib_rtc_padrao, pibs_uf_padrao, pibs_mun_padrao, pcbs_padrao, tpag_padrao
) VALUES (
    'BA', 'BA', '2',
    '/home/schemas/', '/home/certificados/certificado.pfx', '123456',
    '/home/xml', '1', '3435', 1, 0,
    'VENDA', '1', '1', '1',
    '000', '000001', 0.10, 0.00, 0.90, '01'
);

INSERT INTO empresa (
    cnpj, xnome, xfant, ie, crt,
    xlgr, nro, xcpl, xbairro, cmun, xmun, uf, cep, fone, email
) VALUES (
    '33630582000149',
    'Editora Roberto Luiz Souza Monteiro',
    'Editora Science Books',
    '158039716', '3',
    'Rua Chile', 's/n', 'Edifício Eduardo De Moraes, sala 606', 'Centro', '2927408', 'Salvador', 'BA',
    '40020000', '71991721874', 'roberto@souzamonteiro.com'
);

INSERT INTO produto (
    cprod, cean, xprod, ncm, cfop, ucom, vuncom,
    cest, orig, cst_icms, modbc_icms, predbc_icms, picms,
    vbcst, pst, vicmsst, vicmsdeson, mot_des_icms, vbcstret, vicmssubstituto, vicmsstret,
    cst_pis, ppis, cst_cofins, pcofins,
    cst_rtc, cclasstrib_rtc, pibs_uf, pibs_mun, pcbs
) VALUES
(
    '7898480650104', '7898480650104',
    'Óleo de cambio manual SAE 80W Flex Oil 1Ltl',
    '27101932', '5102', 'UN', 13.00,
    '0600500', '0', '00', '3', 0.00, 7.00,
    0.00, 0.00, 0.00, 0.00, '9', 0.00, 0.00, 0.00,
    '01', 1.65, '01', 7.60,
    '000', '000001', 0.10, 0.00, 0.90
),
(
    '7890647097098', '7890647097098',
    'Caneta esferográfica azul',
    '96081000', '5102', 'UN', 1.49,
    '1902700', '0', '00', '3', 0.00, 18.50,
    0.00, 0.00, 0.00, 0.00, '9', 0.00, 0.00, 0.00,
    '01', 1.65, '01', 7.60,
    '000', '000001', 0.10, 0.00, 0.90
);

INSERT INTO cliente (
    xnome, cpf, xlgr, nro, xcpl, xbairro, cmun, xmun, uf, cep, fone, email
) VALUES (
    'Andréia Rita da Silva',
    '40325635862',
    'Rua Tanhaçu', '11', 'Quadra 5', 'Ipitanga', '2919207', 'Lauro de Freitas', 'BA',
    '42706730', '71991965292', 'andreiarita2@gmail.com'
);

-- =============================================
-- TRIGGERS MYSQL
-- =============================================

DELIMITER //

CREATE TRIGGER trigger_calcular_valor_produto
BEFORE INSERT ON produto
FOR EACH ROW
BEGIN
    IF NEW.vuncom IS NOT NULL THEN
        SET NEW.vprod = NEW.vuncom * COALESCE(NEW.qcom, 1.0000);
    END IF;

    IF NEW.vuntrib IS NULL AND NEW.vuncom IS NOT NULL THEN
        SET NEW.vuntrib = NEW.vuncom;
    END IF;
END//

CREATE TRIGGER trigger_calcular_valor_item_venda
BEFORE INSERT ON item_venda
FOR EACH ROW
BEGIN
    SET NEW.valor_total = NEW.quantidade * NEW.valor_unitario;
END//

DELIMITER ;

-- =============================================
-- VIEWS ÚTEIS
-- =============================================

CREATE VIEW vw_produtos_ativos AS
SELECT id, cprod, xprod, ncm, vuncom, ativo
FROM produto
WHERE ativo = 1
ORDER BY xprod;

CREATE VIEW vw_clientes_ativos AS
SELECT id, xnome, COALESCE(cpf, cnpj) AS documento, xmun, uf, ativo
FROM cliente
WHERE ativo = 1
ORDER BY xnome;

CREATE VIEW vw_vendas_completas AS
SELECT v.id, v.data_venda, v.numero_nfe, v.chave_nfe, v.status, v.valor_total,
       c.xnome AS cliente_nome, c.cpf AS cliente_cpf, c.cnpj AS cliente_cnpj
FROM venda v
JOIN cliente c ON v.cliente_id = c.id
ORDER BY v.data_venda DESC;

CREATE VIEW vw_itens_venda_detalhados AS
SELECT iv.id, iv.venda_id, iv.produto_id, p.cprod, p.xprod, p.ncm,
       iv.quantidade, iv.valor_unitario, iv.valor_total
FROM item_venda iv
JOIN produto p ON iv.produto_id = p.id;

-- =============================================
-- PERMISSÕES
-- =============================================

-- A criação/alteração do usuário do banco deve ser feita pelo administrador do MySQL.
-- O usuário da aplicação já deve existir com acesso total ao schema nfedb.

-- =============================================
-- VALIDAÇÕES FINAIS
-- =============================================

SHOW TABLES;

SELECT 'usuario' AS tabela, COUNT(*) AS total FROM usuario
UNION ALL
SELECT 'empresa', COUNT(*) FROM empresa
UNION ALL
SELECT 'configuracao', COUNT(*) FROM configuracao
UNION ALL
SELECT 'produto', COUNT(*) FROM produto
UNION ALL
SELECT 'cliente', COUNT(*) FROM cliente;

SELECT '=========================================' AS '';
SELECT 'BANCO NF-E EMISSOR CRIADO COM SUCESSO!' AS '';
SELECT '=========================================' AS '';
SELECT 'Database: nfedb' AS '';
SELECT 'Usuário padrão: admin / admin123' AS '';
SELECT '=========================================' AS '';
