package br.com.rhitmohospede.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

import static br.com.rhitmohospede.validator.EmailRegexValidator.emailRegex;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CreateReservationRequest {

    @NotNull(message = "must not be blank")
    private Integer numberRoom;

    @NotNull(message = "must not be blank")
    private String reservationDate;

    @NotNull(message = "must not be blank")
    private Long numberDaysReserved;

    @Email(message = "Email is not valid", regexp = emailRegex)
    private String email;
}
