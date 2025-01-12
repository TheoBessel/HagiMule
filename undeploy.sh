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

# Stop Clients
for device in "${devices[@]}"; do
    echo "Stopping Client" $device "..."
    tmux new-session -d -s $device-unsession "
        ssh -o StrictHostKeyChecking=no tbl3216@$device '
            killall nohup;
            killall java;
            rm -rf /work/HagiMule;
            rm /work/hagimule_logs.txt
        '&> ~/hagimule_logs_$device.txt&
    ";
    sleep 0.1;
done

# Stop Diary
echo "Stopping Diary ..."
tmux new-session -d -s iode-unsession "
    ssh -o StrictHostKeyChecking=no tbl3216@iode.enseeiht.fr '
        killall nohup;
        killall java;
        rm -rf ~/HagiMule;
        rm ~/hagimule_logs.txt
    '&> ~/hagimule_logs.txt&
";

sleep 0.2;

pkill -f tmux