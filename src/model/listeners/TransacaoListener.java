package model.listeners;

import model.entities.Transacao;

public interface TransacaoListener {

	void transacaoRealizada(Transacao transacao);
}
