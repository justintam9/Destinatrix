package com.project.destinatrix;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.app.AppCompatDialogFragment;

public class RemoveCityDialog extends AppCompatDialogFragment {
    private RemoveCityDialog.cityDialogListener listener;
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.city_dialog,null);

        builder.setView(view);
        builder.setTitle("Remove City");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Bundle args = getArguments();
                        Integer pos = args.getInt("pos");
                        listener.remove(pos);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (RemoveCityDialog.cityDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "need to implement cityDialogListener");
        }
    }

    public interface cityDialogListener{
        void remove(Integer pos);
    }
}
