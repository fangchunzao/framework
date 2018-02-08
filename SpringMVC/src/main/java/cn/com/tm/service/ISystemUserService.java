package cn.com.tm.service;

import cn.com.tm.entity.PageDatagrid;

import java.util.List;
import java.util.Map;

public interface ISystemUserService {

    PageDatagrid getAllUser(int pageNum, int pageSize);

}
