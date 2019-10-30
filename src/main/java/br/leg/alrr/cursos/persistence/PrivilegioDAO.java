package br.leg.alrr.cursos.persistence;

import br.leg.alrr.cursos.model.Privilegio;
import br.leg.alrr.cursos.model.Sistema;
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
public class PrivilegioDAO{

    @PersistenceContext
    protected EntityManager em;
    
    public void salvar(Privilegio o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar permissao.", e);
        }
    }

    public void atualizar(Privilegio o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar permissao.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Privilegio o order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar permissôes.", e);
        }
    }
    
    public List listarTodosPelaChaveDoSistema(String chave) throws DAOException{
        try {
            return em.createQuery("select o from Privilegio o where o.sistema.chave =:chave order by o.descricao asc")
                    .setParameter("chave", chave)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar permissôes.", e);
        }
    }
    
    public List listarTodosAtivosPelaChaveDoSistema(String chave) throws DAOException{
        try {
            return em.createQuery("select o from Privilegio o where o.status = true and o.sistema.chave =:chave order by o.descricao asc")
                    .setParameter("chave", chave)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar permissôes.", e);
        }
    }
    
    public List listarTodosAtivosPeloSistema(Sistema s) throws DAOException{
        try {
            return em.createQuery("select o from Privilegio o where o.status = true and o.sistema.id =:idSistema order by o.descricao asc")
                    .setParameter("idSistema", s.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar permissôes.", e);
        }
    }
    
    public List listarTodosAtivos() throws DAOException{
        try {
            return em.createQuery("select o from Privilegio o where o.status = true order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar permissoes.", e);
        }
    }

    public void remover(Privilegio o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover permissao.", e);
        }
    }
    
    public Privilegio buscarPorID(Long id) throws DAOException{
        try {
            return em.find(Privilegio.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar permissao por ID.", e);
        }
    }
    
    public List<Privilegio> listarPrivilegioPorSistema(Sistema s) throws DAOException{
        try {
            return em.createQuery("select o from Privilegio o where o.sistema =:s order by o.descricao asc")
                    .setParameter("s", s)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar permissao por município.", e);
        }
    }

    public boolean haPrivilegioComMesmoNomeESistema(Privilegio p) throws DAOException{
        try {
            List l = em.createQuery("select o from Privilegio o where o.descricao =:descricao and o.sistema.id =:idSistema")
                    .setParameter("descricao", p.getDescricao())
                    .setParameter("idSistema", p.getSistema().getId())
                    .getResultList();
            return l.size() > 0;
        } catch (Exception e) {
            throw new DAOException("Erro ao verificar se há privilégio com mesmo nome e sistema.", e);
        }
    }
}
