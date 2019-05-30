package org.xi.myserver.pojo;

import java.io.Serializable;

public class CreateSessionPOJO implements Serializable {

    public String uuid;

    public int sort_id;

    public int frequency;

    public int time; //minutes

    public long create_time;

    public String name;

//    public List<GetSessionReturnDataPOJO> s;

}
