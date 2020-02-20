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

public class EditCityDialog  extends AppCompatDialogFragment {
    private EditText cityName;
    private EditCityDialog.cityDialogListener listener;
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.city_dialog,null);

        builder.setView(view);
        builder.setTitle("Edit City");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
                .setNeutralButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Bundle args = getArguments();
                        Integer pos = args.getInt("pos");
                        listener.remove(pos);
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Bundle args = getArguments();
                        String name = cityName.getText().toString();
                        Integer pos = args.getInt("pos");
                        listener.editTexts(name,pos);
                    }
                });
        cityName = view.findViewById(R.id.city_name);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (EditCityDialog.cityDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "need to implement cityDialogListener");
        }
    }

    public interface cityDialogListener{
        void editTexts(String name,Integer pos);
        void remove(Integer pos);
    }
}
