package br.leg.alrr.cursos.business;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author heliton
 */
public enum Escolaridade {
    
    FUNDAMENTAL, MEDIO, SUPERIOR, POS_GRADUACAO;

    public String value() {
		return name();
	}

	public String getValue() {
		return name();
	}

	public static Escolaridade fromValue(String v) {
		return valueOf(v);
	}
	
	public List<Escolaridade> getLista() {
		ArrayList<Escolaridade> a = new ArrayList<>();
		a.add(Escolaridade.FUNDAMENTAL);
		a.add(Escolaridade.MEDIO);
		a.add(Escolaridade.SUPERIOR);
		a.add(Escolaridade.POS_GRADUACAO);
		return a;
	}
    
}
