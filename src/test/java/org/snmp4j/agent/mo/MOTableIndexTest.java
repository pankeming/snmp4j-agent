/*_############################################################################
  _## 
  _##  SNMP4J-Agent 3 - MOTableIndexTest.java  
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
package org.snmp4j.agent.mo;

import org.junit.Test;
import org.snmp4j.smi.*;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MOTableIndexTest {

    // Index OID definitions
    public static final OID oidId = new OID(new int[] { 1,3,6,1,4,1,4976,1,1,1,4,4,2,1,1 });
    public static final OID oidVersion = new OID(new int[] { 1,3,6,1,4,1,4976,1,1,1,4,4,2,1,2 });
    public static final OID oidInstance = new OID(new int[] { 1,3,6,1,4,1,4976,1,1,1,4,4,2,1,3 });

    @Test
    public void getIndexOIDFixedLength() {
        DefaultMOFactory moFactory = new DefaultMOFactory();
        MOTableSubIndex[] moTableSubIndices =
                new MOTableSubIndex[] {
                        moFactory.createSubIndex(oidId, SMIConstants.SYNTAX_OCTET_STRING, 1, 32),
                        moFactory.createSubIndex(oidVersion,
                                SMIConstants.SYNTAX_OCTET_STRING, 12, 12),
                        moFactory.createSubIndex(oidInstance, SMIConstants.SYNTAX_INTEGER, 1, 1) };
        MOTableIndex moTableIndex = new MOTableIndex(moTableSubIndices, false);
        Variable[] testIndex = new Variable[] {
                new OctetString("TestSignal"),
                new OctetString("V01.01.01.01"),
                new Integer32(5)
        };
        OID indexOID = moTableIndex.getIndexOID(testIndex);
        assertEquals(new OID("10.84.101.115.116.83.105.103.110.97.108.86.48.49.46.48.49.46.48.49.46.48.49.5"), indexOID);
    }

    @Test
    public void getIndexOIDVariableLength() {
        DefaultMOFactory moFactory = new DefaultMOFactory();
        MOTableSubIndex[] moTableSubIndices =
                new MOTableSubIndex[] {
                        moFactory.createSubIndex(oidId, SMIConstants.SYNTAX_OCTET_STRING, 1, 32),
                        moFactory.createSubIndex(oidVersion,
                                SMIConstants.SYNTAX_OCTET_STRING, 11, 12),
                        moFactory.createSubIndex(oidInstance, SMIConstants.SYNTAX_INTEGER, 1, 1) };
        MOTableIndex moTableIndex = new MOTableIndex(moTableSubIndices, false);
        Variable[] testIndex = new Variable[] {
                new OctetString("TestSignal"),
                new OctetString("V01.01.01.0"),
                new Integer32(5)
        };
        OID indexOID = moTableIndex.getIndexOID(testIndex);
        assertEquals(new OID("10.84.101.115.116.83.105.103.110.97.108.11.86.48.49.46.48.49.46.48.49.46.48.5"), indexOID);
        assertEquals("[TestSignal, V01.01.01.0, 5]", Arrays.toString(moTableIndex.getIndexValues(indexOID)));
    }

}
