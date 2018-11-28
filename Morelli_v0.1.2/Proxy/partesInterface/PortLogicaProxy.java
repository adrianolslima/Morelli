package partesInterface;

import br.ufsc.inf.leobr.cliente.Jogada;
import cip.InterfacePort;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void iniciarPartida() {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void finalizarPartida() {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public void enviarJogada(Jogada jogada) {
		// TODO Auto-generated method stub
		
	}

	public void setReferenciaInterna(NetGames netGames) {

		this.netGames = netGames;
	}

	@Override
	public void reiniciarPartida() {
		// TODO Auto-generated method stub
		
	}

}
