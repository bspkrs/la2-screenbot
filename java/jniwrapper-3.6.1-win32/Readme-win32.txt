--------------------------------------------------------------------------------

                 JNIWrapper v3.6.1 for Windows, Linux and MacOS X

Readme and Quick Manual

                       Copyright (c) 2000-2007 TeamDev Ltd. All Rights Reserved.
--------------------------------------------------------------------------------

CONTENTS
1. Release Notes
2. Getting Started
3. Support
4. Purchase Information
5. Legal Information
6. Contact

JNIWrapper  is  the  technology  that  eliminates  difficulties  in working with
native  code from Java(TM) programs  using  standard JNI (Java Native Interface)
approach.

================================================================================
1. RELEASE NOTES

Release notes are available online http://www.teamdev.com/jniwrapper/whats_new.jsf

API  differences between JNIWrapper 3.6 and 3.6.1 versions are available online
at http://www.teamdev.com/downloads/jniwrapper/javadoc/jniwrapper_v3/changes.html.

================================================================================
2. GETTING STARTED

System Requirements
-------------------
The following are general requirements  for running JNIWrapper on any  supported
platform:

   Java 2 compatible SDK or Runtime Environment version 1.3.x, 1.4.x or 1.5.x
   Note: JNIWrapper  memory  consumption  is  negligible  compared  to  that  of 
         Java2 runtime.   

   Windows
   ---------
   JNIWrapper for Windows supports Windows 9x, Me, NT 4.0, 2000, XP and 2003.

Package Contents and Installation
---------------------------------
JNIWrapper package consists of the following main files:

lib/jniwrap.jar - Library JAR file
bin/jniwrap.dll - Native code library for Windows
bin/libjniwrap.so - Native code library for Linux
bin/libjniwrap.jnilib - Native code library for MacOS X
docs/ - includes JNIWrapper documentation.
Readme.txt  - this file
Runtime.txt - describes how to deploy applications, which use JNIWrapper
License.txt - license agreement
samples/ - samples, which demonstrate basic features of JNIWrapper

You can select a package type that fits your needs best on  JNIWrapper Download
page (http://www.teamdev.com/jniwrapper/downloads.jsf).

All the files have to be placed in the appropriate locations. Please read below
for more details about the product installation instructions.

   Installing Library JAR File
   ---------------------------
JNIWrapper JAR file should be on the program's class path.
Library file  can be  also placed  on the  boot class  path or  in the extension
directory of Java runtime, but this is not required.

   Installing Native Code Library
   ------------------------------
   
JNIWrapper native  DLL can  be placed  virtually anywhere.  Its actual  location
should take into  account that Java  code must find  the DLL to  load. It can be
placed  somewhere  within program's  library  search path. Users  can add search 
path to the default library loader used by JNIWrapper or even write a custom one 
that searches for  native code in  a predefined location. Since  JNIWrapper v3.0
native  libraries  can  be  put into  any  jar  library  from the  application's
classpath (but not into the META-INF folder).

   Windows
   ---------
Certain  users may like to install the  native  DLL into the directories on  the
default  system path (e.g. Windows  root or  Windows\System32) –  this  requires
adequate  rights  on NT/2000/XP systems. Installing  native DLL  this way may be
convenient, but is not required.

   Installing License File
   -----------------------
License file is not the part of this package.  It has to be obtained separately.
Visit  TeamDev  site  (http://www.teamdev.com) to purchase the product or get an 
evaluation license.

License  file  has  to  be installed  in  the  same directory as the native code
library or in the  META-INF subfolder of any jar library from the  application's 
classpath. Do NOT rename the license file or it will not be recognized.

================================================================================
2. SUPPORT

If  you  encounter any problems  or  have questions regarding our product please
first check the documents listed  below. The answer to your question may already
present there.

 - Installation instructions
 - User's Guide
 - Troubleshooting section (http://www.teamdev.com/jniwrapper/tshoot.jsf)
 - Frequently Asked Questions (FAQ)(http://www.teamdev.com/jniwrapper/faq.jsf)
 - JNIWrapper Forum (http://support.teamdev.com/category.jspa?categoryID=10)

If none of above sources contains  the information that you need please  send us
an e-mail at the following address jniwrapper-support@teamdev.com.

Reporting Problems
------------------
Should you experience a problem or find a bug, please submit us  an issue  using 
the  special  report  form on TeamDev Support site (http://support.teamdev.com).
The form will help you provide all necessary information.

================================================================================
3. PURCHASE INFORMATION

To obtain the latest version of JNIWrapper and to receive up-to-date
information visit: http://www.teamdev.com/jniwrapper/downloads.jsf

To purchase JNIWrapper online, please point your browser to:
http://www.teamdev.com/jniwrapper/purchase.jsf

================================================================================
4. LEGAL INFORMATION

Development and Runtime License
-------------------------------
Having purchased JNIWrapper license you receive the license pack  that  includes
two types of license keys:
 - development key: to be installed at each of  your  developers'  workstations.
   You can install development licenses on the number of computers  not  greater
   than the number of licenses purchased.
 - runtime  key:  it  has  to  be  deployed  with  your   application   to   the
   customers. There is no  limit  for  client  installations  number  with  this
   runtime license key.

Evaluation License
------------------
The evaluation key received upon request on our web site grants  you  permission
to use JNIWrapper for evaluation purposes during the period of 30 days.

License Agreement
-----------------
Also please be sure to read carefully the license  agreement  document  supplied
with the product in the License.txt file.

Acknowledgements
----------------
This product includes software  developed  by  the  Apache  Software  Foundation 
(http://www.apache.org/).

================================================================================
5. CONTACT

TeamDev Ltd.
E-mail  : info@teamdev.com, or
Phone   : 1-425-223-3079 (US),
          38-057-766-0163 (UA)
          Monday - Friday, from 11 a.m. to 7 p.m. (GMT+2)
Web     : http://www.teamdev.com
