package com.example.testapp;

public class MyScheduleDetailsPopulate {
    public String AffNo;
    public String hospotalName;
    public String purpose;
    public String scheduleType;
    public String status;
    public String myScheduleID;


    public MyScheduleDetailsPopulate() {
        super();
    }

    public MyScheduleDetailsPopulate(String affNo,
                                     String hospotalname,
                                     String purpose,
                                     String scheduletype,
                                     String status,
                                     String myscheduleID) {
        super();
        this.AffNo = affNo;
        this.hospotalName = hospotalname;
        this.purpose = purpose;
        this.scheduleType = scheduletype;
        this.status = status;
        this.myScheduleID = myscheduleID;
    }
}

