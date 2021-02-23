package org.snmp4j.agent.example;

import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.MOServer;
import org.snmp4j.agent.mo.MOFactory;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import org.snmp4j.smi.OctetString;

//--AgentGen BEGIN=_IMPORT
//--AgentGen END

public class Yn01Module implements MOGroup {

    private static final LogAdapter LOGGER =
            LogFactory.getLogger(Yn01Mib.class);

    private Yn01Mib yn01Mib;

    private MOFactory factory;

    public Yn01Module(MOFactory factory) {
    }

//--AgentGen BEGIN=_MEMBERS
//--AgentGen END

//    private Yn01Module(){

//        yn01Mib = new Yn01Mib();
//--AgentGen BEGIN=_DEFAULTCONSTRUCTOR
//--AgentGen END
//   }

//    public Yn01Module(){

//        yn01Mib = new Yn01Mib(factory);

//--AgentGen BEGIN=_CONSTRUCTOR
//--AgentGen END
//    }


    @Override
    public void registerMOs(MOServer server, OctetString context) throws DuplicateRegistrationException {

        yn01Mib.registerMOs(server,context);
//--AgentGen BEGIN=_registerMOs
//--AgentGen END
    }

    @Override
    public void unregisterMOs(MOServer server, OctetString context) {

        yn01Mib.unregisterMOs(server,context);
    }

    public Yn01Mib getYn01Mib(){
        return yn01Mib;
    }



//--AgentGen BEGIN=_METHODS
//--AgentGen END

//--AgentGen BEGIN=_CLASSES
//--AgentGen END

//--AgentGen BEGIN=_END
//--AgentGen END

}
