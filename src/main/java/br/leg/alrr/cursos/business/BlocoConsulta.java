package br.leg.alrr.cursos.business;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.model.SelectItem;

/**
 *
 * @author heliton
 */
public class BlocoConsulta {

    private SelectItem tipo;
    private Object valor1;
    private String conectivo;
    private String texto;

    public SelectItem getTipo() {
        return tipo;
    }

    public void setTipo(SelectItem tipo) {
        this.tipo = tipo;
    }

    public Object getValor1() {
        return valor1;
    }

    public void setValor1(Object valor1) {
        this.valor1 = valor1;
    }

    public String getConectivo() {
        return conectivo;
    }

    public void setConectivo(String conectivo) {
        this.conectivo = conectivo;
    }

    public String getValor() {

        int i = Integer.parseInt(tipo.getValue().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM");
        String s = "";
        if (!conectivo.equals("-")) {
            s = " - " + conectivo;
        }

        switch (i) {
            case 0:
                return ("realizadas na data " + sdf.format((Date) valor1) + s).toLowerCase();
            case 1:
                return ("realizadas antes da data " + sdf.format((Date) valor1) + s).toLowerCase();
            case 2:
                return ("realizadas depois da data " + sdf.format((Date) valor1) + s).toLowerCase();
            case 3:
                return ("realizadas entre a data " + sdf.format((Date) valor1) + s).toLowerCase();
            case 4:
                return ("a data  " + sdf.format((Date) valor1) + s).toLowerCase();
            case 5:
                return ("com o seguinte CPF: " + valor1.toString() + s).toLowerCase();
            case 6:
                return ("de alunos nascidos na data  " + sdf2.format((Date) valor1) + s).toLowerCase();
            case 7:
                return ("de alunos nascidos entre a data  " + sdf2.format((Date) valor1) + s).toLowerCase();
            case 8:
                return ("a data  " + sdf2.format((Date) valor1) + s).toLowerCase();
            case 9:
                return ("de alunos do sexo: " + valor1.toString() + s).toLowerCase();
            case 10:
                boolean b = (boolean) valor1;
                return (b ? "de alunos que possuem filhos " + s : "de alunos que NÃO possuem filhos" + s).toLowerCase();
            case 11:
                boolean bb = (boolean) valor1;
                return (bb ? "em andamento " + s : "finalizadas" + s).toLowerCase();
            case 12:
                return ("do o curso: " + texto.toUpperCase() + s);
            case 13:
                return ("de alunos que moram no município: " + texto.toUpperCase() + s);
            case 14:
                return ("de alunos que moram no bairro: " + texto.toUpperCase() + s);
            case 15:
                return ("de alunos que têm como país de origem: " + texto.toUpperCase() + s);
            case 16:
                return ("de alunos com idade maior ou igual: " + (int)valor1 + " anos");
            case 17:
                return ("de alunos com idade menor ou igual: " + (int)valor1 + " anos");
            case 18:
                return ("de alunos com idade entre: " + (int)valor1);
            case 19:
                return ("e: " + (int)valor1 + " anos");
            default:
                break;
        }
        return "";
    }

    public String getConectivoParaConsulta() {
        if (conectivo.equalsIgnoreCase("e")) {
            return "and ";
        } else if (conectivo.equalsIgnoreCase("ou")) {
            return "or ";
        }
        return "";
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

}
