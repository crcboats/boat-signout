package org.crc.boat.reservation.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Boat {
	@Id
	String name;
	
	boolean rowable = true;
	String warningMessage;
	
	public Boat() {
		
	}
	
	public Boat(String name) {
        super();
        this.name = name;
    }

    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isRowable() {
        return rowable;
    }

    public void setRowable(boolean rowable) {
        this.rowable = rowable;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    @Override
	public String toString() {
		return "Boat [name=" + name + "]";
	}
	
}
