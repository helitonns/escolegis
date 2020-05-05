package br.leg.alrr.cursos.persistence;

import br.leg.alrr.cursos.model.LogSistema;
import br.leg.alrr.cursos.model.Usuario;
import br.leg.alrr.cursos.util.DAOException;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe usada para fazer as operações no banco de dados para a Classe LogSistema.
 * 
 * @author Heliton Nascimento
 * @since 2019-11-27
 * @version 1.0
 * @see LogSistema
 */
@Stateless
public class LogSistemaDAO{

    @PersistenceContext
    protected EntityManager em;
    
    public void salvar(LogSistema o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar log.", e);
        }
    }

    public void atualizar(LogSistema o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar log.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from LogSistema o order by o.dataOperacao, o.usuario.login").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar logs.", e);
        }
    }

    public void remover(LogSistema o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover log.", e);
        }
    }
    
    public LogSistema buscarPorID(Long id) throws DAOException{
        try {
            return em.find(LogSistema.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar log por ID.", e);
        }
    }
    
    public List<LogSistema> listarLogPorUsuario(Usuario u) throws DAOException{
        try {
            return em.createQuery("select o from LogSistema o where o.usuario.id =:idUsuario order by o.dataOperacao")
                    .setParameter("idUsuario", u.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar Log por usuário.", e);
        }
    }
    
    public Long contarLogsPorData(LocalDate dataLog) throws DAOException{
        try {
            Long quantidade = (Long) em.createQuery("select COUNT (o) from LogSistema o where o.dataOperacao = :dataOperacao and o.dataOperacao ")
                    .setParameter("dataOperacao", dataLog)
                    .getSingleResult();
            return quantidade;
        } catch (Exception e) {
            throw new DAOException("Erro ao contar acessos por data.", e);
        }
    }
    
    public List listarLogsPorData(LocalDate dataDeLog) throws DAOException{
        try {
            return em.createQuery("select o from LogSistema o where o.dataOperacao = :dataOperacao ORDER BY o.dataDeLog, o.momentoDoLog")
                    .setParameter("dataDeLog", dataDeLog)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar acessos por data.", e);
        }
    }
    
    public List listarLogsPorIntervaloDeDatas(LocalDate data1, LocalDate data2) throws DAOException{
        try {
            return em.createQuery("select o from LogSistema o where o.dataDeLog BETWEEN :data1 and :data2 ORDER BY o.usuario.login, o.dataDeLog, o.momentoDoLog")
                    .setParameter("data1", data1)
                    .setParameter("data2", data2)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar acessos por data.", e);
        }
    }
    
    public List listarLogsPorUsuarioEIntervaloDeDatas(Usuario u, LocalDate data1, LocalDate data2) throws DAOException{
        try {
            return em.createQuery("select o from LogSistema o where o.usuario .id = :idUsuario and o.dataDeLog BETWEEN :data1 and :data2 ORDER BY o.usuario.login, o.dataDeLog, o.momentoDoLog")
                    .setParameter("idUsuario", u.getId())
                    .setParameter("data1", data1)
                    .setParameter("data2", data2)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar acessos por data.", e);
        }
    }
    
    public Long contarLogsPorUsuarioEIntervaloDeDatas(Usuario u, LocalDate data1, LocalDate data2) throws DAOException{
        try {
            return (Long) em.createQuery("select COUNT(o) from LogSistema o where o.usuario .id = :idUsuario and o.dataDeLog BETWEEN :data1 and :data2")
                    .setParameter("idUsuario", u.getId())
                    .setParameter("data1", data1)
                    .setParameter("data2", data2)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar acessos por data.", e);
        }
    }

}
