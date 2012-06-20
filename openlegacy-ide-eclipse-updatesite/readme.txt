Manual Build process (for now):
1. openlegacy-install maven task copies all project dependency to lib folder of this project. The openlegacy-install task/launcher copies all web templates (from openlegacy-templates) into openlegacy-eclipse-ide/lib/openlegacy-designtime.jar!templates 
2. build.properties is mapped to this lib folder jars list. Some manual maintenance of build.properties is required upon JAR's versions change
3. To build a full updatesite, open site.xml and click "Build All"