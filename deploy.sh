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

    "albator.enseeiht.fr"
    "bouba.enseeiht.fr"
    "calimero.enseeiht.fr"
    "candy.enseeiht.fr"
    "casimir.enseeiht.fr"
    "clementine.enseeiht.fr"
    "diabolo.enseeiht.fr"
    "esteban.enseeiht.fr"
    "goldorak.enseeiht.fr"
    "heidi.enseeiht.fr"
    "ladyoscar.enseeiht.fr"
    "maya.enseeiht.fr"
    "scoubidou.enseeiht.fr"
    "snorki.enseeiht.fr"
    "tao.enseeiht.fr"

    "apollinaire.enseeiht.fr"
    "baudelaire.enseeiht.fr"
    "brassens.enseeiht.fr"
    "demusset.enseeiht.fr"
    "ferre.enseeiht.fr"
    "gautier.enseeiht.fr"
    "hugo.enseeiht.fr"
    "lafontaine.enseeiht.fr"
    "lamartine.enseeiht.fr"
    "mallarme.enseeiht.fr"
    "maupassant.enseeiht.fr"
    "poe.enseeiht.fr"
    "prevert.enseeiht.fr"
    "rimbaud.enseeiht.fr"
    "sand.enseeiht.fr"
    "verlaine.enseeiht.fr"
)

# Setup Diary
echo "Launching Diary  iode.enseeiht.fr ..."
tmux new-session -d -s iode-session "
    ssh -o StrictHostKeyChecking=no tbl3216@iode.enseeiht.fr '
        git clone https://github.com/TheoBessel/HagiMule.git --branch wip ~/HagiMule;
        cd ~/HagiMule;
        ./gradlew jar;
        export PORT=5021;
        java -jar Diary/build/libs/Diary.jar >> ~/hagimule_logs.txt
    '&> logs/hagimule_logs.txt&
";

sleep 15;

# Setup Clients
for device in "${devices[@]}"; do
    echo "Launching Client" $device "..."
    tmux new-session -d -s $device-session "
        ssh -o StrictHostKeyChecking=no tbl3216@$device '
            cp -r ~/HagiMule /work/HagiMule;
            cd /work/HagiMule;
            export IP=iode.enseeiht.fr;
            export PORT=5021;
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
export IP=iode.enseeiht.fr
export PORT=5021
java -jar Downloader/build/libs/Downloader.jar test3.ml