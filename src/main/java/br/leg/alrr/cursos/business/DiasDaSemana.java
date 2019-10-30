package br.leg.alrr.cursos.business;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author heliton
 */
public enum DiasDaSemana {

    DOMINGO("DOM"), SEGUNDA("SEG"), TERCA("TER"), QUARTA("QUA"), QUINTA("QUI"), SEXTA("SEX"), SABADO("SAB");

    private final String abreviacao;

    private DiasDaSemana(String abreviacao) {
        this.abreviacao = abreviacao;
    }

       public String getValue() {
        return name();
    }

    public static DiasDaSemana fromValue(String v) {
        return valueOf(v);
    }

    public List<DiasDaSemana> getListaComecandoComDomingo() {
        ArrayList<DiasDaSemana> a = new ArrayList<>();
        a.add(DiasDaSemana.DOMINGO);
        a.add(DiasDaSemana.SEGUNDA);
        a.add(DiasDaSemana.TERCA);
        a.add(DiasDaSemana.QUARTA);
        a.add(DiasDaSemana.QUINTA);
        a.add(DiasDaSemana.SEXTA);
        a.add(DiasDaSemana.SABADO);
        return a;
    }
    
    public List<DiasDaSemana> getListaComecandoComSegunda() {
        ArrayList<DiasDaSemana> a = new ArrayList<>();
        a.add(DiasDaSemana.SEGUNDA);
        a.add(DiasDaSemana.TERCA);
        a.add(DiasDaSemana.QUARTA);
        a.add(DiasDaSemana.QUINTA);
        a.add(DiasDaSemana.SEXTA);
        a.add(DiasDaSemana.SABADO);
        a.add(DiasDaSemana.DOMINGO);
        return a;
    }
    
    public List<DiasDaSemana> getListaSemDomingo() {
        ArrayList<DiasDaSemana> a = new ArrayList<>();
        a.add(DiasDaSemana.SEGUNDA);
        a.add(DiasDaSemana.TERCA);
        a.add(DiasDaSemana.QUARTA);
        a.add(DiasDaSemana.QUINTA);
        a.add(DiasDaSemana.SEXTA);
        a.add(DiasDaSemana.SABADO);
        return a;
    }

    public List<DiasDaSemana> getListaMeioDaSemana() {
        ArrayList<DiasDaSemana> a = new ArrayList<>();
        a.add(DiasDaSemana.SEGUNDA);
        a.add(DiasDaSemana.TERCA);
        a.add(DiasDaSemana.QUARTA);
        a.add(DiasDaSemana.QUINTA);
        a.add(DiasDaSemana.SEXTA);
        return a;
    }

    public String getAbreviacao() {
        return abreviacao;
    }
    
    public static DiasDaSemana getDiasDaSemanaPelaAbreviacao(String s){
        switch (s) {
            case "SEG":
                return DiasDaSemana.SEGUNDA;
            case "TER":
                return DiasDaSemana.TERCA;
            case "QUA":
                return DiasDaSemana.QUARTA;
            case "QUI":
                return DiasDaSemana.QUINTA;
            case "SEX":
                return DiasDaSemana.SEXTA;
            case "SAB":
                return DiasDaSemana.SABADO;
            default:
                return DiasDaSemana.DOMINGO;
        }
    }

}
