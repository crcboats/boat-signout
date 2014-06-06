package org.crc.boat.reservation.web;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.crc.boat.reservation.dao.ProfilePictureDao;

import com.cilogi.shiro.gae.GaeUser;
import com.cilogi.shiro.gae.GaeUserDAO;
import com.cilogi.shiro.web.BaseServlet;
import com.cilogi.util.Crypto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

@Singleton
@SuppressWarnings("serial")
public class ProfileServlet extends BaseServlet {
    static final Logger LOG = Logger.getLogger(ProfileServlet.class.getName());

    private ProfilePictureDao profilePictureDao;

    ObjectMapper mapper = new ObjectMapper();

    @Inject
    public ProfileServlet(ProfilePictureDao profilePictureDao, Provider<GaeUserDAO> daoProvider) {
        super(daoProvider);
        this.profilePictureDao = profilePictureDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = StringUtils.substringAfterLast(req.getRequestURI(), "_");
        String userName = null;
        if(!StringUtils.isBlank(id) ){
            try {
                userName = Crypto.decrypt(id);
            } catch (Exception e) {
                LOG.info(e.getMessage());
                return;
            }
        }
        GaeUser currentUser = getCurrentGaeUser();
        if(currentUser == null){
            resp.sendRedirect("/logout");
            return;
        }
        if(StringUtils.isBlank(id) || currentUser.getName().equals(userName) || userName == null){
            showView(resp, "editprofile.ftl", mapUser(currentUser));
        }else{
            GaeUser user = daoProvider.get().findUser(userName);
            showView(resp, "viewprofile.ftl", mapUser(user));
        }
    }

    private Map<String, Object> mapUser(GaeUser user) {
        Map<String,Object> map = Maps.newHashMap();
        map.put("staticBaseUrl", "/");
        if(user != null){
            map.put("username", user.getName());
            map.put("displayName", user.getDisplayName());
            map.put("pictureUrl", user.getPictureUrl());
            map.put("contactInfo", user.getContactInfo());
            map.put("bio", user.getBio());
        }
        return map;
    }



}
