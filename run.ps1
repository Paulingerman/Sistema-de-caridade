$env:JAVA_HOME = "$PSScriptRoot\jdk-21.0.10+7"
$env:Path = "$env:JAVA_HOME\bin;" + (($env:Path -split ';' | Where-Object { $_ -notmatch 'Java|jdk|jre' }) -join ';')
where.exe java
java -version
.\mvnw.cmd -version
.\mvnw.cmd clean spring-boot:run
