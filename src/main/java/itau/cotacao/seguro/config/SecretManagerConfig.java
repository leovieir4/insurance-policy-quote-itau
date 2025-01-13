package itau.cotacao.seguro.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import itau.cotacao.seguro.config.data.UserServiceAws;
import itau.cotacao.seguro.properties.AwsCredentialsProperties;
import itau.cotacao.seguro.properties.DynamoDBProperties;
import itau.cotacao.seguro.utils.Profiles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Configuration
public class SecretManagerConfig {

    private final DynamoDBProperties dynamoDBProperties;
    private final AwsCredentialsProperties awsCredentialsProperties;
    private ObjectMapper objectMapper;

    private Region region = Region.of(Region.US_EAST_2.toString());

    private SecretsManagerClient client = SecretsManagerClient.builder()
            .region(region)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();

    public SecretManagerConfig(DynamoDBProperties dynamoDBProperties,
                               AwsCredentialsProperties awsCredentialsProperties, ObjectMapper objectMapper) {
        this.dynamoDBProperties = dynamoDBProperties;
        this.awsCredentialsProperties = awsCredentialsProperties;
        this.objectMapper = objectMapper;
    }

    @Bean
    @Profile(Profiles.LOCAL)
    public UserServiceAws getUserServiceDynamoDBLocal(){
        UserServiceAws userServiceAws = new UserServiceAws();

        userServiceAws.setSecretKey(awsCredentialsProperties.getAccessKeyLocal());
        userServiceAws.setAccessKey(awsCredentialsProperties.getSecretKeyLocal());

        return userServiceAws;
    }

    @Bean
    @Profile(Profiles.NOT_LOCAL)
    public UserServiceAws getUserServiceDynamoDB() throws JsonProcessingException {
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(dynamoDBProperties.getSecretArn())
                .build();

        GetSecretValueResponse getSecretValueResult = client.getSecretValue(getSecretValueRequest);
        String secret = getSecretValueResult.secretString();

        return objectMapper.readValue(secret, UserServiceAws.class);
    }
}
