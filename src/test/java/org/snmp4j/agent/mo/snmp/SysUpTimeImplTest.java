/*_############################################################################
  _## 
  _##  SNMP4J-Agent 3 - SysUpTimeImplTest.java  
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

import java.io.*;

import static org.junit.Assert.fail;

/**
 * Test for the {@link org.snmp4j.agent.mo.snmp.SNMPv2MIB.SysUpTimeImpl} class.
 *
 * @author Frank Fock
 */
public class SysUpTimeImplTest {

  @Test
  public void test3SysUpTimeSerializable() throws IOException {
    SysUpTime sysUpTime1 = new SNMPv2MIB.SysUpTimeImpl();
    SysUpTime sysUpTime2;

   try {
      FileOutputStream fout = new FileOutputStream("target/sysUpTimeTest");
      ObjectOutputStream oos = new ObjectOutputStream(fout);
      oos.writeObject(sysUpTime1);
      oos.close();
    } catch (NotSerializableException e) {
      fail("ERROR: Trying to write object. SysUpTime is not Serializable! " + e.getMessage());
    }

   try {
      FileInputStream fin = new FileInputStream("target/sysUpTimeTest");
      ObjectInputStream ois = new ObjectInputStream(fin);
      sysUpTime2 = (SysUpTime) ois.readObject();
      ois.close();
    } catch (ClassNotFoundException e) {
      fail("ERROR: Trying to read object. SysUpTime is not Serializable! " + e.getMessage());
    }
  }

}
