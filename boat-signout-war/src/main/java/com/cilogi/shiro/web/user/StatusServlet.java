// Copyright (c) 2011 Tim Niblett All Rights Reserved.
//
// File:        StatusServlet.java  (02-Nov-2011)
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

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;

import com.cilogi.shiro.gae.GaeUser;
import com.cilogi.shiro.gae.GaeUserDAO;
import com.cilogi.shiro.web.BaseServlet;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Get the current session status via Ajax.  Gets information about whether the
 * user is known (remembered) and authenticated.  You are authenticated only if
 * you logged in during this session.
 * <p> Note that any client-side record of status can't be definitive -- checks are
 * always run server-side to avoid scams.  However this information can be used to
 * set up display options.
 */
@Singleton
public class StatusServlet extends BaseServlet {
    static final Logger LOG = Logger.getLogger(StatusServlet.class.getName());
    ObjectMapper mapper = new ObjectMapper();
    
    
    @Inject
    StatusServlet(Provider<GaeUserDAO> daoProvider) {
        super(daoProvider);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
       if(request.getRequestURI().contains("whoami")){
           mapper.writeValue(response.getOutputStream(), getCurrentGaeUser());
           return;
       }
        
        
        LOG.info("status GET");
        try {
            Subject subject = SecurityUtils.getSubject();
            boolean isKnown = subject.isAuthenticated();
            if (isKnown) {
                GaeUser currentGaeUser = getCurrentGaeUser();
                LOG.info("status, known: " + currentGaeUser.getName());
                issueJson(response, HTTP_STATUS_OK,
                        MESSAGE, "known",
                        "name", currentGaeUser.getName(),
                        "displayName", currentGaeUser.getDisplayName(),
                        "authenticated", Boolean.toString(subject.isAuthenticated()),
                        "admin", Boolean.toString(hasRole(subject, "admin")));
            } else {
                LOG.info("status, unknown");
                issueJson(response, HTTP_STATUS_OK,
                        MESSAGE, "unknown",
                        "name", "",
                        "authenticated", "false",
                        "admin", "false");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Unable to get status", e);
            issue(MIME_TEXT_PLAIN, HTTP_STATUS_INTERNAL_SERVER_ERROR,
                  "Internal error getting status: " + e.getMessage(), response);
        }
    }

    private static boolean hasRole(Subject subject, String role) {
        try {
            subject.checkRole(role);
            return true;
        }  catch (AuthorizationException e)  {
            return false;
        }
    }

}
