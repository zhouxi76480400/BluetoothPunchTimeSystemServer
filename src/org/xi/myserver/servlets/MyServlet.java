package org.xi.myserver.servlets;

import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.SysexMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

public class MyServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setRequest(req);
        setResponse(resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    private void setResponse(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Access-Control-Allow-Origin","*");
    }

    private void setRequest(HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String generateResponseString(Map<String,String> map) {
        if(map != null) {
            JsonObject jsonObject = new JsonObject();
            Iterator<Map.Entry<String,String>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String,String> entry = iterator.next();
                String key = entry.getKey();
                String value = entry.getValue();
                try {
                    value = URLEncoder.encode(value,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                jsonObject.addProperty(key,value);
            }
            return jsonObject.toString();
        }
        return null;
    }

}
