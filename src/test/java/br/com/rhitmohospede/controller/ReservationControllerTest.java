package br.com.rhitmohospede.controller;

import br.com.rhitmohospede.service.ReservationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static br.com.rhitmohospede.factory.Factory.*;
import static br.com.rhitmohospede.util.JsonResponse.asJsonString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    static String RESERVATION_URI = "/api/v1/reservation";

    @Autowired
    MockMvc mvc;

    @MockBean
    ReservationService service;

    @Test
    @DisplayName("It should get all reservations by status")
    public void getAllReservationsByStatus() throws Exception {
        var reservationResponse = createReservationResponse();

        given(service.getAllReservationsByStatus(anyString())).willReturn(List.of(reservationResponse));

        var request = MockMvcRequestBuilders
                .get(RESERVATION_URI.concat("/status/available"))
                .accept(APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].code").isNotEmpty())
                .andExpect(jsonPath("$[0].checkinDate").isNotEmpty())
                .andExpect(jsonPath("$[0].checkoutDate").isNotEmpty())
                .andExpect(jsonPath("$[0].totalValue").isNotEmpty())
                .andExpect(jsonPath("$[0].roomNumber").isNotEmpty())
                .andExpect(jsonPath("$[0].status").isNotEmpty());
    }

    @Test
    @DisplayName("It should get all reservations by date")
    public void getAllReservationsByDate() throws Exception {
        var reservationResponse = createReservationResponse();

        given(service.getAllReservationsByDate(LocalDate.now().toString(), LocalDate.now().plusDays(3).toString()))
                .willReturn(List.of(reservationResponse));

        var request = MockMvcRequestBuilders
                .get(RESERVATION_URI.concat("/search/date"))
                .param("initialDate", LocalDate.now().toString())
                .param("finalDate", LocalDate.now().plusDays(3).toString())
                .accept(APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").isNotEmpty())
                .andExpect(jsonPath("$[0].checkinDate").isNotEmpty())
                .andExpect(jsonPath("$[0].checkoutDate").isNotEmpty())
                .andExpect(jsonPath("$[0].totalValue").isNotEmpty())
                .andExpect(jsonPath("$[0].roomNumber").isNotEmpty())
                .andExpect(jsonPath("$[0].status").isNotEmpty());
    }

    @Test
    @DisplayName("It should create a reservation")
    public void createReservation() throws Exception {
        var reservationRequest = createReservationRequest();
        var reservationResponse = createReservationResponse();

        given(service.createReservation(reservationRequest)).willReturn(reservationResponse);

        var request = MockMvcRequestBuilders
                .post(RESERVATION_URI)
                .content(asJsonString(reservationRequest))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").isNotEmpty())
                .andExpect(jsonPath("$.checkinDate").isNotEmpty())
                .andExpect(jsonPath("$.checkoutDate").isNotEmpty())
                .andExpect(jsonPath("$.totalValue").isNotEmpty())
                .andExpect(jsonPath("$.roomNumber").isNotEmpty())
                .andExpect(jsonPath("$.status").isNotEmpty());
    }

    @Test
    @DisplayName("It should to make reservation payment")
    public void reservationPayment() throws Exception {
        var paymentRequest = createPaymentRequest();
        var reservationResponse = createReservationResponse();

        given(service.reservationPayment(paymentRequest)).willReturn(reservationResponse);

        var request = MockMvcRequestBuilders
                .patch(RESERVATION_URI.concat("/pay"))
                .content(asJsonString(paymentRequest))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").isNotEmpty())
                .andExpect(jsonPath("$.checkinDate").isNotEmpty())
                .andExpect(jsonPath("$.checkoutDate").isNotEmpty())
                .andExpect(jsonPath("$.totalValue").isNotEmpty())
                .andExpect(jsonPath("$.roomNumber").isNotEmpty())
                .andExpect(jsonPath("$.status").isNotEmpty());
    }

    @Test
    @DisplayName("It should delete reservation")
    public void deleteReservation() throws Exception {
        var request = MockMvcRequestBuilders
                .delete(RESERVATION_URI.concat("/458"))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteReservation(anyString());
    }
}
