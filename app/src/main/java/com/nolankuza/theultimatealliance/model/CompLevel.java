package com.nolankuza.theultimatealliance.model;

public enum CompLevel {
    QM(0), QF(1), SF(2), F(3);

    private int code;

    CompLevel(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
