package com.example.administator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administator.ProcessDaim.ProcessDaim;
import com.example.administator.Slider.SliderAdapter;
import com.example.administator.Worker.Worker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    int images[] = {R.drawable.p1, R.drawable.p2, R.drawable.p3,R.drawable.p4,R.drawable.p5,R.drawable.p6};
    int currentPageCounter = 0;

    ImageButton logOut;
    FirebaseAuth mAuth;
    ImageView notification, setting;
    ViewPager viewPager;
    LinearLayout worker, process;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        logOut = findViewById( R.id.logOut );
        notification = findViewById( R.id.notification );
        setting = findViewById( R.id.app_setting );
        worker = findViewById( R.id.worker );
        process = findViewById( R.id.process );

        mAuth = FirebaseAuth.getInstance();

        worker.setOnClickListener( v ->{
            Intent intent = new Intent(MainActivity.this, Worker.class);
            startActivity( intent );
        } );

        process.setOnClickListener( v->{
            Intent intent = new Intent(MainActivity.this, ProcessDaim.class);
            startActivity( intent );
        } );

        setting.setOnClickListener( v->{
            Intent intent = new Intent(MainActivity.this, Setting.class);
            startActivity( intent );
        } );

        // Image slider
        viewPager = findViewById( R.id.viewpager );
        viewPager.setAdapter( new SliderAdapter( images, MainActivity.this ) );

        // auto change image
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPageCounter == images.length) {
                    currentPageCounter = 0;
                }
                viewPager.setCurrentItem( currentPageCounter++, true );
            }
        };

        Timer timer = new Timer();
        timer.schedule( new TimerTask() {
            @Override
            public void run() {
                handler.post( update );
            }
        }, 5000, 5000 );
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            startActivity( new Intent( this, MobileSignUp.class ) );
        }
    }
}