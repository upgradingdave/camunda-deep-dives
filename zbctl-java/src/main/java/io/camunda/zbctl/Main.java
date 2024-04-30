package io.camunda.zbctl;

import io.camunda.zeebe.client.CredentialsProvider;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.ZeebeClientBuilder;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import org.apache.commons.cli.*;

public class Main {

  public static void main(String[] args) {

    Option gatewayAddressOption =
        Option.builder()
            .longOpt("address")
            .desc("Specify a contact point address.")
            .hasArg()
            .build();

    Option audienceOption =
        Option.builder()
            .longOpt("audience")
            .desc("Specify the resource that the access token should be valid for.")
            .hasArg()
            .build();

    Option scopeOption =
        Option.builder().longOpt("scope").desc("Specify Zeebe Token Scope").hasArg().build();

    Option authzUrlOption =
        Option.builder()
            .longOpt("authzUrl")
            .desc("Specify an authorization server URL from which to request an access token.")
            .hasArg()
            .build();

    Option certPathOption =
        Option.builder()
            .longOpt("certPath")
            .desc("Specify a path to a certificate with which to validate gateway requests.")
            .hasArg()
            .build();

    Option clientIdOption =
        Option.builder()
            .longOpt("clientId")
            .desc("Specify a client identifier to request an access token.")
            .hasArg()
            .build();

    Option clientSecretOption =
        Option.builder()
            .longOpt("clientSecret")
            .desc("Specify a client secret to request an access token.")
            .hasArg()
            .build();

    Option helpOption = Option.builder().longOpt("help").desc("display this help message").build();

    Options options = new Options();
    options.addOption(gatewayAddressOption);
    options.addOption(audienceOption);
    options.addOption(scopeOption);
    options.addOption(authzUrlOption);
    options.addOption(certPathOption);
    options.addOption(clientIdOption);
    options.addOption(clientSecretOption);
    options.addOption(helpOption);

    CommandLineParser parser = new DefaultParser();
    try {
      CommandLine cmd = parser.parse(options, args);
      if (cmd.hasOption("help")) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("zbctl", options);
      } else {

        String address = "http://localhost:26500";
        if (cmd.hasOption("address")) {
          address = cmd.getOptionValue("address");
        }

        String audience = "zeebe-api";
        if (cmd.hasOption("audience")) {
          audience = cmd.getOptionValue("audience");
        }

        String scope = "camunda-identity";
        if (cmd.hasOption("scope")) {
          scope = cmd.getOptionValue("scope");
        }

        String authzUrl =
            "http://locahost:18080/auth/realms/camunda-platform/protocol/openid-connect/token";
        if (cmd.hasOption("authzUrl")) {
          authzUrl = cmd.getOptionValue("authzUrl");
        }

        String clientId = cmd.getOptionValue("clientId");
        String clientSecret = cmd.getOptionValue("clientSecret");

        CredentialsProvider credentialsProvider =
            new OAuthCredentialsProviderBuilder()
                .authorizationServerUrl(authzUrl)
                .audience(clientId)
                .scope(scope)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();

        ZeebeClientBuilder zeebeClientBuilder =
            io.camunda.zeebe.client.ZeebeClient.newClientBuilder()
                .gatewayAddress(address)
                .credentialsProvider(credentialsProvider);

        if (cmd.hasOption("certPath")) {
          String certPath = cmd.getOptionValue("certPath");
          KeystoreManager keystoreManager = new KeystoreManager();
          keystoreManager.createKeystore("zbctl.jks", "camunda", "camunda", certPath);
          // zeebeClientBuilder.caCertificatePath(certPath);
        }

        ZeebeClient zeebeClient = zeebeClientBuilder.build();
        System.out.println(zeebeClient.newTopologyRequest().send().join().toString());
      }

    } catch (ParseException e) {
      throw new RuntimeException(e);
    } catch (CertificateException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (KeyStoreException e) {
      throw new RuntimeException(e);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}
