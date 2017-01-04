package com.github.rjkrish.vimjson.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.VimPortType;

public abstract class VimJsonMapper {

    protected final ObjectMapper objectMapper ;

    public VimJsonMapper() {
        objectMapper = new ObjectMapper();
    }

    public VimJsonMapper(ObjectMapper om) {
        objectMapper = om;
    }

    public ArrayNode list(VimPortType vimPort, ServiceContent serviceContent
    ) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, JsonProcessingException {

        return null;
    }

    public ObjectNode findByRef(VimPortType vimPort, ServiceContent serviceContent,
                                final String ref, String... props
    ) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        return null;
    }

}
