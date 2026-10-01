package model.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import model.services.TaxaPagamento;
import model.services.TaxaPagamentoBrasil;

public class ContaCorrente extends Conta {

	private DateTimeFormatter fm1 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	private double limiteCredito;
	
	private TaxaPagamento taxaPagamento;

	private ContaCorrente(Integer numero, String agencia, Double saldo, double limiteCredito, TaxaPagamento taxaPagamento) {
		super(numero, agencia, saldo);
		this.limiteCredito = limiteCredito;
		this.taxaPagamento = taxaPagamento;
	}

	public double getLimiteCredito() {
		return limiteCredito;
	}

	public void setLimiteCredito(double limiteCredito) {
		this.limiteCredito = limiteCredito;
	}
	
	public boolean concederEmprestimo(double valor) {
		if (valor > 0 && valor <= limiteCredito) {
			this.saldo += valor;
			return true;
		}
		return false;
	}
	
	public Double realizarEmprestimo(double valor, int meses) {
		if (concederEmprestimo(valor)) {
			return valor * Math.pow(1.02, meses);
		}
		return 0.0;
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
		return valor > 0.0;
	}
	
	@Override
	public boolean concederSaque(double valor) {
		return valor <= getSaldo() + getLimiteCredito();
	}
	
	public static class Builder {
		private Integer numero;
		private String agencia;
		private double saldo;
		private double limiteCredito;
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
		
		public Builder setLimiteCredito(double limiteCredito) {
			this.limiteCredito = limiteCredito;
			return this;
		}
		
		public Builder setTaxaPagmento(TaxaPagamento taxaPagamento) {
			this.taxaPagamento = taxaPagamento;
			return this;
		}
		
		public ContaCorrente build() {
			return new ContaCorrente(numero, agencia, saldo, limiteCredito, taxaPagamento);
		}
	}
	
	@Override
	public String toString() {
		return super.toString() +
				"\nLimiteCredito: " + limiteCredito;
	}
}
