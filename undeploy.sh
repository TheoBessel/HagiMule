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

# Stop Clients
for device in "${devices[@]}"; do
    echo "Stopping Client" $device "..."
    tmux new-session -d -s $device-unsession "
        ssh tbl3216@$device '
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
    ssh tbl3216@iode.enseeiht.fr '
        killall nohup;
        killall java;
        rm -rf ~/HagiMule;
        rm ~/hagimule_logs.txt
    '&> ~/hagimule_logs.txt&
";

sleep 0.1;

pkill -f tmux