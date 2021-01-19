package reto2desktopclient.security;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

/**
 *
 * @author Aitor Fidalgo
 */
public class PublicCrypt {
    /**
     * Cifra un texto con RSA, modo ECB y padding PKCS1Padding (asim�trica) y lo
     * retorna
     * 
     * @param mensaje El mensaje a cifrar
     * @return El mensaje cifrado
     */
    public static String encode(String mensaje) {
        String encodedMessageStr = null;
        try {
            byte[] encodedMessage = null;
            // Clave publica
            byte fileKey[] = fileReader(".\\src\\reto2desktopclient\\security\\Public.key");
            
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(fileKey);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encodedMessage = cipher.doFinal(mensaje.getBytes());
            //Encoding encoded message to hexadecimal.
            encodedMessageStr = encodeHexadecimal(encodedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return encodedMessageStr;
    }
    
    /**
     * Retorna el contenido de un fichero
     * 
     * @param path Path del fichero
     * @return El texto del fichero
     */
    private static byte[] fileReader(String path) {
        byte ret[] = null;
        File file = new File(path);
        try {
            ret = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
    
    static String encodeHexadecimal(byte[] message) {
        String hexadecimalString = "";
        for (int i = 0; i < message.length; i++) {
            String h = Integer.toHexString(message[i] & 0xFF);
            if (h.length() == 1)
                    hexadecimalString += "0";
            hexadecimalString += h;
        }
        return hexadecimalString.toUpperCase();
    }
    
    public static void main(String[] args) {
        System.out.println(encode("1234"));
    }
    
}
