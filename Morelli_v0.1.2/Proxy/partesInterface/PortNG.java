package partesInterface;

import br.ufsc.inf.leobr.cliente.Jogada;
import br.ufsc.inf.leobr.cliente.OuvidorProxy;
import cip.InterfacePort;
import classes.JogadaMorelli;
import estrutura.NetGames;
import interfaces.ProxyNG;

public class PortNG extends InterfacePort implements ProxyNG, OuvidorProxy {
	
	protected NetGames netGames;

	public PortNG(String id) {

		this.id = id;
	}

	@Override
	public void initialize() {

		outbox = new PortNGOutbox();
		((PortNGOutbox) outbox).initialize();
	}

	public void setReferenciaInterna(NetGames netGames) {

		this.netGames = netGames;
		((PortNGOutbox) outbox).connectToNG(this);
	}
	
	@Override
	public void iniciarNovaPartida(Integer posicao) {

		netGames.iniciarNovaPartida(posicao);
	}

	@Override
	public void finalizarPartidaComErro(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receberMensagem(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receberJogada(Jogada jogada) {

		netGames.receberJogada(jogada);
	}

	@Override
	public void tratarConexaoPerdida() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tratarPartidaNaoIniciada(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receberSolicitacaoInicio(int ordem) {

		netGames.iniciarNovaPartida(ordem);
	}

	@Override
	public void receberJogada(JogadaMorelli jogada) {

		netGames.receberJogada(jogada);		
	}

}
