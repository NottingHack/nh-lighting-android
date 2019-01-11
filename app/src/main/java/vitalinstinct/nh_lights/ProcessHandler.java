package vitalinstinct.nh_lights;


import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by Reece on 12/12/2018, vitalinstinct.nh_lights, nhLights
 */
public class ProcessHandler {

    JSONProcessor create;
    NetworkControl netCon;

    public ArrayList<Room> rooms = new ArrayList<>();


    public ProcessHandler(String room) throws URISyntaxException {
        create = new JSONProcessor(this);
        netCon = new NetworkControl(this);

    }

    public void handleChangeCommand(String type, int id, boolean state, String room)
    {
        if (type.equals("button"))
        {

            // TOGGLE or on and off?????
            if (id == 0)
            {
                netCon.sendWebMessage(create.jsonConstructLightCommand(newLight(type, id, "TOGGLE", room), 0));
            }
            else
            {
                netCon.sendWebMessage(create.jsonConstructLightCommand(newLight(type, id, "TOGGLE", room), 0));
            }
        }

    }

    public void getRoomData(String room)
    {
        netCon.getRoomData(room);
    }

    public void loadData(ArrayList rooms)
    {
        this.rooms = rooms;
    }

    public void updateStatus()
    {

    }

    public void getLights(String room, ArrayList<Integer> room_lights)
    {
        netCon.getLights(room, room_lights);
    }

    public ArrayList<Light> getLightData(String room)
    {
        for (int i=0; i< rooms.size(); i++) {
            if (room.equals(rooms.get(i).getName())) {
                return rooms.get(i).getLights();
            }
        }
        return null;
    }

    public Light newLight(String type, int id, String state, String room) //private in release
    {
        Light light = new Light();
        light.setEventType("LightRequest");
        light.setId(id);
        light.setRoom(room);
        light.setSingleState(state);
        return light;
    }




}
