package com.qimeixun.util;

import cn.hutool.core.util.IdUtil;

public class IdUtils {

    public static String getId(){
        return IdUtil.getSnowflake(1 , 1).nextIdStr();
    }
}
