package com.project.destinatrix;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class AddCityDialog extends AppCompatDialogFragment {
    private EditText tripName;
    private cityDialogListener listener;
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_city_dialog,null);

        builder.setView(view);
        builder.setTitle("Add City");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = tripName.getText().toString();
                        listener.applyTexts(name);
                    }
                });
        tripName = view.findViewById(R.id.trip_name);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (cityDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "need to implement cityDialogListener");
        }
    }

    public interface cityDialogListener{
        void applyTexts(String name);
    }
}
