package docfinder.app;

import java.util.Date;

public class NearbyList {

    private String id,facility_name,specialization;
    private Double latitude,longitude,distance;

    public NearbyList(String id, String facility_name, String specialization,Double latitude, Double longitude, Double distance) {
        this.id = id;
        this.facility_name = facility_name;
        this.specialization = specialization;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }



    public String getId() {

        return id;
    }

    public String getFacility_name() {

        return facility_name;
    }

    public String getSpecialization() {

        return specialization;
    }

    public Double getLatitude() {

        return latitude;
    }

    public Double getLongitude() {

        return longitude;
    }

    public Double getDistance(){
        return distance;
    }





}