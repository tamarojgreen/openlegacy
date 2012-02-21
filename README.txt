Development environment installation guide:

Pre-requiresites:
JDK 1.6 Installed
Eclipse 3.6 or higher including Maven M2 plugin & AspectJ plug-in
OR (preffered)
Spring tool suite - STS (Spring's eclipse) from http://www.springsource.org/downloads/sts

1.
For STS - start springsource/sts-<VERSION>/sts.exe
OR start your eclipse

Select a clean workspace folder

3.
Select file-> "import" -> Git -> "Project from git"
On "Check HOME Directory" dialog click OK

4.
Copy the following URL to your clipboard: https://github.com/openlegacy/openlegacy.git
Click "Clone"
Provide your user & password

5.
Select the working branch (master). Click "Next"

6.
Select a local git folder. Click "Next", and "Finish"
Wait for the import to complete

7.
Once complete, select "Next" & "Import existing projects"
Select all the project beside "openlegacy-web-resources"

8.
Now import individual project into the workspace.
File->Import -> General ->Existing projects into workspace
Select:
<GIT LOCAL DIRECTORY>/openlegacy/openlegacy-api
<GIT LOCAL DIRECTORY>/openlegacy/openlegacy-impl

Perform project -> clean..., "Clean all project", in case of errors
Execute the test suite: OpenLegacyRuntimeSuite to run all OpenLegacy runtime unitests.
On windows, you may need to allow Windows firewall
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

10.
For Demo web application, import openlegacy/openlegacy-templates/openlegacy-mvc-sample project,
right click on "run-openlegacy-mvc-sample.launch", and click run as-> "run-openlegacy-mvc-sample".
Open your browser to http://localhost:8080/mvc