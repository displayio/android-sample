package io.display.displayiosampleapp.base.util;

import java.util.HashMap;

public class StaticValues {

    public static final String APP_ID = "app_id";
    public static final String PLACEMENT_ID = "placement_id";
    public static final String IS_NATIVE_ADD = "is_native_add";
    public static final String IS_PREDEFINED = "is_predefined";

    public static final String PREDEFINED_PLACEMENTS_APP_ID = "6494";
    public static final String PREDEFINED_PLACEMENTS = "{\n" +
            "  \"3231\":{\n" +
            "    \"name\":\"display.io Interstitial\",\n" +
            "    \"status\":\"enabled\",\n" +
            "    \"ads\":[\n" +
            "      {\n" +
            "        \"adId\":\"II_io.display.displayioshowcase\",\n" +
            "        \"ad\":{\n" +
            "          \"type\":\"interstitial\",\n" +
            "          \"subtype\":\"interactive\",\n" +
            "          \"data\":{\n" +
            "            \"clickUrl\":\"https://appsrv.display.io/click?msessId=5b3f1c5419bbe&tls=19378099_55_19&p=3231\",\n" +
            "            \"impressionBeacon\":\"https://appsrv.display.io/imp?msessId=5b3f1c5419bbe&tls=19378099_55_19&p=3231\",\n" +
            "            \"pkgName\":\"io.display.displayioshowcase\",\n" +
            "            \"appName\":\"Display.io ShowCase\",\n" +
            "            \"shortDescription\":\"Display.io is an Android Monetization platform connecting to mobile app developers via SDK or API.\",\n" +
            "            \"icon100\":\"//cdn.display.io/ctv/38aaecc9153f6f23f30ee75227eaeb91banner100100.png\",\n" +
            "            \"icon200\":\"//cdn.display.io/ctv/4da2bf5aefe5629c3279a8df1e8d1d45banner200200.png\",\n" +
            "            \"callToAction\":\"Install\",\n" +
            "            \"rating\":null,\n" +
            "            \"template\":\"video\",\n" +
            "            \"creatives\":{\n" +
            "              \"carousel\":[\n" +
            "                {\n" +
            "                  \"creativeUrl\":\"http://cdn.display.io/ctv/asset/static/174_310.jpeg\",\n" +
            "                  \"width\":\"174\",\n" +
            "                  \"height\":\"310\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"creativeUrl\":\"http://cdn.display.io/ctv/asset/static/174_310.jpeg\",\n" +
            "                  \"width\":\"174\",\n" +
            "                  \"height\":\"310\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"creativeUrl\":\"http://cdn.display.io/ctv/asset/static/174_310.jpeg\",\n" +
            "                  \"width\":\"174\",\n" +
            "                  \"height\":\"310\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"creativeUrl\":\"http://cdn.display.io/ctv/asset/static/174_310.jpeg\",\n" +
            "                  \"width\":\"174\",\n" +
            "                  \"height\":\"310\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"creativeUrl\":\"http://cdn.display.io/ctv/asset/static/194_310.jpeg\",\n" +
            "                  \"width\":\"194\",\n" +
            "                  \"height\":\"310\"\n" +
            "                }\n" +
            "              ],\n" +
            "              \"video\":[\n" +
            "                {\n" +
            "                  \"creativeUrl\":\"http://cdn.display.io/ctv/asset/video/640_360.mp4\",\n" +
            "                  \"width\":\"640\",\n" +
            "                  \"height\":\"360\",\n" +
            "                  \"duration\":20\n" +
            "                }\n" +
            "              ]\n" +
            "            },\n" +
            "            \"adCloseBeaconUrl\":\"https://appsrv.display.io/imp?msessId=5b3f1c5419bbe&tls=19378099_55_19&p=3231&metric=adclose&sesslength={length}\",\n" +
            "            \"skippableIn\":5\n" +
            "          }\n" +
            "        },\n" +
            "        \"offering\":{\n" +
            "          \"type\":\"app\",\n" +
            "          \"cpn\":3402675,\n" +
            "          \"id\":\"io.display.displayioshowcase\"\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"3266\":{\n" +
            "    \"name\":\"display.io Rewarded Video\",\n" +
            "    \"status\":\"enabled\",\n" +
            "    \"ads\":[\n" +
            "      {\n" +
            "        \"adId\":\"RVA_io.display.displayioshowcase\",\n" +
            "        \"ad\":{\n" +
            "          \"type\":\"rewardedVideo\",\n" +
            "          \"subtype\":\"in_app\",\n" +
            "          \"data\":{\n" +
            "            \"appCategories\":[\n" +
            "              \"Libraries & Demo\"\n" +
            "            ],\n" +
            "            \"numDownloads\":\"10+\",\n" +
            "            \"contentRating\":\"Everyone\",\n" +
            "            \"clickUrl\":\"https://appsrv.display.io/click?msessId=5b3f1c541e85b&tls=19378099_55_19&p=3266\",\n" +
            "            \"impressionBeacon\":\"https://appsrv.display.io/imp?msessId=5b3f1c541e85b&tls=19378099_55_19&p=3266\",\n" +
            "            \"pkgName\":\"io.display.displayioshowcase\",\n" +
            "            \"appName\":\"Display.io ShowCase\",\n" +
            "            \"shortDescription\":\"Display.io is an Android Monetization platform connecting to mobile app developers via SDK or API.\",\n" +
            "            \"icon100\":\"//cdn.display.io/ctv/38aaecc9153f6f23f30ee75227eaeb91banner100100.png\",\n" +
            "            \"icon200\":\"//cdn.display.io/ctv/4da2bf5aefe5629c3279a8df1e8d1d45banner200200.png\",\n" +
            "            \"callToAction\":\"Install\",\n" +
            "            \"rating\":null,\n" +
            "            \"template\":\"in_app\",\n" +
            "            \"creatives\":{\n" +
            "              \"carousel\":[\n" +
            "                {\n" +
            "                  \"creativeUrl\":\"http://cdn.display.io/ctv/asset/static/174_310.jpeg\",\n" +
            "                  \"width\":\"174\",\n" +
            "                  \"height\":\"310\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"creativeUrl\":\"http://cdn.display.io/ctv/asset/static/174_310.jpeg\",\n" +
            "                  \"width\":\"174\",\n" +
            "                  \"height\":\"310\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"creativeUrl\":\"http://cdn.display.io/ctv/asset/static/174_310.jpeg\",\n" +
            "                  \"width\":\"174\",\n" +
            "                  \"height\":\"310\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"creativeUrl\":\"http://cdn.display.io/ctv/asset/static/174_310.jpeg\",\n" +
            "                  \"width\":\"174\",\n" +
            "                  \"height\":\"310\"\n" +
            "                }\n" +
            "              ],\n" +
            "              \"video\":[\n" +
            "                {\n" +
            "                  \"creativeUrl\":\"http://cdn.display.io/ctv/asset/video/640_360.mp4\",\n" +
            "                  \"width\":\"640\",\n" +
            "                  \"height\":\"360\",\n" +
            "                  \"duration\":20\n" +
            "                }\n" +
            "              ]\n" +
            "            },\n" +
            "            \"adCloseBeaconUrl\":\"https://appsrv.display.io/imp?msessId=5b3f1c541e85b&tls=19378099_55_19&p=3266&metric=adclose&sesslength={length}\",\n" +
            "            \"skippableIn\":3\n" +
            "          }\n" +
            "        },\n" +
            "        \"offering\":{\n" +
            "          \"type\":\"app\",\n" +
            "          \"cpn\":3402675,\n" +
            "          \"id\":\"io.display.displayioshowcase\"\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"3264\":{\n" +
            "    \"name\":\"display.io In-Feed\",\n" +
            "    \"status\":\"enabled\",\n" +
            "    \"ads\":[\n" +
            "      {\n" +
            "        \"adId\":\"IFV_c8fd5554a7ae4f71494068b68f622499\",\n" +
            "        \"ad\":{\n" +
            "          \"type\":\"infeed\",\n" +
            "          \"subtype\":\"video\",\n" +
            "          \"data\":{\n" +
            "            \"clk\":\"https://appsrv.display.io/click?msessId=5b3f1c541a325&tls=19378099_55_19&p=3264\",\n" +
            "            \"imp\":\"https://appsrv.display.io/imp?msessId=5b3f1c541a325&tls=19378099_55_19&p=3264\",\n" +
            "            \"video\":\"https://cdn.display.io/ctv/asset/video/640_360.mp4\",\n" +
            "            \"vheight\":320,\n" +
            "            \"vwidth\":640,\n" +
            "            \"duration\":15,\n" +
            "            \"landingCard\":\"https://cdn.display.io/ctv/asset/static/1200_628.jpeg\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"offering\":{\n" +
            "          \"type\":\"app\",\n" +
            "          \"cpn\":3402675,\n" +
            "          \"id\":\"io.display.displayioshowcase\"\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"3265\":{\n" +
            "    \"name\":\"display.io Native\",\n" +
            "    \"status\":\"enabled\",\n" +
            "    \"ads\":[\n" +
            "      {\n" +
            "        \"adId\":\"NS_f6097cca6f310e6e23b48145b778d9a1\",\n" +
            "        \"ad\":{\n" +
            "          \"type\":\"native\",\n" +
            "          \"data\":{\n" +
            "            \"clickUrl\":\"https://appsrv.display.io/click?msessId=5b3f1c541aa18&tls=19378099_55_19&p=3265\",\n" +
            "            \"impressionBeacon\":\"https://appsrv.display.io/imp?msessId=5b3f1c541aa18&tls=19378099_55_19&p=3265\",\n" +
            "            \"pkgName\":\"io.display.displayioshowcase\",\n" +
            "            \"appName\":\"Display.io ShowCase\",\n" +
            "            \"shortDescription\":\"Display.io is an Android Monetization platform connecting to mobile app developers via SDK or API.\",\n" +
            "            \"appCategories\":[\n" +
            "              \"Libraries & Demo\"\n" +
            "            ],\n" +
            "            \"icon100\":\"//cdn.display.io/ctv/38aaecc9153f6f23f30ee75227eaeb91banner100100.png\",\n" +
            "            \"icon200\":\"//cdn.display.io/ctv/4da2bf5aefe5629c3279a8df1e8d1d45banner200200.png\",\n" +
            "            \"callToAction\":\"Install\",\n" +
            "            \"numDownloads\":\"10+\",\n" +
            "            \"rating\":null,\n" +
            "            \"contentRating\":\"Everyone\",\n" +
            "            \"creativeWidth\":\"320\",\n" +
            "            \"creativeHeight\":\"480\",\n" +
            "            \"creativeUrl\":\"//cdn.display.io/ctv/asset/static/320_480.jpeg\"\n" +
            "          },\n" +
            "          \"subtype\":\"static\"\n" +
            "        },\n" +
            "        \"offering\":{\n" +
            "          \"type\":\"app\",\n" +
            "          \"cpn\":3402675,\n" +
            "          \"id\":\"io.display.displayioshowcase\"\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    public static final HashMap<String, String> AD_TYPES = new HashMap<String, String>() {{
        put("interstitial", "[Interstitial]");
        put("rewardedVideo", "[Rewarded Video]");
        put("infeed", "[In-Feed]");
        put("native", "[Native]");
    }};
}
