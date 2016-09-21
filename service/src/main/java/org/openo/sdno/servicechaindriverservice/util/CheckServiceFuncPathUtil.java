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

import org.apache.commons.collections.CollectionUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.overlayvpn.model.netmodel.servicechain.NetServiceChainPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validate ServiceFuncPath data.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-08-19
 */
public class CheckServiceFuncPathUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckServiceFuncPathUtil.class);

    private CheckServiceFuncPathUtil() {

    }

    /**
     * Check ServiceFuncPath data.<br>
     * 
     * @param pathData NetServiceChainPath data
     * @throws ServiceException when ServiceFuncPath data is invalid
     * @since SDNO 0.5
     */
    public static void check(NetServiceChainPath pathData) throws ServiceException {
        UuidUtils.checkUuid(pathData.getUuid());
        UuidUtils.checkUuid(pathData.getScfNeId());

        if(CollectionUtils.isEmpty(pathData.getServicePathHops())) {
            LOGGER.error("No hop information in Service Function Path.");
            throw new ServiceException("No hop information in Service Function Path.");
        }

        UuidUtils.checkUuid(pathData.getServicePathHops().get(0).getSfiId());
    }
}
