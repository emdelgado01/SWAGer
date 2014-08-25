package dashsample.swager.fragments;

import android.app.Activity;
import android.app.Fragment;

/**
 * Created by Eric-Local on 8/21/2014.
 */
public abstract class BaseNavFragment extends Fragment {

    protected Listener mListener;

    public interface Listener {
        public void toggleDrawerIndication(boolean showIndicator);
    }

    public abstract void safeDestroy();

    public abstract int getTitle();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement BaseNavFragment Listener.");
        }
    }

}
