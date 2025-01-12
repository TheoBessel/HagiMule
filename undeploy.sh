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

# Stop Clients
for device in "${devices[@]}"; do
    echo "Stopping Client" $device "..."
    tmux new-session -d -s $device-unsession "
        ssh -o StrictHostKeyChecking=no tbl3216@$device.enseeiht.fr '
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