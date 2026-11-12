package model.services;

public class TaxaPagamentoBrasil implements TaxaPagamento {

	public TaxaPagamentoBrasil() {
	}
	
	@Override
	public double taxa(double valor) {
		if (valor <= 100.0) {
			return 0.0115;
		}
		else {
			return 0.02;
		}
	}
}
