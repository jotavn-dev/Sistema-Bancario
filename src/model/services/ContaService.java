package model.services;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import model.entities.Conta;
import model.entities.ContaCorrente;
import model.entities.ContaPoupanca;

public class ContaService {

	private Scanner scanner;
	
	public ContaService(Scanner scanner) {
		this.scanner = scanner;
	}

	public Conta contaCorrente = new ContaCorrente.Builder()
			.setNumero(1001).setAgencia("0002-4")
			.setTaxaPagamento(new TaxaPagamentoBrasil())
			.build();

	public Conta contaPoupanca = new ContaPoupanca.Builder()
			.setNumero(1003).setAgencia("0234-1")
			.setTaxaPagamento(new TaxaPagamentoBrasil())
			.build();

	public void menu() {

		List<String> menu = Arrays.asList("Consultar saldo", "Depositar", "Sacar", "Transferir", "Extrato",
				"Emprestimo");

		loopPrincipal:
		while (true) {
			System.out.println("=".repeat(16) + " MENU " + "=".repeat(15) + "\n");

			for (int i = 0; i < menu.size(); i++) {
				System.out.println((i + 1) + " - " + menu.get(i));
			}
			System.out.println("0 - Sair");

			System.out.print("Escolha a opção: ");
			int opcao = scanner.nextInt();

			switch (opcao) {

			case 1:
				System.out.println("Você qual conta você quer consultar o saldo:");
				System.out.println("   1 - Conta Poupança:");
				System.out.print("   2 - Conta Corrente:\n-> ");
				int opcaoSaldo = scanner.nextInt();
				
				if (opcaoSaldo == 1) {
					System.out.println("\nSaldo: R$ " + String.format("%.2f", contaPoupanca.getSaldo()));
				}
				else if (opcaoSaldo == 2) {
					System.out.println("\nSaldo: R$ " + String.format("%.2f", contaCorrente.getSaldo()));
				}
				else {
					System.out.println("Opcão inválida. Tente Novamente!");
				}
				break;
			case 2:
				System.out.print("Qual valor do deposito: R$ ");
				contaCorrente.realizarDeposito(scanner.nextDouble());
				break;
			case 3:
				System.out.print("Qual valor do saque: R$ ");
				contaCorrente.realizarSaque(scanner.nextDouble());
				break;
			case 4:
				System.out.println("Em qual conta você quer transferir seu dinheiro:");
				System.out.println("   1 - Conta Poupanca:");
				System.out.print("   2 - Conta Corrente:\n-> ");
				int opcaoTransferencia = scanner.nextInt();

				System.out.print("Digite o valor: ");
				double valor = scanner.nextDouble();

				if (opcaoTransferencia == 1) {
					contaCorrente.realizarTransferencia(contaPoupanca, valor);
				} else if (opcaoTransferencia == 2) {
					contaPoupanca.realizarTransferencia(contaCorrente, valor);
				} else {
					System.out.println("Opção errada! Tente Novamente.");
				}
				break;
			case 5:
				for (Object ext : contaCorrente.getExtratos()) {
					System.out.println(ext);
				}
				break;
			case 6:
				System.out.println("1 - Você quer fazer um emprestimo: ");
				System.out.print("2 - Você quer consultar as parcelas:\n-> ");
				int opcaoEmprestimo = scanner.nextInt();

				if (opcaoEmprestimo == 1) {
					double quantia;
					int prazo;
					
					do {
						System.out.print("\nQual valor do emprestimo: R$ ");
						quantia = scanner.nextDouble();
						
						System.out.print("Qual o prazo: ");
						prazo = scanner.nextInt();
						
						if (quantia < 0.0) {
							System.out.println("\nValor inválido. Valor menor que R$ 0.00");
						}
						
						if (prazo < 1) {
							System.out.println("\nPrazo inválido. Tente Novamente!");
						}
						
					} while (quantia < 0.0 || prazo < 1);
					
					contaCorrente.realizarEmprestimo(quantia, prazo);
				}
				
				else if (opcaoEmprestimo == 2) {
					contaCorrente.fluxoParcelas();
				}
				
				else {
					System.out.println("Essa opção não existe!");
				}
				break;
				
			case 0:
				System.out.println("Saindo do sistema...");
				break loopPrincipal;
			}
		}
	}
}
