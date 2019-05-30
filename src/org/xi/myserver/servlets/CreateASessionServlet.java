package org.xi.myserver.servlets;

import com.google.gson.Gson;
import org.xi.myserver.pojo.CreateSessionPOJO;
import org.xi.myserver.utils.FileUtil;
import org.xi.myserver.utils.StatusCodeList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateASessionServlet extends MyServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        handle(req,resp);
    }

    private void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Map<String,String> map = new HashMap<>();
        map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_PARAMETER_NOT_EQUALS));
        String str = httpServletRequest.getParameter("l");
        if(str != null) {
            CreateSessionPOJO createSessionPOJO = null;
            try {
                createSessionPOJO = new Gson().fromJson(str,CreateSessionPOJO.class);
            }catch (Exception e) {
                e.printStackTrace();
            }
            if(createSessionPOJO != null) {
                createSessionPOJO.create_time = System.currentTimeMillis();
                createSessionPOJO.sort_id = getLastFileCount() + 1;
                createSessionPOJO.uuid = UUID.randomUUID().toString();
                String return_json = createSessionFile(createSessionPOJO);
                map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_OK));
                map.put("d",return_json);
            }else {
                map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_JSON_CONVERT_FAILED));
            }
        }else {
            map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_PARAMETER_NOT_EQUALS));
        }
        try {
            httpServletResponse.getWriter().print(generateResponseString(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createSessionFile(CreateSessionPOJO createSessionPOJO) {
        File sessionPath = new File(FileUtil.getSessionPath(this),String.valueOf(createSessionPOJO.sort_id));
        sessionPath.mkdir();
        Gson gson = new Gson();
        String str = gson.toJson(createSessionPOJO);
        FileUtil.writeStringToFile(str,new File(sessionPath,FileUtil.JSON_CONFIGURE_FILE_NAME));
        File dataPath = new File(sessionPath,FileUtil.DATA_PATH_DIRECTORY_NAME);
        dataPath.mkdir();
        updateLastFileCount(createSessionPOJO.sort_id);
        return str;
    }

    private int getLastFileCount() {
        int now = FileUtil.getLastFileCount(new File(FileUtil.getSessionPath(this)));
        return now;
    }

    private void updateLastFileCount(int now) {
        FileUtil.updateLastFileCount(new File(FileUtil.getSessionPath(this)),now);
    }

}
