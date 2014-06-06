package org.crc.boat.reservation.web;

import java.util.List;

public class JsonResponse {
    
	public String Result;
	public Object Records;
	public Object Record;
	
    public JsonResponse(Object data) {
		Result = "OK";
		if(data instanceof List<?>){
		    Records = data;
		}else{
		    Record = data;
		}
	}
	
}
