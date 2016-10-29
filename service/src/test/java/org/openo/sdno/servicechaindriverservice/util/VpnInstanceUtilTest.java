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

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.servicechaindriverservice.deviceconfig.DCDeviceConfig;
import org.openo.sdno.servicechaindriverservice.sbi.GatewayConfigurationAPI;

import mockit.Mock;
import mockit.MockUp;

public class VpnInstanceUtilTest {

    @Test
    public void testQueryVpnInstanceName() throws ServiceException {

        new MockUp<GatewayConfigurationAPI>() {

            @Mock
            public String queryVpnInstance() throws ServiceException, IOException {
                FileInputStream fileStream = null;
                fileStream = new FileInputStream("src/test/resources/queryvpninstance.txt");
                @SuppressWarnings("deprecation")
                String vpnInstanceContent = IOUtils.toString(fileStream);
                return vpnInstanceContent;
            }
        };

        String vpnInstance = VpnInstanceUtil.queryVpnInstanceName(new DCDeviceConfig(), "3444444_23_32_45");
        assertTrue("vrf_1234555_3444444_9999".equals(vpnInstance));
    }

    @Test
    public void testQueryInBoundInterface() throws ServiceException {

        new MockUp<GatewayConfigurationAPI>() {

            @Mock
            public String queryVpnInterface(String vpnName) throws ServiceException, IOException {
                FileInputStream fileStream = null;
                fileStream = new FileInputStream("src/test/resources/queryvpninterface.txt");
                @SuppressWarnings("deprecation")
                String vpnInterfaceContent = IOUtils.toString(fileStream);
                return vpnInterfaceContent;
            }
        };

        String inBoundInterface = VpnInstanceUtil.queryInBoundInterface(new DCDeviceConfig(), "vrf_tenant_vpc_9999");
        assertTrue("10GE1/0/2.2".equals(inBoundInterface));
    }

    @Test
    public void testQueryOutBoundInterface() {

        new MockUp<GatewayConfigurationAPI>() {

            @Mock
            public String queryVpnInterface(String vpnName) throws ServiceException, IOException {
                FileInputStream fileStream = null;
                fileStream = new FileInputStream("src/test/resources/queryvpninterface.txt");
                @SuppressWarnings("deprecation")
                String vpnInterfaceContent = IOUtils.toString(fileStream);
                return vpnInterfaceContent;
            }
        };

        try {
            String outBoundInterface =
                    VpnInstanceUtil.queryOutBoundInterface(new DCDeviceConfig(), "vrf_tenant_vpc_9999");
            assertTrue("10GE1/0/1.2".equals(outBoundInterface));
        } catch(ServiceException e) {
            assertTrue(false);
        }
    }

}
