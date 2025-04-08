package com.stry.group_proj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // 初始化 UI 元素
        ImageView userPic = findViewById(R.id.user_pic);
        TextView userName = findViewById(R.id.user_name);
        Button logoutButton = findViewById(R.id.logout_button);
        TextView gameName = findViewById(R.id.game_name);
        Button stageButton = findViewById(R.id.stage_button);
        Button legendBoardButton = findViewById(R.id.legend_board_button);
        Button settingButton = findViewById(R.id.setting);

        // 設置靜態數據
        userName.setText("Player1");
        gameName.setText("Group-2-Game");

        // Logout 按鈕
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, null);//LoginActivity.class
                startActivity(intent);
                finish();
            }
        });

        // Stage 按鈕
        stageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, stage_select.class);
                startActivity(intent);
            }
        });

        // Legend Board 按鈕（假設未實現，跳轉到 c4_bomb_game1 作為示例）
        legendBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, null);
                startActivity(intent);
            }
        });

        // Setting 按鈕（假設未實現，跳轉到 c4_bomb_game2 作為示例）
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, null);
                startActivity(intent);
            }
        });
    }
}