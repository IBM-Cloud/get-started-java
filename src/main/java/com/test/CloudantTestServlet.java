package com.test;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudant.client.api.Database;
import com.cloudant.client.org.lightcouch.NoDocumentException;

@WebServlet("/cloudant")
public class CloudantTestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Resource(name = "cloudant/connector")
    protected Database db;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = "example_id";
        ExampleDocument document;
        try {
            try {
                document = db.find(ExampleDocument.class, id);
                response.getWriter().println("Updated a doc: " + document);
            } catch (NoDocumentException e) {
                document = new ExampleDocument(id, new Date());
                db.save(document);
                response.getWriter().println("Added a new doc: " + document);
            }
            response.getWriter().println("Test passed.");
        } catch (Exception e) {
            response.getWriter().println(e.getMessage());
        }
    }

    // A Java type that can be serialized to JSON
    public static class ExampleDocument {
        private final String _id;
        private final String _rev = null;
        private final Date date;

        public ExampleDocument(String id, Date date) {
            this._id = id;
            this.date = date;
        }

        @Override
        public String toString() {
            return "{ id: " + _id + ", rev: " + _rev + ", date: \"" + String.valueOf(date) + "\"}";
        }
    }
}
