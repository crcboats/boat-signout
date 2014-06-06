package org.crc.boat.reservation.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Settings {
	@Id
	String key;
	String value;
	
	public Settings() {
		
	}

    public Settings(String key, String value) {
        this.key = key;
        this.value = value;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Settings [key=" + key + ", value=" + value + "]";
    }
	
}
