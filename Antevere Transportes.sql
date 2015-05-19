CREATE TABLE IF NOT EXISTS tipo_usuario(
	id INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(30) NOT NULL,
	descricao VARCHAR(90));
	
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
	pergunta_id INT NOT NULL REFERENCES pergunta(id),
	resposta VARCHAR(60) NOT NULL,
	tipo_usuario_id INT NOT NULL REFERENCES tipo_usuario(id),
	status_id INT NOT NULL REFERENCES status_usuario(id),
	chave_senha_perdida VARCHAR(30)	
);

CREATE TABLE IF NOT EXISTS status_cliente(
	id INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS cliente(
	id 	INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(30) NOT NULL,
	email VARCHAR(60) NOT NULL,
	telefone VARCHAR(15) NOT NULL,
	cnpj VARCHAR(18) UNIQUE NOT NULL, 
	observacao VARCHAR(150),
	status_id INT NOT NULL REFERENCES status_cliente(id)
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
	cargo_id INT NOT NULL REFERENCES cargo(id),
	cpf VARCHAR(15) NOT NULL UNIQUE,
	rg VARCHAR(12) NOT NULL UNIQUE 
);
CREATE TABLE IF NOT EXISTS endereco(
	id INT PRIMARY KEY AUTO_INCREMENT,
	rua VARCHAR(30) NOT NULL,
	bairro VARCHAR(30) NOT NULL,
	numero VARCHAR(10) NOT NULL,
	cidade VARCHAR(30) NOT NULL,
	estado VARCHAR(30) NOT NULL,
	pais VARCHAR(30) NOT NULL,
	dono_id INT NOT NULL
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
	id INT NOT NULL AUTO_INCREMENT,
	descricao VARCHAR(100),
	sobre_id INT,
	usuario_id INT REFERENCES usuario(id)
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
CREATE TABLE IF NOT EXISTS viagem(
	id INT PRIMARY KEY AUTO_INCREMENT,
	funcionario_id INT NOT NULL REFERENCES funcionario(id),
	endereco_carga_id INT NOT NULL REFERENCES endereco(id),
	endereco_descarga_id INT NOT NULL REFERENCES endereco(id),
	valor DECIMAL(10,2) NOT NULL,
	tipo_carga_id INT NOT NULL REFERENCES carga(id),
	cliente_id INT NOT NULL REFERENCES cliente(id),
	adiantamento DECIMAL(10,2) NOT NULL,
	status_viagem_id INT NOT NULL REFERENCES status_viagem(id),
	motivo_cancelamento VARCHAR(60)
);

CREATE TABLE IF NOT EXISTS banco(
	id INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(20) NOT NULL,
	numero VARCHAR(15) NOT NULL
);

CREATE TABLE IF NOT EXISTS conta_bancaria(
	id INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(20) NOT NULL,
	numero VARCHAR(20) NOT NULL,
	pessoa_id INT NOT NULL REFERENCES pessoa(id),
	banco_id INT NOT NULL REFERENCES banco(id)
);

CREATE TABLE IF NOT EXISTS pessoa(
	id INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS fornecedor(
	id INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(60) NOT NULL,
	endereco_id INT NOT NULL REFERENCES endereco(id),
	email VARCHAR(50),
	telefone VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS debito_automatico(
	id INT PRIMARY KEY AUTO_INCREMENT,
	conta_bancaria_id INT NOT NULL REFERENCES conta_bancaria(id),
	data_inicio DATE,
	data_fim DATE,
	dia_mensal INT,
	nome VARCHAR(40) NOT NULL,
	descricao VARCHAR(60),
	fornecedor_id INT NOT NULL REFERENCES fornecedor(id),
	valor DECIMAL(10,2) NOT NULL
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
	status_pagamento_id INT NOT NULL REFERENCES status_pagamento(id),
	conta_bancaria_id INT NOT NULL REFERENCES conta_bancaria(id)
);

CREATE TABLE IF NOT EXISTS contas_a_receber(
	id INT PRIMARY KEY AUTO_INCREMENT,
	viagem_id INT NOT NULL REFERENCES viagem(id),
	parcela_id INT NOT NULL REFERENCES parcela_recebimento(id)
);

CREATE TABLE IF NOT EXISTS parcela_pagamento(
	id INT PRIMARY KEY AUTO_INCREMENT,
	vencimento DATE NOT NULL,
	valor DECIMAL(10,2) NOT NULL,
	descontos DECIMAL(10,2) NOT NULL,
	juros DECIMAL(10,2) NOT NULL,
	status_pagamento_id INT NOT NULL REFERENCES status_pagamento(id),
	data_pagamento DATE NOT NULL,
	comprovante VARCHAR(200) NOT NULL,
	conta_bancaria_id INT NOT NULL REFERENCES conta_bancaria(id),
	descricao VARCHAR(90),
	debito_automatico_id INT REFERENCES debito_automatico(id)
);

CREATE TABLE IF NOT EXISTS conta_pagar(
	id INT PRIMARY KEY AUTO_INCREMENT,
	fornecedor_id INT NOT NULL REFERENCES fornecedor(id),
	nome VARCHAR(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS conta_parcela(
	id INT PRIMARY KEY AUTO_INCREMENT,
	conta_pagar_id INT NOT NULL REFERENCES conta_pagar(id),
	parcela_pagamento_id INT NOT NULL REFERENCES parcela_pagamento(id)
);


