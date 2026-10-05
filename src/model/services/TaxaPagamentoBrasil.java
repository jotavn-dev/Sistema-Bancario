package model.services;

public class TaxaPagamentoBrasil implements TaxaPagamento {

	public TaxaPagamentoBrasil() {
	}
	
	@Override
	public double tax(double quantia) {
		if (quantia <= 100.0) {
			return 0.0115;
		}
		else {
			return 0.02;
		}
	}
}
