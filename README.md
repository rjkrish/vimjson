# vimjson

Lightweight JSON data binding wrapper for the VMware vSphere webservices SDK.

## Design Principles
* Follow the grain of the vSphere SDK - no new abstractions, no wrapper classes, decorators etc.
* Low conceptual overhead - low learning curve for those who are familiar with the vSphere MOB.
* Provide a resource-oriented, functional interface; not a hierarchical object model
* Favor utility over completeness - address the common cases first

## vSphere SDK setup
1. Download SDK zip file
2. Unzip
3. cd vsphere-ws/java/JAXWS
4. Run build.sh
5. cd lib
6. Install vim25.jar into local Maven repo.
mvn install:install-file -Dfile=./vim25.jar -DgroupId=com.vmware -DartifactId=vim25 -Dversion=6.0.0 -Dpackaging=jar

## Running the code
mvn test -DargLine="-Dvc.host=127.0.0.1 -Dvc.username=administrator@vsphere.local -Dvc.password='password' -Dvc.vmname='VMware vCenter Server Appliance'"