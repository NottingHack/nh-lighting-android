package vitalinstinct.nh_lights;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Reece on 12/12/2018, vitalinstinct.nh_lights, nhLights
 */
public class JSONProcessor {

    ProcessHandler handler;
    Room room;
    ArrayList<Room> rooms = new ArrayList<>();
    ArrayList<Integer> room_lights;
    ArrayList<Light> light_list = new ArrayList<>();

    public JSONProcessor(ProcessHandler handler)
    {
        this.handler = handler;
    }


    //not implemented
    public JSONObject jsonConstructInitialCommand(String token)
    {
        JSONObject command = new JSONObject();
        try {
            command.put("eventType", "ConnectRequest");
            command.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return command;
    }

    public void jsonDecodeResponseObject(JSONObject object) throws JSONException {

        if (object.getString("eventType").equals("lightsLoaded")) {
            handler.loadData(rooms);
            // TODO: PATTERNS ???????
        }
    }

    public void jsonDecodeResponse(JSONArray array) throws JSONException {
        for (int i=0; i< array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            if (object.getString("eventType").equals("RoomDescription")) {
                room = new Room();
                room.setLights(new ArrayList<Light>());
                room.setPatterns(new ArrayList<Pattern>());

                room.setName(object.getString("room"));
                JSONArray json_lights = object.getJSONArray("lights");

                room_lights = new ArrayList<>();
                for (int ii = 0; ii < json_lights.length(); ii++) {
                    room_lights.add(json_lights.getInt(ii));
                }

                handler.getLights(room.getName(), room_lights);

                boolean newRoom = true;
                for (int iii=0; iii< rooms.size(); iii++)
                {
                    if (rooms.get(iii).getName().equals(room.getName()))
                    {
                        rooms.set(iii, room);
                        newRoom = false;
                    }
                }
                if (newRoom == true)
                {
                    rooms.add(room);
                }
            }

            if (object.getString("eventType").equals("LightState")) {
                Light light = new Light();

                light.setRoom(object.getString("room"));
                light.setId(object.getInt("light"));
                light.setEventType("LightRequest");

                for (int ii=0; ii< rooms.size(); ii++)
                {
                    boolean newLight = true;
                    if (light.getRoom().equals(rooms.get(ii).getName()))
                    {
                        for (int iii=0; iii< rooms.get(ii).getLights().size(); iii++) {
                            if (rooms.get(ii).getLights().get(iii).getId() == light.getId())
                            {
                                rooms.get(ii).getLights().set(iii, light);
                                newLight = false;
                            }
                        }
                        if (newLight == true) {
                            rooms.get(ii).getLights().add(light);
                        }
                    }
                }

                handler.loadData(rooms);
            }
        }
    }

    public void jsonConstructCheckState(Light light)
    {

    }

    public JSONObject jsonConstructLightCommand(Light light, int stateId)
    {
        JSONObject command = new JSONObject();
        try {
            command.put("eventType", light.getEventType());
            command.put("room", light.getRoom());
            command.put("light", light.getId());
            command.put("state", "TOGGLE"); // toggle requiring to be used?
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return command;
    }

    public JSONObject jsonConstructRoomCommand(String room)
    {
        JSONObject command = new JSONObject();
        try {
            command.put("eventType", "RoomDescription");
            command.put("room", room);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return command;
    }
}
