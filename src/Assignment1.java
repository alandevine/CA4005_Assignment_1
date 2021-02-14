import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class Assignment1 {

    final static BigInteger p = new BigInteger(
            "b59dd79568817b4b9f6789822d22594f376e6a9abc0241846de" +
                    "426e5dd8f6eddef00b465f38f509b2b18351064704fe75f012f" +
                    "a346c5e2c442d7c99eac79b2bc8a202c98327b96816cb804269" +
                    "8ed3734643c4c05164e739cb72fba24f6156b6f47a7300ef778" +
                    "c378ea301e1141a6b25d48f1924268c62ee8dd3134745cdf7323", 16);

    final static BigInteger g = new BigInteger(
            "44ec9d52c8f9189e49cd7c70253c2eb3154dd4f08467a64a026" +
                    "7c9defe4119f2e373388cfa350a4e66e432d638ccdc58eb703e" +
                    "31d4c84e50398f9f91677e88641a2d2f6157e2f4ec538088dcf" +
                    "5940b053c622e53bab0b4e84b1465f5738f549664bd7430961d" +
                    "3e5a2e7bceb62418db747386a58ff267a9939833beefb7a6fd68", 16);

    final static BigInteger A = new BigInteger(
            "5af3e806e0fa466dc75de60186760516792b70fdcd72a5b6238" +
                    "e6f6b76ece1f1b38ba4e210f61a2b84ef1b5dc4151e799485b2" +
                    "171fcf318f86d42616b8fd8111d59552e4b5f228ee838d535b4" +
                    "b987f1eaf3e5de3ea0c403a6c38002b49eade15171cb861b367" +
                    "732460e3a9842b532761c16218c4fea51be8ea0248385f6bac0d", 16);


    public static String bytesToHex(byte[] bytes) {
        StringBuilder buff = new StringBuilder();
        for (byte b : bytes) {
            buff.append(String.format("%02X", b));
        }
        return buff.toString();
    }

    public static BigInteger rightToLeftModExponentiation(BigInteger a, BigInteger exp, BigInteger mod) {

        /*
         * This method implements right-to-left modular exponentiation
         * */

        int k = exp.bitLength();

        BigInteger y = new BigInteger("1");

        for (int i = k - 1; i >= 0; i--){
            y = y.multiply(y).mod(mod);
            y = (exp.testBit(i)) ? y.multiply(a).mod(mod) : y;
        }

        return y;
    }

    public static void writeToFile(String fileName, String value) throws IOException{
        Files.write(Paths.get(fileName), Collections.singleton(value));
    }

    public static void main(String[] args) throws IOException,
                                                  NoSuchAlgorithmException,
                                                  NoSuchPaddingException,
                                                  BadPaddingException,
                                                  IllegalBlockSizeException,
                                                  InvalidAlgorithmParameterException,
                                                  InvalidKeyException
    {

        String inputFile = (args.length > 0) ? args[0] : null;

        if (inputFile == null) {
            System.out.println("Please provide a file to be encrypted.");
            System.exit(0);
        }

        Random rand = new Random();

        final BigInteger b = new BigInteger(1023, rand);
        final BigInteger B = rightToLeftModExponentiation(g, b, p);


        writeToFile("DH.txt", bytesToHex(B.toByteArray()));

        final BigInteger s = rightToLeftModExponentiation(A, b, p);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] k = md.digest(s.toByteArray());
        SecretKeySpec key = new SecretKeySpec(k, "AES");

        byte[] iv = new byte[16];
        SecureRandom rnd = new SecureRandom();
        rnd.nextBytes(iv);

        IvParameterSpec initVector = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, initVector);

        writeToFile("IV.txt", bytesToHex(cipher.getIV()));

        File f = new File(inputFile);

        FileInputStream is = new FileInputStream(f);

        int fileLength = (int) f.length();

        int padLength = 16 - (fileLength % 16);

        byte[] pad = new byte[padLength];
        Arrays.fill(pad, (byte) 0);
        pad[0] = (byte) 128;


        byte[] paddedInput = new byte[fileLength + padLength];
        byte[] fileBytes = is.readAllBytes();

        is.close();

        System.arraycopy(fileBytes, 0, paddedInput, 0, fileBytes.length);
        System.arraycopy(pad, 0, paddedInput, fileBytes.length, pad.length);

        byte[] output = cipher.doFinal(paddedInput);

        System.out.print(bytesToHex(output));
    }
}