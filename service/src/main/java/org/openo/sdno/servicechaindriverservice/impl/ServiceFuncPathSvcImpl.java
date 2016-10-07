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

package org.openo.sdno.servicechaindriverservice.impl;

import javax.annotation.Resource;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.overlayvpn.brs.invdao.NetworkElementInvDao;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.model.netmodel.servicechain.NetServiceChainPath;
import org.openo.sdno.servicechaindriverservice.db.ServiceChainInfoDao;
import org.openo.sdno.servicechaindriverservice.deviceconfig.DCDeviceConfig;
import org.openo.sdno.servicechaindriverservice.deviceconfig.DCDeviceConfigReader;
import org.openo.sdno.servicechaindriverservice.inf.ServiceFuncPathService;
import org.openo.sdno.servicechaindriverservice.model.ServiceChainInfo;
import org.openo.sdno.servicechaindriverservice.sbi.FirewallConfigurationAPI;
import org.openo.sdno.servicechaindriverservice.sbi.GatewayConfigurationAPI;
import org.openo.sdno.servicechaindriverservice.util.VpnInstanceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation Class of ServiceFuncPath Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-08-19
 */
public class ServiceFuncPathSvcImpl implements ServiceFuncPathService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceFuncPathSvcImpl.class);

    @Resource
    private ServiceChainInfoDao serviceInfoDao;

    public void setServiceInfoDao(ServiceChainInfoDao serviceInfoDao) {
        this.serviceInfoDao = serviceInfoDao;
    }

    @Override
    public NetServiceChainPath create(NetServiceChainPath path) throws ServiceException {

        String serviceChainId = path.getUuid();
        if(null != serviceInfoDao.queryByServiceChainId(serviceChainId)) {
            LOGGER.error("Service Chain Info already exist!!");
            throw new ServiceException("Service Chain Info already exist");
        }

        String sfcNeId = path.getScfNeId();
        String sfiNeId = path.getServicePathHops().get(0).getSfiId();

        NetworkElementInvDao neInvDao = new NetworkElementInvDao();

        NetworkElementMO sfcNetworkElement = neInvDao.query(sfcNeId);
        if(null == sfcNetworkElement) {
            LOGGER.error("Sfc Device Not exist!!");
            throw new ServiceException("Sfc Device Not exist");
        }

        NetworkElementMO sfiNetworkElement = neInvDao.query(sfiNeId);
        if(null == sfiNetworkElement) {
            LOGGER.error("Sfi Device Not exist!!");
            throw new ServiceException("Sfi Device Not exist!!");
        }

        DCDeviceConfig sfcDevice =
                DCDeviceConfigReader.getInstance().getDeviceConfigByIp(sfcNetworkElement.getIpAddress());

        DCDeviceConfig sfiDevice =
                DCDeviceConfigReader.getInstance().getDeviceConfigByIp(sfiNetworkElement.getIpAddress());

        int vlanId = serviceInfoDao.allocVlanId();
        if(vlanId < 0) {
            LOGGER.error("Vlan Id is invalid!!");
            throw new ServiceException("Vlan Id is invalid!!");
        }

        String vpnInstance = VpnInstanceUtil.queryVpnInstanceName(sfcDevice, path.getName(), path.getDescription());
        String inBoundInterface = VpnInstanceUtil.queryInBoundInterface(sfcDevice, vpnInstance);
        String outBoundInterface = VpnInstanceUtil.queryOutBoundInterface(sfcDevice, vpnInstance);

        ServiceChainInfo serviceChainInfo = new ServiceChainInfo();
        serviceChainInfo.setVlanId(vlanId);
        serviceChainInfo.setServiceChainId(serviceChainId);
        serviceChainInfo.setSffNeId(sfcNeId);
        serviceChainInfo.setSfiNeId(sfiNeId);
        serviceChainInfo.setInBoundInterface(inBoundInterface);
        serviceChainInfo.setOutBoundInterface(outBoundInterface);
        serviceChainInfo.setVpnInstance(vpnInstance);

        GatewayConfigurationAPI gwAPI = new GatewayConfigurationAPI(sfcDevice);
        FirewallConfigurationAPI fwAPI = new FirewallConfigurationAPI(sfiDevice);

        gwAPI.loadConfiguration(serviceChainInfo);
        fwAPI.loadConfiguration(serviceChainInfo);

        serviceInfoDao.insertServiceChainInfo(serviceChainInfo);

        return path;
    }

    @Override
    public void delete(String uuid) throws ServiceException {

        ServiceChainInfo serviceChainInfo = serviceInfoDao.queryByServiceChainId(uuid);
        if(null == serviceChainInfo) {
            LOGGER.warn("Service Chain Info do not exist!!");
            return;
        }

        NetworkElementInvDao neInvDao = new NetworkElementInvDao();

        NetworkElementMO sfcNetworkElement = neInvDao.query(serviceChainInfo.getSffNeId());
        if(null == sfcNetworkElement) {
            LOGGER.error("Sfc Device Not exist!!");
            throw new ServiceException("Sfc Device Not exist");
        }

        NetworkElementMO sfiNetworkElement = neInvDao.query(serviceChainInfo.getSfiNeId());
        if(null == sfiNetworkElement) {
            LOGGER.error("Sfi Device Not exist!!");
            throw new ServiceException("Sfi Device Not exist!!");
        }

        DCDeviceConfig sfcDevice =
                DCDeviceConfigReader.getInstance().getDeviceConfigByIp(sfcNetworkElement.getIpAddress());

        DCDeviceConfig sfiDevice =
                DCDeviceConfigReader.getInstance().getDeviceConfigByIp(sfiNetworkElement.getIpAddress());

        GatewayConfigurationAPI gwAPI = new GatewayConfigurationAPI(sfcDevice);
        FirewallConfigurationAPI fwAPI = new FirewallConfigurationAPI(sfiDevice);

        gwAPI.deLoadConfiguration(serviceChainInfo);
        fwAPI.deLoadConfiguration(serviceChainInfo);

        serviceInfoDao.deleteServiceChainInfo(serviceChainInfo);
    }
}
