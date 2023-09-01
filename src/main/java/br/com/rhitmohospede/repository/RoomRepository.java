package br.com.rhitmohospede.repository;

import br.com.rhitmohospede.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByNumber(int number);
}
