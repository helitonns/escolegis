package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.business.Loger;
import br.leg.alrr.cursos.business.TipoAcao;
import br.leg.alrr.cursos.model.Autorizacao;
import br.leg.alrr.cursos.model.Privilegio;
import br.leg.alrr.cursos.model.Sistema;
import br.leg.alrr.cursos.model.Unidade;
import br.leg.alrr.cursos.model.Usuario;
import br.leg.alrr.cursos.model.UsuarioComUnidade;
import br.leg.alrr.cursos.persistence.AutorizacaoDAO;
import br.leg.alrr.cursos.persistence.LogSistemaDAO;
import br.leg.alrr.cursos.persistence.PrivilegioDAO;
import br.leg.alrr.cursos.persistence.SistemaDAO;
import br.leg.alrr.cursos.persistence.UnidadeDAO;
import br.leg.alrr.cursos.persistence.UsuarioComUnidadeDAO;
import br.leg.alrr.cursos.persistence.UsuarioDAO;
import br.leg.alrr.cursos.util.DAOException;
import br.leg.alrr.cursos.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author heliton
 */
@Named
@ViewScoped
public class AutorizacaoMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AutorizacaoDAO autorizacaoDAO;

    @EJB
    private PrivilegioDAO permissaoDAO;

    @EJB
    private SistemaDAO sistemaDAO;

    @EJB
    private UsuarioDAO usuarioDAO;
    
    @EJB
    private UsuarioComUnidadeDAO usuarioComUnidadeDAO;
    
    @EJB
    private UnidadeDAO unidadeDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private ArrayList<Privilegio> permissoes;
    private ArrayList<Sistema> sistemas;
    private ArrayList<Usuario> usuarios;
    private ArrayList<Autorizacao> autorizacoes;
    private ArrayList<Unidade> unidades;

    private Autorizacao autorizacao;

    private Long idUsuario;
    private Long idSistema;
    private Long idPermissao;
    private Long idUnidade;

    private boolean removerAutorizacao;

    //==========================================================================
    @PostConstruct
    public void init() {
        limparForm();

        listarSistema();
        listarUsuarios();
        listarUnidades();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    private void listarSistema() {
        try {
            sistemas = (ArrayList<Sistema>) sistemaDAO.listarTodosAtivos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void carregarPermissoes(ValueChangeEvent event) {
        try {
            idSistema = Long.parseLong(event.getNewValue().toString());
            listarPermissoesPorSistema(new Sistema(idSistema));
        } catch (NumberFormatException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    private void listarPermissoesPorSistema(Sistema s) {
        try {
            permissoes = (ArrayList<Privilegio>) permissaoDAO.listarTodosAtivosPeloSistema(s);
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void listarPermissoesPorSistema(Long id) {
        try {
            permissoes = (ArrayList<Privilegio>) permissaoDAO.listarTodosAtivosPeloSistema(new Sistema(id));
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void listarUsuarios() {
        try {
            String[] s = FacesUtils.getURL().split("/");
            ArrayList<Usuario> usuariosDoSistema = (ArrayList<Usuario>) autorizacaoDAO.listarUsuariosQueTemPermissaoNoSistema(s[1]);
            ArrayList<Usuario> todosUsuariosAtivos = (ArrayList<Usuario>) usuarioDAO.listarTodosAtivos();
            
            for (Usuario u : todosUsuariosAtivos) {
                boolean encontrou = false;
                for (Usuario u2 : usuariosDoSistema) {
                    if (u.equals(u2)) {
                        encontrou = true;
                        break;
                    }
                }
                if (!encontrou) {
                    usuarios.add(u);
                }
            }
            
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void listarAutorizacoes() {
        try {
            String[] s = FacesUtils.getURL().split("/");
            autorizacoes = (ArrayList<Autorizacao>) autorizacaoDAO.listarTodasPorChaveDoSistema(s[1]);
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }
    
    private void listarUnidades() {
        try {
            unidades = (ArrayList<Unidade>) unidadeDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public String salvarAutorizacao() {
        try {
            autorizacao.setUsuario(new UsuarioComUnidade(idUsuario));
            autorizacao.setPrivilegio(new Privilegio(idPermissao));

            if (autorizacao.getId() != null) {
                autorizacaoDAO.atualizar(autorizacao);
                FacesUtils.addInfoMessageFlashScoped("Autorização atualizada com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método AutorizacaoMB.salvarAutorizacao() para atualizar a autorização "+ autorizacao.getId()+".");
            } else {
                if (!autorizacaoDAO.verificarSeHaAutorizacaoParaUsuarioNoSistema(autorizacao.getUsuario(), new Sistema(idSistema))) {
                    
                    //SALVAR A UNIDADE PARA O USUÁRIO AQUI
                    usuarioComUnidadeDAO.salvarUnidadeParaUsuario(idUsuario, idUnidade);
                    //_________________________________
                    
                    autorizacaoDAO.salvar(autorizacao);
                    FacesUtils.addInfoMessageFlashScoped("Autorização salva com sucesso!");
                    Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método AutorizacaoMB.salvarAutorizacao() para salvar a autorização "+ autorizacao.getId()+".");
                } else {
                    FacesUtils.addWarnMessageFlashScoped("O usuário já possui autorização no sistema!");
                }
            }
            limparForm();
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
            System.out.println(e.getCause());
        }
        return "autorizacao.xhtml" + "?faces-redirect=true";
    }

    public void removerAutorizacao() {
        try {
            if (removerAutorizacao) {
                autorizacaoDAO.remover(autorizacao);
                FacesUtils.addInfoMessage("Autorização removida com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método AutorizacaoMB.removerAutorizacao() para excluir a autorização "+ autorizacao.getId()+".");
            }
            limparForm();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void limparForm() {
        idUsuario = 0l;
        idSistema = 0l;
        idPermissao = 0l;

        usuarios = new ArrayList<>();
        sistemas = new ArrayList<>();
        permissoes = new ArrayList<>();
        autorizacoes = new ArrayList<>();
        unidades = new ArrayList<>();

        autorizacao = new Autorizacao();
        autorizacao.setStatus(true);

        removerAutorizacao = false;
        listarAutorizacoes();
    }

    public String cancelar() {
        return "autorizacao.xhtml" + "?faces-redirect=true";
    }

    //==========================================================================
    public ArrayList<Sistema> getSistemas() {
        return sistemas;
    }

    public ArrayList<Privilegio> getPermissoes() {
        return permissoes;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public Long getIdSistema() {
        return idSistema;
    }

    public void setIdSistema(Long idSistema) {
        this.idSistema = idSistema;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdPermissao() {
        return idPermissao;
    }

    public void setIdPermissao(Long idPermissao) {
        this.idPermissao = idPermissao;
    }

    public Autorizacao getAutorizacao() {
        return autorizacao;
    }

    public void setAutorizacao(Autorizacao autorizacao) {
        this.autorizacao = autorizacao;
    }

    public boolean isRemoverAutorizacao() {
        return removerAutorizacao;
    }

    public void setRemoverAutorizacao(boolean removerAutorizacao) {
        this.removerAutorizacao = removerAutorizacao;
    }

    public ArrayList<Autorizacao> getAutorizacoes() {
        return autorizacoes;
    }

    public Long getIdUnidade() {
        return idUnidade;
    }

    public void setIdUnidade(Long idUnidade) {
        this.idUnidade = idUnidade;
    }

    public ArrayList<Unidade> getUnidades() {
        return unidades;
    }

    
}
