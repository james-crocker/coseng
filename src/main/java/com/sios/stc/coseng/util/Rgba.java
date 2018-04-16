package com.sios.stc.coseng.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Rgba {

    String rgb  = null;
    String rgba = null;

    public Rgba(Integer r, Integer g, Integer b, Integer a) {
        if (r == null || g == null || b == null || r < 0 || g < 0 || b < 0 || (a != null && a < 0)) {
            throw new IllegalArgumentException(
                    "Unable to set RGBA values; All RGB(A) values must be greater than or equal to 0.");
        }
        List<Integer> values = new ArrayList<Integer>();
        values.add(r);
        values.add(g);
        values.add(b);
        if (a == null) {
            values.add(0);
        } else {
            values.add(a);
        }
        rgb = "rgb(" + StringUtils.join(values.subList(0, values.size() - 1), ", ") + ")";
        rgba = "rgba(" + StringUtils.join(values, ", ") + ")";
    }

    public boolean isComparable(String cssRgba) {
        /*
         * Expecting getCssValue("background-color"); results in rgb(#, #, #) or rgba(#,
         * #, #, #)
         * 
         * 2017-10-09 geckodriver 0.19.0, FF 53+ is reporting without the alpha (if 1?)
         */
        if (cssRgba != null) {
            /* Most descriptive to least */
            if (cssRgba.equals(rgba)) {
                return true;
            } else if (cssRgba.equals(rgb)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return rgb + ", " + rgba;
    }

}
