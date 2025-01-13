package itau.cotacao.seguro.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.Data;

@Data
@DynamoDBDocument
public class CustomerEntity {
    @DynamoDBAttribute
    private String documentNumber;

    @DynamoDBAttribute
    private String name;

    @DynamoDBAttribute
    private String type;

    @DynamoDBAttribute
    private String gender;

    @DynamoDBAttribute
    private String dateOfBirth;

    @DynamoDBAttribute
    private String email;

    @DynamoDBAttribute
    private Long phoneNumber;
}