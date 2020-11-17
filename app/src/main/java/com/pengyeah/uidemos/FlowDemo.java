package com.pengyeah.uidemos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.pengyeah.flowview.ToutiaoLoading;
import com.pengyeah.flowview.ToutiaoLoading2;

public class FlowDemo extends AppCompatActivity {

    ImageView iv;

    SeekBar seekBar;

    ImageView ivLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_demo);

        iv = findViewById(R.id.iv);
        final ToutiaoLoading toutiaoLoading = new ToutiaoLoading();
        iv.setImageDrawable(toutiaoLoading);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toutiaoLoading.start();
            }
        }, 1000l);


        ivLoading = findViewById(R.id.ivLoading);
        final ToutiaoLoading2 toutiaoLoading2 = new ToutiaoLoading2();
        ivLoading.setImageDrawable(toutiaoLoading2);

        seekBar = findViewById(R.id.seekBar);

        final ToutiaoLoading2 toutiaoLoading3 = new ToutiaoLoading2();
        toutiaoLoading3.configABaABaFunc();
        ((ImageView)findViewById(R.id.ivABa)).setImageDrawable(toutiaoLoading3);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toutiaoLoading2.startAnim();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                seekBar.setMax(toutiaoLoading2.getBounds().height() - 20);
                seekBar.setProgress(0);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        toutiaoLoading2.transform(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                toutiaoLoading3.configABaABaFunc();
                toutiaoLoading3.startAnim();
            }
        }, 1000l);
    }
}
