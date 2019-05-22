package org.xi.myserver.servlets;

import org.xi.myserver.db.SqlUtiClass;
import org.xi.myserver.pojo.SQLReturnDataClass;
import org.xi.myserver.utils.SQLStatusCODEList;
import org.xi.myserver.utils.StatusCodeList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
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
        String id_str = request.getParameter("id");
        if(id_str != null) {
            System.out.println(id_str);


//            int uid = 0;
//            try{
//                uid = Integer.parseInt(id_str);
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//            if(uid != 0) {
//                //check id exists
//                SQLReturnDataClass sqlReturnDataClass = SqlUtiClass.getCountWithId(uid);
//                if(sqlReturnDataClass.DB_ERR_CODE == SQLStatusCODEList.DB_OK) {
//                    boolean exist = (boolean) sqlReturnDataClass.payload;
//                    if(exist) {
//                        sqlReturnDataClass = SqlUtiClass.deleteUserWithId(uid);
//                        if(sqlReturnDataClass.DB_ERR_CODE == SQLStatusCODEList.DB_OK) {
//                            if((int)sqlReturnDataClass.payload > 0) {
//                                map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_OK));
//                            }  else {
//                                map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_USER_DATA_NOT_WRITE_SUCCESS));
//                            }
//                        }
//                    }else {
//                        map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_SQL_ID_NOT_EXIST));
//                    }
//                }else {
//                    map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_PARAMETER_NOT_EQUALS));
//                }
//            }else {
//                map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_PARAMETER_NOT_EQUALS));
//            }
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
