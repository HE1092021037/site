package cn.localhost.site.Common;

import java.util.HashMap;
import java.util.Map;

public class ALiYunSmsErrorCode {
    public static Map<String,String> reCodeMap = new HashMap<>();
    static{
        reCodeMap.put("isv.MOBILE_NUMBER_ILLEGAL","非法手机号");
        reCodeMap.put("isp.SYSTEM_ERROR","服务异常,请稍后再试");
        reCodeMap.put("isv.BLACK_KEY_CONTROL_LIMIT","处于黑名单管控中");
        reCodeMap.put("isv.BUSINESS_LIMIT_CONTROL","服务器请求频繁,注册暂时停止");
    }

}
