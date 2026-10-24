package model.listeners;

import java.util.ArrayList;
import java.util.List;

import model.entities.Transacao;

public class ExtratoService {
	
	private List<TransacaoListener> listeners = new ArrayList<>();
	
	public ExtratoService() {
	}
	
	public void addListener(TransacaoListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(TransacaoListener listener) {
		listeners.remove(listener);
	}
	
	public void notifyExtrato(Transacao transacao) {
		for (TransacaoListener enviarTransacao : listeners) {
			enviarTransacao.transacaoRealizada(transacao);
		}
	}
}