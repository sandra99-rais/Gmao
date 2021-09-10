package com.example.gmaoapp;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class header extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header);
        //link
        TextView textView = (TextView) findViewById(R.id.text3);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

}}
