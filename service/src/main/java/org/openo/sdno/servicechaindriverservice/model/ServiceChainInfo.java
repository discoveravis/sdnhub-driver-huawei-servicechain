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

package org.openo.sdno.servicechaindriverservice.model;

import org.openo.sdno.overlayvpn.inventory.sdk.model.annotation.MOResType;
import org.openo.sdno.overlayvpn.model.uuid.AbstUuidModel;

/**
 * Class of ServiceChainInfo Model Data. <br>
 * <p>
 * It is used to recode the service chain info data.
 * </p>
 * 
 * @author
 * @version SDNO 0.5 August 27, 2016
 */
@MOResType(infoModelName = "servicechain_info")
public class ServiceChainInfo extends AbstUuidModel {

    private String serviceChainId;

    private String vpnInstance;

    private int vlanId;

    private String inBoundInterface;

    private String outBoundInterface;

    private String sfiNeId;

    private String sffNeId;

    public String getServiceChainId() {
        return serviceChainId;
    }

    public void setServiceChainId(String serviceChainId) {
        this.serviceChainId = serviceChainId;
    }

    public String getVpnInstance() {
        return vpnInstance;
    }

    public void setVpnInstance(String vpnInstance) {
        this.vpnInstance = vpnInstance;
    }

    public int getVlanId() {
        return vlanId;
    }

    public void setVlanId(int vlanId) {
        this.vlanId = vlanId;
    }

    public String getInBoundInterface() {
        return inBoundInterface;
    }

    public void setInBoundInterface(String inBoundInterface) {
        this.inBoundInterface = inBoundInterface;
    }

    public String getOutBoundInterface() {
        return outBoundInterface;
    }

    public void setOutBoundInterface(String outBoundInterface) {
        this.outBoundInterface = outBoundInterface;
    }

    public String getSfiNeId() {
        return sfiNeId;
    }

    public void setSfiNeId(String sfiNeId) {
        this.sfiNeId = sfiNeId;
    }

    public String getSffNeId() {
        return sffNeId;
    }

    public void setSffNeId(String sffNeId) {
        this.sffNeId = sffNeId;
    }
}
