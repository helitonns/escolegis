/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.leg.alrr.cursos.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class Relatorio<T> {

    private HttpServletResponse response;
    private FacesContext context;
    private ByteArrayOutputStream baos;
    private InputStream stream;
    private InputStream subreport;
    private InputStream imagem1;
    private InputStream imagem2;
    private Connection con;
    private String texto;

    public Relatorio() {
        this.context = FacesContext.getCurrentInstance();
        this.response = (HttpServletResponse) context.getExternalContext().getResponse();

    }

    /*
    defina um parametro: List<Objeto> lista, se usar JavaBean DataSource
     */
    public void getRelatorio(List<T> lista) {

        stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/matriculados.jasper");
        imagem1 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-escolegis.png");
        
        
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("logo-escolegis", imagem1);
        baos = new ByteArrayOutputStream();

        try {
        	
  
       	 
       	/*--------------------------------------------------------------------------------------*/
        	
       	 /* Relatório*/
        	
            JRBeanCollectionDataSource datasrc = new JRBeanCollectionDataSource(lista);
            params.put("ItemDataSource", datasrc);
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);

            /*Para usar JavaBeanDataSource defina: new JRBeanCollectionDataSource(lista)
            mude a string do getResourceAsStream("/report/reportPessoaJavaBeanDS.jasper")
             */
            JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=relatorio.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

            //fecharConexao();
        } catch (JRException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar o relatorio!"));
        }
    }
    
    public void getRelatorioLegado(List<T> lista) {

        stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/matriculadosLegado.jasper");
        imagem1 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-escolegis.png");
        
        
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("logo-escolegis", imagem1);
        baos = new ByteArrayOutputStream();

        try {
        	
  
       	 
       	/*--------------------------------------------------------------------------------------*/
        	
       	 /* Relatório*/
        	
            JRBeanCollectionDataSource datasrc = new JRBeanCollectionDataSource(lista);
            params.put("ItemDataSource", datasrc);
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);

            /*Para usar JavaBeanDataSource defina: new JRBeanCollectionDataSource(lista)
            mude a string do getResourceAsStream("/report/reportPessoaJavaBeanDS.jasper")
             */
            JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=relatorio.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

            //fecharConexao();
        } catch (JRException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar o relatorio!"));
        }
    }
    
    
    public void getFrequencia(List<T> lista) {

        stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/frequencia.jasper");
        subreport = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/subreport.jasper");
        imagem1 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-escolegis.png");
        imagem2 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-action.png");
        
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("logo-escolegis", imagem1);
        params.put("logo-action", imagem2);
        
        baos = new ByteArrayOutputStream();

        try {	
        	
        	
        	
        	/* Cabeçalho da Pagina - Subreport*/
        	
        	
        	 JRBeanCollectionDataSource dsc = new JRBeanCollectionDataSource(lista);
        	 params.put("cabecalho", dsc);
        	 
        	/*--------------------------------------------------------------------------------------*/
        	 
        	 /* Lista de Frequência */
        	 
            JRBeanCollectionDataSource datasrc = new JRBeanCollectionDataSource(lista);
            params.put("ItemDataSource", datasrc);
            params.put("subreport", subreport);
            
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);

            /*Para usar JavaBeanDataSource defina: new JRBeanCollectionDataSource(lista)
            mude a string do getResourceAsStream("/report/reportPessoaJavaBeanDS.jasper")
             */
            JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=frequencia.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

            //fecharConexao();
        } catch (JRException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar frequência!"));
        }
    }
    
    
    public void getFrequencia2(List<T> lista) {

        stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/frequencia2.jasper");
        subreport = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/subreport2.jasper");
        imagem1 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-escolegis.png");
        imagem2 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-action.png");
        
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("logo-escolegis", imagem1);
        params.put("logo-action", imagem2);
        
        baos = new ByteArrayOutputStream();

        try {	
        	
        	
        	
        	/* Cabeçalho da Pagina - Subreport*/
        	
        	
        	 JRBeanCollectionDataSource dsc = new JRBeanCollectionDataSource(lista);
        	 params.put("cabecalho", dsc);
        	 
        	/*--------------------------------------------------------------------------------------*/
        	 
        	 /* Lista de Frequência */
        	 
            JRBeanCollectionDataSource datasrc = new JRBeanCollectionDataSource(lista);
            params.put("ItemDataSource", datasrc);
            params.put("subreport2", subreport);
            
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);

            /*Para usar JavaBeanDataSource defina: new JRBeanCollectionDataSource(lista)
            mude a string do getResourceAsStream("/report/reportPessoaJavaBeanDS.jasper")
             */
            JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=frequencia.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

            //fecharConexao();
        } catch (JRException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar frequência!"));
        }
    }
    
    /*
    public Connection getConexao(){        
        try {            
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_estudo_java", "root", "root");
            return con;
            
        } catch (SQLException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return con;
    }
    
    public void fecharConexao(){
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     */
}
