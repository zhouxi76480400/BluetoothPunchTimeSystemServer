package org.xi.myserver.servlets;

import com.google.gson.Gson;
import org.xi.myserver.db.CreateSqlStatementClass;
import org.xi.myserver.db.SqlUtiClass;
import org.xi.myserver.pojo.SQLReturnDataClass;
import org.xi.myserver.pojo.StudentInformationObject;
import org.xi.myserver.utils.CharsetUtil;
import org.xi.myserver.utils.SQLStatusCODEList;
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
        System.out.println(parameterMap.toString());
        Map<String,String> map = new HashMap<>();
        map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_PARAMETER_NOT_EQUALS));
        if(parameterMap.size() == 2) {
            boolean isEdit = false;
            try {
                isEdit = Boolean.valueOf(CharsetUtil.getUTF_8String(request.getParameter("e")));
            }catch (Exception e) {
                e.printStackTrace();
            }
            String json = CharsetUtil.getUTF_8String(request.getParameter("d"));
            System.out.println(request.getParameter("d"));
            System.out.println(json);
            StudentInformationObject object = null;
            if(json != null && json.length() != 0) {
                Gson gson = new Gson();
                object = gson.fromJson(json, StudentInformationObject.class);
                boolean isMACOK = checkMACAddressLengthOK(object.mac_address);
                boolean isSMOK = checkStrIsLongerThan0(object.student_number);
                boolean isLNOK = checkStrIsLongerThan0(object.last_name);
                boolean isFNOK = checkStrIsLongerThan0(object.first_name);
                if(isMACOK && isSMOK && isLNOK && isFNOK) {
                    if(isEdit) {
                        long id = object.id;
                        if(id > 0) {
                            modifyData(map,object);
                        }else {
                            map.put("s", String.valueOf(StatusCodeList.
                                    STATUS_CODE_SQL_ID_NOT_EXIST));
                        }
                    }else {
                        // check mac is uq
                        SQLReturnDataClass isMACUQ =
                                SqlUtiClass.checkUQ(CreateSqlStatementClass.user_list,
                                        "mac_address",object.mac_address);
                        if(isMACUQ.DB_ERR_CODE == SQLStatusCODEList.DB_OK) {
                            if(isMACUQ.OPT_ERR_CODE == SQLStatusCODEList.OPT_UNIQUE_STATUS_NOT_EXIST) {
                                // check student number is uq
                                SQLReturnDataClass isSNUQ =
                                        SqlUtiClass.checkUQ(CreateSqlStatementClass.user_list,
                                                "student_number",object.student_number);
                                if(isSNUQ.DB_ERR_CODE == SQLStatusCODEList.DB_OK) {
                                    if(isSNUQ.OPT_ERR_CODE == SQLStatusCODEList.OPT_UNIQUE_STATUS_NOT_EXIST) {
                                        // write
                                        SQLReturnDataClass isSuccessful = SqlUtiClass.writeNewUserToDB(object);
                                        if(isSuccessful.DB_ERR_CODE == SQLStatusCODEList.DB_OK) {
                                            map.put("s", String.valueOf(StatusCodeList.
                                                    STATUS_CODE_OK));
                                            int id =  ((Integer)isSuccessful.payload).intValue();
                                            map.put("id",String.valueOf(id));
                                        }else {
                                            map.put("s", String.valueOf(StatusCodeList.
                                                      STATUS_CODE_USER_DATA_NOT_WRITE_SUCCESS));
                                        }
                                    }else {
                                        map.put("s", String.valueOf(StatusCodeList.STATUS_CODE_STUDENT_NUMBER_EXIST));
                                    }
                                }else {
                                    map.put("s", String.valueOf(StatusCodeList.
                                            STATUS_CODE_USER_DATA_NOT_WRITE_SUCCESS));
                                }
                            }else {
                                map.put("s", String.valueOf(StatusCodeList.STATUS_CODE_MAC_ADDRESS_EXIST));
                            }
                        }else {
                            map.put("s", String.valueOf(StatusCodeList.STATUS_CODE_USER_DATA_NOT_WRITE_SUCCESS));
                        }
                    }
                }else {
                    map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_JSON_PARAMETER_NOT_EQUALS));
                }
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

    /**
     * Modify Exists Data
     * @param object
     */
    private void modifyData(Map<String, String> map, StudentInformationObject object) {
        if(object != null) {
            SQLReturnDataClass sqlReturnDataClass = SqlUtiClass.modifyUserToDB(object);
            if(sqlReturnDataClass.DB_ERR_CODE == SQLStatusCODEList.DB_OK) {
                if(sqlReturnDataClass.OPT_ERR_CODE == SQLStatusCODEList.DB_OK) {
                    map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_OK));
                }else {
                    map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_SQL_ID_NOT_EXIST));
                }
            }else {
                map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_USER_DATA_NOT_WRITE_SUCCESS));
            }

        }
    }

    private boolean checkMACAddressLengthOK(String mac) {
        int length = CharsetUtil.checkStringLength(mac);
        boolean isOK = false;
        if(length == 12) {
            isOK = true;
        }
        return isOK;
    }

    private boolean checkStrIsLongerThan0(String str) {
        boolean isOK = false;
        if(CharsetUtil.checkStringLength(str) > 0){
            isOK = true;
        }
        return isOK;
    }
}
