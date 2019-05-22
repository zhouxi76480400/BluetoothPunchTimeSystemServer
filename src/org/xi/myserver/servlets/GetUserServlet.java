package org.xi.myserver.servlets;

import com.google.gson.Gson;
import org.xi.myserver.db.SqlUtiClass;
import org.xi.myserver.pojo.GetUserReturnPOJO;
import org.xi.myserver.pojo.SQLReturnDataClass;
import org.xi.myserver.pojo.StudentInformationObject;
import org.xi.myserver.utils.SQLStatusCODEList;
import org.xi.myserver.utils.StatusCodeList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GetUserServlet extends MyServlet {

    private static final int DEFAULT_C = 10;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        query(req,resp);
        super.doPost(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    private void query(HttpServletRequest request, HttpServletResponse response) {
        //check params first
        //l for last id
        //c for one page how many users
        // is no any params return the first page
        String l_str = request.getParameter("l");
        String c_str = request.getParameter("c");
        int l = 0;
        int c;
        if(c_str == null) {
            c = DEFAULT_C;
        }else {
            c = Integer.parseInt(c_str);
        }
        if(l_str != null) {
            l = Integer.parseInt(l_str);
        }
        List<StudentInformationObject> studentInformationObjectList = new ArrayList<>();
        SQLReturnDataClass sqlReturnDataClass = SqlUtiClass.getUserList(l,c);
        if(sqlReturnDataClass.DB_ERR_CODE == SQLStatusCODEList.DB_OK) {
            studentInformationObjectList.
                    addAll((Collection<? extends StudentInformationObject>) sqlReturnDataClass.payload);
        }
        boolean isLastPage = false;
        if(studentInformationObjectList.size() < c) {
            isLastPage = true;
        }
        GetUserReturnPOJO getUserReturnPOJO = new GetUserReturnPOJO();
        getUserReturnPOJO.data = studentInformationObjectList;
        getUserReturnPOJO.end_page = isLastPage;
        getUserReturnPOJO.s = String.valueOf(StatusCodeList.STATUS_CODE_OK);
        Gson gson = new Gson();
        String json = gson.toJson(getUserReturnPOJO);
        try {
            response.addHeader("Content-type","application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        get_fake_data();
    }

    private void get_fake_data() {
        String a = "INSERT INTO `punchtimesystem`.`user_list` (`mac_address`, `student_number`, `last_name`, `first_name`) VALUES ('%s', '%s', '%s', '%s');";
        for (int i = 0 ; i < 200 ; i++) {
            String hex = Integer.toHexString(i);
            if(hex.length() == 1) {
                hex = "0"+hex;
            }
            hex = hex.toUpperCase();
            String reverseHex = Integer.toHexString(200 - i);
            if(reverseHex.length() == 1) {
                reverseHex = "0" + reverseHex;
            }
            reverseHex = reverseHex.toUpperCase();

            String mac = "70BF8EBA59" + hex;
            String first_name = hex;
            String last_name = reverseHex;
            String sn = String.valueOf(i);
            System.out.println(String.format(a,mac,sn,first_name,last_name));

        }
    }

}
