package partesInterface;

import cip.StandardPortOutbox;
import classes.Faixa;
import interfaces.InterfaceLogica;

public class PortInterfaceOutbox extends StandardPortOutbox implements InterfaceLogica {

	@Override
	public void notificar(String msg) {

		((PortLogica) externalPort).notificar(msg);
	}

	@Override
	public void atualizarTabuleiro(Faixa[] tabuleiro) {

		((PortLogica) externalPort).atualizarTabuleiro(tabuleiro);
	}

	@Override
	public void solicitarAcordo() {

		((PortLogica) externalPort).solicitarAcordo();
	}

	@Override
	public void informarEmpate() {

		((PortLogica) externalPort).informarEmpate();
	}

	@Override
	public void informarVencedor() {

		((PortLogica) externalPort).informarVencedor();
	}

}
