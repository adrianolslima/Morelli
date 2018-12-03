package estrutura;

import java.util.ResourceBundle;

import classes.JogadaMorelli;
import classes.Posicao;

public class Recebedor {

	protected ResourceBundle msgs;
	Controlador ctrl;

	public Recebedor(ResourceBundle msgs, Controlador ctrl) {

		this.msgs = msgs;
		this.ctrl = ctrl;
	}

	public void tratarJogada(JogadaMorelli jogada) {

		switch (jogada.getTipoDeJogada()) {
		case proporAcordo:
			realizarAcordo();
			break;
			
		case acordoAceito:
			tratarAcordoAceito();
			break;
			
		case acordoNegado:
			tratarAcordoNegado();
			break;

		case abandonarPartida:
			abandonarPartida();
			break;

		case atualizarTabuleiro:
			atualizarTabuleiro(jogada);
			break;
			
		default:
			finalizarPartida(jogada);
			break;
		}
	}

	public void realizarAcordo() {
    	
    	ctrl.realizarAcordo();
    }

    private void tratarAcordoAceito() {

		ctrl.comunicar(false, msgs.getString("TheDealWasAccepted")
				+ "\n" + msgs.getString("TheMatchEndedTied"));
		
		ctrl.setPartidaEmAndamento(true);;
	}

	private void tratarAcordoNegado() {

		ctrl.comunicar(false, msgs.getString("TheDealWasDenied")
				+ "\n" + msgs.getString("TheMatchWillContinue"));
	}

	public void abandonarPartida() {
		
		ctrl.setPartidaEmAndamento(false);
		
		ctrl.comunicar(false,msgs.getString("YourTimeToPlay") 
				+ " " + msgs.getString("YouAreTheWinner"));
		
		ctrl.comunicar(true, "Game Over!");
	}

	private void atualizarTabuleiro(JogadaMorelli jogada) {
		
		ctrl.atualizarTabuleiro(jogada.getTabuleiro());		
	}

	private void finalizarPartida(JogadaMorelli jogada) {
		
		ctrl.atualizarTabuleiro(jogada.getTabuleiro());

		Posicao trono = ctrl.getTrono().getPosicoes()[0];
		
		if (trono.getCor() == ctrl.getJogador1().getCor()) {
			
			ctrl.comunicar(true, msgs.getString("YouAreTheWinner"));
		
		} else {
			
			ctrl.comunicar(true, msgs.getString("YouLost"));
		}
		
		ctrl.comunicar(true, msgs.getString("TheMatchIsOver"));
		
		ctrl.setDaVez(false);
		ctrl.setPartidaEmAndamento(false);
	}
}
