package entidades;

public class Ajuda {

    protected String[] ajuda;

    public Ajuda() {
        this.ajuda = new String[4];
        this.ajuda[0] = "Realizar acordo: Um usuário, em qualquer momento do jogo, pode solicitar um acordo entre os dois jogadores para que a partida seja encerrada sem que um deles seja considerado vencedor (empate).";
        this.ajuda[1] = "Abandonar partida: Em uma partida em andamento, um usuário poderá abandonar o jogo a qualquer momento, declarando o seu oponente como o vencedor da partida.";
        this.ajuda[2] = "Como movimentar uma peça: Com um jogo em andamento, um usuário deverá selecionar com o mouse a peça que deseja movimentar e, em seguida, selecionar uma posição válida para finalizar o movimento.";
        this.ajuda[3] = "Para que um movimento seja possível, deve haver pelo menos uma posição, em pelo menos uma faixa interior (mais próxima do centro), que esteja em uma linha ortogonal ou diagonal a partir da posição original da peça a ser movimentada;";
    }

    public String getAjuda() {
        String instrucoes = "";

        for (String ajuda1 : this.ajuda) {
            instrucoes += ajuda1 + "\n\n";
        }

        return instrucoes;
    }
}
