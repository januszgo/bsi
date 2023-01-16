package org.example;

import javax.crypto.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

public class Main {
    public static void rsaFile(Path path,Path path1,int keysize) {
        try {
            int inputlen = keysize/8 - 20;
            //System.out.println("Message before encryption: " + str);
            long startTime = System.nanoTime();
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(keysize);
            KeyPair pair = generator.generateKeyPair();
            PrivateKey privateRsaKey = pair.getPrivate();
            PublicKey publicRsaKey = pair.getPublic();
            FileOutputStream fos = new FileOutputStream("publicRsa.key");
            fos.write(publicRsaKey.getEncoded());
            FileOutputStream fos1 = new FileOutputStream("privateRsa.key");
            fos1.write(privateRsaKey.getEncoded());
            long endTime = System.nanoTime();
            long duration = (endTime - startTime)/1000000;
            System.out.println("Key generating time: " + duration);

            startTime = System.nanoTime();

            byte[] fileBytes = new byte[inputlen];
            byte[] encryptedFileBytes;
            RandomAccessFile data = new RandomAccessFile(path.toFile(), "r");

            for (long i = 0, len = path.toFile().length() / inputlen; i < len; i++) {
                data.readFully(fileBytes);

                Cipher encryptCipher = Cipher.getInstance("RSA");
                encryptCipher.init(Cipher.ENCRYPT_MODE, publicRsaKey);
                encryptedFileBytes = encryptCipher.doFinal(fileBytes);
                FileOutputStream stream = new FileOutputStream(path1.toFile());
                stream.write(encryptedFileBytes);
                stream.write("\n".getBytes());

            }

            endTime = System.nanoTime();
            duration = (endTime - startTime)/1000000;
            System.out.println("Encryption time: " + duration);

            startTime = System.nanoTime();

            fileBytes = new byte[inputlen];
            byte[] decryptedFileBytes = new byte[inputlen];
            data = new RandomAccessFile(path1.toFile(), "r");
            Scanner scanner = new Scanner(new File(path1.toFile().toURI()));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                fileBytes= line.getBytes(StandardCharsets.UTF_8);
                Cipher decryptCipher = Cipher.getInstance("RSA");
                decryptCipher.init(Cipher.ENCRYPT_MODE, publicRsaKey);
                decryptedFileBytes = decryptCipher.doFinal(fileBytes);
                FileOutputStream stream = new FileOutputStream(path.toFile());
                stream.write(decryptedFileBytes);
            }
            endTime = System.nanoTime();
            duration = (endTime - startTime)/1000000;
            System.out.println("Decryption time: " + duration);
            //System.out.println("Decrypted message: " + decryptedMessage);
            //return decryptedMessage;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String rsaString(String str) {
        try {
            //System.out.println("Message before encryption: " + str);

            long startTime = System.nanoTime();
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair pair = generator.generateKeyPair();
            PrivateKey privateRsaKey = pair.getPrivate();
            PublicKey publicRsaKey = pair.getPublic();
            FileOutputStream fos = new FileOutputStream("publicRsa.key");
            fos.write(publicRsaKey.getEncoded());
            FileOutputStream fos1 = new FileOutputStream("privateRsa.key");
            fos1.write(privateRsaKey.getEncoded());
            long endTime = System.nanoTime();
            long duration = (endTime - startTime)/1000000;
            System.out.println("Key generating time: " + duration);

            startTime = System.nanoTime();

            String exampleString = str;
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicRsaKey);
            byte[] secretMessageBytes = exampleString.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
            String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);
            endTime = System.nanoTime();
            duration = (endTime - startTime)/1000000;
            System.out.println("Encryption time: " + duration);

            startTime = System.nanoTime();
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateRsaKey);
            byte[] decryptedMessageBytes = decryptCipher.doFinal(encryptedMessageBytes);
            String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
            endTime = System.nanoTime();
            duration = (endTime - startTime)/1000000;
            System.out.println("Decryption time: " + duration);
            //System.out.println("Decrypted message: " + decryptedMessage);
            return decryptedMessage;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void rsaModule(){
        String str = "ABCDEFGHIJ";
        if (str.equals(rsaString(str))) {
            System.out.println("Encryption and decryption have been conducted correctly");
        }
        //200 characters
        str = "vei9AjFEw2gSW10xX9U4Svkwua7Lm2dFDK9oeBbSpRVgHIkpIit6vtTI53VpayAzd3BgEc7Mm7A1um3YDD0VH7GcMZWeq5jt9FreRWXj8Dk4UAqQv4SQIQi6Tsj9YSafnQ31w1vJqRv13KXeXwwZkEPTQbJXg2BXYvGio1PgKD5us9rjKBMSN11UOgWTUmmchcLO65qS";
        if (str.equals(rsaString(str))) {
            System.out.println("Encryption and decryption have been conducted correctly");
        }

        System.out.println("File with 1000 characters");
        rsaFile(new File("string1000.txt").toPath(),new File("string1000_encrypted.txt").toPath(),4096);

        System.out.println("File with 10000 characters");
        rsaFile(new File("string10000.txt").toPath(),new File("string10000_encrypted.txt").toPath(),4096);

        System.out.println("File with 100000 characters");
        rsaFile(new File("string100000.txt").toPath(),new File("string100000_encrypted.txt").toPath(),4096);

        System.out.println("File with 1000000 characters");
        rsaFile(new File("string1000000.txt").toPath(),new File("string1000000_encrypted.txt").toPath(),4096);

        System.out.println("File with 10-7 characters");
        rsaFile(new File("string10-7.txt").toPath(),new File("string10-7_encrypted.txt").toPath(),4096);

        System.out.println("File with 10-8 characters");
        rsaFile(new File("string10-8.txt").toPath(),new File("string10-8_encrypted.txt").toPath(),4096);

        System.out.println("File with 10-9 characters");
        rsaFile(new File("string10-9.txt").toPath(),new File("string10-9_encrypted.txt").toPath(),4096);
    }

    public static void aes(Path path,Path path1,int keysize) {
        try {
            int inputlen = keysize;
            //System.out.println("Message before encryption: " + str);
            long startTime = System.nanoTime();
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(keysize);
            SecretKey key = generator.generateKey();
            FileOutputStream fos = new FileOutputStream("aes.key");
            fos.write(key.getEncoded());
            long endTime = System.nanoTime();
            long duration = (endTime - startTime)/1000000;
            System.out.println("Key generating time: " + duration);

            startTime = System.nanoTime();

            byte[] fileBytes = new byte[inputlen];
            byte[] encryptedFileBytes;
            RandomAccessFile data = new RandomAccessFile(path.toFile(), "r");

            for (long i = 0, len = path.toFile().length() / inputlen; i < len; i++) {
                data.readFully(fileBytes);

                Cipher encryptCipher = Cipher.getInstance("AES");
                encryptCipher.init(Cipher.ENCRYPT_MODE, key);
                encryptedFileBytes = encryptCipher.doFinal(fileBytes);
                FileOutputStream stream = new FileOutputStream(path1.toFile());
                stream.write(encryptedFileBytes);
                stream.write("\n".getBytes());

            }

            endTime = System.nanoTime();
            duration = (endTime - startTime)/1000000;
            System.out.println("Encryption time: " + duration);

            startTime = System.nanoTime();

            fileBytes = new byte[inputlen];
            byte[] decryptedFileBytes = new byte[inputlen];
            data = new RandomAccessFile(path1.toFile(), "r");
            Scanner scanner = new Scanner(new File(path1.toFile().toURI()));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                fileBytes= line.getBytes(StandardCharsets.UTF_8);
                Cipher decryptCipher = Cipher.getInstance("AES");
                decryptCipher.init(Cipher.ENCRYPT_MODE, key);
                decryptedFileBytes = decryptCipher.doFinal(fileBytes);
                FileOutputStream stream = new FileOutputStream(path.toFile());
                stream.write(decryptedFileBytes);
            }
            endTime = System.nanoTime();
            duration = (endTime - startTime)/1000000;
            System.out.println("Decryption time: " + duration);
            //System.out.println("Decrypted message: " + decryptedMessage);
            //return decryptedMessage;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void aesModule(){

        System.out.println("File with 1000 characters");
        aes(new File("string1000.txt").toPath(),new File("string1000_encrypted.txt").toPath(),128);

        System.out.println("File with 10000 characters");
        aes(new File("string10000.txt").toPath(),new File("string10000_encrypted.txt").toPath(),192);

        System.out.println("File with 100000 characters");
        aes(new File("string100000.txt").toPath(),new File("string100000_encrypted.txt").toPath(),192);

        System.out.println("File with 1000000 characters");
        aes(new File("string1000000.txt").toPath(),new File("string1000000_encrypted.txt").toPath(),192);

        System.out.println("File with 10-7 characters");
        aes(new File("string10-7.txt").toPath(),new File("string10-7_encrypted.txt").toPath(),192);

        System.out.println("File with 10-8 characters");
        aes(new File("string10-8.txt").toPath(),new File("string10-8_encrypted.txt").toPath(),192);

        System.out.println("File with 10-9 characters");
        aes(new File("string10-9.txt").toPath(),new File("string10-9_encrypted.txt").toPath(),192);
    }

    public static void main(String[] args) {
        rsaModule();
        //aesModule();
    }
}