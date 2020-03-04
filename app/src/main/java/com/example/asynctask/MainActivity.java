package com.example.asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    EditText mNumber;
    Button mRollButton;
    TextView mCountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNumber = findViewById(R.id.etNumber);
        mRollButton = findViewById(R.id.btnSubmit);
        mCountView = findViewById(R.id.tvCount);

        mRollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int numOfRolls = Integer.parseInt(mNumber.getText().toString());

                new ProcessDiceInBackground().execute(numOfRolls);




            }
        });
    }

    public class ProcessDiceInBackground extends AsyncTask<Integer, Integer, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(Integer.parseInt(mNumber.getText().toString()));
            dialog.show();

        }

        @Override
        protected String doInBackground(Integer... integers) {
            int ones = 0, twos = 0, threes = 0, fours = 0, fives = 0, sixes = 0, noOnTheRoll;

            String result;

            Random random = new Random();

            double currentProgress = 0;
            double previousProgress = 0;

            for (int i=0; i < integers[0]; i++) {
                currentProgress = (double) i / integers[0];

                if (currentProgress - previousProgress >= 0.03) {
                    publishProgress(i);
                    previousProgress = currentProgress;
                }

                noOnTheRoll = random.nextInt(6) + 1;

                switch (noOnTheRoll) {
                    case 1 :
                        ones++;
                        break;
                    case 2 :
                        twos++;
                        break;
                    case 3 :
                        threes++;
                        break;
                    case 4 :
                        fours++;
                        break;
                    case 5 :
                        fives++;
                        break;

                    default: sixes++;
                }
            }

            result = "Count:\n1:" + ones + "\n2:" + twos +"\n3:" + threes + "\n4:" + fours + "\n5:" + fives + "\n6:" + sixes;

            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            dialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mCountView.setText(s);

            dialog.dismiss();

            Toast.makeText(MainActivity.this, "Process Done", Toast.LENGTH_SHORT).show();


        }
    }
}
