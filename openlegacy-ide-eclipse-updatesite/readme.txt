Manual Build process (for now):
1. Run openlegacy/openlegacy-install (copies all project dependency to lib folder of this project. The openlegacy-install task/launcher copies all web templates (from openlegacy-templates) into openlegacy-eclipse-ide/lib/openlegacy-designtime.jar!templates) 
2. Run openlegacy-ide-eclipse/openlegacy-eclipse-dependencies.launch
3. Open openlegacy-ide-eclipse-updatesite/site.xml
4. Remove openlegacy feature from the root category and re-add it (openlegacy*) - may cause category to hide on installation if not done
5. Delete folders features & plugins of openlegacy-ide-eclipse-updatesite project
6. Run Build All in site.xml
7. Upload to OL updateste via FTP (eclipse.openlegacy.org/updatesite - for privileged users), content of openlegacy-ide-eclipse-updatesite.
 
NOTE:
 build.properties is mapped to this lib folder jars list. Some manual maintenance of build.properties is required upon JAR's versions change
