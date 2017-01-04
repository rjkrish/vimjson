package com.github.rjkrish.vimjson;

import com.fasterxml.jackson.databind.JsonNode;

public class RootFolderTest
        extends ConnectedTest {

    private final RootFolder rootFolder = new RootFolder();

    public RootFolderTest(String testName) {
        super(testName);
    }


    public void testFind() {
        System.out.println("--- testFind ---");
        try {
            JsonNode obj = rootFolder.get(connection.getServiceContent());

            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));

            JsonNode typ = obj.findPath("type");
            assertEquals("Folder", typ.asText());

        } catch (Exception x) {
            x.printStackTrace(System.err);
            fail(x.getMessage());
        }
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
