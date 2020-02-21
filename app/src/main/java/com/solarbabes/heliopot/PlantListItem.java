package com.solarbabes.heliopot;

public class PlantListItem {

    // Images are referenced by their ID numbers.
    // A plant item should display a thumbnail of the plant.

    private int imageID;
    private String name;
    private String wateringTime;
    private String ID;

    public PlantListItem(String ID, int imageID, String name, String wateringTime) {
        this.imageID = imageID;
        this.name = name;
        this.wateringTime = wateringTime;
        this.ID = ID;
    }

    public int getImageID() {
        return imageID;
    }

    public String getName() {
        return name;
    }

    public String getWateringTime() {
        return wateringTime;
    }

    public String getID() { return ID; }
}
