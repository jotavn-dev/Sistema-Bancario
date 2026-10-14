package model.entities;

import java.time.LocalDateTime;

public class Transacao {

	private String operacao;
	private Double valor;
	private LocalDateTime data;
	private Integer numeroConta;
	
	private Cliente cliente;
	
	private Transacao(String operacao, Double valor, LocalDateTime data, Integer numeroConta, Cliente cliente) {
		this.operacao = operacao;
		this.valor = valor;
		this.data = data;
		this.numeroConta = numeroConta;
		this.cliente = cliente;
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
		return cliente;
	}
	
	public Integer getNumeroConta() {
		return numeroConta;
	}

	public static class Builder {
		private String operacao;
		private Double valor;
		private LocalDateTime data;
		private Integer numeroConta;
		private Cliente cliente;
		
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
		
		public Builder setCliente(Cliente cliente) {
			this.cliente = cliente;
			return this;
		}
		
		public Transacao build() {
			return new Transacao(operacao, valor, data, numeroConta, cliente);
		}
	}
}

	
