package org.crc.boat.reservation.web;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.crc.boat.reservation.dao.SettingsDao;
import org.crc.boat.reservation.model.Settings;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Singleton
@SuppressWarnings("serial")
public class SystemSettingsServlet extends HttpServlet {
    static final Logger LOG = Logger.getLogger(SystemSettingsServlet.class.getName());

    private SettingsDao settingsDao;

    @Inject
    SystemSettingsServlet(SettingsDao settingsDAO) {
        this.settingsDao = settingsDAO;
    }

    ObjectMapper mapper = new ObjectMapper();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        list(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws JsonParseException, JsonMappingException, IOException {
        String action = StringUtils.substringAfterLast(req.getRequestURI(), "/");
        switch (action) {
        case "post":
            creatUpdate(req, resp);
            break;

        case "jsonlist":
            jsonlist(req, resp);
            break;

        case "delete":
            delete(req, resp);
            break;

        default:
            list(req, resp);
            break;
        }

    }

    protected void list(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonResponse result = new JsonResponse(settingsDao.listSettings());
        mapper.writeValue(resp.getOutputStream(), result);

    }

    protected void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        settingsDao.deleteSettings(req.getParameter("key"));
        mapper.writeValue(resp.getOutputStream(),  new JsonResponse(null));
    }

    protected void creatUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Settings savedSettings = settingsDao.saveSettings(new Settings(req.getParameter("key"), req.getParameter("value")));
        JsonResponse result = new JsonResponse(savedSettings);
        mapper.writeValue(resp.getOutputStream(), result);
    }
    
    protected void jsonlist(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Settings> settingsList = mapper.readValue(req.getParameter("settings"), new TypeReference<List<Settings>>(){});
        for (Settings settings : settingsList) {
            settingsDao.saveSettings(settings);
        }
    }

}
