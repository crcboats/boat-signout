package org.crc.boat.reservation.dao;


import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import java.util.logging.Logger;

import org.crc.boat.reservation.model.Boat;

import com.googlecode.objectify.ObjectifyService;

public class BoatDao {
	static final Logger LOG = Logger.getLogger(BoatDao.class.getName());
	static {
        ObjectifyService.register(Boat.class);
    }

	public Boat saveBoat(Boat boat) {

		LOG.info("Saving boat " + boat);
		ofy().save().entity(boat).now();
		return boat;
		
	}

	public void deleteBoat(Boat boat) {

		LOG.info("Deleting boat " + boat);
		ofy().delete().entity(boat).now();
		
	}

	public List<Boat> listBoats(){
		LOG.info("List boats ");
		List<Boat> list = ofy().load().type(Boat.class).list();
		LOG.info("Number of boats: " + (list != null ? list.size() : "null") );
		return list;
		
	}
}
