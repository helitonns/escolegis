package br.leg.alrr.cursos.util;

/**
 *
 * @author Heliton
 */
import java.io.Serializable;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("entityConverter")
public class EntityConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
        if (value != null) {
            Object o = this.getAttributesFrom(component).get(value);
            if (o != null) {
                return o;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent component, Object value) {
        
        if (value != null && !"".equals(value) && value instanceof BaseEntity) {

            @SuppressWarnings("rawtypes")
            BaseEntity entity = (BaseEntity) value;

            // adiciona item como atributo do componente
            this.addAttribute(component, entity);
            Serializable codigo = entity.getId();


            return (codigo != null ? String.valueOf(codigo) : "");
        }

        return "";

    }

    @SuppressWarnings("rawtypes")
    protected void addAttribute(UIComponent component, BaseEntity o) {
        if (o.getId() != null) {
            this.getAttributesFrom(component).put(o.getId().toString(), o);
        }
    }

    protected Map<String, Object> getAttributesFrom(UIComponent component) {
        return component.getAttributes();
    }
}
