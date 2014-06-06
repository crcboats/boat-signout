package org.crc.boat.reservation.web;

public class JsonErrorResponse {
    
	public String Result = "ERROR";
	public String Message;
	public String ErrorType;
	
    public JsonErrorResponse(String message) {
		this.Message = message;
	}
	
}
