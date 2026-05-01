package com.java110.vo.api.pubCarTrip;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/10/9
 */
public class ApiChooseTripDataVo implements Serializable {

    private static final long serialVersionUID = -1660011707782742916L;

    private String tripId;
    private String startDate;
    private String start;
    private String destination;

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
