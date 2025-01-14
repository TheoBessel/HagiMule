package AliveClientsService;

import java.rmi.RemoteException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Device.ClientInfoImpl;
import Diary.Diary;

public class AliveClientsService implements Runnable {

    /**
     * @param d the Diary for which to control alive clients
     */
    private Diary diary;

    public AliveClientsService(Diary d) {
        this.diary = d;
    }

    @Override
    public void run() {
        // Wait for client to connect
        while (true) {
            try {
                Map<String, LocalTime> map = diary.getClientsLastAliveTimes();
                // clients to remove
                List<String> keysToRemove = new ArrayList<>();
                LocalTime t = LocalTime.now();
                map.forEach((key, value) ->
                    {
                        long diff = Duration.between(value, t).toMillis();
                        // System.out.println(key);
                        // System.out.println(diff >= 2);
                        if (diff >= 2000) {
                            // System.out.println("Client deco !!");
                        //     // delete client from list
                        //     // String[] parts = key.split(":");
                        //     // String host = parts[0];
                        //     // String port = parts[1];
                            keysToRemove.add(key);
                        //     // try {
                        //     //     diary.removeClient(new ClientInfoImpl(host, Integer.parseInt(port)));
                        //     // } catch (NumberFormatException e) {
                        //     //     System.err.println("Error unparsable port");
                        //     //     e.printStackTrace();
                        //     // } catch (RemoteException e) {
                        //     //     System.err.println("Error couldn't remove owner");
                        //     //     e.printStackTrace();
                        //     // }
                        }
                    }
                    );
                    for (String key : keysToRemove) {
                    // System.out.println(key);
                    String[] parts = key.split(":");
                    String host = parts[0];
                    String port = parts[1];
                    diary.removeClient(new ClientInfoImpl(host, Integer.parseInt(port)));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        System.err.println(e1);
                    }
                    // System.out.println("yes");
                    // List<ClientInfo> l = this.diary.getFile("test1.ml").getOwners();
                    // for (ClientInfo c : l) {
                    //     System.out.println(c.getAddress());
                    // }
                    // System.out.println("yes2");
                }
                //     String[] parts = key.split(":");
                //     String host = parts[0];
                //     String port = parts[1];
                //     try {
                //         diary.removeClient(new ClientInfoImpl(host, Integer.parseInt(port)));
                //     } catch (NumberFormatException e) {
                //         System.err.println("Error unparsable port");
                //         e.printStackTrace();
                //     } catch (RemoteException e) {
                //         System.err.println("Error couldn't remove owner");
                //         e.printStackTrace();
                //     }

                // }
                // System.out.println("caca");
                // System.out.println(map.size());
                // for (Map.Entry<String, LocalTime> entry : map.entrySet()) {
                    // System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    // if (Duration.between(LocalTime.now(), entry.getValue()).toSeconds().compare(Duration.ofSeconds(0))) {
                    // }
                    // LocalTime t2 = LocalTime.now();
                    // LocalTime t1 = entry.getValue();
                    // long diff = Duration.between(t2, t1).toSeconds();
                    // System.out.println(diff >= 3);



                // }
            } catch (RemoteException e) {
                System.err.println("Error while getting the map");
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                System.err.println(e1);
            }
        }
    }

}
