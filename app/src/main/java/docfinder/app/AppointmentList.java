package docfinder.app;

import java.util.Date;

public class AppointmentList {

    private String id,details,name,patientID,doctorID;
    private Date date,start,end;



    public AppointmentList(String id, Date date, Date start, Date end,String name,String details,String patientID,String doctorID) {
        this.id = id;
        this.details = details;
        this.name = name;
        this.patientID = patientID;
        this.doctorID = doctorID;

        this.date = date;
        this.start = start;
        this.end = end;

    }

    public String getId() {

        return id;
    }



    public String getDetails() {
        return details;

    }


    public String getName() {
        return name;

    }


    public String getPatientID() {
        return patientID;

    }


    public String getDoctorID() {
        return doctorID;

    }




    public Date getDate() {

        return date;

    }

    public Date getStart() {

        return start;

    }

    public Date getEnd() {

        return end;

    }







}