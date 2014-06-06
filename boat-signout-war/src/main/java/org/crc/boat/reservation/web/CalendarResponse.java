package org.crc.boat.reservation.web;

import java.util.List;

import org.crc.boat.reservation.model.Reservation;

public class CalendarResponse {
    public int success = 1;
    public List<Reservation> result;
    public CalendarResponse(List<Reservation> result) {
        this.result = result;
    }
}
