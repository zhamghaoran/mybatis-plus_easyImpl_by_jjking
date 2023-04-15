package org.zhr.Service.Interface;

import java.util.Map;

/**
 * @author 20179
 */
public interface SqlMakeFactory {
    String where(Map<String,String> map);
    String lt(Map<String,String> map);
    String bt(Map<String,String> map) ;

    String like(Map<String, String> map);

    String orderby(String s) ;
}
