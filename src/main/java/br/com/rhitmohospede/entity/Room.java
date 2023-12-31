package br.com.rhitmohospede.entity;

import br.com.rhitmohospede.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_room")
@Entity
public class Room implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer number;

    @Column(nullable = false)
    private Integer guests;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @NotNull
    @Column(name = "daily_value")
    private BigDecimal dailyValue;
}
