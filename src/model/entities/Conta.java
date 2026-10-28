package model.entities;

import model.exceptions.OperacaoInvalidaException;
import model.listeners.ExtratoListener;
import model.listeners.ExtratoService;
import model.services.TaxaPagamentoBrasil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Conta {
	
	protected final int HORA_INICIO_NOTURNO = 20;
	protected final double LIMITE_DEPOSITO_DIURNO = 10000.0;
	protected final double LIMITE_DEPOSITO_NORTUNO = 1000.0;
	
	private Integer numeroConta;
	private String agencia;
	private double saldo;
	private double limiteEmprestimo;
	
	private Cliente clienteAutenticado;

	private final List<Emprestimo> emprestimos = new ArrayList<>();
	
	private final ExtratoService extrato = new ExtratoService();
	private final ExtratoListener extratoListener = new ExtratoListener();

	protected Conta(Integer numeroConta, String agencia, Double saldo, Cliente clienteAutenticado) {
		this.numeroConta = numeroConta;
		this.agencia = agencia;
		this.saldo = 100000.0;
		this.clienteAutenticado = clienteAutenticado;
		
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
	
	public Cliente getClienteAutenticado() {
		return clienteAutenticado;
	}

	public void setClienteAutenticado(Cliente clienteAutenticado) { this.clienteAutenticado = clienteAutenticado; }

	public List<Emprestimo> getEmprestimos() { return emprestimos; }

	public double getLimiteEmprestimo() { return limiteEmprestimo; }

	public ExtratoService getExtrato() {
		return extrato;
	}
	
	public ExtratoListener getExtratoListener() {
		return extratoListener;
	}
	
	public void realizarSaque(double valor) {

		if (valor <= 0.0) {
			throw new OperacaoInvalidaException("Valor do saque tem que ser maior que R$ 0.00.");
		}

		if (valor > getSaldo()) {
			throw new OperacaoInvalidaException("Saldo insuficiente");
		}

		if (!this.concederSaque(valor)) {
			throw new OperacaoInvalidaException("Saque não autorizado para este tipo de conta.");
		}

		if (this.concederSaque(valor)) {
			saldo -= valor;
			
			Transacao transacao = new Transacao.Builder()
					.setNumeroConta(this.numeroConta)
					.setCliente(this.clienteAutenticado)
					.setData(LocalDateTime.now())
					.setOperacao("Saque")
					.setValor(valor)
					.build();
		
			extrato.notifyExtrato(transacao);
		}
	}
	
	public void realizarDeposito(double valor) {
		int hora = LocalDateTime.now().getHour();

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

		if (!concederDeposito(valor)) {
			throw new OperacaoInvalidaException("Depósito não autorizado para este tipo de conta.");
		}

		if (this.concederDeposito(valor)) {
			saldo += valor;

			Transacao transacao = new Transacao.Builder()
					.setNumeroConta(this.numeroConta)
					.setCliente(this.clienteAutenticado)
					.setData(LocalDateTime.now())
					.setOperacao("Depósito")
					.setValor(valor)
					.build();

			extrato.notifyExtrato(transacao);
		}
	}
	
	public void realizarEmprestimo(double valor, int prazo) {
		calcularLimiteEmprestimo(prazo);

		if (valor <= 0.0) {
			throw new OperacaoInvalidaException("Valor do emprestimo tem que ser maior que R$ 0.00.");
		}

		if (prazo <= 0) {
			throw new OperacaoInvalidaException("Prazo inválido.");
		}

		if (valor > getLimiteEmprestimo()) {
			throw new OperacaoInvalidaException("Valor ultrapassou seu limite de emprestimo de R$ "
					+ String.format("%.2f", getLimiteEmprestimo()));
		}

		if (!concederEmprestimo(valor, prazo)) {
			throw new OperacaoInvalidaException("Emprestimo não autorizado para este tipo de conta.");
		}

		if (concederEmprestimo(valor, prazo)) {
			saldo += valor;
			Emprestimo emprestimo = new Emprestimo(valor, prazo, new TaxaPagamentoBrasil());
			emprestimos.add(emprestimo);

			emprestimo.calcularJuros();
			emprestimo.addParcela();
			
			Transacao transacao = new Transacao.Builder()
					.setNumeroConta(this.numeroConta)
					.setCliente(this.clienteAutenticado)
					.setData(LocalDateTime.now())
					.setOperacao("Emprestimo")
					.setValor(valor)
					.build();
			
			extrato.notifyExtrato(transacao);
		}
	}

	private void calcularLimiteEmprestimo(int prazo) {
		this.limiteEmprestimo = (saldo * 0.30) / prazo;
	}
	
	public void realizarTransferencia(Conta conta, double valor) {

		if (valor <= 0.0) {
			throw new OperacaoInvalidaException("Valor da transferência tem que ser maior que R$ 0.00");
		}

		if (valor > getSaldo()) {
			throw new OperacaoInvalidaException("Saldo insuficiente.");
		}

		if (!concederTransferencia(valor)) {
			throw new OperacaoInvalidaException("Transferência não autorizada para este tipo de conta");
		}

		if (concederTransferencia(valor)) {
			this.debitar(valor);
			conta.creditar(valor);
			
			Transacao transacao = new Transacao.Builder()
					.setNumeroConta(this.numeroConta)
					.setCliente(this.clienteAutenticado)
					.setData(LocalDateTime.now())
					.setOperacao("Transferência")
					.setValor(valor)
					.build();
			
			extrato.notifyExtrato(transacao);
		}
	}
	
	private void creditar(double valor) { saldo += valor; }
	
	private void debitar(double valor) { saldo -= valor; }
	
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
