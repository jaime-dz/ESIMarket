import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Base64;

import java.util.Base64;
import java.security.MessageDigest;

public class LoginEncriptado
{

    private static final int longitud_salt = 16; //la longitud de la salt
    private static final int iteraciones = 100; //cuantas vueltas se le da a la clave encriptada
    private static final int longitud_key = 256; //longitus del hash resultatnte

    public static byte[] GenerateSalt()
    {
        SecureRandom n = new SecureRandom();
        byte[] salt = new byte[longitud_salt];
        n.nextBytes(salt);
        return salt;
    }

    public static String HashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        PBEKeySpec DatosEncapsulados = new PBEKeySpec(password.toCharArray(),salt,iteraciones,longitud_key); //encapsulamos los datos ncesarios para hashear en ese bojeto
        SecretKeyFactory ClaveParaHashear = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256"); // hacemos una clave que sirva para hashear, la funcion en si
        byte[] hash = ClaveParaHashear.generateSecret(DatosEncapsulados).getEncoded(); //el get encoded es lo que coje los bytes, el propio hash en si

        return Base64.getEncoder().encodeToString(hash); // en base 64, el getEncoder mete el hash en un objeto encoder, el cual tiene la funcion encodeToString que es el que ransforma la secuencia de bytes a una cadena decaracteres
        //se almacenarian en base 64
    }

    public static boolean CompararContraseñas(String contraseña1,String contraseña2)
    {

        byte[] con1 = Base64.getDecoder().decode(contraseña1); //decodificamos la dos contraseñas, el det decoder lo que hace es crear una clase decoder y de esa lase cogemos la funcion decoder y lo decodificamos a bytes
        byte[] con2 = Base64.getDecoder().decode(contraseña2);

        return MessageDigest.isEqual(con1,con2); //simplemente compara los dos arrays de bytes

    }

    public static void main(String[] args) throws Exception
    {
        String contraseña = "Aqui va la contraseña que quieras encriptar";

        byte[] salt_del_usuario = GenerateSalt();

        String hash1 = HashPassword(contraseña,salt_del_usuario);
        String hash2 = HashPassword(contraseña,salt_del_usuario);

        System.out.println("Salt (Base64): " + Base64.getEncoder().encodeToString(salt_del_usuario));
        System.out.println("Hash (Base64): " + hash1);
        System.out.println("Hash (Base64): " + hash2);

        if(CompararContraseñas(hash1, hash2))
            System.out.println("Funciona la mierda esta");
    }
	// Prueba :)
}
