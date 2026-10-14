package model.services;

import java.util.InputMismatchException;
import java.util.Scanner;

import model.entities.Cliente;
import util.Validacoes;

public class LoginService {
	
	private Scanner scanner;
	
	private Cliente cliente;
	
	public LoginService(Scanner scanner) {
		this.scanner = scanner;
	}
	
	public Cliente login() {
		
		String name = null;
		Long cpf = null;
		
		String linha = "=".repeat(15);
		System.out.println(linha + " Login " + linha + "\n");
		System.out.print("Nome completo:\n-> ");
		name = scanner.nextLine();

		while (Validacoes.identificarRegex(name)) {
			System.out.println("Digite somente letras!");
			System.out.print("Tente Novamente: ");
			name = scanner.nextLine();
		}

			try {
				System.out.print("\nDigite seu CPF (11 dígitos, sem potuações):\n-> ");
				cpf = scanner.nextLong();
				
				while (String.valueOf(cpf).length() != 11) {
					System.out.println("\nErro: o cpf deve ter exatamente 11 dígitos");
					System.out.print("Tente Novamente: ");
					cpf = scanner.nextLong();
				}
				
				String cpfFormatado = Validacoes.formatadorCpf(cpf);
				cliente = new Cliente(name, cpfFormatado);

			} 
			catch (InputMismatchException e) {
				System.out.println("\nErro: Digite apenas número");
				scanner.nextLine();
			}
		
		System.out.print("\nDigite sua senha:\n-> ");
		scanner.nextInt();
		
		System.out.println("\n" + "=".repeat(37));
		
		return cliente;
	}
}
