package model.entities;

public class ContaPoupanca extends Conta {
	
	public ContaPoupanca() {
	}

	public ContaPoupanca(Integer numero, Integer agencia, Double saldo) {
		super(numero, agencia, saldo);
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
	}
	
	@Override
	public void saque(double valor) {
		if (valor <= saldo) {
			saldo -= valor;
		}
		else {
			System.out.println("Saldo insuficiente!");
		}
	}
}
