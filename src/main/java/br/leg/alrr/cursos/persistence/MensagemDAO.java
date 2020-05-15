package br.leg.alrr.cursos.persistence;

import br.leg.alrr.cursos.model.Mensagem;
import br.leg.alrr.cursos.util.DAOException;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe que gerencia a persistência da entidade Mensagem.
 *
 * @author Heliton Nascimento
 * @since 2020-01-22
 * @version 1.0
 * @see Mensagem
 */
@Stateless
public class MensagemDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Mensagem o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar mensagem.", e);
        }
    }

    public void atualizar(Mensagem o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar mensagem.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Mensagem o ORDER BY o.id desc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar mensagem.", e);
        }
    }
    
    public List listarTodasAsMensagensAtivas() throws DAOException {
        try {
            return em.createQuery("select o from Mensagem o where o.status = TRUE").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar mensagens.", e);
        }
    }
    
    public List listarTodasAsMensagensAtivasParaAData(LocalDate d) throws DAOException {
        try {
//            return em.createQuery("select o from Mensagem o where o.status = TRUE and o.dataDeInicio >= :dataInicio and o.dataDeFim <= :dataFim ORDER BY o.id")
            return em.createQuery("select o from Mensagem o where o.status = TRUE and :data between o.dataDeInicio and o.dataDeFim ORDER BY o.id")
                    .setParameter("data", d)
//                    .setParameter("dataFim", d)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar mensagens.", e);
        }
    }
    
    public void remover(Mensagem o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover mensagem.", e);
        }
    }
}
