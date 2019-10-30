package br.leg.alrr.cursos.controller.legado;

import br.leg.alrr.cursos.model.legado.Pessoa;
import br.leg.alrr.cursos.persistence.legado.PessoaDAO;
import br.leg.alrr.cursos.util.DAOException;
import br.leg.alrr.cursos.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Heliton
 */
@ManagedBean
@ViewScoped
public class AlunoListagemLegadoMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private PessoaDAO pessoaDAO;
    
    
    private ArrayList<Pessoa> pessoas;
    
    private Pessoa pessoa;
    private boolean removerAluno;
    
    
//==============================================================================
    @PostConstruct
    public void init() {
        limparForm();
    }

    public void pesquisarAluno() {
        try {
            pessoas = new ArrayList<>();

            if (pessoa.getCpf() != null && !pessoa.getCpf().isEmpty()) {
                pessoas = (ArrayList<Pessoa>) pessoaDAO.pesquisarPorCPF(pessoa.getCpf());
            } else if (pessoa.getNome() != null && !pessoa.getNome().isEmpty()) {
                pessoas = (ArrayList<Pessoa>) pessoaDAO.pesquisarPorNome(pessoa.getNome());
            } else {
//                FacesMessages.warning("Indique o CPF ou o nome do aluno!");
            }
        } catch (DAOException e) {
//            FacesMessages.error(e.getMessage()+": "+e.getCause());
        }
            pessoa = new Pessoa();
    }
    
    public void removerAluno() {
        try {
            if (removerAluno) {
                pessoaDAO.remover(pessoa);
//                FacesMessages.info("Aluno removido com sucesso!");
            }
        } catch (DAOException e) {
//            FacesMessages.error(e.getMessage());
        }
        limparForm();
    }

    
    public String editarAluno(){
        FacesUtils.setBean("aluno", pessoa);
        return "aluno-legado.xhtml" + "?faces-redirect=true";
    }
    
    public String exibirAluno() {
        FacesUtils.setBean("aluno", pessoa);
        return "exibir-aluno-legado.xhtml" + "?faces-redirect=true";
    }
    
    private void limparForm(){
        removerAluno = false;
        pessoa = new Pessoa();
        pessoas = new ArrayList<>();
    }
    
//==============================================================================

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public ArrayList<Pessoa> getPessoas() {
        return pessoas;
    }

    public boolean isRemoverAluno() {
        return removerAluno;
    }

    public void setRemoverAluno(boolean removerAluno) {
        this.removerAluno = removerAluno;
    }

}
