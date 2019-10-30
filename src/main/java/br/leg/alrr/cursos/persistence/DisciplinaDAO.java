package br.leg.alrr.cursos.persistence;

import br.leg.alrr.cursos.model.Disciplina;
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
public class DisciplinaDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Disciplina o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar disciplina.", e);
        }
    }

    public void atualizar(Disciplina o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar disciplina.", e);
        }
    }

    public Disciplina buscarReferencia(Disciplina o) throws DAOException {
        try {
            return em.getReference(Disciplina.class, o.getId());
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar referência de disciplina.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Disciplina o order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar disciplina.", e);
        }
    }

    public List listarDisciplinasAtivas() throws DAOException {
        try {
            return em.createQuery("select o from Disciplina o where o.status = true order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar disciplina ativas", e);
        }
    }

    public List listarDisciplinasAtivasPorUnidade(Unidade u) throws DAOException {
        try {
            return em.createQuery("select o from Disciplina o where o.status = true and o.unidade.id = :idUnidade order by o.nome asc")
                    .setParameter("idUnidade", u.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar disciplina ativas por unidade.", e);
        }
    }

    public List listarDisciplinasAtivasPorGrupoDisciplia(GrupoDisciplina gd) throws DAOException {
        try {
            System.out.println("Está no DAO: " + gd.getId());
            return em.createQuery("select o from Disciplina o where o.status = true and o.grupoDisciplina.id = :idGrupoDisciplia order by o.nome asc")
                    .setParameter("idGrupoDisciplia", gd.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar disciplina ativsas por grupo disciplina.", e);
        }
    }

    public List listarDisciplinasAtivasPorGrupoDisciplia(Long gd) throws DAOException {
        try {
            return em.createQuery("select o from Disciplina o where o.status = true and o.grupoDisciplina.id = :idGrupoDisciplia order by o.nome asc")
                    .setParameter("idGrupoDisciplia", gd)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar disciplina ativsas por grupo disciplina.", e);
        }
    }

    public void remover(Disciplina o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remoevr disciplina.", e);
        }
    }

    public Disciplina buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Disciplina.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar disciplina por ID.", e);
        }
    }

    public boolean disciplinaNaoCadastrada(String disciplina) throws DAOException{
        try {
            List l =  em.createQuery("select o from Disciplina o where o.nome = :disciplina")
                    .setParameter("disciplina", disciplina )
                    .getResultList();
            return l.size() <= 0;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DAOException("Erro ao verificar se a disciplina já foi cadastrada.", e);
        }
    }
}
