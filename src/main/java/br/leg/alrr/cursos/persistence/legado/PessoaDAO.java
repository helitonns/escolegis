package br.leg.alrr.cursos.persistence.legado;

import br.leg.alrr.cursos.model.legado.Pessoa;
import br.leg.alrr.cursos.util.DAOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Heliton
 */
@Stateless
public class PessoaDAO {
    
    @PersistenceContext
    protected EntityManager em;

    public void salvar(Pessoa o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar pessoa.", e);
        }
    }

    public void atualizar(Pessoa o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar pessoa.", e);
        }
    }

    public Pessoa buscarReferencia(Pessoa o) throws DAOException {
        try {
            return em.getReference(Pessoa.class, o.getId());
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar referência de pessoa.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Pessoa o order by nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar pessoa.", e);
        }
    }

    public void remover(Pessoa o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remoevr pessoa.", e);
        }
    }

    public Pessoa buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Pessoa.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar pessoa por ID.", e);
        }
    }
    
    public List pesquisarPorCPF(String cpf) throws DAOException{
        try {
            return  em.createQuery("select o from Pessoa o where o.cpf =:cpf order by o.nome")
                    .setParameter("cpf", cpf)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar aluno por CPF.", e);
        }
    }
    
    public List pesquisarPorNome(String nome) throws DAOException{
        try {
            return em.createQuery("select o from Pessoa o where o.nome LIKE :nome order by o.nome")
                    .setParameter("nome", "%" + nome + "%")
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar pessoa por nome.", e);
        }
    }
    
    public List listarAlunosJaMatriculadosNoCurso(Long idAtividade, Long idPeriodo, Long idDias, Long idHorario) throws DAOException{
        try {
            return em.createQuery(
                    "select p from Pessoa p, "
                    + "AtividadePessoa ap "
                    + "where p.id = ap.idPessoa "
                    + "and ap.idAtividade = :idAtividade "
                    + "and ap.idPeriodo = :idPeriodo "
                    + "and ap.idDias = :idDias "
                    + "and ap.idHorario = :idHorario "
                    + "order by p.nome")
                    .setParameter("idAtividade", idAtividade)
                    .setParameter("idPeriodo", idPeriodo)
                    .setParameter("idDias", idDias)
                    .setParameter("idHorario", idHorario)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar alunos já matriculados no curso.", e);
        }
    }
    
    public List pesquisarALunosMatriculados(Long idAtividade, Long idPeriodo, Long idDias, Long idHorario) throws DAOException{
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("select p from Pessoa p, AtividadePessoa ap where p.id = ap.idPessoa ");
            
            if (idAtividade != 0) {
                sb.append("and ap.idAtividade = :idAtividade ");
            }
            
            if (idPeriodo != 0) {
                sb.append("and ap.idPeriodo = :idPeriodo ");
            }
            
            if (idDias != 0) {
                sb.append("and ap.idDias = :idDias ");
            }
            
            if (idHorario != 0) {
                sb.append("and ap.idHorario = :idHorario ");
            }
            
            sb.append("order by p.nome");
            
            Query q = em.createQuery(sb.toString());
            
            if (idAtividade != 0) {
                q.setParameter("idAtividade", idAtividade);
            }
            
            if (idPeriodo != 0) {
                q.setParameter("idPeriodo", idPeriodo);
            }
            
            if (idDias != 0) {
                q.setParameter("idDias", idDias);
            }
            
            if (idHorario != 0) {
                q.setParameter("idHorario", idHorario);
            }
            
            
            return q.getResultList();
            
        } catch (Exception e) {
            throw new DAOException("Erro ao listar alunos matriculados.", e);
        }
    }
}