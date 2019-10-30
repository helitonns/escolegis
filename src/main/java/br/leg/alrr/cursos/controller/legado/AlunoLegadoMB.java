package br.leg.alrr.cursos.controller.legado;

import br.leg.alrr.cursos.business.Sexo;
import br.leg.alrr.cursos.model.Bairro;
import br.leg.alrr.cursos.model.Municipio;
import br.leg.alrr.cursos.model.legado.Pessoa;
import br.leg.alrr.cursos.persistence.BairroDAO;
import br.leg.alrr.cursos.persistence.MunicipioDAO;
import br.leg.alrr.cursos.persistence.legado.PessoaDAO;
import br.leg.alrr.cursos.util.DAOException;
import br.leg.alrr.cursos.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author heliton
 */
@ManagedBean
@ViewScoped
public class AlunoLegadoMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private PessoaDAO pessoaDAO;

    @EJB
    private MunicipioDAO municipioDAO;

    @EJB
    private BairroDAO bairroDAO;

    private ArrayList<Municipio> municipios;
    private ArrayList<Bairro> bairros;

    private Pessoa pessoa;
    private Municipio municipio;
    private Bairro bairro;
    private Sexo sexo;

    private Long idMunicipio;

//==============================================================================
    @PostConstruct
    public void init() {
        limparForm();
        listarMunicipio();
        
        // VERIFICA SE HÁ ALGUM ALUNO NA SESSÃO PARA SER EDITADO, SE HOUVER SETA OS VALORES CORRESPONDENTES
        try {
            if (FacesUtils.getBean("aluno") != null) {
                pessoa = (Pessoa) FacesUtils.getBean("aluno");
                
                for (Municipio m : municipios) {
                    if(m.getNome().equals(pessoa.getEndereco_cidade())){
                        idMunicipio = m.getId();
                    }
                }
                municipio.setId(idMunicipio);
                listarBairroPorMunicipio();
                FacesUtils.removeBean("aluno");
            }
        } catch (Exception e) {
//            FacesMessages.error("Erro ao tentar editar aluno.");
        }
    }

    private void limparForm() {
        pessoa = new Pessoa();
        bairro = new Bairro();
        municipio = new Municipio();
        sexo = Sexo.MASCULINO;
        idMunicipio = 0l;
    }

    private void listarMunicipio() {
        try {
            municipios = (ArrayList<Municipio>) municipioDAO.listarTodos();
        } catch (DAOException e) {
//            FacesMessages.error(e.getMessage());
        }
    }

    private void listarBairroPorMunicipio() {
        try {
            bairros = (ArrayList<Bairro>) bairroDAO.listarBairroPorMunicipio(municipio);
        } catch (DAOException e) {
//            FacesMessages.error(e.getMessage());
        }
    }

    public void valueChanged(ValueChangeEvent event) {
        try {
            idMunicipio = Long.parseLong(event.getNewValue().toString());
            municipio.setId(idMunicipio);
            listarBairroPorMunicipio();
        } catch (NumberFormatException e) {
//            FacesMessages.error(e.getMessage());
        }
    }

    public void salvarAluno() {
        try {
            if (pessoa.getId() != null) {
                pessoaDAO.atualizar(pessoa);
//                FacesMessages.info("Aluno atualizado com sucesso!!!");
            } else {
                //setando o nome do municipio em pessoa
                for (Municipio m : municipios) {
                    if (m.getId() == idMunicipio) {
                        pessoa.setEndereco_cidade(m.getNome());
                        break;
                    }
                }
                pessoaDAO.salvar(pessoa);
//                FacesMessages.info("Aluno salvo com sucesso!!!");
            }
        } catch (DAOException e) {
//            FacesMessages.error(e.getMessage() + ": " + e.getCause());
        }
        limparForm();
    }

    public void salvarBairro() {
        try {
            bairro.setMunicipio(municipio);
            bairroDAO.salvar(bairro);
            cancelarBairro();
        } catch (DAOException e) {
//            FacesMessages.error(e.getMessage());
        }
    }

    public void cancelarBairro() {
        municipio = new Municipio();
        bairro = new Bairro();
    }

    public String cancelar() {
        return "aluno-legado.xhtml" + "?faces-redirect=true";
    }
//==============================================================================

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public ArrayList<Municipio> getMunicipios() {
        return municipios;
    }

    public ArrayList<Bairro> getBairros() {
        return bairros;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Long getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Long idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

}
