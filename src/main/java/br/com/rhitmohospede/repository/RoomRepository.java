package br.com.rhitmohospede.repository;

import br.com.rhitmohospede.entity.Room;
import br.com.rhitmohospede.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByNumber(int number);
    List<Room> findRoomsByStatus(Status status);
}
