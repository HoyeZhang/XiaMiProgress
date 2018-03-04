package com.example.a25216.xiamiprogress;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.a25216.xiamiprogress.view.TimeProgressView;

public class MainActivity extends AppCompatActivity {
    private Boolean isDraw = true;
    private Boolean isLoading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TimeProgressView timeView1 = (TimeProgressView) findViewById(R.id.tv_home1);
        final TimeProgressView timeView2 = (TimeProgressView) findViewById(R.id.tv_home2);
        final TimeProgressView timeView3 = (TimeProgressView) findViewById(R.id.tv_home3);

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeView1.setmLineStartColor(Color.GREEN);
                timeView2.setmLineStartColor(Color.BLUE);
                timeView3.setmLineEndColor(Color.RED);
                timeView3.setmLineStartColor(Color.BLACK);

            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeView1.setmTotalTime(400);
                timeView2.setmTotalTime(10);
                timeView3.setmTotalTime(300);

            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeView1.setmCurrentTime(20);
                timeView2.setmCurrentTime(3);
                timeView3.setmCurrentTime(500);
            }
        });
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeView1.setmBgColor(Color.BLUE);
                timeView2.setmBgColor(Color.GRAY);
                timeView3.setmBgColor(Color.CYAN);

            }
        });

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeView1.setmTextColor(Color.BLACK);
                timeView2.setmTextColor(Color.RED);
                timeView3.setmTextColor(Color.YELLOW);

            }
        });
        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDraw = !isDraw;
                timeView1.setDraw(isDraw);
                timeView2.setDraw(isDraw);
                timeView3.setDraw(isDraw);

            }
        });
        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoading = !isLoading;
                timeView1.setLoading(isLoading);
                timeView2.setLoading(isLoading);
                timeView3.setLoading(isLoading);


            }
        });


    }

}
