package com.pengyeah.uidemos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.pengyeah.rulerview.RulerView2;

public class RulerViewDemo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruler_view_demo);

        final RulerView2 rulerView = findViewById(R.id.rulerView);
        rulerView.setOnNumSelectListener(new RulerView2.OnNumSelectListener() {
            @Override
            public void onNumSelect(int selectedNum) {
                TextView tvNum = findViewById(R.id.tvNum);
                tvNum.setText(selectedNum + " cm");
                tvNum.setTextColor(rulerView.getIndicatorColor());
            }
        });
    }
}
