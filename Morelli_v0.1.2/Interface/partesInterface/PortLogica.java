package partesInterface;

import cip.InterfacePort;
import classes.Faixa;
import estrutura.AtorJogador;
import interfaces.InterfaceLogica;

public class PortLogica extends InterfacePort implements InterfaceLogica {
	
	protected AtorJogador jogador;

	public PortLogica(String id) {

		this.id = id;
	}
	
	@Override
	public void initialize() {

		outbox = new PortLogicaOutbox();
	}

	@Override
	public void notificar(String msg) {

		jogador.notificar(msg);
	}

	@Override
	public void atualizarTabuleiro(Faixa[] tabuleiro) {

		jogador.atualizarTabuleiro(tabuleiro);
	}

	@Override
	public void solicitarAcordo() {

		jogador.solicitarAcordo();
	}

	@Override
	public void informarEmpate() {

		jogador.informarEmpate();
	}

	@Override
	public void informarVencedor() {

		jogador.informarVencedor();
	}

	public void setReferenciaInterna(AtorJogador jogador) {

		this.jogador = jogador;
	}

}
