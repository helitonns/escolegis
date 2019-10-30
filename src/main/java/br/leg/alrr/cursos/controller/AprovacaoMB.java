package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.business.BlocoParametro;
import br.leg.alrr.cursos.model.Aluno;
import br.leg.alrr.cursos.model.Curso;
import br.leg.alrr.cursos.model.Turma;
import br.leg.alrr.cursos.persistence.CursoDAO;
import br.leg.alrr.cursos.persistence.FrequenciaDAO;
import br.leg.alrr.cursos.persistence.TurmaDAO;
import br.leg.alrr.cursos.util.DAOException;
import br.leg.alrr.cursos.util.FacesUtils;
import br.leg.alrr.cursos.util.GeneratorPDF;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author heliton
 */
@Named
@ViewScoped
public class AprovacaoMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private CursoDAO cursoDAO;

    @EJB
    private TurmaDAO turmaDAO;

    @EJB
    private FrequenciaDAO frequenciaDAO;

    private Curso curso;
    private Aluno aluno;

    private ArrayList<Curso> cursos;
    private ArrayList<Turma> turmas;
    private ArrayList<Aluno> alunos;
    private ArrayList<BlocoParametro> blocoDeParametros;

    private StringBuilder query;
    
    private boolean incluirNomeNapesquisa;
    private boolean incluirDataInicioNaPesquisa;
    private boolean incluirDataTerminoNaPesquisa;
    private int porcentagemDePresenca;
    
    private GeneratorPDF gerarPdf;

    //==========================================================================
    @PostConstruct
    public void init() {
        limparForm();

        //VERIFICA SE FOI PASSADO UM CURSO PARA ANALISE DOS APROVADOS
        if (FacesUtils.getBean("curso") != null) {
            try {
                curso = (Curso) FacesUtils.getBean("curso");
                turmas = (ArrayList<Turma>) turmaDAO.listarTurmasConcluidasPorCurso(curso);
            } catch (DAOException e) {
                FacesUtils.addErrorMessage(e.getMessage());
            }
        }
    }

    private void limparForm() {
        incluirNomeNapesquisa = false;
        incluirDataInicioNaPesquisa = false;
        incluirDataTerminoNaPesquisa = false;
        porcentagemDePresenca = 75;

        query = new StringBuilder();
        curso = new Curso();
        aluno = new Aluno();

        blocoDeParametros = new ArrayList<>();
        turmas = new ArrayList<>();
        alunos = new ArrayList<>();
    }

    private void construirConsulta() {

        query.append("SELECT o FROM Curso o WHERE o.iniciado = false");

        if (incluirNomeNapesquisa) {
            query.append(" AND o.nome LIKE :nome");
            blocoDeParametros.add(new BlocoParametro("nome", curso.getNome()));
        }
        if (incluirDataInicioNaPesquisa) {
            query.append(" AND o.dataDeInicio <= :dataDeInicio");
            blocoDeParametros.add(new BlocoParametro("dataDeInicio", curso.getDataDeInicio()));
        }
        if (incluirDataTerminoNaPesquisa) {
            query.append(" AND o.dataDeTermino >= :dataDeTermino");
            blocoDeParametros.add(new BlocoParametro("dataDeTermino", curso.getDataDeTermino()));
        }
    }

    public void pesquisarCurso() {
        try {
            if (podePesquisar()) {
                construirConsulta();

                cursos = (ArrayList<Curso>) cursoDAO.pesquisarCursoPorConsultaDinamica(query.toString(), blocoDeParametros);

                if (cursos.size() < 1) {
                    FacesUtils.addWarnMessage("Sem resultado para a pesquisa realizada!");
                }
            } else {
                FacesUtils.addWarnMessage("Selecione pelo menos um campo para realizar a consulta!");
            }
            limparForm();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private boolean podePesquisar() {
        return incluirNomeNapesquisa || incluirDataInicioNaPesquisa || incluirDataTerminoNaPesquisa;
    }

    public String verificarAprovados() {
        FacesUtils.setBean("curso", curso);
        return "aprovados.xhtml" + "?faces-redirect=true"; 
    }

    /**
     * Monta a lista de alunos, contando quantas aulas houve na turma e de quantas aulas ele participou.
     * Setando os valores de campos transientes, para serem exibidos em página própria.
     */
    public void montarListaDeAlunos() {
        try {
            alunos = new ArrayList<>();
            for (Turma t : turmas) {
                Long quantidadeDeAulas = frequenciaDAO.contarFrequenciaDaTurma(t);
                Collections.sort(t.getAlunos());
                for (Aluno a : t.getAlunos()) {
                    Aluno novoAluno = new Aluno();
                    novoAluno.setNome(a.getNome());
                    novoAluno.setCpf(a.getCpf());
                    novoAluno.setTurma(t);
                    Long quantidadeDePresencas = frequenciaDAO.contarFrequenciaDoAlunoPorTurma(a, t);
                    novoAluno.setQuantidadeDeAulas(quantidadeDeAulas);
                    novoAluno.setQuantidadeDePresencas(quantidadeDePresencas);
                    alunos.add(novoAluno);
                }
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        FacesUtils.removeBean("curso");
    }

    public void imprimirCertificadoIndividual(){
        try {
            gerarPdf = new GeneratorPDF();
            //seta curso em aluno
            aluno.setCurso(curso);
            
            gerarPdf.certificado(aluno);
            
        } catch (Exception e) {
        	FacesUtils.addErrorMessage("Erro ao imprimir certificado individual: "+e.getCause());
        }
    }
    
    public void imprimirTodosOsCertificados(){
        try {
            gerarPdf = new GeneratorPDF();
            
            List<Aluno> alunosAprovados = new ArrayList();
            
            //verifica e seleciona os alunos aprovados
            for (Aluno a : alunos) {
                if (((a.getQuantidadeDePresencas()*100)/a.getQuantidadeDeAulas()) >= porcentagemDePresenca) {
                    a.setCurso(curso);
                    alunosAprovados.add(a);
                }
            }
            
            //imprimir os certificados dos alunos aprovados
            gerarPdf.certificados(alunosAprovados);
            
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Erro ao imprimir todos os certificados: "+e.getCause());
        }
    }
    
    
    
    public String cancelar() {
        return "verificar-aprovacao.xhtml" + "?faces-redirect=true";
    }

    //==========================================================================
    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public ArrayList<Curso> getCursos() {
        return cursos;
    }

    public boolean isIncluirNomeNapesquisa() {
        return incluirNomeNapesquisa;
    }

    public void setIncluirNomeNapesquisa(boolean incluirNomeNapesquisa) {
        this.incluirNomeNapesquisa = incluirNomeNapesquisa;
    }

    public boolean isIncluirDataInicioNaPesquisa() {
        return incluirDataInicioNaPesquisa;
    }

    public void setIncluirDataInicioNaPesquisa(boolean incluirDataInicioNaPesquisa) {
        this.incluirDataInicioNaPesquisa = incluirDataInicioNaPesquisa;
    }

    public boolean isIncluirDataTerminoNaPesquisa() {
        return incluirDataTerminoNaPesquisa;
    }

    public void setIncluirDataTerminoNaPesquisa(boolean incluirDataTerminoNaPesquisa) {
        this.incluirDataTerminoNaPesquisa = incluirDataTerminoNaPesquisa;
    }

    public int getPorcentagemDePresenca() {
        return porcentagemDePresenca;
    }

    public void setPorcentagemDePresenca(int porcentagemDePresenca) {
        this.porcentagemDePresenca = porcentagemDePresenca;
    }

    public ArrayList<Aluno> getAlunos() {
        return alunos;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
    
    
}
