package br.com.rhitmohospede.service.impl;

import br.com.rhitmohospede.entity.Guest;
import br.com.rhitmohospede.entity.Reservation;
import br.com.rhitmohospede.entity.Room;
import br.com.rhitmohospede.enums.Status;
import br.com.rhitmohospede.exception.*;
import br.com.rhitmohospede.repository.GuestRepository;
import br.com.rhitmohospede.repository.ReservationRepository;
import br.com.rhitmohospede.repository.RoomRepository;
import br.com.rhitmohospede.request.CreateReservationRequest;
import br.com.rhitmohospede.request.PaymentRequest;
import br.com.rhitmohospede.response.ReservationResponse;
import br.com.rhitmohospede.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static br.com.rhitmohospede.service.utils.Validators.isStatusProvidedValid;
import static br.com.rhitmohospede.utils.ReservationUtils.*;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;

    @Override
    public List<ReservationResponse> getAllReservationsByStatus(String status) {
        var upperCaseStatus = transformLowerCaseToUpperCase(status);
        isStatusProvidedValid(upperCaseStatus);

        var reservationList = reservationRepository.findAllByStatus(Status.valueOf(upperCaseStatus));
        return makeReservationListResponse(reservationList);

    }

    @Override
    public List<ReservationResponse> getAllReservationsByDate(LocalDate initialDate, LocalDate finalDate) {

        if (initialDate.isAfter(finalDate) || finalDate.isBefore(initialDate)) {
            throw new InvalidDateException(String.format("Divergence between dates: initialDate: %s and finalDate: %s",
                    initialDate, finalDate));
        }

        var reservationList = reservationRepository.findAllByReservationDateBetween(initialDate, finalDate);

        if (reservationList.isEmpty()) {
            return Collections.emptyList();
        }

        return makeReservationListResponse(reservationList);
    }

    @Transactional
    @Override
    public ReservationResponse createReservation(CreateReservationRequest createReservationRequest) {

        LocalDate reservationDate = LocalDate.parse(createReservationRequest.getReservationDate());

        var guest = findGuestByEmail(createReservationRequest.getEmail());
        var reservations = reservationRepository.findByGuestAndReservationDate(guest, reservationDate);

        if (!reservations.isEmpty()) {
            throw new BusinessException("You already have a reservation for that date");
        }

        var optionalRoom = roomRepository.findByNumber(createReservationRequest.getNumberRoom());
        if (optionalRoom.isEmpty()) {
            throw new RoomNotFoundException("Room not found in database");
        }

        Room room = optionalRoom.get();

        if (room.getStatus().equals(Status.PRE_BOOKING) || room.getStatus().equals(Status.RESERVED)) {
            throw new BusinessException("Room already taken or in PRE_BOOKING");
        }

        var reservationCode = gerarCodigo();

        var reservation = new Reservation();
        reservation.setCode(reservationRepository.existsByCode(reservationCode) ? gerarCodigo() : reservationCode);
        reservation.setReservationDate(reservationDate);
        reservation.setDataCheckin(reservationDate);
        reservation.setDataCheckout(reservationDate.plusDays(createReservationRequest.getNumberDaysReserved()));
        reservation.setRoomReserved(createReservationRequest.getNumberRoom());
        reservation.setStatus(Status.PRE_BOOKING);
        reservation.setGuest(guest);
        reservation.setTotalValue(calculateTotalValue(optionalRoom.get(), createReservationRequest.getNumberDaysReserved()));
        optionalRoom.get().setStatus(Status.PRE_BOOKING);
        reservation.setRoom(optionalRoom.get());

        return makeReservationResponse(reservationRepository.save(reservation));
    }

    @Transactional
    @Override
    public void deleteReservation(String code) {
        var optionalReservation = reservationRepository.findByCode(code);
        if (optionalReservation.isEmpty()) {
            throw new ReservationNotFoundException("No reservations found!");
        }
        var room = roomRepository.findByNumber(optionalReservation.get().getRoom().getNumber());

        if (room.isPresent()) {
            reservationRepository.delete(optionalReservation.get());
            room.get().setStatus(Status.AVAILABLE);
            roomRepository.saveAndFlush(room.get());
        }
    }

    @Override
    public ReservationResponse reservationPayment(PaymentRequest paymentRequest) {
        var optionalReservation = reservationRepository.findByCode(paymentRequest.getCode());
        if (optionalReservation.isEmpty()) {
            throw new ReservationNotFoundException("No reservations found!");
        }

        var reservation = optionalReservation.get();
        if (!reservation.getStatus().equals(Status.PRE_BOOKING)) {
            throw new BusinessException("The reservation status is not PRE_BOOKING");
        }
        var room = roomRepository.findByNumber(optionalReservation.get().getRoom().getNumber());

        if (room.isPresent()) {
            reservation.setStatus(Status.RESERVED);
            reservationRepository.save(reservation);

            room.get().setStatus(Status.RESERVED);
            roomRepository.save(room.get());
        }
        return makeReservationResponse(reservation);
    }

    private String transformLowerCaseToUpperCase(String status) {
        return status.toUpperCase();
    }

    private Guest findGuestByEmail(String email) {
        var optionalGuest = guestRepository.findByEmail(email);

        if (optionalGuest.isEmpty()) {
            throw new GuestNotFoundException("Guest not found in database");
        }

        return optionalGuest.get();
    }
}
