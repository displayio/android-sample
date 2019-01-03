package io.display.displayiosampleapp.base.util;

import java.util.ArrayList;

public class StaticValues {

    public static final String APP_ID = "appId";
    public static final String PLACEMENT_ID = "placementId";
    public static final String REQUEST_ID = "requestId";
    public static final String IS_PREDEFINED = "isPredefined";
    public static final String POSITION = "position";

    public static final ArrayList<PlacementWrapper> PREDEFINED_PLACEMENTS = new ArrayList<PlacementWrapper>() {{
        add(new PlacementWrapper("6494", "4654", "Video Interstitial", "[Interstitial]"));
        add(new PlacementWrapper("6494", "3231", "Display Interstitial", "[Interstitial]"));
        add(new PlacementWrapper("6494", "4655", "Video Banner", "[Banner]"));
    }};
}
