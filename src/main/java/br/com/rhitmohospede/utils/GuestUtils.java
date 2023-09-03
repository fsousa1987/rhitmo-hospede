package br.com.rhitmohospede.utils;

import br.com.rhitmohospede.entity.Guest;
import br.com.rhitmohospede.response.GuestResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GuestUtils {

    public static List<GuestResponse> makeListGuestToListGuestResponse(List<Guest> guests) {
        List<GuestResponse> guestResponses = new ArrayList<>();

        guests.forEach(guest -> guestResponses.add(
                GuestResponse
                        .builder()
                        .name(guest.getName())
                        .phone(guest.getPhone())
                        .email(guest.getEmail())
                        .build()));

        return guestResponses;
    }

    public static GuestResponse makeGuestResponse(Guest guest) {
        return GuestResponse
                .builder()
                .name(guest.getName())
                .email(guest.getEmail())
                .phone(guest.getPhone())
                .build();
    }
}
