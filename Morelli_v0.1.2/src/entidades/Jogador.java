package entidades;

public class Jogador {

    protected String nome;               //Nome do jogador humano
    protected boolean pecasPretas;       //Pretas = true; Brancas = False
    protected boolean daVez;             //Proximo jogador a efetuar jogada
    protected boolean vencedor;          //Jogador venceu a ultima partida
    protected boolean abandonarPartida;  //Jogador abandonou a partida

    public Jogador(String nome) {
        this.nome = nome;
        this.pecasPretas = false;
        this.daVez = false;
        this.vencedor = false;
        this.abandonarPartida = false;
    }

    /**
     *
     * @param nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setPecasPretas() {
        this.pecasPretas = true;
    }

    public boolean getCor() {
        return pecasPretas;
    }

    /**
     *
     * @param daVez
     */
    public void setDaVez(boolean daVez) {
        this.daVez = daVez;
    }

    public boolean isDaVez() {
        return daVez;
    }

    /**
     *
     * @param vencedor
     */
    public void setVencedor(boolean vencedor) {
        this.vencedor = vencedor;
    }

    public boolean isVencedor() {
        return vencedor;
    }

    /**
     *
     * @param abandonarPartida
     */
    public void setAbandonarPartida(boolean abandonarPartida) {
        this.abandonarPartida = abandonarPartida;
    }

    public boolean isAbandonarPartida() {
        return abandonarPartida;
    }
}
