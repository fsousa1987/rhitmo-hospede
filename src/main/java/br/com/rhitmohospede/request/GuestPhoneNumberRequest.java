package br.com.rhitmohospede.request;

import br.com.rhitmohospede.validator.Phone;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import lombok.*;

import static br.com.rhitmohospede.validator.EmailRegexValidator.emailRegex;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GuestPhoneNumberRequest {

    @Email(message = "Email is not valid", regexp = emailRegex)
    private String email;

    @Phone(message = "Phone is not valid")
    private String cellPhone;
}
