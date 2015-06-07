drop database anteveretransportes;
CREATE DATABASE IF NOT EXISTS anteveretransportes;
USE anteveretransportes;

CREATE TABLE IF NOT EXISTS tipo_usuario(
	id INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(30) NOT NULL,
	descricao VARCHAR(90));
	

CREATE TABLE IF NOT EXISTS endereco(
	id INT PRIMARY KEY AUTO_INCREMENT,
	rua VARCHAR(30) NOT NULL,
	bairro VARCHAR(30) NOT NULL,
	numero VARCHAR(10) NOT NULL,
	cidade VARCHAR(30) NOT NULL,
	estado VARCHAR(30) NOT NULL,
	pais VARCHAR(30) NOT NULL
);


CREATE TABLE IF NOT EXISTS status_usuario(
	id 	INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(30) NOT NULL
);


	
CREATE TABLE IF NOT EXISTS pergunta(
	id INT PRIMARY KEY AUTO_INCREMENT,
	pergunta VARCHAR(90) NOT NULL
);

CREATE TABLE IF NOT EXISTS usuario(
	id INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	senha VARCHAR(60) NOT NULL,
	pergunta_id INT NOT NULL,
	FOREIGN KEY (pergunta_id) REFERENCES pergunta(id),
	resposta VARCHAR(60) NOT NULL,
	tipo_usuario_id INT NOT NULL,
	FOREIGN KEY (tipo_usuario_id) REFERENCES tipo_usuario(id),
	status_id INT NOT NULL,
	FOREIGN KEY (status_id) REFERENCES status_usuario(id),
	chave_senha_perdida VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS status_cliente(
	id INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS cliente(
	id INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(90) NOT NULL,
	email VARCHAR(60) NOT NULL,
	telefone VARCHAR(15) NOT NULL,
	cnpj VARCHAR(18) UNIQUE NOT NULL, 
	observacao VARCHAR(150),
	status_id INT NOT NULL,
	FOREIGN KEY (status_id) REFERENCES status_cliente(id),
	endereco_id INT NOT NULL,
	FOREIGN KEY (endereco_id) REFERENCES endereco(id)
);

CREATE TABLE IF NOT EXISTS cargo(
	id INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(30) NOT NULL,
	descricao VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS funcionario(
	id INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(30) NOT NULL,
	sobrenome VARCHAR(30) NOT NULL,
	telefone VARCHAR(15) NOT NULL,
	email VARCHAR(50) NOT NULL UNIQUE,
	cargo_id INT NOT NULL,
	FOREIGN KEY (cargo_id) REFERENCES cargo(id),
	cpf VARCHAR(15) NOT NULL UNIQUE,
	rg VARCHAR(12) NOT NULL UNIQUE,
	endereco_id INT NOT NULL,
	FOREIGN KEY (endereco_id) REFERENCES endereco(id)
);

CREATE TABLE IF NOT EXISTS caminhao(
	id INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(30) NOT NULL,
	placa VARCHAR(10) NOT NULL UNIQUE,
	renavam VARCHAR(30) NOT NULL UNIQUE,
	cor VARCHAR(15),
	marca VARCHAR(20) NOT NULL,
	modelo VARCHAR(20) NOT NULL,
	data_compra DATE NOT NULL,
	ano_modelo INT NOT NULL,
	gasto_kilometros INT NOT NULL
);

CREATE TABLE IF NOT EXISTS log_situation(
	id INT PRIMARY KEY AUTO_INCREMENT,
	descricao VARCHAR(100),
	sobre_id INT,
	usuario_id INT,
	FOREIGN KEY (usuario_id) REFERENCES usuario(id)
); 

CREATE TABLE IF NOT EXISTS carga(
	id INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(30) NOT NULL,
	descricao VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS status_viagem(
	id INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(30) NOT NULL,
	descricao VARCHAR(100) NOT NULL
);


CREATE TABLE IF NOT EXISTS banco(
	id INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(20) NOT NULL,
	numero VARCHAR(15) NOT NULL
);


CREATE TABLE IF NOT EXISTS pessoa(
	id INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS conta_bancaria(
	id INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(20) NOT NULL,
	numero VARCHAR(20) NOT NULL,
	agencia VARCHAR(10) NOT NULL,
	pessoa_id INT NOT NULL,
	FOREIGN KEY (pessoa_id) REFERENCES pessoa(id),
	banco_id INT NOT NULL,
	FOREIGN KEY (banco_id) REFERENCES banco(id)
);

CREATE TABLE IF NOT EXISTS fornecedor(
	id INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(60) NOT NULL,
	endereco_id INT NOT NULL,
	FOREIGN KEY (endereco_id) REFERENCES endereco(id),
	email VARCHAR(50),
	telefone VARCHAR(20),
        contato VARCHAR(90)
);

CREATE TABLE debito_automatico (
  id int(11) NOT NULL AUTO_INCREMENT,
  conta_bancaria_id int(11) NOT NULL,
  data_inicio date DEFAULT NULL,
  data_fim date DEFAULT NULL,
  dia_mensal int(11) DEFAULT NULL,
  nome varchar(40) NOT NULL,
  descricao varchar(60) DEFAULT NULL,
  fornecedor_id int(11) NOT NULL,
  valor decimal(10,2) NOT NULL,
  PRIMARY KEY (id),
  KEY conta_bancaria_id (conta_bancaria_id),
  KEY fornecedor_id (fornecedor_id),
  CONSTRAINT debito_automatico_ibfk_1 FOREIGN KEY (conta_bancaria_id) REFERENCES conta_bancaria (id),
  CONSTRAINT debito_automatico_ibfk_2 FOREIGN KEY (fornecedor_id) REFERENCES fornecedor (id)
);



CREATE TABLE IF NOT EXISTS status_pagamento(
	id INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(30) NOT NULL,
	descricao VARCHAR(90)
);

CREATE TABLE IF NOT EXISTS parcela_recebimento(
	id INT PRIMARY KEY AUTO_INCREMENT,
	data_pagamento DATE NOT NULL,
	valor DECIMAL(10,2) NOT NULL,
	status_pagamento_id INT NOT NULL,
	FOREIGN KEY (status_pagamento_id) REFERENCES status_pagamento(id),
	conta_bancaria_id INT NOT NULL,
	FOREIGN KEY (conta_bancaria_id) REFERENCES conta_bancaria(id)
);



CREATE TABLE conta_pagar (
  id int(11) NOT NULL AUTO_INCREMENT,
  fornecedor_id int(11) NOT NULL,
  nome varchar(40) NOT NULL,
  quantidade int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY fornecedor_id (fornecedor_id),
  CONSTRAINT conta_pagar_ibfk_1 FOREIGN KEY (fornecedor_id) REFERENCES fornecedor (id)
);

CREATE TABLE arquivo(
	id int(11) PRIMARY KEY AUTO_INCREMENT,
	nome varchar(100) NOT NULL,
	tamanho_bytes varchar(50) NOT NULL,
	data_criacao Date NOT NULL,
	arquivo MEDIUMBLOB NOT NULL
);

CREATE TABLE parcela_pagamento (
  id int(11) NOT NULL AUTO_INCREMENT,
  vencimento date NOT NULL,
  valor decimal(10,2) NOT NULL,
  descontos decimal(10,2) NOT NULL,
  juros decimal(10,2) NOT NULL,
  status_pagamento_id int(11) NOT NULL,
  data_pagamento date DEFAULT NULL,
  comprovante_id int(11) DEFAULT NULL,
  boleto_id int(11) DEFAULT NULL,
  conta_bancaria_id int(11) DEFAULT NULL,
  descricao varchar(90) DEFAULT NULL,
  debito_automatico_id int(11) DEFAULT NULL,
  conta_pagar_id int(11),
  numero int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (id),
  KEY status_pagamento_id (status_pagamento_id),
  KEY conta_bancaria_id (conta_bancaria_id),
  KEY debito_automatico_id (debito_automatico_id),
  KEY conta_pagar_id (conta_pagar_id),
  CONSTRAINT parcela_pagamento_ibfk_1 FOREIGN KEY (status_pagamento_id) REFERENCES status_pagamento (id),
  CONSTRAINT parcela_pagamento_ibfk_2 FOREIGN KEY (conta_bancaria_id) REFERENCES conta_bancaria (id),
  CONSTRAINT parcela_pagamento_ibfk_3 FOREIGN KEY (debito_automatico_id) REFERENCES debito_automatico (id),
  CONSTRAINT parcela_pagamento_ibfk_4 FOREIGN KEY (conta_pagar_id) REFERENCES conta_pagar (id),
CONSTRAINT parcela_pagamento_ibfk_5 FOREIGN KEY (comprovante_id) REFERENCES arquivo (id),
CONSTRAINT parcela_pagamento_ibfk_6 FOREIGN KEY (boleto_id) REFERENCES arquivo (id)
);

CREATE TABLE IF NOT EXISTS viagem(
	id INT PRIMARY KEY AUTO_INCREMENT,
	funcionario_id INT NOT NULL,
	FOREIGN KEY (funcionario_id) REFERENCES funcionario(id),
	valor DECIMAL(10,2) NOT NULL,
	tipo_carga_id INT NOT NULL,
	FOREIGN KEY (tipo_carga_id) REFERENCES carga(id),
	cliente_id INT NOT NULL,
	FOREIGN KEY (cliente_id) REFERENCES cliente(id),
	adiantamento DECIMAL(10,2) NOT NULL,
	status_viagem_id INT NOT NULL,
	FOREIGN KEY (status_viagem_id) REFERENCES status_viagem(id),
	motivo_cancelamento VARCHAR(60),
        caminhao_id INT NOT NULL,
        FOREIGN KEY (caminhao_id) REFERENCES caminhao(id)
);

CREATE TABLE IF NOT EXISTS endereco_viagem(
    id INT PRIMARY KEY AUTO_INCREMENT,
    viagem_id INT NOT NULL,
    FOREIGN KEY (viagem_id) REFERENCES viagem(id),
    partida varchar(300) NOT NULL,
    destino varchar(300) NOT NULL,
    dados LONGTEXT
);

CREATE TABLE IF NOT EXISTS contas_a_receber(
	id INT PRIMARY KEY AUTO_INCREMENT,
	viagem_id INT NOT NULL,
	FOREIGN KEY (viagem_id) REFERENCES viagem(id),
	parcela_id INT NOT NULL,
	FOREIGN KEY (parcela_id) REFERENCES parcela_recebimento(id)
);

insert into tipo_usuario(nome) values("Root");
insert into tipo_usuario(nome) values("Normal");

insert into status_usuario(nome)
values ("Ativo");
insert into status_usuario(nome)
values ("Inativo");

insert into pergunta(pergunta) values("Qual o nome do seu primeiro animal de estimação?");

insert into usuario(nome, email, senha, pergunta_id, resposta, tipo_usuario_id, 
status_id) values("ROOT", "antevere.transportes.ADM@gmail.com", "root", 1, "Pitoco",
1, 1);

insert into status_cliente(nome) values("Ativo");
insert into status_cliente(nome) values("Inativo");

insert into status_viagem(nome, descricao) values("Pendente", "Viagem não realizada.");
insert into status_viagem(nome, descricao) values("Finalizada", "Viagem finalizada.");

insert into pessoa(nome) values("Física");
insert into pessoa(nome) values("Jurídica");

insert into status_pagamento(nome, descricao) values("Não pago", "Pagamento não realizado.");
insert into status_pagamento(nome, descricao) values("Pago", "Pagamento realizado.");

insert into banco(nome, numero)values("Banco da Polônia", "777");

insert into conta_bancaria(nome, numero, agencia, pessoa_id, banco_id) values("Teste Conta", "123", "345", 1, 1);