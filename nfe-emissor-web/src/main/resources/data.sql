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