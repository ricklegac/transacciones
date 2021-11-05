-- public.persona definition

-- Drop table

-- DROP TABLE public.persona;

CREATE TABLE public.persona (
	cedula int4 NOT NULL,
	nombre varchar(1000) NULL,
	apellido varchar(1000) NULL,
	saldo int4 NULL,
	CONSTRAINT pk_cedula PRIMARY KEY (cedula)
);
