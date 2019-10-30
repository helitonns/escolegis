package br.leg.alrr.cursos.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class FacesUtils {

    private FacesUtils() {
    }

    public static void addErrorMessage(String msg) {
        addErrorMessage(null, msg);
    }
    
    public static void addErrorMessageFlashScoped(String msg) {
        addErrorMessage(null, msg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    public static void addErrorMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
    }

    public static void addInfoMessage(String msg) {
        addInfoMessage(null, msg);
    }
    
    public static void addInfoMessageFlashScoped(String msg) {
        addInfoMessage(null, msg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    public static void addInfoMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
    }

    public static void addWarnMessage(String msg) {
        addWarnMessage(null, msg);
    }
    
    public static void addWarnMessageFlashScoped(String msg) {
        addWarnMessage(null, msg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    public static void addWarnMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg));
    }

    public static Object getBean(String nome) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(nome);
    }

    public static void setBean(String mb, Object o) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(mb, o);
    }

    public static void removeBean(String bean) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(bean);
    }

    public static String getIP() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getRemoteAddr();
    }
    
    public static String getURL() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        return request.getRequestURI();
    }
}
