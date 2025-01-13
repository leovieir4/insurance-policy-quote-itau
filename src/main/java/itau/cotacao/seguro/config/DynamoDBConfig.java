package itau.cotacao.seguro.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import itau.cotacao.seguro.config.data.UserServiceAws;
import itau.cotacao.seguro.properties.DynamoDBProperties;
import itau.cotacao.seguro.utils.Profiles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DynamoDBConfig {

    private final DynamoDBProperties dynamoDBProperties;
    private final UserServiceAws userServiceAws;


    public DynamoDBConfig(DynamoDBProperties dynamoDBProperties, UserServiceAws userServiceAws) {
        this.dynamoDBProperties = dynamoDBProperties;
        this.userServiceAws = userServiceAws;
    }

    @Bean
    @Profile({Profiles.NOT_LOCAL, Profiles.LOCAL})
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(amazonDynamoDB());
    }

    @Bean
    @Profile({Profiles.NOT_LOCAL, Profiles.LOCAL})
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(dynamoDBProperties.getEndpoint(),
                        dynamoDBProperties.getRegion()))
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(this.userServiceAws.getAccessKey(),
                                this.userServiceAws.getSecretKey()))) // Usar as propriedades injetadas
                .build();
    }
}
