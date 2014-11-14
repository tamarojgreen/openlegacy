Build process:
1. Run openlegacy/openlegacy-install (copies all project dependency to lib folder of this project. The openlegacy-install task/launcher copies all web templates (from openlegacy-templates) into openlegacy-eclipse-ide/lib/openlegacy-designtime.jar!templates) 
2. Run openlegacy-ide-eclipse/openlegacy-eclipse-dependencies.launch
3. Open openlegacy-ide-eclipse-updatesite/site.xml
 
NOTE:
 build.properties is mapped to this lib folder jars list. Some manual maintenance of build.properties is required upon JAR's versions change
