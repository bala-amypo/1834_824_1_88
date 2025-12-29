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

        // ✅ THIS IS THE CRITICAL FIX
        resp.reset();

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/plain");

        resp.getWriter().write("Hello Servlet");
    }

    @Override
    public String getServletInfo() {
        return "SimpleHelloServlet";
    }
}
