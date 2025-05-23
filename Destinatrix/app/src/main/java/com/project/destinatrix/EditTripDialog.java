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

public class EditTripDialog extends AppCompatDialogFragment {
    private EditText tripName;
    private EditText tripDesc;
    private tripDialogListener listener;
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.trip_dialog,null);

        builder.setView(view);
        builder.setTitle("Edit Trip");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Bundle args = getArguments();
                        String name = tripName.getText().toString();
                        String description = tripDesc.getText().toString();
                        Integer pos = args.getInt("pos");
                        listener.editTexts(name,description,pos);
                    }
                });
        tripName = view.findViewById(R.id.trip_name);
        tripDesc = view.findViewById(R.id.trip_description);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (tripDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "need to implement tripDialogListener");
        }
    }

    public interface tripDialogListener{
        void editTexts(String name, String description,Integer pos);
    }
}
