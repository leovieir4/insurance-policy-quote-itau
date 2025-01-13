package itau.cotacao.seguro.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws.credentials")
@Getter
@Setter
public class AwsCredentialsProperties {

    private String accessKeyLocal;
    private String secretKeyLocal;

}
