package model.services;

import application.LeitorConsole;
import model.entities.Cliente;
import util.Validacoes;

public class LoginService {

	private final LeitorConsole leitor;

	private Cliente clienteAutenticado;
	
	public LoginService(LeitorConsole leitor) {
		this.leitor = leitor;
	}
	
	public Cliente login() {
		
		String linha = "=".repeat(15);
		System.out.println(linha + " Login " + linha + "\n");
		System.out.print("Nome completo:\n-> ");
		String nome = leitor.lerTexto();

		Validacoes.identificarRegex(nome);

		System.out.print("\nDigite seu CPF (11 dígitos, sem potuações):\n-> ");
		Long cpf = leitor.lerLong();

		while (String.valueOf(cpf).length() != 11) {
			System.out.println("\nErro: o cpf deve ter exatamente 11 dígitos");
			System.out.print("Tente Novamente: ");
			cpf = leitor.lerLong();
		}

		String cpfFormatado = Validacoes.formatadorCpf(cpf);
		clienteAutenticado = new Cliente(nome, cpfFormatado);

		System.out.print("\nDigite sua senha:\n-> ");
		int senha = leitor.lerInteiro();
		
		System.out.println("\n" + "=".repeat(37));
		
		return clienteAutenticado;
	}
}
