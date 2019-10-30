package br.leg.alrr.cursos.persistence;

import br.leg.alrr.cursos.model.Sistema;
import br.leg.alrr.cursos.util.DAOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author heliton
 */
@Stateless
public class SistemaDAO{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Sistema o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar sistema.", e);
        }
    }

    public void atualizar(Sistema o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar sistema.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Sistema o order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar sistemas.", e);
        }
    }
    
    public List listarTodosAtivos() throws DAOException{
        try {
            return em.createQuery("select o from Sistema o where o.status = true order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar sistemas.", e);
        }
    }
    
    public void remover(Sistema o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover sistema.", e);
        }
    }
}
