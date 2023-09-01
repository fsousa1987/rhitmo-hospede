package br.com.rhitmohospede.controller;

import br.com.rhitmohospede.request.GuestPhoneNumberRequest;
import br.com.rhitmohospede.service.GuestService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static br.com.rhitmohospede.factory.Factory.*;
import static br.com.rhitmohospede.util.JsonResponse.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GuestController.class)
public class GuestControllerTest {

    static String GUEST_URI = "/api/v1/guest";

    @Autowired
    MockMvc mvc;

    @MockBean
    GuestService service;

    @Test
    @DisplayName("It should list all guests")
    public void listAllGuestsTest() throws Exception {
        var guests = createGuestResponse();

        given(service.getAllGuest()).willReturn(List.of(guests));

        var request = MockMvcRequestBuilders
                .get(GUEST_URI)
                .accept(APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").isNotEmpty())
                .andExpect(jsonPath("$[0].email").isNotEmpty())
                .andExpect(jsonPath("$[0].phone").isNotEmpty());
    }

    @Test
    @DisplayName("It should retrieve all of a guest's reservations")
    public void getAllReservationByGuest() throws Exception {
        var requestGuestReservation = createGuestReservationRequest();
        var response = createGuestReservationResponse();

        var request = MockMvcRequestBuilders
                .get(GUEST_URI.concat("/reservations"))
                .content(asJsonString(requestGuestReservation))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);

        given(service.getAllReservationByGuest(requestGuestReservation)).willReturn(response);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.email").isNotEmpty())
                .andExpect(jsonPath("$.reservationList[0].code").isNotEmpty())
                .andExpect(jsonPath("$.reservationList[0].checkinDate").isNotEmpty())
                .andExpect(jsonPath("$.reservationList[0].checkoutDate").isNotEmpty())
                .andExpect(jsonPath("$.reservationList[0].totalValue").isNotEmpty())
                .andExpect(jsonPath("$.reservationList[0].roomNumber").isNotEmpty())
                .andExpect(jsonPath("$.reservationList[0].status").isNotEmpty());
    }

    @Test
    @DisplayName("It should update guest phone number")
    public void updateGuestPhoneNumber() throws Exception {
        var guestNumberRequest = createGuestPhoneNumberRequest();

        var request = MockMvcRequestBuilders
                .patch(GUEST_URI.concat("/update/phone"))
                .content(asJsonString(guestNumberRequest))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNoContent());

        verify(service, times(1)).updateGuestNumber(any(GuestPhoneNumberRequest.class));
    }

    @Test
    @DisplayName("It should create a new guest")
    public void createGuest() throws Exception {
        var registrationGuestRequest = createRegistrationGuestRequest();

        var guestResponse = createGuestResponse();

        var request = MockMvcRequestBuilders
                .post(GUEST_URI)
                .content(asJsonString(registrationGuestRequest))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);

        given(service.createGuest(registrationGuestRequest)).willReturn(guestResponse);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.email").isNotEmpty())
                .andExpect(jsonPath("$.phone").isNotEmpty());
    }
}
