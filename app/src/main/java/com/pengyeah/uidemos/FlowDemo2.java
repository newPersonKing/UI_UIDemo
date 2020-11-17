package com.pengyeah.uidemos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.pengyeah.flowview.FlowGuideView;
import com.pengyeah.flowview.FlowSurfaceView;
import com.pengyeah.flowview.FlowView;

import static com.pengyeah.flowview.ConstantKt.STATE_EXPANDED;
import static com.pengyeah.flowview.ConstantKt.STATE_MOVING;

public class FlowDemo2 extends AppCompatActivity {

    FlowSurfaceView fv1, fv2;

    FlowGuideView fgv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_demo2);

        fv1 = findViewById(R.id.fv1);
        fv2 = findViewById(R.id.fv2);

        fv1.setImageResource(R.mipmap.guide5);
        fv2.setImageResource(R.mipmap.guide6);

        fv1.setOnStateChangedListener(new FlowSurfaceView.OnStateChangedListener() {
            @Override
            public void onStateChanged(int state) {
                if (state == STATE_EXPANDED) {
//                    fv2.showWithAnim();
//                    fv2.setVisibility(View.VISIBLE);
                } else if (state == STATE_MOVING) {
//                    if (fv2.getVisibility() == View.VISIBLE) {
//                        fv2.hideWithAnim();
//                    }
                } else {
//                    fv2.hideWithAnim();
//                    fv2.setVisibility(View.GONE);
                }
            }
        });

        fv2.setOnStateChangedListener(new FlowSurfaceView.OnStateChangedListener() {
            @Override
            public void onStateChanged(int state) {
                if (state == STATE_EXPANDED) {

                } else if (state == STATE_MOVING) {

                } else {

                }
            }
        });

        fgv = findViewById(R.id.fgv);
        fgv.addGuides(R.mipmap.app_guide1, R.mipmap.app_guide2,R.mipmap.app_guide3);
    }
}
