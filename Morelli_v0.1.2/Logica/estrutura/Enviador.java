package estrutura;

import java.util.Optional;
import java.util.ResourceBundle;

import classes.JogadaMorelli;
import classes.TipoJogada;
import partesInterface.PortInterfaceOutbox;
import partesInterface.PortProxyOutbox;

public class Enviador {

	protected ResourceBundle msgs;
	Tabuleiro tabuleiro;

	public Enviador(ResourceBundle msgs, Tabuleiro tabuleiro) {

		this.msgs = msgs;
		this.tabuleiro = tabuleiro;
	}

	/*--- Caso de uso: enviar jogada ---*/
	public JogadaMorelli tratarJogada(TipoJogada tipo) {

		JogadaMorelli jogada;

		switch (tipo) {
		
		case realizarAcordo:
			jogada = new JogadaMorelli(TipoJogada.realizarAcordo);
			break;
			
		case acordoAceito:
			jogada = new JogadaMorelli(TipoJogada.acordoAceito);
			tabuleiro.finalizaPartida();
			break;
			
		case acordoNegado:
			jogada = new JogadaMorelli(TipoJogada.acordoNegado);
			break;
			
		case abandonarPartida:
			jogada = abandonarPartida(tipo);
			break;
			
		case atualizarTabuleiro:
			jogada = new JogadaMorelli(TipoJogada.atualizarTabuleiro, tabuleiro.getTabuleiro());
			break;
			
		default:
			jogada = new JogadaMorelli(TipoJogada.encerramento);
			String msg = msgs.getString("TheMatchIsOver");
			tabuleiro.comunicar(true, msg);
			tabuleiro.finalizaPartida();
			break;
		}
		
		return jogada;
	}


	public JogadaMorelli abandonarPartida(TipoJogada tipo) {

		JogadaMorelli jogada;
		
		if (tabuleiro.isPartidaEmAndamento()) {
			
			jogada = new JogadaMorelli(tipo);

			tabuleiro.setPartidaEmAndamento(false);

			tabuleiro.comunicar(false, msgs.getString("You")
					+ " " + msgs.getString("YourTimeToPlay")
					+ " " + msgs.getString("YouLost"));
			
			
		} else {
			
			jogada = null;
			
			tabuleiro.comunicar(true, msgs.getString("ThereIsNoMatchInProgress"));
		}

		return jogada;
	}

}
