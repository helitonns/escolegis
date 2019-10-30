package br.leg.alrr.cursos.business;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author heliton
 */
public enum Sexo {
    
    MASCULINO, FEMININO, OUTRO;

    public String value() {
		return name();
	}

	public String getValue() {
		return name();
	}

	public static Sexo fromValue(String v) {
		return valueOf(v);
	}
	
	public List<Sexo> getLista() {
		ArrayList<Sexo> a = new ArrayList<>();
		a.add(Sexo.MASCULINO);
		a.add(Sexo.FEMININO);
		a.add(Sexo.OUTRO);
		return a;
	}
	
        public List<Sexo> getListaResumida() {
		ArrayList<Sexo> a = new ArrayList<>();
		a.add(Sexo.MASCULINO);
		a.add(Sexo.FEMININO);
		return a;
	}
    
}
