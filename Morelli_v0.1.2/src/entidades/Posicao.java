package entidades;

import br.ufsc.inf.leobr.cliente.Jogada;

public class Posicao implements Jogada {

    protected Jogador jogador;          //Jogador que ocupa a posicao
    protected int faixa;                //Ordem da faixa a que pertence
    protected int linha;                //Linha a que pertence
    protected int coluna;               //Coluna a que pertence
    protected boolean ocupada;          //Ocupada = true
    protected boolean preta;            //Cor da peca que ocupa a posicao
    protected Posicao[][] adjacentes;   //Posicoes adjacentes

    public Posicao(int faixa, int linha, int coluna) {
        this.faixa = faixa;
        this.linha = linha;
        this.coluna = coluna;
        this.ocupada = false;
    }

    /**
     *
     * @param jogador
     */
    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public int getFaixa() {
        return faixa;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void retirarPeca() {
        ocupada = false;
    }

    /**
     *
     * @param corPeca
     */
    public void posicionarPeca(boolean corPeca) {
        preta = corPeca;
        ocupada = true;
    }

    public boolean isOcupada() {
        return this.ocupada;
    }

    public void setCor(boolean corPeca) {
        this.preta = corPeca;
    }

    public boolean getCor() {
        return preta;
    }

    public void setAdjacentes() {
        // TODO - implement Posicao.setAdjacentes
        throw new UnsupportedOperationException();
    }

    public Posicao[][] getAdjacentes() {
        return adjacentes;
    }

    /**
     *
     * @param adjacente
     */
    public Posicao verificarAdjacentes(Posicao adjacente) {
        // TODO - implement Posicao.verificarAdjacentes
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param posicao
     */
    public boolean possuiAdjacentesCorOposta(Posicao posicao) {
        // TODO - implement Posicao.possuiAdjacentesCorOposta
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param origem
     * @param destino
     */
    public boolean verficarCaminho(Posicao origem, Posicao destino) {
        // TODO - implement Posicao.verficarCaminho
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object posicao) {
        if (!(posicao instanceof Posicao)) {
            return false;
        }

        return posicao == this;
    }

}
