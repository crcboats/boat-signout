package com.cilogi.shiro.web;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet is run to cleanup expired sessions.  Since our
 * sessions are clustered, no individual runtime knows when they expire (nor
 * do we guarantee that runtimes survive to do cleanup), so we have to push
 * this determination out to an external sweeper like cron.
 *
 */
@Singleton
public class SessionCleanupServlet extends HttpServlet {

    static final String SESSION_ENTITY_TYPE = "_ah_SESSION";
    static final String EXPIRES_PROP = "_expires";

    // N.B.(schwardo): This must be less than 500, which is the maximum
    // number of entities that may occur in a single bulk delete call.
    static final int MAX_SESSION_COUNT = 400;

    private DatastoreService datastore;

    @Inject
    public SessionCleanupServlet() {
        super();
    }

    @Override
    public void init() {
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        if ("clear".equals(request.getQueryString())) {
            clearAll(response);
        } else {
            sendForm(request.getRequestURI() + "?clear", response);
        }
    }

    private void clearAll(HttpServletResponse response) {
        Query query = new Query(SESSION_ENTITY_TYPE);
        query.setKeysOnly();
        query.addFilter(EXPIRES_PROP, Query.FilterOperator.LESS_THAN,
                System.currentTimeMillis());
        ArrayList<Key> killList = new ArrayList<Key>();
        Iterable<Entity> entities = datastore.prepare(query).asIterable(
                FetchOptions.Builder.withLimit(MAX_SESSION_COUNT));
        for (Entity expiredSession : entities) {
            Key key = expiredSession.getKey();
            killList.add(key);
        }
        datastore.delete(killList);
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            response.getWriter().println("Cleared " + killList.size() + " expired sessions.");
        } catch (IOException ex) {
            // We still did the work, and successfully... just send an empty body.
        }
    }

    private void sendForm(String actionUrl, HttpServletResponse response) {
        Query query = new Query(SESSION_ENTITY_TYPE);
        query.setKeysOnly();
        query.addFilter(EXPIRES_PROP, Query.FilterOperator.LESS_THAN,
                System.currentTimeMillis());
        int count = datastore.prepare(query).countEntities();

        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.println("<html><head><title>Session Cleanup</title></head>");
            writer.println("<body>There are currently " + count + " expired sessions.");
            writer.println("<p><form method=\"POST\" action=\"" + actionUrl + "\">");
            writer.println("<input type=\"submit\" value=\"Delete Next 100\" >");
            writer.println("</form></body></html>");
        } catch (IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().println(ex);
            } catch (IOException innerEx) {
                // we lose notifying them what went wrong.
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}