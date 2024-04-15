-- PROCEDURE: public.criar_cliente(character varying, character varying, character varying, date, character varying, character varying, double precision, character varying)

-- DROP PROCEDURE IF EXISTS public.criar_cliente(character varying, character varying, character varying, date, character varying, character varying, double precision, character varying);

CREATE OR REPLACE PROCEDURE public.criar_cliente(
	IN cpf_param character varying,
	IN nome_param character varying,
	IN endereco_param character varying,
	IN data_param date,
	IN senha_param character varying,
	IN tipoconta_param character varying,
	IN saldo_param double precision,
	IN categoriaconta_param character varying)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    INSERT INTO cliente (cpf, nome, endereco, data, senha, tipoconta, saldo, categoriaconta, contaativa, limitetransacoes)
    VALUES (cpf_param, nome_param, endereco_param, data_param, senha_param, tipoconta_param, saldo_param, categoriaconta_param, true, 5);
END;
$BODY$;
ALTER PROCEDURE public.criar_cliente(character varying, character varying, character varying, date, character varying, character varying, double precision, character varying)
    OWNER TO postgres;


-- FUNCTION: public.listar_clientes()

-- DROP FUNCTION IF EXISTS public.listar_clientes();

CREATE OR REPLACE FUNCTION public.listar_clientes(
	)
    RETURNS TABLE(id integer, cpf character varying, nome character varying, endereco character varying, data date, senha character varying, tipoconta character varying, saldo double precision, categoriaconta character varying, contaativa boolean, limitetransacoes integer) 
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
BEGIN
    RETURN QUERY SELECT cliente.id, cliente.cpf, cliente.nome, cliente.endereco, cliente.data, cliente.senha, cliente.tipoConta, cliente.saldo, cliente.categoriaconta, cliente.contaativa, cliente.limitetransacoes FROM cliente;
END;
$BODY$;

ALTER FUNCTION public.listar_clientes()
    OWNER TO postgres;