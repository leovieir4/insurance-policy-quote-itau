package itau.cotacao.seguro.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws.dynamodb")
@Getter
@Setter
public class DynamoDBPropertiesTest {

    private String endpoint;
    private String region;
    private String secretArn;

}
