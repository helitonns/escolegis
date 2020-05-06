package br.leg.alrr.cursos.business;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum usado para indicar o tipo de cadastro do aluno
 *
 * @author Heliton Nascimento
 * @since 2020-05-06
 * @version 1.0
 * @see Log
 */
public enum TipoCadastro {

    EAD, PRESENCIAL;

    public String value() {
        return name();
    }

    public String getValue() {
        return name();
    }

    public static TipoCadastro fromValue(String v) {
        return valueOf(v);
    }

    public List<TipoCadastro> getLista() {
        ArrayList<TipoCadastro> a = new ArrayList<>();
        a.add(TipoCadastro.EAD);
        a.add(TipoCadastro.PRESENCIAL);
        return a;
    }

}
