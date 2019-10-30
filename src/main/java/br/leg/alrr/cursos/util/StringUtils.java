package br.leg.alrr.cursos.util;

/**
 *
 * @author heliton
 */
public class StringUtils {

    public static String removerAcentos(String s) {
        String out = s;
        
        out = out.replaceAll("[àáâäã]", "a");
        out = out.replaceAll("[èéêëẽ]", "e");
        out = out.replaceAll("[ìíîïĩ]", "i");
        out = out.replaceAll("[òóôöõ]", "o");
        out = out.replaceAll("[ùúûüũ]", "u");
        out = out.replaceAll("ç", "c");
        out = out.replaceAll("ñ", "n");

        out = out.replaceAll("[ÀÁÂÄÃ]", "A");
        out = out.replaceAll("[ÈÉÊËẼ]", "E");
        out = out.replaceAll("[ÌÍÎÏĨ]", "I");
        out = out.replaceAll("[ÒÓÔÖÕ]", "O");
        out = out.replaceAll("[ÙÚÛÜŨ]", "U");
        out = out.replaceAll("Ç", "C");
        out = out.replaceAll("Ñ", "N");

        return out;
    }
    
    public static String retornarPrimeiroNome(String s) {
        try {
            String[] resultado = s.split(" ");
            return resultado[0];
        } catch (Exception e) {
        }
        return null;
    }

}
