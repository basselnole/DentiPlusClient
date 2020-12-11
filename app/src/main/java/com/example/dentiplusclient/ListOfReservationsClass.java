package com.example.dentiplusclient;

public class ListOfReservationsClass {


    private String  Patient_name,Patient_email,Patient_phone,Patient_address,Reservation_Date,Reservation_Time,Reservation_Cause,Status;
    //name = dr name in case of admin check reservations
    public ListOfReservationsClass(){

    }
// lazm el string tb2a zae asm l child bzbt

    public ListOfReservationsClass(String Patient_name,String Patient_email,
                                   String Patient_phone,String Patient_address,String Reservation_Date,String Reservation_Time,
                                   String Reservation_Cause,String status) {

        this.Patient_name=Patient_name;
        this.Patient_email=Patient_email;
        this.Patient_phone=Patient_phone;
        this.Patient_address=Patient_address;
        this.Reservation_Date=Reservation_Date;
        this.Reservation_Time=Reservation_Time;
        this.Reservation_Cause=Reservation_Cause;
        this.Status=status;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPatient_name() {
        return Patient_name;
    }

    public String getPatient_email() {
        return Patient_email;
    }

    public String getPatient_phone() {
        return Patient_phone;
    }

    public String getPatient_address() {
        return Patient_address;
    }

    public String getReservation_Date() {
        return Reservation_Date;
    }

    public String getReservation_Time() {
        return Reservation_Time;
    }

    public String getReservation_Cause() {
        return Reservation_Cause;
    }

    public void setPatient_name(String patient_name) {
        Patient_name = patient_name;
    }


    public void setPatient_phone(String patient_phone) {
        Patient_phone = patient_phone;
    }

    public void setPatient_address(String patient_address) {
        Patient_address = patient_address;
    }

    public void setReservation_Date(String reservation_Date) {
        Reservation_Date = reservation_Date;
    }

    public void setReservation_Time(String reservation_Time) {
        Reservation_Time = reservation_Time;
    }

    public void setReservation_Cause(String reservation_Cause) {
        Reservation_Cause = reservation_Cause;
    }
}
