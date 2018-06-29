package interfaceGrafica;

import entidades.Faixa;
import entidades.JogadaMorelli;
import entidades.Jogador;
import entidades.NetGames;
import entidades.Posicao;
import entidades.Tabuleiro;
import entidades.TipoJogada;
import javax.swing.JOptionPane;

public class AtorJogador {

    protected NetGames netGames;
    protected Tabuleiro tabuleiro;
    protected TelaJogador tela;

    protected boolean conectado;

    protected Jogador jogador;
    protected boolean daVez;

    protected Faixa[] tabuleiroAtualizado;
    protected Posicao posicaoOrigem;
    protected Posicao posicaoDestino;

    public AtorJogador() {

        this.netGames = new NetGames(this);
        this.tabuleiro = new Tabuleiro(this);

        this.tela = new TelaJogador(this);
        this.tela.setVisible(true);

        this.conectado = false;

        this.jogador = null;
        this.daVez = false;

        this.posicaoOrigem = null;
        this.posicaoDestino = null;

    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }

    public Jogador getJogador() {
        return jogador;
    }

    /**
     *
     * @param vez
     */
    public void setDaVez(boolean vez) {
        this.daVez = vez;
    }

    public boolean isDaVez() {
        return daVez;
    }

    public void conectar() {

        //Testa se ja esta conectado antes de tentar se conectar
        if (!conectado) {

            //Dados para a conexão 
            String ip = this.solicitaIpServidor();
            String nomeJogador = this.solicitaNomeJogador();

            //Faz a conexão
            conectado = netGames.conectar(ip, nomeJogador);

        } else {
            notificarConectado();
        }
        informarEstadoConexao();
    }

    public String solicitaIpServidor() {

        String ip = "";

        while (ip.trim().isEmpty()) {
            ip = JOptionPane.showInputDialog("IP do servidor:", "");
        }

        return ip;
    }

    public String solicitaNomeJogador() {

        String nome = "";

        while (nome.trim().isEmpty()) {
            nome = JOptionPane.showInputDialog("Insira seu nome:", "");
        }

        return nome;
    }

    public void desconectar() {

        if (conectado) {
            tabuleiro.setPartidaEmAndamento(false);
            conectado = netGames.desconectar();
        } else {
            notificarDesconectado();
        }

        this.informarEstadoConexao();
    }

    public void iniciarPartida() {

        //Verifica se ha partida em andamento
        if (tabuleiro.isPartidaEmAndamento()) {

            notificarPartidaEmAndamento();

            if (this.confirmarReiniciarPartida()) {
                netGames.reiniciarPartida();
            }

        } else {
            tabuleiro.setPartidaEmAndamento(true);
            netGames.iniciarPartida();
        }
    }

    /**
     *
     * @param ordem
     */
    public void receberSolicitacaoInicio(int ordem) {

        if (ordem == 1) {
            tela.setPainel("Nome: " + netGames.getNomeAdversario(1) + "\n");
            tela.setPainel("Adversario: " + netGames.getNomeAdversario(2) + "\n\n");
            tela.setPainel("Você joga com as peças brancas.\n");
            tela.setPainel("Seu adversário começa jogando. Aguarde a sua vez!");
            setDaVez(true);
        } else {
            tela.setPainel("Nome: " + netGames.getNomeAdversario(2) + "\n");
            tela.setPainel("Adversario: " + netGames.getNomeAdversario(1) + "\n\n");
            tela.setPainel("Você joga com as peças pretas.\n");
            tela.setPainel("Você começa jogando.");
            setDaVez(false);
        }

        String nomeJogador1 = netGames.getNomeAdversario(1);
        String nomeJogador2 = netGames.getNomeAdversario(2);

        tabuleiroAtualizado = tabuleiro.iniciarPartida(jogador, ordem, nomeJogador1, nomeJogador2);

        if (ordem == 1) {
            enviarJogada(TipoJogada.atualizarTabuleiro);
            tela.atualizaTabuleiro(tabuleiroAtualizado);
        }

    }

    public void finalizarPartidaEmpate() {
        tabuleiro.setPartidaEmAndamento(false);
        this.exibeMensagemEmpate();
    }

    public boolean confirmarReiniciarPartida() {
        int resposta = JOptionPane.showConfirmDialog(null,
                "Deseja reiniciar a partida?", "Reiniciar Partida",
                JOptionPane.YES_NO_OPTION);
        return resposta == JOptionPane.YES_OPTION;
    }

    public void realizarAcordo() {
        int resposta = JOptionPane.showConfirmDialog(null,
                "Deseja realizar o acordo e terminar a partida com um empate?",
                "O seu oponente pediu um acordo", JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            this.finalizarPartidaEmpate();
            enviarJogada(TipoJogada.acordoAceito);
        } else {
            enviarJogada(TipoJogada.acordoNegado);
        }
    }

    public String ajuda() {
        return tabuleiro.getAjuda();
    }

    /**
     * @param peca
     * @param origem
     * @param destino
     */
    public void movimentarPeca(Posicao origem, Posicao destino) {
        try {
            if (daVez && tabuleiro.isPartidaEmAndamento()) {
                if (origem.getCor() == jogador.getCor()) {
                    posicaoOrigem = origem;
                    posicaoDestino = destino;

                    if (tabuleiro.calcularMovimento(origem, destino)) {
                        tabuleiro.calcularCaptura(destino);
                        tabuleiro.calcularTomadaTrono(destino);
                        tabuleiroAtualizado = tabuleiro.getTabuleiro();
                        enviarJogada(TipoJogada.atualizarTabuleiro);
                        tela.atualizaTabuleiro(tabuleiroAtualizado);
                    }
                }
            }
        } catch (Exception e) {
            notificar(e.toString() + " movimentarPeca");
        }
    }

    /**
     *
     * @param tipo
     */
    public void enviarJogada(TipoJogada tipoJogada) {

        if (daVez && tabuleiro.isPartidaEmAndamento()) {
            setDaVez(false);
            tela.setPainel("Aguarde seu adversário jogar");
            JogadaMorelli jogada;

            if (null != tipoJogada) {
                switch (tipoJogada) {
                    case realizarAcordo:
                        jogada = new JogadaMorelli(TipoJogada.realizarAcordo);
                        netGames.enviarJogada(jogada);
                        break;
                    case acordoAceito:
                        jogada = new JogadaMorelli(TipoJogada.acordoAceito);
                        netGames.enviarJogada(jogada);
                        netGames.finalizarPartida();
                        break;
                    case acordoNegado:
                        jogada = new JogadaMorelli(TipoJogada.acordoNegado);
                        netGames.enviarJogada(jogada);
                        break;
                    case abandonarPartida:
                        jogada = new JogadaMorelli(TipoJogada.abandonarPartida);
                        netGames.enviarJogada(jogada);
                        break;
                    case atualizarTabuleiro:
                        jogada = new JogadaMorelli(tipoJogada.atualizarTabuleiro, tabuleiroAtualizado);
                        netGames.enviarJogada(jogada);
                        break;
                    default:
                        jogada = new JogadaMorelli(tipoJogada.encerramento);
                        String msg = "A partida foi encerrada!";
                        notificar(msg);
                        netGames.finalizarPartida();
                        break;
                }

            }
        }
    }

    /**
     *
     * @param jogada
     */
    public void receberJogada(JogadaMorelli jogada) {

        setDaVez(true);
        tela.setPainel("Sua vez de jogar!");
        TipoJogada tipoJogada = jogada.getTipoDeJogada();

        if (null != tipoJogada) {
            switch (tipoJogada) {
                case realizarAcordo:
                    this.realizarAcordo();
                    break;
                case acordoAceito:
                    finalizarPartidaEmpate();
                    break;
                case acordoNegado:
                    exibeMensagemAcordoNegado();
                    break;
                case abandonarPartida:
                    tabuleiro.setPartidaEmAndamento(false);
                    netGames.finalizarPartida();
                    String msg = netGames.getNomeJogador()
                            + " abandonou a partida. Você é o vencedor!";
                    notificar(msg);
                    break;
                case atualizarTabuleiro:
                    atualizaTabuleiro(jogada.getTabuleiro());
                    break;
                case encerramento:
                    informaPartidaEncerrada();
                    break;
                default:
                    break;
            }
        }
    }

    public void abandonarPartida() {
        if (tabuleiro.isPartidaEmAndamento()) {
            tabuleiro.setPartidaEmAndamento(false);
            enviarJogada(TipoJogada.abandonarPartida);
            JOptionPane.showMessageDialog(tela, "Você abandonou a partida e perdeu!");
        }
    }

    /**
     *
     * @param tabuleiro
     */
    public void atualizaTabuleiro(Faixa[] tabuleiroAtualizado) {
        this.tabuleiroAtualizado = tabuleiroAtualizado;
        tabuleiro.atualizarTabuleiro(tabuleiroAtualizado);
        tela.atualizaTabuleiro(tabuleiroAtualizado);
    }

    public void notificarIrregularidade() {
        JOptionPane.showMessageDialog(tela, "Jogada Irregular!");

    }

    public void notificar(String msg) {
        JOptionPane.showMessageDialog(tela, msg);
    }

    public void informarEstadoConexao() {
        if (conectado) {
            tela.setPainel("Conectado");
        } else {
            tela.setPainel("Desconectado");
        }

    }

    public void notificarFalhaDesconexao() {
        JOptionPane.showMessageDialog(tela, "Falha ao tentar desconectar");
    }

    public void notificarPartidaEmAndamento() {
        tela.setPainel("Há uma partida em andamento!");
    }

    public void notificarResultado(String msg) {
        tela.setPainel(msg);
    }

    public void exibeMensagemEmpate() {
        tela.setPainel("O acordo foi aceito e a partida terminou empatada!");
    }

    public void informaPartidaEncerrada() {
        tabuleiro.finalizaPartida();
    }

    public void exibeMensagemAcordoNegado() {
        tela.setPainel("O acordo foi negado. A partida continuará.");
    }

    public void notificaNaoJogando() {
        JOptionPane.showMessageDialog(tela, "Não é a sua vez de jogar!");
    }

    public void notificaJaConectado() {
        tela.setPainel("Você já está conectado!");
    }

    public void notificarConectado() {
        JOptionPane.showMessageDialog(tela, "Você já está conectado!");
    }

    public void notificarDesconectado() {
        JOptionPane.showMessageDialog(tela, "Você já está desconectado!");
    }
}
