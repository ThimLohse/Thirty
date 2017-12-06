package se.umu.thlo0007.dicegame_revised;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * This class is used to create and return a custom DialogFragment to be displayed when the user wants to start a new game.
 * It creates an AlertDialog, and it contains a custom interface for passing back the user selection to the parent/creating activity.
 *
 * @author Thim Lohse
 *
 */
public class NewGameDialogFragment extends DialogFragment {

    /**
     * A custom interface used to pass back the user selection to the parent/creating activity.
     * Only positive and negative click is implemented, not neutral.
     */
    public interface NewGameDialogListener
    {
        void onDialogPositiveClick(DialogFragment dialogFragment);
        void onDialogNegativeClick(DialogFragment dialogFragment);
    }

    NewGameDialogListener mListener;

    /**
     *
     * @param context The parent activity.
     * @throws ClassCastException if the creating activity has not implemented the needed interface.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

            try {
                mListener = (NewGameDialogListener) context;

            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString() + " must implement NewGameDialogListener");
            }

    }

    /**
     * The method used to create a new AlertDialog.
     * The interface callback methods are used here to pass back the user selection to the parent activity.
     * @param savedInstanceState unused in this implementation.
     * @return A new AlertDialog.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.alert_title).setMessage(R.string.alert_message);

        builder.setPositiveButton(R.string.alert_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogPositiveClick(NewGameDialogFragment.this);
            }
        });
        builder.setNegativeButton(R.string.alert_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogNegativeClick(NewGameDialogFragment.this);
            }
        });


        return builder.create();
    }
}
