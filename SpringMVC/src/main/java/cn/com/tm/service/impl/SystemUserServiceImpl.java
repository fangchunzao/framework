package cn.com.tm.service.impl;

import cn.com.tm.dao.SystemUserMapper;
import cn.com.tm.entity.PageDatagrid;
import cn.com.tm.service.ISystemUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class SystemUserServiceImpl implements ISystemUserService {

    @Autowired
    private SystemUserMapper systemUserMapper;

    @Override
    public PageDatagrid getAllUser(int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        PageHelper.orderBy("id desc");
        List<Map<String,String>> list = systemUserMapper.getAllUser();
        PageInfo<Map<String,String>> pageInfo = new PageInfo<Map<String,String>>(list);
        PageDatagrid pageDatagrid = new PageDatagrid(pageInfo.getTotal(),pageInfo.getList());
        return pageDatagrid;
    }
}
