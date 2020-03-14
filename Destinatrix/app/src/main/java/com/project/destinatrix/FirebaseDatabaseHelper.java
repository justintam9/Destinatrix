package com.project.destinatrix;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseDatabaseHelper {

    private DatabaseReference mDatabaseRef;
    private FirebaseDatabaseHelper() {}

    public FirebaseDatabaseHelper(String path) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(path);
    }

    public boolean createData(Object data, DataAction action) {
        action.addData(data, mDatabaseRef);
        return false;
    }

    public void readData(DataAction action, ReadCallback callback) {
        action.readData(mDatabaseRef, new ReadCallback() {
            @Override
            public void onCallBack(ArrayList<Object> list) {
                callback.onCallBack(list);
            }
        });
    }

    public String getDataId() {
        return mDatabaseRef.push().getKey();
    }
}
