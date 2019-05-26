package org.xi.myserver.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateASessionServlet extends MyServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        handle(req,resp);
    }

    private void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        

    }

}
