/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.sdno.servicechaindriverservice.sbi;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.servicechaindriverservice.deviceconfig.DCDeviceConfig;
import org.openo.sdno.servicechaindriverservice.model.ServiceChainInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration API for Gateway.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-8-27
 */
public class GatewayConfigurationAPI extends ConfigurationAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationAPI.class);

    private static final int BASE_ACL_NUMBER = 2000;

    private static final String LOAD_CONFIG_FILE = "scripts/LoadGwConfig.script";

    private static final String DELOAD_CONFIG_FILE = "scripts/DeLoadGwConfig.script";

    private static final String QUERY_VPN_INSTANCE_FILE = "scripts/QueryVpnInstance.script";

    private static final String QUERY_VPN_INTERFACE_FILE = "scripts/QueryVpnInterface.script";

    /**
     * Constructor<br>
     * 
     * @param config DCDevice Configuration
     * @since SDNO 0.5
     */
    public GatewayConfigurationAPI(DCDeviceConfig config) {
        super(config);
    }

    @Override
    public void loadConfiguration(ServiceChainInfo scInfo) throws ServiceException {
        if(null == scInfo) {
            LOGGER.error("ServiceChainInfo parameter is invalid!!");
            throw new ServiceException("ServiceChainInfo parameter is invalid");
        }

        Map<String, String> scParamMap = new HashMap<String, String>();
        scParamMap.put("Vpn_Instance", scInfo.getVpnInstance());
        scParamMap.put("Vlan_ID", String.valueOf(scInfo.getVlanId()));
        scParamMap.put("Inbound_Interface", scInfo.getInBoundInterface());
        scParamMap.put("Outbound_Interface", scInfo.getOutBoundInterface());
        scParamMap.put("Acl_Rule_ID", String.valueOf(scInfo.getVlanId() + BASE_ACL_NUMBER));
        sshProtocol.executeShellScript(LOAD_CONFIG_FILE, scParamMap);
    }

    @Override
    public void deLoadConfiguration(ServiceChainInfo scInfo) throws ServiceException {
        if(null == scInfo) {
            LOGGER.error("ServiceChainInfo parameter is invalid!!");
            throw new ServiceException("ServiceChainInfo parameter is invalid");
        }

        Map<String, String> scParamMap = new HashMap<String, String>();
        scParamMap.put("Vpn_Instance", scInfo.getVpnInstance());
        scParamMap.put("Vlan_ID", String.valueOf(scInfo.getVlanId()));
        scParamMap.put("Inbound_Interface", scInfo.getInBoundInterface());
        scParamMap.put("Outbound_Interface", scInfo.getOutBoundInterface());
        scParamMap.put("Acl_Rule_ID", String.valueOf(scInfo.getVlanId() + BASE_ACL_NUMBER));
        sshProtocol.executeShellScript(DELOAD_CONFIG_FILE, scParamMap);
    }

    /**
     * Query Vpn Instance.<br>
     * 
     * @return Vpn Instance queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public String queryVpnInstance() throws ServiceException {
        return sshProtocol.executeShellScript(QUERY_VPN_INSTANCE_FILE, null);
    }

    /**
     * Query Vpn Interface Interface.<br>
     * 
     * @param vpnName VpnInstance name
     * @return Vpn Interface queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    public String queryVpnInterface(String vpnName) throws ServiceException {

        if(StringUtils.isEmpty(vpnName)) {
            throw new ServiceException("vpnName is invalid");
        }

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("Vpn_Instance", vpnName);
        return sshProtocol.executeShellScript(QUERY_VPN_INTERFACE_FILE, paramMap);
    }

}
