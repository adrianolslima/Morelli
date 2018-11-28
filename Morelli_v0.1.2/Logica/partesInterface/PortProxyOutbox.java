package partesInterface;

import br.ufsc.inf.leobr.cliente.Jogada;
import cip.StandardPortOutbox;
import interfaces.ProxyLogica;

public class PortProxyOutbox extends StandardPortOutbox implements ProxyLogica {

	@Override
	public boolean conectar(String ip, String nome) {
		
		return ((PortLogicaProxy) externalPort).conectar(ip, nome);
	}

	@Override
	public boolean desconectar() {

		return ((PortLogicaProxy) externalPort).desconectar();
	}

	@Override
	public void iniciarPartida() {

		((PortLogicaProxy) externalPort).iniciarPartida();
	}

//	@Override
//	public void finalizarPartida() {
//
//		((PortLogicaProxy) externalPort).finalizarPartida();
//	}

	@Override
	public void enviarJogada(Jogada jogada) {

		((PortLogicaProxy) externalPort).enviarJogada(jogada);
	}

	@Override
	public void reiniciarPartida() {
		// TODO Auto-generated method stub
		
	}

}
