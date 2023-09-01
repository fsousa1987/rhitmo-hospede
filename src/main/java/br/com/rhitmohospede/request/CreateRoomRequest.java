package br.com.rhitmohospede.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CreateRoomRequest {

    @NotNull(message = "must not be blank")
    private Integer number;

    @NotNull(message = "must not be blank")
    private Integer guests;

    @NotNull(message = "must not be blank")
    private String description;

    @NotNull(message = "must not be blank")
    private BigDecimal value;
}
