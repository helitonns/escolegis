package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.model.Bairro;
import br.leg.alrr.cursos.business.BlocoConsulta;
import br.leg.alrr.cursos.business.BlocoParametro;
import br.leg.alrr.cursos.business.Loger;
import br.leg.alrr.cursos.business.Sexo;
import br.leg.alrr.cursos.business.TipoAcao;
import br.leg.alrr.cursos.model.Curso;
import br.leg.alrr.cursos.model.Matricula;
import br.leg.alrr.cursos.model.Municipio;
import br.leg.alrr.cursos.model.Pais;
import br.leg.alrr.cursos.model.UsuarioComUnidade;
import br.leg.alrr.cursos.persistence.BairroDAO;
import br.leg.alrr.cursos.persistence.CursoDAO;
import br.leg.alrr.cursos.persistence.LogSistemaDAO;
import br.leg.alrr.cursos.persistence.MatriculaDAO;
import br.leg.alrr.cursos.persistence.MunicipioDAO;
import br.leg.alrr.cursos.persistence.PaisDAO;
import br.leg.alrr.cursos.util.DAOException;
import br.leg.alrr.cursos.util.FacesUtils;
import br.leg.alrr.cursos.util.Relatorio;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author heliton
 */
@Named
@ViewScoped
public class RelatorioMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private MatriculaDAO matriculaDAO;

    @EJB
    private BairroDAO bairroDAO;

    @EJB
    private MunicipioDAO municipioDAO;

    @EJB
    private CursoDAO cursoDAO;

    @EJB
    private PaisDAO paisDAO;

    @EJB
    private LogSistemaDAO logSistemaDAO;

    private InputStream logo;

    private InputStream jasper;

    private Date naData = new Date();
    private Date data1 = new Date();
    private Date data2 = new Date();
    private Date antesDaData = new Date();
    private Date depoisDaData = new Date();
    private String cpf = "";
    private List<SelectItem> parametros;
    private List<SelectItem> conectivos;
    private int parametroEscolhido = 0;
    private String conectivoEscolhido = "";
    private boolean simNao = false;
    private String sexo = "";
    private int idade = 0;

    private ArrayList<BlocoConsulta> blocos;
    private ArrayList<BlocoParametro> blocosParametros;
    private BlocoConsulta itemEscolhido = new BlocoConsulta();

    private ArrayList<Matricula> matriculas;
    private ArrayList<Bairro> bairros;
    private ArrayList<Municipio> municipios;
    private ArrayList<Curso> cursos;
    private ArrayList<Pais> paises;

    private Long idMS = 0l;
    private Long idBS = 0l;
    private Long idCS = 0l;
    private Long idPS = 0l;

    private Sexo sexoLista;
//==========================================================================

    @PostConstruct
    public void init() {
        sexoLista = Sexo.MASCULINO;

        parametros = new ArrayList<>();
        blocos = new ArrayList<>();
        blocosParametros = new ArrayList<>();
        conectivos = new ArrayList<>();

        parametros.add(new SelectItem(0, "Na data"));
        parametros.add(new SelectItem(1, "Antes da data"));
        parametros.add(new SelectItem(2, "Depois da data"));
        parametros.add(new SelectItem(3, "Intervalo entre datas 1"));
        parametros.add(new SelectItem(4, "Intervalo entre datas 2"));
        parametros.add(new SelectItem(5, "CPF"));
        parametros.add(new SelectItem(6, "Data de nascimento"));
        parametros.add(new SelectItem(7, "Data de nascimento intervalo 1"));
        parametros.add(new SelectItem(8, "Data de nascimento intervalo 2"));
        parametros.add(new SelectItem(9, "Sexo"));
        parametros.add(new SelectItem(10, "Possui filho(a)?"));
        parametros.add(new SelectItem(11, "Ainda cursando?"));
        parametros.add(new SelectItem(12, "Curso"));
        parametros.add(new SelectItem(13, "Município"));
        parametros.add(new SelectItem(14, "Bairro"));
        parametros.add(new SelectItem(15, "País"));
        parametros.add(new SelectItem(16, "Com idade igual ou maior"));
        parametros.add(new SelectItem(17, "Com idade igual ou menor"));
        parametros.add(new SelectItem(18, "Com idade intervalo 1"));
        parametros.add(new SelectItem(19, "Com idade intervalo 2"));
        //parametros.add(new SelectItem(20, "Data de cadastro no sistema"));

        conectivos.add(new SelectItem("-", "-"));
        conectivos.add(new SelectItem("E", "E"));
        conectivos.add(new SelectItem("OU", "OU"));

        jasper = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/matriculados.jasper");
        logo = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-rel-mat.png");

        listarMunicipio();
        listarCurso();
        listarPaises();

        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL() + ".");
    }

    public void incluirNaConsulta() {
        BlocoConsulta b = new BlocoConsulta();
        b.setConectivo(conectivoEscolhido);
        conectivoEscolhido = "-";
        for (SelectItem si : parametros) {
            int i = 0;
            if (!si.getValue().toString().equals("")) {
                i = Integer.parseInt(si.getValue().toString());
            }
            if (i == parametroEscolhido) {
                b.setTipo(si);
            }
        }

        switch (parametroEscolhido) {
            case 0:
                b.setValor1(naData);
                naData = new Date();
                break;
            case 1:
                b.setValor1(antesDaData);
                antesDaData = new Date();
                break;
            case 2:
                b.setValor1(depoisDaData);
                depoisDaData = new Date();
                break;
            case 3:
                b.setValor1(data1);
                data1 = new Date();
                break;
            case 4:
                b.setValor1(data2);
                data1 = new Date();
                break;
            case 5:
                b.setValor1(cpf);
                cpf = "";
                break;
            //data de nascimento
            case 6:
                b.setValor1(naData);
                naData = new Date();
                break;
            //data de nascimento intervalo 1
            case 7:
                b.setValor1(data1);
                data1 = new Date();
                break;
            //data de nascimento intervalo 2
            case 8:
                b.setValor1(data2);
                data2 = new Date();
                break;
            case 9:
                b.setValor1(Sexo.fromValue(sexo));
                sexo = "";
                break;
            //possui filho
            case 10:
                b.setValor1(simNao);
                simNao = false;
                break;
            //ainda cursando
            case 11:
                b.setValor1(simNao);
                simNao = false;
                break;
            case 12:
                b.setValor1(idCS);
                b.setTexto(nomeDoCursoSelecionado());
                break;
            case 13:
                b.setValor1(idMS);
                b.setTexto(nomeDoMunicipioSelecionado());
                break;
            case 14:
                b.setValor1(idBS);
                b.setTexto(nomeDoBairroSelecionado());
                idBS = 0l;
                break;
            case 15:
                b.setValor1(idPS);
                b.setTexto(nomeDoPaisSelecionado());
                idPS = 0l;
                break;
            case 16:
                b.setValor1(idade);
                idade = 0;
                break;
            case 17:
                b.setValor1(idade);
                idade = 0;
                break;
            case 18:
                b.setValor1(idade);
                idade = 0;
                break;
            case 19:
                b.setValor1(idade);
                idade = 0;
                break;
            case 20:
                b.setValor1(naData);
                naData = new Date();
                break;
        }
        blocos.add(b);

        Loger.registrar(logSistemaDAO, TipoAcao.EXECUTAR, "O usuário executou o método RelatorioMB.incluirNaConsulta().");
    }

    public void montarConsulta() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            StringBuilder sb = new StringBuilder();

            sb.append("select m from Matricula m where m.unidade.id = ").append(u.getUnidade().getId()).append(" and ");
            for (BlocoConsulta b : blocos) {
                Integer i = (Integer) b.getTipo().getValue();
                BlocoParametro bp = new BlocoParametro();

                switch (i) {
                    case 0:
                        sb.append("m.dataMatricula >=:naData and m.dataMatricula <=:naData2 ");
                        bp.setParametro("naData");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;
                    case 1:
                        sb.append("m.dataMatricula <:antesDaData ");
                        bp.setParametro("antesDaData");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;
                    case 2:
                        sb.append("m.dataMatricula >:depoisDaData ");
                        bp.setParametro("depoisDaData");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;
                    case 3:
                        sb.append("m.dataMatricula >=:intervalo1 ");
                        bp.setParametro("intervalo1");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;
                    case 4:
                        sb.append("m.dataMatricula <=:intervalo2 ");
                        bp.setParametro("intervalo2");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;
                    case 5:
                        sb.append("m.aluno.cpf =:cpf ");
                        bp.setParametro("cpf");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;
                    case 6:
                        sb.append("DAY(m.aluno.dataNascimento) = :dia AND MONTH (m.aluno.dataNascimento) = :mes ");
                        bp.setParametro("dataNascimento");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;
                    case 7:
                        sb.append("DAY(m.aluno.dataNascimento) >= :dia1 AND MONTH (m.aluno.dataNascimento) >= :mes1 ");
                        bp.setParametro("dataNascimento1");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;
                    case 8:
                        sb.append("DAY(m.aluno.dataNascimento) <= :dia2 AND MONTH (m.aluno.dataNascimento) <= :mes2 ");
                        bp.setParametro("dataNascimento2");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;
                    case 9:
                        sb.append("m.aluno.sexo = :sexo ");
                        bp.setParametro("sexo");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;
                    case 10:
                        sb.append("m.aluno.possuiFilho =:possuiFilho ");
                        bp.setParametro("possuiFilho");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;
                    case 11:
                        sb.append("m.status =:aindaCursando ");
                        bp.setParametro("aindaCursando");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;
                    case 12:
                        sb.append("m.curso.id =:idCurso ");
                        bp.setParametro("idCurso");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;
                    case 13:
                        sb.append("m.aluno.endereco.bairro.municipio.id =:idMunicipio ");
                        bp.setParametro("idMunicipio");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;
                    case 14:
                        sb.append("m.aluno.endereco.bairro.id =:idBairro ");
                        bp.setParametro("idBairro");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;
                    case 15:
                        sb.append("m.aluno.paisDeOrigem.id =:idPais ");
                        bp.setParametro("idPais");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;
                    case 16:
                        sb.append("YEAR(m.aluno.dataNascimento) <= :ano ");
                        bp.setParametro("ano");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;
                    case 17:
                        sb.append("YEAR(m.aluno.dataNascimento) >= :ano ");
                        bp.setParametro("ano");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;
                    case 18:
                        sb.append("YEAR(m.aluno.dataNascimento) <= :ano1 ");
                        bp.setParametro("ano1");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;
                    case 19:
                        sb.append("YEAR(m.aluno.dataNascimento) >= :ano2 ");
                        bp.setParametro("ano2");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;
                    case 20:
                        sb.append("m.aluno.dataDeCadastro >=:naData and m.aluno.dataDeCadastro <=:naData2 ");
                        bp.setParametro("naData");
                        bp.setValor(b.getValor1());
                        blocosParametros.add(bp);
                        break;

                    default:
                        break;
                }
                sb.append(b.getConectivoParaConsulta());
            }

            sb.append("order by m.curso.nome, m.aluno.nome");

            matriculas = (ArrayList<Matricula>) matriculaDAO.gerarRelatorioDinamico(sb.toString(), blocosParametros);

            blocosParametros = new ArrayList<>();
            blocos = new ArrayList<>();

            limparVariaveis();
            Loger.registrar(logSistemaDAO, TipoAcao.EXECUTAR, "O usuário executou o método RelatorioMB.montarConsulta().");
        } catch (DAOException e) {
            FacesUtils.addErrorMessage("Erro ao montar consulta: " + e.getCause());
        }
    }

    public String exportarPDF() {
        try {
            Relatorio<Matricula> report = new Relatorio<>();

            if (matriculas == null) {
                try {
                    montarConsulta();
                    if (matriculas.isEmpty()) {
                        FacesUtils.addWarnMessage("Não há registros!");
                    } else {
                        report.getRelatorio(matriculas);
                        limparBlocosDeConculta();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                report.getRelatorio(matriculas);
                limparBlocosDeConculta();
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Erro ao gerar relatório: " + e.getMessage());
        }

        Loger.registrar(logSistemaDAO, TipoAcao.EXECUTAR, "O usuário executou o método RelatorioGeralMB.exportarPDF() para atualizar o país ");
        return "relatorio";
    }

    public void removerDaConsulta() {
        blocos.remove(itemEscolhido);
        itemEscolhido = new BlocoConsulta();
    }

    public void limparBlocosDeConculta() {
        blocos = new ArrayList<>();
        matriculas = new ArrayList<>();
    }

    public void valueChanged(ValueChangeEvent event) {
        idMS = Long.parseLong(event.getNewValue().toString());
        listarBairroPorMunicipio();
    }

    private void listarMunicipio() {
        try {
            municipios = (ArrayList<Municipio>) municipioDAO.listarTodos();
        } catch (DAOException ex) {
            Logger.getLogger(RelatorioMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listarBairroPorMunicipio() {
        Municipio m = new Municipio(idMS);
        try {
            bairros = (ArrayList<Bairro>) bairroDAO.listarBairroPorMunicipio(m);
        } catch (DAOException ex) {
            Logger.getLogger(RelatorioMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listarCurso() {
        try {
            cursos = (ArrayList<Curso>) cursoDAO.listarCursosAtivos();
        } catch (DAOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listarPaises() {
        try {
            paises = (ArrayList<Pais>) paisDAO.listarTodos();
        } catch (DAOException ex) {
            Logger.getLogger(RelatorioMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String reset() {
        return "relatorio.xhtml" + "?faces-redirect=true";
    }

    private void limparVariaveis() {
        idMS = 0l;
        idBS = 0l;
        idCS = 0l;
    }

    private String nomeDoCursoSelecionado() {
        for (Curso c : cursos) {
            if (c.getId().equals(idCS)) {
                return c.getNome();
            }
        }
        return "esclhido";
    }

    private String nomeDoMunicipioSelecionado() {
        for (Municipio m : municipios) {
            if (m.getId().equals(idMS)) {
                return m.getNome();
            }
        }
        return "esclhido";
    }

    private String nomeDoBairroSelecionado() {
        for (Bairro b : bairros) {
            if (b.getId().equals(idBS)) {
                return b.getNome();
            }
        }
        return "esclhido";
    }

    private String nomeDoPaisSelecionado() {
        for (Pais p : paises) {
            if (p.getId().equals(idPS)) {
                return p.getNome();
            }
        }
        return "esclhido";
    }
//==========================================================================

    public Date getData1() {
        return data1;
    }

    public void setData1(Date data1) {
        this.data1 = data1;
    }

    public Date getData2() {
        return data2;
    }

    public void setData2(Date data2) {
        this.data2 = data2;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public List<SelectItem> getParametros() {
        return parametros;
    }

    public Date getAntesDaData() {
        return antesDaData;
    }

    public void setAntesDaData(Date antesDaData) {
        this.antesDaData = antesDaData;
    }

    public Date getDepoisDaData() {
        return depoisDaData;
    }

    public void setDepoisDaData(Date depoisDaData) {
        this.depoisDaData = depoisDaData;
    }

    public int getParametroEscolhido() {
        return parametroEscolhido;
    }

    public void setParametroEscolhido(int parametroEscolhido) {
        this.parametroEscolhido = parametroEscolhido;
    }

    public boolean isSimNao() {
        return simNao;
    }

    public void setSimNao(boolean simNao) {
        this.simNao = simNao;
    }

    public ArrayList<BlocoConsulta> getBlocos() {
        return blocos;
    }

    public BlocoConsulta getItemEscolhido() {
        return itemEscolhido;
    }

    public void setItemEscolhido(BlocoConsulta itemEscolhido) {
        this.itemEscolhido = itemEscolhido;
    }

    public boolean getBlocosSize() {
        return blocos.size() >= 1;
    }

    public List<SelectItem> getConectivos() {
        return conectivos;
    }

    public String getConectivoEscolhido() {
        return conectivoEscolhido;
    }

    public void setConectivoEscolhido(String conectivoEscolhido) {
        this.conectivoEscolhido = conectivoEscolhido;
    }

    public Date getNaData() {
        return naData;
    }

    public void setNaData(Date naData) {
        this.naData = naData;
    }

    public ArrayList<Matricula> getMatriculas() {
        return matriculas;
    }

    public void setMatriculas(ArrayList<Matricula> matriculas) {
        this.matriculas = matriculas;
    }

    public Long getIdMS() {
        return idMS;
    }

    public void setIdMS(Long idMS) {
        this.idMS = idMS;
    }

    public Long getIdBS() {
        return idBS;
    }

    public void setIdBS(Long idBS) {
        this.idBS = idBS;
    }

    public ArrayList<Bairro> getBairros() {
        return bairros;
    }

    public ArrayList<Municipio> getMunicipios() {
        return municipios;
    }

    public Long getIdCS() {
        return idCS;
    }

    public void setIdCS(Long idCS) {
        this.idCS = idCS;
    }

    public ArrayList<Curso> getCursos() {
        return cursos;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Sexo getSexoLista() {
        return sexoLista;
    }

    public void setSexoLista(Sexo sexoLista) {
        this.sexoLista = sexoLista;
    }

    public ArrayList<Pais> getPaises() {
        return paises;
    }

    public Long getIdPS() {
        return idPS;
    }

    public void setIdPS(Long idPS) {
        this.idPS = idPS;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

}
