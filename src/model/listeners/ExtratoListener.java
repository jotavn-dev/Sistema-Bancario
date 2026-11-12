package model.listeners;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import model.entities.Transacao;

public class ExtratoListener implements TransacaoListener {
	
	private List<Transacao> extratos = new ArrayList<>();
	
	public List<Transacao> getExtratos() {
		return extratos;
	}
	
	@Override
	public void transacaoRealizada(Transacao transacao) {
		extratos.add(transacao);
	}
	
	public void exibirExtrato() {
		ExtratoFormatado formatador = new ExtratoFormatado(extratos);
		formatador.preparaExtrato();
	}
}

class ExtratoFormatado {
	
	private final DateTimeFormatter fm1 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	private List<Transacao> extratos;

	public ExtratoFormatado(List<Transacao> extratos) {
		this.extratos = extratos;
	}
	
	public void preparaExtrato() {
		for(Transacao extrato : extratos) {
			System.out.println("\nNúmero da conta: " + extrato.getNumeroConta());
			System.out.println("\nNome: " + extrato.getCliente().getNome());
			System.out.println("\nCpf: " + extrato.getCliente().getCpf());
			System.out.println("\nData: " + fm1.format(extrato.getData()));
			System.out.println("\nOperação: " + extrato.getOperacao());
			System.out.println("\nR$ " + String.format("%.2f", extrato.getValor()));
		}
	}
}
