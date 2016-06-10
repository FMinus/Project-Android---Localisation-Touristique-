package Models;

import java.util.Date;

/**
 * Created by Ayoub on 6/9/2016.
 */
public class Point
{
    private String event;
    private double x;
    private double y;
    private String color;
    private Date dateEvent;

    public Point(String event, double x, double y)
    {
        this.event = event;
        this.x = x;
        this.y = y;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Date getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(Date dateEvent) {
        this.dateEvent = dateEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;

        Point point = (Point) o;

        if (Double.compare(point.getX(), getX()) != 0) return false;
        if (Double.compare(point.getY(), getY()) != 0) return false;
        if (!getEvent().equals(point.getEvent())) return false;
        return !(getDateEvent() != null ? !getDateEvent().equals(point.getDateEvent()) : point.getDateEvent() != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getEvent().hashCode();
        temp = Double.doubleToLongBits(getX());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getY());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getDateEvent() != null ? getDateEvent().hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Point{" +
                "event='" + event + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", dateEvent=" + dateEvent +
                '}';
    }
}
