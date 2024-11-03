# Compiles the java code
# and then creates a jar file versioned according to the version file
# and then runs the jar file.

$manifestPath = "META-INF/MANIFEST.MF"
$versionFilePath = "version.txt"

$jarName = "OthelloTrainer_v" + (Get-Content -Path $versionFilePath -TotalCount 1).substring(9) + "j.jar"
$createJarExpression = "jar cfm output/" + $jarName + " " + $manifestPath + " " + "src/othellotrainer"
$runJarExpression = "java -jar output/" + $jarName

javac src/othellotrainer/*.java
Invoke-Expression $createJarExpression
Invoke-Expression $runJarExpression