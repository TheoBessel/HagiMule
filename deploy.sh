devices=(
    "acdc.enseeiht.fr"
    "aerosmith.enseeiht.fr"
    "beatles.enseeiht.fr"
    "clapton.enseeiht.fr"
    "clash.enseeiht.fr"
    "cooper.enseeiht.fr"
    "deeppurple.enseeiht.fr"
    "doors.enseeiht.fr"
    "dylan.enseeiht.fr"
    "eagles.enseeiht.fr"
    "epica.enseeiht.fr"
    "hendrix.enseeiht.fr"
)

#tmux new-session -d -s iode-session "ssh tbl3216@iode -t 'git clone https://github.com/TheoBessel/HagiMule.git --branch wip /work/HagiMule; cd /work/HagiMule; ./gradlew Diary:run'";
#tmux new-session -d -s $device-session "ssh tbl3216@$device 'rm -rf /work/HagiMule; git clone https://github.com/TheoBessel/HagiMule.git --branch wip /work/HagiMule; cd /work/HagiMule; export IP=\"iode.enseeiht.fr\"; nohup ./gradlew Daemon:run >> /work/hagimule_logs.txt'"

for device in "${devices[@]}"; do
    tmux new-session -d -s $device-session "
        ssh tbl3216@$device '
            rm -rf /work/HagiMule;
            git clone https://github.com/TheoBessel/HagiMule.git --branch wip /work/HagiMule;
            cd /work/HagiMule;
            ./gradlew jar;
            export IP=iode.enseeiht.fr;
            nohup java -jar Daemon/build/libs/Daemon.jar >> /work/hagimule_logs.txt
        '
    ";
done


#git clone https://github.com/TheoBessel/HagiMule.git --branch wip /work/HagiMule; cd /work/HagiMule; ./gradlew Diary:run;
#rm -rf /work/HagiMule; git clone https://github.com/TheoBessel/HagiMule.git --branch wip /work/HagiMule; cd /work/HagiMule; export IP=iode.enseeiht.fr; ./gradlew Daemon:run