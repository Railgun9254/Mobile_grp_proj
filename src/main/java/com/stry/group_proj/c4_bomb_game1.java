package com.stry.group_proj;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class c4_bomb_game1 extends AppCompatActivity implements Animation.AnimationListener , View.OnClickListener {
    Animation anim_move_left,anim_shaking;
    ImageView c4_main, c4_wire, c4_cutter, c4_bomb, c4_wire_cut;
    TextView sign_text,choice1,choice2,choice3,choice4,sign_result_text;
    View scrolling_line,detect_line1,detect_line2,detect_line3,detect_line4;
    ImageButton exit_button,retry_button,hint_button;
    ObjectAnimator scrollingAnimator; // Store the animator reference
    private int correctAnswerLine = 3; // the correct choice of line
    private int correctAnswerCount = 0; // 跟踪正确答案的数量
    private final int REQUIRED_CORRECT_ANSWERS = 2; // 需要连续答对的次数
    private int hintUsageCount = 0; // 记录提示使用次数
    private int currentScore = 5000; // 假设初始分数为5000
    private MediaPlayer mediaPlayer; // 添加 MediaPlayer 用於播放 BGM
    private MediaPlayer bomb_effect;
    private boolean touchEnabled = true; // 跟踪触摸是否可用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.c4_bomb_game1);

        View scrollingLine = findViewById(R.id.scrolling_line);
        animateLine(scrollingLine);
        loadAnimations();
        loadUI();
        // 设置触摸事件监听器
        findViewById(R.id.c4_main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!touchEnabled) return false; // 如果不可用，直接返回
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchEnabled = false;
                    stopScrollingLine();
                    cutter_cut();
                    return true; // 事件处理成功
                }
                return false; // 事件未处理
            }
        });
        mediaPlayer = MediaPlayer.create(this, R.raw.ticking_bomb);
        mediaPlayer.setLooping(true); // Loop the music
        mediaPlayer.start();


    }
    @Override
    protected void onPause() {
        super.onPause();
        // Pause the MediaPlayer when the activity is paused
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume the MediaPlayer when the activity is resumed
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void animateLine(View line) {
        scrollingAnimator = ObjectAnimator.ofFloat(line, "translationX", -350f, 345f);
        scrollingAnimator.setDuration(1000); // animation time
        scrollingAnimator.setRepeatCount(ObjectAnimator.INFINITE); // infinite loop
        scrollingAnimator.setRepeatMode(ObjectAnimator.REVERSE); // looping
        scrollingAnimator.start();
    }
    private void stopScrollingLine() {
        // Check if the animator exists
        if (scrollingAnimator != null) {
            // This will pause the animation at its current position
            scrollingAnimator.pause();
            detectCollision();//detect hitbox
        }
    }
    private void detectCollision() {
        // location detect
        Rect scrollingLineRect = new Rect();
        scrolling_line.getGlobalVisibleRect(scrollingLineRect);

        // location detect
        Rect detectLine1Rect = new Rect();
        Rect detectLine2Rect = new Rect();
        Rect detectLine3Rect = new Rect();
        Rect detectLine4Rect = new Rect();

        detect_line1.getGlobalVisibleRect(detectLine1Rect);
        detect_line2.getGlobalVisibleRect(detectLine2Rect);
        detect_line3.getGlobalVisibleRect(detectLine3Rect);
        detect_line4.getGlobalVisibleRect(detectLine4Rect);

        // hitbox check
        if (Rect.intersects(scrollingLineRect, detectLine1Rect)) {
            handleLineSelection(1);
        } else if (Rect.intersects(scrollingLineRect, detectLine2Rect)) {
            handleLineSelection(2);
        } else if (Rect.intersects(scrollingLineRect, detectLine3Rect)) {
            handleLineSelection(3);
        } else if (Rect.intersects(scrollingLineRect, detectLine4Rect)) {
            handleLineSelection(4);
        } else {
            //fail detect , not possible
            handleWrongAnswer();
        }
    }
    // choice allocation
    private void handleLineSelection(int selectedLine) {
        Log.d("LineDetection", "Selected line: " + selectedLine);

        // update text
        sign_result_text.setText("You choose " + selectedLine);

        // correct check
        if (selectedLine == correctAnswerLine) {
            // correct
            handleCorrectAnswer();
        } else {
            // fail
            handleWrongAnswer();
        }
    }
    // correct ans event
    private void handleCorrectAnswer() {
        touchEnabled = false; // 禁用触摸

        correctAnswerCount++; // 增加正确答案计数
        c4_wire.setVisibility(View.VISIBLE);
        c4_wire_cut.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "Correct! (" + correctAnswerCount + "/" + REQUIRED_CORRECT_ANSWERS + ")", Toast.LENGTH_SHORT).show();
        sign_result_text.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

        if (correctAnswerCount >= REQUIRED_CORRECT_ANSWERS) {
            // 玩家已经连续答对了足够的次数
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sign_result_text.setText("You win!");
                    // 可以在这里添加胜利的动画或其他效果
                    Intent intent = new Intent(c4_bomb_game1.this, winning_stage.class);
                    // 传递关卡和分数信息
                    intent.putExtra("currentLevel", 11);
                    // 计算得分 - 这里只是一个示例，您可以根据实际情况计算分数
                    intent.putExtra("score", currentScore);

                    // 启动胜利界面
                    startActivity(intent);

                    // 可选：关闭当前界面
                    finish();
                }
            }, 1000);
        } else {
            // 还需要答对更多题目
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sign_result_text.setText("Correct! Need " + (REQUIRED_CORRECT_ANSWERS - correctAnswerCount) + " more.");
                    continueGame();
                    touchEnabled = true;
                }
            }, 500);
        }
    }

    // 继续游戏，生成新题目
    private void continueGame() {
        animateLine(scrolling_line);
        // 生成新的正确答案位置
        correctAnswerLine = (int)(Math.random() * 4) + 1;
        // 生成新的问题
        updateQuestion();
        c4_wire.setVisibility(View.VISIBLE);
        c4_wire_cut.setVisibility(View.INVISIBLE);
    }

    // Wrong ans event
    private void handleWrongAnswer() {
        correctAnswerCount = 0; // 重置正确答案计数
        Toast.makeText(this, "Wrong！", Toast.LENGTH_SHORT).show();
        sign_result_text.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

        // anim event + lose
        cutter_cut();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bomb_explode();
                // Use the outer class context to create MediaPlayer
                bomb_effect = MediaPlayer.create(c4_bomb_game1.this, R.raw.bomb_explosion); // Corrected context
                bomb_effect.start(); // Start playing

                // Optional: Release resources after playback
                bomb_effect.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release(); // Release MediaPlayer resources when done
                    }
                });
            }
        }, 500);

        // reset game delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetGame();
                touchEnabled = true;
            }
        }, 2000);
    }

    // Reset game function
    private void resetGame() {
        correctAnswerCount = 0; // 重置正确答案计数
        animateLine(scrolling_line);
        loadUI();

        // ans generate
        correctAnswerLine = (int)(Math.random() * 4) + 1;

        // question generate
        updateQuestion();
    }
    private void updateQuestion() {
        int num1 = (int)(Math.random() * 100);
        int num2 = (int)(Math.random() * 100);
        int correctAnswer = num1 + num2;

        sign_text.setText(num1 + " + " + num2 + " = ?");

        // choice setting
        int[] answers = new int[4];
        answers[correctAnswerLine - 1] = correctAnswer;

        for (int i = 0; i < 4; i++) {
            if (i != correctAnswerLine - 1) {
                // wrong ans generate
                int wrongAnswer;
                do {
                    wrongAnswer = correctAnswer + (int)(Math.random() * 20) - 10;
                } while (wrongAnswer == correctAnswer);

                answers[i] = wrongAnswer;
            }
        }

        // update text
        choice1.setText(String.valueOf(answers[0]));
        choice2.setText(String.valueOf(answers[1]));
        choice3.setText(String.valueOf(answers[2]));
        choice4.setText(String.valueOf(answers[3]));

        // 更新进度显示
        sign_result_text.setText("Progress: " + correctAnswerCount + "/" + REQUIRED_CORRECT_ANSWERS);
    }



    private void loadAnimations() {
        anim_move_left= AnimationUtils.loadAnimation(this, R.anim.move_left);
        anim_shaking = AnimationUtils.loadAnimation(this, R.anim.shaking);
    }

    private void loadUI() {
        c4_main = findViewById(R.id.c4_main);
        c4_wire = findViewById(R.id.c4_wire);
        c4_cutter = findViewById(R.id.c4_cutter);
        c4_bomb = findViewById(R.id.c4_bomb);
        c4_wire_cut = findViewById(R.id.c4_wire_cut);
        scrolling_line = findViewById(R.id.scrolling_line);
        detect_line1 = findViewById(R.id.detect_line1);
        detect_line2 = findViewById(R.id.detect_line2);
        detect_line3 = findViewById(R.id.detect_line3);
        detect_line4 = findViewById(R.id.detect_line4);

        c4_wire.setVisibility(View.VISIBLE);
        c4_wire_cut.setVisibility(View.INVISIBLE);
        c4_bomb.setVisibility(View.INVISIBLE);

        sign_text = findViewById(R.id.sign_text);
        choice1 = findViewById(R.id.choice1);
        choice2 = findViewById(R.id.choice2);
        choice3 = findViewById(R.id.choice3);
        choice4 = findViewById(R.id.choice4);
        sign_result_text= findViewById(R.id.sign_result_text);
        sign_result_text.setText("Progress: " + correctAnswerCount + "/" + REQUIRED_CORRECT_ANSWERS);

        exit_button = findViewById(R.id.exit_button);
        exit_button.setOnClickListener(this);

        retry_button = findViewById(R.id.retry_button);
        retry_button.setOnClickListener(this);

        hint_button = findViewById(R.id.hint_button);
        hint_button.setOnClickListener(this);
        hintUsageCount = 0; // 记录提示使用次数
        currentScore = 5000; // 假设初始分数为5000

        // update question
        updateQuestion();
    }
    @Override
    public void onAnimationEnd(Animation animation){

    }
    @Override
    public void onAnimationRepeat(Animation animation){

    }
    @Override
    public void onAnimationStart(Animation animation){

    }

    private void bomb_explode(){
        c4_bomb.setVisibility(View.VISIBLE);
        anim_shaking.setDuration(1000);
        anim_shaking.setAnimationListener(this);
        c4_bomb.startAnimation(anim_shaking);
    }
    private void cutter_cut(){
        anim_move_left.setDuration(1000);
        anim_move_left.setAnimationListener(this);
        c4_cutter.startAnimation(anim_move_left);
        c4_wire.setVisibility(View.INVISIBLE);
        c4_wire_cut.setVisibility(View.VISIBLE);

    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.exit_button) {
            Intent intent = new Intent(c4_bomb_game1.this, MainMenu.class);
            startActivity(intent);
            finish(); // Exit the game and return to the previous activity
        } else if (v.getId() == R.id.retry_button) {
            resetGame(); // Reset the game state
        } else if (v.getId() == R.id.hint_button) {
            provideHint(); // Show a hint to the player
        }
    }

    private void provideHint() {
        // Example hint: Show the correct answer
        if (hintUsageCount < 2) { // 假设最多使用 3 次提示
            hintUsageCount++; // 增加提示使用次数
            currentScore -= 2000; // 扣除 2000 分
            // 显示提示
            Toast.makeText(this, "The correct answer is: " + getCorrectAnswer(), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "No more hints available!", Toast.LENGTH_SHORT).show();
        }
    }

    private int getCorrectAnswer() {
        // Generate the correct answer based on the current question
        int num1 = Integer.parseInt(sign_text.getText().toString().split(" ")[0]);
        int num2 = Integer.parseInt(sign_text.getText().toString().split(" ")[2]);
        return num1 + num2; // Return the correct answer
    }



}