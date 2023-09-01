package br.com.rhitmohospede.controller;

import br.com.rhitmohospede.enums.Status;
import br.com.rhitmohospede.request.CreateRoomRequest;
import br.com.rhitmohospede.request.UpdateRoomRequest;
import br.com.rhitmohospede.service.RoomService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoomController.class)
public class RoomControllerTest {

    static String ROOM_URI = "/api/v1/room";

    @Autowired
    MockMvc mvc;

    @MockBean
    RoomService service;

    @Test
    @DisplayName("It should get all rooms by status active")
    public void findAllRoomsByStatusAvailable() throws Exception {
        var roomResponse = createRoomResponse();

        given(service.findAllRoomByStatus(any(Status.class), any(LocalDate.class), any(LocalDate.class))).willReturn(List.of(roomResponse));

        var request = MockMvcRequestBuilders
                .get(ROOM_URI.concat("/available"))
                .param("initialDate", LocalDate.now().toString())
                .param("finalDate", LocalDate.now().plusDays(3).toString())
                .accept(APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomNumber").isNotEmpty())
                .andExpect(jsonPath("$[0].numberGuests").isNotEmpty())
                .andExpect(jsonPath("$[0].status").isNotEmpty())
                .andExpect(jsonPath("$[0].description").isNotEmpty())
                .andExpect(jsonPath("$[0].dailyValue").isNotEmpty());
    }

    @Test
    @DisplayName("It should get all rooms by status reserved")
    public void findAllRoomsByStatusReserved() throws Exception {
        var roomResponse = createRoomResponse();
        roomResponse.setStatus(Status.RESERVED);

        given(service.findAllRoomByStatus(any(Status.class), any(LocalDate.class), any(LocalDate.class))).willReturn(List.of(roomResponse));

        var request = MockMvcRequestBuilders
                .get(ROOM_URI.concat("/reserved"))
                .param("initialDate", LocalDate.now().toString())
                .param("finalDate", LocalDate.now().plusDays(3).toString())
                .accept(APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomNumber").isNotEmpty())
                .andExpect(jsonPath("$[0].numberGuests").isNotEmpty())
                .andExpect(jsonPath("$[0].status").isNotEmpty())
                .andExpect(jsonPath("$[0].description").isNotEmpty())
                .andExpect(jsonPath("$[0].dailyValue").isNotEmpty());
    }

    @Test
    @DisplayName("It should create a room")
    public void createRoom() throws Exception {
        var roomResponse = createRoomResponse();
        var roomRequest = createRoomRequest();

        given(service.createRoom(any(CreateRoomRequest.class))).willReturn(roomResponse);

        var request = MockMvcRequestBuilders
                .post(ROOM_URI)
                .content(asJsonString(roomRequest))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.roomNumber").isNotEmpty())
                .andExpect(jsonPath("$.numberGuests").isNotEmpty())
                .andExpect(jsonPath("$.status").isNotEmpty())
                .andExpect(jsonPath("$.description").isNotEmpty())
                .andExpect(jsonPath("$.dailyValue").isNotEmpty());
    }

    @Test
    @DisplayName("It should update a room")
    public void updateRoom() throws Exception {
        var updateRoomRequest = createUpdateRoomRequest();

        var request = MockMvcRequestBuilders
                .patch(ROOM_URI.concat("/update"))
                .content(asJsonString(updateRoomRequest))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNoContent());

        verify(service, times(1)).updateRoom(any(UpdateRoomRequest.class));
    }

    @Test
    @DisplayName("It should delete a room")
    public void deleteRoom() throws Exception {
        var request = MockMvcRequestBuilders
                .delete(ROOM_URI.concat("/432"))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteRoom(anyInt());
    }
}
