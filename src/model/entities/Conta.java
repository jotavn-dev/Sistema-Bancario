package model.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Conta {
	
	protected final int HORA_INICIO_NOTURNO = 20;
	protected final double LIMITE_DEPOSITO_DIURNO = 10000.0;
	protected final double LIMITE_DEPOSITO_NORTUNO = 1000.0;
	
	private Integer numeroConta;
	private String agencia;
	private double saldo;
	private double limiteEmprestimo;
	
	private Cliente cliente;
	
	private List<Transacao> consultarExtratos = new ArrayList<>();
	
	private Map<String, String> fluxoParcelas = new HashMap<>();

	public Conta(Integer numeroConta, String agencia, Double saldo, Cliente cliente) {
		this.numeroConta = numeroConta;
		this.agencia = agencia;
		this.saldo = 100000.0;
		this.cliente = cliente;
	}

	public Integer getNumeroConta() {
		return numeroConta;
	}

	public String getAgencia() {
		return agencia;
	}

	public Double getSaldo() {
		return saldo;
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public List<Transacao> getConsultarExtratos() {
		return consultarExtratos;
	}
	
	public Map<String, String> getFluxoParcelas() {
		return fluxoParcelas;
	}
	
	public double getLimiteEmprestimo() {
		return limiteEmprestimo;
	}
	
	private void calcularLimiteEmprestimo(int meses) {
		double trintaPorCento = 0.30;
		
		double limiteEmprestimo = saldo * trintaPorCento * meses;
		
		this.limiteEmprestimo = limiteEmprestimo;
	}
	
	public void realizarSaque(double valor) {
		
		if (this.concederSaque(valor)) {
			saldo -= valor;
			System.out.println("Saque realizado com sucesso!\n");
			consultarExtratos.add(new Transacao.Builder()
					.setNumeroConta(this.numeroConta)
					.setCliente(this.cliente)
					.setData(LocalDateTime.now())
					.setOperacao("Saque")
					.setValor(valor)
					.build());
		}
		else {
			if (valor <= 0.0) {
				System.out.println("Saque negado. O valor é menor que R$ 0.00.");
			}
			
			if (valor > getSaldo()) {
				System.out.println("Saque negado. Saldo insuficiente!");
			}		
		}
	}
	
	public void realizarDeposito(double valor) {
		int hora = LocalDateTime.now().getHour();
		
		if (this.concederDeposito(valor)) {
			saldo += valor;
			System.out.println("Depósito realizado com sucesso!\n");
			consultarExtratos.add(new Transacao.Builder()
					.setNumeroConta(this.numeroConta)
					.setCliente(cliente)
					.setData(LocalDateTime.now())
					.setOperacao("Deposito")
					.setValor(valor)
					.build());
		}
		else {
			if (valor <= 0.0) {
				System.out.println("Saque negado. Valor menor que R$ 0.00.");
			}
			
			if (hora < HORA_INICIO_NOTURNO && valor > LIMITE_DEPOSITO_DIURNO) {
				System.out.println("Saque negado. O valor ultrapassou o limite diuno padrão de R$ " + LIMITE_DEPOSITO_DIURNO);
			}
			
			if (hora >= HORA_INICIO_NOTURNO && valor > LIMITE_DEPOSITO_NORTUNO) {
				System.out.println("Saque negado. O valor ultrapassou o limte nortuno padrão de R$ " + LIMITE_DEPOSITO_NORTUNO);
			}
		}
	}
	
	public void realizarEmprestimo(double subValor, int meses) {
		calcularLimiteEmprestimo(meses);

		if (concederEmprestimo(subValor, meses)) {
			saldo += subValor;
			double jurosComposto = subValor * Math.pow(1 + taxa(subValor), meses);
			double parcelas = jurosComposto / meses;
			
			for (int data = 1; data <= meses; data++) {
				fluxoParcelas.put(data + " - parcela", String.format("R$ %.2f", parcelas));
			}
			
			System.out.println("Emprestimo de R$ " + String.format("%.2f", subValor) + " aprovado.\n");
			
			consultarExtratos.add(new Transacao.Builder()
					.setNumeroConta(this.numeroConta)
					.setCliente(cliente)
					.setData(LocalDateTime.now())
					.setOperacao("Emprestimo")
					.setValor(subValor)
					.build());
		}
		else {
			if (subValor <= 0.0) {
				System.out.println("Emprestimo negado. Valor menor que R$ 0.00.");
			}
			
			if (meses <= 0) {
				System.out.println("Emprestimo negado. Prazo inválido.");
			}
			
			else if (subValor > getLimiteEmprestimo()) {
				System.out.println("Emprestimo negado. Valor ultrapassou seu limite de emprestimo de R$ "
					+ String.format("%.2f", getLimiteEmprestimo()));
			}
		}
	}
	
	public void realizarTransferencia(Conta conta, double valor) {
		if (concederTransferencia(valor)) {
			this.debitar(valor);
			conta.creditar(valor);
			System.out.println("Transferência realizada com sucesso!\n");
			
			consultarExtratos.add(new Transacao.Builder()
					.setNumeroConta(this.numeroConta)
					.setCliente(cliente)
					.setData(LocalDateTime.now())
					.setOperacao("Transferência")
					.setValor(valor)
					.build());
		}
		else {
			if (valor <= 0.0) {
				System.out.println("Transferência negada. Valor menor que R$ 0.00");
			}
			
			if (valor > saldo) {
				System.out.println("Transferência negada. Saldo insuficiente.");
			}
		}
	}
	
	private void creditar(double valor) {
		saldo += valor;
	}
	
	private void debitar(double valor) {
		saldo -= valor;
	}
	
	public void fluxoParcelas() {
		for (String consulta : fluxoParcelas.keySet()) {
			System.out.println(consulta + " - " + this.getFluxoParcelas().get(consulta));
		}
	}
	
	protected abstract boolean concederDeposito(double valor);
	
	protected abstract boolean concederSaque(double valor);
	
	protected abstract boolean concederEmprestimo(double valor, int meses);
	
	protected abstract boolean concederTransferencia(double valor);
	
	protected abstract Double taxa(double valor);
	
	@Override
	public String toString() {
		return "Titular: " + numeroConta +
				"\nAgencia: " + agencia +
				"\nSaldo: " + saldo;
	}
}
