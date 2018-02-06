package sinosoftsh.security.client.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sinosoftsh.security.client.dao.ApplyDao;
import sinosoftsh.security.client.service.ApplyService;

import java.util.List;
import java.util.Map;

@Service
public class ApplyServiceImpl implements ApplyService{

    @Autowired
    private ApplyDao applyDao;

    @Override
    public Map findApplyById(String id) {
        return applyDao.findApplyById(id);
    }
}
