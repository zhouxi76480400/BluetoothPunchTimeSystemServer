package org.xi.myserver.utils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import java.io.*;

public class FileUtil {

    public static final String JSON_CONFIGURE_FILE_NAME = "config.json";

    public static final String DATA_PATH_DIRECTORY_NAME = "data";

    public static final String FILE_NAME_LAST_CREATE_COUNT_FILE = "last_counter";

    private static final String session_dir_str = "sessions";

    public static String getSessionPath(HttpServlet servlet) {
//        ServletContext context = servlet.getServletContext();
//        String context_path = context.getRealPath("");
//        File data_path_file = new File(context_path,session_dir_str);
//        if(!data_path_file.exists()) {
//            data_path_file.mkdir();
//        }

//        return data_path_file.toString();
        return "/Users/zhouxi/sessions";
    }

    public static void writeStringToFile(String s,File path) {
        if(path != null) {
            if(!path.exists()) {
                try {
                    path.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(path);
                fileOutputStream.write(s.getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readStringFromFile(File path) {
        String str = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte [] buffer = new byte[1024];
            int len;
            while ((len = fileInputStream.read(buffer,0,buffer.length)) != -1) {
                byteArrayOutputStream.write(buffer,0,len);
            }
            str = byteArrayOutputStream.toString();
            fileInputStream.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

}
