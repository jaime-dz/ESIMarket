package es.esimarket.backend.services;
import org.hibernate.engine.transaction.jta.platform.internal.SynchronizationRegistryBasedSynchronizationStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.security.MessageDigest;

@Component
public class LoginEncriptado implements PasswordEncoder {

    private static final int longitud_salt = 16; //la longitud de la salt
    private static final int iteraciones = 100; //cuantas vueltas se le da a la clave encriptada
    private static final int longitud_key = 256; //longitus del hash resultatnte


    @Override
    public String encode(CharSequence rawPassword) {

        String[] parts = rawPassword.toString().split(" ");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        String passwd = parts[1];

        String Hash;
        try {
            Hash = HashPassword(passwd,salt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        return Base64.getEncoder().encodeToString(salt) + " " + Hash;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        String[] parts = encodedPassword.split(" ");
        System.out.println("--------------------------------");
        System.out.println(Arrays.toString(parts));

        byte[] salt = Base64.getDecoder().decode(parts[0]);
        String passwd = parts[1];

        String actualHash;
        try {
            actualHash = HashPassword(rawPassword.toString(), salt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        return actualHash.equals(passwd);
    }



    public static byte[] GenerateSalt() {
        SecureRandom n = new SecureRandom();
        byte[] salt = new byte[longitud_salt];
        n.nextBytes(salt);
        return salt;
    }

    public static String HashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec DatosEncapsulados = new PBEKeySpec(password.toCharArray(), salt, iteraciones, longitud_key); //encapsulamos los datos ncesarios para hashear en ese objeto
        SecretKeyFactory ClaveParaHashear = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256"); // hacemos una clave que sirva para hashear, la funcion en si
        byte[] hash = ClaveParaHashear.generateSecret(DatosEncapsulados).getEncoded(); //el get encoded es lo que coje los bytes, el propio hash en si

        return Base64.getEncoder().encodeToString(hash); // en base 64, el getEncoder mete el hash en un objeto encoder, el cual tiene la funcion encodeToString que es el que ransforma la secuencia de bytes a una cadena decaracteres
        //se almacenarian en base 64
    }

    public static boolean CompararContrasennas(String contrasenna1, String contrasenna2) {

        byte[] con1 = Base64.getDecoder().decode(contrasenna1); //decodificamos la dos contraseñas, el det decoder lo que hace es crear una clase decoder y de esa lase cogemos la funcion decoder y lo decodificamos a bytes
        byte[] con2 = Base64.getDecoder().decode(contrasenna2);

        return MessageDigest.isEqual(con1, con2); //simplemente compara los dos arrays de bytes

    }

}