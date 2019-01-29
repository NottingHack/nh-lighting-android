package vitalinstinct.nh_lights;


import android.os.Handler;
import android.support.v7.widget.RecyclerView;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by Reece on 12/12/2018, vitalinstinct.nh_lights, nhLights
 */
public class ProcessHandler {

    JSONProcessor create;
    NetworkControl netCon;
    RecyclerView.Adapter rv_dashboard_lights_adaptor;
    MainActivity parent;
    String room;

    public ArrayList<Room> rooms = new ArrayList<>();
    public ArrayList<Pattern> patterns = new ArrayList<>();

    public ArrayList<Pattern> getPatterns() {
        return patterns;
    }

    public void setPatterns(ArrayList<Pattern> patterns) {
        this.patterns = patterns;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }


    public ProcessHandler(String room, RecyclerView.Adapter rv_dashboard_lights_adaptor, MainActivity parent) throws URISyntaxException {
        create = new JSONProcessor(this);
        netCon = new NetworkControl(this);

        this.rv_dashboard_lights_adaptor = rv_dashboard_lights_adaptor;
        this.parent = parent;
        this.room = room;
    }

    public void updateDashboardLights()
    {
        //rv_dashboard_lights_adaptor.notifyDataSetChanged();
        parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                parent.rv_dashboard_lights_adaptor.notifyDataSetChanged();
            }
        });
        checkLights();
    }

    public void checkLights()
    {
        boolean lightOn = false;
        for (int i=0; i< rooms.size(); i++)
        {
            if (rooms.get(i).getName().equals(room)) {
                for (int ii=0; ii< rooms.get(i).getLights().size(); ii++) {
                    if (rooms.get(i).getLights().get(ii).getSingleState().equals("ON"))
                    {
                        lightOn = true;
                    }
                }
            }
        }

        if (lightOn == true)
        {
            parent.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    parent.createControls(true);
                }
            });
        }
        if (lightOn == false)
        {
            parent.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    parent.createControls(false);
                }
            });
        }
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

        if (type.equals("pattern"))
        {
            netCon.sendWebMessage(create.jsonConstructPatternCommand(id));
        }
    }

    public void loadData(ArrayList rooms)
    {
        this.rooms = rooms;
        checkLights();
    }

    public void updateAllLights(boolean setting, String room)
    {
        if (setting == true)
        {
            for (int i=0; i< rooms.size(); i++)
            {
                if (rooms.get(i).getName().equals(room))
                {
                    for (int ii=0; ii< rooms.get(i).getLights().size(); ii++)
                    {
                        netCon.sendWebMessage(create.jsonConstructLightCommand(newLight("null", rooms.get(i).getLights().get(ii).getId(), "ON", room), 0));
                    }
                }
            }
        }
        if (setting == false)
        {
            for (int i=0; i< rooms.size(); i++)
            {
                if (rooms.get(i).getName().equals(room))
                {
                    for (int ii=0; ii< rooms.get(i).getLights().size(); ii++)
                    {
                        netCon.sendWebMessage(create.jsonConstructLightCommand(newLight("null", rooms.get(i).getLights().get(ii).getId(), "OFF", room), 0));
                    }
                }
            }
        }
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

    public void testing()
    {
        netCon.sendWebMessage(new JSONObject());
    }





}
