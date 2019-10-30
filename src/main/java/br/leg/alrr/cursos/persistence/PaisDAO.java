package br.leg.alrr.cursos.persistence;

import br.leg.alrr.cursos.model.Pais;
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
public class PaisDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Pais o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar país.", e);
        }
    }

    public void atualizar(Pais o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualiza país.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Pais o order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar paíss.", e);
        }
    }

    public void remover(Pais o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover país.", e);
        }
    }

    public Pais buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Pais.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar país por ID.", e);
        }
    }

}
