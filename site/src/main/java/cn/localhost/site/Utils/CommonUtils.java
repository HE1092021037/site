package cn.localhost.site.Utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;

import java.text.SimpleDateFormat;
import java.util.*;

@Log4j2
public class CommonUtils {
    public static String getNowTime(int type) {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));//important
        switch (type) {
            case 1:
                return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            case 2:
                return new SimpleDateFormat("HH").format(new Date());
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * uuid
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    /**
     * 替换新旧数据
     */
    public static Object replace(Object oldPojo, Object nowPojo) {
        if (oldPojo == null || nowPojo == null) {
            try {
                throw new Exception("替换新旧数据失败 有参数为空");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Map<String, Object> oldParams = JSON.parseObject(JSON.toJSONString(oldPojo), HashMap.class);
        Map<String, Object> nowParams = JSON.parseObject(JSON.toJSONString(nowPojo), HashMap.class);
//        oldParams.remove("id");
//        nowParams.remove("id");
        for (String key : nowParams.keySet()) {
            if (nowParams.get(key) != null) {
                Object value = nowParams.get(key);
                if (value.getClass() == Integer.class || value.getClass() == int.class) {
                    if ((int) value == 0) {
                        continue;
                    }
                }
                log.info("插入 key=" + key + ",,,,value=" + value);
                oldParams.put(key, value);
            }
        }
        return JSON.parseObject(JSON.toJSONString(oldParams), nowPojo.getClass());
    }

    /***
     * 下划线命名转为驼峰命名
     *
     * @param para
     *        下划线命名的字符串
     */
    public static String UnderlineToHump(String para) {
        StringBuilder result = new StringBuilder();
        String a[] = para.split("_");
        for (String s : a) {
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */
    public static String HumpToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;//定位
        for (int i = 0; i < para.length(); i++) {
            if (Character.isUpperCase(para.charAt(i))) {
                sb.insert(i + temp, "_");
                temp += 1;
            }
        }
        return sb.toString().toLowerCase();
    }

    public static String authCode() {
        String[] beforeShuffle = new String[]{"2", "3", "4", "5", "6", "7",
                "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z"};
        List list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        String afterShuffle = sb.toString();
        String result = afterShuffle.substring(3, 9);
        return result.toLowerCase();
    }
}
