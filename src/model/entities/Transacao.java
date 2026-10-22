package model.entities;

import java.time.LocalDateTime;

public class Transacao {

	private String operacao;
	private Double valor;
	private LocalDateTime data;
	private Integer numeroConta;
	
	private Cliente clienteLogado;
	
	private Transacao(String operacao, Double valor, LocalDateTime data, Integer numeroConta, Cliente clienteLogado) {
		this.operacao = operacao;
		this.valor = valor;
		this.data = data;
		this.numeroConta = numeroConta;
		this.clienteLogado = clienteLogado;
	}
	
	public String getOperacao() {
		return operacao;
	}

	public Double getValor() {
		return valor;
	}

	public LocalDateTime getData() {
		return data;
	}
	
	public Cliente getCliente() {
		return clienteLogado;
	}
	
	public Integer getNumeroConta() {
		return numeroConta;
	}

	public static class Builder {
		private String operacao;
		private Double valor;
		private LocalDateTime data;
		private Integer numeroConta;
		private Cliente clienteLogado;
		
		public Builder setOperacao(String operacao) {
			this.operacao = operacao;
			return this;
		}
		
		public Builder setValor(Double valor) {
			this.valor = valor;
			return this;
		}
		
		public Builder setData(LocalDateTime data) {
			this.data = data;
			return this;
		}
		
		public Builder setNumeroConta(Integer numeroConta) {
			this.numeroConta = numeroConta;
			return this;
		}
		
		public Builder setCliente(Cliente clienteLogado) {
			this.clienteLogado = clienteLogado;
			return this;
		}
		
		public Transacao build() {
			return new Transacao(operacao, valor, data, numeroConta, clienteLogado);
		}
	}
}

	
