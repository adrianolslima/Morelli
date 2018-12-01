package estrutura;

import java.util.ResourceBundle;

import classes.JogadaMorelli;
import classes.TipoJogada;
import partesInterface.PortInterfaceOutbox;
import partesInterface.PortProxyOutbox;

public class TratadorJogadas {

	protected ResourceBundle msgs;
	Tabuleiro tabuleiro;

	public TratadorJogadas(ResourceBundle msgs, Tabuleiro tabuleiro) {

		this.msgs = msgs;
		this.tabuleiro = tabuleiro;
	}

	/*--- Caso de uso: enviar jogada ---*/
	public JogadaMorelli tratarJogadaEnviada(TipoJogada tipoJogada) {

		JogadaMorelli jogada;

		switch (tipoJogada) {
		
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
			abandonarPartida();
			jogada = new JogadaMorelli(TipoJogada.abandonarPartida);
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


	public void abandonarPartida() {

		if (tabuleiro.isPartidaEmAndamento()) {

			tabuleiro.setPartidaEmAndamento(false);

			tabuleiro.comunicar(false, msgs.getString("You")
					+ " " + msgs.getString("YourTimeToPlay")
					+ " " + msgs.getString("YouLost"));
			
		} else {
			
			tabuleiro.comunicar(true, msgs.getString("ThereIsNoMatchInProgress"));
		}
	}

}
