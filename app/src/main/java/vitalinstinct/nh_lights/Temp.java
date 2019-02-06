package vitalinstinct.nh_lights;

import java.util.ArrayList;

public class Temp {

    private String TEMP_COMFY_AREA = "ComfyArea";
    private String TEMP_CRAFT_ROOM = "CraftRoom";
    private String TEMP_BLUE_ROOM = "G5-BlueRoom";
    private String TEMP_KITCHEN = "Kitchen";
    private String TEMP_STUDIO = "Studio";
    private String TEMP_VENDING_MACHINES = "VendingMachine";
    private String TEMP_WORKSHOP = "Workshop";

    public ArrayList<String[]> TEMPS;

    public Temp()
    {
        TEMPS = new ArrayList<>();
        TEMPS.add(new String[]{TEMP_COMFY_AREA, "0"});
        TEMPS.add(new String[]{TEMP_CRAFT_ROOM, "0"});
        TEMPS.add(new String[]{TEMP_BLUE_ROOM, "0"});
        TEMPS.add(new String[]{TEMP_KITCHEN, "0"});
        TEMPS.add(new String[]{TEMP_STUDIO, "0"});
        TEMPS.add(new String[]{TEMP_VENDING_MACHINES, "0"});
        TEMPS.add(new String[]{TEMP_WORKSHOP, "0"});

    }

    public void setTEMP_CRAFT_ROOM(String TEMP_CRAFT_ROOM) {
        this.TEMP_CRAFT_ROOM = TEMP_CRAFT_ROOM;
    }

    public ArrayList<String[]> getTEMPS() {
        return TEMPS;
    }

    public void setTEMPS(ArrayList<String[]> TEMPS) {
        this.TEMPS = TEMPS;
    }


    public String getTEMP_COMFY_AREA() {
        return TEMP_COMFY_AREA;
    }

    public void setTEMP_COMFY_AREA(String TEMP_COMFY_AREA) {
        this.TEMP_COMFY_AREA = TEMP_COMFY_AREA;
    }

    public String getTEMP_CRAFT_ROOM() {
        return TEMP_CRAFT_ROOM;
    }

    public void setTEMPT_CRAFT_ROOM(String TEMPT_CRAFT_ROOM) {
        this.TEMP_CRAFT_ROOM = TEMPT_CRAFT_ROOM;
    }

    public String getTEMP_BLUE_ROOM() {
        return TEMP_BLUE_ROOM;
    }

    public void setTEMP_BLUE_ROOM(String TEMP_BLUE_ROOM) {
        this.TEMP_BLUE_ROOM = TEMP_BLUE_ROOM;
    }

    public String getTEMP_KITCHEN() {
        return TEMP_KITCHEN;
    }

    public void setTEMP_KITCHEN(String TEMP_KITCHEN) {
        this.TEMP_KITCHEN = TEMP_KITCHEN;
    }

    public String getTEMP_STUDIO() {
        return TEMP_STUDIO;
    }

    public void setTEMP_STUDIO(String TMEP_STUDIO) {
        this.TEMP_STUDIO = TMEP_STUDIO;
    }

    public String getTEMP_VENDING_MACHINES() {
        return TEMP_VENDING_MACHINES;
    }

    public void setTEMP_VENDING_MACHINES(String TEMP_VENDING_MACHINES) {
        this.TEMP_VENDING_MACHINES = TEMP_VENDING_MACHINES;
    }

    public String getTEMP_WORKSHOP() {
        return TEMP_WORKSHOP;
    }

    public void setTEMP_WORKSHOP(String TEMP_WORKSHOP) {
        this.TEMP_WORKSHOP = TEMP_WORKSHOP;
    }


}
