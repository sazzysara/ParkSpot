@echo off
set "WRAPPER_DIR=%~dp0"
if "%WRAPPER_DIR:~-1%"=="\" set "WRAPPER_DIR=%WRAPPER_DIR:~0,-1%"
java -classpath "%WRAPPER_DIR%\.mvn\wrapper\maven-wrapper.jar" "-Dmaven.multiModuleProjectDirectory=%WRAPPER_DIR%" org.apache.maven.wrapper.MavenWrapperMain %*
