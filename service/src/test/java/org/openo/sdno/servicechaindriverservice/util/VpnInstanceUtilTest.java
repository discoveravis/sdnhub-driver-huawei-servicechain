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

import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.cli.protocol.SshProtocol;
import org.openo.sdno.servicechaindriverservice.deviceconfig.DCDeviceConfig;

import mockit.Mock;
import mockit.MockUp;

public class VpnInstanceUtilTest {

    @Test
    public void testVpnInstanceRegexPattern() {

        Pattern pattern = Pattern.compile("VPN-Instance Name and ID : (.*),");
        Matcher resultMatcher = pattern.matcher(" VPN-Instance Name and ID : test, 1");
        while(resultMatcher.find()) {
            assertTrue("test".equals(resultMatcher.group(1)));
        }

    }

    @Test
    public void testVpnInterfaceRegexPattern() {

        Pattern pattern = Pattern.compile("Interface list: ");
        Matcher resultMatcher = pattern.matcher("Test Interface list: interface1\r\n interface2\r\n interface3\r\n");
        while(resultMatcher.find()) {
            assertTrue(5 == resultMatcher.start());
            assertTrue(21 == resultMatcher.end());
        }
    }

    @Test
    public void testqueryVpnInstanceName() {

        DCDeviceConfig dcConfig = new DCDeviceConfig();

        new MockUp<SshProtocol>() {

            @Mock
            String executeShellScript(String scriptFile, Map<String, String> replaceParamMap) throws ServiceException {
                return "VPN-Instance Name and ID : vrf_Tenant_Vpc_9448, 1";
            }
        };

        try {
            String vpnInstance = VpnInstanceUtil.queryVpnInstanceName(dcConfig, "Tenant", "Vpc");
            assertTrue("vrf_Tenant_Vpc_9448".equals(vpnInstance));
        } catch(ServiceException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testQueryInBoundInterface() {

        DCDeviceConfig dcConfig = new DCDeviceConfig();

        new MockUp<SshProtocol>() {

            @Mock
            String executeShellScript(String scriptFile, Map<String, String> replaceParamMap) throws ServiceException {
                return "Interface list: interface1, interface2";
            }
        };

        try {
            String inBoundInterface = VpnInstanceUtil.queryInBoundInterface(dcConfig, "Vpn");
            assertTrue("interface1".equals(inBoundInterface));
        } catch(ServiceException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testQueryOutBoundInterface() {

        DCDeviceConfig dcConfig = new DCDeviceConfig();

        new MockUp<SshProtocol>() {

            @Mock
            String executeShellScript(String scriptFile, Map<String, String> replaceParamMap) throws ServiceException {
                return "Interface list: interface1, interface2";
            }
        };

        try {
            String outBoundInterface = VpnInstanceUtil.queryOutBoundInterface(dcConfig, "Vpn");
            assertTrue("interface2".equals(outBoundInterface));
        } catch(ServiceException e) {
            assertTrue(false);
        }
    }

}
