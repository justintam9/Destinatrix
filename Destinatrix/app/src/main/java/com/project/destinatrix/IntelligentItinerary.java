package com.project.destinatrix;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.project.destinatrix.objects.DestinationData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.lang.Math.*;
import java.util.Random;

public class IntelligentItinerary {
    public IntelligentItinerary() {}

    public static ArrayList<Object> computeItinerary(double latitude, double longitude, ArrayList<Object> destinations) {
        // Pass current location
        DestinationData currentLocation = new DestinationData("", "", null, new LatLng(latitude, longitude), null, null, null, 0.0, "");
        destinations.add(0, currentLocation);

        // setup algo
        int numIterations = 5000;
        int count = 0;
        Random random = new Random();
        double bestDistance = calcDistance(destinations);
        ArrayList<Object> itinerary = (ArrayList<Object>) destinations.clone();

        while (count < numIterations) {
            // leave current location in front
            int i = random.nextInt((destinations.size() - 1)) + 1;
            int j = random.nextInt((destinations.size() - 1)) + 1;

            swap(destinations, i, j);
            double d = calcDistance(destinations);

            if (d < bestDistance) {
                itinerary = (ArrayList<Object>) destinations.clone();
                bestDistance = d;
            }
            count++;
        }

        itinerary.remove(currentLocation);
        return itinerary;
    }

    private static double calcDistance(ArrayList<Object> destinations) {
        double sum = 0;
        for (int i = 0; i < destinations.size() - 1; i++) {
            DestinationData d1 = (DestinationData) destinations.get(i);
            DestinationData d2 = (DestinationData) destinations.get(i+1);
            double dist = Math.sqrt(Math.abs((d2.getLongitude() - d1.getLongitude()) + (d2.getLatitude() - d1.getLatitude())));
            sum += dist;
        }

        return sum;
    }

    private static void swap(ArrayList<Object> destinations, int i, int j) {
        Object temp = destinations.get(i);
        destinations.set(i, destinations.get(j));
        destinations.set(j, temp);
    }
}
