package com.nolankuza.theultimatealliance;

public interface MessageLogger {
    void broadcast(String message, int color);
    void setDeviceToggle(int position, boolean toggled);
}
