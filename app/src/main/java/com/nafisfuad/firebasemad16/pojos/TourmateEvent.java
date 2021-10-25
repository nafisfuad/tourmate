package com.nafisfuad.firebasemad16.pojos;

public class TourmateEvent {
    private String eventID;
    private String eventName;
    private String departurePlace;
    private String destination;
    private int budget;
    private String departureDate;

    public TourmateEvent() {
        //required by Firebase
    }

    public TourmateEvent(String eventID, String eventName, String departurePlace, String destination, int budget, String departureDate) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.departurePlace = departurePlace;
        this.destination = destination;
        this.budget = budget;
        this.departureDate = departureDate;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDeparturePlace() {
        return departurePlace;
    }

    public void setDeparturePlace(String departurePlace) {
        this.departurePlace = departurePlace;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }
}
