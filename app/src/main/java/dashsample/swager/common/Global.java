package dashsample.swager.common;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Eric-Local on 8/23/2014.
 */
public final class Global {
    private static Toast mToast;

    public static void showToast(Context context, boolean hideExisiting, int stringResource) {
        if (hideExisiting && mToast != null)
            mToast.cancel();
        mToast = Toast.makeText(context, context.getString(stringResource), Toast.LENGTH_SHORT);
        mToast.show();
    }
}
