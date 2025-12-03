@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script, version 3.3.2
@REM ----------------------------------------------------------------------------

@IF "%__MVNW_ARG0_NAME__%"=="" (SET __MVNW_ARG0_NAME__=%~nx0)
@SET __MVNW_CMD__=
@SET __MVNW_ERROR__=
@SET __MVNW_PSMODULEP_SAVE__=%PSModulePath%
@SET PSModulePath=
@FOR /F "usebackq tokens=1* delims==" %%A IN (`powershell -noprofile "& {$scriptDir='%~dp0telerikLicenses'; $env:PSModulePath='%__MVNW_PSMODULEP_SAVE__%'; exit 0}"`) DO @(
  IF "%%A"=="MVNW_USERNAME" SET MVNW_USERNAME=%%B
  IF "%%A"=="MVNW_PASSWORD" SET MVNW_PASSWORD=%%B
)
@SET PSModulePath=%__MVNW_PSMODULEP_SAVE__%
@SET __MVNW_PSMODULEP_SAVE__=

@SETLOCAL
@SET JAVA_EXE=java.exe
@SET WRAPPER_JAR="%~dp0.mvn\wrapper\maven-wrapper.jar"

@IF NOT EXIST %WRAPPER_JAR% (
    echo Maven wrapper JAR not found. Downloading...
    powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar' -OutFile '%~dp0.mvn\wrapper\maven-wrapper.jar'}"
)

@SET WRAPPER_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"

@FOR /F "usebackq delims=" %%A IN (`powershell -noprofile -command "& {$pwd='%~dp0';$wrapperJar=$pwd+'.mvn\wrapper\maven-wrapper.jar';$wrapperUrl='%WRAPPER_URL%';$javaExe='%JAVA_EXE%';if(!(Test-Path $wrapperJar)){$parent=Split-Path $wrapperJar -Parent;if(!(Test-Path $parent)){New-Item -ItemType Directory -Path $parent -Force|Out-Null};[Net.ServicePointManager]::SecurityProtocol=[Net.SecurityProtocolType]::Tls12;Invoke-WebRequest -Uri $wrapperUrl -OutFile $wrapperJar};echo $wrapperJar}"`) DO @SET WRAPPER_JAR=%%A

@IF NOT EXIST "%WRAPPER_JAR%" (
    echo Error: Could not download Maven wrapper JAR
    exit /b 1
)

%JAVA_EXE% ^
  %MAVEN_OPTS% ^
  -classpath "%WRAPPER_JAR%" ^
  org.apache.maven.wrapper.MavenWrapperMain ^
  %*
