package estrutura;

import br.ufsc.inf.leobr.cliente.Jogada;
import br.ufsc.inf.leobr.cliente.OuvidorProxy;
import br.ufsc.inf.leobr.cliente.Proxy;
import classes.*;
import partesInterface.PortLogicaOutbox;
import partesInterface.PortLogicaProxy;
import partesInterface.PortLogicaProxyOutbox;
import partesInterface.PortNG;

public class NetGames implements OuvidorProxy {
	
	protected PortLogicaProxy portoLogica;
	protected PortNG portoNG;

//    protected AtorJogador atorJogador;
//    protected Tabuleiro tabuleiro;
    protected Proxy proxy;
    
    protected boolean conectado = false;

    public NetGames() {

        super();

//        this.tabuleiro = tabuleiro;
        this.proxy = Proxy.getInstance();

        this.proxy.addOuvinte(this);
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

    @Override
    public void finalizarPartidaComErro(String msg) {

//        atorJogador.notificar(msg);
    }

    @Override
    public void tratarConexaoPerdida() {
        
    	String msg = "A conexão com o servidor foi perdida. Partida encerrada";
//        atorJogador.notificar(msg);
    }

    @Override
    public void tratarPartidaNaoIniciada(String message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void receberMensagem(String msg) {
        
//    	atorJogador.notificar(msg);
    }

    /*--- Caso de uso: conectar ---*/
    public boolean conectar(String ip, String nomeJogador) {
     	
     	try {
     		
     		proxy.conectar(ip, nomeJogador);
     		return true;

     	} catch (Exception e) {
     		
     		return false;
     	}
     }

    /*--- Caso de uso: desconectar ---*/
    public boolean desconectar() {
     
     	try {
     		
             proxy.desconectar();
             return false;
         
     	} catch (Exception e) {

         	return true;
         }
     }

    /*--- Caso de uso: iniciar partida ---*/
    public boolean iniciarPartida() {
    
    	try {
    		
            proxy.iniciarPartida(2);
            return true;
            
        } catch (Exception e) {

        	return false;
        }
    }

    /*--- Caso de uso: iniciar nova partida ---*/
    @Override
    public void iniciarNovaPartida(Integer posicao) {

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

    @Override
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
