package estrutura;

import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import classes.Faixa;
import classes.Jogador;
import classes.Posicao;
import classes.TipoJogada;
import interfaces.InterfaceLogica;
import partesInterface.PortLogica;
import partesInterface.PortLogicaOutbox;
import partesInterface.PortLogicaProxy;

public class AtorJogador implements InterfaceLogica {
	
	protected PortLogica portoLogica;

	protected ResourceBundle msgs;
	
    protected TelaJogador tela;
    
    protected boolean conectado;
    protected boolean partidaEmAndamento;

//    protected Jogador jogador;
    protected boolean daVez;

    protected Faixa[] tabuleiroAtualizado;
    protected Posicao posicaoOrigem;
    protected Posicao posicaoDestino;

    public AtorJogador(ResourceBundle msgs) {

    	this.msgs = msgs;
    	
        this.tela = new TelaJogador(this, msgs);
        this.tela.setVisible(true);

        this.conectado = false;
        this.partidaEmAndamento = false;

//        this.jogador = null;
        this.daVez = false;

        this.posicaoOrigem = null;
        this.posicaoDestino = null;

    }

//    public void setJogador(Jogador jogador) {
//    	
//        this.jogador = jogador;
//    }
//
//    public Jogador getJogador() {
//    	
//        return jogador;
//    }

    public void setDaVez(boolean vez) {
    	
        this.daVez = vez;
    }

    public boolean isDaVez() {
    	
        return daVez;
    }
    
    public boolean isPartidaEmAndamento() {
    	
    	return partidaEmAndamento;
    }

    public void informarEmpate() {
        setPartidaEmAndamento(false);
        tela.exibeMensagemEmpate();
    }

    @Override
    public void solicitarAcordo() {
    	
    	PortLogicaOutbox portOutbox = (PortLogicaOutbox) portoLogica.getOutbox();
    	
        int resposta = JOptionPane.showConfirmDialog(null,
                msgs.getString("DoYouWantToMakeADealAndFinishTheMatch"),
                msgs.getString("YourOpponentAskedForADeal"), 
                JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            this.informarEmpate();
            portOutbox.enviarJogada(TipoJogada.acordoAceito);
        } else {
        	portOutbox.enviarJogada(TipoJogada.acordoNegado);
        }
    }

    public void movimentarPeca(Posicao origem, Posicao destino) {
        
    	PortLogicaOutbox portOutbox = (PortLogicaOutbox) portoLogica.getOutbox();

    	try {
            if (daVez && partidaEmAndamento) {

//            	portOutbox.movimentarPeca(origem, destino);
            	
            } else if (!partidaEmAndamento) {
            	
            	tela.notificar("Não há partida em andamento!");
            	
            } else {
            	
            	tela.notificar(msgs.getString("ItIsNotYourTurn"));
            }
            
        } catch (Exception e) {
        	
            notificar(e.toString() + " movimentarPeca");
        }
    }

//    public void receberJogada(JogadaMorelli jogada) {
//
//        setDaVez(true);
//        tela.informar(msgs.getString("YourTimeToPlay"));
//        TipoJogada tipoJogada = jogada.getTipoDeJogada();
//
//        if (null != tipoJogada) {
//            switch (tipoJogada) {
//                case realizarAcordo:
//                    this.realizarAcordo();
//                    break;
//                case acordoAceito:
//                    finalizarPartidaEmpate();
//                    break;
//                case acordoNegado:
//                    tela.exibeMensagemAcordoNegado();
//                    break;
//                case abandonarPartida:
//                    tabuleiro.setPartidaEmAndamento(false);
//                    netGames.finalizarPartida();
//                    String msg = netGames.getNomeJogador()
//                    		+ " " + msgs.getString("YourTimeToPlay") 
//                    		+ " " + msgs.getString("YouAreTheWinner");
//                    notificar(msg);
//                    break;
//                case atualizarTabuleiro:
//                    atualizaTabuleiro(jogada.getTabuleiro());
//                    break;
//                case encerramento:
//                    informaPartidaEncerrada();
//                    break;
//                default:
//                    break;
//            }
//        }
//    }

    public void abandonarPartida() {
    	
    	PortLogicaOutbox portOutbox = (PortLogicaOutbox) portoLogica.getOutbox();

    	portOutbox.enviarJogada(TipoJogada.abandonarPartida);
    }

    public void informaPartidaEncerrada() {
    	
    	PortLogicaOutbox portOutbox = (PortLogicaOutbox) portoLogica.getOutbox();
    	
//    	finalizarPartida();
    }

    public void notificar(String msg) {
    	
        tela.notificar(msg);
    }
    
    public void comunicar(boolean notificacao, String msg) {
    	
    	tela.notificar(msg);
    }

    public void notificarIrregularidade() {
    	
    	tela.notificar(msgs.getString("IrregularPlay"));
    }

    /*--- Caso de uso: conectar interno ---*/
    public void conectar() {

    	PortLogicaOutbox outbox = (PortLogicaOutbox) portoLogica.getOutbox();

        if (!conectado) {

        	String ip = "localhost";
//            String ip = tela.solicitarIpServidor();
            String nomeJogador = tela.solicitarNomeJogador();

           	conectado = outbox.conectar(ip, nomeJogador);

           	if (conectado) {
           		
            	tela.informar(msgs.getString("Connected"));
            
            } else {
				
            	tela.notificar("String: Problema na conexão. Tente novamente!");
			}
            
        } else {
        	
            tela.notificar(msgs.getString("YouAreAlreadyConnected"));
        }        
    }

    /*--- Caso de uso: desconectar interno ---*/
    public void desconectar() {

    	PortLogicaOutbox portOutbox = (PortLogicaOutbox) portoLogica.getOutbox();

        if (conectado) {
        	
        	conectado = portOutbox.desconectar();

        	if (!conectado) {
        	            
        		tela.informar(msgs.getString("Disconnected"));
//        		partidaEmAndamento = false;
        	
        	} else {
        		
        		tela.notificar("String: Problema na desconexão. Tente novamente!");
        	}
            
        } else {
        	
            tela.notificar(msgs.getString("YouAreAlreadyDisconnected"));
        }
    }

    /*--- Caso de uso: ajuda interno ---*/
    public void getAjuda() {

    	PortLogicaOutbox portOutbox = (PortLogicaOutbox) portoLogica.getOutbox();

    	tela.informar(portOutbox.getAjuda());
    }

    /*--- Caso de uso: iniciar partida interno ---*/
    public void iniciarPartida() {

    	PortLogicaOutbox portOutbox = (PortLogicaOutbox) portoLogica.getOutbox();

    	if (conectado) {
    		
    		boolean partidaIniciada = portOutbox.iniciarPartida();
    		
    		if (!partidaIniciada) {
    			
    			tela.notificar("String: Partida não iniciada!");
    		}

    	} else {
        	
            tela.notificar(msgs.getString("YouAreDisconnected"));
    	
    	}
    }

    /*--- Caso de uso: enviar jogada interno ---*/
    public void enviarJogada(TipoJogada tipoJogada) {

    	PortLogicaOutbox portOutbox = (PortLogicaOutbox) portoLogica.getOutbox();

        if (daVez) {
        	
            setDaVez(false);
            tela.informar(msgs.getString("WaitUntilYourOpponentHasPlayed"));
            portOutbox.enviarJogada(tipoJogada);
        	
        } else {
        	
        	tela.notificar(msgs.getString("ItIsNotYourTurn"));
        }
    }

//    public void receberSolicitacaoInicio(int ordem) {
//
//        if (ordem == 1) {
//        	
//            tela.informar(msgs.getString("Name") + netGames.getNomeAdversario(1) + "\n");
//            tela.informar(msgs.getString("Opponent") + netGames.getNomeAdversario(2) + "\n\n");
//            tela.informar(msgs.getString("YouPlayWith") + " " + msgs.getString("WhiteStones") + ".\n");
//            tela.informar(msgs.getString("YourOpponentStartsPlaying"));
//            setDaVez(true);
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
//        String nomeJogador1 = netGames.getNomeAdversario(1);
//        String nomeJogador2 = netGames.getNomeAdversario(2);
//
//        tabuleiroAtualizado = tabuleiro.iniciarPartida(ordem, nomeJogador1, nomeJogador2);
//
//        if (ordem == 1) {
//            enviarJogada(TipoJogada.atualizarTabuleiro);
//            tela.atualizaTabuleiro(tabuleiroAtualizado);
//        }
//    }

	/*--- Caso de uso: atualizar tabuleiro ---*/
	public void atualizarTabuleiro(Faixa[] tabuleiroAtualizado) {
    	
    	PortLogicaOutbox portOutbox = (PortLogicaOutbox) portoLogica.getOutbox();

        this.tabuleiroAtualizado = tabuleiroAtualizado;
//        portOutbox.atualizarTabuleiro(tabuleiroAtualizado);
        try {
        tela.atualizaTabuleiro(tabuleiroAtualizado);
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
	}

	@Override
	public void informarVencedor() {
		// TODO Auto-generated method stub
		
	}

	public PortLogica getPortoLogica() {
		return portoLogica;
	}

	public void setPortoLogica(PortLogica portoLogica) {
		this.portoLogica = portoLogica;
	}

	public ResourceBundle getMsgs() {
		return msgs;
	}

	public void setMsgs(ResourceBundle msgs) {
		this.msgs = msgs;
	}

	public TelaJogador getTela() {
		return tela;
	}

	public void setTela(TelaJogador tela) {
		this.tela = tela;
	}

	public boolean isConectado() {
		return conectado;
	}

	public void setConectado(boolean conectado) {
		this.conectado = conectado;
	}

	public Faixa[] getTabuleiroAtualizado() {
		return tabuleiroAtualizado;
	}

	public void setTabuleiroAtualizado(Faixa[] tabuleiroAtualizado) {
		this.tabuleiroAtualizado = tabuleiroAtualizado;
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

	public void setPartidaEmAndamento(boolean partidaEmAndamento) {
		this.partidaEmAndamento = partidaEmAndamento;
	}

	public void proporAcordo() {

		PortLogicaOutbox outbox = (PortLogicaOutbox) portoLogica.getOutbox();

		outbox.enviarJogada(TipoJogada.proporAcordo);
	}
	
	
    
}
