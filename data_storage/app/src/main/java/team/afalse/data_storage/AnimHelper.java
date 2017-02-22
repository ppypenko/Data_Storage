package team.afalse.data_storage;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

/**
 * Created by Kyle on 2/22/2017.
 */

public class AnimHelper {

    public static void animate(Context context, View v) {
        v.startAnimation(AnimationUtils.loadAnimation(context, getAnimation(v)));
        v.setVisibility(isViewVisible(v) ? View.GONE : View.VISIBLE);
    }

    private static int getAnimation(View v) {
        return isViewVisible(v) ? R.anim.to_bottom : R.anim.from_bottom;
    }

    private static boolean isViewVisible(View v) {
        return v.getVisibility() == View.VISIBLE;
    }
}
