package com.comincini_micheli.quest4run.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.comincini_micheli.quest4run.R;

public class FaqActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView faq1_q = (TextView)findViewById(R.id.faq1_q);
        final TextView faq1_a = (TextView)findViewById(R.id.faq1_a);
        TextView faq2_q = (TextView)findViewById(R.id.faq2_q);
        final TextView faq2_a = (TextView)findViewById(R.id.faq2_a);
        TextView faq3_q = (TextView)findViewById(R.id.faq3_q);
        final TextView faq3_a = (TextView)findViewById(R.id.faq3_a);
        TextView faq4_q = (TextView)findViewById(R.id.faq4_q);
        final TextView faq4_a = (TextView)findViewById(R.id.faq4_a);
        TextView faq5_q = (TextView)findViewById(R.id.faq5_q);
        final TextView faq5_a = (TextView)findViewById(R.id.faq5_a);
        TextView faq6_q = (TextView)findViewById(R.id.faq6_q);
        final TextView faq6_a = (TextView)findViewById(R.id.faq6_a);
        TextView faq7_q = (TextView)findViewById(R.id.faq7_q);
        final TextView faq7_a = (TextView)findViewById(R.id.faq7_a);
        TextView faq8_q = (TextView)findViewById(R.id.faq8_q);
        final TextView faq8_a = (TextView)findViewById(R.id.faq8_a);
        TextView faq9_q = (TextView)findViewById(R.id.faq9_q);
        final TextView faq9_a = (TextView)findViewById(R.id.faq9_a);

        faq1_q.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(faq1_a.getVisibility() == TextView.GONE)
                    faq1_a.setVisibility(TextView.VISIBLE);

                else if(faq1_a.getVisibility() == TextView.VISIBLE)
                    faq1_a.setVisibility(TextView.GONE);
            }
        });

        faq2_q.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(faq2_a.getVisibility() == TextView.GONE)
                    faq2_a.setVisibility(TextView.VISIBLE);

                else if(faq2_a.getVisibility() == TextView.VISIBLE)
                    faq2_a.setVisibility(TextView.GONE);
            }
        });

        faq3_q.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(faq3_a.getVisibility() == TextView.GONE)
                    faq3_a.setVisibility(TextView.VISIBLE);

                else if(faq3_a.getVisibility() == TextView.VISIBLE)
                    faq3_a.setVisibility(TextView.GONE);
            }
        });

        faq4_q.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(faq4_a.getVisibility() == TextView.GONE)
                    faq4_a.setVisibility(TextView.VISIBLE);

                else if(faq4_a.getVisibility() == TextView.VISIBLE)
                    faq4_a.setVisibility(TextView.GONE);
            }
        });

        faq5_q.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(faq5_a.getVisibility() == TextView.GONE)
                    faq5_a.setVisibility(TextView.VISIBLE);

                else if(faq5_a.getVisibility() == TextView.VISIBLE)
                    faq5_a.setVisibility(TextView.GONE);
            }
        });

        faq6_q.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(faq6_a.getVisibility() == TextView.GONE)
                    faq6_a.setVisibility(TextView.VISIBLE);

                else if(faq6_a.getVisibility() == TextView.VISIBLE)
                    faq6_a.setVisibility(TextView.GONE);
            }
        });

        faq7_q.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(faq7_a.getVisibility() == TextView.GONE)
                    faq7_a.setVisibility(TextView.VISIBLE);

                else if(faq7_a.getVisibility() == TextView.VISIBLE)
                    faq7_a.setVisibility(TextView.GONE);
            }
        });

        faq8_q.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(faq8_a.getVisibility() == TextView.GONE)
                    faq8_a.setVisibility(TextView.VISIBLE);

                else if(faq8_a.getVisibility() == TextView.VISIBLE)
                    faq8_a.setVisibility(TextView.GONE);
            }
        });

        faq9_q.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(faq9_a.getVisibility() == TextView.GONE)
                    faq9_a.setVisibility(TextView.VISIBLE);

                else if(faq9_a.getVisibility() == TextView.VISIBLE)
                    faq9_a.setVisibility(TextView.GONE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
