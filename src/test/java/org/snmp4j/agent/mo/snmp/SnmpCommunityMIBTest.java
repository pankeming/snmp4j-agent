/*_############################################################################
  _## 
  _##  SNMP4J-Agent 3 - SnmpCommunityMIBTest.java  
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

import org.junit.Before;
import org.junit.Test;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.USM;
import org.snmp4j.smi.OctetString;

import static org.junit.Assert.*;

/**
 * Created by fock on 08.06.2015.
 */
public class SnmpCommunityMIBTest {

  private MessageDispatcherImpl messageDispatcher;

  @Before
  public void setUp() throws Exception {
    messageDispatcher = new MessageDispatcherImpl();
    messageDispatcher.addMessageProcessingModel(new MPv3());
  }

  @Test
  public void testGetCoexistenceInfo() throws Exception {
    SnmpCommunityMIB snmpCommunityMIB = new SnmpCommunityMIB(new SnmpTargetMIB(messageDispatcher));
    assertNull(snmpCommunityMIB.getCoexistenceInfo(new OctetString("public")));
  }

  @Test
  public void testAddSnmpCommunityEntry() throws Exception {
    SnmpCommunityMIB snmpCommunityMIB = new SnmpCommunityMIB(new SnmpTargetMIB(messageDispatcher));
    snmpCommunityMIB.addSnmpCommunityEntry(new OctetString("index"), new OctetString("public2"),
        new OctetString("public1"), new OctetString(), new OctetString(), null, StorageType.readOnly);
    CoexistenceInfo[] coexistenceInfos = snmpCommunityMIB.getCoexistenceInfo(new OctetString("public2"));
    assertNotNull(coexistenceInfos);
    assertEquals(1, coexistenceInfos.length);
    assertEquals(new OctetString("public1"),coexistenceInfos[0].getSecurityName());
  }

  @Test
  public void testRemoveSnmpCommuntiyEntry() throws Exception {
    SnmpCommunityMIB snmpCommunityMIB = new SnmpCommunityMIB(new SnmpTargetMIB(messageDispatcher));
    snmpCommunityMIB.addSnmpCommunityEntry(new OctetString("index"), new OctetString("public2"),
        new OctetString("public1"), new OctetString(), new OctetString(), null, StorageType.readOnly);
    CoexistenceInfo[] coexistenceInfos = snmpCommunityMIB.getCoexistenceInfo(new OctetString("public2"));
    assertNotNull(coexistenceInfos);
    assertEquals(1, coexistenceInfos.length);
    assertEquals(new OctetString("public1"),coexistenceInfos[0].getSecurityName());
    snmpCommunityMIB.removeSnmpCommuntiyEntry(new OctetString("index"));
    coexistenceInfos = snmpCommunityMIB.getCoexistenceInfo(new OctetString("public2"));
    assertTrue(coexistenceInfos == null || coexistenceInfos.length == 0);
  }
}
