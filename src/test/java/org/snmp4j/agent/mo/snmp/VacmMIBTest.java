/*_############################################################################
  _## 
  _##  SNMP4J-Agent 3 - VacmMIBTest.java  
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
package org.snmp4j.agent.mo.snmp;

import org.junit.Test;
import org.snmp4j.agent.DefaultMOServer;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class VacmMIBTest {

    @Test
    public void isBitSet() {
        OID testOID =     new OID("1.3.6.1.4.1.4976.1.1.7.6.2.2.1.4.1.2.1");
        OID testSubTree = new OID("1.3.6.1.4.1.4976.1.1.7.6.2.2.1.1.1");
        OctetString testMsk = OctetString.fromHexString("ff:fc");
        int[] expectedResult = new int[] {
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 0, 0
        };
        int[] result = new int[testSubTree.size()];
        for (int i=0; i<testSubTree.size() && i<testMsk.length()*8; i++) {
            result[i] = VacmMIB.isBitSet(i, testMsk) ? 1 : 0;
        }
        assertArrayEquals(Arrays.copyOf(expectedResult, testSubTree.size()), result);
        boolean match = true;
        for (int i=0; i< testSubTree.size(); i++) {
            if ((testSubTree.get(i) != testOID.get(i)) && VacmMIB.isBitSet(i, testMsk)) {
                match = false;
                break;
            }
        }
        assertTrue(match);
    }

    @Test
    public void testGetViews() {
        OctetString viewName = new OctetString("testCmd2View");
        DefaultMOServer server = new DefaultMOServer();
        VacmMIB vacmMIB = new VacmMIB(new DefaultMOServer[] { server });
        vacmMIB.addViewTreeFamily(viewName, new OID("1.3.6.1.4.1.4976.1.1.7.6.2.1.1.3.1.2"),
                OctetString.fromHexString("ff:fc:80"), VacmMIB.vacmViewIncluded, StorageType.nonVolatile);
        vacmMIB.addViewTreeFamily(viewName, new OID("1.3.6.1.4.1.162.1.1.7.6.1.1.1.9.1.2"),
                OctetString.fromHexString("ff:fc:80"), VacmMIB.vacmViewIncluded, StorageType.nonVolatile);
        vacmMIB.addViewTreeFamily(viewName, new OID("1.3.6.1.4.1.162.1.1.7.6.2.2.1.1.1"),
                OctetString.fromHexString("ff:fc"), VacmMIB.vacmViewIncluded, StorageType.nonVolatile);
        List<MOTableRow> views = vacmMIB.getViews(viewName);
        assertEquals(3, views.size());
    }
}
