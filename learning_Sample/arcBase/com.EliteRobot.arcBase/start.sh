
echo 'start elibot'
#kill
kill -9 $(ps -elf | grep felix | grep -v grep | head -n 1 | awk '{print $4}')

#install
mvn install -Pdeploy_local

#run
/home/elite/Desktop/CS_SIMULATOR_v2.3.0-test/run_elisim.sh
