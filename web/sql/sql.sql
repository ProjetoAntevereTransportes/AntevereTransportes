create database if not exists antevere;

use antevere;

create table if not exists usuario(
    ID int primary key auto_increment,
    nome varchar(200) not null,
    login varchar(100) not null unique,
    senha varchar(64) not null,
    data_nascimento date not null
);