package areeb.xposed.eggster;

import android.content.Context;
import android.os.Build;

public enum Egg {

    GINGERBREAD("GB", "Gingerbread"),
    HONEYCOMB("HC", "HoneyComb"),
    ICE_CREAM_SANDWICH("ICS", "ICS"),
    JELLY_BEAN("JB", "JellyBean"),
    KITKAT("KK", "KitKat"),
    L_PREVIEW("L", "L"),
    LOLLIPOP("LP", "Lollipop"),
    MARSHMALLOW("MM", "Marshmallow"),
    N_PREVIEW("N", "N");


    public static int BUILD = Build.VERSION.SDK_INT;
    private String name;
    private String id;

    Egg(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Egg getEggFromId(String id) {
        for (Egg egg : Egg.values()) {
            if (egg.getId().equals(id))
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
        else if (build_no < 24)
            return MARSHMALLOW;
        else if (build_no == 24)
            return N_PREVIEW;
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

    public String getDrawableId() {
        return "logo_" + id.toLowerCase();
    }

    public int getDrawableRes(Context context) {
        return context.getResources().getIdentifier(getDrawableId(), "drawable", "areeb.xposed.eggster");
    }

    public String getColorId() {
        return "color_" + id.toLowerCase();
    }

    public int getColorRes(Context context) {
        return context.getResources().getIdentifier(getColorId(), "color", "areeb.xposed.eggster");
    }
}
