package com.example.finddifference;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class FindDifference1 extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView1, imageView2;
    private FrameLayout imageFrame1, imageFrame2;
    private ImageButton exit_button, retry_button, hintButton;
    private Button resumeButton, nextLevelButton;
    private LinearLayout pauseMenu;
    private RelativeLayout successLayout;
    private List<PointF> differences;
    private List<PointF> foundDifferences;
    private List<View> circleViews;
    private ScaleGestureDetector scaleGestureDetector1, scaleGestureDetector2;
    private float scaleFactor1 = 1.0f, scaleFactor2 = 1.0f;
    private MediaPlayer wrongSound;
    private MediaPlayer correctSound;
    private Vibrator vibrator;
    private RelativeLayout mainLayout;
    
    // 倒計時相關
    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 30000; // 30秒
    private boolean timerRunning;
    
    // 分數相關
    private int score = 0;
    private int stars = 0;
    private TextView scoreTextView;
    private ImageView star1, star2, star3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_difference1);

        mainLayout = findViewById(R.id.main_layout);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageFrame1 = findViewById(R.id.image_frame1);
        imageFrame2 = findViewById(R.id.image_frame2);
        exit_button = findViewById(R.id.exit_button);
        retry_button = findViewById(R.id.retry_button);
        hintButton = findViewById(R.id.hintButton);
        pauseMenu = findViewById(R.id.pauseMenu);
        resumeButton = findViewById(R.id.resumeButton);
        successLayout = findViewById(R.id.successLayout);
        nextLevelButton = findViewById(R.id.nextLevelButton);
        timerTextView = findViewById(R.id.timerTextView);
        scoreTextView = findViewById(R.id.score_text);
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);

        differences = new ArrayList<>();
        foundDifferences = new ArrayList<>();
        circleViews = new ArrayList<>();
        
        setupDifferencePoints();

        scaleGestureDetector1 = new ScaleGestureDetector(this, new ScaleListener(true));
        scaleGestureDetector2 = new ScaleGestureDetector(this, new ScaleListener(false));

        try {
            wrongSound = MediaPlayer.create(this, R.raw.wrong);
            correctSound = MediaPlayer.create(this, R.raw.correct);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        View.OnTouchListener touchListener1 = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector1.onTouchEvent(event);

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float x = event.getX();
                    float y = event.getY();
                    
                    float percentX = x / v.getWidth();
                    float percentY = y / v.getHeight();
                    
                    checkDifference(new PointF(percentX, percentY), v);
                } else if (event.getAction() == MotionEvent.ACTION_UP || 
                         event.getAction() == MotionEvent.ACTION_CANCEL) {
                    resetScale(true);
                }
                return true;
            }
        };

        View.OnTouchListener touchListener2 = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector2.onTouchEvent(event);

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float x = event.getX();
                    float y = event.getY();
                    
                    float percentX = x / v.getWidth();
                    float percentY = y / v.getHeight();
                    
                    checkDifference(new PointF(percentX, percentY), v);
                } else if (event.getAction() == MotionEvent.ACTION_UP || 
                         event.getAction() == MotionEvent.ACTION_CANCEL) {
                    resetScale(false);
                }
                return true;
            }
        };

        imageView1.setOnTouchListener(touchListener1);
        imageView2.setOnTouchListener(touchListener2);

        // 設置按鈕點擊事件
        exit_button.setOnClickListener(this);
        retry_button.setOnClickListener(this);
        hintButton.setOnClickListener(this);
        resumeButton.setOnClickListener(v -> togglePauseMenu());
        nextLevelButton.setOnClickListener(v -> {
            resetGame();
            successLayout.setVisibility(View.GONE);
        });
        
        startTimer();
    }
    
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.exit_button) {
            // 退出返回關卡選擇
            Intent intent = new Intent(FindDifference1.this, stage_select.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.retry_button) {
            // 重置遊戲
            resetGame();
        } else if (id == R.id.hintButton) {
            // 提示
            showHint();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (timerRunning) {
            pauseTimer();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
    
    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                timeLeftInMillis = 0;
                updateTimerText();
                showGameResult();
            }
        }.start();
        
        timerRunning = true;
    }
    
    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
    }
    
    private void resumeTimer() {
        startTimer();
    }
    
    private void updateTimerText() {
        int seconds = (int) (timeLeftInMillis / 1000);
        timerTextView.setText(String.valueOf(seconds));
    }
    
    private void setupDifferencePoints() {
        // 根據新圖片設置四個不同點的位置
        differences.add(new PointF(0.85f, 0.85f));  // 右下角的皮球 (更向右)
        differences.add(new PointF(0.08f, 0.8f));   // 左下角的花朵 (更向左)
        differences.add(new PointF(0.5f, 0.7f));   // 中間偏下方的頸圈
        differences.add(new PointF(0.30f, 0.5f));  // 左中位置的牌 (偏右一點)
    }

    private void checkDifference(PointF clickPercent, View sourceView) {
        boolean found = false;
        for (PointF point : differences) {
            if (!foundDifferences.contains(point) &&
                Math.abs(clickPercent.x - point.x) < 0.15f && 
                Math.abs(clickPercent.y - point.y) < 0.15f) {
                foundDifferences.add(point);
                drawCircle(point);
                found = true;
                if (foundDifferences.size() < differences.size()) {
                    score = 25 * foundDifferences.size();
                } else {
                    score = 100;
                }
                
                if (scoreTextView != null) {
                    scoreTextView.setText(String.valueOf(score));
                }
                
                try {
                    if (correctSound != null) {
                        if (correctSound.isPlaying()) {
                            correctSound.stop();
                            correctSound.prepare();
                        }
                        correctSound.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                if (foundDifferences.size() == differences.size()) {
                    pauseTimer();
                    stars = 3;
                    showGameResult();
                }
                break;
            }
        }

        if (!found) {
            score = Math.max(0, score - 5); // 錯誤點擊扣5分，最低0分
            try {
                if (wrongSound != null) {
                    if (wrongSound.isPlaying()) {
                        wrongSound.stop();
                        wrongSound.prepare();
                    }
                    wrongSound.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            if (vibrator != null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(200);
                }
            }
        }
    }
    
    private void calculateStars() {
        int foundCount = foundDifferences.size();
        
        if (foundCount == 4) {
            stars = 3;
        } else if (foundCount == 3) {
            stars = 2;
        } else if (foundCount >= 1) {
            stars = 1;
        } else {
            stars = 0;
        }
    }
    
    private void showGameResult() {
        calculateStars();
        
        // 不再使用successLayout，而是啟動新的勝利畫面
        Intent intent = new Intent(this, FindDifferenceWin1.class);
        intent.putExtra("level", 3); // 第3關
        intent.putExtra("score", score);
        intent.putExtra("stars", stars);
        startActivity(intent);
    }

    private void drawCircle(PointF pointPercent) {
        drawCircleInFrame(imageFrame1, pointPercent);
        drawCircleInFrame(imageFrame2, pointPercent);
    }

    private void drawCircleInFrame(FrameLayout frame, PointF pointPercent) {
        View circleView = new View(this) {
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(10);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - 12, paint);
            }
        };
        
        int circleSize = 200;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(circleSize, circleSize);
        
        params.leftMargin = (int)(pointPercent.x * frame.getWidth() - circleSize / 2);
        params.topMargin = (int)(pointPercent.y * frame.getHeight() - circleSize / 2);
        
        frame.addView(circleView, params);
        circleViews.add(circleView);
        
        circleView.bringToFront();
        frame.invalidate();
    }

    private void togglePauseMenu() {
        if (pauseMenu.getVisibility() == View.VISIBLE) {
            pauseMenu.setVisibility(View.GONE);
            if (!timerRunning && timeLeftInMillis > 0) {
                resumeTimer();
            }
        } else {
            pauseMenu.setVisibility(View.VISIBLE);
            if (timerRunning) {
                pauseTimer();
            }
        }
    }

    private void resetGame() {
        foundDifferences.clear();
        score = 0;
        
        for (View view : circleViews) {
            if (view != null && view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }
        circleViews.clear();
        
        scaleFactor1 = 1.0f;
        scaleFactor2 = 1.0f;
        imageView1.setScaleX(1.0f);
        imageView1.setScaleY(1.0f);
        imageView2.setScaleX(1.0f);
        imageView2.setScaleY(1.0f);
        pauseMenu.setVisibility(View.GONE);
        successLayout.setVisibility(View.GONE);
        
        // 重置計時器
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timeLeftInMillis = 60000;
        startTimer();
    }

    private void showHint() {
        for (PointF point : differences) {
            if (!foundDifferences.contains(point)) {
                foundDifferences.add(point);
                drawCircle(point);
                // 提示後更新分數
                if (foundDifferences.size() < differences.size()) {
                    score = 20 * foundDifferences.size() + 5; // 提示時每個20分，額外+5
                } else {
                    score = 100; // 找到全部時設為100分
                }
                // 更新得分顯示
                if (scoreTextView != null) {
                    scoreTextView.setText(String.valueOf(score));
                }
                break;
            }
        }
        
        if (foundDifferences.size() == differences.size()) {
            pauseTimer();
            stars = 3; // 找到全部時設為三星
            showGameResult();
        }
    }

    private void resetScale(boolean isFirstImage) {
        if (isFirstImage) {
            scaleFactor1 = 1.0f;
            imageView1.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200);
        } else {
            scaleFactor2 = 1.0f;
            imageView2.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200);
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private boolean isFirstImage;
        
        public ScaleListener(boolean isFirstImage) {
            this.isFirstImage = isFirstImage;
        }
        
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (isFirstImage) {
                scaleFactor1 *= detector.getScaleFactor();
                scaleFactor1 = Math.max(0.5f, Math.min(scaleFactor1, 2.0f));
                
                imageView1.setScaleX(scaleFactor1);
                imageView1.setScaleY(scaleFactor1);
            } else {
                scaleFactor2 *= detector.getScaleFactor();
                scaleFactor2 = Math.max(0.5f, Math.min(scaleFactor2, 2.0f));
                
                imageView2.setScaleX(scaleFactor2);
                imageView2.setScaleY(scaleFactor2);
            }
            return true;
        }
    }
} 