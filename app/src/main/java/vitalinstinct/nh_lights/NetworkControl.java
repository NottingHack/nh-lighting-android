package vitalinstinct.nh_lights;


import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.extensions.IExtension;
import org.java_websocket.protocols.IProtocol;
import org.java_websocket.protocols.Protocol;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Reece on 12/12/2018, vitalinstinct.nh_lights, nhLights
 */
public class NetworkControl {

    WebSocket cn;
    JSONProcessor json;
    ProcessHandler handler;

    JSONObject connectCommand;

    public NetworkControl(ProcessHandler handler) throws URISyntaxException {
        this.handler = handler;
        json = new JSONProcessor(handler);

        // This draft only allows you to use the specific Sec-WebSocket-Protocol without a fallback.
        Draft_6455 draft_ocppOnly = new Draft_6455(Collections.<IExtension>emptyList(), Collections.<IProtocol>singletonList(new Protocol("lighting")));

        // This draft allows the specific Sec-WebSocket-Protocol and also provides a fallback, if the other endpoint does not accept the specific Sec-WebSocket-Protocol
        ArrayList<IProtocol> protocols = new ArrayList<IProtocol>();
        protocols.add(new Protocol("lighting"));
        Draft_6455 draft_ocppAndFallBack = new Draft_6455(Collections.<IExtension>emptyList(), protocols);


        // delay required ?
        cn = new WebSocket(new URI("ws://queen:8181/lighting"), handler, draft_ocppOnly);
        cn.connect();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        connectCommand = json.jsonConstructInitialCommand("{token}");
        cn.send(connectCommand.toString());

    }

    public void sendWebMessage(JSONObject object)
    {
        cn.send(object.toString());
    }

    public void loadData(String room)
    {
        getRoomData(room);
    }

    public void getLightStatus(int id)
    {
        cn.send(""); // work out command
    }

    public void getLights(String room, ArrayList<Integer> room_lights)
    {

        // resend connect request to get status?
        cn.send(connectCommand.toString());

        JSONObject obj = new JSONObject();
        try {
            obj.put("eventType", "lightsLoaded");
            json.jsonDecodeResponseObject(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getRoomData(String room)
    {
        JSONObject obj = json.jsonConstructRoomCommand("Blue room");
        System.out.println(obj.toString());
        //cn.send(obj.toString()); // work out command
    }

}
