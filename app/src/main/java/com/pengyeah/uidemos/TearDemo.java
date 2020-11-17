package com.pengyeah.uidemos;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.pengyeah.tear.PaperLayout;

import static com.pengyeah.tear.ConstantKt.STATE_TEARED;

public class TearDemo extends AppCompatActivity {

    public PaperLayout paperLayout1, paperLayout2, paperLayout3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tear_demo);
        paperLayout1 = findViewById(R.id.paperLayout1);
        paperLayout1.setOnTearStateChangeListener(new PaperLayout.OnTearStateChangeListener() {
            @Override
            public void onTearStateChanged(int tearState) {
                if (tearState == STATE_TEARED) {
                    paperLayout1.setVisibility(View.GONE);
                }
            }
        });
        paperLayout2 = findViewById(R.id.paperLayout2);
        paperLayout2.setOnTearStateChangeListener(new PaperLayout.OnTearStateChangeListener() {
            @Override
            public void onTearStateChanged(int tearState) {
                if (tearState == STATE_TEARED) {
                    paperLayout2.setVisibility(View.GONE);
                }
            }
        });
        paperLayout3 = findViewById(R.id.paperLayout3);
        paperLayout3.setOnTearStateChangeListener(new PaperLayout.OnTearStateChangeListener() {
            @Override
            public void onTearStateChanged(int tearState) {
                if (tearState == STATE_TEARED) {
                    paperLayout3.setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paperLayout1.startTearAnim();
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paperLayout2.startTearAnim();
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paperLayout3.startTearAnim();
            }
        });
    }
}
