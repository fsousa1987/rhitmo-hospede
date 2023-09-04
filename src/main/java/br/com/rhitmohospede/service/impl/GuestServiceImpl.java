package br.com.rhitmohospede.service.impl;

import br.com.rhitmohospede.entity.Guest;
import br.com.rhitmohospede.exception.BusinessException;
import br.com.rhitmohospede.exception.GuestNotFoundException;
import br.com.rhitmohospede.repository.GuestRepository;
import br.com.rhitmohospede.request.GuestPhoneNumberRequest;
import br.com.rhitmohospede.request.GuestReservationRequest;
import br.com.rhitmohospede.request.RegistrationGuestRequest;
import br.com.rhitmohospede.response.GuestReservationResponse;
import br.com.rhitmohospede.response.GuestResponse;
import br.com.rhitmohospede.service.GuestService;
import br.com.rhitmohospede.utils.GuestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.com.rhitmohospede.utils.GuestUtils.makeListGuestToListGuestResponse;
import static br.com.rhitmohospede.utils.ReservationUtils.makeReservationListResponse;
import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;

    @Override
    public List<GuestResponse> getAllGuests() {
        var guestList = guestRepository.findAll();

        if (guestList.isEmpty()) {
            return emptyList();
        }

        return makeListGuestToListGuestResponse(guestList);
    }

    @Override
    @Transactional
    public GuestReservationResponse getAllReservationsByGuest(GuestReservationRequest guestReservationRequest) {
        var guestOptional = guestRepository.findByEmail(guestReservationRequest.getEmail());
        if (guestOptional.isPresent()) {
            var guest = guestOptional.get();
            return GuestReservationResponse.builder()
                    .name(guest.getName())
                    .email(guest.getEmail())
                    .reservationList(makeReservationListResponse(guest.getReservations()))
                    .build();
        } else {
            throw new GuestNotFoundException("Guest not found in database");
        }
    }

    @Override
    @Transactional
    public GuestResponse createGuest(RegistrationGuestRequest registrationGuestRequest) {
        var guestOptional = guestRepository.findByEmail(registrationGuestRequest.getEmail());
        if (guestOptional.isEmpty()) {
            Guest guest = guestRepository.save(Guest.builder()
                    .name(registrationGuestRequest.getName())
                    .email(registrationGuestRequest.getEmail())
                    .reservations(new ArrayList<>())
                    .phone(registrationGuestRequest.getPhone())
                    .build());
            return GuestUtils.makeGuestResponse(guest);
        }
        throw new BusinessException("Guest already registered");
    }

    @Override
    @Transactional
    public void updateGuestNumber(GuestPhoneNumberRequest guestNumberRequest) {
        Optional<Guest> guestOptional = guestRepository.findByEmail(guestNumberRequest.getEmail());

        if (guestOptional.isPresent()) {
            var guest = guestOptional.get();
            guest.setPhone(guestNumberRequest.getCellPhone());
            guestRepository.save(guest);
        } else {
            throw new GuestNotFoundException("Guest not found in database");
        }
    }
}
