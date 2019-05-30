package org.xi.myserver.servlets;

import com.google.gson.Gson;
import org.xi.myserver.pojo.CreateSessionPOJO;
import org.xi.myserver.pojo.GetSessionsServletReturnPOJO;
import org.xi.myserver.utils.FileUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GetSessionsServlet extends MyServlet {

    private static final int DEFAULT_C = 10;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req,resp);
        super.doPost(req, resp);
    }

    private void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String l_str = httpServletRequest.getParameter("l");
        String c_str = httpServletRequest.getParameter("c");
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
//        System.out.println(l);
//        System.out.println(c);
        List<CreateSessionPOJO> data = readFileList(FileUtil.getSessionPath(this),c,l);
        boolean isLastPage = false;
        if(data.size() != c) {
            isLastPage = true;
        }
        GetSessionsServletReturnPOJO getSessionsServletReturnPOJO = new GetSessionsServletReturnPOJO();
        getSessionsServletReturnPOJO.data = data;
        getSessionsServletReturnPOJO.s = "0";
        getSessionsServletReturnPOJO.end_page = isLastPage;
        String json = new Gson().toJson(getSessionsServletReturnPOJO);
        System.out.println(json);
        try {
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<CreateSessionPOJO> readFileList(String base_path,int max_size, int last) {
        List<CreateSessionPOJO> all = new ArrayList<>();
        File base_path_file = new File(base_path);
        String [] directories = base_path_file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir,name).isDirectory();
            }
        });
        List<String> all_names_str = Arrays.asList(directories);
        directories = null;
        List<Integer> all_names = new ArrayList<>();
        for(String str : all_names_str) {
            all_names.add(Integer.valueOf(str));
        }
        Collections.sort(all_names,Collections.reverseOrder());

        if(last == 0) {
            int for_count = max_size < all_names.size() ? max_size : all_names.size();
            all.addAll(readData(base_path,all_names,for_count,0));
        }else {
            // TODO




        }
        return all;
    }

    private List<CreateSessionPOJO> readData(
            String base_path, List<Integer> all_names, int repeat_count, int start_position) {
        Gson gson = new Gson();
        List<CreateSessionPOJO> datas = new ArrayList<>();
        for (int i = start_position;i < start_position + repeat_count ; i ++) {
            int session_name = all_names.get(i);
            File full_path = new File(base_path,String.valueOf(session_name));
            //get main profile form path
            File main_profile_path = new File(full_path,FileUtil.JSON_CONFIGURE_FILE_NAME);
            String main_profile_string = FileUtil.readStringFromFile(main_profile_path);
            CreateSessionPOJO createSessionPOJO = gson.fromJson(main_profile_string, CreateSessionPOJO.class);
            datas.add(createSessionPOJO);
        }
        return datas;
    }

}
