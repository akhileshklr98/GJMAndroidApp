package com.example.testapp;

public class MyScheduleDetailsPopulate {
    public String AffNo, hospotalName, purpose, scheduleType, status, myScheduleID;

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
    public String getHospitalName() {
        return this.hospotalName;
    }
}

