package com.example.lrtproject;

public class DeviceT {
    public String uID, deviceToken, roleId, role;

    public DeviceT(String uID, String deviceToken, String roleId, String role) {
        this.uID = uID;
        this.deviceToken = deviceToken;
        this.roleId = roleId;
        this.role = role;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
