package application;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.entities.Cliente;
import model.entities.Conta;
import model.entities.ContaCorrente;
import model.entities.ContaPoupanca;

public class Main {

	public static Scanner scanner;

	public static Conta contaCorrente = new ContaCorrente(1001, 0002-9, 1000.0, 2000.0);
	public static Conta contaPoupanca = new ContaPoupanca();

	static void main(String[] args) {

		Locale.setDefault(Locale.US);
		scanner = new Scanner(System.in);

		login();
		
		menu();
	}

	public static void login() {
		String linha = "=".repeat(15);
		System.out.println(linha+" Login "+linha);
		System.out.print("Nome completo:\n-> ");
		String name = scanner.nextLine();
		
		while (identificarRegex(name)) {
			System.out.println("Digite somente letras!");
			System.out.print("Tente Novamente:\n-> ");
			name = scanner.nextLine();
		}
		
		System.out.print("CPF:\n-> ");
		int cpf = scanner.nextInt();
		
		System.out.print("Senha:\n-> ");
		int senha = scanner.nextInt();
		
		System.out.println("\n" + "=".repeat(30));
		
		Cliente cliente = new Cliente(name, String.valueOf(cpf));
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
		case 3:
			System.out.println("Qual valor do saque: R$ ");
			contaCorrente.saque(scanner.nextDouble());
			break;
		case 4:
			System.out.println("Em qual conta você quer transferir seu dinheiro:");
			System.out.println("   1 - Conta Poupanca:");
			System.out.print("   2 - Conta Corrente:\n-> ");
			int opcaoTransferencia = scanner.nextInt();
			
			System.out.print("Digite o valor: ");
			double valor = scanner.nextDouble();
			
			if (opcaoTransferencia == 1) {
				contaCorrente.transferir(contaPoupanca, valor);
			}
			else if (opcaoTransferencia == 2) {
				contaPoupanca.transferir(contaCorrente, valor);
			}
			else {
				System.out.println("Opção errada! Tente Novamente.");
			}
			break;
		case 5:
			for (Object ext : contaCorrente.getExtratos()) {
				System.out.println(ext);
			}
			break;
		case 6:
			Map<String, Double> parcelas = new LinkedHashMap<>();
			System.out.println("1 - Você quer fazer um emprestimo: ");
			System.out.print("2 - Você quer consultar as parcelas:\n-> ");
			int opcaoEmprestimo = scanner.nextInt();
			
			System.out.println();
			if (opcaoEmprestimo == 1) {
				System.out.print("Qual valor do emprestimo: R$ ");
				double quantia = scanner.nextDouble();
				System.out.print("Qual o prazo: ");
				int prazo = scanner.nextInt();
		
				if (contaCorrente instanceof ContaCorrente cc) {
					if (cc.concederEmprestimo(quantia)) {
						System.out.println("Emprestimo de R$ " + String.format("%.2f", quantia) + " aprovado.");
						
						for (int i=0; i<prazo; i++) {
							double valorParcela = cc.realizarEmprestimo(quantia/prazo, (i+1));
							parcelas.put((i+1) + " - parcela", valorParcela);
						}
					}
					else {
						System.out.println("Emprestimo negado. Valor excede o limite disponível");
						}
					}
				}
			else if (opcaoEmprestimo == 2) {
				for (String parcela : parcelas.keySet()) {
					System.out.println(parcela + " - R$ " + String.format("%.2f\n", parcelas.get(parcela)));
					}
				}
			break;
		case 0:
			break;
		}
	}
	
	public static boolean identificarRegex(String texto) {
		Pattern patternNumero = Pattern.compile("[^\\p{L}\\s]");
		Matcher temNumeroOuSimbolo = patternNumero.matcher(texto);
		
		return temNumeroOuSimbolo.find();
	}
}
