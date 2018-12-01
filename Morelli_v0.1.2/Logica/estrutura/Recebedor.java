package estrutura;

import java.util.ResourceBundle;

import classes.JogadaMorelli;
import classes.TipoJogada;

public class Recebedor {

	protected ResourceBundle msgs;
	Tabuleiro tabuleiro;

	public Recebedor(ResourceBundle msgs, Tabuleiro tabuleiro) {

		this.msgs = msgs;
		this.tabuleiro = tabuleiro;
	}

	public void tratarJogada(JogadaMorelli jogada) {

//        tela.informar(msgs.getString("YourTimeToPlay"));
        TipoJogada tipoJogada = jogada.getTipoDeJogada();

        if (null != tipoJogada) {
            switch (tipoJogada) {
                case realizarAcordo:
                    tabuleiro.realizarAcordo();
                    break;
                case acordoAceito:
//                    outbox.informarEmpate();
                    break;
                case acordoNegado:
//                    tela.exibeMensagemAcordoNegado();
                    break;
                    
                case abandonarPartida:
                	abandonarPartida();
                    break;
                    
                case atualizarTabuleiro:
                	tabuleiro.atualizarTabuleiro(jogada.getTabuleiro());
//                	outbox.atualizarTabuleiro(jogada.getTabuleiro());
                    break;
                default:
//                	outbox.informarVencedor();
                    break;
            }
        }
	}

	public void abandonarPartida() {
		
		tabuleiro.setPartidaEmAndamento(false);

		String msg = msgs.getString("YourTimeToPlay") 
				+ " " + msgs.getString("YouAreTheWinner");
		
		tabuleiro.comunicar(true, msg);
	}

}
