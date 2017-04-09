export MAVEN_OPTS="-Xmx2g -XX:ReservedCodeCacheSize=512m"
./build/mvn -Pyarn -Phadoop-2.7 -Dhadoop.version=2.7.3 -Denforcer.skip=true -DskipTests clean package
./dev/make-distribution.sh --name custom-spark --tgz -Psparkr -Phadoop-2.7 -Phive -Phive-thriftserver -Denforcer.skip=true -Pmesos -Pyarn

