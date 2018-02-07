package cn.com.tm.utils;


import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ParamsUtil {
	/**
	 * @Title: getParmas
	 * @Description: 得到前台转来的参数
	 * @param @param request
	 * @param @return
	 * @return Map
	 * @throws
	 * @author fcz
	 * @date 2017年8月23日
	 */

	public static  Map<String, Object>  getParmas(ServletRequest request){
		//拿到输入流，通过Request传给Controller，以便Controller相关方法执行JSON的反序列化
		byte[] requestAttributeData = new byte[0];
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			StreamUtils streamUtils = new StreamUtils();
			requestAttributeData = streamUtils.readFromStream(request.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute(Constants.REQUEST_STREM, requestAttributeData);

		Map<String, Object> reMap = new HashMap<>();
		byte[] requestData = (byte[]) request.getAttribute(Constants.REQUEST_STREM);
		if (requestData == null || requestData.length==0){
			//GET方法获取参数
            Map map = request.getParameterMap();
            Iterator it=map.keySet().iterator();
            while(it.hasNext()){
                String key;
                Object rvalue;
                key=it.next().toString();
                String[] value=(String[])map.get(key);
                rvalue = value[0];
                reMap.put(key,rvalue);
            }
        }else{
            reMap= JsonUtils.readValue(requestData, Map.class);
        }
		return reMap;
	}


}
