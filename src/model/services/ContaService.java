package model.services;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
			.setSaldo(1000.0).setLimiteCredito(2000.0)
			.setTaxaPagamento(new TaxaPagamentoBrasil())
			.build();

	public Conta contaPoupanca = new ContaPoupanca.Builder()
			.setNumero(1003).setAgencia("0234-1")
			.setSaldo(500.0)
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
				System.out.println("\nSaldo: R$ " + String.format("%.2f", contaCorrente.getSaldo()));
				break;
			case 2:
				System.out.print("Qual valor do deposito: R$ ");
				contaCorrente.realizarDeposito(scanner.nextDouble());
				break;
			case 3:
				System.out.println("Qual valor do saque: R$ ");
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

							for (int i = 0; i < prazo; i++) {
								double valorParcela = cc.realizarEmprestimo(quantia / prazo, (i + 1));
								parcelas.put((i + 1) + " - parcela", valorParcela);
							}
						} else {
							System.out.println("Emprestimo negado. Valor excede o limite disponível");
						}
					}
				} else if (opcaoEmprestimo == 2) {
					for (String parcela : parcelas.keySet()) {
						System.out.println(parcela + " - R$ " + String.format("%.2f\n", parcelas.get(parcela)));
					}
				}
				break;
			case 0:
				System.out.println("Saindo do sistema...");
				break loopPrincipal;
			}
		}
	}
}
