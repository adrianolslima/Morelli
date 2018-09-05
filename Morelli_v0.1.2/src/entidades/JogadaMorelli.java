package entidades;

import br.ufsc.inf.leobr.cliente.Jogada;

public class JogadaMorelli implements Jogada {

    protected Faixa[] tabuleiro;
    protected TipoJogada tipoJogada;
    
    protected Posicao posicaoOrigem;
    protected Posicao posicaoDestino;

    public JogadaMorelli(TipoJogada tipoJogada, Faixa[] tabuleiro) {
        this.tabuleiro = tabuleiro;
        this.tipoJogada = tipoJogada;
    }

    public JogadaMorelli(TipoJogada tipoJogada) {
        this.tipoJogada = tipoJogada;
    }

    public Faixa[] getTabuleiro() {
        return this.tabuleiro;
    }

    public TipoJogada getTipoDeJogada() {
        return this.tipoJogada;
    }
    
//    public boolean[] getMapaPretas() {
//        return this.mapaPretas;
//    }
}
