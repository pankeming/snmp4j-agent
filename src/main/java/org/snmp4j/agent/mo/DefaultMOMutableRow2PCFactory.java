/*_############################################################################
  _## 
  _##  SNMP4J-Agent 3 - DefaultMOMutableRow2PCFactory.java  
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

import org.snmp4j.smi.*;

public class DefaultMOMutableRow2PCFactory implements MOTableRowFactory<DefaultMOMutableRow2PC> {

  public DefaultMOMutableRow2PCFactory() {
  }

  /**
   * Creates a new <code>MOMutableRow2PC</code> row instance and returns it.
   *
   * @param index the index OID for the new row.
   * @param values the values to be contained in the new row.
   * @return the created <code>DefaultMOMutableRow2PC</code> by default.
   * @throws UnsupportedOperationException if the specified row cannot be
   *   created.
   */
  public DefaultMOMutableRow2PC createRow(OID index, Variable[] values) throws
      UnsupportedOperationException {
    return new DefaultMOMutableRow2PC(index, values);
  }

  public void freeRow(DefaultMOMutableRow2PC row) {
    // nothing to do here by default
  }
}
