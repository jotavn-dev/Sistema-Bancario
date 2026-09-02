package model.entities;

import java.time.LocalDate;

public class Cliente {

	private String name;
	private String cpf;
	
	private Conta conta;
	
	public Cliente() {
	}

	public Cliente(String name, String cpf) {
		this.name = name;
		this.cpf = cpf;
	}

	public String getName() {
		return name;
	}

	public String getCpf() {
		return cpf;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}
	
	
}
