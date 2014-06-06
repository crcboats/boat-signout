// Copyright (c) 2011 Tim Niblett All Rights Reserved.
//
// File:        SettingsServlet.java  (14-Nov-2011)
// Author:      tim

//
// Copyright in the whole and every part of this source file belongs to
// Tim Niblett (the Author) and may not be used,
// sold, licenced, transferred, copied or reproduced in whole or in
// part in any manner or form or in or on any media to any person
// other than in accordance with the terms of The Author's agreement
// or otherwise without the prior written consent of The Author.  All
// information contained in this source file is confidential information
// belonging to The Author and as such may not be disclosed other
// than in accordance with the terms of The Author's agreement, or
// otherwise, without the prior written consent of The Author.  As
// confidential information this source file must be kept fully and
// effectively secure at all times.
//


package com.cilogi.shiro.web.user;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.cilogi.shiro.gae.GaeUser;
import com.cilogi.shiro.gae.GaeUserDAO;
import com.cilogi.shiro.web.BaseServlet;

@Singleton
public class SettingsServlet extends BaseServlet {
    static final Logger LOG = Logger.getLogger(SettingsServlet.class.getName());

    @Inject
    SettingsServlet(Provider<GaeUserDAO> daoProvider) {
        super(daoProvider);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String command = StringUtils.substringAfterLast(request.getRequestURI(), "/");
            boolean profileUpdate = "profile".equals(command);
            
            GaeUserDAO dao = new GaeUserDAO();
            String userName = request.getParameter(USERNAME);
            String password = request.getParameter(PASSWORD);
            String displayName = request.getParameter(DISPLAYNAME);
            String bio = request.getParameter(BIO);
            String contactInfo = request.getParameter(CONTACT_INFO);

            Subject subject = SecurityUtils.getSubject();
            String subjectID = (String)subject.getPrincipal();
            GaeUser user = dao.findUser(subjectID);
            if (subject.isAuthenticated() && user != null) {
                if (userName.equals(subjectID)) {
                    if (StringUtils.isNotBlank(password)) {
                        user.setPassword(password);
                    }
                    if (StringUtils.isNotBlank(displayName)) {
                        user.setDisplayName(displayName);
                    }
                    if(profileUpdate){
                        user.setBio(bio);
                        user.setContactInfo(contactInfo);
                    }
                    if(StringUtils.isNotBlank(password) || StringUtils.isNotBlank(displayName)){
                    	issueJson(response, HTTP_STATUS_OK, MESSAGE, "password changed successfully");
                    	dao.saveUser(user, false);
                    }

                } else {
                    LOG.warning("username (" + userName +") does not match (" + subjectID + ")");
                    issue(MIME_TEXT_PLAIN, HTTP_STATUS_NOT_FOUND, "There is a problem authentication. Please try signing out and log in again" , response);
                }
            }  else {
                if (user == null) {
                    issue(MIME_TEXT_PLAIN, HTTP_STATUS_FORBIDDEN, "You're not a user I can set the password for", response);
                }  else {
                    issue(MIME_TEXT_PLAIN, HTTP_STATUS_FORBIDDEN, "You're not authenticated", response);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Oops, error in settings", e);
            issue(MIME_TEXT_PLAIN, HTTP_STATUS_INTERNAL_SERVER_ERROR,
                  "Oops, error in settings: " + e.getMessage(), response);
        }
    }
}
