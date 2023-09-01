package br.com.rhitmohospede.service.impl;

import br.com.rhitmohospede.entity.Guest;
import br.com.rhitmohospede.exception.BusinessException;
import br.com.rhitmohospede.exception.GuestNotFoundException;
import br.com.rhitmohospede.exception.IntegrationException;
import br.com.rhitmohospede.repository.GuestRepository;
import br.com.rhitmohospede.request.GuestPhoneNumberRequest;
import br.com.rhitmohospede.request.GuestReservationRequest;
import br.com.rhitmohospede.request.RegistrationGuestRequest;
import br.com.rhitmohospede.response.GuestReservationResponse;
import br.com.rhitmohospede.response.GuestResponse;
import br.com.rhitmohospede.service.GuestService;
import br.com.rhitmohospede.utils.GuestUtils;
import br.com.rhitmohospede.utils.ReservationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.com.rhitmohospede.enums.ErrorMessages.*;
import static br.com.rhitmohospede.utils.GuestUtils.makeListGuestDTO;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;

    @Override
    public List<GuestResponse> getAllGuest() {
        List<Guest> guestList = guestRepository.findAll();
        if (guestList.isEmpty()) {
            throw new GuestNotFoundException(NO_GUEST_FOUND_MESSAGE.toString());
        }
        return makeListGuestDTO(guestList);
    }

    @Override
    public GuestReservationResponse getAllReservationByGuest(GuestReservationRequest guestReservationRequest) {
        Optional<Guest> guestOptional = guestRepository.findByEmail(guestReservationRequest.getEmail());
        if (guestOptional.isPresent()) {
            var guest = guestOptional.get();
            return GuestReservationResponse.builder()
                    .name(guest.getName())
                    .email(guest.getEmail())
                    .reservationList(ReservationUtils.makeReservationListResponse(guest.getReservations()))
                    .build();
        } else {
            throw new GuestNotFoundException(NO_GUEST_FOUND_MESSAGE.toString());
        }
    }

    @Override
    @Transactional
    public GuestResponse createGuest(RegistrationGuestRequest registrationGuestRequest) {
        Optional<Guest> guestOptional = guestRepository.findByEmail(registrationGuestRequest.getEmail());
        if (guestOptional.isEmpty()) {
            try {
                Guest guest = guestRepository.save(Guest.builder()
                        .name(registrationGuestRequest.getName())
                        .email(registrationGuestRequest.getEmail())
                        .reservations(new ArrayList<>())
                        .phone(registrationGuestRequest.getPhone())
                        .build());
                return GuestUtils.makeGuestResponse(guest);
            } catch (IntegrationException e) {
                throw new IntegrationException(AN_UNKNOWN_ERROR_MESSAGE.toString());
            }
        } else {
            throw new BusinessException(GUEST_ALREADY_REGISTERED_MESSAGE.toString());
        }
    }

    @Override
    @Transactional
    public void updateGuestNumber(GuestPhoneNumberRequest guestNumberRequest) {
        Optional<Guest> guestOptional = guestRepository.findByEmail(guestNumberRequest.getEmail());
        if (guestOptional.isPresent()) {
            try {
                var guest = guestOptional.get();
                guest.setPhone(guestNumberRequest.getCellPhone());
                guestRepository.save(guest);
            } catch (IntegrationException e) {
                throw new IntegrationException(AN_UNKNOWN_ERROR_MESSAGE.toString());
            }
        } else {
            throw new BusinessException(GUEST_NOT_FOUND.toString());
        }
    }

}
