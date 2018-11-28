package partesInterface;

import br.ufsc.inf.leobr.cliente.Proxy;
import cip.NonComponentPortOutbox;
import interfaces.NGProxy;

public class PortNGOutbox extends NonComponentPortOutbox implements NGProxy {

	public void initialize() {
		
		Proxy proxy = new Proxy();
//		Proxy proxy = Proxy.getInstance();
		this.connectNonComponent(proxy);

	}
	
	@Override
	public boolean conectar(String ip, String nome) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean desconectar() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void iniciarPartida() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enviarJogada() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reiniciarPartida() {
		// TODO Auto-generated method stub
		
	}

}
