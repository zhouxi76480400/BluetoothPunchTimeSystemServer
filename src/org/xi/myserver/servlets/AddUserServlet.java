package org.xi.myserver.servlets;

import com.google.gson.Gson;
import org.xi.myserver.pojo.StudentInformationObject;
import org.xi.myserver.utils.CharsetUtil;
import org.xi.myserver.utils.StatusCodeList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddUserServlet extends MyServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req,resp);
        super.doPost(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    private void handle(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String,String> map = new HashMap<>();
        map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_PARAMETER_NOT_EQUALS));
        if(parameterMap.size() == 2) {
            boolean isEdit = false;
            try {
                isEdit = Boolean.getBoolean(CharsetUtil.getUTF_8String(request.getParameter("e")));
            }catch (Exception e) {
                e.printStackTrace();
            }
            String json = CharsetUtil.getUTF_8String(request.getParameter("d"));
            StudentInformationObject object = null;
            if(json != null && json.length() != 0) {
                Gson gson = new Gson();
                object = gson.fromJson(json, StudentInformationObject.class);

                System.out.println("test");
            }else {
                map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_JSON_CONVERT_FAILED));
            }
        }else {
            map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_PARAMETER_NOT_EQUALS));
        }
        try {
            response.getWriter().print(generateResponseString(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
