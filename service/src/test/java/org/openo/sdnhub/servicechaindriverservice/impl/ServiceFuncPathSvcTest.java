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

package org.openo.sdnhub.servicechaindriverservice.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdnhub.servicechaindriverservice.db.ServiceChainInfoDao;
import org.openo.sdnhub.servicechaindriverservice.deviceconfig.DCDeviceConfig;
import org.openo.sdnhub.servicechaindriverservice.deviceconfig.DCDeviceConfigReader;
import org.openo.sdnhub.servicechaindriverservice.deviceconfig.DCDeviceConfigReaderTest;
import org.openo.sdnhub.servicechaindriverservice.model.ServiceChainInfo;
import org.openo.sdnhub.servicechaindriverservice.sbi.FirewallConfigurationAPI;
import org.openo.sdnhub.servicechaindriverservice.sbi.GatewayConfigurationAPI;
import org.openo.sdnhub.servicechaindriverservice.util.VpnInstanceUtil;
import org.openo.sdno.overlayvpn.brs.invdao.NetworkElementInvDao;
import org.openo.sdno.overlayvpn.brs.model.NetworkElementMO;
import org.openo.sdno.overlayvpn.dao.common.InventoryDao;
import org.openo.sdno.overlayvpn.model.netmodel.servicechain.NetServiceChainPath;
import org.openo.sdno.overlayvpn.model.netmodel.servicechain.NetServiceClassifer;
import org.openo.sdno.overlayvpn.model.netmodel.servicechain.NetServicePathHop;
import org.openo.sdno.overlayvpn.model.servicechain.Rule;
import org.openo.sdno.overlayvpn.result.ResultRsp;

import mockit.Mock;
import mockit.MockUp;

/**
 * Implementation Class of ServiceFuncPath Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-08-19
 */
public class ServiceFuncPathSvcTest  
{
	private ServiceFuncPathSvcImpl serviceFuncPathImpl = new ServiceFuncPathSvcImpl();
	private NetServiceChainPath netSvcChainPath = new NetServiceChainPath();
	private ServiceChainInfoDao oServiceChainInfoDao;

	@Before
	public void setup() 
	{	    	
		netSvcChainPath.setUuid("Uuid");
		netSvcChainPath.setSymmetric(true);
		netSvcChainPath.setTransportType("TransportType");
		netSvcChainPath.setScfNeId("ScfNeId");
		netSvcChainPath.setName("Name");
		netSvcChainPath.setDescription("Description");

		NetServicePathHop oNetServicePathHop = new NetServicePathHop();
		oNetServicePathHop.setHopNumber(5);
		oNetServicePathHop.setSffId("sffid");
		oNetServicePathHop.setSfgId("sfgid");
		oNetServicePathHop.setSfiId("sfid");   	
		List<NetServicePathHop> oNetServicePathHopList =  new ArrayList<>();
		oNetServicePathHopList.add(oNetServicePathHop);

		List<Rule> ruleList =  new ArrayList<>();
		Rule oRule = new Rule(); 
		oRule.setDestIp("10.11.12.18");
		oRule.setDestMask("127.0.0.5");
		oRule.setDestPort("8034");
		oRule.setPolicy("policy");
		oRule.setProtocol("netconfg");
		oRule.setSrcIp("10.12.13.14");
		oRule.setSrcMask("127.0.2.3");
		oRule.setSrcPort("8055");
		ruleList.add(oRule);

		NetServiceClassifer oNetServiceClassifer = new NetServiceClassifer();
		oNetServiceClassifer.setInterfaceName("InterfaceName");
		oNetServiceClassifer.setZone("ZONE");  	
		oNetServiceClassifer.setRules(ruleList);
		List<NetServiceClassifer> classifiersList =  new ArrayList<>();
		classifiersList.add(oNetServiceClassifer);

		netSvcChainPath.setClassifiers(classifiersList);
		netSvcChainPath.setServicePathHops(oNetServicePathHopList);

		InventoryDao<ServiceChainInfo> inventoryDao = new InventoryDao<ServiceChainInfo>();

		oServiceChainInfoDao = new ServiceChainInfoDao(); 
		oServiceChainInfoDao.setInventoryDao(inventoryDao );
		serviceFuncPathImpl.setServiceInfoDao(oServiceChainInfoDao);

	}

	@Test
	public void testsetServiceInfoDao() throws ServiceException 
	{		
		serviceFuncPathImpl.setServiceInfoDao(oServiceChainInfoDao);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testcreateBranch1() throws ServiceException 
	{		
		new MockUp<InventoryDao>()
		{
			@Mock
			ResultRsp<List<ServiceChainInfo>> batchQuery(Class clazz, String filter) throws ServiceException
			{
				ResultRsp<List<ServiceChainInfo>> res = new ResultRsp<List<ServiceChainInfo>>();
				res.setAdviceArg("adviarg");
				List<ServiceChainInfo> data = new ArrayList<>();
				ServiceChainInfo oServiceChainInfo = new ServiceChainInfo();
				oServiceChainInfo.setUuid("uuid");
				oServiceChainInfo.setServiceChainId("UuidC");
				data.add(oServiceChainInfo );
				res.setData(data);
				return res;
			}
		};

		new MockUp<NetworkElementInvDao>()
		{
			@Mock
			NetworkElementMO query(String id) throws ServiceException
			{
				NetworkElementMO oNetworkElementMO = new NetworkElementMO();
				oNetworkElementMO.setId("1001");
				oNetworkElementMO.setIpAddress("10.12.13.14");
				return oNetworkElementMO;
			}
		};

		new MockUp<DCDeviceConfigReader>()
		{
			@Mock
			DCDeviceConfig getDeviceConfigByIp(String id) throws ServiceException
			{
				DCDeviceConfig oDeviceConfig = new DCDeviceConfig();
				return oDeviceConfig;
			}
		};

		new MockUp<VpnInstanceUtil>()
		{
			@Mock
			String queryVpnInstanceName(DCDeviceConfig deviceCfg, String vpcName) throws ServiceException
			{
				return "VpnInstanceName";
			}

			@Mock
			String queryInBoundInterface(DCDeviceConfig deviceCfg, String vpcName) throws ServiceException
			{
				return "InBoundInterface";
			}

			@Mock
			String queryOutBoundInterface(DCDeviceConfig deviceCfg, String vpcName) throws ServiceException
			{
				return "OutBoundInterface";
			}
		};

		new MockUp<GatewayConfigurationAPI>()
		{
			@Mock
			void loadConfiguration(ServiceChainInfo scInfo) throws ServiceException
			{
			}
		};

		new MockUp<FirewallConfigurationAPI>()
		{
			@Mock
			void loadConfiguration(ServiceChainInfo scInfo) throws ServiceException
			{
			}
		};	
		new MockUp<ServiceChainInfoDao>()
		{
			@Mock
			void insertServiceChainInfo(ServiceChainInfo scInfo) throws ServiceException
			{
			}
		};	
		serviceFuncPathImpl.create(netSvcChainPath);
	}

	@SuppressWarnings("rawtypes")
	@Test(expected = ServiceException.class)
	public void testcreateBranch2() throws ServiceException 
	{		
		new MockUp<InventoryDao>()
		{
			@Mock
			ResultRsp<List<ServiceChainInfo>> batchQuery(Class clazz, String filter) throws ServiceException
			{
				ResultRsp<List<ServiceChainInfo>> res = new ResultRsp<List<ServiceChainInfo>>();
				res.setAdviceArg("adviarg");
				List<ServiceChainInfo> data = new ArrayList<>();
				ServiceChainInfo oServiceChainInfo = new ServiceChainInfo();
				oServiceChainInfo.setUuid("uuid");
				oServiceChainInfo.setServiceChainId("UuidC");
				data.add(oServiceChainInfo );
				res.setData(data);
				return res;
			}
		};

		new MockUp<NetworkElementInvDao>()
		{
			@Mock
			NetworkElementMO query(String id) throws ServiceException
			{
				return null;
			}
		};
		serviceFuncPathImpl.create(netSvcChainPath);
	}

	@SuppressWarnings("rawtypes")
	@Test(expected = ServiceException.class)
	public void testcreateBranch3() throws ServiceException 
	{		
		new MockUp<InventoryDao>()
		{
			@Mock
			ResultRsp<List<ServiceChainInfo>> batchQuery(Class clazz, String filter) throws ServiceException
			{
				ResultRsp<List<ServiceChainInfo>> res = new ResultRsp<List<ServiceChainInfo>>();
				res.setAdviceArg("adviarg");
				List<ServiceChainInfo> data = new ArrayList<>();
				ServiceChainInfo oServiceChainInfo = new ServiceChainInfo();
				oServiceChainInfo.setUuid("uuid");
				oServiceChainInfo.setServiceChainId("UuidC");
				data.add(oServiceChainInfo );
				res.setData(data);
				return res;
			}
		};

		new MockUp<NetworkElementInvDao>()
		{
			@Mock
			NetworkElementMO query(String id) throws ServiceException
			{
				NetworkElementMO oNetworkElementMO = new NetworkElementMO();
				oNetworkElementMO.setId("1001");
				oNetworkElementMO.setIpAddress("10.12.13.14");
				return oNetworkElementMO;
			}
		};

		new MockUp<DCDeviceConfigReader>()
		{
			@Mock
			DCDeviceConfig getDeviceConfigByIp(String id) throws ServiceException
			{
				DCDeviceConfig oDeviceConfig = new DCDeviceConfig();
				return oDeviceConfig;
			}
		};

		new MockUp<VpnInstanceUtil>()
		{
			@Mock
			String queryVpnInstanceName(DCDeviceConfig deviceCfg, String vpcName) throws ServiceException
			{
				return "VpnInstanceName";
			}

			@Mock
			String queryInBoundInterface(DCDeviceConfig deviceCfg, String vpcName) throws ServiceException
			{
				return "InBoundInterface";
			}

			@Mock
			String queryOutBoundInterface(DCDeviceConfig deviceCfg, String vpcName) throws ServiceException
			{
				return "OutBoundInterface";
			}
		};

		new MockUp<GatewayConfigurationAPI>()
		{
			@Mock
			void loadConfiguration(ServiceChainInfo scInfo) throws ServiceException
			{
			}
		};

		new MockUp<FirewallConfigurationAPI>()
		{
			@Mock
			void loadConfiguration(ServiceChainInfo scInfo) throws ServiceException
			{
			}
		};	
		new MockUp<ServiceChainInfoDao>()
		{
			@Mock
			int allocVlanId() throws ServiceException
			{
				return -1;
			}
		};	
		serviceFuncPathImpl.create(netSvcChainPath);
	}

	@Test(expected = ServiceException.class)
	public void testcreateBranch4() throws ServiceException 
	{				
		new MockUp<ServiceChainInfoDao>()
		{
			@Mock
			ServiceChainInfo queryByServiceChainId(String sfcId) throws ServiceException
			{
				return new ServiceChainInfo();
			}
		};
		serviceFuncPathImpl.create(netSvcChainPath);
	}

	@Test
	public void testdeleteBranch1() throws ServiceException 
	{				
		new MockUp<ServiceChainInfoDao>()
		{
			@Mock
			ServiceChainInfo queryByServiceChainId(String sfcId) throws ServiceException
			{
				return null;
			}
		};
		serviceFuncPathImpl.delete("uuid");;
	}

	@Test(expected = ServiceException.class)
	public void testdeleteBranch2() throws ServiceException 
	{				
		new MockUp<ServiceChainInfoDao>()
		{
			@Mock
			ServiceChainInfo queryByServiceChainId(String sfcId) throws ServiceException
			{
				return new ServiceChainInfo();
			}
		};

		new MockUp<NetworkElementInvDao>()
		{
			@Mock
			NetworkElementMO query(String id) throws ServiceException
			{
				return null;
			}
		};
		serviceFuncPathImpl.delete("uuid");;
	}

	@Test
	public void testdeleteBranch3() throws ServiceException 
	{				
		new MockUp<ServiceChainInfoDao>()
		{
			@Mock
			ServiceChainInfo queryByServiceChainId(String sfcId) throws ServiceException
			{
				return new ServiceChainInfo();
			}

			@Mock
			void deleteServiceChainInfo(ServiceChainInfo scInfo) throws ServiceException
			{
			}
		};

		new MockUp<NetworkElementInvDao>()
		{
			@Mock
			NetworkElementMO query(String id) throws ServiceException
			{
				NetworkElementMO oNetworkElementMO = new NetworkElementMO();
				oNetworkElementMO.setId("1001");
				oNetworkElementMO.setIpAddress("10.12.13.14");
				return oNetworkElementMO;
			}
		};

		new MockUp<DCDeviceConfigReader>()
		{
			@Mock
			DCDeviceConfig getDeviceConfigByIp(String id) throws ServiceException
			{
				DCDeviceConfig oDeviceConfig = new DCDeviceConfig();
				return oDeviceConfig;
			}
		};

		new MockUp<VpnInstanceUtil>()
		{
			@Mock
			String queryVpnInstanceName(DCDeviceConfig deviceCfg, String vpcName) throws ServiceException
			{
				return "VpnInstanceName";
			}

			@Mock
			String queryInBoundInterface(DCDeviceConfig deviceCfg, String vpcName) throws ServiceException
			{
				return "InBoundInterface";
			}

			@Mock
			String queryOutBoundInterface(DCDeviceConfig deviceCfg, String vpcName) throws ServiceException
			{
				return "OutBoundInterface";
			}
		};

		new MockUp<GatewayConfigurationAPI>()
		{
			@Mock
			void deLoadConfiguration(ServiceChainInfo scInfo) throws ServiceException
			{
			}
		};

		new MockUp<FirewallConfigurationAPI>()
		{
			@Mock
			void deLoadConfiguration(ServiceChainInfo scInfo) throws ServiceException
			{
			}
		};	
		serviceFuncPathImpl.delete("uuid");;
	}
}
