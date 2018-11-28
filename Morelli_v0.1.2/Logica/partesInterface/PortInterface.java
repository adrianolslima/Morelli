package partesInterface;

import cip.InterfacePort;
import classes.TipoJogada;
import estrutura.Tabuleiro;
import interfaces.LogicaInterface;

public class PortInterface extends InterfacePort implements LogicaInterface {
	
	protected Tabuleiro tabuleiro;
	
	public PortInterface(String id) {

		this.id = id;
	}

	@Override
	public void initialize() {

		outbox = new PortInterfaceOutbox();
	}

	@Override
	public boolean conectar(String ip, String nome) {

		return tabuleiro.conectar(ip, nome);
	}

	@Override
	public boolean desconectar() {

		return tabuleiro.desconectar();
	}

	@Override
	public String getAuda() {

		return tabuleiro.getAjuda();
	}

	@Override
	public boolean iniciarPartida() {

		return tabuleiro.solicitarInicioPartida();
	}

	@Override
	public boolean reiniciarPartida() {
		
		return tabuleiro.reiniciarPartida();
	}

	@Override
	public void enviarJogada(TipoJogada tipoJogada) {

		tabuleiro.enviarJogada(tipoJogada);
	}

	public void setReferenciaInterna(Tabuleiro tabuleiro) {

		this.tabuleiro = tabuleiro;
	}

}
