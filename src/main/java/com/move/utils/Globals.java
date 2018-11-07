package com.move.utils;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Globals {
    public static Map<String, UserInfo> USER_INFOS = Maps.newHashMap();
    public static String ACCESS_TOKEN = null;
    public static Long EXPIRES_DATE = 0L;
    public static String CORP_ID = "wx1575c3f8d572890d";
    public static String CORP_SECRET = "P8mS9dDsgD3YNOH5EB3IC8cbpXaHLdQXHJebb81i_Fo";
    public static String AGENT_ID = "1000033";
}
