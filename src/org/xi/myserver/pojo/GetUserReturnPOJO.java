package org.xi.myserver.pojo;

import java.io.Serializable;
import java.util.List;

public class GetUserReturnPOJO implements Serializable {

    public List<StudentInformationObject> data;

    public boolean end_page;

    public String s;

}
