package com.raissapayments.conector.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

@Slf4j
public class GeneradorClaveEncriptamiento {
    private static final String ALGORITHM = "AES";

    /**
     * Genera una clave secreta para el cifrado.
     * @return SecretKey
     */
    public static String generateSecretKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(128); // Tamaño de clave: 128 bits
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public static void main(String[] args) {
        try {
            log.info("Clave secreta (Base64):" + generateSecretKey());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
