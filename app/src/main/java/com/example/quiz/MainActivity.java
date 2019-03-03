package com.example.quiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private Button mCheatButton;

    private TextView mQuestionTextView;

    private static final String TAG ="QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;

    private boolean mIsCheater;

    private Question[] mQuestions = new Question[]{
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex = 0;

    private  void updateQuestions(){
        int question = mQuestions[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressTrue){
        boolean answerIsTrue = mQuestions[mCurrentIndex].isAnswerTrue();

        int messagesResId =0;
        if(mIsCheater){
            messagesResId = R.string.judgment_toast;
        }else{
            if(userPressTrue == answerIsTrue){
                messagesResId = R.string.correct_toast;
            }
            else{
                messagesResId=R.string.incorrect_toast;
            }
        }
        Toast.makeText(this,messagesResId,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_main);


        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);


        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                    checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex+1)% mQuestions.length;
                mIsCheater = false;
                updateQuestions();
            }
        });

        mPrevButton = (Button) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentIndex==0)return;
                mCurrentIndex = (mCurrentIndex-1)%mQuestions.length ;
                Log.d(TAG,"prev button called");
                updateQuestions();
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestions[mCurrentIndex].isAnswerTrue();
                Intent i = Cheat.newIntent(MainActivity.this, answerIsTrue);
                startActivityForResult(i,REQUEST_CODE_CHEAT);

            }
        });

        if (savedInstanceState!=null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
        }
        updateQuestions();
    }

   @Override
   public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG,"onSavedInstanceState");
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_CODE_CHEAT){
            if(data == null){
                return;
            }
            mIsCheater = Cheat.wasAnswerShown(data);
        }
    }
}