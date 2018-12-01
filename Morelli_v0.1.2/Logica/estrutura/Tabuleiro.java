package estrutura;

import java.util.Random;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import classes.Ajuda;
import classes.Faixa;
import classes.JogadaMorelli;
import classes.Jogador;
import classes.Posicao;
import classes.TipoJogada;
import partesInterface.PortInterface;
import partesInterface.PortInterfaceOutbox;
import partesInterface.PortLogicaOutbox;
import partesInterface.PortProxy;
import partesInterface.PortProxyOutbox;

public class Tabuleiro {
	
	/*--- Portos ---*/
	protected PortInterface portoInterface;
	protected PortProxy portoProxy;

	protected ResourceBundle msgs;
	protected Enviador enviador;
	protected Recebedor recebedor;

//    protected AtorJogador atorJogador;
//    protected NetGames netGames;
    
    protected Ajuda ajuda;

    protected boolean partidaEmAndamento;
    protected boolean daVez;

    protected Faixa[] tabuleiro;
    protected Faixa trono;

    protected Jogador jogador;
//    protected Jogador jogador2;

    protected AtorJogador proximoJogador;
    protected Posicao posicaoOrigem;
    protected Posicao posicaoDestino;

    public Tabuleiro(ResourceBundle msgs) {
    	
    	this.msgs = msgs;
    	this.enviador = new Enviador(msgs, this);
    	this.recebedor = new Recebedor(msgs, this);

//        this.atorJogador = atorJogador;
//        this.netGames = new NetGames(this);

        this.ajuda = new Ajuda();

        this.partidaEmAndamento = false;

        this.tabuleiro = new Faixa[7]; // O tabuleiro sempre tem 7 faixas

        this.proximoJogador = null;
        this.posicaoOrigem = null;
        this.posicaoDestino = null;
    }
    
    public PortInterface getPortoInterface() {
		return portoInterface;
	}

	public void setPortoInterface(PortInterface portoInterface) {
		this.portoInterface = portoInterface;
	}

	public PortProxy getPortoProxy() {
		return portoProxy;
	}

	public void setPortoProxy(PortProxy portoProxy) {
		this.portoProxy = portoProxy;
	}

    public void setPartidaEmAndamento(boolean partidaEmAndamento) {
    
    	this.partidaEmAndamento = partidaEmAndamento;
    }

    public boolean isPartidaEmAndamento() {
   
    	return this.partidaEmAndamento;
    }

    public void atualizarTabuleiro(Faixa[] tabuleiro) {
        
    	PortInterfaceOutbox outbox = (PortInterfaceOutbox) portoInterface.getOutbox();

    	this.tabuleiro = tabuleiro;
    	
    	outbox.atualizarTabuleiro(tabuleiro);
    }

    public Faixa[] getTabuleiro() {
        
    	return tabuleiro;
    }

    public void realizarAcordo() {
    	
    	PortInterfaceOutbox outbox = (PortInterfaceOutbox) portoInterface.getOutbox();
        
    	outbox.solicitarAcordo();
    }

    public void atualizaJogadorDaVez() {
        
    	PortInterfaceOutbox outboxInterface = (PortInterfaceOutbox) portoInterface.getOutbox();

//    	AtorJogador aux = this.atorJogador;
//        this.atorJogador = this.proximoJogador;
//        this.proximoJogador = aux;
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
    
    	PortInterfaceOutbox outbox = (PortInterfaceOutbox) portoInterface.getOutbox();

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
            outbox.comunicar(true, e.toString() + " calcularCaptura");
        }
    }

    public Posicao[] verificarAdjacentes(Posicao destino) {
    
    	PortInterfaceOutbox outbox = (PortInterfaceOutbox) portoInterface.getOutbox();

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
            outbox.comunicar(true, e.toString() + " verificarAdjacentes");
        }

        return adjacentes;
    }

    public void calcularTomadaTrono(Posicao posicao) {

    	PortInterfaceOutbox outbox = (PortInterfaceOutbox) portoInterface.getOutbox();

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
                                    Posicao tronoTomado = trono.getPosicoes()[0];
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
            outbox.comunicar(true, e.toString() + " calcularTomadaTrono");            
        }
    }

//    public int calcularVerticeFaixa(Faixa faixa) {
//        // TODO - implement Tabuleiro.calcularVerticeFaixa
//        throw new UnsupportedOperationException();
//    }

    public JogadaMorelli criaJogadaDeFinalizacaoPartida() {
    
    	JogadaMorelli jogada = new JogadaMorelli(TipoJogada.encerramento);
        return jogada;
    }

//    public boolean definePartidaFinalizada() {
//        // TODO - implement Tabuleiro.definePartidaFinalizada
//        throw new UnsupportedOperationException();
//    }

    public void finalizaPartida() {
        
    	setPartidaEmAndamento(false);
//        AtorJogador novoVencedor = this.proximoJogador;
    }

    public boolean calcularMovimento(Posicao origem, Posicao destino) {

    	PortInterfaceOutbox outbox = (PortInterfaceOutbox) portoInterface.getOutbox();

        //O trono não pode receber movimento; deve ser conquistado
        if (destino.getColuna() == 6) {
            if (destino.getLinha() == 6) {
                return false;
            }
        }

        //Verifica se a posicao de destino esta ocupada
        if (destino.isOcupada()) {
            outbox.comunicar(true, "Jogada irregular");
        } else {

            //Verifica se a peca se move em direcao ao centro do tabuleiro
            if (!movimentoAoCentro(origem, destino)) {
                outbox.comunicar(true, "Jogada irregular");

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

//    public Faixa recuperarFaixaDaPosicao() {
//        // TODO - implement Tabuleiro.recuperarFaixaDaPosicao
//        throw new UnsupportedOperationException();
//    }

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
    	
    	PortProxyOutbox outboxProxy = (PortProxyOutbox) portoProxy.getOutbox(); 

    	return outboxProxy.conectar(ip, nomeJogador);
    }

    /*--- Caso de uso: desconectar ---*/
	public boolean desconectar() {

    	PortProxyOutbox outboxProxy = (PortProxyOutbox) portoProxy.getOutbox(); 

		boolean conectado = outboxProxy.desconectar();
		
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
	public boolean solicitarInicioPartida() {
		
		if (partidaEmAndamento) {
			
			PortInterfaceOutbox outboxInterface = (PortInterfaceOutbox) portoInterface.getOutbox();
			
			outboxInterface.comunicar(true, msgs.getString("ThereIsAMatchInProgress"));
		
		} else {

			PortProxyOutbox outboxProxy = (PortProxyOutbox) portoProxy.getOutbox(); 

			partidaEmAndamento = outboxProxy.iniciarPartida();
		}
    	
		return partidaEmAndamento; 
	}

	/*--- Caso de uso: receber solicitacao de inicio ---*/
    public void receberSolicitacaoInicio(int ordem) {

    	PortInterfaceOutbox outboxInterface = (PortInterfaceOutbox) portoInterface.getOutbox();
//    	PortProxyOutbox outboxProxy = (PortProxyOutbox) portoProxy.getOutbox(); 

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
//        String nomeJogador1 = outboxProxy.getNomeAdversario(1);
//        String nomeJogador2 = outboxProxy.getNomeAdversario(2);

        tabuleiro = iniciarPartida(ordem, "nomeJogador1", "nomeJogador2");

        if (ordem == 1) {
            setDaVez(true);
            enviarJogada(TipoJogada.atualizarTabuleiro);
        	outboxInterface.atualizarTabuleiro(tabuleiro);
        }
    }

    public Faixa[] iniciarPartida(int ordem, String nomeJogador1, String nomeJogador2) {

//    	PortInterfaceOutbox outboxInterface = (PortInterfaceOutbox) portoInterface.getOutbox();

        jogador = new Jogador(nomeJogador1);
//        jogador2 = new Jogador(nomeJogador2);

        //jogador1 joga com as pecas pretas e inica a partida
//        jogador1.setDaVez(true);
//        jogador2.setPecasPretas();

        if (ordem == 1) {
//            outbox.setJogador(jogador1);
        } else {
//            outbox.setJogador(jogador2);
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
                tabuleiro[6].getPosicoes()[i].posicionarPeca(true);
                tabuleiro[6].getPosicoes()[i + 24].posicionarPeca(false);
            } else {
                tabuleiro[6].getPosicoes()[i].posicionarPeca(false);
                tabuleiro[6].getPosicoes()[i + 24].posicionarPeca(true);
            }
        }
    }

	public boolean reiniciarPartida() {
		// TODO Auto-generated method stub
		return true;		
	}

    public void movimentarPeca(Posicao origem, Posicao destino) {
    	
    	PortInterfaceOutbox outbox = (PortInterfaceOutbox) portoInterface.getOutbox();

        try {
            if (isDaVez() && isPartidaEmAndamento()) {
                if (origem.getCor() == jogador.getCor()) {
                    posicaoOrigem = origem;
                    posicaoDestino = destino;

                    if (calcularMovimento(origem, destino)) {
                        calcularCaptura(destino);
                        calcularTomadaTrono(destino);
//                        tabuleiroAtualizado = tabuleiro.getTabuleiro();
                        enviarJogada(TipoJogada.atualizarTabuleiro);
                        outbox.atualizarTabuleiro(tabuleiro);
                    }
                }
            }
        } catch (Exception e) {
        	outbox.comunicar(true, e.toString() + " movimentarPeca");
        }
    }

	public ResourceBundle getMsgs() {
		return msgs;
	}

	public void setMsgs(ResourceBundle msgs) {
		this.msgs = msgs;
	}

	public boolean isDaVez() {
		return daVez;
	}

	public void setDaVez(boolean daVez) {
		this.daVez = daVez;
	}

	public Faixa getTrono() {
		return trono;
	}

	public void setTrono(Faixa trono) {
		this.trono = trono;
	}

	public Jogador getJogador1() {
		return jogador;
	}

	public void setJogador1(Jogador jogador1) {
		this.jogador = jogador1;
	}

	public AtorJogador getProximoJogador() {
		return proximoJogador;
	}

	public void setProximoJogador(AtorJogador proximoJogador) {
		this.proximoJogador = proximoJogador;
	}

	public void setAjuda(Ajuda ajuda) {
		this.ajuda = ajuda;
	}

	public void setTabuleiro(Faixa[] tabuleiro) {
		this.tabuleiro = tabuleiro;
	}

	/*--- Caso de uso: notificar ---*/
	public void comunicar(boolean notificacao, String msg) {

		PortInterfaceOutbox outbox = (PortInterfaceOutbox) portoInterface.getOutbox();

		outbox.comunicar(notificacao, msg);
	}

	/*--- Caso de uso: enviar jogada ---*/
	public void enviarJogada(TipoJogada tipo) {
		
		PortProxyOutbox outbox = (PortProxyOutbox) portoProxy.getOutbox();

		JogadaMorelli jogada = enviador.tratarJogada(tipo);
		
		if (jogada != null) {

			outbox.enviarJogada(jogada);
		}
	}

    public void receberJogada(JogadaMorelli jogada) {

    	PortInterfaceOutbox outbox = (PortInterfaceOutbox) portoInterface.getOutbox();
    	
    	recebedor.tratarJogada(jogada);
    }

}
