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

package org.openo.sdno.servicechaindriverservice.nbimodel;

import java.util.List;

/**
 * NBI model of Service Function Path.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-08-19
 */
public class ServiceFunctionPath {

    private String id;

    private String name;

    private String description;

    private boolean symmetric;

    private String transportType;

    private String scfNeId;

    private List<ServicePathHop> servicePathHop;

    public List<ServicePathHop> getServicePathHop() {
        return servicePathHop;
    }

    public void setServicePathHop(List<ServicePathHop> servicePathHop) {
        this.servicePathHop = servicePathHop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScfNeId() {
        return scfNeId;
    }

    public void setScfNeId(String scfNeId) {
        this.scfNeId = scfNeId;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSymmetric() {
        return symmetric;
    }

    public void setSymmetric(boolean symmetric) {
        this.symmetric = symmetric;
    }

}
