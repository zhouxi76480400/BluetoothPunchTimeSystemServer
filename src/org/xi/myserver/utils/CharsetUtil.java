package org.xi.myserver.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class CharsetUtil {

    public static String getUTF_8String(String original) {
        String utf_8 = null;
        try {
            utf_8 = URLDecoder.decode(original,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return utf_8;
    }
}
