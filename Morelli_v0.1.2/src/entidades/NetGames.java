package entidades;

import br.ufsc.inf.leobr.cliente.*;
import interfaceGrafica.AtorJogador;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetGames implements OuvidorProxy {

    protected AtorJogador atorJogador;
    protected Tabuleiro tabuleiro;
    protected Proxy proxy;
    
    protected boolean conectado = false;

    public NetGames(AtorJogador atorJogador) {

        super();

        this.atorJogador = atorJogador;
        this.proxy = Proxy.getInstance();

        this.proxy.addOuvinte(this);
    }

    public NetGames(Tabuleiro tabuleiro) {

        super();

        this.tabuleiro = tabuleiro;
        this.proxy = Proxy.getInstance();

        this.proxy.addOuvinte(this);
    }

    public void reiniciarPartida() {
        
    	try {
            proxy.reiniciarPartida();
        } catch (Exception e) {
            atorJogador.notificar(e.getMessage());
        }
    }

    public void finalizarPartida() {
        
    	try {
            proxy.finalizarPartida();
        } catch (Exception e) {
            atorJogador.notificar(e.getMessage());
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

        atorJogador.notificar(msg);
    }

    @Override
    public void tratarConexaoPerdida() {
        
    	String msg = "A conexão com o servidor foi perdida. Partida encerrada";
        atorJogador.notificar(msg);
    }

    @Override
    public void tratarPartidaNaoIniciada(String message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void receberMensagem(String msg) {
        
    	atorJogador.notificar(msg);
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

    /*--- Caso de uso: conectar ---*/
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
    	
        tabuleiro.receberSolicitacaoInicio(posicao);
    }

    /*--- Caso de uso: enviar jogada ---*/
    public void enviarJogada(JogadaMorelli jogada) {
    
    	try {
            Proxy.getInstance().enviaJogada(jogada);
        } catch (Exception e) {
            atorJogador.notificar(e.getMessage());
        }
    }

    @Override
    public void receberJogada(Jogada jogada) {
     
    	tabuleiro.receberJogada((JogadaMorelli) jogada);
    }
}
