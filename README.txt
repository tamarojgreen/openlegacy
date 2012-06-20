Development environment installation guide:

Pre-requiresites:
JDK 1.6 Installed
Spring tool suite - STS (Spring's eclipse) from http://www.springsource.org/downloads/sts
OR
Eclipse 3.6 or higher including Maven M2eclipse plugin, AspectJ plug-in & EGit plug-in

Tested with STS 2.8.1 and STS 2.9.2

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

Select all projects

8.
Perform project -> clean..., "Clean all project", in case of errors
Execute the test suite: OpenLegacyRuntimeSuite (CTRL+SHIFT+T to find it) to run all OpenLegacy runtime unitests.
On windows, you may need to allow Windows firewall

designtime project is using Drools rule engine. It is recommended to install the plugin from:
http://download.jboss.org/jbosstools/updates/development/indigo/soa-tooling/

To install the plugin, select "Help" -> "Install New Software", click "Add..." and add the above URL.
Select "JBoss SOA development" -> JBoss Drools Core.
Uncheck "Contact All update sites".

9.
Import code style preferences. Select Import -> prefrences, and select the file from openlegacy/eclipsep-

openlegacy-prefs.epf

10.
For running OpenLegacy an web application, import:

- <GIT CLONE DIR>/openlegacy/openlegacy-templates/openlegacy-mvc-sample project (web demo application)
(URL is http://localhost:8080/openlegacy-mvc-sample)

OR 

- <GIT CLONE DIR>/openlegacy/openlegacy-templates/openlegacy-mobile-sample project (mobile demo application)
(URL is http://localhost:8080/openlegacy-mobile-sample)
OR

- <GIT CLONE DIR>/openlegacy/openlegacy-templates/openlegacy-mvc-new project (new empty web application)
(URL is http://localhost:8080/openlegacy-mvc-new/emulation)

right click on "run-application.launch", and click run as-> "run-application".
Verify the launcher is using JDK 1.6 in case of an error.

Open your browser to the chosen application above mentioned URL.