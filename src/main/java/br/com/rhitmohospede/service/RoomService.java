package br.com.rhitmohospede.service;

import br.com.rhitmohospede.enums.Status;
import br.com.rhitmohospede.request.CreateRoomRequest;
import br.com.rhitmohospede.request.UpdateRoomRequest;
import br.com.rhitmohospede.response.RoomResponse;

import java.util.List;

public interface RoomService {

    List<RoomResponse> findAllRoomByStatus(Status status);

    RoomResponse createRoom(CreateRoomRequest request);

    void updateRoom(UpdateRoomRequest updateRoomRequest);

    void deleteRoom(int roomNumber);

}
