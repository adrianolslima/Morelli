package estrutura;

import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import classes.Faixa;
import classes.JogadaMorelli;
import classes.Posicao;
import classes.TipoJogada;
import interfaces.InterfaceLogica;
import partesInterface.PortLogica;
import partesInterface.PortLogicaOutbox;

public class AtorJogador implements InterfaceLogica {
	
	protected PortLogica portoLogica;

	protected ResourceBundle msgs;
	
    protected TelaJogador tela;
    
    protected boolean conectado;

    protected Faixa[] tabuleiro;
    protected Posicao posicaoOrigem;
    protected Posicao posicaoDestino;

    public AtorJogador(ResourceBundle msgs) {

    	this.msgs = msgs;
    	
        this.tela = new TelaJogador(this, msgs);
        this.tela.setVisible(true);

        this.conectado = false;

        this.posicaoOrigem = null;
        this.posicaoDestino = null;

    }

    public void informarEmpate() {

        tela.exibeMensagemEmpate();
    }

    @Override
    public void solicitarAcordo() {
    	
    	int resposta = tela.solicitarAcordo();
        		
        if (resposta == JOptionPane.YES_OPTION) {
        	
            enviarJogada(TipoJogada.acordoAceito);
            
        } else {
        	
        	enviarJogada(TipoJogada.acordoNegado);
        }
    }

    public void movimentarPeca(Posicao origem, Posicao destino) {
    	
    	this.posicaoOrigem = origem;
    	this.posicaoDestino = destino;
        
    	enviarJogada(TipoJogada.atualizarTabuleiro);
    }

    public void abandonarPartida() {
    	
    	enviarJogada(TipoJogada.abandonarPartida);
    }

    public void informarPartidaEncerrada() {
    	//TODO
    }

    public void notificar(String msg) {
    	
        tela.notificar(msg);
    }
    
    public void comunicar(boolean notificacao, String msg) {
    	
    	if (notificacao) {
    		
    		tela.notificar(msg);
    		
    	} else {
    		
    		tela.informar(msg);
    	}
    }

    public void notificarIrregularidade() {
    	
    	tela.notificar(msgs.getString("IrregularPlay"));
    }

    /*--- Caso de uso: conectar interno ---*/
    public void conectar() {

    	PortLogicaOutbox outbox = (PortLogicaOutbox) portoLogica.getOutbox();

        if (!conectado) {

            String ip = tela.solicitarIpServidor();
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

    	PortLogicaOutbox outbox = (PortLogicaOutbox) portoLogica.getOutbox();
    	
    	JogadaMorelli jogada = new JogadaMorelli(tipoJogada, tabuleiro, posicaoOrigem, posicaoDestino);

    	tela.informar(msgs.getString("WaitUntilYourOpponentHasPlayed"));
        
    	outbox.enviarJogada(jogada);
    }

	/*--- Caso de uso: atualizar tabuleiro ---*/
	public void atualizarTabuleiro(Faixa[] tabuleiroAtualizado) {
    	
        this.tabuleiro = tabuleiroAtualizado;

        try {
        	tela.atualizarTabuleiro(tabuleiroAtualizado);
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
	}

	@Override
	public void informarVencedor() {
		// TODO Auto-generated method stub
		
	}

	public void setPortoLogica(PortLogica portoLogica) {
		this.portoLogica = portoLogica;
	}

	public void proporAcordo() {

		enviarJogada(TipoJogada.proporAcordo);
	}
	
	
    
}
