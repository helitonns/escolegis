package br.leg.alrr.cursos.business;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum usado para indicar a ação designada pelo Log.
 * 
 * @author Heliton Nascimento
 * @since 2020-05-01
 * @version 1.0
 * @see Log
 */
public enum TipoAcao {
    
    SALVAR, ATUALIZAR, APAGAR, ACESSAR, EXECUTAR, ENTRAR, SAIR;

    public String value() {
		return name();
	}

	public String getValue() {
		return name();
	}

	public static TipoAcao fromValue(String v) {
		return valueOf(v);
	}
	
	public List<TipoAcao> getLista() {
		ArrayList<TipoAcao> a = new ArrayList<>();
		a.add(TipoAcao.ACESSAR);
		a.add(TipoAcao.ATUALIZAR);
		a.add(TipoAcao.APAGAR);
		a.add(TipoAcao.ENTRAR);
		a.add(TipoAcao.EXECUTAR);
		a.add(TipoAcao.SALVAR);
		a.add(TipoAcao.SAIR);
		return a;
	}
    
}
