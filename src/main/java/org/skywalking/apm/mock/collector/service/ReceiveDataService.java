package org.skywalking.apm.mock.collector.service;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReceiveDataService extends HttpServlet {
    public static final String SERVLET_PATH = "/receiveData";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/x-yaml");
        resp.setCharacterEncoding("utf-8");
        resp.setStatus(200);

        PrintWriter out = resp.getWriter();
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
