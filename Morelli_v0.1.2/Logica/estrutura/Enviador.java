package estrutura;

import java.util.ResourceBundle;

import classes.JogadaMorelli;
import classes.Posicao;

public class Enviador {

	protected ResourceBundle msgs;
	Controlador ctrl;

	public Enviador(ResourceBundle msgs, Controlador ctrl) {

		this.msgs = msgs;
		this.ctrl = ctrl;
	}

	/*--- Caso de uso: enviar jogada ---*/
	public JogadaMorelli tratarJogada(JogadaMorelli jogada) {

		JogadaMorelli jogadaTratada;

		switch (jogada.getTipoDeJogada()) {
		
		case proporAcordo:
			jogadaTratada = proporAcordo(jogada);
			break;
			
		case acordoAceito:
			jogadaTratada = tratarAcordoAceito(jogada);
			break;
			
		case acordoNegado:
			jogadaTratada = tratarAcordoNegado(jogada);
			break;
			
		case abandonarPartida:
			jogadaTratada = abandonarPartida(jogada);
			break;
			
		case atualizarTabuleiro:
			jogadaTratada = atualizarTabuleiro(jogada);
			break;
			
		default:
			jogadaTratada = finalizarPartida(jogada);
			break;
		}
		
		return jogadaTratada;
	}

	private JogadaMorelli proporAcordo(JogadaMorelli jogada) {
		
		if (ctrl.isPartidaEmAndamento()) {
			
			return jogada;
		
		} else {
			
			return null;
		}

	}


	private JogadaMorelli tratarAcordoAceito(JogadaMorelli jogada) {

		ctrl.comunicar(false, msgs.getString("YouAcceptedTheDeal")
				+ "\n" + msgs.getString("TheMatchEndedTied"));

		ctrl.setPartidaEmAndamento(false);
		
		return jogada;
	}

	private JogadaMorelli tratarAcordoNegado(JogadaMorelli jogada) {

		ctrl.comunicar(false, msgs.getString("YouDeniedTheDeal")
				+ "\n" + msgs.getString("TheMatchWillContinue"));
		
		return jogada;
	}

	public JogadaMorelli abandonarPartida(JogadaMorelli jogada) {

		if (ctrl.isPartidaEmAndamento()) {

			ctrl.setPartidaEmAndamento(false);

			ctrl.comunicar(false, msgs.getString("You")
					+ " " + msgs.getString("YourTimeToPlay")
					+ " " + msgs.getString("YouLost"));
			
			ctrl.comunicar(true, "Game Over!");
			
			return jogada;
			
		} else {
			
			ctrl.comunicar(true, msgs.getString("ThereIsNoMatchInProgress"));
			
			return null;
		}
	}

	private JogadaMorelli atualizarTabuleiro(JogadaMorelli jogada) {
		
		JogadaMorelli jogadaAtualizada = ctrl.movimentarPeca(jogada); 
		
		return jogadaAtualizada;
	}

	private JogadaMorelli finalizarPartida(JogadaMorelli jogada) {

		Posicao trono = ctrl.getTrono().getPosicoes()[0];
		
		if (trono.getCor() == ctrl.getJogador().getCor()) {
			
			ctrl.comunicar(true, msgs.getString("YouAreTheWinner"));
		
		} else {
			
			ctrl.comunicar(true, msgs.getString("YouLost"));
		}
		
		ctrl.comunicar(true, msgs.getString("TheMatchIsOver"));
		
		ctrl.setDaVez(false);
		ctrl.setPartidaEmAndamento(false);
		
		return jogada;
	}
}
