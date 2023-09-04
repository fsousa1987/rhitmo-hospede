package br.com.rhitmohospede.factory;

import br.com.rhitmohospede.entity.Guest;
import br.com.rhitmohospede.entity.Reservation;
import br.com.rhitmohospede.entity.Room;
import br.com.rhitmohospede.enums.Status;
import br.com.rhitmohospede.request.*;
import br.com.rhitmohospede.response.GuestReservationResponse;
import br.com.rhitmohospede.response.GuestResponse;
import br.com.rhitmohospede.response.ReservationResponse;
import br.com.rhitmohospede.response.RoomResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Factory {

    public static GuestResponse createGuestResponse() {
        return GuestResponse.builder()
                .email("test@test.com.br")
                .name("John Doe")
                .phone("(99) 99999-9999")
                .build();
    }

    public static GuestReservationRequest createGuestReservationRequest() {
        return GuestReservationRequest.builder()
                .email("test@test.com.br")
                .build();
    }

    public static ReservationResponse createReservationResponse() {
        return ReservationResponse.builder()
                .status("AVAILABLE")
                .code("456")
                .checkinDate(LocalDate.now())
                .checkoutDate(LocalDate.now().plusDays(3))
                .roomNumber(256)
                .totalValue(BigDecimal.TEN)
                .build();
    }

    public static GuestReservationResponse createGuestReservationResponse() {
        return GuestReservationResponse.builder()
                .name("John Doe")
                .email("test@test.com.br")
                .reservationList(List.of(createReservationResponse()))
                .build();
    }

    public static GuestPhoneNumberRequest createGuestPhoneNumberRequest() {
        return GuestPhoneNumberRequest.builder()
                .cellPhone("99 99999-9999")
                .email("test@test.com.br")
                .build();
    }

    public static RegistrationGuestRequest createRegistrationGuestRequest() {
        return RegistrationGuestRequest.builder()
                .email("test@test.com.br")
                .phone("(99) 99999-9999")
                .name("John Doe")
                .build();
    }

    public static CreateReservationRequest createReservationRequest() {
        return CreateReservationRequest.builder()
                .email("test@test.com.br")
                .numberDaysReserved(3L)
                .numberRoom(3589)
                .reservationDate(LocalDate.now().toString())
                .build();
    }

    public static PaymentRequest createPaymentRequest() {
        return PaymentRequest.builder()
                .code("456")
                .build();
    }

    public static RoomResponse createRoomResponse() {
        return RoomResponse.builder()
                .dailyValue(BigDecimal.TEN)
                .description("Great room")
                .numberGuests(2)
                .roomNumber(458)
                .status(Status.AVAILABLE)
                .build();
    }

    public static CreateRoomRequest createRoomRequest() {
        return CreateRoomRequest.builder()
                .description("Great room")
                .guests(3)
                .number(389)
                .value(BigDecimal.TEN)
                .build();
    }

    public static Guest createGuest() {
        return Guest.builder()
                .phone("(99) 99999-9999")
                .name("John Doe")
                .email("test@test.com.br")
                .reservations(null)
                .build();
    }

    public static Reservation createReservation() {
        return Reservation.builder()
                .id(1L)
                .code("441")
                .daysReserved(3)
                .dataCheckin(LocalDate.now())
                .dataCheckout(LocalDate.now().plusDays(3))
                .guest(null)
                .roomReserved(335)
                .reservationDate(LocalDate.now())
                .room(createRoom())
                .status(Status.AVAILABLE)
                .totalValue(BigDecimal.TEN)
                .build();
    }

    public static Room createRoom() {
        return Room.builder()
                .dailyValue(BigDecimal.TEN)
                .description("Nice Room")
                .guests(2)
                .id(1L)
                .status(Status.AVAILABLE)
                .number(332)
                .build();
    }
}
