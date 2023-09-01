package br.com.rhitmohospede.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReservationResponse {

    private String code;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private BigDecimal totalValue;
    private int roomNumber;
    private String status;
}
