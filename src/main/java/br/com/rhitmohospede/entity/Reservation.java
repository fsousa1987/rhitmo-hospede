package br.com.rhitmohospede.entity;

import br.com.rhitmohospede.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tb_reservation")
@Entity
public class Reservation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(name = "reservation_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservationDate;

    @FutureOrPresent
    @Column(name = "checkin_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataCheckin;

    @Future
    @Column(name = "checkout_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataCheckout;

    @Column(name = "total_value")
    private BigDecimal totalValue;

    @OneToOne(orphanRemoval = true, fetch = FetchType.EAGER)
    private Room room;

    @Column(name = "room_reserved")
    private int roomReserved;

    @Column(name = "days_reserved")
    private int daysReserved;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(orphanRemoval = true, fetch = FetchType.EAGER)
    private Guest guest;
}
