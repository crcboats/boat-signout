package org.crc.boat.reservation.dao;


import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.crc.boat.reservation.model.Reservation;

import com.googlecode.objectify.ObjectifyService;

public class ReservationDao {
	static final Logger LOG = Logger.getLogger(ReservationDao.class.getName());
	static {
        ObjectifyService.register(Reservation.class);
    }

	public Reservation saveReservation(Reservation reservation) {

		LOG.info("Saving reservation " + reservation);
		ofy().save().entity(reservation).now();
		return reservation;
		
	}

	public void deleteReservation(Long id) {

		LOG.info("Deleting reservation " + id);
		ofy().delete().type(Reservation.class).id(id).now();
		
	}

	public List<Reservation> listReservations(){
		LOG.info("List reservations ");
		List<Reservation> list = ofy().load().type(Reservation.class).list();
		LOG.info("Number of reservations: " + (list != null ? list.size() : "null") );
		return list;
		
	}
	
	public List<Reservation> listReservations(Long from, Long to){
        LOG.info("List reservations from " + new Date(from) + " to " + new Date(to));
        List<Reservation> list = ofy().load().type(Reservation.class)
                .filter("end >=", from)
                .filter("end <=", to)
                .list();
        LOG.info("Number of reservations: " + (list != null ? list.size() : "null") );
        return list;
        
    }
	
	public List<Reservation> hasConflict(Reservation r){
	    List<Reservation> conflicts = new ArrayList<>();
	    List<Reservation> newStartLtExistingEnd = ofy().load().type(Reservation.class)
	            .filter("boatName", r.getBoatName())
	            .filter("end >", Long.valueOf(r.getStart()))
	            .list();
	    for (Reservation existing : newStartLtExistingEnd) {
	        if(r.getEnd() > existing.getStart()){
	            conflicts.add(existing);
	        }
	    }
	    return conflicts;
	}
}
