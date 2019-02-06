package vitalinstinct.nh_lights;

import android.os.AsyncTask;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MQTT {

    String TOPIC_STATUS = "nh/status/req";
    String TOPIC_TEMP = "nh/temp";
    String TOPIC_GK_ENTRY = "nh/gk/entry_announce/known";
    Temp temp;

    MainActivity parent;
    MqttAsyncClient client;

    public MQTT(MainActivity activity)
    {
        this.parent = activity;
        temp = new Temp();
    }

    public void connect()
    {
        try {
            client = new MqttAsyncClient("tcp://jarvis:1883", "HS-FIRETABS", new MemoryPersistence());
            client.setCallback(new MqttCallbackExtended() {

                @Override
                public void connectComplete(boolean b, String s) {
                    System.out.println(s);
                }

                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println(cause.toString());
                }

                @Override
                public void messageArrived(String topic, final MqttMessage message) throws Exception {
                    System.out.println(message.toString());

                    if (message.toString().equals("STATUS")) {
                        MqttMessage item = new MqttMessage("Running: HS-FIRETABS".getBytes());
                        message.setQos(0);
                        message.setRetained(false);
                        client.publish("nh/status/res", item);
                    }

                    if (topic.contains("temperature"))
                    {
                        for (int i=0; i< temp.getTEMPS().size(); i++)
                        {
                            if (topic.contains(temp.getTEMPS().get(i)[0]))
                            {
                                temp.getTEMPS().get(i)[1] = message.toString();
                                parent.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        parent.updateTemps(temp);
                                    }
                                });
                            }
                        }
                    }

                    if (topic.contains("announce"))
                    {
                        parent.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                parent.updateNotification("announce", message.toString());
                            }
                        });
                    }

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
            client.connect();

            Thread.sleep(1000);

            subscribe(client, TOPIC_STATUS);
           // subscribe(client, TOPIC_TEMP);
            subscribe(client, TOPIC_GK_ENTRY);
            subscribe(client, "nh/temperature/" + temp.getTEMP_BLUE_ROOM());
            subscribe(client, "nh/temperature/" + temp.getTEMP_COMFY_AREA());
            subscribe(client, "nh/temperature/" + temp.getTEMP_KITCHEN());
            subscribe(client, "nh/temperature/" + temp.getTEMP_VENDING_MACHINES());
            subscribe(client, "nh/temperature/" + temp.getTEMP_WORKSHOP());
            subscribe(client, "nh/temperature/" + temp.getTEMP_CRAFT_ROOM());
            subscribe(client, "nh/temperature/" + temp.getTEMP_STUDIO());

        } catch (MqttException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(MqttAsyncClient client , String topic)
    {
        try {
            client.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("Mqtt" + "Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("Mqtt" + "Subscribed fail!");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }



}
