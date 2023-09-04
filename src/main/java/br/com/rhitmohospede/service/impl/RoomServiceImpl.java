package br.com.rhitmohospede.service.impl;

import br.com.rhitmohospede.entity.Room;
import br.com.rhitmohospede.enums.Status;
import br.com.rhitmohospede.exception.IntegrationException;
import br.com.rhitmohospede.exception.RoomAlreadyRegisteredException;
import br.com.rhitmohospede.exception.RoomNotFoundException;
import br.com.rhitmohospede.exception.UnknownException;
import br.com.rhitmohospede.repository.ReservationRepository;
import br.com.rhitmohospede.repository.RoomRepository;
import br.com.rhitmohospede.request.CreateRoomRequest;
import br.com.rhitmohospede.request.UpdateRoomRequest;
import br.com.rhitmohospede.response.RoomResponse;
import br.com.rhitmohospede.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static br.com.rhitmohospede.utils.RoomUtils.makeListToRoomResponse;
import static br.com.rhitmohospede.utils.RoomUtils.makeRoomResponse;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public List<RoomResponse> findAllRoomByStatus(Status status) {
        var rooms = roomRepository.findRoomsByStatus(status);

        if (rooms.isEmpty()) {
            return Collections.emptyList();
        }
        return makeListToRoomResponse(rooms);
    }

    @Transactional
    @Override
    public RoomResponse createRoom(CreateRoomRequest request) {
        Room savedRoom;
        var roomOptional = roomRepository.findByNumber(request.getNumber());
        if (roomOptional.isPresent()) {
            throw new RoomAlreadyRegisteredException("There is already a room with these settings");
        }
        Room room = Room.builder()
                .number(request.getNumber())
                .guests(request.getGuests())
                .description(request.getDescription())
                .dailyValue(request.getValue())
                .status(Status.AVAILABLE)
                .build();

        savedRoom = roomRepository.save(room);
        return makeRoomResponse(savedRoom);
    }

    @Override
    @Transactional
    public void updateRoom(UpdateRoomRequest updateRoomRequest) {
        var roomOptional = roomRepository.findByNumber(updateRoomRequest.getNumber());
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            room.setGuests(updateRoomRequest.getGuests());
            room.setDescription(updateRoomRequest.getDescription());
            room.setDailyValue(updateRoomRequest.getValue());
            try {
                roomRepository.save(room);
            } catch (UnknownException e) {
                throw new UnknownException("Unknown error when trying to save room");
            }
        } else {
            throw new RoomNotFoundException("Room not found!");
        }
    }

    @Override
    public void deleteRoom(int roomNumber) {
        var reservationList = reservationRepository.findAllByRoomReserved(roomNumber);

        if (!reservationList.isEmpty()) {
            throw new IntegrationException("You cannot delete a room with a registered reservation.");
        }

        var optionalRoom = roomRepository.findByNumber(roomNumber);
        if (optionalRoom.isPresent()) {
            try {
                roomRepository.delete(optionalRoom.get());
            } catch (UnknownException e) {
                throw new UnknownException("Unknown error when trying to save room");
            }
        } else {
            throw new RoomNotFoundException("Room not found!");
        }

    }
}
