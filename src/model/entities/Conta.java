package model.entities;

import java.time.LocalDateTime;
import java.util.*;

import model.exceptions.OperacaoInvalidaException;
import model.listeners.ExtratoListener;
import model.listeners.ExtratoService;

public abstract class Conta {
	
	protected final int HORA_INICIO_NOTURNO = 20;
	protected final double LIMITE_DEPOSITO_DIURNO = 10000.0;
	protected final double LIMITE_DEPOSITO_NORTUNO = 1000.0;
	
	private Integer numeroConta;
	private String agencia;
	private double saldo;
	private double limiteEmprestimo;
	
	private Cliente clienteLogado;
	
	private ExtratoService extrato = new ExtratoService();
	private ExtratoListener extratoListener = new ExtratoListener();
	
	private List<String> fluxoParcelas = new ArrayList<>();

	protected Conta(Integer numeroConta, String agencia, Double saldo, Cliente clienteLogado) {
		this.numeroConta = numeroConta;
		this.agencia = agencia;
		this.saldo = 100000.0;
		this.clienteLogado = clienteLogado;
		
		extrato.addListener(extratoListener);
	}

	public Integer getNumeroConta() {
		return numeroConta;
	}

	public String getAgencia() {
		return agencia;
	}

	public Double getSaldo() {
		return saldo;
	}
	
	public Cliente getClienteLogado() {
		return clienteLogado;
	}

	public void setClienteLogado(Cliente clienteLogado) {
		this.clienteLogado = clienteLogado;
	}
	
	public ExtratoService getExtrato() {
		return extrato;
	}
	
	public ExtratoListener getExtratoListener() {
		return extratoListener;
	}
	
	public List<String> getFluxoParcelas() {
		return fluxoParcelas;
	}
	
	public double getLimiteEmprestimo() {
		return limiteEmprestimo;
	}
	
	public void realizarSaque(double valor) {
		if (this.concederSaque(valor)) {
			saldo -= valor;
			
			Transacao transacao = new Transacao.Builder()
					.setNumeroConta(this.numeroConta)
					.setCliente(this.clienteLogado)
					.setData(LocalDateTime.now())
					.setOperacao("Saque")
					.setValor(valor)
					.build();
		
			extrato.notifyExtrato(transacao);
		}

		if (valor <= 0.0) {
			throw new OperacaoInvalidaException("Valor do saque tem que ser maior que R$ 0.00.");
		}

		if (valor > getSaldo()) {
			throw new OperacaoInvalidaException("Saldo insuficiente");
		}

		if (!this.concederSaque(valor)) {
			throw new OperacaoInvalidaException("Saque não autorizado para este tipo de conta.");
		}
	}
	
	public void realizarDeposito(double valor) {
		int hora = LocalDateTime.now().getHour();
		
		if (this.concederDeposito(valor)) {
			saldo += valor;

			Transacao transacao = new Transacao.Builder()
					.setNumeroConta(this.numeroConta)
					.setCliente(this.clienteLogado)
					.setData(LocalDateTime.now())
					.setOperacao("Depósito")
					.setValor(valor)
					.build();

			extrato.notifyExtrato(transacao);

		}
		if (valor <= 0.0) {
			throw new OperacaoInvalidaException("Valor do depósito tem que ser maior que R$ 0.00.");
		}

		if (hora < HORA_INICIO_NOTURNO && valor > LIMITE_DEPOSITO_DIURNO) {
			throw new OperacaoInvalidaException("Valor ultrapassou o limite diuno padrão de R$ "
					+ LIMITE_DEPOSITO_DIURNO);
		}

		if (hora >= HORA_INICIO_NOTURNO && valor > LIMITE_DEPOSITO_NORTUNO) {
			throw new OperacaoInvalidaException("Valor ultrapassou o limte nortuno padrão de R$ "
					+ LIMITE_DEPOSITO_NORTUNO);
		}

	}
	
	public void realizarEmprestimo(double subValor, int meses) {
		calcularLimiteEmprestimo(meses);

		if (concederEmprestimo(subValor, meses)) {
			saldo += subValor;
			double jurosComposto = subValor * Math.pow(1 + taxa(subValor), meses);
			double parcelas = jurosComposto / meses;
			
			for (int data = 1; data <= meses; data++) {
				fluxoParcelas.add(data + " - parcela - R$ " +
						String.format("%.2f", parcelas));
			}
			
			Transacao transacao = new Transacao.Builder()
					.setNumeroConta(this.numeroConta)
					.setCliente(this.clienteLogado)
					.setData(LocalDateTime.now())
					.setOperacao("Emprestimo")
					.setValor(subValor)
					.build();
			
			extrato.notifyExtrato(transacao);
		}
		if (subValor <= 0.0) {
			throw new OperacaoInvalidaException("Valor do emprestimo tem que ser maior que R$ 0.00.");
		}

		if (meses <= 0) {
			throw new OperacaoInvalidaException("Prazo inválido.");
		}

		if (subValor > getLimiteEmprestimo()) {
			throw new OperacaoInvalidaException("Valor ultrapassou seu limite de emprestimo de R$ "
					+ String.format("%.2f", getLimiteEmprestimo()));
		}
	}
	
	private void calcularLimiteEmprestimo(int meses) {
		double trintaPorCento = 0.30;

        this.limiteEmprestimo = saldo * trintaPorCento * meses;
	}
	
	public void realizarTransferencia(Conta conta, double valor) {
		if (concederTransferencia(valor)) {
			this.debitar(valor);
			conta.creditar(valor);
			
			Transacao transacao = new Transacao.Builder()
					.setNumeroConta(this.numeroConta)
					.setCliente(this.clienteLogado)
					.setData(LocalDateTime.now())
					.setOperacao("Transferência")
					.setValor(valor)
					.build();
			
			extrato.notifyExtrato(transacao);
		}
		if (valor <= 0.0) {
			throw new OperacaoInvalidaException("Valor da transferência tem que ser maior que R$ 0.00");
		}

		if (valor > getSaldo()) {
			throw new OperacaoInvalidaException("Saldo insuficiente.");
		}
	}
	
	private void creditar(double valor) {
		saldo += valor;
	}
	
	private void debitar(double valor) {
		saldo -= valor;
	}

	public void fluxoParcelas() {
		for (String consultar : fluxoParcelas) {
			System.out.println(consultar);
		}
	}
	
	protected abstract boolean concederDeposito(double valor);
	
	protected abstract boolean concederSaque(double valor);
	
	protected abstract boolean concederEmprestimo(double valor, int meses);
	
	protected abstract boolean concederTransferencia(double valor);
	
	protected abstract Double taxa(double valor);
	
	@Override
	public String toString() {
		return "Titular: " + numeroConta +
				"\nAgencia: " + agencia +
				"\nSaldo: " + saldo;
	}
}
