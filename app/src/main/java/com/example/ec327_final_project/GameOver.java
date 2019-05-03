package com.example.ec327_final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.ImageButton;

import com.example.ec327_final_project.MainActivity;
import com.example.ec327_final_project.R;

public class GameOver extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        ImageButton restartButton = findViewById(R.id.restartButton);
        ImageButton titleButton = findViewById(R.id.titleButton);

        restartButton.setOnClickListener(this);
        titleButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.restartButton :
            {
                startActivity(new Intent(GameOver.this, GameActivity.class));
                break;
            }
            case R.id.titleButton :
            {
                startActivity(new Intent(GameOver.this, MainActivity.class));
                break;
            }
            default :
            {
                break;
            }

        }
    }
}
