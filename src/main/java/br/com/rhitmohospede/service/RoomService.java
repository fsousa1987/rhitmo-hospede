package br.com.rhitmohospede.service;

import br.com.rhitmohospede.enums.Status;
import br.com.rhitmohospede.request.CreateRoomRequest;
import br.com.rhitmohospede.request.UpdateRoomRequest;
import br.com.rhitmohospede.response.RoomResponse;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {

    List<RoomResponse> findAllRoomByStatus(Status status, LocalDate initialDate, LocalDate finalDate);
    RoomResponse createRoom(CreateRoomRequest request);
    void updateRoom(UpdateRoomRequest updateRoomRequest);
    void deleteRoom(int roomNumber);

}
