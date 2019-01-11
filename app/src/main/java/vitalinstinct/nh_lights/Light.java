package vitalinstinct.nh_lights;

/**
 * Created by Reece on 12/12/2018, vitalinstinct.nh_lights, nhLights
 */
public class Light {


    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getState() {
        return state;
    }

    public void setState(String[] state) {
        this.state = state;
    }

    public String getSingleState() {
        return singleState;
    }

    public void setSingleState(String singleState) {
        this.singleState = singleState;
    }

    private String eventType, room, singleState;
    private int id;
    private String[] state = new String[]{"ON", "OFF", "TOGGLE"};
}
