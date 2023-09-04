package br.com.rhitmohospede.controller;

import br.com.rhitmohospede.request.CreateReservationRequest;
import br.com.rhitmohospede.request.PaymentRequest;
import br.com.rhitmohospede.response.ReservationResponse;
import br.com.rhitmohospede.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/api/v1/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping(path = "status/{reservationStatus}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReservationResponse>> getAllReservationsByStatus(@PathVariable(name = "reservationStatus") String reservationStatus) {
        var reservationsByStatus = reservationService.getAllReservationsByStatus(reservationStatus);
        return ResponseEntity.ok(reservationsByStatus);
    }

    @GetMapping(path = "/search/date", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReservationResponse>> getAllReservationsByDate(@RequestParam LocalDate initialDate, @RequestParam LocalDate finalDate) {
        var reservationsByData = reservationService.getAllReservationsByDate(initialDate, finalDate);
        return ResponseEntity.ok(reservationsByData);
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody @Valid CreateReservationRequest createReservationRequest) {
        var reservationResponse = reservationService.createReservation(createReservationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationResponse);
    }

    @PatchMapping(path = "/pay", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationResponse> reservationPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        var reservationResponse = reservationService.reservationPayment(paymentRequest);
        return ResponseEntity.ok(reservationResponse);
    }

    @DeleteMapping(path = "/{code}")
    public ResponseEntity<HttpStatus> deleteReservation(@PathVariable(name = "code") String code) {
        reservationService.deleteReservation(code);
        return ResponseEntity.noContent().build();
    }
}
