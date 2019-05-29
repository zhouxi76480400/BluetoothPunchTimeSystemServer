package org.xi.myserver.pojo;

import java.io.Serializable;
import java.util.List;

public class GetSessionsServletReturnPOJO implements Serializable {

    public boolean end_page;

    public String s;

    public List<CreateSessionPOJO> data;

}
