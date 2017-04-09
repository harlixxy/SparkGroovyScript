#/bin/bash
cp spark-2.1.0-bin-custom-spark.tgz /usr/local/bigdata/
cd /usr/local/bigdata/
tar -zxvf spark-2.1.0-bin-custom-spark.tgz
cp /usr/local/bigdata/confdir/* /usr/local/bigdata/spark-2.1.0-bin-custom-spark/conf/
./spark-2.1.0-bin-custom-spark/sbin/start-all.sh
./spark-2.1.0-bin-custom-spark/bin/spark-shell
