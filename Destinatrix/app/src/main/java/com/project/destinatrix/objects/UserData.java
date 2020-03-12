package com.project.destinatrix.objects;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserData {

    private String name, email, id;

    public UserData(){

    }

    public UserData(String name, String email, String id) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }
}
