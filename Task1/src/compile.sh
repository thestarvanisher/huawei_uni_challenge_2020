wget "https://www.joewillmott.co.uk/jdk-15.0.1_linux-aarch64_bin.tar.gz"
tar -zxf jdk-15.0.1_linux-aarch64_bin.tar.gz
export JAVA_HOME="${SCRIPTPATH=$(dirname \"$SCRIPT\")}/jdk-15.0.1"
export export PATH=$JAVA_HOME/bin:$PATH
javac Main.java
