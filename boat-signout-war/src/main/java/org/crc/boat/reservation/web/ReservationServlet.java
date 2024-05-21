package org.crc.boat.reservation.web;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.crc.boat.reservation.dao.ReservationDao;
import org.crc.boat.reservation.model.Reservation;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.cilogi.shiro.gae.GaeUser;
import com.cilogi.shiro.gae.GaeUserDAO;
import com.cilogi.shiro.web.BaseServlet;
import com.fasterxml.jackson.databind.ObjectMapper;

@Singleton
@SuppressWarnings("serial")
public class ReservationServlet extends BaseServlet  {


    static final Logger LOG = Logger.getLogger(ReservationServlet.class.getName());

    
    private ReservationDao reservationDao;
    ObjectMapper mapper = new ObjectMapper();
    final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd h:mm a").withZone(DateTimeZone.forID("America/Toronto"));
    
    private enum PARAMETER {
        boat,
        reserveDate,
        reserveTime,
        duration,
        units,
        allowConflicts,
        from,
        to
    };
    
    private static final Map<String, Long> UNITS_TO_SECONDS_MAP = new HashMap<String, Long>(){{
        put("Minutes", 1000L * 60L);
        put("Hours", 1000L * 60L * 60L);
        put("Days", 1000L * 60L * 60L * 24L);
    }};
    
    private static final Map<PARAMETER, String> REQUIRED_PARAMETERS = new HashMap<PARAMETER, String>(){{
        put(PARAMETER.boat, "Please select a boat.");
        put(PARAMETER.reserveDate, "Please select a date.");
        put(PARAMETER.reserveTime, "Please select a time.");
        put(PARAMETER.duration, "Please select a duration.");
        put(PARAMETER.units, "Please specify the duration unit");
    }};
    
    @Inject
    ReservationServlet(ReservationDao reservationDAO, Provider<GaeUserDAO> daoProvider) {
        super(daoProvider);
        this.reservationDao = reservationDAO;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        GaeUser currentGaeUser = getCurrentGaeUser();
        if(currentGaeUser == null){
            LOG.info("User not logged in.");
            resp.sendRedirect("/logout");
            return;
        }

        List<String> errors = new ArrayList<>();
        String from = getAndValidate(PARAMETER.from, req, errors);
        String to = getAndValidate(PARAMETER.to, req, errors);
        if(!errors.isEmpty()){
            mapper.writeValue(resp.getOutputStream(), new JsonErrorResponse(StringUtils.join(errors, "<br>")));
            return;
        }
        
        List<Reservation> reservations = reservationDao.listReservations(Long.valueOf(from), Long.valueOf(to));
        for (Reservation reservation : reservations) {
            reservation.setCanDelete(currentGaeUser.getName().equals(reservation.getUserName()));
        }
        mapper.writeValue(resp.getOutputStream(), new CalendarResponse(reservations));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> errors = new ArrayList<>();
        String boat = getAndValidate(PARAMETER.boat, req, errors);
        String reserveDate = getAndValidate(PARAMETER.reserveDate, req, errors);
        String reserveTime = getAndValidate(PARAMETER.reserveTime, req, errors);
        String durationString = getAndValidate(PARAMETER.duration, req, errors);
        String units = getAndValidate(PARAMETER.units, req, errors);
        if(!errors.isEmpty()){
            mapper.writeValue(resp.getOutputStream(), new JsonErrorResponse(StringUtils.join(errors, "<br>")));
            return;
        }
        String allowConflicts = getAndValidate(PARAMETER.allowConflicts, req, errors);
        DateTime startDate = null;
        try {
            startDate = dateTimeFormatter.parseDateTime(reserveDate + " " + reserveTime.toUpperCase());
        } catch (Exception e) {
            mapper.writeValue(resp.getOutputStream(), new JsonErrorResponse("Date and time is not valid."));
            return;
        }
        
        long duration;
        try {
            duration = Long.valueOf(durationString);
        } catch (NumberFormatException e1) {
            mapper.writeValue(resp.getOutputStream(), new JsonErrorResponse("Duration is not a valid number."));
            return;
        }
        
        Long unitInSeconds = UNITS_TO_SECONDS_MAP.get(units.trim());
        if(unitInSeconds == null){
            mapper.writeValue(resp.getOutputStream(), new JsonErrorResponse("Duration unit is not recognized: " + units));
            return;
        }
        
        long start = startDate.getMillis();
        long end = start + duration * unitInSeconds;
        
        GaeUser user = getCurrentGaeUser();
        if (user == null) {
            issueJson(resp, HTTP_STATUS_FORBIDDEN, MESSAGE, "Session expired");
            return;
        }
        Reservation reservation = new Reservation();
        reservation.setBoatName(boat);
        reservation.setUserName(user.getName());
        String displayName = user.getDisplayName();
        if(StringUtils.isBlank(displayName)){
            displayName = user.getName().replaceAll("@.*", "");
        }
        reservation.setUserDisplayName(displayName);
        reservation.setTitle(MessageFormat.format("{0} - {1}", reservation.getBoatName(), reservation.getUserDisplayName()));
        reservation.setStart(start);
        reservation.setEnd(end);
        reservation.setPictureUrl(StringUtils.isBlank(user.getPictureUrl()) ? "img/rower.png" : user.getPictureUrl());
        
        if(!"true".equals(allowConflicts)){
            List<Reservation> conflicts = reservationDao.hasConflict(reservation);
            if(conflicts != null && !conflicts.isEmpty()){
                List<String> messages = new ArrayList<>();
                for (Reservation r : conflicts) {
                    messages.add(MessageFormat.format("{0} to {1} {2}", 
                            dateTimeFormatter.print(r.getStart()).toLowerCase(), 
                            dateTimeFormatter.print(r.getEnd()).toLowerCase(), 
                            r.getUserDisplayName()));
                }
                JsonErrorResponse error = new JsonErrorResponse(StringUtils.join(messages, "<br>"));
                error.ErrorType = "CONFLICT";
                mapper.writeValue(resp.getOutputStream(), error);
                return;
            }
        }
        
        reservationDao.saveReservation(reservation);
        
        mapper.writeValue(resp.getOutputStream(), new JsonResponse(null));
        
    }

    private String getAndValidate(PARAMETER parameter, HttpServletRequest req, List<String> errors){
         String value = req.getParameter(parameter.toString());
         if(StringUtils.isBlank(value)){
             errors.add(REQUIRED_PARAMETERS.get(parameter));
         }
         return value;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = StringUtils.substringAfterLast(req.getRequestURI(), "/");
        if(!StringUtils.isBlank(id)){
            reservationDao.deleteReservation(Long.parseLong(id.trim()));
        }
        mapper.writeValue(resp.getOutputStream(), new JsonResponse(null));
    }
    
    
    
    
    
}
