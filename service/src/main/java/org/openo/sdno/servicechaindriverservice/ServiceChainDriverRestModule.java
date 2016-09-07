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

package org.openo.sdno.servicechaindriverservice;

import org.openo.sdno.framework.container.service.IRoaModule;
import org.openo.sdno.overlayvpn.inventory.sdk.DbOwerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rest module class for Service Chain driver Service.<br>
 *
 * @author
 * @version SDNO 0.5 2016-6-23
 */
public class ServiceChainDriverRestModule extends IRoaModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceChainDriverRestModule.class);

    @Override
    protected void init() {
        LOGGER.info("Start ServiceChain Driver Service");
        DbOwerInfo.init("scdriversvc", "scdriverdb");
    }

    @Override
    protected void destroy() {
        LOGGER.info("Stop ServiceChain Driver Service");
    }
}
