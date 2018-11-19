package entidades;

import interfaceGrafica.AtorJogador;
import java.util.Random;
import java.util.ResourceBundle;

public class Tabuleiro {
    
    protected ResourceBundle msgs;

    protected AtorJogador atorJogador;
    protected NetGames netGames;
    
    protected Ajuda ajuda;

    protected boolean partidaEmAndamento;

    protected Faixa[] tabuleiro;
    protected Faixa trono;

    protected Jogador jogador1;
    protected Jogador jogador2;

    protected AtorJogador proximoJogador;
    protected Posicao posicaoOrigem;
    protected Posicao posicaoDestino;

    public Tabuleiro(AtorJogador atorJogador, ResourceBundle msgs) {
    	
    	this.msgs = msgs;

        this.atorJogador = atorJogador;
        this.netGames = new NetGames(this);

        this.ajuda = new Ajuda();

        this.partidaEmAndamento = false;

        this.tabuleiro = new Faixa[7]; // O tabuleiro sempre tem 7 faixas

        this.proximoJogador = null;
        this.posicaoOrigem = null;
        this.posicaoDestino = null;
    }

    public void setPartidaEmAndamento(boolean partidaEmAndamento) {
    
    	this.partidaEmAndamento = partidaEmAndamento;
    }

    public boolean isPartidaEmAndamento() {
   
    	return this.partidaEmAndamento;
    }

    public void atualizarTabuleiro(Faixa[] tabuleiro) {
        
    	this.tabuleiro = tabuleiro;
    }

    public Faixa[] getTabuleiro() {
        
    	return tabuleiro;
    }

    public Faixa[] iniciarPartida(int ordem, String nomeJogador1, String nomeJogador2) {

        jogador1 = new Jogador(nomeJogador1);
        jogador2 = new Jogador(nomeJogador2);

        //jogador1 joga com as pecas pretas e inica a partida
//        jogador1.setDaVez(true);
        jogador2.setPecasPretas();

        if (ordem == 1) {
            atorJogador.setJogador(jogador1);
        } else {
            atorJogador.setJogador(jogador2);
        }

        //Prepara o tabuleiro para iniciar a partida
        this.limparTabuleiro();
        if (ordem == 1) {
            this.distribuiPecas();
        }

        //Inicia a partida
        setPartidaEmAndamento(true);
        return tabuleiro;
    }

    public void limparTabuleiro() {

        //Cria as faixas do tabuleiro
        for (int i = 0; i < 7; i++) {
            Faixa faixa = new Faixa(i);
            this.tabuleiro[i] = faixa;
        }

        //Define trono
        this.trono = tabuleiro[0];

    }

    public void distribuiPecas() {

        for (int i = 0; i <= 23; i++) {

            Random random = new Random();
            int x = random.nextInt(2);

            if (x == 0) {
                tabuleiro[6].posicoes[i].preta = true;
                tabuleiro[6].posicoes[i].ocupada = true;
                tabuleiro[6].posicoes[i + 24].preta = false;
                tabuleiro[6].posicoes[i + 24].ocupada = true;
            } else {
                tabuleiro[6].posicoes[i].preta = false;
                tabuleiro[6].posicoes[i].ocupada = true;
                tabuleiro[6].posicoes[i + 24].preta = true;
                tabuleiro[6].posicoes[i + 24].ocupada = true;
            }
        }
    }

    public void abandonarPartida() {
    
    	this.partidaEmAndamento = false;
    }

    public void realizarAcordo() {
        
    	atorJogador.realizarAcordo();
    }

    public void atualizaJogadorDaVez() {
        
    	AtorJogador aux = this.atorJogador;
        this.atorJogador = this.proximoJogador;
        this.proximoJogador = aux;
    }

    public Posicao getPosicaoOrigem() {
        
    	return posicaoOrigem;
    }

    public void setPosicaoOrigem(Posicao posicaoOrigem) {
    
    	this.posicaoOrigem = posicaoOrigem;
    }

    public Posicao getPosicaoDestino() {
        
    	return posicaoDestino;
    }

    public void setPosicaoDestino(Posicao posicaoDestino) {
    
    	this.posicaoDestino = posicaoDestino;
    }

    public boolean movimentoAoCentro(Posicao origem, Posicao destino) {
        
    	return origem.getFaixa() > destino.getFaixa();
    }

    public boolean calcularMovimentoLinha(Posicao origem, Posicao destino) {

        int linhaOrigem = origem.getLinha();
        int linhaDestino = destino.getLinha();

        if (linhaOrigem == linhaDestino) {

            int faixaOrigem = origem.getFaixa();
            int faixaAtual = faixaOrigem - 1;

            for (int i = faixaAtual; i > 0; i--) {
                Posicao[] posicoesFaixa = tabuleiro[faixaAtual].getPosicoes();
                for (int j = 0; j < posicoesFaixa.length; j++) {
                    if (posicoesFaixa[j].getLinha() == linhaOrigem) {
                        if (!posicoesFaixa[j].isOcupada()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean calcularMovimentoColuna(Posicao origem, Posicao destino) {

        int colunaOrigem = origem.getColuna();
        int colunaDestino = destino.getColuna();

        if (colunaOrigem == colunaDestino) {

            int faixaOrigem = origem.getFaixa();
            int faixaAtual = faixaOrigem - 1;

            for (int i = faixaAtual; i > 0; i--) {
                Posicao[] posicoesFaixa = tabuleiro[faixaAtual].getPosicoes();
                for (int j = 0; j < posicoesFaixa.length; j++) {
                    if (posicoesFaixa[j].getColuna() == colunaOrigem) {
                        if (!posicoesFaixa[j].isOcupada()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean calcularMovimentoDiagonal(Posicao origem, Posicao destino) {
        
    	int linhaOrigem = origem.getLinha();
        int colunaOrigem = origem.getColuna();

        int linhaDestino = destino.getLinha();
        int colunaDestino = destino.getColuna();

        int diagonalOrigem;
        int diagonalDestino;

        //Diagonal superior-esquerdo <-> inferior-direito
        diagonalOrigem = linhaOrigem - colunaOrigem;
        diagonalDestino = linhaDestino - colunaDestino;
        if (diagonalOrigem == diagonalDestino) {
            return true;
        }

        //Diagonal superior-direito <-> inferior-esquerdo
        diagonalOrigem = linhaOrigem + colunaOrigem;
        diagonalDestino = linhaDestino + colunaDestino;
        if (diagonalOrigem == diagonalDestino) {
            return true;
        }

        return false;
    }

    public void calcularCaptura(Posicao destino) {
    
    	try {
            boolean cor = destino.getCor();

            Posicao[] posicoes = verificarAdjacentes(destino);

            for (int i = 0; i < posicoes.length; i++) {
                Posicao posicao = posicoes[i];
                if (posicao != null) {
                    if (posicao.isOcupada()) {
                        if (posicao.getCor() != cor) {
                            Posicao[] adjacentes = verificarAdjacentes(posicao);
                            for (int j = 0; j < adjacentes.length; j++) {
                                Posicao adjacente = adjacentes[j];
                                if (adjacente != null) {
                                    if (destino.equals(adjacente)) {
                                        Posicao oposta = adjacentes[(j + 4) % 8];
                                        if (oposta != null) {
                                            if (oposta.getCor() == cor) {
                                                posicao.setCor(cor);
                                                atualizarPosicaoTabuleiro(posicao);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            atorJogador.notificar(e.toString() + " calcularCaptura");
        }
    }

    public Posicao[] verificarAdjacentes(Posicao destino) {
    
    	int linha = destino.getLinha();
        int coluna = destino.getColuna();
        boolean cor = destino.getCor();

        int faixaAtual = destino.getFaixa();
        int faixaSuperior = faixaAtual - 1;
        int faixaInferior = faixaAtual + 1;

        Posicao[] adjacentes = new Posicao[8];

        //Busca adjacentes na faixa superior
        try {
            Posicao[] posicoes = tabuleiro[faixaSuperior].getPosicoes();
            for (Posicao posicao : posicoes) {
                if (posicao != null) {
                    if (posicao.isOcupada() && posicao.getCor() != cor) {
                        if (posicao.getLinha() == linha - 1 && posicao.getColuna() == coluna - 1) {
                            adjacentes[0] = posicao;
                        } else if (posicao.getLinha() == linha - 1 && posicao.getColuna() == coluna) {
                            adjacentes[1] = posicao;
                        } else if (posicao.getLinha() == linha - 1 && posicao.getColuna() == coluna + 1) {
                            adjacentes[2] = posicao;
                        } else if (posicao.getLinha() == linha && posicao.getColuna() == coluna + 1) {
                            adjacentes[3] = posicao;
                        } else if (posicao.getLinha() == linha + 1 && posicao.getColuna() == coluna + 1) {
                            adjacentes[4] = posicao;
                        } else if (posicao.getLinha() == linha + 1 && posicao.getColuna() == coluna) {
                            adjacentes[5] = posicao;
                        } else if (posicao.getLinha() == linha + 1 && posicao.getColuna() == coluna - 1) {
                            adjacentes[6] = posicao;
                        } else if (posicao.getLinha() == linha && posicao.getColuna() == coluna - 1) {
                            adjacentes[7] = posicao;
                        }
                    }
                }
            }

            //Busca adjacentes na mesma faixa
            posicoes = tabuleiro[faixaAtual].getPosicoes();
            for (Posicao posicao : posicoes) {
                if (posicao.isOcupada()) {
                    if (posicao.getLinha() == linha - 1 && posicao.getColuna() == coluna - 1) {
                        adjacentes[0] = posicao;
                    } else if (posicao.getLinha() == linha - 1 && posicao.getColuna() == coluna) {
                        adjacentes[1] = posicao;
                    } else if (posicao.getLinha() == linha - 1 && posicao.getColuna() == coluna + 1) {
                        adjacentes[2] = posicao;
                    } else if (posicao.getLinha() == linha && posicao.getColuna() == coluna + 1) {
                        adjacentes[3] = posicao;
                    } else if (posicao.getLinha() == linha + 1 && posicao.getColuna() == coluna + 1) {
                        adjacentes[4] = posicao;
                    } else if (posicao.getLinha() == linha + 1 && posicao.getColuna() == coluna) {
                        adjacentes[5] = posicao;
                    } else if (posicao.getLinha() == linha + 1 && posicao.getColuna() == coluna - 1) {
                        adjacentes[6] = posicao;
                    } else if (posicao.getLinha() == linha && posicao.getColuna() == coluna - 1) {
                        adjacentes[7] = posicao;
                    }
                }
            }
            //Busca adjacentes na faixa inferior
            posicoes = tabuleiro[faixaInferior].getPosicoes();
            for (Posicao posicao : posicoes) {
                if (posicao.isOcupada()) {
                    if (posicao.getLinha() == linha - 1 && posicao.getColuna() == coluna - 1) {
                        adjacentes[0] = posicao;
                    } else if (posicao.getLinha() == linha - 1 && posicao.getColuna() == coluna) {
                        adjacentes[1] = posicao;
                    } else if (posicao.getLinha() == linha - 1 && posicao.getColuna() == coluna + 1) {
                        adjacentes[2] = posicao;
                    } else if (posicao.getLinha() == linha && posicao.getColuna() == coluna + 1) {
                        adjacentes[3] = posicao;
                    } else if (posicao.getLinha() == linha + 1 && posicao.getColuna() == coluna + 1) {
                        adjacentes[4] = posicao;
                    } else if (posicao.getLinha() == linha + 1 && posicao.getColuna() == coluna) {
                        adjacentes[5] = posicao;
                    } else if (posicao.getLinha() == linha + 1 && posicao.getColuna() == coluna - 1) {
                        adjacentes[6] = posicao;
                    } else if (posicao.getLinha() == linha && posicao.getColuna() == coluna - 1) {
                        adjacentes[7] = posicao;
                    }
                }
            }
        } catch (Exception e) {
            atorJogador.notificar(e.toString() + " verificarAdjacentes");
        }

        return adjacentes;
    }

    public void calcularTomadaTrono(Posicao posicao) {

        int faixa = posicao.getFaixa();
        boolean cor = posicao.getCor();

        int indice = 0;

        Posicao[] posicoes = tabuleiro[faixa].getPosicoes();

        for (int i = 0; i < posicoes.length; i++) {
            if (posicoes[i].equals(posicao)) {
                indice = i;
                break;
            }
        }

        int lado = faixa * 2;
        int numPosicoes = lado * 4;

        try {
            Posicao vertice = posicoes[(indice + lado) % numPosicoes];
            if (vertice.isOcupada()) {
                if (vertice.getCor() == cor) {
                    lado += lado;
                    vertice = posicoes[(indice + lado) % numPosicoes];
                    if (vertice.isOcupada()) {
                        if (vertice.getCor() == cor) {
                            lado += lado;
                            vertice = posicoes[(indice + lado) % numPosicoes];
                            if (vertice.isOcupada()) {
                                if (vertice.getCor() == cor) {
                                    Posicao tronoTomado = trono.posicoes[0];
                                    tronoTomado.setCor(cor);
                                    tronoTomado.posicionarPeca(cor);
                                    atualizarPosicaoTabuleiro(tronoTomado);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            atorJogador.notificar(e.toString() + " calcularTomadaTrono");            
        }
    }

    public int calcularVerticeFaixa(Faixa faixa) {
        // TODO - implement Tabuleiro.calcularVerticeFaixa
        throw new UnsupportedOperationException();
    }

    public JogadaMorelli criaJogadaDeFinalizacaoPartida() {
    
    	JogadaMorelli jogada = new JogadaMorelli(TipoJogada.encerramento);
        return jogada;
    }

    public boolean definePartidaFinalizada() {
        // TODO - implement Tabuleiro.definePartidaFinalizada
        throw new UnsupportedOperationException();
    }

    public void finalizaPartida() {
        
    	setPartidaEmAndamento(false);
        AtorJogador novoVencedor = this.proximoJogador;
    }

    public boolean calcularMovimento(Posicao origem, Posicao destino) {

        //O trono não pode receber movimento; deve ser conquistado
        if (destino.getColuna() == 6) {
            if (destino.getLinha() == 6) {
                return false;
            }
        }

        //Verifica se a posicao de destino esta ocupada
        if (destino.isOcupada()) {
            atorJogador.notificarIrregularidade();
        } else {

            //Verifica se a peca se move em direcao ao centro do tabuleiro
            if (!movimentoAoCentro(origem, destino)) {
                atorJogador.notificarIrregularidade();

            } //Verifica se a peca se move na mesma linha
            else if (calcularMovimentoLinha(origem, destino)) {
                moverPeca(origem, destino);
                return true;

            } //Verifica se a peca se move na mesma coluna
            else if (calcularMovimentoColuna(origem, destino)) {
                moverPeca(origem, destino);
                return true;

            } //Verifica se a peca se move na mesma diagonal            
            else if (calcularMovimentoDiagonal(origem, destino)) {
                moverPeca(origem, destino);
                return true;
            }
        }

        return false;
    }

    public Faixa recuperarFaixaDaPosicao() {
        // TODO - implement Tabuleiro.recuperarFaixaDaPosicao
        throw new UnsupportedOperationException();
    }

    public void moverPeca(Posicao origem, Posicao destino) {
    
    	destino.posicionarPeca(origem.getCor());
        origem.retirarPeca();
        atualizarPosicaoTabuleiro(origem);
        atualizarPosicaoTabuleiro(destino);
    }

    public void atualizarPosicaoTabuleiro(Posicao posicao) {
   
    	int ordemFaixa = posicao.getFaixa();
        Posicao[] faixa = tabuleiro[ordemFaixa].getPosicoes();
        for (int i = 0; i < faixa.length; i++) {
            if (posicao.getLinha() == faixa[i].getLinha()) {
                if (posicao.getColuna() == faixa[i].getColuna()) {
                    faixa[i] = posicao;
                    break;
                }
            }
        }
    }
    
    /*--- Caso de uso: conectar ---*/
    public boolean conectar(String ip, String nomeJogador) {
    	
    	return netGames.conectar(ip, nomeJogador);
    }

    /*--- Caso de uso: desconectar ---*/
	public boolean desconectar() {

		boolean conectado = netGames.desconectar();
		
		if (!conectado) {
			
			partidaEmAndamento = false;
		}
		
		return conectado;
	}

	/*--- Caso de uso: ajuda ---*/
	public String getAjuda() {

		return ajuda.getAjuda();
	}

	/*--- Caso de uso: iniciar partida ---*/
	public boolean iniciarPartida() {

		return netGames.iniciarPartida();
	}

	/*--- Caso de uso: receber solicitacao de inicio ---*/
    public void receberSolicitacaoInicio(int ordem) {

//        if (ordem == 1) {
//        	
//            tela.informar(msgs.getString("Name") + netGames.getNomeAdversario(1) + "\n");
//            tela.informar(msgs.getString("Opponent") + netGames.getNomeAdversario(2) + "\n\n");
//            tela.informar(msgs.getString("YouPlayWith") + " " + msgs.getString("WhiteStones") + ".\n");
//            tela.informar(msgs.getString("YourOpponentStartsPlaying"));
//            
//        } else {
//        	
//            tela.informar(msgs.getString("Name") + ": " + netGames.getNomeAdversario(2) + "\n");
//            tela.informar(msgs.getString("Opponent") + ": " + netGames.getNomeAdversario(1) + "\n\n");
//            tela.informar(msgs.getString("YouPlayWith") + " " + msgs.getString("BlackStones") + ".\n");
//            tela.informar(msgs.getString("YouStartPlaying"));
//            setDaVez(false);
//        }
//
        String nomeJogador1 = netGames.getNomeAdversario(1);
        String nomeJogador2 = netGames.getNomeAdversario(2);

        tabuleiro = iniciarPartida(ordem, nomeJogador1, nomeJogador2);

        if (ordem == 1) {
            atorJogador.setDaVez(true);
        	atorJogador.atualizaTabuleiro(tabuleiro);
            enviarJogada(TipoJogada.atualizarTabuleiro);
        }
    }

    public void enviarJogada(TipoJogada tipoJogada) {

        if (atorJogador.isDaVez() && atorJogador.isPartidaEmAndamento()) {
            atorJogador.setDaVez(false);
//            tela.informar(msgs.getString("WaitUntilYourOpponentHasPlayed"));
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
                        jogada = new JogadaMorelli(tipoJogada.atualizarTabuleiro, tabuleiro);
                        netGames.enviarJogada(jogada);
                        break;
                    default:
                        jogada = new JogadaMorelli(tipoJogada.encerramento);
                        String msg = msgs.getString("TheMatchIsOver");
                        atorJogador.notificar(msg);
                        netGames.finalizarPartida();
                        break;
                }

            }
        }
    }

    public void receberJogada(JogadaMorelli jogada) {

        atorJogador.setDaVez(true);
//        tela.informar(msgs.getString("YourTimeToPlay"));
        TipoJogada tipoJogada = jogada.getTipoDeJogada();

        if (null != tipoJogada) {
            switch (tipoJogada) {
                case realizarAcordo:
                    this.realizarAcordo();
                    break;
                case acordoAceito:
                    atorJogador.finalizarPartidaEmpate();
                    break;
                case acordoNegado:
//                    tela.exibeMensagemAcordoNegado();
                    break;
                case abandonarPartida:
                    setPartidaEmAndamento(false);
                    netGames.finalizarPartida();
                    String msg = netGames.getNomeJogador()
                    		+ " " + msgs.getString("YourTimeToPlay") 
                    		+ " " + msgs.getString("YouAreTheWinner");
                    atorJogador.notificar(msg);
                    break;
                case atualizarTabuleiro:
                	atorJogador.atualizaTabuleiro(jogada.getTabuleiro());
                    break;
                case encerramento:
                	atorJogador.informaPartidaEncerrada();
                    break;
                default:
                    break;
            }
        }
    }

}
