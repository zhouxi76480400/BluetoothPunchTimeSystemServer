package org.xi.myserver.utils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import java.io.File;

public class FileUtil {

    private static final String session_dir_str = "SESSIONS";

    public static String getSessionPath(HttpServlet servlet) {
        ServletContext context = servlet.getServletContext();
        String context_path = context.getRealPath("");
        File data_path_file = new File(context_path,session_dir_str);
        if(!data_path_file.exists()) {
            data_path_file.mkdir();
        }
        return data_path_file.toString();
    }

}
