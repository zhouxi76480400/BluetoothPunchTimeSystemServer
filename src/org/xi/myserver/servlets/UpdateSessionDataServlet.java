package org.xi.myserver.servlets;

import com.google.gson.Gson;
import org.xi.myserver.pojo.UpdateSessionDataPOJO;
import org.xi.myserver.utils.StatusCodeList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            UpdateSessionDataPOJO updateSessionDataPOJO = gson.fromJson(json, UpdateSessionDataPOJO.class);




            System.out.println(updateSessionDataPOJO.sid);
        }
        try {
            httpServletResponse.getWriter().print(generateResponseString(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
