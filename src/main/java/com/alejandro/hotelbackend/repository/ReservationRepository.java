package com.alejandro.hotelbackend.repository;

import com.alejandro.hotelbackend.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ReservationRepository {

    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<Reservation> findAll() {
        return new ArrayList<>(reservations);
    }

    public Optional<Reservation> findById(Long id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findFirst();
    }

    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            reservation.setId(idGenerator.getAndIncrement());
            reservations.add(reservation);
            return reservation;
        }

        Optional<Reservation> existingReservation = findById(reservation.getId());

        if (existingReservation.isPresent()) {
            int index = reservations.indexOf(existingReservation.get());
            reservations.set(index, reservation);
        } else {
            reservations.add(reservation);
        }

        return reservation;
    }
}