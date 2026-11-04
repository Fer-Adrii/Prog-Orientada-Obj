package seguridad;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Cifrado de contraseñas con PBKDF2-HMAC-SHA256 y sal aleatoria.
 * Formato guardado en la BD:  PBKDF2$iteraciones$sal$hash  (Base64)
 */
public final class PasswordUtil {

    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final String PREFIJO = "PBKDF2";
    private static final int ITERACIONES = 210_000;
    private static final int BYTES_SAL = 16;
    private static final int BITS_HASH = 256;

    private PasswordUtil() { }

    /** Genera el texto a guardar en la columna clave_hash. */
    public static String hash(char[] clave) {
        byte[] sal = new byte[BYTES_SAL];
        new SecureRandom().nextBytes(sal);
        byte[] hash = derivar(clave, sal, ITERACIONES, BITS_HASH);
        return PREFIJO + "$" + ITERACIONES + "$"
                + Base64.getEncoder().encodeToString(sal) + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    /** Comprueba una contraseña contra el texto guardado en la BD. */
    public static boolean verificar(char[] clave, String almacenado) {
        if (almacenado == null) {
            return false;
        }
        String[] partes = almacenado.split("\\$");
        if (partes.length != 4 || !PREFIJO.equals(partes[0])) {
            return false;
        }
        try {
            int iteraciones = Integer.parseInt(partes[1]);
            byte[] sal = Base64.getDecoder().decode(partes[2]);
            byte[] esperado = Base64.getDecoder().decode(partes[3]);
            if (iteraciones < 1 || esperado.length == 0) {
                return false;
            }
            byte[] calculado = derivar(clave, sal, iteraciones, esperado.length * 8);
            return MessageDigest.isEqual(esperado, calculado);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] derivar(char[] clave, byte[] sal, int iteraciones, int bits) {
        PBEKeySpec spec = new PBEKeySpec(clave, sal, iteraciones, bits);
        try {
            return SecretKeyFactory.getInstance(ALGORITMO).generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("No se pudo cifrar la contraseña", e);
        } finally {
            spec.clearPassword();
        }
    }
}
