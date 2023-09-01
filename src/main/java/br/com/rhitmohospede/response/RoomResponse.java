package br.com.rhitmohospede.response;

import br.com.rhitmohospede.enums.Status;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RoomResponse {

    private int roomNumber;
    private int numberGuests;
    private Status status;
    private String description;
    private BigDecimal dailyValue;
}
