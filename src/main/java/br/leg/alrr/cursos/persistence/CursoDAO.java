package br.leg.alrr.cursos.persistence;

import br.leg.alrr.cursos.business.BlocoParametro;
import br.leg.alrr.cursos.model.Curso;
import br.leg.alrr.cursos.model.Modulo;
import br.leg.alrr.cursos.model.Turma;
import br.leg.alrr.cursos.model.Unidade;
import br.leg.alrr.cursos.util.DAOException;
import java.util.ArrayList;
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
public class CursoDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Curso o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar curso.", e);
        }
    }

    public void atualizar(Curso o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atulizar curso.", e);
        }
    }

    public Curso buscarReferencia(Curso o) throws DAOException {
        try {
            return em.getReference(Curso.class, o.getId());
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar referência de curso.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Curso o where o.status = TRUE order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar cursos.", e);
        }
    }

    public List listarCursosAtivos() throws DAOException {
        try {
            return em.createQuery("select o from Curso o where o.status = TRUE AND o.status = true order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar cursos ativos.", e);
        }
    }

    public List listarCursosAtivosPorUnidade(Unidade u) throws DAOException {
        try {
            return em.createQuery("select o from Curso o where o.status = TRUE AND o.status = true and o.unidade.id = :idUnidade order by o.nome asc")
                    .setParameter("idUnidade", u.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar cursos por unidade.", e);
        }
    }

    public List listarCursosIniciadosPorUnidade(Unidade u) throws DAOException {
        try {
            return em.createQuery("select o from Curso o where o.status = TRUE AND o.iniciado = true and o.unidade.id = :idUnidade order by o.nome asc")
                    .setParameter("idUnidade", u.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar cursos por unidade.", e);
        }
    }

    public void remover(Curso o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover curso.", e);
        }
    }

    public Curso buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Curso.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar curso por ID.", e);
        }
    }

    public List pesquisarCursoConcluidoPorNome(String nome) throws DAOException {
        try {
            return em.createQuery("SELECT o FROM Curso o WHERE o.nome LIKE :nome AND o.iniciado = false AND o.status = TRUE")
                    .setParameter("nome", "%" + nome + "%")
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar curso por nome.", e);
        }
    }

    public List pesquisarCursoPorConsultaDinamica(String query, ArrayList<BlocoParametro> parametros) throws DAOException {
        try {
            Query q = em.createQuery(query);
            for (BlocoParametro b : parametros) {

                if (b.getParametro().equals("nome")) {
                    q.setParameter(b.getParametro(), "%" + (String) b.getValor() + "%");
                }
                if (b.getParametro().equals("dataDeInicio")) {
                    q.setParameter(b.getParametro(), (Date) b.getValor());
                }
                if (b.getParametro().equals("dataDeTermino")) {
                    q.setParameter(b.getParametro(), (Date) b.getValor());
                }
            }
            return q.getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao fazer pesquisa dinâmica.", e);
        }
    }

    public boolean cursoAlcancouSeuTermoFinal(Curso c) throws DAOException {
        try {
            Curso curso = buscarPorID(c.getId());
            GregorianCalendar gc = new GregorianCalendar();
            gc.add(GregorianCalendar.DAY_OF_MONTH, -1);
            return gc.getTime().after(curso.getDataDeTermino());
        } catch (DAOException e) {
            throw new DAOException("Erro ao verificar se o curso alcançouo o seu termo final.", e);
        }
    }
    
    public void finalizarMatriculaPorCurso(Curso c){
        em.createQuery("UPDATE Matricula SET status = false where curso.id = :idCurso").setParameter("idCurso", c.getId()).executeUpdate();
    }
    
    public int pesquisarCargaHorariaDoCurso(Curso c){
        List<Modulo> modulos = em.createQuery("SELECT m FROM Modulo m where m.curso.id = :idCurso")
                .setParameter("idCurso", c.getId())
                .getResultList();
        
        List<Turma> turmas = new ArrayList<>();
        
        for (Modulo m : modulos) {
            List<Turma> tt = em.createQuery("SELECT t FROM Turma t where t.modulo.id = :idModulo")
                    .setParameter("idModulo", m.getId())
                    .getResultList();
            turmas.addAll(tt);
        }

        int cargaHorariaDoCurso = 0;

        for (Turma t : turmas) {
            cargaHorariaDoCurso += t.getCargaHoraria();
        }
        return cargaHorariaDoCurso;
    }
}
