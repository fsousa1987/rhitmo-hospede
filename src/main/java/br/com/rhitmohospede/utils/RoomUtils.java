package br.com.rhitmohospede.utils;

import br.com.rhitmohospede.entity.Room;
import br.com.rhitmohospede.response.RoomResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomUtils {

    public static List<RoomResponse> makeListToRoomResponse(List<Room> roomList) {
        List<RoomResponse> responseList = new ArrayList<>();
        roomList.forEach(room -> responseList.add(RoomResponse.builder()
                .roomNumber(room.getNumber())
                .numberGuests(room.getGuests())
                .description(room.getDescription())
                .dailyValue(room.getDailyValue())
                .build()));
        return responseList;
    }

    public static RoomResponse makeRoomResponse(Room room) {
        return RoomResponse.builder()
                .roomNumber(room.getNumber())
                .numberGuests(room.getGuests())
                .description(room.getDescription())
                .status(room.getStatus())
                .dailyValue(room.getDailyValue())
                .build();
    }
}
