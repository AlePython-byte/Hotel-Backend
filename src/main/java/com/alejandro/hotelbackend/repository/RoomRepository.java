package com.alejandro.hotelbackend.repository;

import com.alejandro.hotelbackend.model.Room;
import com.alejandro.hotelbackend.model.RoomType;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RoomRepository {

    private final List<Room> rooms = new ArrayList<>();

    @PostConstruct
    public void initRooms() {
        rooms.add(new Room(1L, "101", RoomType.SINGLE, 100.0, true));
        rooms.add(new Room(2L, "102", RoomType.SINGLE, 100.0, true));
        rooms.add(new Room(3L, "103", RoomType.SINGLE, 100.0, true));
        rooms.add(new Room(4L, "104", RoomType.SINGLE, 100.0, true));
        rooms.add(new Room(5L, "105", RoomType.SINGLE, 100.0, true));

        rooms.add(new Room(6L, "201", RoomType.DOUBLE, 150.0, true));
        rooms.add(new Room(7L, "202", RoomType.DOUBLE, 150.0, true));
        rooms.add(new Room(8L, "203", RoomType.DOUBLE, 150.0, true));
        rooms.add(new Room(9L, "204", RoomType.DOUBLE, 150.0, true));
        rooms.add(new Room(10L, "205", RoomType.DOUBLE, 150.0, true));

        rooms.add(new Room(11L, "301", RoomType.SUITE, 250.0, true));
        rooms.add(new Room(12L, "302", RoomType.SUITE, 250.0, true));
        rooms.add(new Room(13L, "303", RoomType.SUITE, 250.0, true));
        rooms.add(new Room(14L, "304", RoomType.SUITE, 250.0, true));
        rooms.add(new Room(15L, "305", RoomType.SUITE, 250.0, true));
    }

    public List<Room> findAll() {
        return new ArrayList<>(rooms);
    }

    public Optional<Room> findById(Long id) {
        return rooms.stream()
                .filter(room -> room.getId().equals(id))
                .findFirst();
    }

    public Optional<Room> findByRoomNumber(String roomNumber) {
        return rooms.stream()
                .filter(room -> room.getRoomNumber().equalsIgnoreCase(roomNumber))
                .findFirst();
    }

    public List<Room> findByType(RoomType roomType) {
        return rooms.stream()
                .filter(room -> room.getType() == roomType)
                .collect(Collectors.toList());
    }

    public List<Room> findAvailableByType(RoomType roomType) {
        return rooms.stream()
                .filter(room -> room.getType() == roomType && room.isAvailable())
                .collect(Collectors.toList());
    }

    public void save(Room room) {
        Optional<Room> existingRoom = findById(room.getId());

        if (existingRoom.isPresent()) {
            int index = rooms.indexOf(existingRoom.get());
            rooms.set(index, room);
        } else {
            rooms.add(room);
        }
    }
}