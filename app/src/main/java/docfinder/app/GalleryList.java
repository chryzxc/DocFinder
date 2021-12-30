package docfinder.app;

import java.util.Date;

public class GalleryList {

    private String imageId,viewId;
    private Date date;



    public GalleryList(String imageId,String viewId) {
        this.imageId = imageId;
        this.viewId = viewId;


    }

    public String getImageId() {

        return imageId;
    }

    public String getViewId() {

        return viewId;
    }
}