devices=(
    "acdc"
    "aerosmith"
    "beatles"
    "clapton"
    "clash"
    "cooper"
    "deeppurple"
    "doors"
    "dylan"
    "eagles"
)

# Setup Diary
echo "Launching Diary  iode.enseeiht.fr ..."
tmux new-session -d -s iode-session "
    ssh -o StrictHostKeyChecking=no $USER_ID@iode.enseeiht.fr '
        git clone https://github.com/TheoBessel/HagiMule.git --branch main ~/HagiMule;
        cd ~/HagiMule;
        curl https://upload.wikimedia.org/wikipedia/commons/a/aa/%22Das_Lagerregal_Gottes%22_-_Menger_Mod_1_OpenCL_12K_HQ_20200517.png -o downloads/test_image.png;
        ./gradlew jar;
        export RMI_PORT=5021;
        java -jar Diary/build/libs/Diary.jar >> ~/hagimule_logs.txt
    '&> ~/hagimule_logs.txt&
";

sleep 60;

# Setup Clients
for device in "${devices[@]}"; do
    echo "Launching Client" $device "..."
    tmux new-session -d -s $device-session "
        ssh -o StrictHostKeyChecking=no $USER_ID@$device.enseeiht.fr '
            cp -r ~/HagiMule /work/HagiMule;
            cd /work/HagiMule;
            export RMI_IP=iode.enseeiht.fr;
            export RMI_PORT=5021;
            export TCP_PORT=5022;
            java -jar Daemon/build/libs/Daemon.jar&> /work/HagiMule/hagimule_logs.txt&
            sleep 2;
            cp /work/test_image.png /work/HagiMule/downloads/test_image.png;
        '&> ~/hagimule_logs_$device.txt&
    ";
    sleep 0.1;
done

sleep 60;

echo "-----------------------"

# Test with downloader
./gradlew jar
export RMI_IP=iode.enseeiht.fr
export RMI_PORT=5021
export TCP_PORT=5022
java -jar Downloader/build/libs/Downloader.jar test_image.png
diff downloads/test_image.png ~/test.png && echo "DIFF OK" || echo "DIFF KO"