$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
java -classpath "$scriptDir\.mvn\wrapper\maven-wrapper.jar" "-Dmaven.multiModuleProjectDirectory=$scriptDir" org.apache.maven.wrapper.MavenWrapperMain $args
