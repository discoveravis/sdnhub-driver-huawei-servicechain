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

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.cli.protocol.ProtocolParameter;
import org.openo.sdno.cli.protocol.SshProtocol;
import org.openo.sdno.servicechaindriverservice.deviceconfig.DCDeviceConfig;
import org.openo.sdno.servicechaindriverservice.model.ServiceChainInfo;

/**
 * Base class of Configuration API.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-8-27
 */
public abstract class ConfigurationAPI {

    /**
     * SshProtocol Instance
     */
    protected SshProtocol sshProtocol;

    /**
     * Constructor<br>
     * 
     * @param config DCDevice Configuration
     * @since SDNO 0.5
     */
    public ConfigurationAPI(DCDeviceConfig config) {
        ProtocolParameter param =
                new ProtocolParameter(config.getIp(), config.getPort(), config.getUser(), config.getPassword());
        sshProtocol = new SshProtocol(param);
    }

    /**
     * Load Device Configuration.<br>
     * 
     * @param scInfo ServiceChain Info
     * @throws ServiceException when load configuration failed.
     * @since SDNO 0.5
     */
    public abstract void loadConfiguration(ServiceChainInfo scInfo) throws ServiceException;

    /**
     * DeLoad Device Configuration.<br>
     * 
     * @param scInfo ServiceChain Info
     * @throws ServiceException when deload configuration failed
     * @since SDNO 0.5
     */
    public abstract void deLoadConfiguration(ServiceChainInfo scInfo) throws ServiceException;
}
