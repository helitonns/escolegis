package br.leg.alrr.cursos.persistence.legado;

import br.leg.alrr.cursos.model.legado.Dias;
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
public class DiasDAO {
    
    @PersistenceContext
    protected EntityManager em;

    public void salvar(Dias o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar dias.", e);
        }
    }

    public void atualizar(Dias o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar dias.", e);
        }
    }

    public Dias buscarReferencia(Dias o) throws DAOException {
        try {
            return em.getReference(Dias.class, o.getId());
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar referência de dias.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Dias o order by descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar dias.", e);
        }
    }

    public void remover(Dias o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remoevr dias.", e);
        }
    }

    public Dias buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Dias.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar dias por ID.", e);
        }
    }
}