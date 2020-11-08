export JAVA_HOME="${SCRIPTPATH=$(dirname \"$SCRIPT\")}/jdk-15.0.1"
export export PATH=$JAVA_HOME/bin:$PATH
java Main $1 128