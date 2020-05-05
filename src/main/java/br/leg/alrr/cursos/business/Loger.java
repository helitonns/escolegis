package br.leg.alrr.cursos.business;

import br.leg.alrr.cursos.model.LogSistema;
import br.leg.alrr.cursos.model.UsuarioComUnidade;
import br.leg.alrr.cursos.persistence.LogSistemaDAO;
import br.leg.alrr.cursos.util.FacesUtils;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 *
 * @author Heliton
 */
public class Loger {

    private static final ZoneId ZONE1 = ZoneId.of("America/Boa_Vista");

    public static void registrar(LogSistemaDAO logSistemaDAO, TipoAcao t, String mensagenEntrada) {
        try {
            LogSistema log = new LogSistema();
            log.setUsuario((UsuarioComUnidade) FacesUtils.getBean("usuario"));
            log.setDataOperacao(LocalDateTime.now(ZONE1));
            log.setTipoAcao(t);
            log.setMensagemDeEntrada(mensagenEntrada);
            logSistemaDAO.salvar(log);
        } catch (Exception e) {
            System.out.println("Causa: "+e.getCause());
            System.out.println("Mensagem: "+e.getMessage());
            System.out.println("To String: "+e.toString());
            e.printStackTrace();
        }
    }
    
    public static void registrar(LogSistemaDAO logSistemaDAO, TipoAcao t, String mensagenEntrada, String mensagemSaida) {
        try {
            LogSistema log = new LogSistema();
            log.setUsuario((UsuarioComUnidade) FacesUtils.getBean("usuario"));
            log.setDataOperacao(LocalDateTime.now(ZONE1));
            log.setTipoAcao(t);
            log.setMensagemDeEntrada(mensagenEntrada);
            log.setMensagemDeSaida(mensagemSaida);
            logSistemaDAO.salvar(log);
        } catch (Exception e) {
            System.out.println("Causa: "+e.getCause());
            System.out.println("Mensagem: "+e.getMessage());
            System.out.println("To String: "+e.toString());
            e.printStackTrace();
        }
    }

}
