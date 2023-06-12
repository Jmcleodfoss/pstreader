# pstExtractor

A JSF-based server for uploading all parts of a PST file except the messages (you can see contacts, tasks, and calendar entries). Note that, as it uses CDI, it needs an EE-capable servlet to run. Tomcat is no longer supported, but Tomcat EE is.

## Variants
### Basic
The basic version of this application does not include an implementation of Java Server Faces; it needs to be deployed to a full-fledged Java EE server like TomEE.

### MyFaces
This application can be built to provide Java Server Faces support using Apache MyFaces using the **myfaces** profile. This will create a war file with the classifier "myfaces".
    mvn clean install package -P myfaces

### Mojarra
This application can be built to provide Java Server Faces support using Eclipse Mojarra using the **mojarra** profile. This will create a war file with the classifier "mojarra".
    mvn clean install package -P mojarra

#### Mojarra, Weld, and Jetty
The Eclipse Foundation's recommendation for Mojarra is to include the [weld-servlet-shaded](https://mvnrepository.com/artifact/org.jboss.weld.servlet/weld-servlet-shaded) library. This triggers warnings about classes being imported from multiple places when running Jetty via the jetty-maven-plugin. To eliminate these warnings, you can use a subset of weld servlet libraries with `-Dweld.library=jatty`.

## TomcatEE
This has been tested on Tomcat EE 8. To deploy this on a locally running version of Tomcat, ensure that there is a user with the role _manager-script_ configured in your tomcat-users.xml file. In my case, the user with this role is _script_, and the password is _admin_:
```
    <tomcat-users>
	<role rolename="manager-script"/>
	<user username="script" password="admin" roles="manager-script"/>
    </tomcat-users>
```

Add the following section to the <servers> element of your maven settings.xml:
```
    <!-- Local Tomcat server -->
    <server>
      <id>LocalTomcatServer</id>
      <username>script</username>
      <password>admin</password>
    </server>
```

You may now deploy/undeploy/redeploy the app via
- `mvn tomcat7:deploy`
- `mvn tomcat7:undeploy`
- `mvn tomcat7:redeploy`

### Jetty
This has been tested on Jetty 9 using the jetty-maven-plugin. To run on Jetty:
`mvn jetty:run -P jetty -Dweld.library=jetty`

### Troubleshooting
If you find the server is low on memory, add the following line to the file <tomcat-home>/bin/catalina.sh
```
    # Required for pst file processing
    JAVA_OPTS="$JAVA_OPTS -Xmx1024m"
```
THe upload limit defaults to 500 MB. If you need to adjust this, change the following entries in [src/main/webapp/WEB_INF/web.xml](src/main/webapp/WEB_INF/web.xml):

### web-app/filter/filter-name/init-params/param-value
```
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
```

### web-app/multipart-config
```
    <multipart-config>
      <!-- Set server upload size to match MyFacesExtensionsFilter -->
      <max-file-size>52428800</max-file-size>
      <max-request-size>52428800</max-request-size>
      <file-size-threshold>0</file-size-threshold>
    </multipart-config>
```
