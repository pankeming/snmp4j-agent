/*_############################################################################
  _## 
  _##  SNMP4J-Agent 3 - module-info.java  
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

module org.snmp4j.agent {
    requires transitive org.snmp4j;
    exports org.snmp4j.agent;
    exports org.snmp4j.agent.cfg;
    exports org.snmp4j.agent.io;
    exports org.snmp4j.agent.io.prop;
    exports org.snmp4j.agent.mo;
    exports org.snmp4j.agent.mo.snmp4j;
    exports org.snmp4j.agent.mo.snmp;
    exports org.snmp4j.agent.mo.snmp.dh;
    exports org.snmp4j.agent.mo.snmp.smi;
    exports org.snmp4j.agent.mo.snmp.tc;
    exports org.snmp4j.agent.mo.ext;
    exports org.snmp4j.agent.mo.lock;
    exports org.snmp4j.agent.mo.util;
    exports org.snmp4j.agent.request;
    exports org.snmp4j.agent.security;
    exports org.snmp4j.agent.util;
    exports org.snmp4j.agent.version;
}
