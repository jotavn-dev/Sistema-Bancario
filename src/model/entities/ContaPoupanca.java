package model.entities;

import java.time.LocalDateTime;

import model.services.TaxaPagamento;

public class ContaPoupanca extends Conta {

	private TaxaPagamento taxaPagamento;

	private ContaPoupanca(Integer numero, String agencia, Double saldo, Cliente cliente, TaxaPagamento taxaPagamento) {
		super(numero, agencia, saldo, cliente);
		this.taxaPagamento = taxaPagamento;
	}

	@Override
	protected boolean concederDeposito(double valor) {
		int hora = LocalDateTime.now().getHour();

		Double limiteDeposito;
		if (hora >= HORA_INICIO_NOTURNO) {
			limiteDeposito = LIMITE_DEPOSITO_NORTUNO;
		} else {
			limiteDeposito = LIMITE_DEPOSITO_DIURNO;
		}

		return valor > 0.0 && valor <= limiteDeposito;
	}

	@Override
	protected boolean concederSaque(double valor) {
		return valor > 0.0 && valor <= getSaldo();
	}
	
	@Override
	protected boolean concederEmprestimo(double valor, int meses) {
		return valor > 0.0 && valor <= this.getLimiteEmprestimo() && meses > 0;
	}
	
	@Override
	protected boolean concederTransferencia(double valor) {
		return valor > 0.0 && valor <= this.getSaldo();
	}

	@Override
	protected Double taxa(double valor) {
		return 0.0;
	}

	public static class Builder {
		private Integer numero;
		private String agencia;
		private double saldo;
		private Cliente cliente;
		private TaxaPagamento taxaPagamento;

		public Builder setNumero(Integer numero) {
			this.numero = numero;
			return this;
		}

		public Builder setAgencia(String agencia) {
			this.agencia = agencia;
			return this;
		}
		
		public Builder setCliente(Cliente cliente) {
			this.cliente = cliente;
			return this;
		}

		public Builder setTaxaPagamento(TaxaPagamento taxaPagamento) {
			this.taxaPagamento = taxaPagamento;
			return this;
		}

		public ContaPoupanca build() {
			return new ContaPoupanca(numero, agencia, saldo, cliente, taxaPagamento);
		}
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public TaxaPagamento getTaxaPagamento() {
		return taxaPagamento;
	}

	public void setTaxaPagamento(TaxaPagamento taxaPagamento) {
		this.taxaPagamento = taxaPagamento;
	}
}
