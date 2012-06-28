In order to use openlegacy with Applinx, the following actions needs to be performed:
- Place your copy of apx license file under: openlegacy-applinx/src/test/resources/apx (Tested with version 8.2)
- Place the following JAR's from Applinx installation in lib folder:
gxserver.jar, gxadmin.jar, gxframework.jar, saglic.jar

- Install to local maven repository: Edit each of the following launchers under: openlegacy\openlegacy-providers\openlegacy-applinx\lib,
to match your local maven path (typically C:\Users\**** YOUR_USER*****\.m2)

- Execute ALL the launchers (right click run as -> <launcher name> )
- Verify execution is successful in eclipse console
  
