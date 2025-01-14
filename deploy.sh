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
    "epica"
    "hendrix"
)

# Setup Diary
echo "Launching Diary  iode.enseeiht.fr ..."
tmux new-session -d -s iode-session "
    ssh -o StrictHostKeyChecking=no tbl3216@iode.enseeiht.fr '
        git clone https://github.com/TheoBessel/HagiMule.git --branch wip ~/HagiMule;
        cd ~/HagiMule;
        ./gradlew jar;
        export RMI_PORT=5021;
        java -jar Diary/build/libs/Diary.jar >> ~/hagimule_logs.txt
    '&> ~/hagimule_logs.txt&
";

sleep 15;

# Setup Clients
for device in "${devices[@]}"; do
    echo "Launching Client" $device "..."
    tmux new-session -d -s $device-session "
        ssh -o StrictHostKeyChecking=no tbl3216@$device.enseeiht.fr '
            cp -r ~/HagiMule /work/HagiMule;
            cd /work/HagiMule;
            export RMI_IP=iode.enseeiht.fr;
            export RMI_PORT=5021;
            export TCP_PORT=5022;
            java -jar Daemon/build/libs/Daemon.jar&> /work/HagiMule/hagimule_logs.txt&
            sleep 2;
            cp /work/test.mp4 /work/HagiMule/downloads/test.mp4;
        '&> ~/hagimule_logs_$device.txt&
    ";
    #/home/tbl3216/.local/bin/yt-dlp -f "642" -o /work/test.mp4 https://youtu.be/njX2bu-_Vw4?si=p3JRR_I6KgbKSxh8
    sleep 0.1;
done

sleep 20;

echo "-----------------------"

# Test with downloader
./gradlew jar
export RMI_IP=iode.enseeiht.fr
export RMI_PORT=5021
export TCP_PORT=5022
java -jar Downloader/build/libs/Downloader.jar test.mp4