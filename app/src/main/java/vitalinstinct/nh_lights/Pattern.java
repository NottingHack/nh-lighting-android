package vitalinstinct.nh_lights;

import java.util.ArrayList;

/**
 * Created by Reece on 12/12/2018, vitalinstinct.nh_lights, nhLights
 */
public class Pattern {


    private int patternId;
    private String eventType;
    private String name;
    private ArrayList<Light> lights = new ArrayList<>();


    public int getPatternId() {
        return patternId;
    }

    public void setPatternId(int patternId) {
        this.patternId = patternId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Light> getLights() {
        return lights;
    }

    public void setLights(ArrayList<Light> lights) {
        this.lights = lights;
    }


}
