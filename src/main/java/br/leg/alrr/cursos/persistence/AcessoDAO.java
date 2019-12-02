package br.leg.alrr.cursos.persistence;

import br.leg.alrr.cursos.model.Acesso;
import br.leg.alrr.cursos.model.Usuario;
import br.leg.alrr.cursos.util.DAOException;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe usada para fazer as operações no banco de dados para a Classe Acesso.
 * 
 * @author Heliton Nascimento
 * @since 2019-11-27
 * @version 1.0
 * @see Acesso
 */
@Stateless
public class AcessoDAO{

    @PersistenceContext
    protected EntityManager em;
    
    public void salvar(Acesso o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar acesso.", e);
        }
    }

    public void atualizar(Acesso o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar acesso.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Acesso o order by o.dataDeAcesso,o.momentoDoAcesso, o.usuario.login").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar acessos.", e);
        }
    }

    public void remover(Acesso o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover acesso.", e);
        }
    }
    
    public Acesso buscarPorID(Long id) throws DAOException{
        try {
            return em.find(Acesso.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar acesso por ID.", e);
        }
    }
    
    public List<Acesso> listarAcessoPorUsuario(Usuario u) throws DAOException{
        try {
            return em.createQuery("select o from Acesso o where o.usuario.id =:idUsuario order by o.dataDeAcesso, o.momentoDoAcesso")
                    .setParameter("idUsuario", u.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar bairro por usuário.", e);
        }
    }
    
    public Long contarAcessosPorData(LocalDate dataDeAcesso) throws DAOException{
        try {
            Long quantidade = (Long) em.createQuery("select COUNT (o) from Acesso o where o.dataDeAcesso = :dataDeAcesso")
                    .setParameter("dataDeAcesso", dataDeAcesso)
                    .getSingleResult();
            return quantidade;
        } catch (Exception e) {
            throw new DAOException("Erro ao contar acessos por data.", e);
        }
    }
    
    public List listarAcessosPorData(LocalDate dataDeAcesso) throws DAOException{
        try {
            return em.createQuery("select o from Acesso o where o.dataDeAcesso = :dataDeAcesso ORDER BY o.dataDeAcesso, o.momentoDoAcesso")
                    .setParameter("dataDeAcesso", dataDeAcesso)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar acessos por data.", e);
        }
    }
    
    public List listarAcessosPorIntervaloDeDatas(LocalDate data1, LocalDate data2) throws DAOException{
        try {
            return em.createQuery("select o from Acesso o where o.dataDeAcesso BETWEEN :data1 and :data2 ORDER BY o.usuario.login, o.dataDeAcesso, o.momentoDoAcesso")
                    .setParameter("data1", data1)
                    .setParameter("data2", data2)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar acessos por data.", e);
        }
    }
    
    public List listarAcessosPorUsuarioEIntervaloDeDatas(Usuario u, LocalDate data1, LocalDate data2) throws DAOException{
        try {
            return em.createQuery("select o from Acesso o where o.usuario .id = :idUsuario and o.dataDeAcesso BETWEEN :data1 and :data2 ORDER BY o.usuario.login, o.dataDeAcesso, o.momentoDoAcesso")
                    .setParameter("idUsuario", u.getId())
                    .setParameter("data1", data1)
                    .setParameter("data2", data2)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar acessos por data.", e);
        }
    }
    
    public Long contarAcessosPorUsuarioEIntervaloDeDatas(Usuario u, LocalDate data1, LocalDate data2) throws DAOException{
        try {
            return (Long) em.createQuery("select COUNT(o) from Acesso o where o.usuario .id = :idUsuario and o.dataDeAcesso BETWEEN :data1 and :data2")
                    .setParameter("idUsuario", u.getId())
                    .setParameter("data1", data1)
                    .setParameter("data2", data2)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar acessos por data.", e);
        }
    }

}
