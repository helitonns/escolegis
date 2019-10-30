package br.leg.alrr.cursos.persistence;

import br.leg.alrr.cursos.model.Bairro;
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
public class BairroDAO{

    @PersistenceContext
    protected EntityManager em;
    
    public void salvar(Bairro o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar bairro.", e);
        }
    }

    public void atualizar(Bairro o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar bairro.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Bairro o order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar bairros.", e);
        }
    }

    public void remover(Bairro o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover bairro.", e);
        }
    }
    
    public Bairro buscarPorID(Long id) throws DAOException{
        try {
            return em.find(Bairro.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar bairro por ID.", e);
        }
    }
    
    public List<Bairro> listarBairroPorMunicipio(Municipio m) throws DAOException{
        try {
            return em.createQuery("select o from Bairro o where o.municipio =:m order by o.nome asc")
                    .setParameter("m", m)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar bairro por município.", e);
        }
    }

}
