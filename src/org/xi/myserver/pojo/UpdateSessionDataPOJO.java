package org.xi.myserver.pojo;

import java.io.Serializable;
import java.util.List;

public class UpdateSessionDataPOJO implements Serializable {

    public int sid;

    public long time;

    public List<String> mac;

}
