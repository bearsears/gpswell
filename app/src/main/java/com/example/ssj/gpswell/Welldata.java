package com.example.ssj.gpswell;

/**
 * Created by ssj on 15/02/18.
 */

public class Welldata {

    private String licence, status;
    private String surface, uwi;
    private double blat, blong;

    public Welldata(String licence, String status,
                    String surface, String uwi,
                    double blat, double blong) {

        this.licence = licence;
        this.status = status;
        this.surface = surface;
        this.uwi = uwi;
        this.blat = blat;
        this.blong = blong;

    }

    public String getStatus() {return status; }

    public String getLicence() {
        return licence;
    }

    public String getSurface() { return surface; }

    public String getUWI() {return uwi; }

    public double getblat() { return blat; }

    public double getblong() { return blong; }

}
