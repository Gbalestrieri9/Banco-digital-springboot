-- Table: public.cliente

-- DROP TABLE IF EXISTS public.cliente;

CREATE TABLE IF NOT EXISTS public.cliente
(
    id integer NOT NULL DEFAULT nextval('cliente_id_seq'::regclass),
    cpf character varying(14) COLLATE pg_catalog."default" NOT NULL,
    nome character varying(100) COLLATE pg_catalog."default" NOT NULL,
    endereco character varying(255) COLLATE pg_catalog."default" NOT NULL,
    data date,
    senha character varying COLLATE pg_catalog."default",
    tipoconta character varying COLLATE pg_catalog."default",
    saldo double precision,
    categoriaconta character varying COLLATE pg_catalog."default",
    contaativa boolean DEFAULT true,
    limitetransacoes integer DEFAULT 0,
    CONSTRAINT cliente_pkey PRIMARY KEY (id),
    CONSTRAINT cliente_cpf_unique UNIQUE (cpf)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.cliente
    OWNER to postgres;

    
-- Table: public.cartaocredito

-- DROP TABLE IF EXISTS public.cartaocredito;

CREATE TABLE IF NOT EXISTS public.cartaocredito
(
    id integer NOT NULL DEFAULT nextval('cartaocredito_id_seq'::regclass),
    limite_credito numeric(15,2) NOT NULL,
    valor_fatura numeric(15,2),
    cliente_id bigint,
    CONSTRAINT cartaocredito_pkey PRIMARY KEY (id),
    CONSTRAINT cartaocredito_cliente_id_fkey FOREIGN KEY (cliente_id)
        REFERENCES public.cliente (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.cartaocredito
    OWNER to postgres;


-- Table: public.apoliceviagem

-- DROP TABLE IF EXISTS public.apoliceviagem;

CREATE TABLE IF NOT EXISTS public.apoliceviagem
(
    id integer NOT NULL DEFAULT nextval('apoliceviagem_id_seq'::regclass),
    numero_apolice character varying(100) COLLATE pg_catalog."default" NOT NULL,
    data_contratacao date NOT NULL,
    detalhes_cartao character varying(255) COLLATE pg_catalog."default" NOT NULL,
    valor_apolice numeric(15,2) NOT NULL,
    condicoes_acionamento text COLLATE pg_catalog."default" NOT NULL,
    cartao_credito_id integer NOT NULL,
    CONSTRAINT apoliceviagem_pkey PRIMARY KEY (id),
    CONSTRAINT apoliceviagem_numero_apolice_key UNIQUE (numero_apolice),
    CONSTRAINT fk_cartao_credito_viagem FOREIGN KEY (cartao_credito_id)
        REFERENCES public.cartaocredito (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.apoliceviagem
    OWNER to postgres;


-- Table: public.apolicefraude

-- DROP TABLE IF EXISTS public.apolicefraude;

CREATE TABLE IF NOT EXISTS public.apolicefraude
(
    id integer NOT NULL DEFAULT nextval('apolicefraude_id_seq'::regclass),
    numero_apolice character varying(100) COLLATE pg_catalog."default" NOT NULL,
    data_contratacao date NOT NULL,
    detalhes_cartao character varying(255) COLLATE pg_catalog."default" NOT NULL,
    valor_apolice numeric(15,2) NOT NULL,
    condicoes_acionamento text COLLATE pg_catalog."default" NOT NULL,
    cartao_credito_id integer NOT NULL,
    CONSTRAINT apolicefraude_pkey PRIMARY KEY (id),
    CONSTRAINT apolicefraude_numero_apolice_key UNIQUE (numero_apolice),
    CONSTRAINT fk_cartao_credito_fraude FOREIGN KEY (cartao_credito_id)
        REFERENCES public.cartaocredito (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.apolicefraude
    OWNER to postgres;


-- Table: public.apolice

-- DROP TABLE IF EXISTS public.apolice;

CREATE TABLE IF NOT EXISTS public.apolice
(
    id integer NOT NULL DEFAULT nextval('apolice_id_seq'::regclass),
    numero_apolice character varying(100) COLLATE pg_catalog."default" NOT NULL,
    data_contratacao date NOT NULL,
    detalhes_cartao character varying(255) COLLATE pg_catalog."default" NOT NULL,
    valor_apolice numeric(15,2) NOT NULL,
    condicoes_acionamento text COLLATE pg_catalog."default" NOT NULL,
    cartao_credito_id integer NOT NULL,
    CONSTRAINT apolice_pkey PRIMARY KEY (id),
    CONSTRAINT apolice_numero_apolice_key UNIQUE (numero_apolice),
    CONSTRAINT fk_cartao_credito FOREIGN KEY (cartao_credito_id)
        REFERENCES public.cartaocredito (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.apolice
    OWNER to postgres;