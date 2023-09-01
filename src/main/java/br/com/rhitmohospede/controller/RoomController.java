package br.com.rhitmohospede.controller;

import br.com.rhitmohospede.enums.Status;
import br.com.rhitmohospede.request.CreateRoomRequest;
import br.com.rhitmohospede.request.UpdateRoomRequest;
import br.com.rhitmohospede.response.RoomResponse;
import br.com.rhitmohospede.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping(path = "/available", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoomResponse>> findAllRoomsByStatusAvailable(@RequestParam LocalDate initialDate, @RequestParam LocalDate finalDate) {
        var responseList = roomService.findAllRoomByStatus(Status.AVAILABLE, initialDate, finalDate);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping(path = "/reserved", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoomResponse>> findAllRoomsByStatusReserved(@RequestParam LocalDate initialDate, @RequestParam LocalDate finalDate) {
        var responseList = roomService.findAllRoomByStatus(Status.RESERVED, initialDate, finalDate);
        return ResponseEntity.ok(responseList);
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody CreateRoomRequest createRoomRequest) {
        var response = roomService.createRoom(createRoomRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping(path = "/update", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> updateRoom(@RequestBody UpdateRoomRequest updateRoomRequest) {
        roomService.updateRoom(updateRoomRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/{roomNumber}")
    public ResponseEntity<HttpStatus> deleteRoom(@PathVariable(name = "roomNumber") int roomNumber) {
        roomService.deleteRoom(roomNumber);
        return ResponseEntity.noContent().build();
    }
}
