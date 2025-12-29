package com.example.demo.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/")
public class SimpleHelloServlet extends HttpServlet {

    // ✅ PUBLIC so tests can call directly
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/plain");

        // ✅ EXACT STRING REQUIRED BY TEST
        resp.getWriter().write("Hello Servlet");
    }

    // ✅ REQUIRED BY t4_servletInfo
    @Override
    public String getServletInfo() {
        return "SimpleHelloServlet";
    }
}
