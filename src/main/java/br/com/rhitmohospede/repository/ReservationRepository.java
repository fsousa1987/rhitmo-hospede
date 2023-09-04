package br.com.rhitmohospede.repository;

import br.com.rhitmohospede.entity.Guest;
import br.com.rhitmohospede.entity.Reservation;
import br.com.rhitmohospede.entity.Room;
import br.com.rhitmohospede.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByStatus(Status status);

    List<Reservation> findAllByReservationDateBetween(LocalDate initialDate, LocalDate finalDate);

    List<Reservation> findAllByRoomReserved(int number);

    List<Reservation> findByGuestAndReservationDate(Guest guest, LocalDate reservationDate);

    Optional<Reservation> findByReservationDateAndRoomReserved(LocalDate reservationDate, int number);

    Optional<Reservation> findByCode(String code);

    boolean existsByCode(String code);
}
