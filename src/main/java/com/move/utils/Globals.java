package com.move.utils;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Globals {
    public static Map<String, UserInfo> USER_INFOS = Maps.newHashMap();
}
