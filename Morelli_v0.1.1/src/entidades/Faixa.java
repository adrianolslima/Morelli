package entidades;

import br.ufsc.inf.leobr.cliente.Jogada;

public class Faixa implements Jogada {

    protected int ordem;            //Ordem no tabuleiro, de dentro para fora
    protected Cor cor;              //Cor da faixa
    protected Posicao[] posicoes;   //Colecao de posicoes que pertence a faixa

    public Faixa(int ordem) {

        //Ordem da faixa no tabuleiro, de dentro para fora
        this.ordem = ordem;

        //Definicao da cor da faixa
        switch (ordem) {
            case 0:
                this.cor = Cor.roxo;
                break;
            case 1:
                this.cor = Cor.azulEscuro;
                break;
            case 2:
                this.cor = Cor.azulClaro;
                break;
            case 3:
                this.cor = Cor.verde;
                break;
            case 4:
                this.cor = Cor.amarelo;
                break;
            case 5:
                this.cor = Cor.laranja;
                break;
            case 6:
                this.cor = Cor.vermelho;
        }

        int numPosicoes = ordem * 8;

        //Definicao do tamanho da faixa
        if (ordem == 0) {       //Faixa do centro

            this.posicoes = new Posicao[1];
            Posicao posicao = new Posicao(0, 6, 6);
            this.addPosicao(posicao, ordem);

        } else {                //Outras faixas

            this.posicoes = new Posicao[numPosicoes];

            //Cria as posicoes e adiciona a faixa
            int i = 0;

            int linha = 6 - ordem;
            int coluna;

            //Canto superior
            for (coluna = linha; coluna <= 6 + ordem; coluna++) {
                Posicao posicao = new Posicao(ordem, linha, coluna);
                this.addPosicao(posicao, i);
                i++;
            }

            //Canto direito
            coluna--;
            for (linha++; linha <= 6 + ordem; linha++) {
                Posicao posicao = new Posicao(ordem, linha, coluna);
                this.addPosicao(posicao, i);
                i++;
            }

            //Canto inferior
            linha--;
            for (coluna--; coluna >= 6 - ordem; coluna--) {
                Posicao posicao = new Posicao(ordem, linha, coluna);
                this.addPosicao(posicao, i);
                i++;
            }

            //Canto esquerdo
            coluna++;
            for (linha--; linha >= 7 - ordem; linha--) {
                Posicao posicao = new Posicao(ordem, linha, coluna);
                this.addPosicao(posicao, i);
                i++;
            }
        }
    }

    public int getOrdem() {
        return ordem;
    }

    public Cor getCor() {
        return cor;
    }

    public Posicao[] getPosicoes() {
        return posicoes;
    }

    /**
     *
     * @param posicao
     * @param indice
     */
    public void addPosicao(Posicao posicao, int indice) {
        this.posicoes[indice] = posicao;
    }

    /**
     *
     * @param posicao
     * @return
     */
    public boolean contemPosicao(Posicao posicao) {
        for (Posicao item : posicoes) {
            if (posicao.equals(item)) {
                return true;
            }
        }
        return false;
    }

    public int getTamanho() {
        return posicoes.length;
    }

}
