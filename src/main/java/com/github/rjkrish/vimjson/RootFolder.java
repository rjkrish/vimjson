package com.github.rjkrish.vimjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.rjkrish.vimjson.util.VimJsonMapper;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.ServiceContent;

public class RootFolder extends VimJsonMapper {
    public JsonNode get(ServiceContent serviceContent)
            throws RuntimeFaultFaultMsg, JsonProcessingException

    {
        return objectMapper.convertValue(serviceContent.getRootFolder(), JsonNode.class);
    }

}
