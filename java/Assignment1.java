import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.*;
import javax.crypto.*;

public class Assignment1 {

    public static void main(String[] args) throws FileNotFoundException {
        String inputFile = (args.length > 0) ? args[0] : null;

        if (inputFile != null) {
            InputStream is = new FileInputStream(inputFile);

        } else {
            System.out.println("Please provide a file to be encrypted.");
        }
    }
}
