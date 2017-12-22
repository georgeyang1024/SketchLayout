package com.lvshou.sketchlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SketchLayout layout = new SketchLayout(this);
        setContentView(layout);
        layout.show();
    }
}
