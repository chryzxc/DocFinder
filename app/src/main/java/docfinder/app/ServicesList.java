package docfinder.app;

public class ServicesList {

    private String service,serviceID;


    public ServicesList(String serviceID,String service) {
        this.service = service;
        this.serviceID = serviceID;


    }


    public String getService() {
        return service;

    }

    public String getServiceID(){
        return serviceID;
    }
}