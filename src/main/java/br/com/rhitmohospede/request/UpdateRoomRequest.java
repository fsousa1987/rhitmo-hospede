package br.com.rhitmohospede.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UpdateRoomRequest {

    @NotNull(message = "enter a valid value")
    private Integer number;

    @NotNull(message = "enter a valid value")
    private Integer guests;

    @NotNull(message = "enter a valid value")
    private String description;

    @NotNull(message = "enter a valid value")
    private BigDecimal value;
}
