/*
 * Versão 0.1.2 - Componentes
 */
package morelli;

import cip.InterfacePort;
import partesInterface.InterfaceMorelli;
import partesInterface.LogicaMorelli;
import partesInterface.ProxyMorelli;

/**
 *
 * @author Adriano Lima
 */
public class Morelli {

	public static void main(String args[]) {

		InterfaceMorelli im = new InterfaceMorelli();
		im.initialize();
		
		LogicaMorelli lm = new LogicaMorelli();
		lm.initialize();
		
		ProxyMorelli pm = new ProxyMorelli();
		pm.initialize();
		
		InterfacePort portIL = im.getPort("interfaceLogica");
		InterfacePort portLI = lm.getPort("logicaInterface");
		im.connect(portLI, "interfaceLogica");
		lm.connect(portIL, "logicaInterface");
		
		InterfacePort portLP = lm.getPort("logicaProxy");
		InterfacePort portPL = pm.getPort("proxyLogica");
		lm.connect(portPL, "logicaProxy");
		pm.connect(portLP, "proxyLogica");
	}

}
