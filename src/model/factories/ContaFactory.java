package model.factories;

import model.entities.Conta;
import model.entities.ContaCorrente;
import model.entities.ContaPoupanca;
import model.services.TaxaPagamentoBrasil;

public class ContaFactory {

	public static Conta contaCorrente() {
		
		return new ContaCorrente.Builder()
				.setNumero(1001)
				.setAgencia("0002-4")
				.setTaxaPagamento(new TaxaPagamentoBrasil())
				.build();
	}
	
	public static Conta contaPoupanca() {
		
		return new ContaPoupanca.Builder()
				.setNumero(1003)
				.setAgencia("0234-1")
				.setTaxaPagamento(new TaxaPagamentoBrasil())
				.build();
	}
}