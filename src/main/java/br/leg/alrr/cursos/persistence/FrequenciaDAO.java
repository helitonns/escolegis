package br.leg.alrr.cursos.persistence;

import br.leg.alrr.cursos.model.Aluno;
import br.leg.alrr.cursos.model.Frequencia;
import br.leg.alrr.cursos.model.Turma;
import br.leg.alrr.cursos.util.DAOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author heliton
 */
@Stateless
public class FrequenciaDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Frequencia o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar frequência.", e);
        }
    }

    public void atualizar(Frequencia o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar frequência.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Frequencia o order by o.aluno.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar frequências.", e);
        }
    }

    public List listarAlunosFaltosos(Turma t, Date dataDaFalta) throws DAOException {
        try {
            return em.createQuery("SELECT o FROM Frequencia o WHERE o.turma.id =:idTurma and o.presenca = FALSE AND o.faltaJustificada = false AND o.dataFrequencia = :dataDaFalta order by o.aluno.nome asc")
                    .setParameter("idTurma", t.getId())
                    .setParameter("dataDaFalta", dataDaFalta)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar alunos faltosos.", e);
        }
    }

    public void remover(Frequencia o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover frequência.", e);
        }
    }

    public Frequencia buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Frequencia.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar frequência por ID.", e);
        }
    }

    public Long contarFrequenciaDaTurma(Turma turma) throws DAOException {
        try {
            return (Long) em.createQuery("SELECT COUNT(DISTINCT o.dataFrequencia) FROM Frequencia o WHERE o.turma.id = :idTurma")
                    .setParameter("idTurma", turma.getId())
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao contar frequência da turma.", e);
        }
    }

    public Long contarFrequenciaDoAlunoPorTurma(Aluno aluno, Turma turma) throws DAOException {
        try {
            return (Long) em.createQuery("SELECT COUNT(o) FROM Frequencia o WHERE o.aluno.id = :idAluno AND o.turma.id = :idTurma AND (o.presenca = true OR o.faltaJustificada = true)")
                    .setParameter("idAluno", aluno.getId())
                    .setParameter("idTurma", turma.getId())
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao contar frequência da turma.", e);
        }
    }

    public List listarAulasDaTurma(Turma turma) throws DAOException {
        try {
            List<Frequencia> lista = em.createQuery("SELECT o FROM Frequencia o WHERE o.turma.id = :idTurma order by o.dataFrequencia asc")
                    .setParameter("idTurma", turma.getId())
                    .getResultList();
            List<Frequencia> aulasDaTurma = new ArrayList();

            for (Frequencia f : lista) {
                boolean jaEstaNaLista = false;
                for (Frequencia ff : aulasDaTurma) {
                    if (f.getDataFrequencia().equals(ff.getDataFrequencia())) {
                        jaEstaNaLista = true;
                        break;
                    }
                }
                if (!jaEstaNaLista) {
                    aulasDaTurma.add(f);
                }
            }
            return aulasDaTurma;
        } catch (Exception e) {
            throw new DAOException("Erro ao listar aulas da turma!", e);
        }
    }

    public List listarFrequenciaDaAula(Frequencia frequencia) throws DAOException {
        try {
            return em.createQuery("SELECT o FROM Frequencia o WHERE o.dataFrequencia = :dataFrequencia and o.turma.id = :idTurma order by o.aluno.nome asc")
                    .setParameter("dataFrequencia", frequencia.getDataFrequencia())
                    .setParameter("idTurma", frequencia.getTurma().getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar frequência da aula.", e);
        }
    }

    public List listarFrequenciaDaAula(Turma t, Date dataAula) throws DAOException {
        try {
            return em.createQuery("SELECT o FROM Frequencia o WHERE o.dataFrequencia = :dataFrequencia and o.turma.id = :idTurma order by o.aluno.nome asc")
                    .setParameter("dataFrequencia", dataAula)
                    .setParameter("idTurma", t.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar frequência da aula.", e);
        }
    }
    
    public boolean verificarSeAFrequenciaJaFoiLancada(Frequencia f) throws DAOException {
        try {
            List l = em.createQuery("SELECT o FROM Frequencia o WHERE o.dataFrequencia = :dataFrequencia and o.turma.id = :idTurma and o.aluno.id = :idAluno order by o.aluno.nome asc")
                    .setParameter("dataFrequencia", f.getDataFrequencia())
                    .setParameter("idTurma", f.getTurma().getId())
                    .setParameter("idAluno", f.getAluno().getId())
                    .getResultList();
            return l.size() >= 1;
        } catch (Exception e) {
            throw new DAOException("Erro ao listar frequência da aula.", e);
        }
    }

    public Long contarFaltaJustifcadasDoAlunoPorTurma(Aluno aluno, Turma turma) throws DAOException {
        try {
            return (Long) em.createQuery("SELECT COUNT(o) FROM Frequencia o WHERE o.aluno.id = :idAluno AND o.turma.id = :idTurma AND o.faltaJustificada = true")
                    .setParameter("idAluno", aluno.getId())
                    .setParameter("idTurma", turma.getId())
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao contar frequência da turma.", e);
        }
    }
    
    public int excluirFrequenciaPorTurma(Turma turma) throws DAOException {
        try {
            return  em.createQuery("DELETE FROM Frequencia f WHERE f.turma.id = :idTurma")
                    .setParameter("idTurma", turma.getId())
                    .executeUpdate();
        } catch (Exception e) {
            throw new DAOException("Erro ao contar frequência da turma.", e);
        }
    }

}
