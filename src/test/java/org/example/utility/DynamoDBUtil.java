package org.example.utility;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.*;

public class DynamoDBUtil {

    private static final String TABLE_NAME = "OnlineExpenseManagerExpenses";

    public static Map<String, String> getRandomExpense() {

        DynamoDbClient dynamoDBClient = DynamoDbClient.builder()
                .region(Region.AP_SOUTHEAST_2)
              //  .credentialsProvider(AwsCredentialProvider.getCredentials()) -- Use this line incase you want to use this utility in a CI/CD pipeline with an assumed role. Make sure to set the roleArn in your config file.
                .credentialsProvider(DefaultCredentialsProvider.create()) // Use this line if you are running the tests locally and have your AWS credentials configured in the default location (e.g., ~/.aws/credentials).
                .build();

        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .build();

        ScanResponse response = dynamoDBClient.scan(scanRequest);

        List<Map<String, AttributeValue>> items = response.items();

        if (items.isEmpty()) {
            return null;
        }

        Random random = new Random();

        Map<String, AttributeValue> randomItem =
                items.get(random.nextInt(items.size()));

        Map<String, String> result = new HashMap<>();

        randomItem.forEach((k, v) -> {
            if (v.s() != null) {
                result.put(k, v.s());
            } else if (v.n() != null) {
                result.put(k, v.n());
            }
        });

        return result;
    }

}
