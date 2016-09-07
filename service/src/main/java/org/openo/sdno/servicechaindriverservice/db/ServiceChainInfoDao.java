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

package org.openo.sdno.servicechaindriverservice.db;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.overlayvpn.dao.common.InventoryDao;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.openo.sdno.servicechaindriverservice.model.ServiceChainInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Dao class of Service Chain Info.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-8-30
 */
public class ServiceChainInfoDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceChainInfoDao.class);

    private static final Integer MIN_VLAN_ID = 2;

    private static final Integer MAX_VLAN_ID = 4095;

    private static final Integer INVALID_VLAN_ID = -1;

    @Autowired
    private InventoryDao<ServiceChainInfo> inventoryDao;

    public void setInventoryDao(InventoryDao<ServiceChainInfo> inventoryDao) {
        this.inventoryDao = inventoryDao;
    }

    /**
     * Query ServiceChainInfo By Id.<br>
     * 
     * @param sfcId service chain info id
     * @return ServiceChainInfo queried out
     * @since SDNO 0.5
     */
    public ServiceChainInfo queryByServiceChainId(String sfcId) throws ServiceException {
        ResultRsp<List<ServiceChainInfo>> sfcRsp = inventoryDao.batchQuery(ServiceChainInfo.class, (String)null);
        if(!sfcRsp.isSuccess()) {
            LOGGER.error("Query ServiceChainInfo Failed!!");
            throw new ServiceException("Query ServiceChainInfo Failed");
        }

        List<ServiceChainInfo> scList = sfcRsp.getData();
        if(CollectionUtils.isEmpty(scList)) {
            LOGGER.info("No ServiceChainInfo queried out");
            return null;
        }

        // Select target ServiceChain
        for(ServiceChainInfo serviceChainInfo : scList) {
            if(serviceChainInfo.getServiceChainId().equals(sfcId)) {
                return serviceChainInfo;
            }
        }

        return null;
    }

    /**
     * Query All Service Chain Info.<br>
     * 
     * @return Service Chain Info queried out
     * @throws ServiceException when query Service Chain Info failed
     * @since SDNO 0.5
     */
    public List<ServiceChainInfo> queryAllServiceChainInfo() throws ServiceException {

        ResultRsp<List<ServiceChainInfo>> sfcRsp = inventoryDao.batchQuery(ServiceChainInfo.class, (String)null);
        if(!sfcRsp.isSuccess()) {
            LOGGER.error("Query ServiceChainInfos Failed!!");
            throw new ServiceException("Query ServiceChainInfos Failed");
        }

        return sfcRsp.getData();
    }

    /**
     * Insert Service Chain Info Data.<br>
     * 
     * @param scInfo Service Chain Info
     * @throws ServiceException when insert Service Chain Info failed
     * @since SDNO 0.5
     */
    public void insertServiceChainInfo(ServiceChainInfo scInfo) throws ServiceException {
        if(null == scInfo) {
            LOGGER.error("scInfo is null!!");
            throw new ServiceException("scInfo is null!!");
        }

        inventoryDao.insert(scInfo);
    }

    /**
     * Delete Service Chain Info Data.<br>
     * 
     * @param scInfo Service Chain Info
     * @throws ServiceException when delete Service Chain Info failed
     * @since SDNO 0.5
     */
    public void deleteServiceChainInfo(ServiceChainInfo scInfo) throws ServiceException {
        if(null == scInfo) {
            LOGGER.error("scInfo is null!!");
            throw new ServiceException("scInfo is null!!");
        }

        inventoryDao.delete(ServiceChainInfo.class, scInfo.getUuid());
    }

    /**
     * Allocate Vlan Id Resource.<br>
     * 
     * @return Vlan Id allocated
     * @throws ServiceException when allocate vlan id failed
     * @since SDNO 0.5
     */
    public int allocVlanId() throws ServiceException {
        List<ServiceChainInfo> scInfoList = queryAllServiceChainInfo();
        if(scInfoList.isEmpty()) {
            return MIN_VLAN_ID;
        }

        @SuppressWarnings("unchecked")
        List<Integer> vlanIdList = new ArrayList<>(CollectionUtils.collect(scInfoList, new Transformer() {

            @Override
            public Object transform(Object arg0) {
                return ((ServiceChainInfo)arg0).getVlanId();
            }
        }));

        for(Integer vlanId = MIN_VLAN_ID; vlanId <= MAX_VLAN_ID; vlanId++) {
            if(!vlanIdList.contains(vlanId)) {
                return vlanId;
            }
        }

        return INVALID_VLAN_ID;
    }
}
