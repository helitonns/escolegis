package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.model.Bairro;
import br.leg.alrr.cursos.model.Aluno;
import br.leg.alrr.cursos.model.Endereco;
import br.leg.alrr.cursos.model.Municipio;
import br.leg.alrr.cursos.business.Sexo;
import br.leg.alrr.cursos.model.Matricula;
import br.leg.alrr.cursos.model.Pais;
import br.leg.alrr.cursos.model.Turma;
import br.leg.alrr.cursos.model.UsuarioComUnidade;
import br.leg.alrr.cursos.persistence.BairroDAO;
import br.leg.alrr.cursos.persistence.AlunoDAO;
import br.leg.alrr.cursos.persistence.MatriculaDAO;
import br.leg.alrr.cursos.persistence.MunicipioDAO;
import br.leg.alrr.cursos.persistence.PaisDAO;
import br.leg.alrr.cursos.persistence.TurmaDAO;
import br.leg.alrr.cursos.util.DAOException;
import br.leg.alrr.cursos.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author heliton
 */
@Named
@ViewScoped
public class AlunoMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AlunoDAO alunoDAO;

    @EJB
    private MunicipioDAO municipioDAO;

    @EJB
    private BairroDAO bairroDAO;

    //Usada para fazer a matricula
    @EJB
    private TurmaDAO turmaDAO;

    @EJB
    private MatriculaDAO matriculaDAO;

    @EJB
    private PaisDAO paisDAO;

    private ArrayList<Pais> paises;
    private ArrayList<Municipio> municipios;
    private ArrayList<Bairro> bairros;
    private ArrayList<Turma> turmas;

    private Aluno aluno;
    private Endereco endereco;
    private Municipio municipio;
    private Bairro bairro;
    private Long pais;

    //Usada para fazer a matricula
    private Turma turma;

    private Sexo sexo = Sexo.MASCULINO;
    private Long idMunicipio;
    private boolean editandoALuno;
    private boolean matricularNaTurma;

    //==========================================================================
    @PostConstruct
    public void init() {
        limparForm();

        bairros = new ArrayList<>();
        municipios = new ArrayList<>();

        listarMunicipio();
        listarPaises();

        try {
            //VERIFICA SE HÁ CPF A SER VIRIFICADO
            if (FacesUtils.getBean("alunoCPF") != null) {
                aluno = (Aluno) FacesUtils.getBean("alunoCPF");
                FacesUtils.removeBean("alunoCPF");
            }

            // VERIFICA SE HÁ ALGUM ALUNO NA SESSÃO PARA SER EDITADO, SE HOUVER SETA OS VALORES CORRESPONDENTES
            if (FacesUtils.getBean("aluno") != null) {
                aluno = (Aluno) FacesUtils.getBean("aluno");
                endereco = aluno.getEndereco();
                bairro = endereco.getBairro();
                municipio = bairro.getMunicipio();
                idMunicipio = municipio.getId();
                listarBairroPorMunicipio();
                editandoALuno = true;
                pais = aluno.getPaisDeOrigem().getId();
                FacesUtils.removeBean("aluno");
            }
        } catch (Exception e) {
            FacesUtils.addInfoMessage("Erro ao tentar editar aluno.");
        }

        listarTurmasAtivas();
    }

    private void listarTurmasAtivas() {
        try {
            turmas = (ArrayList<Turma>) turmaDAO.listarTurmasIniciadas();
        } catch (DAOException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    private void listarMunicipio() {
        try {
            municipios = (ArrayList<Municipio>) municipioDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    private void listarBairroPorMunicipio() {
        try {
            bairros = (ArrayList<Bairro>) bairroDAO.listarBairroPorMunicipio(municipio);
        } catch (DAOException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    private void listarPaises() {
        try {
            paises = (ArrayList<Pais>) paisDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    public String verificarSeCpfCadastrado() {

        if (aluno.getCpf() != null && !aluno.getCpf().isEmpty()) {
            try {
                Aluno a = alunoDAO.pesquisarPorCPF(aluno.getCpf());
                if (a != null) {
                    FacesUtils.setBean("aluno", a);
                    return "matricula.xhtml" + "?faces-redirect=true";
                }
            } catch (DAOException e) {
                FacesUtils.setBean("alunoCPF", aluno);
                return "aluno.xhtml" + "?faces-redirect=true";
            }

        } else {
            FacesUtils.addWarnMessage("Informe o CPF!!!");
        }

        return null;
    }

    public void valueChanged(ValueChangeEvent event) {
        try {
            idMunicipio = Long.parseLong(event.getNewValue().toString());
            municipio.setId(idMunicipio);
            listarBairroPorMunicipio();
        } catch (NumberFormatException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    public String salvarAluno() {
        try {

            if (verificarSeOAlunoPodeSerCadastradoPelaIdade(aluno.getDataNascimento())) {

                UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
                aluno.setUnidade(u.getUnidade());

                endereco.setBairro(bairro);
                aluno.setEndereco(endereco);
                aluno.setPaisDeOrigem(new Pais(pais));

                if (aluno.getId() != null) {
                    alunoDAO.atualizar(aluno);
                    FacesUtils.addInfoMessageFlashScoped("Aluno atualizado com sucesso!!!");

                    //FAZENDO A MATRICULA NA TURMA
                    if (matricularNaTurma && turma.getId() != null) {
                        //verificar se a turma alcançou o seu termo final, realizar a operação se não alcançou
                        if (!turmaDAO.turmaAlcancouSeuTermoFinal(turma)) {
                            if (!turmaDAO.verificarSeAlunoJaEstaMatriculadoNaTurma(aluno, turma)) {
                                turma.getAlunos().add(aluno);
                                turmaDAO.atualizar(turma);
                                FacesUtils.addInfoMessageFlashScoped("Aluno matriculado na turma com sucesso!!!");

                                //FAZENDO A MATRÍCULA NO CURSO
                                Matricula matricula = new Matricula();
                                GregorianCalendar gc = new GregorianCalendar();
                                matricula.setDataMatricula(gc.getTime());
                                matricula.setAluno(aluno);
                                matricula.setCurso(turma.getModulo().getCurso());
                                matricula.setUnidade(u.getUnidade());
                                matricula.setStatus(true);
                                matriculaDAO.salvar(matricula);

                                //liberar memória
                                matricula = null;
                                gc = null;
                            } else {
                                FacesUtils.addWarnMessageFlashScoped("Este aluno já está matriculado na turma!!!");
                            }
                        } else {
                            FacesUtils.addWarnMessageFlashScoped("Não foi possível realizar a matrícula do aluno na turma, pois esta já terminou!!!");
                        }
                    }

                } else {
                    if (alunoDAO.cpfUnico(aluno.getCpf())) {
                        alunoDAO.salvar(aluno);
                        FacesUtils.addInfoMessageFlashScoped("Aluno salvo com sucesso!!!");

                        //FAZENDO A MATRICULA NA TURMA
                        if (matricularNaTurma && turma.getId() != null) {
                            //verificar se a turma alcançou o seu termo final, realizar a operação se não alcançou
                            if (!turmaDAO.turmaAlcancouSeuTermoFinal(turma)) {
                                if (!turmaDAO.verificarSeAlunoJaEstaMatriculadoNaTurma(aluno, turma)) {
                                    turma.getAlunos().add(aluno);
                                    turmaDAO.atualizar(turma);
                                    FacesUtils.addInfoMessageFlashScoped("Aluno matriculado na turma com sucesso!!!");

                                    //FAZENDO A MATRÍCULA NO CURSO
                                    Matricula matricula = new Matricula();
                                    GregorianCalendar gc = new GregorianCalendar();
                                    matricula.setDataMatricula(gc.getTime());
                                    matricula.setAluno(aluno);
                                    matricula.setCurso(turma.getModulo().getCurso());
                                    matricula.setUnidade(u.getUnidade());
                                    matricula.setStatus(true);
                                    matriculaDAO.salvar(matricula);

                                    //liberar memória
                                    matricula = null;
                                    gc = null;
                                } else {
                                    FacesUtils.addWarnMessageFlashScoped("Este aluno já está matriculado na turma!!!");
                                }

                            } else {
                                FacesUtils.addWarnMessageFlashScoped("Não foi possível realizar a matrícula do aluno na turma, pois esta já terminou!!!");
                            }
                        }
                    } else {
                        FacesUtils.addWarnMessageFlashScoped("O CPF de número " + aluno.getCpf() + " já está cadastrado!!!");
                    }
                }
                //liberar memória
                u = null;
            } else {
                FacesUtils.addWarnMessageFlashScoped("O aluno não pode ser cadastrado no sistema, pois não possui 16 anos completos e nem irá completá-lo pelos próximos 3 meses!!!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage() + ": " + e.getCause());
        }
        FacesUtils.removeBean("cpf");

        //liberar memória
        limparMemoria();
        return "aluno.xhtml" + "?faces-redirect=true";
    }

    private void limparForm() {
        aluno = new Aluno();
        aluno.setPossuiFilho(false);
        bairro = new Bairro();
        municipio = new Municipio();
        pais = 1l;
        endereco = new Endereco();
        turma = new Turma();
        idMunicipio = 0l;
        editandoALuno = false;
        matricularNaTurma = false;
    }

    public String cancelar() {
        return "aluno.xhtml" + "?faces-redirect=true";
    }

    public String incluirMatricula() {
        FacesUtils.setBean("idAluno", aluno.getId());
        return "matricula.xhtml" + "?faces-redirect=true";
    }

    public String editarAluno() {
        FacesUtils.setBean("aluno", aluno);
        return "aluno.xhtml" + "?faces-redirect=true";
    }

    public String enviarAlunoParaMatricula() {
        FacesUtils.setBean("aluno", aluno);
        return "matricula.xhtml" + "?faces-redirect=true";
    }

    public void salvarBairro() {
        try {
            bairro.setMunicipio(municipio);
            bairroDAO.salvar(bairro);
            cancelarBairro();
        } catch (DAOException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    public void cancelarBairro() {
        municipio = new Municipio();
        bairro = new Bairro();
    }

    /**
     * Método usado para liberar memória. Foi necessário adicionar este método
     * porque, possivelmente, está havendo vazamento de memória, fazendo com que
     * a aplicação pare de funcionar. Basicamente o método irá anular as
     * referências das variáveis, sinalizando para o Garbage Collector realizar
     * a coleta.
     */
    private void limparMemoria() {
        alunoDAO = null;
        municipioDAO = null;
        bairroDAO = null;
        turmaDAO = null;
        matriculaDAO = null;
        paisDAO = null;
        paises = null;
        municipios = null;
        bairros = null;
        turmas = null;
        aluno = null;
        endereco = null;
        municipio = null;
        bairro = null;
        pais = null;
        turma = null;
        idMunicipio = null;
    }

    /**
     * Ao sair da página executa o método @limparMemoria.
     */
    @PreDestroy
    private void saindoDaPagina() {
        limparMemoria();
    }

    /**
     * O sistema trabalha e cadastra pessoas que tenham 16 anos ou mais. Então,
     * o método em questão verifica se o aluno a ser cadastrado possui ou não os
     * 16 anos. Caso tenha 16 anos completos ou que, pelo menos, falte no máximo
     * 3 meses para completá-los o sistema irá permitir o seu cadastro, caso
     * contrário, o sistema emitirá uma mensagem de alerta não permitindo o seu
     * cadastro. O método não é tão rígido, neste sentido ele irá trabalhar apenas
     * com os anos e meses, desconsiderando os dias.
     *
     * @param dataDeNascimento variável que representa a data de nascimento do
     * aluno.
     *
     * @return o sistema retornará um valor booleano, se o valor for verdadeiro
     * o aluno terá 16 completos ou estará faltando no máximo 3 meses para
     * completá-lo, se o valor for falso, significa que o aluno não tem 16 anos
     * completos, ele não deve ser cadastrado no sistema.
     */
    public boolean verificarSeOAlunoPodeSerCadastradoPelaIdade(Date dataDeNascimento) {
        if (dataDeNascimento != null) {
            //variável que representa a data atual
            GregorianCalendar g1 = new GregorianCalendar();
            //variável que representa a data de nascimento do aluno
            GregorianCalendar g2 = new GregorianCalendar();
            g2.setTime(dataDeNascimento);
            //com essa operação se obtem a idade geral do aluno
            int idade = g1.get(GregorianCalendar.YEAR) - g2.get(GregorianCalendar.YEAR);
            //variável que representa o mês atual
            int mesAtual = g1.get(GregorianCalendar.MONTH) + 1;
            //variável que representa o mês de nascimento do aluno
            int mesDaIdade = g2.get(GregorianCalendar.MONTH) + 1;
            //variável usada para verificar se o aluno completou o ano completo ou se falta alguns meses para completá-lo
            int fracaoDeMeses = mesAtual - mesDaIdade;

            //se o aluno possui 17 anos ou mais o sistema irá retornar verdaderiro
            if (idade > 16) {
                return true;
            } //se o aluno possui 16 anos, é necessário saber se é 16 anos completos ou não
            else if (idade == 16) {
                //se fracaoDeMeses for igual a zero, o aluno completou o ano
                if (fracaoDeMeses == 0) {
                    return true;
                } //se fracaoDeMeses for maior que zero, o aluno completou o ano e possui mais alguns meses de vida
                else if (fracaoDeMeses > 0) {
                    return true;
                } //se fracaoDeMeses for menor que zero, o aluno não completou o ano, deve-se verificar quantos meses faltam para ele completar o ano
                else if (fracaoDeMeses < 0) {
                    //adiciona mais 3 (que foi o limite estabelecido pela regra de negócio) meses a data atual
                    int mesesAdionais = mesAtual + 3;
                    //se com os meses adionais foi possível chegar ou passar do mês de aniversário do aluno o método retornará verdadeiro, caso contrário falso
                    return mesesAdionais >= mesDaIdade;
                }
            } //se o aluno possui menos de 16 anos o sistema irá retorna falso
            else if (idade < 16) {
                return false;
            }
        }
        return false;
    }
//==========================================================================

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public ArrayList<Municipio> getMunicipios() {
        return municipios;
    }

    public ArrayList<Bairro> getBairros() {
        return bairros;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Long getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Long idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public boolean isEditandoALuno() {
        return editandoALuno;
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

    public boolean isMatricularNaTurma() {
        return matricularNaTurma;
    }

    public void setMatricularNaTurma(boolean matricularNaTurma) {
        this.matricularNaTurma = matricularNaTurma;
    }

    public void setTurmas(ArrayList<Turma> turmas) {
        this.turmas = turmas;
    }

    public ArrayList<Pais> getPaises() {
        return paises;
    }

    public Long getPais() {
        return pais;
    }

    public void setPais(Long pais) {
        this.pais = pais;
    }

}
