package org.example.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

import java.util.HashMap;
import java.util.Map;

public class SecretManagerUtil {
    private static final Region REGION = Region.AP_SOUTHEAST_2;
    public static Map<String, String> getCredentialsFromSecretsManager() {

        SecretsManagerClient client =
                SecretsManagerClient.builder()
                        .region(REGION)
                      //  .credentialsProvider(AwsCredentialProvider.getCredentials())
                        .credentialsProvider(DefaultCredentialsProvider.create()) // Use this line if you are running the tests locally and have your AWS credentials configured in the default location (e.g., ~/.aws/credentials).
                        .build();

        String secretName = "onlineexpensemanager/credentials";

        String secretString =
                client.getSecretValue(
                        GetSecretValueRequest.builder()
                                .secretId(secretName)
                                .build()
                ).secretString();

        try {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(secretString);
            Map<String, String> creds = new HashMap<>();

            creds.put("username", node.get("aceonlineexpensemanager-username").asText());
            creds.put("password", node.get("aceonlineexpensemanager-password").asText());
            return creds;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to read secret manager data",
                    e
            );
        }
    }


}
