package br.com.rhitmohospede.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PaymentRequest {

    @NotBlank(message = "must not be blank")
    private String code;
}
