package br.com.rhitmohospede.request;

import br.com.rhitmohospede.validator.CustomDateConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CreateReservationRequest {

    private int numberRoom;

    @CustomDateConstraint(message = "Invalid")
    private LocalDate reservationDate;

    private Long numberDaysReserved;

    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
}
