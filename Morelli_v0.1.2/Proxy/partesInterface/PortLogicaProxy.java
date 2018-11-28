package partesInterface;

import br.ufsc.inf.leobr.cliente.Jogada;
import cip.InterfacePort;
import classes.JogadaMorelli;
import estrutura.NetGames;
import interfaces.ProxyLogica;

public class PortLogicaProxy extends InterfacePort implements ProxyLogica {
	
	protected NetGames netGames;

	public PortLogicaProxy(String id) {

		this.id = id;
	}

	@Override
	public void initialize() {

		outbox = new PortLogicaProxyOutbox();
	}
	
	@Override
	public boolean conectar(String ip, String nome) {

		return netGames.conectar(ip, nome);
	}

	@Override
	public boolean desconectar() {

		return netGames.desconectar();
	}

	@Override
	public void iniciarPartida() {

		netGames.iniciarPartida();
	}

	@Override
	public void finalizarPartida() {

		netGames.finalizarPartida();
	}

	@Override
	public void enviarJogada(Jogada jogada) {

		netGames.enviarJogada((JogadaMorelli) jogada);
	}

	public void setReferenciaInterna(NetGames netGames) {

		this.netGames = netGames;
	}

}
