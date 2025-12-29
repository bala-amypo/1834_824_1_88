package com.example.demo.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/")
public class SimpleHelloServlet extends HttpServlet {

    // ✅ MUST BE PUBLIC (tests call it directly)
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("text/plain");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("Hello");
    }

    // ✅ Required for t4_servletInfo
    @Override
    public String getServletInfo() {
        return "Simple Hello Servlet";
    }
}
