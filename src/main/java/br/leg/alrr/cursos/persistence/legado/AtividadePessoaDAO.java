package br.leg.alrr.cursos.persistence.legado;

import br.leg.alrr.cursos.model.legado.AtividadePessoa;
import br.leg.alrr.cursos.util.DAOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Heliton
 */
@Stateless
public class AtividadePessoaDAO {
    
    @PersistenceContext
    protected EntityManager em;
    
    public void salvar(AtividadePessoa o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar Atividade Pessoa.", e);
        }
    }

    public void atualizar(AtividadePessoa o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
             throw new DAOException("Erro ao atualizar Atividade Pessoa.", e);
        }
    }
    
    public AtividadePessoa buscarReferencia(AtividadePessoa o) throws DAOException{
        try {
            return em.getReference(AtividadePessoa.class, o.getId());
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar referência de Atividade Pessoa.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from AtividadePessoa o order by id asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar Atividade Pessoa.", e);
        }
    }
    
    public void remover(AtividadePessoa o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remoevr Atividade Pessoa.", e);
        }
    }
    
    public AtividadePessoa buscarPorID(Long id) throws DAOException {
        try {
            return em.find(AtividadePessoa.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar Atividade Pessoa por ID.", e);
        }
    }
}
