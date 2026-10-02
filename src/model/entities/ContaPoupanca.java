package model.entities;

import java.time.LocalDateTime;

import model.services.TaxaPagamento;

public class ContaPoupanca extends Conta {

	private TaxaPagamento taxaPagamento;

	public ContaPoupanca(Integer numero, String agencia, Double saldo, TaxaPagamento taxaPagamento) {
		super(numero, agencia, saldo);
		this.taxaPagamento = taxaPagamento;
	}

	@Override
	public void addExtrato(Conta conta, Cliente cliente) {
		this.getExtratos().add(conta.getNumero());
		this.getExtratos().add(cliente.getName());
		this.getExtratos().add(cliente.getCpf());
	}

	@Override
	public boolean concederTransferencia(double valor) {
		return this.concederSaque(valor) && this.concederDeposito(valor);
	}

	@Override
	public boolean concederDeposito(double valor) {
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
	public boolean concederSaque(double valor) {
		return valor <= getSaldo();
	}

	@Override
	public Double taxa(double valor) {
		return 0.0;
	}

	public static class Builder {
		private Integer numero;
		private String agencia;
		private double saldo;
		private TaxaPagamento taxaPagamento;

		public Builder setNumero(Integer numero) {
			this.numero = numero;
			return this;
		}

		public Builder setAgencia(String agencia) {
			this.agencia = agencia;
			return this;
		}

		public Builder setSaldo(double saldo) {
			this.saldo = saldo;
			return this;
		}

		public Builder setTaxaPagamento(TaxaPagamento taxaPagamento) {
			this.taxaPagamento = taxaPagamento;
			return this;
		}

		public ContaPoupanca build() {
			return new ContaPoupanca(numero, agencia, saldo, taxaPagamento);
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
