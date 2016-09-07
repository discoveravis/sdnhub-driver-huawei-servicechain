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

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.servicechaindriverservice.deviceconfig.DCDeviceConfig;
import org.openo.sdno.servicechaindriverservice.model.ServiceChainInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration API for Firewall.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-8-27
 */
public class FirewallConfigurationAPI extends ConfigurationAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirewallConfigurationAPI.class);

    private static final String LOAD_CONFIG_FILE = "scripts/LoadFwConfig.script";

    private static final String DELOAD_CONFIG_FILE = "scripts/DeLoadFwConfig.script";

    /**
     * Constructor<br>
     * 
     * @param config DCDevice Configuration
     * @since SDNO 0.5
     */
    public FirewallConfigurationAPI(DCDeviceConfig config) {
        super(config);
    }

    @Override
    public void loadConfiguration(ServiceChainInfo scInfo) throws ServiceException {
        if(null == scInfo) {
            LOGGER.error("ServiceChainInfo parameter is invalid!!");
            throw new ServiceException("ServiceChainInfo parameter is invalid");
        }

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("Vpn_Instance", scInfo.getVpnInstance());
        paramMap.put("Vlan_ID", String.valueOf(scInfo.getVlanId()));
        sshProtocol.executeShellScript(LOAD_CONFIG_FILE, paramMap);
    }

    @Override
    public void deLoadConfiguration(ServiceChainInfo scInfo) throws ServiceException {
        if(null == scInfo) {
            LOGGER.error("ServiceChainInfo parameter is invalid!!");
            throw new ServiceException("ServiceChainInfo parameter is invalid");
        }

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("Vpn_Instance", scInfo.getVpnInstance());
        paramMap.put("Vlan_ID", String.valueOf(scInfo.getVlanId()));
        sshProtocol.executeShellScript(DELOAD_CONFIG_FILE, paramMap);
    }

}
