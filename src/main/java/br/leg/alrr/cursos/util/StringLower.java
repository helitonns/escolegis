package br.leg.alrr.cursos.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("lower")
public class StringLower implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return value != null ? value.toLowerCase() : null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value != null ? String.valueOf(value).toLowerCase() : null;
    }

}
