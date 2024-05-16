create  table usuario
(
	id bigserial not null primary key,
	nome character varying(150),
	email character varying(100),
	senha character varying(150),
	data_cadastro date default now()
	
)

create table lancamento
(
	id bigserial not null primary key,
	descricao character varying(100) not null,
	mes integer not null,
	ano integer not null,
	valor numeric(16,2) not null,
	tipo character varying(20) check (tipo in ('RECEITA','DESPESA')) not null,
	status character varying(20) check (tipo in ('PENDENTE','CANCELADO','EFETIVADO')) not null,
	id_usuario bigint references usuario(id),
	data_cadastro date default now()
	
);


create  table usuario
(
	id bigserial not null primary key,
	nome character varying(150),
	email character varying(100),
	senha character varying(150),
	data_cadastro date default now()
	
);

create table lancamento
(
	id bigserial not null primary key,
	descricao character varying(100) not null,
	mes integer not null,
	ano integer not null,
	valor numeric(16,2) not null,
	tipo character varying(20) check (tipo in ('RECEITA','DESPESA')) not null,
	status character varying(20) check (tipo in ('PENDENTE','CANCELADO','EFETIVADO')) not null,
	id_usuario bigint references usuario(id),
	data_cadastro date default now()
	
);

insert into usuario(id,nome,email,senha) values (1,'luiz carlos','lcfernande@gmail.com','quimica');
insert into lancamento(id,descricao,mes,ano,valor,tipo,status,id_usuario) values
(3,' dfskjfsf fsjfklfj',1,2024,200.0,'DESPESA','PENDENTE',1);



create table usuario(
  id bigserial not null primary key,
  nome character varying(100) not null);
  
create table orcamento(
  id bigserial not null primary key,
  tipo character varying(20)  check (tipo IN ('RECEITA','DESPESA')) not null,
  id_usuario bigint references usuario(id)
  );
  
  insert into usuario(id,nome) values (1,'luiz carlos');
 insert into orcamento(id,tipo,id_usuario) values (1,'DESPESA',1);
