package itau.cotacao.seguro.controllers.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerResponse {

    private String documentNumber;
    private String name;
    private String type;
    private String gender;
    private LocalDate dateOfBirth;
    private String email;
    private Long phoneNumber;

}
