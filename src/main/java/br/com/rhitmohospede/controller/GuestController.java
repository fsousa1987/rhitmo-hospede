package br.com.rhitmohospede.controller;

import br.com.rhitmohospede.request.GuestPhoneNumberRequest;
import br.com.rhitmohospede.request.GuestReservationRequest;
import br.com.rhitmohospede.request.RegistrationGuestRequest;
import br.com.rhitmohospede.response.GuestReservationResponse;
import br.com.rhitmohospede.response.GuestResponse;
import br.com.rhitmohospede.service.GuestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/guest")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GuestResponse>> getAllGuests() {
        var guests = guestService.getAllGuest();
        return ResponseEntity.ok(guests);
    }

    @GetMapping(path = "/reservations", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<GuestReservationResponse> getAllReservationsByGuest(@Valid GuestReservationRequest guestReservationRequest) {
        var guestsReservation = guestService.getAllReservationByGuest(guestReservationRequest);
        return ResponseEntity.ok(guestsReservation);
    }

    @PatchMapping(path = "/update/phone", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> updateGuestPhoneNumber(@Valid @RequestBody GuestPhoneNumberRequest guestNumberRequest) {
        guestService.updateGuestNumber(guestNumberRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<GuestResponse> createGuest(@Valid @RequestBody RegistrationGuestRequest registrationGuestRequest) {
        var guest = guestService.createGuest(registrationGuestRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(guest);
    }
}
