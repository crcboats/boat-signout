package org.crc.boat.reservation.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Boat {
	@Id
	String name;
	String displayName;
	boolean rowable = true;
    boolean event = false;
	String warningMessage;
	
	public Boat() {
		
	}
	
	public Boat(String name) {
        super();
        this.name = name;
        this.displayName = name;
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

    public boolean isEvent() {
        return event;
    }

    public void setEvent(boolean event) {
        this.event = event;
    }

    public String getDisplayName() {
        return displayName == null ? name : displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
	public String toString() {
		return "Boat [name=" + name + "]";
	}
	
}
