package com.example.demo.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/")
public class SimpleHelloServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        // ✅ CRITICAL FIX: clear previous response data
        resp.resetBuffer();

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/plain");

        // ✅ EXACT output required by tests
        resp.getWriter().print("Hello Servlet");
    }

    @Override
    public String getServletInfo() {
        return "SimpleHelloServlet";
    }
}
