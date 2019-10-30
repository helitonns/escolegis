package br.leg.alrr.cursos.business;

/**
 *
 * @author heliton
 */
public class BlocoParametro {

    private String parametro;
    private Object valor;

    public String getParametro() {
        return parametro;
    }

    public BlocoParametro() {
    }
    
    public BlocoParametro(String parametro, Object valor) {
        this.parametro = parametro;
        this.valor = valor;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }
    
    
}
