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

package org.openo.sdno.servicechaindriverservice.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.servicechaindriverservice.deviceconfig.DCDeviceConfig;
import org.openo.sdno.servicechaindriverservice.sbi.GatewayConfigurationAPI;

/**
 * Utility class of VpnInstance.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-08-30
 */
public class VpnInstanceUtil {

    private static final String VRF_KEY_WORD = "vrf";

    private static final Pattern QUERY_VPN_INSTANCE_PATTERN = Pattern.compile("VPN-Instance Name and ID : (.*),");

    private static final Pattern QUERY_VPN_INTERFACE_PATTERN = Pattern.compile("Interface list : ");

    private VpnInstanceUtil() {
    }

    /**
     * Construct VPN Name.<br>
     * 
     * @param deviceCfg Device Configuration
     * @param tenantName tenant name
     * @param vpcName VPC name
     * @return VpnInstance Name returned
     * @since SDNO 0.5
     */
    public static String queryVpnInstanceName(DCDeviceConfig deviceCfg, String tenantName, String vpcName)
            throws ServiceException {

        GatewayConfigurationAPI gwAPI = new GatewayConfigurationAPI(deviceCfg);
        String vpnInstanceContent = gwAPI.queryVpnInstance();

        List<String> vpnNameList = new ArrayList<String>();
        Matcher resultMatcher = QUERY_VPN_INSTANCE_PATTERN.matcher(vpnInstanceContent);
        while(resultMatcher.find()) {
            vpnNameList.add(resultMatcher.group(1));
        }

        for(String vpnName : vpnNameList) {
            if(vpnName.contains(VRF_KEY_WORD + "_" + tenantName + "_" + vpcName)) {
                return vpnName;
            }
        }

        return null;
    }

    /**
     * Query InBound Interface.<br>
     * 
     * @param deviceCfg Device Configuration
     * @param vpnName VPN instance name
     * @return InBound Interface queried out
     * @throws ServiceException when query InBound Interface failed
     * @since SDNO 0.5
     */
    public static String queryInBoundInterface(DCDeviceConfig deviceCfg, String vpnName) throws ServiceException {
        return queryVpnInterface(deviceCfg, vpnName, 0);
    }

    /**
     * Query OutBound Interface.<br>
     * 
     * @param deviceCfg Device Configuration
     * @param vpnName VPN instance name
     * @return OutBound Interface queried out
     * @throws ServiceException when query OutBound Interface failed
     * @since SDNO 0.5
     */
    public static String queryOutBoundInterface(DCDeviceConfig deviceCfg, String vpnName) throws ServiceException {
        return queryVpnInterface(deviceCfg, vpnName, 1);
    }

    private static String queryVpnInterface(DCDeviceConfig deviceCfg, String vpnName, int infIndex)
            throws ServiceException {
        GatewayConfigurationAPI gwAPI = new GatewayConfigurationAPI(deviceCfg);
        String vpnInterfaceContent = gwAPI.queryVpnInterface(vpnName);
        Matcher resultMatcher = QUERY_VPN_INTERFACE_PATTERN.matcher(vpnInterfaceContent);
        String rawInfNameContent = "";
        while(resultMatcher.find()) {
            rawInfNameContent = vpnInterfaceContent.substring(resultMatcher.end());
        }

        String[] splitInfContent = rawInfNameContent.split("\r\n\r\n");
        if(null == splitInfContent || 0 == splitInfContent.length) {
            throw new ServiceException("Queried Interface List is wrong!!");
        }

        String infNameContent = splitInfContent[0].replaceAll("\r\n", "");
        infNameContent = infNameContent.replaceAll(" ", "");
        String[] infNameList = infNameContent.split(",");
        if(null == infNameList || 2 != infNameList.length) {
            throw new ServiceException("Queried Interface List is wrong!!");
        }

        return infNameList[infIndex];
    }

}
