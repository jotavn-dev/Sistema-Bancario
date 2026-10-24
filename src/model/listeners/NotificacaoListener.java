package model.listeners;

import model.entities.Transacao;

public class NotificacaoListener implements TransacaoListener {

	@Override
	public void transacaoRealizada(Transacao transacao) {
		System.out.printf("\n%s de R$ %.2f realizado com sucesso!\n", 
				transacao.getOperacao(),
				transacao.getValor());		
	}
}
