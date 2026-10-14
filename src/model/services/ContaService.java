package model.services;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import model.entities.Cliente;
import model.entities.Conta;
import model.entities.ContaCorrente;
import model.entities.ContaPoupanca;
import model.entities.Transacao;

public class ContaService {

	private Scanner scanner;
	
	private Cliente cliente;
	
	private Conta contaCorrente = new ContaCorrente.Builder()
			.setNumero(1001).setAgencia("0002-4")
			.setTaxaPagamento(new TaxaPagamentoBrasil())
			.build();

	private Conta contaPoupanca = new ContaPoupanca.Builder()
			.setNumero(1003).setAgencia("0234-1")
			.setTaxaPagamento(new TaxaPagamentoBrasil())
			.build();
	
	private DateTimeFormatter fm1 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	public ContaService(Scanner scanner, Cliente cliente) {
		this.scanner = scanner;
		this.cliente = cliente;
		
		this.contaCorrente.setCliente(cliente);
		this.contaPoupanca.setCliente(cliente);
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void menu() {

		List<String> listMenu = Arrays.asList("Consultar saldo", "Depositar", "Sacar", "Transferir", "Emprestimo",
				"Extrato");

		loopPrincipal:
		while (true) {
			System.out.println("\n" + "=".repeat(16) + " MENU " + "=".repeat(15) + "\n");

			for (int menu = 0; menu < listMenu.size(); menu++) {
				System.out.println((menu + 1) + " - " + listMenu.get(menu));
			}
			System.out.println("0 - Sair");

			System.out.print("Escolha a opção: ");
			int opcao = scanner.nextInt();

			switch (opcao) {

			case 1:
				System.out.println("\nVocê qual conta você quer consultar o saldo:");
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
				System.out.print("\nQual valor do deposito: R$ ");
				contaCorrente.realizarDeposito(scanner.nextDouble());
				break;
			case 3:
				System.out.print("\nQual valor do saque: R$ ");
				contaCorrente.realizarSaque(scanner.nextDouble());
				break;
			case 4:
				System.out.print("\nDigite o valor: ");
				double valor = scanner.nextDouble();
				
				System.out.println("Seu dinheiro vai ser transferido para a poupanca:");
				
				contaCorrente.realizarTransferencia(contaPoupanca, valor);
				break;
			case 5:
				System.out.println("\n1 - Você quer fazer um emprestimo: ");
				System.out.print("2 - Você quer consultar as parcelas:\n-> ");
				int opcaoEmprestimo = scanner.nextInt();

				if (opcaoEmprestimo == 1) {
					System.out.print("\nQual valor do emprestimo: R$ ");
					double quantia = scanner.nextDouble();
					
					System.out.print("Qual o prazo: ");
					int prazo = scanner.nextInt();
					
					contaCorrente.realizarEmprestimo(quantia, prazo);
				}
				
				else if (opcaoEmprestimo == 2) {
					contaCorrente.fluxoParcelas();
				}
				
				else {
					System.out.println("Essa opção não existe!");
				}
				break;
				
			case 6:
				if (contaCorrente.getConsultarExtratos().isEmpty()) {
					System.out.println("Você não executou nenhuma operação!");
				}
				else {
					
					for (Transacao extrato : contaCorrente.getConsultarExtratos()) {
						System.out.println("\nNumero da conta: " + extrato.getNumeroConta());
						System.out.println("Nome: " + extrato.getCliente().getName());
						System.out.println("Cpf: " + extrato.getCliente().getCpf());
						System.out.println("Data: " + fm1.format(extrato.getData()));
						System.out.println("\nOperação: " + extrato.getOperacao());
						System.out.println("R$ " + String.format("%.2f", 
								extrato.getValor()) + "\n");
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
