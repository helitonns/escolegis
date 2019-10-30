package br.leg.alrr.cursos.auditoria;

import br.leg.alrr.cursos.model.UsuarioComUnidade;
import br.leg.alrr.cursos.util.FacesUtils;
import java.time.LocalDateTime;

import org.hibernate.envers.RevisionListener;

public class AuditListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        AuditEntity revEntity = (AuditEntity) revisionEntity;
        LocalDateTime data = LocalDateTime.now();
        UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
        String user = u.getLogin();

        revEntity.setIdUsuario(u.getId());
        revEntity.setUsuario(user);
        revEntity.setSenha(u.getSenha());
        revEntity.setIp(FacesUtils.getIP());
        revEntity.setData(data);
    }

}
