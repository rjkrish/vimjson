package com.github.rjkrish.vimjson;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.rjkrish.vimjson.util.VimJsonMapper;
import com.vmware.vim25.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class VirtualMachines extends VimJsonMapper {

    public VirtualMachines() {
        super();
    }


    /**
     * Finds a virtual machine by name.
     * If there are multiple virtual machines with the same name,
     * the returned value is non-deterministic.
     *
     * @param vimPort
     * @param serviceContent
     * @param vmName         name of VM to get
     * @param vmProps
     * @return virtual machine information.
     * @throws InvalidPropertyFaultMsg
     * @throws RuntimeFaultFaultMsg
     */
    public JsonNode findByName(VimPortType vimPort, ServiceContent serviceContent,
                               final String vmName, String... vmProps
    ) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {

        ObjectContent rslt = new ObjectContent();
        ManagedObjectReference rootFolder = serviceContent.getRootFolder();
        PropertySpec propSpec = new PropertySpec();

        // Now create Object Spec
        ObjectSpec objectSpec = new ObjectSpec();
        objectSpec.setObj(rootFolder);
        objectSpec.setSkip(true);
        objectSpec.getSelectSet().add(getVmTraversalSpec());

        propSpec.setAll(false);
        propSpec.setType("VirtualMachine");
        propSpec.getPathSet().addAll(Arrays.asList(vmProps));

        // Create PropertyFilterSpec using the PropertySpec and ObjectPec
        // created above.
        PropertyFilterSpec propertyFilterSpec = new PropertyFilterSpec();
        propertyFilterSpec.getPropSet().add(propSpec);
        propertyFilterSpec.getObjectSet().add(objectSpec);

        List<PropertyFilterSpec> listpfs =
                new ArrayList<PropertyFilterSpec>(1);
        listpfs.add(propertyFilterSpec);

        List<ObjectContent> listobcont =
                vimPort.retrievePropertiesEx(
                        serviceContent.getPropertyCollector()
                        , listpfs
                        , new RetrieveOptions()).getObjects();

        if (listobcont == null)
            return null;

        for (ObjectContent oc : listobcont) {
            ManagedObjectReference mr = oc.getObj();
            String vmnm = "";
            List<DynamicProperty> dps = oc.getPropSet();
            if (dps != null) {
                for (DynamicProperty dp : dps) {
//                    System.out.println("DP: " + dp.getName() + " :: " + dp.getVal().getClass() );
                    if (dp.getVal() instanceof String && dp.getName().equals("name")) {
                        vmnm = (String) dp.getVal();
//                        System.out.println(dp.getName() + "=" + dp.getVal());
                        if (vmnm.equals(vmName)) {
                            rslt = oc;
                            break;
                        }
                    }
                }
            }
            if (vmnm.equals(vmName))
                break;
        }

        return objectMapper.convertValue(rslt, JsonNode.class);
    }

    private TraversalSpec getVmTraversalSpec() {
        // Create a traversal spec that starts from the 'root' objects
        // and traverses the inventory tree to get to the VirtualMachines.
        // Build the traversal specs bottoms up

        //Traversal to get to the VM in a VApp
        TraversalSpec vAppToVM = new TraversalSpec();
        vAppToVM.setName("vAppToVM");
        vAppToVM.setType("VirtualApp");
        vAppToVM.setPath("vm");

        SelectionSpec v1 = new SelectionSpec();
        v1.setName("vAppToVApp");

        SelectionSpec v2 = new SelectionSpec();
        v2.setName("vAppToVM");


        //Traversal spec for VApp to VApp
        TraversalSpec vAppToVApp = new TraversalSpec();
        vAppToVApp.setName("vAppToVApp");
        vAppToVApp.setType("VirtualApp");
        vAppToVApp.setPath("resourcePool");
        vAppToVApp.getSelectSet().add(v1);
        vAppToVApp.getSelectSet().add(v2);


        //This SelectionSpec is used for recursion for Folder recursion
        SelectionSpec visitFolders = new SelectionSpec();
        visitFolders.setName("VisitFolders");

        // Traversal to get to the vmFolder from DataCenter
        TraversalSpec dataCenterToVMFolder = new TraversalSpec();
        dataCenterToVMFolder.setName("DataCenterToVMFolder");
        dataCenterToVMFolder.setType("Datacenter");
        dataCenterToVMFolder.setPath("vmFolder");
        dataCenterToVMFolder.setSkip(false);
        dataCenterToVMFolder.getSelectSet().add(visitFolders);

        // TraversalSpec to get to the DataCenter from rootFolder
        TraversalSpec rslt = new TraversalSpec();

        rslt.setName("VisitFolders");
        rslt.setType("Folder");
        rslt.setPath("childEntity");
        rslt.setSkip(false);
        rslt.getSelectSet().add(visitFolders);
        rslt.getSelectSet().add(dataCenterToVMFolder);
        rslt.getSelectSet().add(vAppToVM);
        rslt.getSelectSet().add(vAppToVApp);

        return rslt;

    }
}

