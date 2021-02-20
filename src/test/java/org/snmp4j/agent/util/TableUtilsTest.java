/*_############################################################################
  _## 
  _##  SNMP4J-Agent 3 - TableUtilsTest.java  
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
package org.snmp4j.agent.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.snmp4j.*;
import org.snmp4j.agent.*;
import org.snmp4j.agent.cfg.EngineBootsProvider;
import org.snmp4j.agent.mo.*;
import org.snmp4j.agent.mo.snmp.StorageType;
import org.snmp4j.agent.security.VACM;
import org.snmp4j.log.ConsoleLogFactory;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import org.snmp4j.log.LogLevel;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TableUtilsTest {

    static {
        LogFactory.setLogFactory(new ConsoleLogFactory());
        LogFactory.getLogFactory().getRootLogger().setLogLevel(LogLevel.WARN);
    }

    private static final LogAdapter logger = LogFactory.getLogger(TableUtilsTest.class);

    private static final OID TEST_TABLE_OID = new OID("1.3.6.1.4.1.4976.6.3.1.3");

    private AgentConfigManager agentConfigManager;
    private DefaultMOServer moServer;
    private DefaultUdpTransportMapping tmCR;
    private TransportMapping<?> tmCS;
    private OctetString engineID;

    @Before
    public void setUp() throws Exception {
        moServer = new DefaultMOServer();
        engineID = new OctetString(MPv3.createLocalEngineID());
        VACM vacm = new VACM() {
            @Override
            public int isAccessAllowed(OctetString context, OctetString securityName,
                                       int securityModel, int securityLevel, int viewType, OID oid) {
                return VACM_OK;
            }

            @Override
            public int isAccessAllowed(OctetString viewName, OID oid) {
                return VACM.VACM_OK;
            }

            @Override
            public OctetString getViewName(OctetString context, OctetString securityName,
                                           int securityModel, int securityLevel, int viewType) {
                return new OctetString();
            }
        };
        MessageDispatcher messageDispatcher = new MessageDispatcherImpl();
        messageDispatcher.addMessageProcessingModel(new MPv2c());
        tmCR = new DefaultUdpTransportMapping(new UdpAddress("127.0.0.1/0"));
        tmCS = new DefaultUdpTransportMapping(new UdpAddress("127.0.0.1/0"));
        messageDispatcher.addTransportMapping(tmCR);
        agentConfigManager = new AgentConfigManager(engineID, messageDispatcher, vacm, new MOServer[]{moServer},
                ThreadPool.create("test-pool", 2), null, null, new EngineBootsProvider() {
            @Override
            public int updateEngineBoots() {
                return 2;
            }

            @Override
            public int getEngineBoots() {
                return 1;
            }
        },
                DefaultMOFactory.getInstance());
    }

    @After
    public void tearDown() throws Exception {
        agentConfigManager.shutdown();
        tmCR.close();
        tmCS.close();
    }

    @Test
    public void getTableTestDoubleCheckIncomplete() {
        testTableUtils(20, 6, 3, 10, new int[][] {{ 11, 7 }}, new int[0][0],
                TableUtils.SparseTableMode.denseTableDoubleCheckIncompleteRows, 20+1);
    }

    @Test
    public void getTableTestDoubleCheckIncompleteWithRemove() {
        testTableUtils(20, 6, 3, 10, new int[][] {{ 11, 7 }}, new int[][] {{ 61, 56 }},
                TableUtils.SparseTableMode.denseTableDoubleCheckIncompleteRows, 20);
    }

    @Test
    public void getTableTestDropIncomplete() {
        testTableUtils(20, 6, 3, 10, new int[][] {{ 11, 7 }},new int[0][0],
                TableUtils.SparseTableMode.denseTableDropIncompleteRows, 20);
    }

    @Test
    public void getTableSparse() {
        testTableUtils(20, 6, 3, 10, new int[][] {{ 11, 7 }}, new int[0][0],
                TableUtils.SparseTableMode.sparseTable, 20+1);
    }

    @SuppressWarnings({"rawtypes"})
    private void testTableUtils(int numRows, int numCols, int maxColsPerPDU, int maxNumRowsPerPDU,
                                int[][] dynamicNewRows, int[][] dynamicDeleteRows, TableUtils.SparseTableMode sparseTableMode,
                                int expectedTableEventRows) {
        MOColumn<?>[] moColumns = new MOColumn<?>[numCols];
        for (int i=0; i<moColumns.length; i++) {
            moColumns[i] = new MOColumn<>(i+2, SMIConstants.SYNTAX_OCTET_STRING);
        }
        DefaultMOTable<DefaultMOTableRow,MOColumn,DefaultMOTableModel<DefaultMOTableRow>> moTable =
                new DefaultMOTable<>(
                        TEST_TABLE_OID,
                        new MOTableIndex(new MOTableSubIndex[] { new MOTableSubIndex(SMIConstants.SYNTAX_INTEGER32)}),
                moColumns);
        for (int i=0; i<numRows*5; i+=5) {
            Variable[] values = new Variable[moColumns.length];
            for (int c=0; c< values.length; c++) {
                values[c] = new OctetString(""+(c+2));
            }
            moTable.addRow(new DefaultMOTableRow(new Integer32(i+1).toSubIndex(false), values));
        }

        agentConfigManager.addAgentStateListener(new AgentStateListener<AgentConfigManager>() {
            @Override
            public void agentStateChanged(AgentConfigManager agentConfigManager, AgentState newState) {
                if (newState.getState() == AgentState.STATE_CONFIGURED) {
                    try {
                        moServer.register(moTable, null);
                    } catch (DuplicateRegistrationException e) {
                        assertNull(e);
                    }
                }
            }
        });
        agentConfigManager.run();
        agentConfigManager.getSnmpCommunityMIB().addSnmpCommunityEntry(new OctetString("index"),
                new OctetString("public"), new OctetString("dummy"), engineID,
                new OctetString(), new OctetString(), StorageType.readOnly);
        moServer.addLookupListener(new MOServerLookupListener() {
            @Override
            public void lookupEvent(MOServerLookupEvent event) {
            }

            @Override
            public void queryEvent(MOServerLookupEvent event) {
                for (int[] dynamicNewRow : dynamicNewRows) {
                    OID startRow = new OID(TEST_TABLE_OID);
                    startRow.append(2).append(dynamicNewRow[0]);
                    if (event.getQuery().getLowerBound().compareTo(startRow) == 0) {
                        for (int j = 0; j < numCols; j++) {
                            Variable[] values = new Variable[moColumns.length];
                            for (int c = 0; c < values.length; c++) {
                                values[c] = new OctetString("" + j + ":" + (c + 2));
                            }
                            DefaultMOTableRow newRow = new DefaultMOTableRow(
                                    new Integer32(dynamicNewRow[1]).toSubIndex(false), values);
                            moTable.addRow(newRow);
                        }
                    }
                }
                for (int[] dynamicDeleteRow : dynamicDeleteRows) {
                    OID startRow = new OID(TEST_TABLE_OID);
                    startRow.append(2).append(dynamicDeleteRow[0]);
                    if (event.getQuery().getLowerBound().compareTo(startRow) == 0) {
                        moTable.removeRow(new Integer32(dynamicDeleteRow[1]).toSubIndex(false));
                    }
                }
            }
        }, moTable);

        Snmp snmp = new Snmp(tmCS);
        try {
            snmp.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TableUtils tableUtils = new TableUtils(snmp, new DefaultPDUFactory(PDU.GETBULK));
        tableUtils.setMaxNumRowsPerPDU(maxNumRowsPerPDU);
        tableUtils.setMaxNumColumnsPerPDU(maxColsPerPDU);
        tableUtils.setSendColumnPDUsMultiThreaded(false);
        OID[] requestColumns = new OID[numCols];
        for (int c=0; c<requestColumns.length; c++) {
            requestColumns[c] = new OID(TEST_TABLE_OID.getValue(), c+2);
        }
        TableTestListener tableListener = new TableTestListener();
        synchronized (tableListener) {
            CommunityTarget communityTarget =
                    new CommunityTarget<UdpAddress>(tmCR.getListenAddress(), new OctetString("public"));
            communityTarget.setVersion(SnmpConstants.version2c);
            tableUtils.getTable(communityTarget, requestColumns, tableListener, null, null, null,
                    sparseTableMode);
            try {
                tableListener.wait(5000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                assertNull(ie);
            }
            assertEquals(expectedTableEventRows+1, tableListener.tableEventList.size());
            checkTableEventForOrderAndNullValues(moColumns, tableListener, sparseTableMode);
            assertEquals(0, tableListener.tableEventList.get(tableListener.tableEventList.size()-1).getStatus());
            try {
                snmp.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void checkTableEventForOrderAndNullValues(MOColumn<?>[] moColumns, TableTestListener tableListener,
                                                        TableUtils.SparseTableMode sparseTableMode) {
        OID lastIndex = new OID();
        for (TableEvent tableEvent : tableListener.tableEventList) {
            if (tableEvent.getIndex() != null) {
                assertEquals(moColumns.length, tableEvent.getColumns().length);
                if (sparseTableMode != TableUtils.SparseTableMode.sparseTable) {
                    for (VariableBinding vb : tableEvent.getColumns()) {
                        assertNotNull(vb);
                    }
                }
                assertTrue(lastIndex.compareTo(tableEvent.getIndex()) < 0);
                lastIndex = tableEvent.getIndex();
            }
        }
    }

    private class TableTestListener implements TableListener {
        public List<TableEvent> tableEventList = new ArrayList<>();
        @Override
        public boolean next(TableEvent event) {
            tableEventList.add(event);
            logger.debug("TableListener.next "+event.toString());
            return true;
        }

        @Override
        public synchronized void finished(TableEvent event) {
            tableEventList.add(event);
            logger.debug("TableListener.finished "+event.toString());
            notify();
        }

        @Override
        public boolean isFinished() {
            return false;
        }
    }
}
