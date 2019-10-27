package com.nolankuza.theultimatealliance.model;

public enum Alliance {
    Red(0), Blue(1);

    private int code;

    Alliance(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
