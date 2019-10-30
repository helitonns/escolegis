package br.leg.alrr.cursos.persistence;

import br.leg.alrr.cursos.model.GrupoDisciplina;
import br.leg.alrr.cursos.model.Unidade;
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
public class GrupoDisciplinaDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(GrupoDisciplina o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar grupo de disciplinas.", e);
        }
    }

    public void atualizar(GrupoDisciplina o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar grupo de disciplinas.", e);
        }
    }

    public GrupoDisciplina buscarReferencia(GrupoDisciplina o) throws DAOException {
        try {
            return em.getReference(GrupoDisciplina.class, o.getId());
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar referência de grupo de disciplinas.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from GrupoDisciplina o order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar grupo de disciplinas.", e);
        }
    }

    public List listarGrupoDisciplinaAtivos() throws DAOException {
        try {
            return em.createQuery("select o from GrupoDisciplina o where o.status = true order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar grupo de disciplinas ativos.", e);
        }
    }

    public List listarGrupoDisciplinaAtivosPorUnidade(Unidade u) throws DAOException {
        try {
            return em.createQuery("select o from GrupoDisciplina o where o.status = true and o.unidade.id = :idUnidade order by o.nome asc")
                    .setParameter("idUnidade", u.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar grupo de disciplinas ativos por unidade.", e);
        }
    }

    public void remover(GrupoDisciplina o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover grupo de disciplina.", e);
        }
    }

    public GrupoDisciplina buscarPorID(Long id) throws DAOException {
        try {
            return em.find(GrupoDisciplina.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar grupo de diciplina por ID.", e);
        }
    }
    
    public boolean grupoDisciplinaNaoCadastrado(String grupoDisciplina) throws DAOException{
        try {
            List l =  em.createQuery("select o from GrupoDisciplina o where o.nome = :grupoDisciplina")
                    .setParameter("grupoDisciplina", grupoDisciplina )
                    .getResultList();
            return l.size() <= 0;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DAOException("Erro ao verificar se a disciplina já foi cadastrada.", e);
        }
    }

}
