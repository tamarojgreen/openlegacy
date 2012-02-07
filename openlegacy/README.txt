Development environment installation guide:

1.
Download Spring Tools suite from:
http://www.springsource.org/downloads/sts
and extract it.
2.
Start: springsource/sts-<VERSION>/sts.exe

Select a clean workspace folder

3.
Select "import" -> "Project from git"
On "Check HOME Directory" click OK

4.
Click "Clone"
Enter: https://openlegacy@github.com/openlegacy/openlegacy.git
Provide your user & password
TODO check annonymous

5.
Select the working branch (master). Click "Next"

6.
Select a local git folder. Click "Next", and "Finish"
Wait for the import to complete

7.
Once complete, select "Next" & "Import existing projects"
Select all the project beside "openlegacy-web-resources", "openlegacy-mvc-sample"

8.
Now import individual project into the workspace.
Import -> Existing projects into workspace
Select:
<GIT LOCAL DIRECTORY>/openlegacy/openlegacy-api
<GIT LOCAL DIRECTORY>/openlegacy/openlegacy-impl

Perform project -> clean..., "Clean all project", in case of errors
Execute the test suite: OpenLegacyRuntimeSuite to run all OpenLegacy runtime unitests.

Optional projects:
<GIT LOCAL DIRECTORY>/openlegacy/openlegacy-designtime
<GIT LOCAL DIRECTORY>/openlegacy/openlegacy-ide-eclipse

designtime project is using Drools rule engine. It is recommended to install the plugin from:
http://download.jboss.org/jbosstools/updates/development/indigo/soa-tooling/

To install the plugin, select "Help" -> "Install New Software", click "Add..." and add the above URL.
Select "JBoss SOA development" -> JBoss Drools Core.
Uncheck "Contact All update sites".

9.
Import code style preferences. Select Import -> prefrences, and select the file from openlegacy/eclipsep-

openlegacy-prefs.epf