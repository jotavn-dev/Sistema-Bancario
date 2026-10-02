package model.entities;

import java.util.ArrayList;
import java.util.List;

public abstract class Conta {

	private Integer numero;
	private String agencia;
	protected double saldo;
	
	private List<Object> extratos = new ArrayList<>();

	public Conta() {
	}

	public Conta(Integer numero, String agencia, Double saldo) {
		this.numero = numero;
		this.agencia = agencia;
		this.saldo = saldo;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public Double getSaldo() {
		return saldo;
	}

	public List<Object> getExtratos() {
		return extratos;
	}

	public void setExtratos(List<Object> extratos) {
		this.extratos = extratos;
	}
	
	public void realizarSaque(double subValor) {
		Double valorTotal = subValor + taxa(subValor);
		
		if (this.concederSaque(valorTotal)) {
			saldo -= valorTotal;
		}
	}
	
	public void realizarDeposito(double subValor) {
		Double valorTotal = subValor + taxa(subValor);
		
		if (this.concederDeposito(valorTotal)) {
			saldo += valorTotal;
		}
	}
	
	public void realizarTransferencia(Conta conta, double valor) {
		if (concederTransferencia(valor)) {
			this.realizarSaque(valor);
			conta.realizarDeposito(valor);
		}
	}

	public abstract void addExtrato(Conta conta, Cliente cliente);
	
	public abstract boolean concederDeposito(double valor);
	
	public abstract boolean concederSaque(double valor);
	
	public abstract boolean concederTransferencia(double valor);
	
	public abstract Double taxa(double valor);
	
	@Override
	public String toString() {
		return "Titular: " + numero +
				"\nAgencia: " + agencia +
				"\nSaldo: " + saldo;
	}
}
