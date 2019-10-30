package br.leg.alrr.cursos.persistence.legado;

import br.leg.alrr.cursos.model.legado.Atividade;
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
public class AtividadeDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Atividade o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar atividade.", e);
        }
    }

    public void atualizar(Atividade o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar atividade.", e);
        }
    }

    public Atividade buscarReferencia(Atividade o) throws DAOException {
        try {
            return em.getReference(Atividade.class, o.getId());
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar referência de atividade.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Atividade o order by descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar atividade.", e);
        }
    }

    public void remover(Atividade o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remoevr atividade.", e);
        }
    }

    public Atividade buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Atividade.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar atividade por ID.", e);
        }
    }
}
