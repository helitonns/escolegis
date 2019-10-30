package br.leg.alrr.cursos.persistence;

import br.leg.alrr.cursos.business.BlocoParametro;
import br.leg.alrr.cursos.business.Sexo;
import br.leg.alrr.cursos.model.Aluno;
import br.leg.alrr.cursos.model.Curso;
import br.leg.alrr.cursos.model.Matricula;
import br.leg.alrr.cursos.model.Unidade;
import br.leg.alrr.cursos.util.DAOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author heliton
 */
@Stateless
public class MatriculaDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Matricula o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar matrícula.", e);
        }
    }

    public void atualizar(Matricula o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar matrícula.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Matricula o order by o.curso.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar matrículas.", e);
        }
    }

    public List listarMatriculasAtivas() throws DAOException {
        try {
            return em.createQuery("select o from Matricula o where o.status = true order by o.curso.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar matrículas ativas.", e);
        }
    }

    public List listarMatriculasPorCurso(Long id) throws DAOException {
        try {
            return em.createQuery("select o from Matricula o where o.status = true and o.curso.id = :idCurso order by o.curso.nome, o.aluno.nome asc")
                    .setParameter("idCurso", id)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar matrículas por curso.", e);
        }
    }

    public List listarMatriculasAtivasPorUnidade(Unidade u) throws DAOException {
        try {
            return em.createQuery("select o from Matricula o where o.status = true and o.unidade.id = :idUnidade order by o.curso.nome asc")
                    .setParameter("idUnidade", u.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar matrículas por unidade.", e);
        }
    }
    
    public List listarMatriculasConcluidasPorAluno(Aluno a) throws DAOException {
        try {
            return em.createQuery("select o from Matricula o where o.status = FALSE and o.curso.iniciado = false AND o.aluno.id = :idAluno order by o.curso.nome asc")
                    .setParameter("idAluno", a.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar matrículas concluidas por aluno.", e);
        }
    }

    public void remover(Matricula o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover matrícula.", e);
        }
    }

    public Matricula buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Matricula.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar matrícula por ID.", e);
        }
    }

    public List<Matricula> gerarRelatorioDinamico(String consulta, List<BlocoParametro> parametros) throws DAOException {
        try {

            Query q = em.createQuery(consulta);
            Calendar d = new GregorianCalendar();

            for (BlocoParametro b : parametros) {

                if (b.getParametro().equals("naData")) {
                    d.setTime((Date) b.getValor());
                    d.set(Calendar.HOUR, 6);
                    d.set(Calendar.MINUTE, 0);
                    d.set(Calendar.SECOND, 0);
                    q.setParameter(b.getParametro(), d.getTime());
                    d.set(Calendar.HOUR, 23);
                    q.setParameter("naData2", d.getTime());
                } else if (b.getParametro().equals("depoisDaData")) {
                    d.setTime((Date) b.getValor());
                    d.set(Calendar.HOUR, 23);
                    d.set(Calendar.MINUTE, 0);
                    d.set(Calendar.SECOND, 0);
                    q.setParameter(b.getParametro(), d.getTime());
                } else if (b.getParametro().equals("intervalo1") || b.getParametro().equals("antesDaData")) {
                    d.setTime((Date) b.getValor());
                    d.set(Calendar.HOUR, 6);
                    d.set(Calendar.MINUTE, 0);
                    d.set(Calendar.SECOND, 0);
                    q.setParameter(b.getParametro(), d.getTime());
                } 
                else if (b.getParametro().equals("intervalo2")) {
                    d.setTime((Date) b.getValor());
                    d.set(Calendar.HOUR, 23);
                    d.set(Calendar.MINUTE, 0);
                    d.set(Calendar.SECOND, 0);
                    q.setParameter(b.getParametro(), d.getTime());
                } 
                else if (b.getParametro().equals("dataNascimento")) {
                    d.setTime((Date) b.getValor());
                    q.setParameter("dia", d.get(Calendar.DAY_OF_MONTH));
                    q.setParameter("mes", d.get(Calendar.MONTH)+1);
                } 
                else if (b.getParametro().equals("dataNascimento1")) {
                    d.setTime((Date) b.getValor());
                    q.setParameter("dia1", d.get(Calendar.DAY_OF_MONTH));
                    q.setParameter("mes1", d.get(Calendar.MONTH)+1);
                } 
                
                else if (b.getParametro().equals("dataNascimento2")) {
                    d.setTime((Date) b.getValor());
                    q.setParameter("dia2", d.get(Calendar.DAY_OF_MONTH));
                    q.setParameter("mes2", d.get(Calendar.MONTH)+1);
                } 
                
                else if (b.getParametro().equals("ano")) {
                    d = new GregorianCalendar();
                    int ano = (d.get(GregorianCalendar.YEAR) - (int)b.getValor());
                    q.setParameter("ano", ano);
                } 
                else if (b.getParametro().equals("ano1")) {
                    d = new GregorianCalendar();
                    int ano = (d.get(GregorianCalendar.YEAR) - (int)b.getValor());
                    q.setParameter("ano1", ano);
                } 
                else if (b.getParametro().equals("ano2")) {
                    d = new GregorianCalendar();
                    int ano = (d.get(GregorianCalendar.YEAR) - (int)b.getValor());
                    q.setParameter("ano2", ano);
                } 
                
                
                else if (b.getParametro().equals("andamento") || b.getParametro().equals("aindaCursando") || b.getParametro().equals("possuiFilho")) {
                    q.setParameter(b.getParametro(), (boolean) b.getValor());
                } else if (b.getParametro().equals("idMunicipio") || b.getParametro().equals("idBairro") || b.getParametro().equals("idCurso") || b.getParametro().equals("idPais")) {
                    q.setParameter(b.getParametro(), (Long) b.getValor());
                } else if (b.getParametro().equals("cpf") || b.getParametro().equals("nome") || b.getParametro().equals("celular")) {
                    q.setParameter(b.getParametro(), (String) b.getValor());
                } else if (b.getParametro().equals("sexo")) {
                    q.setParameter(b.getParametro(), (Sexo) b.getValor());
                }
            }
            return q.getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao gerar relatório.", e);
        }
    }

    public boolean podeMatricular(Long idCurso, Long idALuno) throws DAOException {
        try {
            List<Matricula> m = em.createQuery("select m from Matricula m where m.curso.id = :idCurso and m.aluno.id = :idAluno")
                    .setParameter("idCurso", idCurso)
                    .setParameter("idAluno", idALuno)
                    .getResultList();
            return m.size() <= 0;
        } catch (Exception e) {
            throw new DAOException("Erro ao verificar se é possível matrícular.", e);
        }
    }
    
    public void finalizarMatriculaPorCurso(Curso c){
        em.createQuery("UPDATE Matricula m SET m.status = false where m.curso.id = :idCurso").setParameter("idCurso", c.getId()).executeUpdate();
    }
    
    public int excluirMatriculaPorCurso(Curso curso) throws DAOException {
        try {
            return  em.createQuery("DELETE FROM Matricula m WHERE m.curso.id = :idCurso")
                    .setParameter("idCurso", curso.getId())
                    .executeUpdate();
        } catch (Exception e) {
            throw new DAOException("Erro ao contar frequência da turma.", e);
        }
    }
    
}
