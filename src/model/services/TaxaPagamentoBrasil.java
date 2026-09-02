package model.services;

public class TaxaPagamentoBrasil implements TaxaPagamento {

	public TaxaPagamentoBrasil() {
	}
	
	@Override
	public double tax(double quantia) {
		if (quantia <= 100.0) {
			return quantia * 0.02;
		}
		else {
			return quantia * 0.15;
		}
	}
}
