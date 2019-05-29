package org.xi.myserver.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateSessionDataServlet extends MyServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        handle(req,resp);
    }

    private void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        System.out.println("sssssss");
    }

}
