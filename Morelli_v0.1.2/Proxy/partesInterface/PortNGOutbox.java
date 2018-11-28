package partesInterface;

import br.ufsc.inf.leobr.cliente.Proxy;
import br.ufsc.inf.leobr.cliente.exception.ArquivoMultiplayerException;
import br.ufsc.inf.leobr.cliente.exception.JahConectadoException;
import br.ufsc.inf.leobr.cliente.exception.NaoConectadoException;
import br.ufsc.inf.leobr.cliente.exception.NaoJogandoException;
import br.ufsc.inf.leobr.cliente.exception.NaoPossivelConectarException;
import cip.NonComponentPortOutbox;
import classes.JogadaMorelli;
import classes.TipoJogada;
import interfaces.NGProxy;

public class PortNGOutbox extends NonComponentPortOutbox implements NGProxy {

	public void initialize() {
		
//		Proxy proxy = new Proxy();
		Proxy proxy = Proxy.getInstance();
		this.connectNonComponent(proxy);

	}
	
	@Override
	public boolean conectar(String ip, String nome) {

		try {
			
			((Proxy) externalReference).conectar(ip, nome);
			return true; 
			
		} catch (Exception e) {
			
			return false;
		}
	}

	@Override
	public boolean desconectar() {

		try {
			
			((Proxy) externalReference).desconectar();
			return false;
			
		} catch (NaoConectadoException e) {

			return true;
		}
	}

	@Override
	public void iniciarPartida() {

		try {
    		
			((Proxy) externalReference).iniciarPartida(2);
            
        } catch (Exception e) {

        	System.out.println(e.getStackTrace());
        }
	}

	@Override
	public void enviarJogada() {

		try {
    		
			JogadaMorelli jogada = new JogadaMorelli(TipoJogada.realizarAcordo);
            ((Proxy) externalReference).enviaJogada(jogada);
//            return true;
            
        } catch (Exception e) {
        	
//            return false;
        }
	}

	@Override
	public void reiniciarPartida() {

		try {

			((Proxy) externalReference).reiniciarPartida();
		
		} catch (Exception e) {

			System.out.println("TEstando!");
		}
	}

}
