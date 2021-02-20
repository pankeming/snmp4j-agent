package org.snmp4j.agent.example;

import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.MOServer;
import org.snmp4j.smi.OctetString;

public class Yn01Modules implements MOGroup {


    @Override
    public void registerMOs(MOServer server, OctetString context) throws DuplicateRegistrationException {

    }

    @Override
    public void unregisterMOs(MOServer server, OctetString context) {

    }
}
