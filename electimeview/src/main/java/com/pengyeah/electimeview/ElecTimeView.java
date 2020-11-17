package com.pengyeah.electimeview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;
import java.util.Calendar;

/**
 * Created by pengyeah on 2020/8/27
 * 佛祖开光，永无bug
 * God bless U
 */
public class ElecTimeView extends RelativeLayout {
    ElecTimeNumView hour1, hour2;
    ElecTimeNumView minute1, minute2;
    ElecTimeNumView second1, second2;

    MainHandler handler;

    private static class MainHandler extends Handler {

        private WeakReference<ElecTimeView> mWeakReference;

        public MainHandler(ElecTimeView elecTimeView) {
            mWeakReference = new WeakReference<>(elecTimeView);
        }

        @Override
        public void handleMessage(Message msg) {
            ElecTimeView elecTimeView = mWeakReference.get();
            if (elecTimeView == null) {
                return;
            }
            if (msg.what == 0x01) {
                int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int minute = Calendar.getInstance().get(Calendar.MINUTE);
                int second = Calendar.getInstance().get(Calendar.SECOND);

                elecTimeView.hour1.setCurNum(hour / 10);
                elecTimeView.hour2.setCurNum(hour % 10);

                elecTimeView.minute1.setCurNum(minute / 10);
                elecTimeView.minute2.setCurNum(minute % 10);

                elecTimeView.second1.setCurNum(second / 10);
                elecTimeView.second2.setCurNum(second % 10);
            }
            elecTimeView.handler.sendEmptyMessageDelayed(0x01, 1000);
        }
    }

    public ElecTimeView(Context context) {
        this(context, null);
    }

    public ElecTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(context).inflate(R.layout.view_elec_time, this);

        //findView
        hour1 = view.findViewById(R.id.numView1);
        hour2 = view.findViewById(R.id.numView2);

        minute1 = view.findViewById(R.id.numView3);
        minute2 = view.findViewById(R.id.numView4);

        second1 = view.findViewById(R.id.numView5);
        second2 = view.findViewById(R.id.numView6);

        handler = new MainHandler(this);

        handler.sendEmptyMessageDelayed(0x01, 1000);

    }
}
