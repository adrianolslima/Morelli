package estrutura;

import java.util.ResourceBundle;

import classes.JogadaMorelli;

public class Recebedor {

	protected ResourceBundle msgs;
	Controlador ctrl;

	public Recebedor(ResourceBundle msgs, Controlador tabuleiro) {

		this.msgs = msgs;
		this.ctrl = tabuleiro;
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
			//                	outbox.informarVencedor();
			break;
		}
	}

	public void realizarAcordo() {
    	
    	ctrl.realizarAcordo();
    }

    private void tratarAcordoAceito() {

		ctrl.comunicar(false, msgs.getString("TheDealWasAccepted")
				+ "\n" + msgs.getString("TheMatchEndedTied"));
		
		ctrl.finalizarPartida();
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
}
