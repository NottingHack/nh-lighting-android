package vitalinstinct.nh_lights;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.Map;

/**
 * Created by Reece on 15/12/2018, vitalinstinct.nh_lights, nhLights
 */
public class WebSocket extends WebSocketClient {

    JSONProcessor decode;
    ProcessHandler handler;

    public WebSocket(URI serverUri, ProcessHandler handler, Draft draft) {
        super(serverUri, draft);
        this.handler = handler;
        decode = new JSONProcessor(handler);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("CONNECTED");
    }

    @Override
    public void onMessage(String message) {
        System.out.println( "received: " + message );
        //[{"room":"CNC room","lights":[10,11,12,13,14,15],"eventType":"RoomDescription"},{"room":"Blue room","lights":[1,2,3,4,5,6,7,8,9],"eventType":"RoomDescription"},{"room":"Team storage","lights":[18,19],"eventType":"RoomDescription"},{"room":"Members storage","lights":[],"eventType":"RoomDescription"},{"room":"Class room","lights":[20,21,22,23,24,25,26,27,28,29,30,31],"eventType":"RoomDescription"},{"room":"Metal working","lights":[],"eventType":"RoomDescription"},{"room":"Disabled toilet","lights":[32],"eventType":"RoomDescription"},{"room":"CNC room corridor","lights":[16,17],"eventType":"RoomDescription"},{"room":"Corridor","lights":[],"eventType":"RoomDescription"},{"room":"Airlock","lights":[],"eventType":"RoomDescription"},{"room":"Kitchen","lights":[],"eventType":"RoomDescription"},{"room":"Toilets","lights":[],"eventType":"RoomDescription"},{"room":"Studio","lights":[],"eventType":"RoomDescription"},{"room":"Comfy area","lights":[],"eventType":"RoomDescription"},{"room":"Members storage","lights":[],"eventType":"RoomDescription"},{"room":"Craft room","lights":[],"eventType":"RoomDescription"},{"room":"Electronics","lights":[],"eventType":"RoomDescription"},{"room":"Dusty area","lights":[],"eventType":"RoomDescription"},{"room":"Wood working","lights":[],"eventType":"RoomDescription"},{"room":"Project storage","lights":[],"eventType":"RoomDescription"}]
        //[{"light":1,"state":"ON","room":"Blue room","eventType":"LightState"},{"light":2,"state":"ON","room":"Blue room","eventType":"LightState"},{"light":3,"state":"ON","room":"Blue room","eventType":"LightState"},{"light":4,"state":"ON","room":"Blue room","eventType":"LightState"},{"light":5,"state":"ON","room":"Blue room","eventType":"LightState"},{"light":6,"state":"ON","room":"Blue room","eventType":"LightState"},{"light":7,"state":"ON","room":"Blue room","eventType":"LightState"},{"light":8,"state":"ON","room":"Blue room","eventType":"LightState"},{"light":16,"state":"OFF","room":"CNC room corridor","eventType":"LightState"},{"light":15,"state":"ON","room":"CNC room","eventType":"LightState"},{"light":14,"state":"ON","room":"CNC room","eventType":"LightState"},{"light":13,"state":"ON","room":"CNC room","eventType":"LightState"},{"light":12,"state":"OFF","room":"CNC room","eventType":"LightState"},{"light":11,"state":"OFF","room":"CNC room","eventType":"LightState"},{"light":10,"state":"ON","room":"CNC room","eventType":"LightState"},{"light":9,"state":"ON","room":"Blue room","eventType":"LightState"},{"light":17,"state":"OFF","room":"CNC room corridor","eventType":"LightState"},{"light":18,"state":"ON","room":"Team storage","eventType":"LightState"},{"light":19,"state":"OFF","room":"Team storage","eventType":"LightState"}]
        //
        try {
            decode.jsonDecodeResponse(new JSONArray(message));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) + " Code: " + code + " Reason: " + reason );
    }

    @Override
    public void onError(Exception ex) {

    }
}
