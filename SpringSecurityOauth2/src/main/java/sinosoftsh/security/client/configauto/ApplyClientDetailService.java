package sinosoftsh.security.client.configauto;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import sinosoftsh.security.client.configauto.jdbcdetail.MyJdbcClientDetailsService;
import sinosoftsh.security.client.service.ApplyService;

import java.util.Map;

public class ApplyClientDetailService implements ClientDetailsService {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private DataSource dataSource;

    @Override
    public ClientDetails loadClientByClientId(String applyName) throws ClientRegistrationException {

        /*Map clientMap = applyService.findApplyById(applyName);

        if(clientMap == null) {
            throw new ClientRegistrationException("应用" + applyName + "不存在!");
        }*/

//        MyJdbcClientDetailsService jdbcClientDetailsService= new MyJdbcClientDetailsService(dataSource, "authentication");
        JdbcClientDetailsService jdbcClientDetailsService= new JdbcClientDetailsService(dataSource);
        ClientDetails clientDetails = jdbcClientDetailsService.loadClientByClientId(applyName);

        return clientDetails;
    }
}
