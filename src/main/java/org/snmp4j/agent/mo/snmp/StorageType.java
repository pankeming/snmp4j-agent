/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - StorageType.java  
  _## 
  _##  Copyright (C) 2005-2014  Frank Fock (SNMP4J.org)
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

import org.snmp4j.agent.mo.MOColumn;
import org.snmp4j.agent.mo.MOMutableColumn;
import org.snmp4j.agent.MOAccess;
import org.snmp4j.agent.mo.MOTable;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.SMIConstants;
import org.snmp4j.smi.Integer32;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.agent.mo.MOTableRow;

public class StorageType extends MOMutableColumn<Integer32> {

  public static final int other = 1;
  public static final int volatile_ = 2;
  public static final int nonVolatile = 3;
  public static final int permanent = 4;
  public static final int readOnly = 5;


  public StorageType(int columnID, MOAccess access,
                     Integer32 defaultValue, boolean mutableInService) {
    super(columnID, SMIConstants.SYNTAX_INTEGER,
          access, defaultValue, mutableInService);
  }

  public StorageType(int columnID, MOAccess access,
                     Integer32 defaultValue) {
    super(columnID, SMIConstants.SYNTAX_INTEGER,
          access, defaultValue);
  }

  /**
   * Gets the value of the StorageType column (if it exists) in the specified row and table.
   * It starts searching for the {@link StorageType} column from the last column and then proceeds down to the first
   * until it finds it. If no {@link StorageType} column can be found, {@code null} is returned.
   * @param row
   *         a row with a {@link StorageType} column (otherwise {@code null} is returned).
   * @param table
   *         the table of the given {@code row}.
   * @return
   *         the {@link Integer32} value of the {@link StorageType} column in the given {@code row} or {@code null} if
   *         the table has no {@link StorageType} column.
   * @since 2.7.3
   */
  public static Integer32 getStorageType(MOTableRow<?> row, MOTable<?,?,?> table) {
    int maxCol = table.getColumnCount();
    for (int c=maxCol-1; c >= 0; c--) {
      MOColumn<?> moColumn = table.getColumn(c);
      if (moColumn instanceof StorageType) {
        return new Integer32(row.getValue(c).toInt());
      }
    }
    return null;
  }

  public synchronized int validate(Variable newValue, Variable oldValue) {
    int v = (((Integer32)newValue).getValue());
    if ((v < 1) || (v > 5)) {
      return SnmpConstants.SNMP_ERROR_WRONG_VALUE;
    }
    if (oldValue != null) {
      int ov = (((Integer32)oldValue).getValue());
      if (ov >= 4) {
        return SnmpConstants.SNMP_ERROR_WRONG_VALUE;
      }
    }
    return super.validate(newValue, oldValue);
  }

  public boolean isVolatile(MOTableRow<Integer32> row, int column) {
    Integer32 value = row.getValue(column);
    if (value != null) {
      int storageType = value.getValue();
      switch (storageType) {
        case other:
        case volatile_:
        case readOnly: {
          return true;
        }
        default:
          return false;
      }
    }
    return false;
  }

}
