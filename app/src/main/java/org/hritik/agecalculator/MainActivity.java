package org.hritik.agecalculator;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    RelativeLayout dobselrel,todayselrel,resultView;
    TextView dobsel,todaysel, logoText;
    AppCompatButton calculate;
    TextView resultText;
    DatePickerDialog.OnDateSetListener onDateSetListener, onDateSetListenertoday;
    int dyear,dmonth,dday,  tyear,tmonth,tday;
    int touchCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDarkMode(getWindow());
        getSupportActionBar().hide();

        dobsel = findViewById(R.id.dobsel);
        calculate = findViewById(R.id.calculateBtn);
        todaysel = findViewById(R.id.todaysel);
        dobselrel = findViewById(R.id.dobselrel);
        todayselrel = findViewById(R.id.todayselrel);
        resultView = findViewById(R.id.resultView);
        resultText = findViewById(R.id.resultText);
        logoText = findViewById(R.id.textView);

        logoText.setOnClickListener(v -> {
            touchCount = touchCount+1;
            if(touchCount==4){
                Toast.makeText(MainActivity.this, "Designed by Hritik", Toast.LENGTH_SHORT).show();
                vibe(90);
            }else if(touchCount == 7){
                Toast.makeText(MainActivity.this, "Version 1.4", Toast.LENGTH_SHORT).show();
                vibe(90);
            }

        });


        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        todayselrel.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth,
                    onDateSetListenertoday,year,month,day);

            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.setCancelable(false);
            datePickerDialog.show();
        });

        dobselrel.setOnClickListener(v -> {  
            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,android.R.style.Theme_Holo_Dialog_MinWidth,
                    onDateSetListener,
                    year,month,day);

            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.setCancelable(false);
            datePickerDialog.show();
        });

        onDateSetListener = (view12, year12, month12, dayOfMonth) -> {
            month12 = month12 +1;
            String date = dayOfMonth+" - "+ month12 +" - "+ year12;
            dyear = year12;
            dmonth = month12;
            dday = dayOfMonth;
            dobsel.setText(date);
        };
        onDateSetListenertoday = (view1, year1, month1, dayOfMonth) -> {
            month1 = month1 +1;
            String date = dayOfMonth+" - "+ month1 +" - "+ year1;
            tyear = year1;
            tmonth = month1;
            tday = dayOfMonth;
            todaysel.setText(date);
        };

        calculate.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                calculateAge();
            }
        });
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void calculateAge() {
        try{
            LocalDate birthDate = LocalDate.of(dyear,dmonth,dday);
            LocalDate today = LocalDate.of(tyear,tmonth,tday);
            int years = Period.between(birthDate,today).getYears();
            int months = Period.between(birthDate,today).getMonths();
            int days = Period.between(birthDate,today).getDays();
            String txt = "You are now "+years+" years "+months+" months and "+days+ " days old. ";
            resultView.setVisibility(View.VISIBLE);
            if(years<0 || months<0 ||days<0){
                resultText.setText("Select correct date");
                vibe(120);
            }else{
                resultText.setText(txt);
            }
        }catch (Exception e){
            Toast.makeText(this, "Invalid Date !!", Toast.LENGTH_SHORT).show();
        }


    }

    private void vibe(int ms){
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(ms);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setDarkMode(Window window) {
        NestedScrollView relativeLayout = findViewById(R.id.mainBgNSV);
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                window.setStatusBarColor(Color.BLACK);
                relativeLayout.setBackgroundColor(Color.BLACK);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                int flags = window.getDecorView().getSystemUiVisibility();
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                window.getDecorView().setSystemUiVisibility(flags);
                window.setStatusBarColor(Color.WHITE);
                relativeLayout.setBackgroundColor(Color.WHITE);
                break;
        }
    }
}