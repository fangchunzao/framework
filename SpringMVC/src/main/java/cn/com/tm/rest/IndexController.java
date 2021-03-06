package cn.com.tm.rest;

import cn.com.tm.dao.SystemUserMapper;
import cn.com.tm.entity.JsonMessage;
import cn.com.tm.service.ISystemUserService;
import cn.com.tm.service.impl.SystemUserServiceImpl;
import cn.com.tm.utils.Constants;
import cn.com.tm.utils.ParamsUtil;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Scott 2017-06-08 13:06:27
 */

@RestController
@RequestMapping("/index")
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private SystemUserServiceImpl systemUserService;

    /**
     * index  需要参数pageNum和pageSize
     */
    @ResponseBody
    @RequestMapping(value="/index",method= RequestMethod.GET)
    public JsonMessage index(HttpServletRequest request, HttpServletResponse response) {
        JsonMessage result = new JsonMessage();
        Map<String, Object> data = new HashMap();
        try{
            Map paramsMap = ParamsUtil.getParmas(request);
            Integer pageNum = Integer.parseInt(paramsMap.get("pageNum").toString());
            Integer pageSize = Integer.parseInt(paramsMap.get("pageSize").toString());
            data.put("userList", systemUserService.getAllUser(pageNum, pageSize));
            result.setResponseCode(Constants.RES_CODE_0);
            result.setErrorMessage(Constants.RES_MESSAGE_0);
            result.setData(data);
            logger.info("获取全部人员信息");
        }catch (Exception e){
            logger.error("error");
            result.setData(data);
            result.setResponseCode(Constants.RES_CODE_904);
            result.setErrorMessage(Constants.RES_MESSAGE_904);
            logger.error("ERROR Message", e);
        }
        return result;
    }

}
