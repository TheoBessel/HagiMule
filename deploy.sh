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

    "albator"
    "bouba"
    "calimero"
    "candy"
    "casimir"
    "clementine"
    "diabolo"
    "esteban"
    "goldorak"
    "heidi"
    "ladyoscar"
    "maya"
    "scoubidou"
    "snorki"
    "tao"

    "apollinaire"
    "baudelaire"
    "brassens"
    "demusset"
    "ferre"
    "gautier"
    "hugo"
    "lafontaine"
    "lamartine"
    "mallarme"
    "maupassant"
    "poe"
    "prevert"
    "rimbaud"
    "sand"
    "verlaine"
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
    '&> logs/hagimule_logs.txt&
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
            java -jar Daemon/build/libs/Daemon.jar&
            sleep 2;
            cp /work/HagiMule/downloads/test1.ml /work/HagiMule/downloads/test3.ml
        '&> logs/hagimule_logs_$device.txt&
    ";
    sleep 0.1;
done

sleep 3;

echo "-----------------------"

# Test with downloader
./gradlew jar
export RMI_IP=iode.enseeiht.fr
export RMI_PORT=5021
export TCP_PORT=5021
java -jar Downloader/build/libs/Downloader.jar test3.ml