package areeb.xposed.eggster;

import android.os.Build;

public enum Eggs {

    GINGERBREAD("GB"),
    HONEYCOMB("HC"),
    ICE_CREAM_SANDWICH("ICS"),
    JELLY_BEAN("JB"),
    KITKAT("KK"),
    L_PREVIEW("L"),
    LOLLIPOP("LP"),
    M_PREVIEW("M"),
    MARSHMALLOW("MM"),
    N_PREVIEW("N");


    public static int BUILD = Build.VERSION.SDK_INT;
    private String id;

    Eggs(String id) {
        this.id = id;
    }

    public static Eggs getEggFromId(String id) {
        for (Eggs egg : Eggs.values()) {
            if (id.equals(egg.getId()))
                return egg;
        }
        return null;
    }

    public static Eggs getEggFromBuld(int build_no) {
        if (build_no < Build.VERSION_CODES.HONEYCOMB)
            return GINGERBREAD;
        else if (build_no < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            return HONEYCOMB;
        else if (build_no < Build.VERSION_CODES.JELLY_BEAN)
            return ICE_CREAM_SANDWICH;
        else if (build_no < Build.VERSION_CODES.KITKAT)
            return JELLY_BEAN;
        else if (build_no < Build.VERSION_CODES.LOLLIPOP)
            return KITKAT;
        else if (build_no < Build.VERSION_CODES.M)
            return LOLLIPOP;
        else if (build_no >= 23)
            return MARSHMALLOW;
        else
            return GINGERBREAD;
    }

    public static Eggs getSystemEgg() {
        return getEggFromBuld(BUILD);
    }

    public String getId() {
        return id;
    }

    public String getPackage() {
        return getPackage() + getId().toLowerCase() + ".PlatLogoActivity";
    }
}
