/*_############################################################################
  _## 
  _##  SNMP4J-Agent 3 - UsmDHParametersImplTest.java  
  _## 
  _##  Copyright (C) 2005-2018  Frank Fock (SNMP4J.org)
  _##  
  _##  Licensed under the Apache License, Version 2.0 (the "License");
  _##  you may not use this file except in compliance with the License.
  _##  You may obtain a copy of the License at
  _##  
  _##      http://www.apache.org/licenses/LICENSE-2.0
  _##  
  _##  Unless required by applicable law or agreed to in writing, software
  _##  distributed under the License is distributed on an "AS IS" BASIS,
  _##  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  _##  See the License for the specific language governing permissions and
  _##  limitations under the License.
  _##  
  _##########################################################################*/
package org.snmp4j.agent.mo.snmp.dh;

import org.junit.Test;
import org.snmp4j.security.dh.DHGroups;
import org.snmp4j.security.dh.DHParameters;
import org.snmp4j.smi.OctetString;

import java.util.Base64;

import static org.junit.Assert.*;

/**
 * Junit test for {@link UsmDHParametersImpl} class.
 */
public class UsmDHParametersImplTest {

    @Test
    public void encodeBER() throws Exception {
        DHParameters usmDHParameters = new DHParameters(DHGroups.P14, DHGroups.G, 2048);
        OctetString encodedDHParameters = DHParameters.encodeBER(DHGroups.P14, DHGroups.G, 2048);
        DHParameters decodedUsmDHParameters = DHParameters.getDHParametersFromBER(encodedDHParameters);
        assertEquals(usmDHParameters, decodedUsmDHParameters);
    }

    @Test
    public void decodeBER() throws Exception {
        byte[] dhParameter = Base64.getDecoder().decode("MIGHAoGBAdilyft4T5FJO0xga8hzoBcUmAZRTlxE930Dfda4LaC8TFsQkibgUYPJ" +
                "AuFWuAnKgjWjLH80Pt1shilYdd8b7vL2TL2/e3BBNr4sr8DJLLa7p04mt0t4CSKO" +
                "Cp+/pqanm6Mq9xJ3Wo4SDrx70czGhwi5ZNLwwusez9Djwq8dX8IDAgEC");
        UsmDHParametersImpl usmDHParameters = new UsmDHParametersImpl(null, null, new OctetString(dhParameter));
        assertEquals("331904441233693192104476260016584324479133460055459757886261577377912",
                usmDHParameters.getDHParamters().getPrime().toString().substring(0, 69));
        assertEquals(2, usmDHParameters.getDHParamters().getGenerator().longValue());
    }

}
