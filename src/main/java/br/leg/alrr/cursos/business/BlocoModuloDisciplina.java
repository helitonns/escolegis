package br.leg.alrr.cursos.business;

/**
 *
 * @author Heliton
 */
public class BlocoModuloDisciplina implements Comparable<BlocoModuloDisciplina> {

    private Long idModulo;
    private String nomeModulo;

    private Long idDisciplina;
    private String nomeDiscipina;

    public BlocoModuloDisciplina() {
    }

    public BlocoModuloDisciplina(Long idModulo, String nomeModulo, Long idDisciplina, String nomeDiscipina) {
        this.idModulo = idModulo;
        this.nomeModulo = nomeModulo;
        this.idDisciplina = idDisciplina;
        this.nomeDiscipina = nomeDiscipina;
    }

    public Long getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(Long idModulo) {
        this.idModulo = idModulo;
    }

    public String getNomeModulo() {
        return nomeModulo;
    }

    public void setNomeModulo(String nomeModulo) {
        this.nomeModulo = nomeModulo;
    }

    public Long getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(Long idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public String getNomeDiscipina() {
        return nomeDiscipina;
    }

    public void setNomeDiscipina(String nomeDiscipina) {
        this.nomeDiscipina = nomeDiscipina;
    }

    @Override
    public int compareTo(BlocoModuloDisciplina o) {
        if (o.getNomeModulo().compareTo(this.nomeModulo) > 0) {
            return -1;
        } else if (o.getNomeModulo().compareTo(this.nomeModulo) < 0) {
            return 1;
        } else {
            if (o.getNomeDiscipina().compareTo(this.nomeDiscipina) > 0) {
                return -1;
            } else if (o.getNomeDiscipina().compareTo(this.nomeDiscipina) < 0) {
                return 1;
            }else{
                return 0;
            }
        }
    }

}
