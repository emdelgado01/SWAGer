package dashsample.swager.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import dashsample.swager.R;

/**
 * Created by Eric-Local on 8/23/2014.
 */
public class CheckoutDialogFragment extends DialogFragment {
    public static final String TAG = "CheckoutDialogFragment";

    private Callbacks mCallbacks;

    public interface Callbacks {
        public void onCreateClick(String name);
    }

    public static CheckoutDialogFragment newInstance(Callbacks callbacks) {
        CheckoutDialogFragment fragment = new CheckoutDialogFragment();
        fragment.mCallbacks = callbacks;
        return fragment;
    }

    public void setCallbacks(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final EditText name = new EditText(getActivity());
        name.setHint(R.string.name);
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setNeutralButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mCallbacks != null)
                            mCallbacks.onCreateClick(name.getText().toString());
                    }
                })
                .setTitle(R.string.action_checkout)
                .setView(name)
                .create();
        return alertDialog;
    }
}
