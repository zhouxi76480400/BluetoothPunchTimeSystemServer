package org.xi.myserver.servlets;

import com.google.gson.Gson;
import org.xi.myserver.db.SqlUtiClass;
import org.xi.myserver.pojo.SQLReturnDataClass;
import org.xi.myserver.utils.SQLStatusCODEList;
import org.xi.myserver.utils.StatusCodeList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * parameter id
 */
public class DeleteUserServlet extends MyServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleData(req,resp);
        super.doPost(req, resp);
    }

    private void handleData(HttpServletRequest request, HttpServletResponse response) {
        Map<String,String> map = new HashMap<>();
        String list_str = request.getParameter("l");
        if(list_str != null) {
            List<Integer> integerList = new ArrayList<>();
            try {
                Gson gson = new Gson();
                int[] array = gson.fromJson(list_str,int[].class);
                for(int i : array) {
                    integerList.add(i);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            if(integerList.size() > 0) {
                SQLReturnDataClass sqlReturnDataClass = SqlUtiClass.removeUsers(integerList);
                if(sqlReturnDataClass.DB_ERR_CODE == SQLStatusCODEList.DB_OK) {
                    map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_OK));
                }else {
                    map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_SQL_REMOVE_USER_FAILED));
                }
            }else {
                map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_PARAMETER_NOT_EQUALS));
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
