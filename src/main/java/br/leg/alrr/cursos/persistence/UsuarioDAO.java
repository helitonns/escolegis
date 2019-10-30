package br.leg.alrr.cursos.persistence;

import br.leg.alrr.cursos.model.Unidade;
import br.leg.alrr.cursos.model.Usuario;
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
public class UsuarioDAO{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Usuario o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar usuário.", e);
        }
    }

    public void atualizar(Usuario o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar usuário.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Usuario o order by o.login asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários.", e);
        }
    }
    
    public List listarTodosAtivos() throws DAOException{
        try {
            return em.createQuery("select o from Usuario o where o.status = true order by o.login asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários.", e);
        }
    }
    
    public List listarTodosPorUnidade(Unidade u) throws DAOException{
        try {
            return em.createQuery("select o from Usuario o where o.unidade.id = :idUnidade order by o.login asc")
                    .setParameter("idUnidade", u.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários por unidade.", e);
        }
    }
    
    public List listarTodosPorUnidadeSemOSuperAdmin(Unidade u) throws DAOException{
        try {
            return em.createQuery("select o from Usuario o where o.unidade.id = :idUnidade and o.tipo != 'SUPER_ADMIN' order by o.login asc")
                    .setParameter("idUnidade", u.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários por unidade.", e);
        }
    }

    public void remover(Usuario o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover usuário.", e);
        }
    }

    public Usuario pesquisarPorLogin(String login) throws DAOException{
        try {
            return (Usuario) em.createQuery("select u from Usuario u where u.login =:login")
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao pesquisar usuário por login.", e);
        }
    }

    public Usuario pesquisarPorLoginESenha(String login, String senha) throws DAOException{
        try {
            return (Usuario) em.createQuery("select u from Usuario u where u.login =:login and u.senha =:senha")
                    .setParameter("login", login)
                    .setParameter("senha", senha)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao pesquisar usuário por login e senha.", e);
        }
    }
    
    public boolean haUsuarioComEsteLogin(String login) throws DAOException{
        try {
            return em.createQuery("select u from Usuario u where u.login =:login")
                    .setParameter("login", login)
                    .getResultList().size() >= 1;
        } catch (Exception e) {
            throw new DAOException("Erro ao pesquisar usuário por login.", e);
        }
    }
}
