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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

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
        boolean isValidStatus = isStatusProvidedValid(upperCaseStatus);

        if (isValidStatus) {
            var reservationList = reservationRepository.findAllByStatus(Status.valueOf(upperCaseStatus));
            return makeReservationListResponse(reservationList);
        }

        throw new InvalidStatusException(String.format("Status %s is not valid", status));
    }

    @Override
    public List<ReservationResponse> getAllReservationsByDate(String initialDate, String finalDate) {
        var dates = createMapOfDates(initialDate, finalDate);
        iterateToMapDateValues(dates);

        LocalDate initialDateParam = LocalDate.parse(initialDate);
        LocalDate finalDateParam = LocalDate.parse(finalDate);

        if (initialDateParam.isAfter(finalDateParam) || finalDateParam.isBefore(initialDateParam)) {
            throw new InvalidDateException(String.format("Divergence between dates: initialDate: %s and finalDate: %s",
                    initialDate, finalDate));
        }

        List<Reservation> reservationList = reservationRepository.findAllByReservationDateBetween(initialDateParam, finalDateParam);

        if (reservationList.isEmpty()) {
            return Collections.emptyList();
        }

        return makeReservationListResponse(reservationList);
    }

    @Transactional
    @Override
    public ReservationResponse createReservation(CreateReservationRequest createReservationRequest) {
        validateDateProvided(createReservationRequest.getReservationDate());
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

    private boolean isStatusProvidedValid(String upperCaseStatus) {
        return Arrays.stream(Status.values()).anyMatch(status -> status.toString().equals(upperCaseStatus));
    }

    private void validateDateProvided(String dateValue) {
        String datePattern = "yyyy-MM-dd";

        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);

        try {
            sdf.setLenient(false);
            sdf.parse(dateValue);
        } catch (ParseException e) {
            throw new InvalidDateException("Invalid date param or date field provided");
        }
    }

    private Map<String, String> createMapOfDates(String initialDate, String finalDate) {
        Map<String, String> dates = new HashMap<>();
        dates.put("initialDate", initialDate);
        dates.put("finalDate", finalDate);
        return dates;
    }

    private void iterateToMapDateValues(Map<String, String> dates) {
        dates.forEach((key, value) -> validateDateProvided(value));
    }

    private Guest findGuestByEmail(String email) {
        var optionalGuest = guestRepository.findByEmail(email);

        if (optionalGuest.isEmpty()) {
            throw new GuestNotFoundException("Guest not found in database");
        }

        return optionalGuest.get();
    }
}
