package reto2desktopclient.security;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ResourceBundle;
import javax.crypto.Cipher;
import reto2desktopclient.exceptions.UnexpectedErrorException;

/**
 * Contains the methods meant to encode Strings using RSA.
 * 
 * @author Aitor Fidalgo
 */
public class PublicCrypt {
    
    /**
     * Relative path of the public key used to encode.
     */
    private static final String PUBLIC_KEY_PATH = ResourceBundle
            .getBundle("reto2desktopclient.properties.properties").getString("publicKeyPath");
    
    
    /**
     * Encodes the given message with RSA/ECB/PKCS1Padding and returns it.
     *
     * @param message The message to be encoded.
     * @return El message cifrado
     * @throws reto2desktopclient.exceptions.UnexpectedErrorException If anything goes wrong.
     */
    public static String encode(String message) throws UnexpectedErrorException {
        String encodedMessageStr = null;
        try {
            byte[] encodedMessage;
            //Getting the public key in a byte array.
            byte fileKey[] = getPublicKey();
            
            //Setting the properties for the encoding...
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(fileKey);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

            //Encoding with public key...
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encodedMessage = cipher.doFinal(message.getBytes());
            
            //Encoding message to hexadecimal now, to avoid '/' character.
            encodedMessageStr = encodeToHexadecimal(encodedMessage);
        } catch (Exception ex) {
            throw new UnexpectedErrorException(ex);
        }

        return encodedMessageStr;
    }
    
    
    /**
     * Reads the public key file and returns it as a byte array.
     *
     * @return Public key content in byte array.
     * @throws IOException If and I/O error occurs.
     */
    private static byte[] getPublicKey() throws IOException {
        byte ret[];
        File file = new File(PUBLIC_KEY_PATH);
        ret = Files.readAllBytes(file.toPath());
        return ret;
    }
    
    /**
     * Encodes a byte array into an hexadecimal String and returns it.
     * 
     * @param message Byte array to be encoded.
     * @return Encoded hexadecimal representation of the given message.
     */
    static String encodeToHexadecimal(byte[] message) {
        String hexadecimalString = "";
        for (int i = 0; i < message.length; i++) {
            String h = Integer.toHexString(message[i] & 0xFF);
            if (h.length() == 1)
                    hexadecimalString += "0";
            hexadecimalString += h;
        }
        return hexadecimalString.toUpperCase();
    }
    
}
