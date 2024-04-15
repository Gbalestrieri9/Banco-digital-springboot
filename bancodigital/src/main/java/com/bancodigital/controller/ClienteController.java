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
import com.bancodigital.dto.JwtData;

@RestController
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private CartaoService cartaoService;

	@PostMapping("/create/account")
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
	public List<Cliente> getAllCliente() {
		return clienteService.listarClientes();
	}

	@PostMapping("/transferir")
	public String transferirSaldo(@RequestBody TransferenciaDTO transferencia,
			@RequestHeader("Authorization") String token) {
		JwtData jwtData = JwtUtils.decodeToken(token);

		return clienteService.transferirSaldo(jwtData.getCpf(), transferencia.getCpfDestino(),
				transferencia.getValor());
	}

	@PostMapping("/login")
	public String login(@RequestBody LoginRequestDTO loginRequest) {
		return clienteService.login(loginRequest.getCpf(), loginRequest.getSenha());
	}

	@GetMapping("/saldo")
	public ResponseEntity<Double> visualizarSaldo(@RequestHeader("Authorization") String token) {
		JwtData jwtData = JwtUtils.decodeToken(token);
		
		if (!jwtData.isContaativa()) {
	        throw new RuntimeException("Sua conta está inativa, não é possível realizar a transferência.");
	    }

		double saldo = clienteService.visualizarSaldo(jwtData.getCpf());

		return ResponseEntity.ok(saldo);
	}

	@PostMapping("/alterar-senha")
	public ResponseEntity<String> alterarSenha(@RequestHeader("Authorization") String token,
			@RequestParam("novaSenha") String novaSenha) {
		JwtData jwtData = JwtUtils.decodeToken(token);
		String resposta = clienteService.alterarSenha(jwtData.getCpf(), novaSenha);
		return ResponseEntity.ok(resposta);
	}

	@PostMapping("/criar-cartao")
	public ResponseEntity<String> criarCartaoCredito(@RequestHeader("Authorization") String token) {
		JwtData jwtData = JwtUtils.decodeToken(token);
		double limiteCartao = 0;

		if (!jwtData.getTipoConta().equalsIgnoreCase("corrente")) {
			return ResponseEntity.badRequest().body("Apenas contas corrente podem ter cartão de crédito.");
		} else {
			limiteCartao = cartaoService.criarCartao(jwtData.getCpf(), jwtData.getCategoriaConta());
		}

		return ResponseEntity.ok("Cartão de crédito criado com sucesso! Limite: " + limiteCartao);
	}
	
	@PostMapping("/efetuar-pagamento")
	public ResponseEntity<String> efetuarPagamento(@RequestBody PagamentoDTO pagamento, @RequestHeader("Authorization") String token) {
	    JwtData jwtData = JwtUtils.decodeToken(token);
	    String cpfCliente = jwtData.getCpf();
	    
	    cartaoService.efetuarPagamento(cpfCliente,pagamento);
	    
	    return null;
	}
	
	@GetMapping("/consultar-cartao")
    public ResponseEntity<CartaoCredito> consultarCartaoCredito(@RequestHeader("Authorization") String token) {
        JwtData jwtData = JwtUtils.decodeToken(token);
        String cpfCliente = jwtData.getCpf();
        
        CartaoCredito cartaoCredito = cartaoService.recuperarCartaoCredito(cpfCliente);
        
        if (cartaoCredito == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(cartaoCredito);
    }
	
	@PostMapping("/desativar-conta")
	public ResponseEntity<String> desativarConta(@RequestHeader("Authorization") String token){
		JwtData jwtData = JwtUtils.decodeToken(token);
		String cpfCliente = jwtData.getCpf();
		
		clienteService.desativarConta(cpfCliente);
		
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/ativar-conta")
	public ResponseEntity<String> ativarConta(@RequestHeader("Authorization") String token){
		JwtData jwtData = JwtUtils.decodeToken(token);
		String cpfCliente = jwtData.getCpf();
		
		clienteService.ativarConta(cpfCliente);
		
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/alterar-limite-transacoes")
	public ResponseEntity<String> alterarLimiteTransacoes(@RequestHeader("Authorization") String token,@RequestParam("novolimite") int novolimite){
		JwtData jwtData = JwtUtils.decodeToken(token);
		String cpfCliente = jwtData.getCpf();
		
		clienteService.ajustarLimiteTransacoes(cpfCliente,novolimite);
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/contratar-apolice-viagem")
	public ResponseEntity<String> contratarApoliceViagem(@RequestHeader("Authorization") String token) {
	    JwtData jwtData = JwtUtils.decodeToken(token);
	    String cpfCliente = jwtData.getCpf();
	    double valorApolice = cartaoService.gerarApoliceSeguroViagem(jwtData.getCategoriaConta());
	    
	    cartaoService.salvarApoliceViagem(cpfCliente, valorApolice);
	    return ResponseEntity.ok("Apólice de viagem contratada com sucesso.");
	}

	@PostMapping("/contratar-apolice-fraude")
	public ResponseEntity<String> contratarApoliceFraude(@RequestHeader("Authorization") String token) {
	    JwtData jwtData = JwtUtils.decodeToken(token);
	    String cpfCliente = jwtData.getCpf();
	    String detalhesApolice = cartaoService.gerarApoliceSeguroFraude();
	    
	    cartaoService.salvarApoliceFraude(cpfCliente, detalhesApolice);
	    return ResponseEntity.ok("Apólice de fraude contratada com sucesso.");
	}
}