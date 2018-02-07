package cn.com.tm.utils;

import java.util.UUID;

/**
 * Created by T420 on 2016/12/20.
 */
public class KeyUtil {

    /**
     * @Title: getKeys
     * @Description: 统计得到32位不重复主键
     * @param @return
     * @return String
     * @throws
     * @author yangdognxu
     * @date 2016年8月17日 下午2:55:04
     */
    public static String getKeys(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
