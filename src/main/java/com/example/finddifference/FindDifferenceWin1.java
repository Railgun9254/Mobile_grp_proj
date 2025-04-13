package com.example.finddifference;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FindDifferenceWin1 extends AppCompatActivity {

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
    private final int MAX_SCORE = 100; // 最高分數

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning_stage);

        // Initialize components
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

        // Get data from previous level
        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        int stars = intent.getIntExtra("stars", 0);

        // Set level display
        levelSign.setText("Level 1 Complete");

        // Set score display
        scoreSign.setText("Score: " + score);

        // Update stars based on star count
        if (stars > 0) {
            updateStarsDirectly(stars);
        } else {
            updateStars(score);
        }

        // Set up button actions
        setupButtons();
    }

    /**
     * Update star display based on star count directly
     * @param stars Number of stars
     */
    private void updateStarsDirectly(int stars) {
        // First star
        if (stars >= 1) {
            starNo1.setImageResource(R.drawable.star_active);
        }

        // Second star
        if (stars >= 2) {
            starNo2.setImageResource(R.drawable.star_active);
        }

        // Third star
        if (stars >= 3) {
            starNo3.setImageResource(R.drawable.star_active);
        }
    }

    /**
     * Update star display based on score percentage
     * @param score Player's score
     */
    private void updateStars(int score) {
        double scorePercentage = (double) score / MAX_SCORE;

        // First star
        if (scorePercentage >= FIRST_STAR_THRESHOLD) {
            starNo1.setImageResource(R.drawable.star_active);
        }

        // Second star
        if (scorePercentage >= SECOND_STAR_THRESHOLD) {
            starNo2.setImageResource(R.drawable.star_active);
        }

        // Third star
        if (scorePercentage >= THIRD_STAR_THRESHOLD) {
            starNo3.setImageResource(R.drawable.star_active);
        }
    }

    /**
     * Set up button click events
     */
    private void setupButtons() {
        // Replay button
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReplayButtonClicked();
            }
        });

        // Next level button
        nextLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextLevelButtonClicked();
            }
        });

        // Menu button
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenuButtonClicked();
            }
        });
    }

    /**
     * Called when replay button is clicked
     */
    protected void onReplayButtonClicked() {
        Intent intent = new Intent(this, FindDifference1.class);
        startActivity(intent);
        finish(); // Close current screen
    }

    /**
     * Called when next level button is clicked
     */
    protected void onNextLevelButtonClicked() {
        Intent intent = new Intent(this, FindDifference2.class);
        startActivity(intent);
        finish();
    }

    /**
     * Called when menu button is clicked
     */
    protected void onMenuButtonClicked() {
        Intent intent = new Intent(this, stage_select.class);
        startActivity(intent);
        finish();
    }
} 