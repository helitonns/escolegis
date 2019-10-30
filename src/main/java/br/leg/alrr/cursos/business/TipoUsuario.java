package br.leg.alrr.cursos.business;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author heliton
 */
public enum TipoUsuario {

    ADMIN, OPERADOR, SUPER_ADMIN, RELATORIO;

    public String value() {
        return name();
    }

    public String getValue() {
        return name();
    }

    public static TipoUsuario fromValue(String v) {
        return valueOf(v);
    }

    public List<TipoUsuario> getLista() {
        ArrayList<TipoUsuario> a = new ArrayList<>();
        a.add(TipoUsuario.RELATORIO);
        a.add(TipoUsuario.OPERADOR);
        a.add(TipoUsuario.ADMIN);
        return a;
    }
    
    public List<TipoUsuario> getListaGeral() {
        ArrayList<TipoUsuario> a = new ArrayList<>();
        a.add(TipoUsuario.RELATORIO);
        a.add(TipoUsuario.OPERADOR);
        a.add(TipoUsuario.ADMIN);
        a.add(TipoUsuario.SUPER_ADMIN);
        return a;
    }

}
