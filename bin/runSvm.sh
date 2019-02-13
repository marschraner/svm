if [ -n "$1" ]; then
  if [ $1 = "-h" ]; then
    echo "Usage: ./runSvm.sh [-h | path to java]"
    echo "If no parameter is provided, the default Java will be used (`which java`)."
    echo "To use another Java specify the path as the first parameter (/bin/java will be added to the path)."
    echo "Show Usage: ./runSvm.sh -h"
    echo "Example to use default Java: ./runSvm.sh"
    echo "Example to use path to Java: ./runSvm.sh /usr/lib/jvm/java-8-openjdk-amd64"
    exit
  else
    JAVA_CMD="$1/bin/java"
  fi
else
  JAVA_CMD=`which java`
fi
echo "Java command used: ${JAVA_CMD}"
echo "Java version used:"
$JAVA_CMD -version

$JAVA_CMD -jar svm-${version}-jar-with-dependencies.jar
