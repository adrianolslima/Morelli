package estrutura;

import br.ufsc.inf.leobr.cliente.Jogada;
import br.ufsc.inf.leobr.cliente.OuvidorProxy;
import br.ufsc.inf.leobr.cliente.Proxy;
import classes.*;
import partesInterface.PortLogicaOutbox;
import partesInterface.PortLogicaProxy;
import partesInterface.PortLogicaProxyOutbox;
import partesInterface.PortNG;
import partesInterface.PortNGOutbox;

public class NetGames implements OuvidorProxy {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected PortLogicaProxy portoLogica;
	protected PortNG portoNG;

    protected Proxy proxy;
    
    protected boolean conectado;

    public NetGames() {

    	this.conectado = false;
    	proxy = Proxy.getInstance();
    	proxy.addOuvinte(portoNG);
    }

    public void reiniciarPartida() {
        
    	try {
            proxy.reiniciarPartida();
        } catch (Exception e) {
//            atorJogador.notificar(e.getMessage());
        }
    }

    public String getNomeJogador() {
        
    	return proxy.getNomeJogador();
    }

    public String getNomeAdversario(int posicao) {
        
    	return proxy.obterNomeAdversario(posicao);
    }

    public void finalizarPartidaComErro(String msg) {

//        atorJogador.notificar(msg);
    }

    public void tratarConexaoPerdida() {
        
    	String msg = "A conexão com o servidor foi perdida. Partida encerrada";
//        atorJogador.notificar(msg);
    }

    public void tratarPartidaNaoIniciada(String message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void receberMensagem(String msg) {
        
//    	atorJogador.notificar(msg);
    }

    /*--- Caso de uso: conectar ---*/
    public boolean conectar(String ip, String nomeJogador) {
    	
    	PortNGOutbox outbox = (PortNGOutbox) portoNG.getOutbox();
     	
     	try {
     		
     		outbox.conectar(ip, nomeJogador);
     		conectado = true;

     	} catch (Exception e) {
     		
     		System.out.println(e.getMessage());
     		conectado = false;
     	}

     	return conectado;
     }

    /*--- Caso de uso: desconectar ---*/
    public boolean desconectar() {
    	
    	PortNGOutbox outbox = (PortNGOutbox) portoNG.getOutbox();

     	try {
     		
             outbox.desconectar();
             return false;
         
     	} catch (Exception e) {

         	return true;
         }
     }

    /*--- Caso de uso: iniciar partida ---*/
    public boolean iniciarPartida() {
    
    	PortNGOutbox outbox = (PortNGOutbox) portoNG.getOutbox();

    	try {
    		
            outbox.iniciarPartida();
            return true;
            
        } catch (Exception e) {

        	System.out.println(e.getMessage());
        	return false;
        }
    }

    /*--- Caso de uso: iniciar nova partida ---*/
    public void iniciarNovaPartida(Integer posicao) {

    	System.out.println("recebi solicitacao");
    	
    	PortLogicaProxyOutbox outbox = (PortLogicaProxyOutbox) portoLogica.getOutbox();

        outbox.receberSolicitacaoInicio(posicao);
    }

    /*--- Caso de uso: enviar jogada ---*/
    public boolean enviarJogada(JogadaMorelli jogada) {
    
    	try {
    		
            Proxy.getInstance().enviaJogada(jogada);
            return true;
            
        } catch (Exception e) {
        	
            return false;
        }
    }

    /*--- Caso de uso: finalizar partida ---*/
    public boolean finalizarPartida() {
        
    	try {
            proxy.finalizarPartida();
            return false;
            
        } catch (Exception e) {
        	
            return true;
        }
    }

    public void receberJogada(Jogada jogada) {
     
    	PortLogicaProxyOutbox outbox = (PortLogicaProxyOutbox) portoLogica.getOutbox();

    	outbox.receberJogada((JogadaMorelli) jogada);
    }

	public void setPortoLogica(PortLogicaProxy pLogicaProxy) {

		this.portoLogica = pLogicaProxy;
	}

	public void setPortoNG(PortNG portNG) {

		this.portoNG = portNG;
	}
}
