package io.display.displayiosampleapp.base.util;

public class PlacementWrapper {

    private String appId;
    private String id;
    private String name;
    private String type;

    public PlacementWrapper(String appId, String id, String name, String type) {
        this.appId = appId;
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getAppId() {
        return appId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
