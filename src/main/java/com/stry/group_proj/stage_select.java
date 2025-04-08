package com.stry.group_proj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class stage_select extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_select);

        // 初始化 Home Button
        ImageButton homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(stage_select.this, MainMenu.class); // 假設主菜單活動名為 MainMenu
                startActivity(intent);
                finish(); // 關閉當前活動
            }
        });

        // 初始化關卡按鈕
        Button level1Button = findViewById(R.id.level1_button);
        Button level2Button = findViewById(R.id.level2_button);
        Button level3Button = findViewById(R.id.level3_button);
        Button level4Button = findViewById(R.id.level4_button);
        Button level5Button = findViewById(R.id.level5_button);
        Button level6Button = findViewById(R.id.level6_button);
        Button level7Button = findViewById(R.id.level7_button);
        Button level8Button = findViewById(R.id.level8_button);
        Button level9Button = findViewById(R.id.level9_button);
        Button level10Button = findViewById(R.id.level10_button);
        Button level11Button = findViewById(R.id.level11_button);
        Button level12Button = findViewById(R.id.level12_button);

        // 設置每個按鈕的點擊事件（目前為空 Intent，你可以填入具體活動）
        level1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(stage_select.this, null); // Level 1 活動（待填入）
                startActivity(intent);
            }
        });

        level2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(stage_select.this, null); // Level 2 活動（待填入）
                startActivity(intent);
            }
        });

        level3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(stage_select.this, null); // Level 3 活動（待填入）
                startActivity(intent);
            }
        });

        level4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(stage_select.this, null); // Level 4 活動（待填入）
                startActivity(intent);
            }
        });

        level5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(stage_select.this, null); // Level 5 活動（待填入）
                startActivity(intent);
            }
        });

        level6Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(stage_select.this, null); // Level 6 活動（待填入）
                startActivity(intent);
            }
        });

        level7Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(stage_select.this, null); // Level 7 活動（待填入）
                startActivity(intent);
            }
        });

        level8Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(stage_select.this, null); // Level 8 活動（待填入）
                startActivity(intent);
            }
        });

        level9Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(stage_select.this, null); // Level 9 活動（待填入）
                startActivity(intent);
            }
        });

        level10Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(stage_select.this, null); // Level 10 活動（待填入）
                startActivity(intent);
            }
        });

        level11Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(stage_select.this, c4_bomb_game1.class); // Level 11 活動（待填入）
                startActivity(intent);
            }
        });

        level12Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(stage_select.this, c4_bomb_game2.class); // Level 12 活動（待填入）
                startActivity(intent);
            }
        });
    }
}