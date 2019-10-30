package br.leg.alrr.cursos.persistence;

import br.leg.alrr.cursos.model.Diretor;
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
public class DiretorDAO{

    @PersistenceContext
    protected EntityManager em;
    
    public void salvar(Diretor o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar diretor.", e);
        }
    }

    public void atualizar(Diretor o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
             throw new DAOException("Erro ao atualizar diretor.", e);
        }
    }
    
    public Diretor buscarReferencia(Diretor o) throws DAOException{
        try {
            return em.getReference(Diretor.class, o.getId());
        } catch (Exception e) {
             throw new DAOException("Erro ao buscar referência de diretor.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Diretor o order by o.nome asc").getResultList();
        } catch (Exception e) {
             throw new DAOException("Erro ao listar diretor.", e);
        }
    }
    
    public List listarDiretoresAtivos() throws DAOException {
        try {
            return em.createQuery("select o from Diretor o where o.status = true order by o.id DESC").getResultList();
        } catch (Exception e) {
             throw new DAOException("Erro ao listar diretor ativos.", e);
        }
    }

    public void remover(Diretor o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
             throw new DAOException("Erro ao remover disciplina.", e);
        }
    }
    
    public Diretor buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Diretor.class, id);
        } catch (Exception e) {
             throw new DAOException("Erro ao buscar diretor por ID.", e);
        }
    }
    
    }
