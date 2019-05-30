package org.xi.myserver.servlets;

import com.google.gson.Gson;
import org.xi.myserver.db.SqlUtiClass;
import org.xi.myserver.pojo.GetSessionReturnDataPOJO;
import org.xi.myserver.pojo.SQLReturnDataClass;
import org.xi.myserver.pojo.SessionDataPOJO;
import org.xi.myserver.pojo.StudentInformationObject;
import org.xi.myserver.utils.FileUtil;
import org.xi.myserver.utils.SQLStatusCODEList;
import org.xi.myserver.utils.StatusCodeList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class GetSessionInformationServlet extends MyServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req,resp);
        super.doPost(req, resp);
    }

    private void handle(HttpServletRequest req, HttpServletResponse resp) {
        Map<String,String> map = new HashMap<>();
        map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_PARAMETER_NOT_EQUALS));
        String id = req.getParameter("id");
        if(id != null) {
            int id_str = -1;
            try {
                id_str = Integer.valueOf(id);
            }catch (Exception e) {
                e.printStackTrace();
            }
            if(id_str != -1) {
                File path = new File(FileUtil.getSessionPath(this),String.valueOf(id_str));
                if(path.exists() && path.isDirectory()) {
                    try {
                        readDataFromDataDirectory(req,resp,path);
                        return;
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    map.put("s",String.valueOf(StatusCodeList.STATUS_CODE_SESSION_ID_NOT_EXISTS));
                }
            }
        }
        try {
            resp.getWriter().print(generateResponseString(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readDataFromDataDirectory(HttpServletRequest request, HttpServletResponse response, File base_dir) {
        File data_dir = new File(base_dir,FileUtil.DATA_PATH_DIRECTORY_NAME);
        if(data_dir.exists() && data_dir.isDirectory()) {
            Gson gson = new Gson();
            String [] files = data_dir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".json");
                }
            });
            List<Integer> files_integer = new ArrayList<>();
            for(String string : files) {
                String file_name_without_extension = string.replace(".json","");
                files_integer.add(Integer.valueOf(file_name_without_extension));
            }
            Collections.sort(files_integer);
            // get all bluetooth mac address
            Set<String> all_need_query_mac_addresses = new HashSet<>();
            for (int i = 0 ; i < files_integer.size() ; i++) {
                SessionDataPOJO sessionDataPOJO = getSessionDataPOJO(data_dir,files_integer,i,gson);
                List<String> mac_addresses = sessionDataPOJO.mac_addresses;
                // query data base to get full information
                all_need_query_mac_addresses.addAll(mac_addresses);
            }
            SQLReturnDataClass sqlReturnDataClass =
                    SqlUtiClass.getUserInformationListWithMacAddress(all_need_query_mac_addresses);
            if(sqlReturnDataClass.DB_ERR_CODE == SQLStatusCODEList.DB_OK) {
                List<StudentInformationObject> studentInformationObjects =
                        (List<StudentInformationObject>) sqlReturnDataClass.payload;
                //
                List<GetSessionReturnDataPOJO> getSessionReturnDataPOJOList = new ArrayList<>();
                for (int i = 0 ; i < files_integer.size() ; i++) {
                    SessionDataPOJO sessionDataPOJO = getSessionDataPOJO(data_dir,files_integer,i,gson);
                    long create_time = sessionDataPOJO.create_time;
                    List<String> mac_addresses = sessionDataPOJO.mac_addresses;
                    List<StudentInformationObject> tmp_info_list = new ArrayList<>();
                    for(int j = 0 ; j < mac_addresses.size(); j ++) {
                        String tmp_mac = mac_addresses.get(j);
                        StudentInformationObject tmp_info =
                                getStudentInformationObjectFromList(tmp_mac,studentInformationObjects);
                        if(tmp_info != null) {
                            tmp_info_list.add(tmp_info);
                        }
                    }
                    GetSessionReturnDataPOJO getSessionReturnDataPOJO = new GetSessionReturnDataPOJO();
                    getSessionReturnDataPOJO.create_time = create_time;
                    getSessionReturnDataPOJO.data = tmp_info_list;
                    getSessionReturnDataPOJOList.add(getSessionReturnDataPOJO);
                }
                ReturnPOJO returnPOJO = new ReturnPOJO();
                returnPOJO.s = String.valueOf(StatusCodeList.STATUS_CODE_OK);
                returnPOJO.l = getSessionReturnDataPOJOList;
                String json = gson.toJson(returnPOJO);
                try {
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().print(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ReturnPOJO implements Serializable {

        public String s;

        public List<GetSessionReturnDataPOJO> l;

    }

    private StudentInformationObject getStudentInformationObjectFromList(
            String mac,List<StudentInformationObject> list) {
        for (int i = 0 ; i < list.size() ; i++) {
               StudentInformationObject tmp = list.get(i);
               if(tmp.mac_address.equals(mac)) {
                   return tmp;
               }
        }
        return null;
    }

    private SessionDataPOJO getSessionDataPOJO(File data_dir, List<Integer> files_integer,int position,Gson gson) {
        File full_file_path = new File(data_dir,String.format("%d.json",files_integer.get(position)));
        String file_string = FileUtil.readStringFromFile(full_file_path);
        SessionDataPOJO sessionDataPOJO = gson.fromJson(file_string,SessionDataPOJO.class);
        return sessionDataPOJO;
    }

}
