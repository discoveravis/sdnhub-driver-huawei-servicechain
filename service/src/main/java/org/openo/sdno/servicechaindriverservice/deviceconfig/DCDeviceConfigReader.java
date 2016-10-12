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

package org.openo.sdno.servicechaindriverservice.deviceconfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.helpers.IOUtils;
import org.codehaus.jackson.type.TypeReference;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reader class of Device Configuration.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-08-27
 */
public class DCDeviceConfigReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DCDeviceConfigReader.class);

    private static final String DEVICE_CONFIG = "etc/deviceconfig.json";

    private List<DCDeviceConfig> deviceCfgList;

    private static DCDeviceConfigReader instance;

    private DCDeviceConfigReader() {
        deviceCfgList = readDeviceConfigList();
    }

    /**
     * Get DCDeviceConfigReader singleton instance.<br>
     * 
     * @return
     * @since SDNO 0.5
     */
    public static synchronized DCDeviceConfigReader getInstance() {
        if(null == instance) {
            instance = new DCDeviceConfigReader();
        }
        return instance;
    }

    /**
     * Get Device Configuration By IpAddress.<br>
     * 
     * @param ipAddress IP Address
     * @return Device Configuration
     * @throws ServiceException when get configuration failed
     * @since SDNO 0.5
     */
    public DCDeviceConfig getDeviceConfigByIp(String ipAddress) throws ServiceException {
        if(StringUtils.isEmpty(ipAddress)) {
            LOGGER.error("ipAddress parameter is wrong!!");
            throw new ServiceException("ipAddress parameter is wrong!!");
        }

        if(CollectionUtils.isEmpty(deviceCfgList)) {
            LOGGER.error("deviceCfgList is wrong!!");
            throw new ServiceException("deviceCfgList is wrong!!");
        }

        for(DCDeviceConfig deviceCfg : deviceCfgList) {
            if(ipAddress.equals(deviceCfg.getIp())) {
                return deviceCfg;
            }
        }

        return null;
    }

    private List<DCDeviceConfig> readDeviceConfigList() {
        try {
            File configFile = new File(DEVICE_CONFIG);
            FileInputStream inStream = new FileInputStream(configFile);
            String configContent = IOUtils.readStringFromStream(inStream);
            return JsonUtil.fromJson(configContent, new TypeReference<List<DCDeviceConfig>>() {});
        } catch(IOException e) {
            LOGGER.error("Read Config File Failed!!", e);
        }
        return new ArrayList<DCDeviceConfig>();
    }

}
