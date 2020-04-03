package com.compulynx.compas.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: SJ
 * Date: 3/2/14
 * Time: 2:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class Recipient {
    public String picture;
    public String first_name;
    public String last_name;
    public String gender;
    public String birthdate;

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    List<FingerPrintDetails> fingerprints = new ArrayList<FingerPrintDetails>();

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<FingerPrintDetails> getFingerprints() {
        return fingerprints;
    }

    public void setFingerprints(List<FingerPrintDetails> fingerprints) {
        this.fingerprints = fingerprints;
    }
}
