package br.leg.alrr.cursos.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Criptografia {

    public static String criptografarEmMD5(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
            return String.format("%32x", hash);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Erro ao tentar criptografar a string");
        }
        return null;
    }

}
