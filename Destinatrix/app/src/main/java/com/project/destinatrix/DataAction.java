package com.project.destinatrix;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.destinatrix.objects.CityData;
import com.project.destinatrix.objects.TripData;
import com.project.destinatrix.objects.UserData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;


// Strategy Design pattern
public enum DataAction {
    CityData {
        void addData(Object data, DatabaseReference dbRef) {
            CityData cityData = (CityData)data;
            dbRef.child(cityData.getTripId()).child(cityData.getCityId()).setValue(cityData);
        }

        void updateData() {
            // TODO: This.
        }

        void readData(DatabaseReference dbRef, ReadCallback callback) {
            ArrayList<Object> cities = new ArrayList<>();
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        CityData city = snapshot.getValue(CityData.class);
                        cities.add(city);
                    }
                    callback.onCallBack(cities);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        void deleteData() {
            // TODO: This.
        }
    },
    TripData {
        void addData(Object data, DatabaseReference dbRef) {
            TripData tripData = (TripData)data;
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            dbRef.child(user.getUid()).child(tripData.getTripID()).setValue(tripData);
        }

        void updateData() {
            // TODO: This.
        }

        void readData(DatabaseReference dbRef, ReadCallback callback) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference tripsRef = dbRef.child(user.getUid());
            ArrayList<Object> trips = new ArrayList<>();
            tripsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        TripData trip = snapshot.getValue(TripData.class);
                        trips.add(trip);
                    }
                    callback.onCallBack(trips);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }

        void deleteData() {
            // TODO: This.
        }
    },
    UserData {
        void addData(Object data, DatabaseReference dbRef) {
            UserData userData = (UserData)data;
            dbRef.child(userData.getId()).setValue(userData);
        }

        void updateData() {

        }

        void readData(DatabaseReference dbRef, ReadCallback callback) {}
        void deleteData() {}
    };

    abstract void addData(Object data, DatabaseReference dbRef);
    abstract void updateData();
    abstract void readData(DatabaseReference dbRef, ReadCallback callback);
    abstract void deleteData();
}

