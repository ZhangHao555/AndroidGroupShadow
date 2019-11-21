package com.ahao.shadow;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.constraint_layout_demo).setOnClickListener(e ->{
            startActivity(new Intent(this, ShadowConstraintLayoutActivity.class));
        });

        findViewById(R.id.relative_layout_demo).setOnClickListener(e ->{
            startActivity(new Intent(this,ShadowRelativeLayoutActivity.class));
        });

        findViewById(R.id.frame_layout_demo).setOnClickListener(e ->{
            startActivity(new Intent(this,ShadowFrameLayoutActivity.class));
        });
        findViewById(R.id.linear_layout_demo).setOnClickListener(e ->{
            startActivity(new Intent(this,ShadowLinearLayoutActivity.class));
        });


    }


}
