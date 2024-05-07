package com.bancodigital.controller;

import java.sql.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bancodigital.dto.LoginRequestDTO;
import com.bancodigital.dto.PagamentoDTO;
import com.bancodigital.dto.TransferenciaDTO;
import com.bancodigital.model.CartaoCredito;
import com.bancodigital.model.Cliente;
import com.bancodigital.usecase.CartaoService;
import com.bancodigital.usecase.ClienteService;
import com.bancodigital.utils.Constantes;
import com.bancodigital.utils.JwtUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import com.bancodigital.dto.JwtData;

@RestController
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private CartaoService cartaoService;

	@PostMapping("/create/account")
	@Operation(description = "criar uma conta no banco")
	@ApiResponses(value = {
			@ApiResponse (responseCode = "200", description = "Conta criada com sucesso"),
			@ApiResponse (responseCode = "400", description = "Falha na criação da conta")
	})
	public ResponseEntity<String> addCliente(@RequestBody Cliente cliente) {
	    Date dataSql = new Date(cliente.getData().getTime());
	    String mensagem = clienteService.criarCliente(cliente.getCpf(), cliente.getNome(), cliente.getEndereco(), dataSql,
	            cliente.getSenha(), cliente.getTipoConta(), cliente.getSaldo(), cliente.getCategoriaConta());
	    
	    if (mensagem.equals(Constantes.MSG_CRIACAO_CLIENTE_SUCESSO)) {
	        clienteService.aplicarTaxaOuRendimentoDiario(cliente.getTipoConta(), cliente.getCategoriaConta(), cliente);
	        return ResponseEntity.ok(mensagem);
	    } else {
	        return ResponseEntity.badRequest().body(mensagem);
	    }
	    
    
	}

	@GetMapping("/all")
	@Operation(description = "Lista todos os clientes do banco")
	@ApiResponses(value = {
			@ApiResponse (responseCode = "200", description = "Retorna todos os clientes"),
			@ApiResponse (responseCode = "400", description = "Falha no retorno dos clientes")
	})
	public List<Cliente> getAllCliente() {
		return clienteService.listarClientes();
	}

	@PostMapping("/transferir")
	@Operation(description = "Transferir o valor da conta para outra")
	@ApiResponses(value = {
			@ApiResponse (responseCode = "200", description = "Valor transferido com sucesso"),
			@ApiResponse (responseCode = "400", description = "Falha na transferencia")
	})
	public String transferirSaldo(@RequestBody TransferenciaDTO transferencia,
			@RequestHeader("Authorization") String token) {
		JwtData jwtData = JwtUtils.decodeToken(token);
		
		if (!jwtData.isContaativa()) {
	        throw new RuntimeException("Sua conta está inativa, não é possível realizar a transferência.");
	    }

		return clienteService.transferirSaldo(jwtData.getCpf(), transferencia.getCpfDestino(),
				transferencia.getValor());
	}

	@PostMapping("/login")
	@Operation(description = "Fazer o login da conta")
	@ApiResponses(value = {
			@ApiResponse (responseCode = "200", description = "Login com sucesso"),
			@ApiResponse (responseCode = "400", description = "Falha no login")
	})
	public String login(@RequestBody LoginRequestDTO loginRequest) {
		return clienteService.login(loginRequest.getCpf(), loginRequest.getSenha());
	}

	@GetMapping("/saldo")
	@Operation(description = "Visualizar o saldo da conta logada")
	@ApiResponses(value = {
			@ApiResponse (responseCode = "200", description = "Retorno do saldo"),
			@ApiResponse (responseCode = "400", description = "Falha no retorno do saldo")
	})
	public ResponseEntity<Double> visualizarSaldo(@RequestHeader("Authorization") String token) {
		JwtData jwtData = JwtUtils.decodeToken(token);
		
		if (!jwtData.isContaativa()) {
	        throw new RuntimeException("Sua conta está inativa, não é possível realizar a transferência.");
	    }

		double saldo = clienteService.visualizarSaldo(jwtData.getCpf());

		return ResponseEntity.ok(saldo);
	}

	@PostMapping("/alterar-senha")
	@Operation(description = "Alterar a senha da conta logada")
	@ApiResponses(value = {
			@ApiResponse (responseCode = "200", description = "Senha trocada com sucesso"),
			@ApiResponse (responseCode = "400", description = "Falha na troca da senha")
	})
	public ResponseEntity<String> alterarSenha(@RequestHeader("Authorization") String token,
			@RequestParam("novaSenha") String novaSenha) {
		JwtData jwtData = JwtUtils.decodeToken(token);
		
		if (!jwtData.isContaativa()) {
	        throw new RuntimeException("Sua conta está inativa, não é possível realizar a transferência.");
	    }
		
		String resposta = clienteService.alterarSenha(jwtData.getCpf(), novaSenha);
		return ResponseEntity.ok(resposta);
	}

	@PostMapping("/criar-cartao")
	@Operation(description = "Criar um cartão de credito na conta logada")
	@ApiResponses(value = {
			@ApiResponse (responseCode = "200", description = "Cartão de credito criado com sucesso"),
			@ApiResponse (responseCode = "400", description = "Falha na criação do cartão de credito")
	})
	public ResponseEntity<String> criarCartaoCredito(@RequestHeader("Authorization") String token) {
		JwtData jwtData = JwtUtils.decodeToken(token);
		double limiteCartao = 0;
		
		if (!jwtData.isContaativa()) {
	        throw new RuntimeException("Sua conta está inativa, não é possível realizar a transferência.");
	    }

		if (!jwtData.getTipoConta().equalsIgnoreCase("corrente")) {
			return ResponseEntity.badRequest().body("Apenas contas corrente podem ter cartão de crédito.");
		} else {
			limiteCartao = cartaoService.criarCartao(jwtData.getCpf(), jwtData.getCategoriaConta());
		}

		return ResponseEntity.ok("Cartão de crédito criado com sucesso! Limite: " + limiteCartao);
	}
	
	@PostMapping("/efetuar-pagamento")
	@Operation(description = "Efetuar pagamentos, com debito ou credito")
	@ApiResponses(value = {
			@ApiResponse (responseCode = "200", description = "Pagamento realizado com sucesso"),
			@ApiResponse (responseCode = "400", description = "Falha no pagamento")
	})
	public ResponseEntity<String> efetuarPagamento(@RequestBody PagamentoDTO pagamento, @RequestHeader("Authorization") String token) {
	    JwtData jwtData = JwtUtils.decodeToken(token);
	    String cpfCliente = jwtData.getCpf();
	    
	    if (!jwtData.isContaativa()) {
	        throw new RuntimeException("Sua conta está inativa, não é possível realizar a transferência.");
	    }
	    
	    cartaoService.efetuarPagamento(cpfCliente,pagamento);
	    
	    return null;
	}
	
	@GetMapping("/consultar-cartao")
	@Operation(description = "Visualizar os dados do cartão de credito")
	@ApiResponses(value = {
			@ApiResponse (responseCode = "200", description = "Retorno dos dados com sucesso"),
			@ApiResponse (responseCode = "400", description = "Falha no retorno dos dados")
	})
    public ResponseEntity<CartaoCredito> consultarCartaoCredito(@RequestHeader("Authorization") String token) {
        JwtData jwtData = JwtUtils.decodeToken(token);
        String cpfCliente = jwtData.getCpf();
        
        if (!jwtData.isContaativa()) {
	        throw new RuntimeException("Sua conta está inativa, não é possível realizar a transferência.");
	    }
        
        CartaoCredito cartaoCredito = cartaoService.recuperarCartaoCredito(cpfCliente);
        
        if (cartaoCredito == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(cartaoCredito);
    }
	
	@PostMapping("/desativar-conta")
	@Operation(description = "Desativar a conta logada")
	@ApiResponses(value = {
			@ApiResponse (responseCode = "200", description = "Conta desativada com sucesso"),
			@ApiResponse (responseCode = "400", description = "Falha desativação da conta")
	})
	public ResponseEntity<String> desativarConta(@RequestHeader("Authorization") String token){
		JwtData jwtData = JwtUtils.decodeToken(token);
		String cpfCliente = jwtData.getCpf();
		
		clienteService.desativarConta(cpfCliente);
		
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/ativar-conta")
	@Operation(description = "Reativar a conta logada")
	@ApiResponses(value = {
			@ApiResponse (responseCode = "200", description = "Conta reativada com sucesso"),
			@ApiResponse (responseCode = "400", description = "Falha na reativação da conta")
	})
	public ResponseEntity<String> ativarConta(@RequestHeader("Authorization") String token){
		JwtData jwtData = JwtUtils.decodeToken(token);
		String cpfCliente = jwtData.getCpf();
		
		clienteService.ativarConta(cpfCliente);
		
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/alterar-limite-transacoes")
	@Operation(description = "Alterar o limite de transações")
	@ApiResponses(value = {
			@ApiResponse (responseCode = "200", description = "Limite de transações realizada com sucesso"),
			@ApiResponse (responseCode = "400", description = "Falha na mudança de transações")
	})
	public ResponseEntity<String> alterarLimiteTransacoes(@RequestHeader("Authorization") String token,@RequestParam("novolimite") int novolimite){
		JwtData jwtData = JwtUtils.decodeToken(token);
		String cpfCliente = jwtData.getCpf();
		
		if (!jwtData.isContaativa()) {
	        throw new RuntimeException("Sua conta está inativa, não é possível realizar a transferência.");
	    }
		
		clienteService.ajustarLimiteTransacoes(cpfCliente,novolimite);
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/contratar-apolice-viagem")
	@Operation(description = "Contratar seguro de viagem")
	@ApiResponses(value = {
			@ApiResponse (responseCode = "200", description = "contratação do seguro viagem realizada com sucesso "),
			@ApiResponse (responseCode = "400", description = "Falha na contratação de viagem")
	})
	public ResponseEntity<String> contratarApoliceViagem(@RequestHeader("Authorization") String token) {
	    JwtData jwtData = JwtUtils.decodeToken(token);
	    String cpfCliente = jwtData.getCpf();
	    
	    if (!jwtData.isContaativa()) {
	        throw new RuntimeException("Sua conta está inativa, não é possível realizar a transferência.");
	    }
	    
	    double valorApolice = cartaoService.gerarApoliceSeguroViagem(jwtData.getCategoriaConta());
	    
	    cartaoService.salvarApoliceViagem(cpfCliente, valorApolice);
	    return ResponseEntity.ok("Apólice de viagem contratada com sucesso.");
	}

	@PostMapping("/contratar-apolice-fraude")
	@Operation(description = "Contratar seguro de fraude")
	@ApiResponses(value = {
			@ApiResponse (responseCode = "200", description = "contratação do seguro fraude realizada com sucesso "),
			@ApiResponse (responseCode = "400", description = "Falha na contratação do seguro fraude")
	})
	public ResponseEntity<String> contratarApoliceFraude(@RequestHeader("Authorization") String token) {
	    JwtData jwtData = JwtUtils.decodeToken(token);
	    String cpfCliente = jwtData.getCpf();
	    
	    if (!jwtData.isContaativa()) {
	        throw new RuntimeException("Sua conta está inativa, não é possível realizar a transferência.");
	    }
	    
	    String detalhesApolice = cartaoService.gerarApoliceSeguroFraude();
	    
	    cartaoService.salvarApoliceFraude(cpfCliente, detalhesApolice);
	    return ResponseEntity.ok("Apólice de fraude contratada com sucesso.");
	}
}