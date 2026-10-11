package model.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Conta {
	
	protected final int HORA_INICIO_NOTURNO = 20;
	protected final double LIMITE_DEPOSITO_DIURNO = 10000.0;
	protected final double LIMITE_DEPOSITO_NORTUNO = 1000.0;
	
	private Integer numero;
	private String agencia;
	private double saldo;
	private double limiteEmprestimo;
	
	private List<Object> extratos = new ArrayList<>();
	
	private Map<String, String> fluxoParcelas = new HashMap<>();
	
	protected DateTimeFormatter fm1 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	public Conta(Integer numero, String agencia, Double saldo) {
		this.numero = numero;
		this.agencia = agencia;
		this.saldo = 1000.0;
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
	
	private void calcularLimiteEmprestimo(int meses) {
		double trintaPorCento = 0.30;
		
		double limiteEmprestimo = saldo * trintaPorCento * meses;
		
		this.limiteEmprestimo = limiteEmprestimo;
	}
	
	public void realizarSaque(double subValor) {
		Double valorTotal = subValor;
		
		if (this.concederSaque(valorTotal)) {
			saldo -= valorTotal;
			System.out.println("Saque realizado com sucesso!");
		}
		else {
			if (subValor <= 0.0) {
				System.out.println("Saque negado. O valor é menor que R$ 0.00.");
			}
			
			if (subValor > getSaldo()) {
				System.out.println("Saque negado. Saldo insuficiente!");
			}		
		}
	}
	
	public void realizarDeposito(double subValor) {
		int hora = LocalDateTime.now().getHour();
		
		if (this.concederDeposito(subValor)) {
			saldo += subValor;
			System.out.println("Depósito realizado com sucesso!");
		}
		else {
			if (subValor <= 0.0) {
				System.out.println("Saque negado. Valor menor que R$ 0.00.");
			}
			
			if (hora < HORA_INICIO_NOTURNO && subValor > LIMITE_DEPOSITO_DIURNO) {
				System.out.println("Saque negado. O valor ultrapassou o limite diuno padrão de R$ " + LIMITE_DEPOSITO_DIURNO);
			}
			
			if (hora >= HORA_INICIO_NOTURNO && subValor > LIMITE_DEPOSITO_NORTUNO) {
				System.out.println("Saque negado. O valor ultrapassou o limte nortuno padrão de R$ " + LIMITE_DEPOSITO_NORTUNO);
			}
		}
	}
	
	public void realizarTransferencia(Conta conta, double valor) {
		Double valorTotal = valor += valor * taxa(valor);
		
		if (concederTransferencia(valorTotal)) {
			this.realizarSaque(valorTotal);
			conta.realizarDeposito(valor);
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
			
			System.out.println("Emprestimo de R$ " + String.format("%.2f", subValor) + " aprovado.");
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
	
	public void fluxoParcelas() {
		for (String consulta : fluxoParcelas.keySet()) {
			System.out.println(consulta + " - " + this.getFluxoParcelas().get(consulta));
		}
	}

	public abstract void addExtrato(Conta conta, Cliente cliente);
	
	protected abstract boolean concederDeposito(double valor);
	
	protected abstract boolean concederSaque(double valor);
	
	protected abstract boolean concederTransferencia(double valor);
	
	protected abstract boolean concederEmprestimo(double valor, int meses);
	
	protected abstract Double taxa(double valor);
	
	@Override
	public String toString() {
		return "Titular: " + numero +
				"\nAgencia: " + agencia +
				"\nSaldo: " + saldo;
	}

	public Map<String, String> getFluxoParcelas() {
		return fluxoParcelas;
	}
	
	public double getLimiteEmprestimo() {
		return limiteEmprestimo;
	}
}
