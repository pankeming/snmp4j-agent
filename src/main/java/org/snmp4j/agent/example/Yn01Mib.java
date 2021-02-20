package org.snmp4j.agent.example;

import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.MOServer;
import org.snmp4j.agent.mo.*;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;

public class Yn01Mib

implements MOGroup
{
    private static final LogAdapter LOGGER =
            LogFactory.getLogger(Yn01Mib.class);

    private MOFactory moFactory = DefaultMOFactory.getInstance();

    public static final OID oidYn01Mib =
            new OID(new int[]{1,3,6,1,4,1,828,2});


    public static final OID oidDevBaseInfoTableType =
            new OID(new int[]{1,3,6,1,4,1,828,2,2});
    public static final OID oidDevNameScalar =
            new OID(new int[]{1,3,6,1,4,1,828,2,2,1,1});
    public static final OID oidDevModelScalar =
            new OID(new int[]{1,3,6,1,4,1,828,2,2,1,2});

    //Enumerations




    //TextualConventions

    //Scalars
    private MOScalar devName;  //设备名称
    private MOScalar devModel;  //设备型号

    //Columnar
    //private MOColumn devName;
    //private MOColumn devModel;

    //Tables
    private MOTable devBaseInfo;


    //--AgentGen BEGIN=_MEMBERS
    //--AgentGen END

    protected Yn01Mib() {
    //--AgentGen BEGIN=_DEFAULTCONSTRUCTOR
    //--AgentGen END
    }

    /**
     * Constructs a Yn01Mib instance and actually creates its
     * <code>ManagedObject</code> instances using the supplied
     * <code>MOFactory</code> (by calling
     * {@link #createMO(MOFactory moFactory)}).
     * @param moFactory
     *    the <code>MOFactory</code> to be used to create the
     *    managed objects for this module.
     */

    public Yn01Mib(MOFactory moFactory){
        createMO(moFactory);
    //--AgentGen BEGIN=_FACTORYCONSTRUCTOR
    //--AgentGen END
    }

    //--AgentGen BEGIN=_CONSTRUCTORS
    //--AgentGen END

    /**
     * Create the ManagedObjects defined for this MIB module
     * using the specified {@link MOFactory}.
     * @param moFactory
     *    the <code>MOFactory</code> instance to use for object
     *    creation.
     */
    protected void createMO(MOFactory moFactory){
        addTCsToFactory(moFactory);
        devName =
                moFactory.createScalar(oidDevNameScalar,moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                        new OctetString("YN01-tapTopComputer"));

        devModel =
                moFactory.createScalar(oidDevModelScalar,moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                        new OctetString("YN01"));
    }

    public  MOScalar getDevName(){
        return devName;
}

    public  MOScalar getDevModel(){
        return devModel;
    }

    public void registerMOs(MOServer server,OctetString context)
        throws DuplicateRegistrationException
    {
        //Scalar Objects
        server.register(this.devName,context);
        server.register(this.devModel,context);

        //--AgentGen BEGIN=_registerMOs
        //--AgentGen END
    }

    public void unregisterMOs(MOServer server, OctetString context) {
        // Scalar Objects
        server.unregister(this.devName, context);
        server.unregister(this.devModel, context);

        //--AgentGen BEGIN=_unregisterMOs
        //--AgentGen END
    }

        // Notifications

        // Scalars

        // Value Validators


        // Rows and Factories


        //--AgentGen BEGIN=_METHODS
        //--AgentGen END

        // Textual Definitions of MIB module Yn01Mib

    private void addTCsToFactory(MOFactory moFactory) {
        //--AgentGen BEGIN=_TC_CLASSES_IMPORTED_MODULES_BEGIN
        //--AgentGen END

        // Textual Definitions of other MIB modules
    }
        public void addImportedTCsToFactory(MOFactory moFactory){

     }

    //--AgentGen BEGIN=_TC_CLASSES_IMPORTED_MODULES_END
    //--AgentGen END

    //--AgentGen BEGIN=_CLASSES
    //--AgentGen END

    //--AgentGen BEGIN=_END
    //--AgentGen END

}


