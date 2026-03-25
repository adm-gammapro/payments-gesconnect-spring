package com.raissapayments.conector.config.encrypted;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AESUtil {
    private static final String ALGORITHM = "AES";
    private final SecretKey secretKey;

    public AESUtil(@Value("${aes.secret.key}") String encodedKey) {
        this.secretKey = getKeyFromBytes(Base64.getDecoder().decode(encodedKey));
    }

    /**
     * Encripta un texto plano usando la clave secreta.
     * @param plainText Texto plano a encriptar.
     * @return Texto cifrado en Base64.
     * @throws Exception
     */
    public String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Desencripta un texto cifrado usando la clave secreta.
     * @param encryptedText Texto cifrado en Base64.
     * @return Texto plano original.
     * @throws Exception
     */
    public String decrypt(String encryptedText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] originalBytes = cipher.doFinal(decodedBytes);
        return new String(originalBytes);
    }

    /**
     * Convierte un arreglo de bytes en una clave secreta.
     * @param keyBytes Arreglo de bytes que representa la clave.
     * @return SecretKey
     */
    private SecretKey getKeyFromBytes(byte[] keyBytes) {
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    /**
     * Genera una nueva clave secreta en Base64 (ejecutar una sola vez para obtenerla).
     * @return Clave secreta en Base64.
     * @throws Exception
     */
    public static String generateSecretKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
}
