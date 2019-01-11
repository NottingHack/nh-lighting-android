package vitalinstinct.nh_lights;

import java.util.ArrayList;

/**
 * Created by Reece on 12/12/2018, vitalinstinct.nh_lights, nhLights
 */
public class Room {


    String name;
    private ArrayList<Light> lights;
    private ArrayList<Pattern> patterns;

    public ArrayList<Light> getLights() {
        return lights;
    }

    public void setLights(ArrayList<Light> lights) {
        this.lights = lights;
    }

    public ArrayList<Pattern> getPatterns() {
        return patterns;
    }

    public void setPatterns(ArrayList<Pattern> patterns) {
        this.patterns = patterns;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
