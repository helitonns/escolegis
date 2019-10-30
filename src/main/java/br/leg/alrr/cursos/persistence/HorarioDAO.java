package br.leg.alrr.cursos.persistence;

import br.leg.alrr.cursos.model.Horario;
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
public class HorarioDAO{

    @PersistenceContext
    protected EntityManager em;
    
    public void salvar(Horario o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
             throw new DAOException("Erro ao salvar horário.", e);
        }
    }

    public void atualizar(Horario o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar horário.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Horario o order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar horários.", e);
        }
    }
    
    public List listarAtivos() throws DAOException{
        try {
            return em.createQuery("select o from Horario o where o.status = true order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar horários ativos.", e);
        }
    }

    public void remover(Horario o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover horário.", e);
        }
    }

    public Horario buscarPorID(Long id) throws DAOException{
        try {
            return em.find(Horario.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar horário por ID.", e);
        }
    }
    
    public boolean horarioNaoCadastrado(String horario) throws DAOException{
        try {
            List l =  em.createQuery("select o from Horario o where o.descricao = :descricao")
                    .setParameter("descricao", horario )
                    .getResultList();
            return l.size() <= 0;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DAOException("Erro ao verificar se a disciplina já foi cadastrada.", e);
        }
    }
}
