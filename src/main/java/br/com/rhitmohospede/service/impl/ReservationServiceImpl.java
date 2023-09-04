package br.com.rhitmohospede.service.impl;

import br.com.rhitmohospede.entity.Guest;
import br.com.rhitmohospede.entity.Reservation;
import br.com.rhitmohospede.entity.Room;
import br.com.rhitmohospede.enums.Status;
import br.com.rhitmohospede.exception.BusinessException;
import br.com.rhitmohospede.exception.IntegrationException;
import br.com.rhitmohospede.exception.InvalidParamException;
import br.com.rhitmohospede.exception.InvalidStatusException;
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
        Map<String, String> dates = new HashMap<>();
        dates.put("initialDate", initialDate);
        dates.put("finalDate", finalDate);

        validateDatesProvided(dates);

        LocalDate initialDateParam = LocalDate.parse(initialDate);
        LocalDate finalDateParam = LocalDate.parse(finalDate);

        if (initialDateParam.isAfter(finalDateParam) || finalDateParam.isBefore(initialDateParam)) {
            throw new InvalidParamException(String.format("Divergence between dates: initialDate: %s and finalDate: %s",
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

        Optional<Guest> optionalGuest = guestRepository.findByEmail(createReservationRequest.getEmail());
        if (optionalGuest.isEmpty()) {
            throw new BusinessException("No guest found with these parameters");
        }

        Optional<Reservation> optionalReservation = reservationRepository.findByGuestAndReservationDate(optionalGuest.get(), createReservationRequest.getReservationDate());
        if (optionalReservation.isPresent()) {
            throw new BusinessException("You already have a reservation for that date!");
        }

        Optional<Reservation> reserved = reservationRepository.findByReservationDateAndRoomReserved(createReservationRequest.getReservationDate(), createReservationRequest.getNumberRoom());
        if (reserved.isPresent()) {
            throw new BusinessException("There is already a reservation for this date!");
        }

        Optional<Room> optionalRoom = roomRepository.findByNumber(createReservationRequest.getNumberRoom());
        if (optionalRoom.isEmpty()) {
            throw new BusinessException("No rooms found with these parameters");
        }

        var reservationCode = gerarCodigo();

        Reservation reservation = new Reservation();
        reservation.setCode(reservationRepository.existsByCode(reservationCode) ? gerarCodigo() : reservationCode);
        reservation.setReservationDate(createReservationRequest.getReservationDate());
        reservation.setDataCheckin(createReservationRequest.getReservationDate());
        reservation.setDataCheckout(createReservationRequest.getReservationDate().plusDays(createReservationRequest.getNumberDaysReserved()));
        reservation.setRoomReserved(createReservationRequest.getNumberRoom());
        reservation.setStatus(Status.PRE_BOOKING);
        reservation.setGuest(optionalGuest.get());
        reservation.setTotalValue(calculateTotalValue(optionalRoom.get(), createReservationRequest.getNumberDaysReserved()));
        reservation.setRoom(optionalRoom.get());
        try {
            return makeReservationResponse(reservationRepository.save(reservation));
        } catch (IntegrationException e) {
            throw new IntegrationException("An unknown error occurred while saving the room!");
        }
    }

    @Transactional
    @Override
    public void deleteReservation(String code) {
        Optional<Reservation> optionalReservation = reservationRepository.findByCode(code);
        if (optionalReservation.isEmpty()) {
            throw new BusinessException("No reservations found!");
        }
        try {
            reservationRepository.delete(optionalReservation.get());
        } catch (IntegrationException e) {
            throw new IntegrationException("An unknown error occurred while deleting the reservation!");
        }
    }

    @Override
    public ReservationResponse reservationPayment(PaymentRequest paymentRequest) {
        Optional<Reservation> optionalReservation = reservationRepository.findByCode(paymentRequest.getCode());
        if (optionalReservation.isEmpty()) {
            throw new BusinessException("No reservations found!");
        }
        try {
            Reservation reservation = optionalReservation.get();
            reservation.setStatus(Status.RESERVED);
            return makeReservationResponse(reservation);
        } catch (IntegrationException e) {
            throw new IntegrationException("An unknown error occurred while deleting the reservation!");
        }
    }

    private String transformLowerCaseToUpperCase(String status) {
        return status.toUpperCase();
    }

    private boolean isStatusProvidedValid(String upperCaseStatus) {
        return Arrays.stream(Status.values()).anyMatch(status -> status.toString().equals(upperCaseStatus));
    }

    private void validateDatesProvided(Map<String, String> dateMap) {
        String datePattern = "yyyy-MM-dd";

        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);

        dateMap.forEach((key, value) -> {
            try {
                sdf.setLenient(false);
                sdf.parse(value);
            } catch (ParseException e) {
                throw new InvalidParamException("Invalid date param provided");
            }
        });
    }
}
