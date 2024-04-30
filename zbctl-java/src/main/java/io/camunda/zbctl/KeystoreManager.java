package io.camunda.zbctl;

import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class KeystoreManager {

  private static InputStream fullStream(String fname) throws IOException {
    FileInputStream fis = new FileInputStream(fname);
    DataInputStream dis = new DataInputStream(fis);
    byte[] bytes = new byte[dis.available()];
    dis.readFully(bytes);
    return new ByteArrayInputStream(bytes);
  }

  public void createKeystore(
      String keystoreFileName, String keystorePassword, String certAlias, String certPath)
      throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {

    char[] password = keystorePassword.toCharArray();

    // Create Keystore if it doesn't exist
    File keystoreFile = new File(keystoreFileName);
    if (keystoreFile.exists()) {
      keystoreFile.delete();
    }

    KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
    keystore.load(null, password);

    FileOutputStream fos = new FileOutputStream(keystoreFileName);
    keystore.store(fos, password);
    fos.close();

    // Create Certificate
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    InputStream certstream = fullStream(certPath);
    Certificate certs = cf.generateCertificate(certstream);

    // Add the certificate
    keystore.setCertificateEntry(certAlias, certs);

    // Update the keystore contents
    FileOutputStream out = new FileOutputStream(keystoreFile);
    keystore.store(out, password);
    out.close();

    // Configure the jvm to use the keystore
    System.setProperty("javax.net.ssl.trustStore", keystoreFileName);
    System.setProperty("javax.net.ssl.trustStorePassword", keystorePassword);
  }
}
