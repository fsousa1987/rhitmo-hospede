package br.com.rhitmohospede.service.impl;

import br.com.rhitmohospede.entity.Reservation;
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
import br.com.rhitmohospede.utils.RoomUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public List<RoomResponse> findAllRoomByStatus(Status status, LocalDate initialDate, LocalDate finalDate) {

        List<Room> rooms = reservationRepository.findAllRoomByStatus(status.toString(), initialDate, finalDate);
        if (rooms.isEmpty()) {
            throw new RoomNotFoundException("Room not found!");
        }
        return RoomUtils.makeListToRoomResponse(rooms);
    }

    @Transactional
    @Override
    public RoomResponse createRoom(CreateRoomRequest request) {
        Room room;
        Room savedRoom;
        Optional<Room> roomOptional = roomRepository.findByNumber(request.getNumber());
        if (roomOptional.isPresent()) {
            throw new RoomAlreadyRegisteredException("Room already registered!");
        } else {
            room = new Room();
            room.setNumber(request.getNumber());
            room.setGuests(request.getGuests());
            room.setDescription(request.getDescription());
            room.setDailyValue(request.getValue());
            room.setStatus(Status.AVAILABLE);
            try {
                savedRoom = roomRepository.save(room);
            } catch (UnknownException e) {
                throw new UnknownException("Unknown error when trying to save room");
            }
        }
        return RoomUtils.makeRoomResponse(savedRoom);
    }

    @Override
    public void updateRoom(UpdateRoomRequest updateRoomRequest) {
        Optional<Room> roomOptional = roomRepository.findByNumber(updateRoomRequest.getNumber());
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
        List<Reservation> reservationList = reservationRepository.findAllByRoomReserved(roomNumber);

        if (!reservationList.isEmpty()) {
            throw new IntegrationException("You cannot delete a room with a registered reservation.");
        } else {
            Optional<Room> optionalRoom = roomRepository.findByNumber(roomNumber);
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
}
