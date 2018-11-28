package partesInterface;

import cip.ComponentInterface;
import estrutura.Tabuleiro;

public class LogicaMorelli extends ComponentInterface {

	@Override
	public void initialize() {

		PortInterface pInterface = new PortInterface("logicaInterface");
		pInterface.initialize();
		ports.add(pInterface);
		
		PortProxy pProxy = new PortProxy("logicaProxy");
		pProxy.initialize();
		ports.add(pProxy);
		
		Tabuleiro tabuleiro = new Tabuleiro();
		
		pInterface.setReferenciaInterna(tabuleiro);
		pProxy.setReferenciaInterna(tabuleiro);
		
		tabuleiro.setPortoInterface(pInterface);
		tabuleiro.setPortoProxy(pProxy);
	}

}
