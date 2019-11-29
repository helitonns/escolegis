package br.leg.alrr.cursos.controller;

import br.leg.alrr.cursos.model.Acesso;
import br.leg.alrr.cursos.persistence.AcessoDAO;
import br.leg.alrr.cursos.util.DAOException;
import br.leg.alrr.cursos.util.FacesUtils;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author heliton
 */
@Named
@ViewScoped
public class EstatisticaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AcessoDAO acessoDAO;

    private ArrayList<Acesso> acessos;
    private ArrayList<HoraQuantidade> horasQuantidades;

    private Long quantidadeDeAcesso;

    private LocalDate dataParaPesquisa;
    
    private LineChartModel grafico;

    //==========================================================================
    @PostConstruct
    public void init() {
        limparForm();

        listarAcessosDoDia();
        
        criarAsHorasDoDia();
        contarOsAcessosParaCadaHora();
        criarGrafico();
    }

    private void contarAcessosDoDia() {
        try {
            quantidadeDeAcesso = acessoDAO.contarAcessosPorData(LocalDate.now());
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void listarAcessosDoDia() {
        try {
            acessos = null;
            acessos = new ArrayList<>();
            horasQuantidades = new ArrayList<>();
            acessos = (ArrayList<Acesso>) acessoDAO.listarAcessosPorData(dataParaPesquisa);
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void limparForm() {
        dataParaPesquisa = LocalDate.now();
    }

    public String cancelar() {
        return "estatistica.xhtml" + "?faces-redirect=true";
    }

    //==========================================================================
    // MÉTODOS PARA CRIAR O GRÁFICO
    private void criarAsHorasDoDia() {
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(7, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(8, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(9, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(10, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(11, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(12, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(13, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(14, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(15, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(16, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(17, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(18, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(19, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(20, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(21, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(22, 0), 0l));
        horasQuantidades.add(new HoraQuantidade(LocalTime.of(23, 0), 0l));
    }

    private void contarOsAcessosParaCadaHora() {
        for (HoraQuantidade hq : horasQuantidades) {
            for (Acesso a : acessos) {
                if (a.getMomentoDoAcesso().getHour() == hq.hora.getHour()) {
                    hq.setQuantidade(hq.getQuantidade() + 1);
                }
            }
        }
        
        ArrayList<HoraQuantidade> horasParaExcluir = new ArrayList<>();
        
        for (HoraQuantidade hq : horasQuantidades) {
            if (hq.getQuantidade() == 0) {
                horasParaExcluir.add(hq);
            }
        }
        
        horasQuantidades.removeAll(horasParaExcluir);
    }

    private LineChartModel popularGrafico() {
        LineChartModel model = new LineChartModel();
        ChartSeries acessos = new ChartSeries();
        acessos.setLabel("acessos");

        for (HoraQuantidade hq : horasQuantidades) {
            acessos.set(hq.getHora().toString(), hq.getQuantidade());
        }
        
        model.addSeries(acessos);
        return model;
    }
    
    private void criarGrafico() {
        grafico = popularGrafico();
        grafico.setTitle("Acessos do dia");
        grafico.setLegendPosition("e");
        grafico.setShowPointLabels(true);
        grafico.getAxes().put(AxisType.X, new CategoryAxis("Horas"));
        
        Axis yAxis = grafico.getAxis(AxisType.Y);
        yAxis.setLabel("Quantidade");
        yAxis.setMin(0);
        yAxis.setTickAngle(5);
        yAxis.setTickCount(5);
    }
    //==========================================================================

    /**
     * Método usado para liberar memória. Foi necessário adicionar este método
     * porque, possivelmente, está havendo vazamento de memória, fazendo com que
     * a aplicação pare de funcionar. Basicamente o método irá anular as
     * referências das variáveis, sinalizando para o Garbage Collector realizar
     * a coleta.
     */
    private void limparMemoria() {
        acessoDAO = null;
        acessos = null;
        quantidadeDeAcesso = null;
        dataParaPesquisa = null;
        horasQuantidades = null;
    }

    /**
     * Ao sair da página executa o método @limparMemoria.
     */
    @PreDestroy
    public void saindoDaPagina() {
        limparMemoria();
    }

    //==========================================================================
    public ArrayList<Acesso> getAcessos() {
        return acessos;
    }

    public Long getQuantidadeDeAcesso() {
        return quantidadeDeAcesso;
    }

    public LocalDate getDataParaPesquisa() {
        return dataParaPesquisa;
    }

    public void setDataParaPesquisa(LocalDate dataParaPesquisa) {
        this.dataParaPesquisa = dataParaPesquisa;
    }

    public LineChartModel getGrafico() {
        return grafico;
    }
    //==========================================================================
    private class HoraQuantidade {

        public LocalTime hora;
        public Long quantidade;

        public HoraQuantidade() {
        }

        public HoraQuantidade(LocalTime hora, Long quantidade) {
            this.hora = hora;
            this.quantidade = quantidade;
        }

        public LocalTime getHora() {
            return hora;
        }

        public void setHora(LocalTime hora) {
            this.hora = hora;
        }

        public Long getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(Long quantidade) {
            this.quantidade = quantidade;
        }

    }

}
