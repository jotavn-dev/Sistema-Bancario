package application;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import model.entities.Cliente;
import model.entities.Conta;
import model.entities.ContaCorrente;
import model.entities.ContaPoupanca;

public class Main {

	public static Scanner scanner;

	public static Conta contaCorrente = new ContaCorrente();
	public static Conta contaPoupanca = new ContaPoupanca();

	static void main(String[] args) {

		Locale.setDefault(Locale.US);
		scanner = new Scanner(System.in);

		menu();
	}

	public static void login() {
		System.out.print("Nome completo:\n-> ");
		String name = scanner.nextLine();

		System.out.print("CPF:\n-> ");
		int cpf = scanner.nextInt();

		System.out.print("Senha:\n-> ");
		int senha = scanner.nextInt();
	}

	public static void menu() {
		
		List<String> menu = Arrays.asList("Consultar saldo", "Depositar", "Sacar", "Transferir"
				,"Extrato", "Emprestimo");
		
		for (int i=0; i<menu.size(); i++) {
			System.out.println((i+1) + " - " + menu.get(i));
		}
		System.out.println("0 - Sair");
		
		System.out.print("Escolha a opção: ");
		int opcao = scanner.nextInt();
		
		switch (opcao) {
		
		case 1:
			System.out.println("Saldo: R$ " + String.format("%.2f", contaCorrente.getSaldo()));
			break;
		case 2:
			System.out.print("Qual valor do deposito: R$ ");
			contaCorrente.deposito(scanner.nextDouble());
			break;
		}
	}
}
