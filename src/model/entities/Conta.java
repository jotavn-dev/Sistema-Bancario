package model.entities;

import java.time.LocalDateTime;
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

	public abstract void addExtrato(Conta conta, Cliente cliente);
	
	public abstract void deposito(double valor);
	
	public abstract void saque(double valor);
	
	public abstract void transferir(Conta conta, double valor);
	
	@Override
	public String toString() {
		return "Titular: " + numero +
				"\nAgencia: " + agencia +
				"\nSaldo: " + saldo;
	}
}
