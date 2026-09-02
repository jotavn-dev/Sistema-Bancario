package model.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ContaCorrente extends Conta {

	private DateTimeFormatter fm1 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	private double limiteCredito;
	
	public ContaCorrente() {
	}

	public ContaCorrente(Integer numero, Integer agencia, Double saldo, double limiteCredito) {
		super(numero, agencia, saldo);
		this.limiteCredito = limiteCredito;
	}

	public double getLimteCredito() {
		return limiteCredito;
	}

	public void setLimteCredito(double limiteCredito) {
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
	public void transferir(Conta conta, double valor) {
		this.saque(valor);
		conta.deposito(valor);
	}
	
	@Override
	public void deposito(double valor) {
		saldo += valor;
		this.getExtratos().add("Deposito: R$ " + String.format("%.2f", valor));
		this.getExtratos().add(fm1.format(LocalDateTime.now()));
		System.out.println("Depósito realizado com sucesso!");
	}
	
	@Override
	public void saque(double valor) {
		if (valor <= saldo) {
			this.saldo -= valor + 2.00;
			this.getExtratos().add("Saque: R$ " + String.format("%.2f", valor));
			this.getExtratos().add(fm1.format(LocalDateTime.now()));
			System.out.println("Saque realizado com sucesso!");
		}
		else {
			System.out.println("Saldo insuficiente!");
		}
	}
}
