package com.stry.group_proj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class winning_stage extends AppCompatActivity {

    private TextView levelSign;
    private TextView scoreSign;
    private ImageView starNo1;
    private ImageView starNo2;
    private ImageView starNo3;
    private ImageButton replayButton;
    private ImageButton nextLevelButton;
    private ImageButton menuButton;

    // 分數門檻
    private final double FIRST_STAR_THRESHOLD = 0.25; // 25%
    private final double SECOND_STAR_THRESHOLD = 0.60; // 60%
    private final double THIRD_STAR_THRESHOLD = 0.85; // 85%
    private final int MAX_SCORE = 5000; // 最高分數

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning_stage);

        // 初始化元件
        levelSign = findViewById(R.id.level_sign);
        scoreSign = findViewById(R.id.score_sign);
        starNo1 = findViewById(R.id.star_no_1);
        starNo2 = findViewById(R.id.star_no_2);
        starNo3 = findViewById(R.id.star_no_3);
        replayButton = findViewById(R.id.replay_button);
        nextLevelButton = findViewById(R.id.nextlevel_button);
        menuButton = findViewById(R.id.menu_button);

        starNo1.setImageResource(R.drawable.star_unactive);
        starNo2.setImageResource(R.drawable.star_unactive);
        starNo3.setImageResource(R.drawable.star_unactive);

        // 取得前一個關卡傳來的資料
        Intent intent = getIntent();
        int currentLevel = intent.getIntExtra("currentLevel", 0); // Updated key
        int score = intent.getIntExtra("score", 0);

        // 設置關卡顯示
        levelSign.setText("Level " + currentLevel + " Complete");

        // 設置分數顯示
        scoreSign.setText("Score: " + score);

        // 根據分數設置星星顯示
        updateStars(score);

        // 設置按鈕動作
        setupButtons(currentLevel);
    }

    /**
     * 根據分數更新星星顯示
     * @param score 玩家得分
     */
    private void updateStars(int score) {
        double scorePercentage = (double) score / MAX_SCORE;

        // 第一顆星
        if (scorePercentage >= FIRST_STAR_THRESHOLD) {
            starNo1.setImageResource(R.drawable.star_active);
        }

        // 第二顆星
        if (scorePercentage >= SECOND_STAR_THRESHOLD) {
            starNo2.setImageResource(R.drawable.star_active);
        }

        // 第三顆星
        if (scorePercentage >= THIRD_STAR_THRESHOLD) {
            starNo3.setImageResource(R.drawable.star_active);
        }
    }

    /**
     * 設置按鈕的點擊事件
     * @param currentLevel 目前關卡
     */
    private void setupButtons(final int currentLevel) {


        // 重新游玩按钮
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReplayButtonClicked(currentLevel);
            }
        });

        // 下一关按钮
        nextLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextLevelButtonClicked(currentLevel);
            }
        });

        // 主菜单按钮
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenuButtonClicked();
            }
        });
    }


    protected void onReplayButtonClicked(int currentLevel) {
        Intent intent = new Intent(this, c4_bomb_game1.class);
        // 如果需要传递关卡信息，可以在这里添加
        switch (currentLevel) {
            case 1:
                intent = new Intent(winning_stage.this, null);
                break;
            case 2:
                intent = new Intent(winning_stage.this, null);
                break;
            case 3:
                intent = new Intent(winning_stage.this, null);
                break;
            case 4:
                intent = new Intent(winning_stage.this, null);
                break;
            case 5:
                intent = new Intent(winning_stage.this, null);
                break;
            case 6:
                intent = new Intent(winning_stage.this, null);
                break;
            case 7:
                intent = new Intent(winning_stage.this, null);
                break;
            case 8:
                intent = new Intent(winning_stage.this, null);
                break;
            case 9:
                intent = new Intent(winning_stage.this, null);
                break;
            case 10:
                intent = new Intent(winning_stage.this, null);
                break;
            case 11:
                intent = new Intent(winning_stage.this, c4_bomb_game1.class);
                break;
            case 12:
                // 最後一個關卡的處理
                intent = new Intent(winning_stage.this, c4_bomb_game2.class);
                startActivity(intent);
                finish();
            default:
                intent = null; // 如果不在範圍內，則不進行任何操作
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
        finish(); // 關閉當前界面
    }

    /**
     * 當下一關按鈕被點擊時呼叫
     * @param currentLevel 目前關卡
     */
    /**
     * 當下一關按鈕被點擊時呼叫，根據目前的關卡來決定進入下一個關卡
     * @param currentLevel 目前關卡
     */
    protected void onNextLevelButtonClicked(int currentLevel) {
        Intent intent;

        switch (currentLevel) {
            case 1:
                intent = new Intent(winning_stage.this, null);
                break;
            case 2:
                intent = new Intent(winning_stage.this, null);
                break;
            case 3:
                intent = new Intent(winning_stage.this, null);
                break;
            case 4:
                intent = new Intent(winning_stage.this, null);
                break;
            case 5:
                intent = new Intent(winning_stage.this, null);
                break;
            case 6:
                intent = new Intent(winning_stage.this, null);
                break;
            case 7:
                intent = new Intent(winning_stage.this, null);
                break;
            case 8:
                intent = new Intent(winning_stage.this, null);
                break;
            case 9:
                intent = new Intent(winning_stage.this, null);
                break;
            case 10:
                intent = new Intent(winning_stage.this, c4_bomb_game1.class);
                break;
            case 11:
                intent = new Intent(winning_stage.this, c4_bomb_game2.class);
                break;
            case 12:
                // 最後一個關卡的處理
                intent = new Intent(winning_stage.this, MainMenu.class);
                startActivity(intent);
                finish();
            default:
                intent = null; // 如果不在範圍內，則不進行任何操作
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
        finish(); // 關閉當前界面
    }

    /**
     * 當選單按鈕被點擊時呼叫
     */
    protected void onMenuButtonClicked() {
        Intent intent = new Intent(winning_stage.this, MainMenu.class);
        startActivity(intent);
        finish();
    }
}