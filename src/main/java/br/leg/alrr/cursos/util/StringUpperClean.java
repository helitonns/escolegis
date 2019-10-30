package br.leg.alrr.cursos.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@FacesConverter("upperClean")
public class StringUpperClean implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return value != null ? StringUtils.removerAcentos(value.toUpperCase()) : null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value != null ? String.valueOf(value).toUpperCase() : null;
    }

}
