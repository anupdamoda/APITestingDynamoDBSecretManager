package org.example.utility;

import org.example.config.Config;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;

public class AwsCredentialProvider {

    public static AwsCredentialsProvider getCredentials() {

        String roleArn = Config.get("aws.roleArn");

        StsClient stsClient = StsClient.builder()
                .region(Region.AP_SOUTHEAST_2)
                .build();

        StsAssumeRoleCredentialsProvider credentialsProvider =
                StsAssumeRoleCredentialsProvider.builder()
                        .stsClient(stsClient)
                        .refreshRequest(r -> r
                                .roleArn(roleArn)
                                .roleSessionName("automation-session"))
                        .build();

        return credentialsProvider;
    }
}
