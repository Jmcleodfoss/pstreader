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
The Eclipse Foundation's recommendation for Mojarra is to include the [weld-servlet-shaded](https://mvnrepository.com/artifact/org.jboss.weld.servlet/weld-servlet-shaded) library. This triggers warnings about classes being imported from multiple places when running Jetty via the jetty-maven-plugin. To eliminate these warnings, you can use a subset of weld servlet libraries with `-Dweld.library=jetty`.

## Details of execution under various containers
### TomcatEE
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

### Tomcat
This has been tested on Tomcat 9. To run on Tomcat 9 using Mojarra:
`mvn tomcat7:deploy -P mojarra`
To run on Tomcat 9 using MyFaces, pull in an extra required library using the environment variable:
`mvn tomcat7:deploy -P mojarra -D environment=myfaces-tomcat`

### Jetty
This has been tested on Jetty 9 using the jetty-maven-plugin. To run on Jetty using Mojarra:
`mvn jetty:run -P mojarra`
To run on Jetty using MyFaces:
`mvn jetty:run -P myfaces'

You can then load the app by browsing to http://localhost:8080

### Troubleshooting
If you find the server is low on memory, add the following line to the file <tomcat-home>/bin/catalina.sh
```
    # Required for pst file processing
    JAVA_OPTS="$JAVA_OPTS -Xmx1024m"
```
