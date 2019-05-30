package org.xi.myserver.servlets;

import com.google.gson.Gson;
import org.xi.myserver.pojo.SessionDataPOJO;
import org.xi.myserver.pojo.UpdateSessionDataPOJO;
import org.xi.myserver.utils.FileUtil;
import org.xi.myserver.utils.StatusCodeList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UpdateSessionDataServlet extends MyServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req,resp);
        super.doPost(req, resp);
    }

    private void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Map<String,String> map = new HashMap<>();
        map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_PARAMETER_NOT_EQUALS));
        String json = httpServletRequest.getParameter("d");
        if(json != null && !json.isEmpty()) {
            Gson gson = new Gson();
            UpdateSessionDataPOJO updateSessionDataPOJO = null;
            try {
                updateSessionDataPOJO = gson.fromJson(json, UpdateSessionDataPOJO.class);
            }catch (Exception e) {
                e.printStackTrace();
            }
            if(updateSessionDataPOJO != null) {
                File path = new File(FileUtil.getSessionPath(this),String.valueOf(updateSessionDataPOJO.sid));
                if(path.exists() && path.isDirectory()) {
                    SessionDataPOJO pojo = new SessionDataPOJO();
                    pojo.create_time = updateSessionDataPOJO.time;
                    pojo.mac_addresses = updateSessionDataPOJO.mac;
                    boolean isOK = writeJSONToFile(path,gson.toJson(pojo));
                    if(isOK) {
                        map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_OK));
                    }else {
                        map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_UPDATE_SESSION_DATA_FAILED));
                    }
                }else {
                    map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_SESSION_ID_NOT_EXISTS));
                }
            }else {
                map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_PARAMETER_NOT_EQUALS));
            }
        }
        try {
            httpServletResponse.getWriter().print(generateResponseString(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean writeJSONToFile(File path, String json) {
        boolean isOK = false;
        if(path != null && json != null) {
            try {
                File data_path = new File(path,FileUtil.DATA_PATH_DIRECTORY_NAME);
                int last_number = FileUtil.getLastFileCount(data_path);
                int now = last_number + 1;
                File full_path = new File(data_path,String.format("%d.json",now));
                FileUtil.writeStringToFile(json,full_path);
                FileUtil.updateLastFileCount(data_path,now);
                isOK = true;
            }catch (Exception e) {e.printStackTrace();}
        }
        return isOK;
    }

}
