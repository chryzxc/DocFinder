package docfinder.app;

import android.graphics.Bitmap;
import android.media.Image;

import java.util.ArrayList;
import java.util.Date;

public class SearchList {

    private String name, id,details,address;
    private Double mapLat,mapLong,ratings,my_rating;
    private int count;
    private Bitmap image;


    public SearchList(String id, String name, String details,String address,Double ratings,Double my_rating,int count,Double mapLat, Double mapLong, Bitmap image) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.address = address;
        this.mapLat = mapLat;
        this.mapLong = mapLong;
        this.ratings = ratings;
        this.my_rating = my_rating;
        this.image = image;
        this.count = count;

    }

    public String getId() {

        return id;
    }


    public String getName() {
        return name;

    }

    public String getDetails() {
        return details;

    }

    public String getAddress() {
        return address;

    }

    public Double getMapLat(){
        return mapLat;
    }

    public Double getMapLong(){
        return mapLong;
    }

    public Double getRatings(){
        return ratings;
    }

    public Double getMy_rating(){
        return my_rating;
    }


    public Bitmap getImage() {
        return image;

    }


    public void setImage(Bitmap image){
        this.image = image;

    }

    public int getCount() {
        return count;

    }

}