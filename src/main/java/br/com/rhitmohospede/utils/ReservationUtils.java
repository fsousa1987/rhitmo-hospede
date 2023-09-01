package br.com.rhitmohospede.utils;

import br.com.rhitmohospede.entity.Reservation;
import br.com.rhitmohospede.entity.Room;
import br.com.rhitmohospede.response.ReservationResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationUtils {

    public static String gerarCodigo() {
        return UUID.randomUUID().toString();
    }

    public static List<ReservationResponse> makeReservationListResponse(List<Reservation> reservations) {
        List<ReservationResponse> responseList = new ArrayList<>();

        reservations.forEach(reservation -> responseList.add(ReservationResponse.builder()
                .code(reservation.getCode())
                .checkinDate(reservation.getDataCheckin())
                .checkoutDate(reservation.getDataCheckout())
                .totalValue(reservation.getTotalValue())
                .roomNumber(reservation.getRoom().getNumber())
                .status(reservation.getStatus().toString())
                .build()));

        return responseList;
    }

    public static BigDecimal calculateTotalValue(Room room, Long numberDaysReserved) {
        return room.getDailyValue().multiply(BigDecimal.valueOf(numberDaysReserved));
    }

    public static ReservationResponse makeReservationResponse(Reservation save) {
        return ReservationResponse.builder()
                .code(save.getCode())
                .checkinDate(save.getDataCheckin())
                .checkoutDate(save.getDataCheckout())
                .totalValue(save.getTotalValue())
                .roomNumber(save.getRoom().getNumber())
                .status(save.getStatus().name())
                .build();
    }
}
