package Models;

import com.example.ayoub.mapdemo2.R;

/**
 * Created by Ayoub on 6/9/2016.
 */
public class Marker
{

    private String event;
    private EventType eventType;
    private String desc;
    private double lat;
    private double log;


    public Marker(String event, double lat, double log)
    {
        this.event = event;
        this.lat = lat;
        this.log = log;
    }


    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }


    @Override
    public String toString() {
        return "Marker{" +
                "event='" + event + '\'' +
                ", eventType=" + eventType +
                ", lat=" + lat +
                ", log=" + log +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Marker)) return false;

        Marker marker = (Marker) o;

        if (Double.compare(marker.getLat(), getLat()) != 0)
            return false;
        if (Double.compare(marker.getLog(), getLog()) != 0)
            return false;
        if (!getEvent().equals(marker.getEvent()))
            return false;

        return getEventType() == marker.getEventType();

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getEvent().hashCode();
        result = 31 * result + getEventType().hashCode();
        temp = Double.doubleToLongBits(getLat());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLog());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public Marker(String event, EventType eventType, double lat, double log)
    {
        this.event = event;
        this.eventType = eventType;
        this.lat = lat;
        this.log = log;
    }

    public Marker(String event, EventType eventType, String desc, double lat, double log)
    {
        this.event = event;
        this.eventType = eventType;
        this.desc = desc;
        this.lat = lat;
        this.log = log;
    }

    public String getCoordsString()
    {
        double lat = (double) Math.round(getLat()*1000)/1000;
        double log = (double) Math.round(getLog()*1000)/1000;

        return "Lat="+lat + " - Long=" +log;
    }

    public int getIcon()
    {
        if(this.eventType == null)
            return R.drawable.circle;

        switch (eventType)
        {
            case Educationel:
                return R.drawable.educationel;

            case Music:
                return R.drawable.music;

            case Sportif:
                return R.drawable.sport;
        }

        return R.drawable.circle;
    }


}
