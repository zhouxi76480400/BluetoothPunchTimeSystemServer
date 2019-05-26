package org.xi.myserver.servlets;


import org.xi.myserver.utils.FileUtil;
import org.xi.myserver.utils.SQLStatusCODEList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class GetAllSessionsServlet extends MyServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        handle(req,resp);
    }

    private void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        System.out.println(FileUtil.getSessionPath(this));

//        select TABLE_NAME from information_schema.tables where table_schema='punchtimesystem' and table_name like '%s%'
    }

}
