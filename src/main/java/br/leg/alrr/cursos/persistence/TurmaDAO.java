package br.leg.alrr.cursos.persistence;

import br.leg.alrr.cursos.model.Aluno;
import br.leg.alrr.cursos.model.Curso;
import br.leg.alrr.cursos.model.Modulo;
import br.leg.alrr.cursos.model.Turma;
import br.leg.alrr.cursos.util.DAOException;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author heliton
 */
@Stateless
public class TurmaDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Turma o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar turma: " + e.getMessage(), e);
        }
    }

    public void atualizar(Turma o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar turma.", e);
        }
    }

    public Turma buscarReferencia(Turma o) throws DAOException {
        try {
            return em.getReference(Turma.class, o.getId());
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar referência do turma.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Turma o order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar turmas.", e);
        }
    }

    public List listarTurmasAtivas() throws DAOException {
        try {
            return em.createQuery("select o from Turma o where o.status = true order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar turmas ativas.", e);
        }
    }

    public List listarTurmasIniciadas() throws DAOException {
        try {
            return em.createQuery("select o from Turma o where o.iniciada = true order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar turmas ativas.", e);
        }
    }

    public List listarTurmasAtivosPorCurso(Curso c) throws DAOException {
        try {
            return em.createQuery("select o from Turma o where o.status = true and o.modulo.curso.id = :idCurso order by o.nome asc")
                    .setParameter("idCurso", c.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar turmas ativas por curso.", e);
        }
    }

    public List listarTurmasIniciadasPorCurso(Curso c) throws DAOException {
        try {
            return em.createQuery("select o from Turma o where o.iniciada = true and o.modulo.curso.id = :idCurso order by o.nome asc")
                    .setParameter("idCurso", c.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar turmas ativas por curso.", e);
        }
    }

    public List listarTurmasIniciadasPorAluno(Aluno a) throws DAOException {
        try {
            return em.createQuery("select o from Turma o JOIN o.alunos a where o.iniciada = true AND a.id = :idAluno order by o.nome asc")
                    .setParameter("idAluno", a.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar turmas em andamento por aluno.", e);
        }
    }

    public List listarTurmasConcluidasPorCurso(Curso c) throws DAOException {
        try {
            return em.createQuery("select o from Turma o where o.iniciada = false and o.modulo.curso.id = :idCurso order by o.nome asc")
                    .setParameter("idCurso", c.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar turmas ativas por curso.", e);
        }
    }

    public List listarTurmasPorAluno(Aluno a) throws DAOException {
        try {
            return em.createQuery("select o from Turma o JOIN o.alunos a where a.id = :idAluno order by o.nome asc")
                    .setParameter("idAluno", a.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar turmas por aluno.", e);
        }
    }

    public void remover(Turma o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover turma.", e);
        }
    }

    public Turma buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Turma.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar turma por ID.", e);
        }
    }

    public boolean turmaAlcancouSeuTermoFinal(Turma t) throws DAOException {
        try {
            Turma turma = buscarPorID(t.getId());
            GregorianCalendar gc = new GregorianCalendar();
            gc.add(GregorianCalendar.DAY_OF_MONTH, -1);
            return gc.getTime().after(turma.getDataTermino());
        } catch (DAOException e) {
            throw new DAOException("Erro ao verificar se o curso alcançouo o seu termo final.", e);
        }
    }
    
     public boolean verificarSeAlunoJaEstaMatriculadoNaTurma(Aluno a, Turma t) throws DAOException {
        try {
            List l = em.createQuery("select o from Turma o JOIN o.alunos a where a.id = :idAluno and o.id = :idTurma order by o.nome asc")
                    .setParameter("idAluno", a.getId())
                    .setParameter("idTurma", t.getId())
                    .getResultList();
            return l.size() > 0;
        } catch (Exception e) {
            throw new DAOException("Erro ao listar turmas por aluno.", e);
        }
    }
     
     public boolean verificarSeModuloTemTurma(Modulo m) throws DAOException {
        try {
            Long quantidade = (Long) em.createQuery("select COUNT(o) from Turma o where o.modulo.id = :idModulo")
                    .setParameter("idModulo", m.getId())
                    .getSingleResult();
            return quantidade > 0;
        } catch (Exception e) {
            throw new DAOException("Erro ao verificar se há turmas para o módulo.", e);
        }
    }

}
