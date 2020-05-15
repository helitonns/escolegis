package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.business.Loger;
import br.leg.alrr.cursos.business.TipoAcao;
import br.leg.alrr.cursos.model.Mensagem;
import br.leg.alrr.cursos.persistence.LogSistemaDAO;
import br.leg.alrr.cursos.persistence.MensagemDAO;
import br.leg.alrr.cursos.util.DAOException;
import br.leg.alrr.cursos.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * Classe de gerenciamento das regras de negócio para a entidade Mensagem.
 *
 * @author Heliton Nascimento
 * @since 2020-02-07
 * @version 1.0
 * @see Mensagem
 * @see MensagemDAO
 */
@ViewScoped
@Named
public class MensagemMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private MensagemDAO mensagemDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;
    
    private List<Mensagem> mensagens;

    private Mensagem mensagem;
    
    private boolean excluirMensagem;


    //==========================================================================
    @PostConstruct
    public void init() {
        iniciar();
        listarTodasAsMensgens();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    private void iniciar() {
        excluirMensagem = false;

        mensagem = new Mensagem();
        mensagem.setStatus(true);
        
        mensagens = new ArrayList<>();
    }

    @PreDestroy
    public void finalizar() {
        mensagem = null;
        mensagens = null;
    }

    public String salvarMensagem() {
        try {
            if (mensagem.getId() != null) {
                mensagemDAO.atualizar(mensagem);
                FacesUtils.addInfoMessageFlashScoped("Mensagem atualizada com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método MensagemMB.salvarMensagem() para atualizar a mensagem "+ mensagem.getId()+".");
            } else {
                mensagemDAO.salvar(mensagem);
                FacesUtils.addInfoMessageFlashScoped("Mensagem salva com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método MensagemMB.salvarMensagem() para salvr a mensagem "+ mensagem.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
            System.out.println(e.getCause());
        }
        return "mensagem.xhtml" + "?faces-redirect=true";
    }

    private void listarTodasAsMensgens() {
        try {
            mensagens = mensagemDAO.listarTodos();
        } catch (NullPointerException | DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }
    

    public String excluirMensagem() {
        try {
            if (excluirMensagem) {
                mensagemDAO.remover(mensagem);
                FacesUtils.addInfoMessageFlashScoped("Mensagem excluída com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método MensagemMB.excluirMensagem() para apagar a mensagem "+ mensagem.getId()+".");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        return "mensagem.xhtml" + "?faces-redirect=true";
    }
    
    public String cancelar() {
        return "mensagem.xhtml" + "?faces-redirect=true";
    }
    
    //==========================================================================

    public Mensagem getMensagem() {
        return mensagem;
    }

    public void setMensagem(Mensagem mensagem) {
        this.mensagem = mensagem;
    }

    public List<Mensagem> getMensagens() {
        return mensagens;
    }
    
    public boolean isExcluirMensagem() {
        return excluirMensagem;
    }

    public void setExcluirMensagem(boolean excluirMensagem) {
        this.excluirMensagem = excluirMensagem;
    }
    
    
}
