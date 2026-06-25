package com.example.trabalhopratico1_samueldemellocagnani;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("TpeRAGDu8RvvV4eravrUyelLzTlnUzvfzseBpmBk")
                .clientKey("DXj3YPNozME9k14GyQR7890NsYgli2hsZnNemNDh")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
