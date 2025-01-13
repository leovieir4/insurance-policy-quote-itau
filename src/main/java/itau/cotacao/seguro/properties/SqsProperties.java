package itau.cotacao.seguro.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws.sqs")
@Getter
@Setter
public class SqsProperties {

    private String region;
    private Queues queues;

    @Getter
    @Setter
    public static class InsurenceQuoteReceived{
        private String url;
        private String group;
    }
    @Getter
    @Setter
    public static class InsurenceQuoteCreated{
        private String url;
        private String group;
        private String name;
    }

    @Getter
    @Setter
    public static class Queues{
        private InsurenceQuoteReceived insurenceQuoteReceived;
        private InsurenceQuoteCreated insurenceQuoteCreated;
    }

}
