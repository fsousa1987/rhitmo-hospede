package br.com.rhitmohospede.service;

import br.com.rhitmohospede.enums.Status;
import br.com.rhitmohospede.request.CreateReservationRequest;
import br.com.rhitmohospede.request.PaymentRequest;
import br.com.rhitmohospede.response.ReservationResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

    List<ReservationResponse> getAllReservationsByStatus(Status status);

    List<ReservationResponse> getAllReservationsByDate(LocalDate dataInicial, LocalDate dataFinal);

    ReservationResponse createReservation(CreateReservationRequest createReservationRequest);

    void deleteReservation(String code);

    ReservationResponse reservationPayment(PaymentRequest paymentRequest);
}
