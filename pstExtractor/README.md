# pstExtractor

A JSF-based server for uploading all parts of a PST file except the messages (you can see contacts, tasks, and calendar entries). Note that, as it uses CDI, it needs an EE-capable servlet to run. Tomcat is no longer supported, but Tomcat EE is.

## Security Warning
This application is based on [JSF](https://en.wikipedia.org/wiki/Jakarta_Server_Faces) and the [Apache Tobago](https://myfaces.apache.org/#/tobago) component libraryi (v4.6). Up until 2023, it used the [MyFaces Tomahawk](https://svn.apache.org/repos/asf/myfaces/site/publish/tomahawk/index.html), which has not been updated since 2012 and contains security vulnerabilities.
As such, it should remain a toy application and never be used in production or even on a publicly-accessible server. See the list of vulnerabilities below for more details.

## TomcatEE
This has been tested on Tomcat EE 8, but not on any other web servers. To deploy this on a locally running version of Tomcat, ensure that there is a user with the role _manager-script_ configured in your tomcat-users.xml file. In my case, the user with this role is _script_, and the password is _admin_:

    <tomcat-users>
	<role rolename="manager-script"/>
	<user username="script" password="admin" roles="manager-script"/>
    </tomcat-users>

Add the following section to the <servers> element of your maven settings.xml:
    <!-- Local Tomcat server -->
    <server>
      <id>LocalTomcatServer</id>
      <username>script</username>
      <password>admin</password>
    </server>

You may now deploy/undeploy/redeploy the app via
- `mvn tomcat7:deploy`
- `mvn tomcat7:undeploy`
- `mvn tomcat7:redeploy`

If you find the server is low on memory, add the following line to the file <tomcat-home>/bin/catalina.sh

    # Required for pst file processing
    JAVA_OPTS="$JAVA_OPTS -Xmx1024m"

THe upload limit defaults to 500 MB. If you need to adjust this, change the following entries in [src/main/webapp/WEB_INF/web.xml](src/main/webapp/WEB_INF/web.xml):

### web-app/filter/filter-name/init-params/param-value
    <filter>
      <filter-name>MyFacesExtensionsFilter</filter-name>
        <filter-class>org.apache.myfaces.webapp.filter.ExtensionsFilter</filter-class>
        <init-param>
          <param-name>uploadMaxFileSize</param-name>
          <param-value>500m</param-value>
          <description>Set the size limit for uploaded files.
                  Format: 10 - 10 bytes
                  10k - 10 KB
                  10m - 10 MB
                  1g - 1 GB
          </description>
        </init-param>
      </filter-name>
    </filter>

### web-app/multipart-config
    <multipart-config>
      <!-- Set server upload size to match MyFacesExtensionsFilter -->
      <max-file-size>52428800</max-file-size>
      <max-request-size>52428800</max-request-size>
      <file-size-threshold>0</file-size-threshold>
    </multipart-config>

## Vulnerabilities
[Xanitizer](https://www.rigs-it.com/xanitizer/) reports the following vulnerabilities.

### batik-awt-util 1.6.1 released 2006-07-09 (transitive dependency from org.apache.myfaces.tomahawk20 1.1.14 released 2012-10-23)
*   [CVE-2015-0250](https://www.cvedetails.com/cve/CVE-2015-0250/), CVSS score 6.4 (Denial of service)
*   [CVE-2017-5662](https://www.cvedetails.com/cve/CVE-2017-5662/), CVSS score 7.9 (Denial of service)
*   [CVE-2018-8013](https://www.cvedetails.com/cve/CVE-2018-8013/), CVSS score 7.5 (Information disclosure, file modification, availability)

There is a newer version of this library, but there is no newer version of Tomahawk 2.0 which uses it.

### batik-gui-util 1.6.1 released 2006-07-09 (transitive depedency from org.apache.myfaces tomahawk20 1.1.14 released 2012-10-23) 

This library demonstrates the same vulnerabilities as batik-awt-util.
*   [CVE-2015-0250](https://www.cvedetails.com/cve/CVE-2015-0250/), CVSS score 6.4 (Denial of service)
*   [CVE-2017-5662](https://www.cvedetails.com/cve/CVE-2017-5662/), CVSS score 7.9 (Denial of service)
*   [CVE-2018-8013](https://www.cvedetails.com/cve/CVE-2018-8013/), CVSS score 7.5 (Information disclosure, file modification, availability)

There is a newer version of this library, but there is no newer version of Tomahawk 2.0 which uses it.

### batik-util 1.6.1 released 2006-07-09 (transitive depedency from org.apache.myfaces tomahawk20 1.1.14 released 2012-10-23) 

This library demonstrates the same vulnerabilities as batik-awt-util and batik-gui-util.
*   [CVE-2015-0250](https://www.cvedetails.com/cve/CVE-2015-0250/), CVSS score 6.4 (Denial of service)
*   [CVE-2017-5662](https://www.cvedetails.com/cve/CVE-2017-5662/), CVSS score 7.9 (Denial of service)
*   [CVE-2018-8013](https://www.cvedetails.com/cve/CVE-2018-8013/), CVSS score 7.5 (Information disclosure, file modification, resource availability)

There is a newer version of this library, but there is no newer version of Tomahawk 2.0 which uses it.

### commons-fileupload 1.2.1 released 2008-02-11 (transitive depedency from org.apache.myfaces tomahawk20 1.1.14 released 2012-10-23) 
*   [CVE-2013-0248](https://www.cvedetails.com/cve/CVE-2013-0248/), CVSS score 3.3 (File modification, availability)
*   [CVE-2014-0050](https://www.cvedetails.com/cve/CVE-2014-0050/), CVSS score 7.5 (Denial of service, bypass a restriction or similar)
*   [CVE-2016-1000031](https://www.cvedetails.com/cve/CVE-2016-1000031/), CVSS score 7.5 (Execute code)
*   [CVE-2016-3092](https://www.cvedetails.com/cve/CVE-2016-3092/), CVSS score 7.8 (Denial of Service)

There is a newer version of this library, but there is no newer version of Tomahawk 2.0 which uses it.

### cdi-api 2.0.SP1 released 2019-07-19 (direct dependency)
*   [CVE-2014-8122](https://www.cvedetails.com/cve/CVE-2014-8122/), CVSS score 4.3 (Obtain information)

This is the latest version of this library as of 2020-11-02
