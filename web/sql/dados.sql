use anteveretransportes;
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
