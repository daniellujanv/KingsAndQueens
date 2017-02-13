package dlapps.dlv.kqandroid.Utils;

/**
 * Created by DanielLujanApps on Sunday29/01/17.
 */

public enum ModeType {
    PLAYDATES, PROMOTIONS, UNKNOWN;

    public static ModeType fromInt(int position) {
        switch (position){
            case 0: return PLAYDATES;
            case 1: return PROMOTIONS;
            default: return UNKNOWN;
        }
    }
}
