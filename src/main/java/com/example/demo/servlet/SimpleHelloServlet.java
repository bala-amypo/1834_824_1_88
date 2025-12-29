package com.example.demo.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleHelloServlet extends HttpServlet {

    // ✅ MUST BE PUBLIC
    @Override
    public void doGet(HttpServletRequest req,
                      HttpServletResponse resp) throws IOException {

        resp.setStatus(200);
        resp.setContentType("text/plain");
        resp.getWriter().write("Hello from Simple Servlet");
    }
}
