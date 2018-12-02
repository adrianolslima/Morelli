package partesInterface;

import cip.StandardPortOutbox;
import classes.JogadaMorelli;
import interfaces.LogicaInterface;

public class PortLogicaOutbox extends StandardPortOutbox implements LogicaInterface {

	@Override
	public boolean conectar(String ip, String nome) {

		return ((PortInterface) externalPort).conectar(ip, nome);
	}

	@Override
	public boolean desconectar() {

		return ((PortInterface) externalPort).desconectar();
	}

	@Override
	public String getAjuda() {
		return ((PortInterface) externalPort).getAjuda();
	}

	@Override
	public boolean iniciarPartida() {

		return ((PortInterface) externalPort).iniciarPartida();
	}

	@Override
	public boolean reiniciarPartida() {

		return ((PortInterface) externalPort).reiniciarPartida();
	}

	@Override
	public void enviarJogada(JogadaMorelli jogada) {

		((PortInterface) externalPort).enviarJogada(jogada);
	}

}
