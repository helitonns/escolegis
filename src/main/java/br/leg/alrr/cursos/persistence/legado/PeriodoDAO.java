package br.leg.alrr.cursos.persistence.legado;

import br.leg.alrr.cursos.model.legado.Periodo;
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
public class PeriodoDAO {
    
    @PersistenceContext
    protected EntityManager em;

    public void salvar(Periodo o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar periodo.", e);
        }
    }

    public void atualizar(Periodo o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar periodo.", e);
        }
    }

    public Periodo buscarReferencia(Periodo o) throws DAOException {
        try {
            return em.getReference(Periodo.class, o.getId());
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar referência de periodo.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Periodo o order by dataDeInicio DESC, dataDeTermino ASC").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar periodo.", e);
        }
    }
    
    public List listarTodosAtivos() throws DAOException {
        try {
            return em.createQuery("select o from Periodo o where o.status = true order by dataDeInicio DESC, dataDeTermino ASC").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar periodo.", e);
        }
    }

    public void remover(Periodo o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remoevr periodo.", e);
        }
    }

    public Periodo buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Periodo.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar periodo por ID.", e);
        }
    }
}
