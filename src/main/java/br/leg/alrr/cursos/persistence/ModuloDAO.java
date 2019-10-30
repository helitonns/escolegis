package br.leg.alrr.cursos.persistence;

import br.leg.alrr.cursos.model.Curso;
import br.leg.alrr.cursos.model.Modulo;
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
public class ModuloDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Modulo o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar módulo: " + e.getMessage(), e);
        }
    }

    public void atualizar(Modulo o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar módulo.", e);
        }
    }

    public Modulo buscarReferencia(Modulo o) throws DAOException {
        try {
            return em.getReference(Modulo.class, o.getId());
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar referência do módulo.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Modulo o order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar módulos.", e);
        }
    }

    public List listarModulosAtivos() throws DAOException {
        try {
            return em.createQuery("select o from Modulo o where o.status = true order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar módulos ativos.", e);
        }
    }

    public List listarModulosAtivosPorUnidade(Unidade u) throws DAOException {
        try {
            return em.createQuery("select o from Modulo o where o.status = true order by o.nome asc")
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar módulos ativos por unidade.", e);
        }
    }

    public List listarModulosAtivosPorCurso(Curso c) throws DAOException {
        try {
            return em.createQuery("select o from Modulo o where o.status = true and o.curso.id = :idCurso order by o.nome asc")
                    .setParameter("idCurso", c.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar módulos ativos por curso.", e);
        }
    }

    public void remover(Modulo o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover módulo.", e);
        }
    }

    public Modulo buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Modulo.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar módulo por ID.", e);
        }
    }

    public boolean verificarSeCursoTemModulos(Curso c) throws DAOException {
        try {
            Long quantidade = (Long) em.createQuery("select COUNT(o) from Modulo o where o.curso.id = :idCurso")
                    .setParameter("idCurso", c.getId())
                    .getSingleResult();
            return quantidade > 0;
        } catch (Exception e) {
            throw new DAOException("Erro ao verificar se há módulos para o curso.", e);
        }
    }

    public List pegarDisciplinasDoModulo(Modulo m) throws DAOException {
        try {
            return em.createQuery("select d from Modulo m inner join m.disciplinas d where m.id=:idModulo order by d.nome")
                    .setParameter("idModulo", m.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar disciplinas do módulos", e);
        }
    }
}
