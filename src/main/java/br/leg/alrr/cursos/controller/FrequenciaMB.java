package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.business.Loger;
import br.leg.alrr.cursos.business.TipoAcao;
import br.leg.alrr.cursos.model.Aluno;
import br.leg.alrr.cursos.model.Curso;
import br.leg.alrr.cursos.model.Frequencia;
import br.leg.alrr.cursos.model.Turma;
import br.leg.alrr.cursos.model.UsuarioComUnidade;
import br.leg.alrr.cursos.persistence.CursoDAO;
import br.leg.alrr.cursos.persistence.FrequenciaDAO;
import br.leg.alrr.cursos.persistence.LogSistemaDAO;
import br.leg.alrr.cursos.persistence.TurmaDAO;
import br.leg.alrr.cursos.util.DAOException;
import br.leg.alrr.cursos.util.FacesUtils;
import br.leg.alrr.cursos.util.Relatorio;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author heliton
 */
@Named
@ViewScoped
public class FrequenciaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private CursoDAO cursoDAO;

    @EJB
    private TurmaDAO turmaDAO;

    @EJB
    private FrequenciaDAO frequenciaDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private ArrayList<Curso> cursos;
    private ArrayList<Turma> turmas;
    private ArrayList<Frequencia> frequencias;
    private ArrayList<Frequencia> frequenciaDosFaltosos;
    private ArrayList<Frequencia> frequenciaDaAula;

    private Turma turma;
    private Frequencia frequencia;

    private long idCurso;
    private boolean exibirListaDeAluno;
    private boolean exibirListaDeAulasDaTurma;
    private boolean exibirFrequenciaDaAula;
    private boolean justificarFrequencia;
    private boolean haAlunosFaltosos;
    private Date dataDaFrequencia;

    private XSSFWorkbook arquivoExcel;
    private XSSFSheet planilha;

//==========================================================================
    @PostConstruct
    public void init() {
        cursos = new ArrayList<>();
        limparForm();
        listarCurso();

        try {
            //VERIFICA SE HÁ FALTA PARA JUSTIFICAR
            if (FacesUtils.getBean("idCurso") != null) {
                idCurso = (Long) FacesUtils.getBean("idCurso");
                FacesUtils.removeBean("idCurso");
            }

            if (FacesUtils.getBean("turma") != null) {
                turmas.add((Turma) FacesUtils.getBean("turma"));
                turma = (Turma) FacesUtils.getBean("turma");
                FacesUtils.removeBean("turma");
            }

            if (FacesUtils.getBean("dataDaFrequencia") != null) {
                dataDaFrequencia = (Date) FacesUtils.getBean("dataDaFrequencia");
                FacesUtils.removeBean("dataDaFrequencia");
            }

            if (FacesUtils.getBean("justificarFalta") != null) {
                buscarAlunosFaltosos();
                FacesUtils.removeBean("justificarFalta");
            }

            if (FacesUtils.getBean("editarFrequencia") != null) {
                listarFrequenciaDaAulaParaEditar();
                FacesUtils.removeBean("editarFrequencia");
            }

        } catch (Exception e) {
            FacesUtils.addErrorMessage("Erro ao tentar editar aluno.");
        }
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    private void limparForm() {
        idCurso = 0l;
        exibirListaDeAluno = false;
        exibirListaDeAulasDaTurma = false;
        exibirFrequenciaDaAula = false;
        justificarFrequencia = false;
        haAlunosFaltosos = false;
        dataDaFrequencia = new Date();

        turma = new Turma();
        frequencia = new Frequencia();

        turmas = new ArrayList<>();
        frequencias = new ArrayList<>();
        frequenciaDosFaltosos = new ArrayList<>();
        frequenciaDaAula = new ArrayList<>();
    }

    private void listarCurso() {
        try {
            cursos = null;
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            cursos = (ArrayList<Curso>) cursoDAO.listarCursosIniciadosPorUnidade(u.getUnidade());
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void selecionarCurso(ValueChangeEvent event) {
        try {
            Long id = Long.parseLong(event.getNewValue().toString());
            turmas = null;
            turmas = (ArrayList<Turma>) turmaDAO.listarTurmasIniciadasPorCurso(new Curso(id));
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void selecionarTurma() {
        try {
            haAlunosFaltosos = false;
            justificarFrequencia = false;
            frequencias = null;
            frequencias = new ArrayList<>();

            for (Aluno a : turma.getAlunos()) {
                Frequencia f = new Frequencia();
                f.setAluno(a);
                f.setTurma(turma);
                f.setPresenca(false);
                f.setFaltaJustificada(false);
                frequencias.add(f);
            }
            Collections.sort(frequencias);
            exibirListaDeAluno = true;
        } catch (Exception e) {
            FacesUtils.addInfoMessage("Erro ao exibir lista de alunos: " + e.getMessage());
        }
    }

    public String salvarFrequencia() {
        try {
            //ALTERNATIVA PARA O CÓDIGO COMENTADO ABAIXO, NESTE TRECHO DE CÓDIGO O SISTEMA VERIFICA SE JÁ FOI LANÇADA, PELO MENOS, UMA FREQUÊNCIA
            //PARA O DIA PASSADO, EM CASO AFIRMATIVO, O SISTEMA ASSUME TODAS AS DEMAIS FREQUÊNCIA TAMBÉM JÁ FORAM LANÇADAS,
            //COM ISSO O SISTEMA PASSA A ATUALIZAR AS FREQUÊNCIAS PASSADAS. EM CASO NEGATIVO, O SISTEMA SALVA AS FREQUÊNCIAS PASSADAS SEM VERIFICAR.
            Frequencia fv = frequencias.get(0);
            fv.setDataFrequencia(dataDaFrequencia);

            if (!frequenciaDAO.verificarSeAFrequenciaJaFoiLancada(fv)) {
                for (Frequencia f : frequencias) {
                    f.setDataFrequencia(dataDaFrequencia);
                    frequenciaDAO.salvar(f);
                }
                FacesUtils.addInfoMessageFlashScoped("Frequência salva com sucesso!!");
            } else {
                for (Frequencia f : frequencias) {
                    frequenciaDAO.atualizar(f);
                }
                FacesUtils.addInfoMessageFlashScoped("Frequência atualizada com sucesso!!");
            }

            //DESTA FORMA DEMOVA BASTANTE PARA SALVAR A FREQUÊNCIA, PORQUE O SISTEMA VERIFICAVA SE CADA ALUNO POSSUI FREQUÊNCIA PARA O DIA PASSADO
            //            for (Frequencia f : frequencias) {
            //                f.setDataFrequencia(dataDaFrequencia);
            //                if (!frequenciaDAO.verificarSeAFrequenciaJaFoiLancada(f)) {
            //                    frequenciaDAO.salvar(f);
            //                } else {
            //                    frequenciaDAO.atualizar(f);
            //                }
            //            }
            Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método FrequenciaMB.salvarFrequencia() para salvar a frequênciia.");
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "frequencia.xhtml" + "?faces-redirect=true";
    }

    public void prepararJustificacao() {
        exibirListaDeAluno = false;
        justificarFrequencia = true;
    }

    public void buscarAlunosFaltosos() {
        try {
            exibirListaDeAluno = false;
            frequenciaDosFaltosos = null;
            frequenciaDosFaltosos = new ArrayList<>();
            frequenciaDosFaltosos = (ArrayList<Frequencia>) frequenciaDAO.listarAlunosFaltosos(turma, dataDaFrequencia);
            if (frequenciaDosFaltosos.size() >= 1) {
                haAlunosFaltosos = true;
            } else {
                FacesUtils.addWarnMessage("Não houve faltas neste dia.");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public String justificarFalta() {
        try {
            for (Frequencia f : frequenciaDosFaltosos) {
                if (frequenciaDAO.contarFaltaJustifcadasDoAlunoPorTurma(f.getAluno(), turma) < turma.getQuantidadeMaximaDeFaltasJustificadas()) {
                    frequenciaDAO.atualizar(f);
                    if (f.isFaltaJustificada()) {
                        FacesUtils.addInfoMessageFlashScoped("A falta do aluno(a): " + f.getAluno().getCpf() + " - " + f.getAluno().getNome() + ", foi justificada!!!");
                        Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método FrequenciaMB.justificarFalta() para justificar a frequênciia do aluno "+f.getAluno().getId()+".");
                    }
                } else {
                    FacesUtils.addWarnMessageFlashScoped("A falta do aluno(a): " + f.getAluno().getCpf() + " - " + f.getAluno().getNome() + ", NÃO foi justificada, pois já excedeu o número máximo de justificação!!!");
                    Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método FrequenciaMB.justificarFalta() para justificar a frequênciia do aluno "+f.getAluno().getId()+", mas não pôde, pois o ele já excedeu o número de faltas.");
                }
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        return "frequencia.xhtml" + "?faces-redirect=true";
    }

    public void imprimirFrequencia() {
        Relatorio<Frequencia> report = new Relatorio<>();
        if (frequencias == null) {
            try {

                if (frequencias.isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Não há registros!"));

                } else {
                    UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
                    u.getUnidade().getNome();
                    report.getFrequencia(frequencias);

                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            report.getFrequencia(frequencias);
        }
        Loger.registrar(logSistemaDAO, TipoAcao.EXECUTAR, "O usuário executou o método FrequenciaMB.imprimirFrequencia().");
    }

    public void imprimirFrequencia2() {
        Relatorio<Frequencia> report = new Relatorio<>();
        if (frequencias == null) {
            try {

                if (frequencias.isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Não há registros!"));

                } else {
                    UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
                    u.getUnidade().getNome();
                    report.getFrequencia2(frequencias);

                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            report.getFrequencia2(frequencias);
        }
        Loger.registrar(logSistemaDAO, TipoAcao.EXECUTAR, "O usuário executou o método FrequenciaMB.imprimirFrequencia2().");
    }

    public void listarAulasDaTurma() {
        try {
            frequencias = null;
            frequencias = (ArrayList<Frequencia>) frequenciaDAO.listarAulasDaTurma(turma);
            exibirListaDeAulasDaTurma = true;
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void listarFrequenciaDaAula() {
        try {
            frequenciaDaAula = null;
            frequenciaDaAula = (ArrayList<Frequencia>) frequenciaDAO.listarFrequenciaDaAula(frequencia);
            exibirFrequenciaDaAula = true;
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void listarFrequenciaDaAulaParaEditar() {
        try {
            frequencias = null;
            frequencias = (ArrayList<Frequencia>) frequenciaDAO.listarFrequenciaDaAula(turma, dataDaFrequencia);
            exibirListaDeAluno = true;
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public String irParaJustificarFalta() {
        try {
            FacesUtils.setBean("idCurso", idCurso);
            FacesUtils.setBean("turma", turma);
            FacesUtils.setBean("dataDaFrequencia", frequenciaDaAula.get(0).getDataFrequencia());
        } catch (Exception e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        return "frequencia.xhtml" + "?faces-redirect=true";
    }

    public String editarFrequencia() {
        try {
            FacesUtils.setBean("editarFrequencia", true);
            FacesUtils.setBean("idCurso", idCurso);
            FacesUtils.setBean("turma", turma);
            FacesUtils.setBean("dataDaFrequencia", frequenciaDaAula.get(0).getDataFrequencia());
        } catch (Exception e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        return "frequencia.xhtml" + "?faces-redirect=true";
    }

    public String cancelar() {
        return "frequencia.xhtml" + "?faces-redirect=true";
    }

    /**
     * Método usado para liberar memória. Foi necessário adicionar este método
     * porque, possivelmente, está havendo vazamento de memória, fazendo com que
     * a aplicação pare de funcionar. Basicamente o método irá anular as
     * referências das variáveis, sinalizando para o Garbage Collector realizar
     * a coleta.
     */
    private void limparMemoria() {
        cursoDAO = null;
        turmaDAO = null;
        frequenciaDAO = null;
        cursos = null;
        turmas = null;
        frequencias = null;
        frequenciaDosFaltosos = null;
        frequenciaDaAula = null;
        turma = null;
        frequencia = null;
    }

    /**
     * Ao sair da página executa o método @limparMemoria.
     */
    @PreDestroy
    public void saindoDaPagina() {
        limparMemoria();
    }

    //==========================================================================
    // Código para lançamento de frequência através de um arquivo excel.
    /**
     * Método que recebe o arquivo pelo fileUpload.
     *
     * @param event
     */
    public void handleFileUpload(FileUploadEvent event) {
        try {
            arquivoExcel = new XSSFWorkbook();
            arquivoExcel = (XSSFWorkbook) WorkbookFactory.create(event.getFile().getInputstream());
            planilha = arquivoExcel.getSheetAt(0);
        } catch (IOException | EncryptedDocumentException e) {
            FacesUtils.addErrorMessage("Houve um erro na importação do arquivo!!!");
        }
    }

    /**
     * Cancela a operação redirecionando para a mesma página.
     *
     * @return
     */
    public String cancelarFrequenciaComArquivo() {
        return "frequencia-com-arquivo.xhtml" + "?faces-redirect=true";
    }

    /**
     * <p>
     * Método que salva as frequências a partir de um arquivo xlsx. Este arquivo
     * deve vir em um determinado layout. A primeira linha do arquivo deve
     * trazer as seguintes informações: a primeira coluna deve conter o id da
     * turma para a qual será lançada a frequência; as demais colunas devem
     * conter as datas das aulas no formato de dd/MM/aaaa. A partir da segunda
     * linha o arquivo deverá as seguintes informações: a primeira coluna deve
     * conter o id do aluno para o qual será lançada a frequência; as demais
     * colunas trará um dos seguintes valores: "P", "F" ou "FJ". Sendo que "P"
     * seguinifica presença, "F" falta e "FJ" fata justificada.
     * </p>
     *
     * @return
     */
    public String salvarFrequenciaPorArquivo() {
        try {
            //ArrayList para percorrer as linhas da planilha
            ArrayList<Row> linhasDaPlanilha = (ArrayList<Row>) itaratorToList(planilha.iterator());

            //pega a primeira linha do arquivo, que conterá o ID da turma e as datas das aulas
            Row row = linhasDaPlanilha.get(0);
            ArrayList<Cell> primeiraLinha = (ArrayList<Cell>) itaratorToList(row.cellIterator());

            //remove a primeira linha
            linhasDaPlanilha.remove(0);

            //Será executado enquanto houver linhas para ser percorridas
            for (Row linha : linhasDaPlanilha) {

                //ArrayList usado para percorrer as colunas de cada linha
                ArrayList<Cell> colunasDaLinha = (ArrayList<Cell>) itaratorToList(linha.cellIterator());

                //será executado enquanto houver colunas para ser percorridas
                for (int i = 1; i < colunasDaLinha.size(); i++) {

                    Frequencia frequenciaDoArquivo = new Frequencia();

                    //===============================================================================
                    //Trabalha a primeira linha do aquivo, pegando o id da turma e as datas das aulas
                    //seta a turma na frequência
                    Double idDaTurma = primeiraLinha.get(0).getNumericCellValue();
                    frequenciaDoArquivo.setTurma(new Turma(idDaTurma.longValue()));

                    //seta a data da frequência
                    frequenciaDoArquivo.setDataFrequencia(primeiraLinha.get(i).getDateCellValue());
                    //===============================================================================

                    //===============================================================================
                    //Trabalha a partir da segunda linha do arquivo, pegando o id dos alunos e o status da frequência
                    //Seta o aluno
                    Double idDoAluno = colunasDaLinha.get(0).getNumericCellValue();
                    frequenciaDoArquivo.setAluno(new Aluno(idDoAluno.longValue()));

                    //seta o status da frequência
                    String statusDaFrequencia = colunasDaLinha.get(i).getStringCellValue();
                    //CASO EM QUE HOUVE FALTA
                    if (statusDaFrequencia.equalsIgnoreCase("F")) {
                        frequenciaDoArquivo.setPresenca(false);
                    } //CASO EM QUE HOUVE FALTA JUSTIFICADA
                    else if (statusDaFrequencia.equalsIgnoreCase("FJ")) {
                        frequenciaDoArquivo.setPresenca(false);
                        frequenciaDoArquivo.setFaltaJustificada(true);
                    } //EM QUALQUER OUTRO CASO SERÁ ATRIBUIDO PRESENÇA PARA O ALUNO
                    else {
                        frequenciaDoArquivo.setPresenca(true);
                    }
                    //===============================================================================

                    frequencias.add(frequenciaDoArquivo);
                }
            }

            //É AQUI QUE AS FREQUÊNCIAS SERÃO EFETICAMENTE SALVAS
            for (Frequencia f : frequencias) {
                frequenciaDAO.salvar(f);
            }
            FacesUtils.addInfoMessageFlashScoped("Frequências salvas com sucesso!!!");
            Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método FrequenciaMB.salvarFrequenciaPorArquivo() para salvar a frequênciia.");
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped("Houve um erro ao salvar frequência por arquivo!!!");
        }
        return "frequencia-com-arquivo.xhtml" + "?faces-redirect=true";
    }

    private List<?> itaratorToList(Iterator<?> i) {
        List l = new ArrayList<>();
        i.forEachRemaining(n -> l.add(n));
        return l;
    }
//==========================================================================

    public long getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(long idCurso) {
        this.idCurso = idCurso;
    }

    public ArrayList<Curso> getCursos() {
        return cursos;
    }

    public ArrayList<Turma> getTurmas() {
        return turmas;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public boolean isExibirListaDeAluno() {
        return exibirListaDeAluno;
    }

    public ArrayList<Frequencia> getFrequencias() {
        return frequencias;
    }

    public Date getDataDaFrequencia() {
        return dataDaFrequencia;
    }

    public void setDataDaFrequencia(Date dataDaFrequencia) {
        this.dataDaFrequencia = dataDaFrequencia;
    }

    public boolean isJustificarFrequencia() {
        return justificarFrequencia;
    }

    public boolean isHaAlunosFaltosos() {
        return haAlunosFaltosos;
    }

    public ArrayList<Frequencia> getFrequenciaDosFaltosos() {
        return frequenciaDosFaltosos;
    }

    public void setFrequenciaDosFaltosos(ArrayList<Frequencia> frequenciaDosFaltosos) {
        this.frequenciaDosFaltosos = frequenciaDosFaltosos;
    }

    public boolean isExibirListaDeAulasDaTurma() {
        return exibirListaDeAulasDaTurma;
    }

    public ArrayList<Frequencia> getFrequenciaDaAula() {
        return frequenciaDaAula;
    }

    public Frequencia getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(Frequencia frequencia) {
        this.frequencia = frequencia;
    }

    public boolean isExibirFrequenciaDaAula() {
        return exibirFrequenciaDaAula;
    }

}
