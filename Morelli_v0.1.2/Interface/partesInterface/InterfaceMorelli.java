package partesInterface;

import java.util.Locale;
import java.util.ResourceBundle;

import cip.ComponentInterface;
import estrutura.AtorJogador;

public class InterfaceMorelli extends ComponentInterface {

	@Override
	public void initialize() {
		
		PortLogica pLogica = new PortLogica("interfaceLogica");
		pLogica.initialize();
		ports.add(pLogica);
		
		Locale currentLocale = Locale.getDefault();
		ResourceBundle bundle = ResourceBundle.getBundle(
				"ApplicationMessages", currentLocale);

		AtorJogador jogador = new AtorJogador(bundle);
		
		pLogica.setReferenciaInterna(jogador);
		jogador.setPortoLogica(pLogica);
	}

}
