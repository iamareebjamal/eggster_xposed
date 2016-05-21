package areeb.xposed.eggster;

import android.os.Build;

public enum Egg {

    GINGERBREAD("GB", "Gingerbread"),
    HONEYCOMB("HC", "HoneyComb"),
    ICE_CREAM_SANDWICH("ICS", "ICS"),
    JELLY_BEAN("JB", "JellyBean"),
    KITKAT("KK", "KitKat"),
    L_PREVIEW("L", "L"),
    LOLLIPOP("LP", "Lollipop"),
    MARSHMALLOW("MM", "Marshmallow");


    public static int BUILD = Build.VERSION.SDK_INT;
    private String name;
    private String id;

    Egg(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Egg getEggFromId(String id) {
        for (Egg egg : Egg.values()) {
            if (id.equals(egg.getId()))
                return egg;
        }
        return null;
    }

    public static Egg getEggFromBuild(int build_no) {
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

    public static Egg getSystemEgg() {
        return getEggFromBuild(BUILD);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPackage() {
        return "areeb.xposed.eggster.eggs." + getId().toLowerCase() + ".PlatLogoActivity";
    }

    public String getDrawable(){
        return "dessert_" + name.toLowerCase();
    }
}
