package model.services;

import application.LeitorConsole;
import model.entities.Cliente;
import model.entities.Conta;
import model.exceptions.OperacaoInvalidaException;
import model.factories.ContaFactory;
import model.listeners.ExtratoListener;

import java.util.Arrays;
import java.util.List;

public class ContaService {

	@SuppressWarnings("unused")
	private final Cliente clienteAutenticado;

	private final LeitorConsole leitor;
	
	private final Conta contaCorrente = ContaFactory.contaCorrente();

	private final Conta contaPoupanca = ContaFactory.contaPoupanca();
	
	public ContaService(LeitorConsole leitor, Cliente clienteAutenticado) {
		this.leitor = leitor;
		this.clienteAutenticado = clienteAutenticado;

		this.contaCorrente.setClienteLogado(clienteAutenticado);
		this.contaPoupanca.setClienteLogado(clienteAutenticado);
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
			int opcao = leitor.lerInteiro();

			switch (opcao) {

			case 1:
				System.out.println("\nVocê qual conta você quer consultar o saldo:");
				System.out.println("   1 - Conta Poupança:");
				System.out.print("   2 - Conta Corrente:\n-> ");
				int opcaoSaldo = leitor.lerInteiro();

				if (opcaoSaldo == 1) {
					System.out.println("\nSaldo: R$ " + String.format("%.2f", contaPoupanca.getSaldo()));

				} else if (opcaoSaldo == 2) {
					System.out.println("\nSaldo: R$ " + String.format("%.2f", contaCorrente.getSaldo()));

				} else {
					System.out.println("Opcão inválida. Tente Novamente!");
				}
				break;

			case 2:
				System.out.print("\nQual valor do deposito: R$ ");
				try {
					double valor = leitor.lerValor();
					contaCorrente.realizarDeposito(valor);
					System.out.printf("\nDepósito de R$ %.2f realizado com sucesso!", valor);
				}
				catch (OperacaoInvalidaException e) {
					System.out.println("Erro: " + e.getMessage());
				}
				break;

			case 3:
				System.out.print("\nQual valor do saque: R$ ");
				try {
					double valor = leitor.lerValor();
					contaCorrente.realizarSaque(valor);
					System.out.printf("\nSaque de R$ %.2f realizado com sucesso!", valor);
				}
				catch (OperacaoInvalidaException e) {
					System.out.println("Erro: " + e.getMessage());
				}
				break;

			case 4:
				System.out.print("\nDigite o valor: ");
				try {
					double valor = leitor.lerValor();
					contaCorrente.realizarTransferencia(contaPoupanca, valor);
					System.out.printf("\nTransferência de R$ %.2f realizada com sucesso!", valor);
                }
				catch (OperacaoInvalidaException e) {
					System.out.println("Erro: " + e.getMessage());
				}
				break;

			case 5:
				System.out.println("\n1 - Você quer fazer um emprestimo: ");
				System.out.print("2 - Você quer consultar as parcelas:\n-> ");
				int opcaoEmprestimo = leitor.lerInteiro();

				try {
					if (opcaoEmprestimo == 1) {
						System.out.print("\nQual valor do emprestimo: R$ ");
						double valor = leitor.lerValor();

						System.out.print("Qual o prazo: ");
						int prazo = leitor.lerInteiro();

						contaCorrente.realizarEmprestimo(valor, prazo);

					} else if (opcaoEmprestimo == 2) {
						contaCorrente.fluxoParcelas();

					} else {
						System.out.println("Essa opção não existe!");
					}
				}
				catch (OperacaoInvalidaException e) {
					System.out.println("Erro: " + e.getMessage());
				}
				break;

			case 6:
				ExtratoListener extratoListener = contaCorrente.getExtratoListener();

				if (extratoListener.getExtratos().isEmpty()) {
					System.out.println("\nVocê não executou nenhuma operação!");
				}

				else {
					extratoListener.exibirExtrato();
				}
				break;

			case 0:
				System.out.println("Saindo do sistema...");
				break loopPrincipal;
			}
		}
	}
}
