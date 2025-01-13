package itau.cotacao.seguro.configs;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import itau.cotacao.seguro.config.data.UserServiceAws;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@TestConfiguration
public class Definictions {

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .build();
    }

    @Bean
    public UserServiceAws getUserServiceDynamoDBTest(){

        UserServiceAws userServiceAws = new UserServiceAws();
        userServiceAws.setAccessKey("test");
        userServiceAws.setSecretKey("test");

        return userServiceAws;
    }

    @Bean
    public DynamoDBMapper dynamoDBMapperTest() {
        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClient.builder().build();
        return new DynamoDBMapper(amazonDynamoDB);
    }
}
