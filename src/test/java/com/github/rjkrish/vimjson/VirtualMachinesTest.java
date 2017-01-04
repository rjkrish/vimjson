package com.github.rjkrish.vimjson;

import com.fasterxml.jackson.databind.JsonNode;
import junit.framework.Test;
import junit.framework.TestSuite;

public class VirtualMachinesTest
        extends ConnectedTest {

    private final VirtualMachines virtualMachines = new VirtualMachines();

    public VirtualMachinesTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(VirtualMachinesTest.class);
    }


    public void testFindByName() {
        System.out.println("--- testFindByName ---");

        String vmName = System.getProperty("vc.vmname");
        assertNotNull(vmName);

        System.out.println("Name: " + vmName);

        try {
            JsonNode obj = virtualMachines.findByName(
                    connection.getVimPort(),
                    connection.getServiceContent(),
                    vmName
                    , "name"
                    , "guest"
//                  , "config"
            );

            println(obj);

            JsonNode typ = obj.findPath("obj").findPath("type");

            assertEquals("VirtualMachine", typ.asText());

            // Now find the property with name = "name"
            JsonNode ps = obj.findPath("propSet");

            assertNotNull(ps);
            assertTrue(ps.isArray());

            JsonNode nameObj = null;
            JsonNode guestObj = null;
            for (JsonNode propObj : ps) {
                String objName = propObj.findValue("name").asText("");
                if (objName.equals("name"))
                    nameObj = propObj;
                else if (objName.equals("guest"))
                    guestObj = propObj;
            }

            assertNotNull(nameObj);
            println(nameObj);
            assertEquals(vmName, nameObj.findValue("val").asText(""));

            assertNotNull(guestObj);
            // println(guestObj);
            assertEquals("linuxGuest", guestObj.findValue("val").findValue("guestFamily").asText());
            assertEquals("SUSE Linux Enterprise 11 (64-bit)", guestObj.get("val").get("guestFullName").asText());

        } catch (Exception x) {
            x.printStackTrace(System.err);
            fail(x.getMessage());
        }


        assertTrue(true);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

}
