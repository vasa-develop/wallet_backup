package com.example.root.authex;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import jnr.ffi.annotations.In;

public class ChooseCard extends AppCompatActivity {

    private TextView t1, t2;
    private ImageView img1, img2;
    private int option;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_card);

        t1 = (TextView) findViewById(R.id.t1);
        t2 = (TextView) findViewById(R.id.t2);

        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);


        next = (Button) findViewById(R.id.next);

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option = 0;
                img1.setImageResource(R.drawable.selected_circle);
                img2.setImageResource(R.drawable.unselected_circle);

            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option = 1;
                img1.setImageResource(R.drawable.unselected_circle);
                img2.setImageResource(R.drawable.selected_circle);

            }
        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChooseCard.this,CardDetails.class);

                Bundle bundle = new Bundle();
                bundle.putInt("card_name",option);
                i.putExtras(bundle);

                startActivityForResult(i,1);
            }
        });
    }
}
