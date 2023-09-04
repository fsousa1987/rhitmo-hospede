package br.com.rhitmohospede.request;

import br.com.rhitmohospede.validator.Phone;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import static br.com.rhitmohospede.validator.EmailRegexValidator.emailRegex;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RegistrationGuestRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email(message = "Email is not valid", regexp = emailRegex)
    private String email;

    @NotBlank
    @Phone(message = "Phone is not valid")
    private String phone;
}
