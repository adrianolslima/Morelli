package partesInterface;

import cip.InterfacePort;
import classes.JogadaMorelli;
import estrutura.Tabuleiro;
import interfaces.LogicaProxy;

public class PortProxy extends InterfacePort implements LogicaProxy {

	protected Tabuleiro tabuleiro;
	
	public PortProxy(String id) {

		this.id = id;
	}

	@Override
	public void initialize() {

		outbox = new PortProxyOutbox();
	}

	@Override
	public void receberSolicitacaoInicio(int ordem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receberJogada(JogadaMorelli Jogada) {
		// TODO Auto-generated method stub
		
	}

	public void setReferenciaInterna(Tabuleiro tabuleiro) {

		this.tabuleiro = tabuleiro;
	}

}
