package model.services;

import java.util.InputMismatchException;
import java.util.Scanner;

import model.entities.Cliente;
import util.Validacoes;

public class LoginService {
	
	private Scanner scanner;
	
	public LoginService(Scanner scanner) {
		this.scanner = scanner;
	}
	
	public void login() {
		String linha = "=".repeat(15);
		System.out.println(linha + " Login " + linha + "\n");
		System.out.print("Nome completo: ");
		String name = scanner.nextLine();

		while (Validacoes.identificarRegex(name)) {
			System.out.println("Digite somente letras!");
			System.out.print("Tente Novamente: ");
			name = scanner.nextLine();
		}

		while (true) {
			try {
				System.out.print("\nCPF: ");
				Long cpf = scanner.nextLong();
				while (String.valueOf(cpf).length() != 11) {
					System.out.println("\nErro: o cpf deve ter exatamente 11 dígitos");
					System.out.print("Tente Novamente: ");
					cpf = scanner.nextLong();
				}
				
				String cpfFormatado = Validacoes.formatadorCpf(cpf);
				Cliente cliente = new Cliente(name, cpfFormatado);
				break;
			} 
			catch (InputMismatchException e) {
				System.out.println("\nErro: Digite apenas número");
				scanner.nextLine();
			}
		}
		
		System.out.print("\nSenha: ");
		int senha = scanner.nextInt();
		
		System.out.println("\n" + "=".repeat(37) + "\n");
		
	}
}
