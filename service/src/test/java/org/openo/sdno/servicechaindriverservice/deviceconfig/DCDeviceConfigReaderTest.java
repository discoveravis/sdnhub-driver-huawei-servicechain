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

package org.openo.sdno.servicechaindriverservice.deviceconfig;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;


/**
 * Reader class of Device Configuration.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-08-27
 */
public class DCDeviceConfigReaderTest 
{
	@Test
	public void testgetInstance() throws ServiceException 
	{
		DCDeviceConfigReader instanceDevConfigReader = DCDeviceConfigReader.getInstance();
		assertTrue(instanceDevConfigReader != null);
	}

	@Test(expected = ServiceException.class)
	public void testgetDeviceConfigByIpBranch1() throws ServiceException
	{
		DCDeviceConfigReader.getInstance().getDeviceConfigByIp("");
	}

	@Test(expected = ServiceException.class)
	public void testgetDeviceConfigByIpBranch2() throws ServiceException
	{
		DCDeviceConfigReader.getInstance().getDeviceConfigByIp("10.12.13.14");
	}
}
