/*
 * Copyright 2016-2017 Huawei Technologies Co., Ltd.
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

package org.openo.sdnhub.servicechaindriverservice.test;

import java.io.File;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.overlayvpn.model.netmodel.servicechain.NetServiceChainPath;
import org.openo.sdno.testframework.checker.IChecker;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.testmanager.TestManager;
import org.openo.sdno.testframework.topology.ResourceType;
import org.openo.sdno.testframework.topology.Topology;

public class ITCreateServiceFunctionPathFail extends TestManager {

    private static final String CREATE_SFP_TESTCASE =
            "src/integration-test/resources/testcase/CreateServiceFunctionPath.json";

    private static final String CREATE_SFP_FAIL_TESTCASE =
            "src/integration-test/resources/testcase/CreateServiceFunctionPathFail.json";

    private static final String CREATE_SFP_NO_PATHHOP_TESTCASE =
            "src/integration-test/resources/testcase/CreateServiceFunctionPathNoPathHop.json";

    private static final String TOPODATA_PATH = "src/integration-test/resources/topodata1";

    private static Topology topo = new Topology(TOPODATA_PATH);

    @BeforeClass
    public static void setup() throws ServiceException {
        topo.createInvTopology();
    }

    @AfterClass
    public static void tearDown() throws ServiceException {
        topo.clearInvTopology();
    }

    @Test
    public void testGateWayNeIdWrongFormat() throws ServiceException {

        HttpRquestResponse httpCreateObject = HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_SFP_TESTCASE);
        HttpRequest createRequest = httpCreateObject.getRequest();

        Map<String, NetServiceChainPath> requestSfp =
                JsonUtil.fromJson(createRequest.getData(), new TypeReference<Map<String, NetServiceChainPath>>() {});
        NetServiceChainPath newSfpData = requestSfp.get("serviceFunctionPath");

        String gwNeId = "745748##*&";
        String fwNeId = topo.getResourceUuid(ResourceType.NETWORKELEMENT, "FwNe");

        newSfpData.setScfNeId(gwNeId);
        newSfpData.getServicePathHops().get(0).setSfiId(fwNeId);

        createRequest.setData(JsonUtil.toJson(requestSfp));

        execTestCase(createRequest, new FailChecker());
    }

    @Test
    public void testGateWayNeIdNotExist() throws ServiceException {

        HttpRquestResponse httpCreateObject = HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_SFP_TESTCASE);
        HttpRequest createRequest = httpCreateObject.getRequest();

        Map<String, NetServiceChainPath> requestSfp =
                JsonUtil.fromJson(createRequest.getData(), new TypeReference<Map<String, NetServiceChainPath>>() {});
        NetServiceChainPath newSfpData = requestSfp.get("serviceFunctionPath");

        String gwNeId = "48484949404040";
        String fwNeId = topo.getResourceUuid(ResourceType.NETWORKELEMENT, "FwNe");

        newSfpData.setScfNeId(gwNeId);
        newSfpData.getServicePathHops().get(0).setSfiId(fwNeId);

        createRequest.setData(JsonUtil.toJson(requestSfp));

        execTestCase(createRequest, new FailChecker());
    }

    @Test
    public void testGateWayUnreachable() throws ServiceException {

        HttpRquestResponse httpCreateObject = HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_SFP_TESTCASE);
        HttpRequest createRequest = httpCreateObject.getRequest();

        Map<String, NetServiceChainPath> requestSfp =
                JsonUtil.fromJson(createRequest.getData(), new TypeReference<Map<String, NetServiceChainPath>>() {});
        NetServiceChainPath newSfpData = requestSfp.get("serviceFunctionPath");

        String gwNeId = topo.getResourceUuid(ResourceType.NETWORKELEMENT, "GwNe");
        String fwNeId = topo.getResourceUuid(ResourceType.NETWORKELEMENT, "FwNe");

        newSfpData.setScfNeId(gwNeId);
        newSfpData.getServicePathHops().get(0).setSfiId(fwNeId);

        createRequest.setData(JsonUtil.toJson(requestSfp));

        execTestCase(createRequest, new FailChecker());
    }

    @Test
    public void testRequestNoBody() throws ServiceException {
        HttpRquestResponse httpCreateObject = HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_SFP_FAIL_TESTCASE);
        HttpRequest createRequest = httpCreateObject.getRequest();
        createRequest.setData("");
        execTestCase(createRequest, new FailChecker());
    }

    @Test
    public void testNoPathHop() throws ServiceException {
        execTestCase(new File(CREATE_SFP_NO_PATHHOP_TESTCASE), new FailChecker());
    }

    @Test
    public void testRequestNoSfp() throws ServiceException {
        execTestCase(new File(CREATE_SFP_FAIL_TESTCASE), new FailChecker());
    }

    private class FailChecker implements IChecker {

        @Override
        public boolean check(HttpResponse response) {
            if(!HttpCode.isSucess(response.getStatus())) {
                return true;
            }
            return false;
        }

    }

}
