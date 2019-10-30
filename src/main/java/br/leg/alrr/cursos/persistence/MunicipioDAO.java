package br.leg.alrr.cursos.persistence;

import br.leg.alrr.cursos.model.Municipio;
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
public class MunicipioDAO{

    @PersistenceContext
    protected EntityManager em;
    
    public void salvar(Municipio o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar município.", e);
        }
    }

    public void atualizar(Municipio o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualiza município.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Municipio o order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar municípios.", e);
        }
    }

    public void remover(Municipio o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover município.", e);
        }
    }

    public Municipio buscarPorID(Long id) throws DAOException{
        try {
            return em.find(Municipio.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar município por ID.", e);
        }
    }
}
