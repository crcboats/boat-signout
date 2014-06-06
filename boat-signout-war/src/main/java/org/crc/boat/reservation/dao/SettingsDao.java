package org.crc.boat.reservation.dao;


import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import java.util.logging.Logger;

import org.crc.boat.reservation.model.Settings;

import com.googlecode.objectify.ObjectifyService;

public class SettingsDao {
	static final Logger LOG = Logger.getLogger(SettingsDao.class.getName());
	static {
        ObjectifyService.register(Settings.class);
    }

	public Settings saveSettings(Settings settings) {

		LOG.info("Saving settings " + settings);
		ofy().save().entity(settings).now();
		return settings;
		
	}

	public void deleteSettings(String  key) {
		LOG.info("Deleting settings " + key);
		ofy().delete().type(Settings.class).id(key).now();
		
	}

	public List<Settings> listSettings(){
		LOG.info("List settings ");
		List<Settings> list = ofy().load().type(Settings.class).list();
		LOG.info("Number of settings: " + (list != null ? list.size() : "null") );
		return list;
		
	}
	
	public String getSetting(String  key){
	    Settings settings = ofy().load().type(Settings.class).id(key).now();
	    return settings == null ? null : settings.getValue();
	}
}
