package br.com.rhitmohospede.service;

import br.com.rhitmohospede.request.GuestPhoneNumberRequest;
import br.com.rhitmohospede.request.GuestReservationRequest;
import br.com.rhitmohospede.request.RegistrationGuestRequest;
import br.com.rhitmohospede.response.GuestReservationResponse;
import br.com.rhitmohospede.response.GuestResponse;

import java.util.List;

public interface GuestService {

    List<GuestResponse> getAllGuest();
    GuestReservationResponse getAllReservationByGuest(GuestReservationRequest guestReservationRequest);
    GuestResponse createGuest(RegistrationGuestRequest registrationGuestRequest);
    void updateGuestNumber(GuestPhoneNumberRequest guestNumberRequest);
}
