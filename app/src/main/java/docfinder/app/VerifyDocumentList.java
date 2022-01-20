package docfinder.app;

import java.util.Date;

public class VerifyDocumentList {

    private String imageId,viewId;
    private Date date;



    public VerifyDocumentList(String imageId, String viewId) {
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