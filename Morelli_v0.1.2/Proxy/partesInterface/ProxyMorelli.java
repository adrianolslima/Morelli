package partesInterface;

import cip.ComponentInterface;
import estrutura.NetGames;

public class ProxyMorelli extends ComponentInterface {

	@Override
	public void initialize() {

		PortLogicaProxy pLogicaProxy = new PortLogicaProxy("proxyLogica");
		pLogicaProxy.initialize();
		ports.add(pLogicaProxy);
		
		PortNG portNG = new PortNG("proxyNG");
		portNG.initialize();
		ports.add(portNG);
		
		NetGames netGames = new NetGames();
		
		pLogicaProxy.setReferenciaInterna(netGames);
		portNG.setReferenciaInterna(netGames);
		
		netGames.setPortoLogica(pLogicaProxy);
		netGames.setPortoNG(portNG);
	}

}
