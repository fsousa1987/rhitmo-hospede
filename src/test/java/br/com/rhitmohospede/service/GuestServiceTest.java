package br.com.rhitmohospede.service;

import br.com.rhitmohospede.entity.Guest;
import br.com.rhitmohospede.exception.BusinessException;
import br.com.rhitmohospede.exception.GuestNotFoundException;
import br.com.rhitmohospede.repository.GuestRepository;
import br.com.rhitmohospede.response.GuestResponse;
import br.com.rhitmohospede.service.impl.GuestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static br.com.rhitmohospede.exception.enums.ErrorMessages.*;
import static br.com.rhitmohospede.factory.Factory.*;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class GuestServiceTest {

    public static final int INDEX = 0;

    GuestService service;

    @MockBean
    GuestRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new GuestServiceImpl(repository);
    }

    @Test
    @DisplayName("Must retrieve all guests successfully")
    public void getAllGuest() {
        var guest = createGuest();

        when(repository.findAll()).thenReturn(List.of(guest));

        var guestResponses = service.getAllGuests();

        assertThat(guestResponses.get(INDEX).getEmail()).isEqualTo(guest.getEmail());
        assertThat(guestResponses.get(INDEX).getPhone()).isEqualTo(guest.getPhone());
        assertThat(guestResponses.get(INDEX).getName()).isEqualTo(guest.getName());
    }

    @Test
    @DisplayName("It should throw an exception when it doesn't find guests in the database")
    public void noGuestsFound() {

        when(repository.findAll()).thenReturn(emptyList());

        var exception = catchThrowable(() -> service.getAllGuests());

        assertThat(exception)
                .isInstanceOf(GuestNotFoundException.class)
                .hasMessage(NO_GUEST_FOUND_MESSAGE.toString());

        verify(repository, atLeastOnce()).findAll();
    }

    @Test
    @DisplayName("Must retrieve all reservations for a guest")
    public void getAllReservationsByGuest() {
        var guestReservationRequest = createGuestReservationRequest();
        var reservation = createReservation();
        var guest = createGuest();
        guest.setReservations(List.of(reservation));

        when(repository.findByEmail(anyString())).thenReturn(of(guest));

        var allReservationsByGuest = service.getAllReservationsByGuest(guestReservationRequest);

        assertThat(allReservationsByGuest.getReservationList().get(INDEX)
                .getCheckinDate()).isEqualTo(guest.getReservations().get(INDEX).getDataCheckin());
        assertThat(allReservationsByGuest.getReservationList().get(INDEX)
                .getCheckoutDate()).isEqualTo(guest.getReservations().get(INDEX).getDataCheckout());
        assertThat(allReservationsByGuest.getReservationList().get(INDEX)
                .getRoomNumber()).isEqualTo(guest.getReservations().get(INDEX).getRoom().getNumber());
        assertThat(allReservationsByGuest.getReservationList().get(INDEX)
                .getStatus()).isEqualTo(guest.getReservations().get(INDEX).getStatus().toString());
        assertThat(allReservationsByGuest.getReservationList().get(INDEX)
                .getCode()).isEqualTo(guest.getReservations().get(INDEX).getCode());
        assertThat(allReservationsByGuest.getReservationList().get(INDEX)
                .getTotalValue()).isEqualTo(guest.getReservations().get(INDEX).getTotalValue());
    }

    @Test
    @DisplayName("It should throw an exception when it doesn't find guest trying retrieve reservation")
    public void noReservationsFoundByGuest() {
        var guestReservationRequest = createGuestReservationRequest();

        when(repository.findByEmail(anyString())).thenReturn(empty());

        var exception = catchThrowable(() -> service.getAllReservationsByGuest(guestReservationRequest));

        assertThat(exception)
                .isInstanceOf(GuestNotFoundException.class)
                .hasMessage(NO_GUEST_FOUND_MESSAGE.toString());

        verify(repository, atLeastOnce()).findByEmail(anyString());
    }

    @Test
    @DisplayName("It should throw an exception when guest already registered")
    public void notCreateGuest() {
        var guest = createGuest();
        var registrationGuestRequest = createRegistrationGuestRequest();

        when(repository.findByEmail(anyString())).thenReturn(of(guest));

        var exception = catchThrowable(() -> service.createGuest(registrationGuestRequest));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage(GUEST_ALREADY_REGISTERED_MESSAGE.toString());
    }

    @Test
    @DisplayName("It should create guest success")
    public void createGuestSuccess() {
        var registrationGuestRequest = createRegistrationGuestRequest();
        var guest = createGuest();

        when(repository.findByEmail(anyString())).thenReturn(empty());
        when(repository.save(any(Guest.class))).thenReturn(guest);

        GuestResponse response = service.createGuest(registrationGuestRequest);

        assertThat(response.getName()).isEqualTo(guest.getName());
        assertThat(response.getEmail()).isEqualTo(guest.getEmail());
        assertThat(response.getPhone()).isEqualTo(guest.getPhone());

        verify(repository, atLeastOnce()).findByEmail(anyString());
        verify(repository, atLeastOnce()).save(any(Guest.class));
    }

    @Test
    @DisplayName("It should update guest phone number success")
    public void updateGuestNumber() {
        var guestPhoneNumberRequest = createGuestPhoneNumberRequest();
        var guest = createGuest();

        when(repository.findByEmail(anyString())).thenReturn(of(guest));
        when(repository.save(any(Guest.class))).thenReturn(guest);

        service.updateGuestNumber(guestPhoneNumberRequest);

        verify(repository, atLeastOnce()).findByEmail(anyString());
        verify(repository, atLeastOnce()).save(any(Guest.class));
    }

    @Test
    @DisplayName("It should throw an exception when not find a guest in the database when trying to update")
    public void noGuestsFoundUpdate() {
        var guestPhoneNumberRequest = createGuestPhoneNumberRequest();

        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

        var exception = catchThrowable(() -> service.updateGuestNumber(guestPhoneNumberRequest));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage(GUEST_NOT_FOUND.toString());

        verify(repository, atLeastOnce()).findByEmail(anyString());
        verify(repository, never()).save(any(Guest.class));
    }
}
