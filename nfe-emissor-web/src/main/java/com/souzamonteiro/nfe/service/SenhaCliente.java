package com.souzamonteiro.nfe.service;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class SenhaCliente {
    private static final int ITERACOES = 600_000;
    private static final SecureRandom RANDOM = new SecureRandom();

    private SenhaCliente() {}

    public static String gerar(String senha) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(salt) + ":" + encoder.encodeToString(derivar(senha, salt));
    }

    public static boolean conferir(String senha, String registro) {
        if (senha == null || senha.length() > 128 || registro == null) return false;
        String[] partes = registro.split(":", -1);
        if (partes.length != 2) return false;
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            return MessageDigest.isEqual(decoder.decode(partes[1]), derivar(senha, decoder.decode(partes[0])));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] derivar(String senha, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(senha.toCharArray(), salt, ITERACOES, 256);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Falha ao proteger a senha.", e);
        } finally {
            spec.clearPassword();
        }
    }
}
