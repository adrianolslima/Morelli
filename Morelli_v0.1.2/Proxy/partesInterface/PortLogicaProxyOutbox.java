package partesInterface;

import cip.StandardPortOutbox;
import classes.JogadaMorelli;
import interfaces.LogicaProxy;

public class PortLogicaProxyOutbox extends StandardPortOutbox implements LogicaProxy {

	@Override
	public void receberSolicitacaoInicio(int ordem) {

		((PortProxy) externalPort).receberSolicitacaoInicio(ordem);
	}

	@Override
	public void receberJogada(JogadaMorelli Jogada) {

		((PortProxy) externalPort).receberJogada(Jogada);
	}

}
