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
import android.content.Context;
import android.media.AudioManager;
import android.database.ContentObserver;
import android.provider.Settings;

public class c4_bomb_game2 extends AppCompatActivity implements Animation.AnimationListener, View.OnClickListener {
    Animation anim_move_left, anim_shaking;
    ImageView c4_main, c4_wire, c4_cutter, c4_bomb, c4_wire_cut;
    TextView sign_text, choice1, choice2, choice3, choice4, sign_result_text,textView;
    View scrolling_line, detect_line1, detect_line2, detect_line3, detect_line4;
    ImageButton exit_button, retry_button, hint_button;
    ObjectAnimator scrollingAnimator; // Store the animator reference
    private int correctAnswerLine = 3; // the correct choice of line
    private int correctAnswerCount = 0; // 跟踪正确答案的数量
    private final int REQUIRED_CORRECT_ANSWERS = 2; // 需要连续答对的次数
    private int hintUsageCount = 0; // 记录提示使用次数
    private int currentScore = 5000; // 假设初始分数为5000
    private int speed = 100;
    private AudioManager audioManager;
    private int previousVolume = -1;
    private VolumeContentObserver volumeContentObserver;
    private MediaPlayer mediaPlayer; // 添加 MediaPlayer 用於播放 BGM
    private MediaPlayer bomb_effect;
    private boolean touchEnabled = true; // 跟踪触摸是否可用
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.c4_bomb_game1);

        // 初始化背景音樂
        setupBackgroundMusic();

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

        // 初始化音频管理器
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        previousVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        // 设置音量变化监听
        volumeContentObserver = new VolumeContentObserver(new Handler());
        getContentResolver().registerContentObserver(
                Settings.System.CONTENT_URI, true, volumeContentObserver);

        Intent intent = getIntent();
    }

    // 初始化並播放背景音樂
    private void setupBackgroundMusic() {
        mediaPlayer = MediaPlayer.create(this, R.raw.ticking_bomb); // 假設音樂文件在 res/raw/casual_bgm.mp3
        mediaPlayer.setLooping(true); // 設置無限循環
        mediaPlayer.start(); // 開始播放
    }

    // 在活動暫停時暫停音樂
    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause(); // 暫停音樂
        }
    }

    // 在活動恢復時繼續播放音樂
    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start(); // 恢復播放
        }
    }

    // 在活動銷毀時停止並釋放音樂資源
    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(volumeContentObserver);
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // 停止播放
            mediaPlayer.release(); // 釋放資源
            mediaPlayer = null;
        }
    }

    // 音量變化監聽器
    private class VolumeContentObserver extends ContentObserver {
        public VolumeContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (currentVolume != previousVolume) {
                previousVolume = currentVolume;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkVolumeChange(currentVolume);
                    }
                });
            }
        }
    }

    private void animateLine(View line) {
        scrollingAnimator = ObjectAnimator.ofFloat(line, "translationX", -350f, 345f);
        scrollingAnimator.setDuration(speed); // 使用當前的 speed 值
        scrollingAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        scrollingAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        scrollingAnimator.start();
    }

    private void stopScrollingLine() {
        if (scrollingAnimator != null) {
            scrollingAnimator.pause();
            detectCollision();
        }
    }

    private void detectCollision() {
        Rect scrollingLineRect = new Rect();
        scrolling_line.getGlobalVisibleRect(scrollingLineRect);

        Rect detectLine1Rect = new Rect();
        Rect detectLine2Rect = new Rect();
        Rect detectLine3Rect = new Rect();
        Rect detectLine4Rect = new Rect();

        detect_line1.getGlobalVisibleRect(detectLine1Rect);
        detect_line2.getGlobalVisibleRect(detectLine2Rect);
        detect_line3.getGlobalVisibleRect(detectLine3Rect);
        detect_line4.getGlobalVisibleRect(detectLine4Rect);

        if (Rect.intersects(scrollingLineRect, detectLine1Rect)) {
            handleLineSelection(1);
        } else if (Rect.intersects(scrollingLineRect, detectLine2Rect)) {
            handleLineSelection(2);
        } else if (Rect.intersects(scrollingLineRect, detectLine3Rect)) {
            handleLineSelection(3);
        } else if (Rect.intersects(scrollingLineRect, detectLine4Rect)) {
            handleLineSelection(4);
        } else {
            handleWrongAnswer();
        }
    }

    private void handleLineSelection(int selectedLine) {
        Log.d("LineDetection", "Selected line: " + selectedLine);
        sign_result_text.setText("You choose " + selectedLine);

        if (selectedLine == correctAnswerLine) {
            handleCorrectAnswer();
        } else {
            handleWrongAnswer();
        }
    }

    private void handleCorrectAnswer() {
        touchEnabled = false; // 禁用触摸
        correctAnswerCount++;
        c4_wire.setVisibility(View.VISIBLE);
        c4_wire_cut.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "Correct! (" + correctAnswerCount + "/" + REQUIRED_CORRECT_ANSWERS + ")", Toast.LENGTH_SHORT).show();
        sign_result_text.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

        if (correctAnswerCount >= REQUIRED_CORRECT_ANSWERS) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sign_result_text.setText("You win!");
                    Intent intent = new Intent(c4_bomb_game2.this, winning_stage.class);
                    intent.putExtra("currentLevel", 12); // Use "level" key as defined in winning_stage2
                    intent.putExtra("score", currentScore);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        } else {
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

    private void continueGame() {
        animateLine(scrolling_line);
        correctAnswerLine = (int)(Math.random() * 4) + 1;
        updateQuestion();
        c4_wire.setVisibility(View.VISIBLE);
        c4_wire_cut.setVisibility(View.INVISIBLE);
    }

    private void handleWrongAnswer() {
        touchEnabled = false; // 禁用触摸
        correctAnswerCount = 0; // Reset correct answer count
        Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
        sign_result_text.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

        cutter_cut(); // Trigger cutting animation

        // Ensure visibility is changed in a delayed manner to allow time for animations

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bomb_explode(); // Trigger explosion effect
                // Play explosion sound effect
                if (bomb_effect != null) {
                    bomb_effect.release(); // Release previous effect if it exists
                }
                bomb_effect = MediaPlayer.create(c4_bomb_game2.this, R.raw.bomb_explosion);
                bomb_effect.start(); // Start playing explosion sound

                // Release resources after playback
                bomb_effect.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release(); // Release MediaPlayer resources when done
                    }
                });


            }
        }, 500); // Delay for half a second to allow for visual effects
        // Reset game after a short delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetGame(); // Reset game state after bomb explosion
                touchEnabled = true; // 禁用触摸
            }
        }, 2000); // Delay for 2 seconds before resetting
    }

    private void resetGame() {
        correctAnswerCount = 0;
        animateLine(scrolling_line);
        updateQuestion();
        loadUI();
        correctAnswerLine = (int)(Math.random() * 4) + 1;
    }

    private void updateQuestion() {
        int num1 = (int)(Math.random() * 100);
        int num2 = (int)(Math.random() * 100);
        int correctAnswer = num1 + num2;

        sign_text.setText(num1 + " + " + num2 + " = ?");

        int[] answers = new int[4];
        answers[correctAnswerLine - 1] = correctAnswer;

        for (int i = 0; i < 4; i++) {
            if (i != correctAnswerLine - 1) {
                int wrongAnswer;
                do {
                    wrongAnswer = correctAnswer + (int)(Math.random() * 20) - 10;
                } while (wrongAnswer == correctAnswer);
                answers[i] = wrongAnswer;
            }
        }

        choice1.setText(String.valueOf(answers[0]));
        choice2.setText(String.valueOf(answers[1]));
        choice3.setText(String.valueOf(answers[2]));
        choice4.setText(String.valueOf(answers[3]));

        sign_result_text.setText("Progress: " + correctAnswerCount + "/" + REQUIRED_CORRECT_ANSWERS);
    }

    private void checkVolumeChange(int currentVolume) {
        speed = 100 + (currentVolume * 35);
        if (speed < 100) speed = 100;
        if (speed > 2000) speed = 2000;

        if (scrollingAnimator != null) {
            scrollingAnimator.cancel();
            animateLine(scrolling_line);
        }
    }

    private void loadAnimations() {
        anim_move_left = AnimationUtils.loadAnimation(this, R.anim.move_left);
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
        sign_result_text = findViewById(R.id.sign_result_text);
        sign_result_text = findViewById(R.id.sign_result_text);
        sign_result_text.setText("Listen the voice");
        sign_result_text.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
        sign_result_text.setTextColor(getResources().getColor(android.R.color.white)); // Ensure

        exit_button = findViewById(R.id.exit_button);
        exit_button.setOnClickListener(this);

        retry_button = findViewById(R.id.retry_button);
        retry_button.setOnClickListener(this);

        hint_button = findViewById(R.id.hint_button);
        hint_button.setOnClickListener(this);

        hintUsageCount = 0;
        currentScore = 5000;
        speed = 100;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sign_result_text.setBackground(null);
                sign_result_text.setTextColor(getResources().getColor(android.R.color.black)); // Ensure
                updateQuestion();
            }
        }, 1000);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    private void bomb_explode() {
        c4_bomb.setVisibility(View.VISIBLE);
        anim_shaking.setDuration(1000);
        anim_shaking.setAnimationListener(this);
        c4_bomb.startAnimation(anim_shaking);
    }

    private void cutter_cut() {
        anim_move_left.setDuration(1000);
        anim_move_left.setAnimationListener(this);
        c4_cutter.startAnimation(anim_move_left);
        c4_wire.setVisibility(View.INVISIBLE);
        c4_wire_cut.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.exit_button) {
            Intent intent = new Intent(c4_bomb_game2.this, MainMenu.class);
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.retry_button) {
            resetGame();
        } else if (v.getId() == R.id.hint_button) {
            provideHint();
        }
    }

    private void provideHint() {
        if (hintUsageCount < 4) {
            hintUsageCount++;
            currentScore -= 1500;

            if (hintUsageCount == 1) {
                Toast.makeText(this, "Volume change speed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "The correct answer is: " + getCorrectAnswer(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No more hints available!", Toast.LENGTH_SHORT).show();
        }
    }

    private int getCorrectAnswer() {
        int num1 = Integer.parseInt(sign_text.getText().toString().split(" ")[0]);
        int num2 = Integer.parseInt(sign_text.getText().toString().split(" ")[2]);
        return num1 + num2;
    }
}