package com.solarbabes.heliopot;

public class PlantListItem {

    // Images are referenced by their ID numbers.
    // A plant item should display a thumbnail of the plant.

    private int imageID;
    private String name;
    private String wateringTime;

    public PlantListItem(int imageID, String name, String wateringTime) {
        this.imageID = imageID;
        this.name = name;
        this.wateringTime = wateringTime;
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
}
