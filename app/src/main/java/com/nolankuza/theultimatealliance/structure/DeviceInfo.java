package com.nolankuza.theultimatealliance.structure;

public class DeviceInfo {
    private String name;
    private boolean enabled = true;
    private Assignment assignment;

    public DeviceInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggleEnabled() {
        this.enabled = !this.enabled;
    }

    //TODO Implement or remove
    public Assignment getAssignment() {
        return assignment;
    }
}
