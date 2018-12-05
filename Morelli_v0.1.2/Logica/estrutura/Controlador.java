package estrutura;

import java.util.Random;
import java.util.ResourceBundle;

import classes.Ajuda;
import classes.Faixa;
import classes.JogadaMorelli;
import classes.Jogador;
import classes.Posicao;
import classes.TipoJogada;
import partesInterface.PortInterface;
import partesInterface.PortInterfaceOutbox;
import partesInterface.PortProxy;
import partesInterface.PortProxyOutbox;

public class Controlador {
	
	/*--- Portos ---*/
	protected PortInterface portoInterface;
	protected PortProxy portoProxy;

	protected ResourceBundle msgs;
	protected Enviador enviador;
	protected Recebedor recebedor;

    protected Ajuda ajuda;

    protected boolean partidaEmAndamento;
    protected boolean daVez;

    protected Faixa[] tabuleiro;
    protected Faixa trono;

    protected Jogador jogador;

    protected Posicao origem;
    protected Posicao destino;

    public Controlador(ResourceBundle msgs) {
    	
    	this.msgs = msgs;
    	this.enviador = new Enviador(msgs, this);
    	this.recebedor = new Recebedor(msgs, this);

        this.ajuda = new Ajuda();

        this.partidaEmAndamento = false;

        this.tabuleiro = new Faixa[7]; // O tabuleiro sempre tem 7 faixas

        this.origem = null;
        this.destino = null;
    }

	public void setPortoInterface(PortInterface portoInterface) {
		this.portoInterface = portoInterface;
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

    	daVez = true;
    	this.tabuleiro = tabuleiro;
    	
    	outbox.atualizarTabuleiro(tabuleiro);
    }

    public void realizarAcordo() {
    	
    	PortInterfaceOutbox outbox = (PortInterfaceOutbox) portoInterface.getOutbox();
        
    	outbox.solicitarAcordo();
    }


    public boolean calcularMovimentoAoCentro(Posicao origem, Posicao destino) {
        
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
    
//    	PortInterfaceOutbox outbox = (PortInterfaceOutbox) portoInterface.getOutbox();

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
            //Busca adjacentes na faixa inferior
            posicoes = tabuleiro[faixaInferior].getPosicoes();
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
        } catch (Exception e) {
//            outbox.comunicar(true, e.toString() + " verificarAdjacentes");
        }

        return adjacentes;
    }

    public void calcularTomadaTrono(Posicao posicao) {

//    	PortInterfaceOutbox outbox = (PortInterfaceOutbox) portoInterface.getOutbox();

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
        	int posVertice = (indice + lado) % numPosicoes;
            Posicao vertice = posicoes[posVertice];
       
            if (vertice.isOcupada()) {
                if (vertice.getCor() == cor) {
                    lado = lado * 2;
                    posVertice = (indice + lado) % numPosicoes;
                    vertice = posicoes[posVertice];
            
                    if (vertice.isOcupada()) {
                        if (vertice.getCor() == cor) {
                            lado = lado + lado / 2;
                            posVertice = (indice + lado) % numPosicoes;
                            vertice = posicoes[posVertice];
                    
                            if (vertice.isOcupada()) {
                                if (vertice.getCor() == cor) {
                            
                                	Posicao tronoTomado = tabuleiro[0].getPosicoes()[0];
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
//            outbox.comunicar(true, e.toString() + " calcularTomadaTrono");            
        }
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
            if (!calcularMovimentoAoCentro(origem, destino)) {
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

        if (ordem == 1) {
            
        	criarTabuleiro();
        	distribuirPecas();
        	
        	jogador = new Jogador("nomeJogador1");
        	jogador.setPecasPretas();
            
            JogadaMorelli jogada = new JogadaMorelli(TipoJogada.atualizarTabuleiro, tabuleiro);
            
            enviarJogada(jogada);
        	outboxInterface.atualizarTabuleiro(tabuleiro);

        } else {
        	
        	jogador = new Jogador("nomeJogador2");
        }

        partidaEmAndamento = true;
    }

    public void criarTabuleiro() {

        //Cria as faixas do tabuleiro
        for (int i = 0; i < 7; i++) {
            Faixa faixa = new Faixa(i);
            this.tabuleiro[i] = faixa;
        }

        //Define trono
        this.trono = tabuleiro[0];

    }

    public void distribuirPecas() {

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

    public JogadaMorelli movimentarPeca(JogadaMorelli jogada) {
    	
    	PortInterfaceOutbox outbox = (PortInterfaceOutbox) portoInterface.getOutbox();
    	
    	origem = jogada.getOrigem();
    	destino = jogada.getDestino();
    	
    	if (origem == null || destino == null) {
    	
    		return jogada;
    		
    	} else if (daVez && partidaEmAndamento) {
            	
    		if (origem.getCor() == jogador.getCor()) {

    			if (calcularMovimento(origem, destino)) {
    				
    				calcularCaptura(destino);
    				calcularTomadaTrono(destino);
    				outbox.atualizarTabuleiro(tabuleiro);
    				
    				daVez = false;
    			
    			} else {
    				
    				outbox.comunicar(true, msgs.getString("IrregularPlay"));
    			}
    			
    			return new JogadaMorelli(TipoJogada.atualizarTabuleiro, tabuleiro);
    		
    		} else {
    			
    			if (jogador.getCor()) {
    				
    				outbox.comunicar(true, msgs.getString("YouPlayWith")
    						+ " " + msgs.getString("BlackStones"));
    				
    			} else {
    				
    				outbox.comunicar(true, msgs.getString("YouPlayWith")
    						+ " " + msgs.getString("WhiteStones"));
    			}
    			
    			
    			return null;
    		}
    		
    	} else if (!daVez) {
    		
    		outbox.comunicar(true, msgs.getString("NotYourTurn"));
    		
    		return null;
    		
    	} else {
    		
    		outbox.comunicar(true, msgs.getString("ThereIsNoMatchInProgress"));
    		
    		return null;
    	}
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

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador1) {
		this.jogador = jogador1;
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
	public void enviarJogada(JogadaMorelli jogada) {
		
		PortProxyOutbox outbox = (PortProxyOutbox) portoProxy.getOutbox();

		JogadaMorelli jogadaTratada = enviador.tratarJogada(jogada);
		
		if (jogadaTratada != null) {

			outbox.enviarJogada(jogadaTratada);
		}
	}

    public void receberJogada(JogadaMorelli jogada) {

    	recebedor.tratarJogada(jogada);
    }

}
