package com.example.qiang.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.example.qiang.R;
import com.example.qiang.entity.Mainlottery;
import com.example.qiang.entity.Theme;
import com.example.qiang.http.HttpUtil;
import com.example.qiang.tool.BundleTemp;
import com.example.qiang.util.ToastUtil;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EditHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText themetext;

    private TimePickerView pvTime;

    private TextView start_day;

    private Long themeId;

    private Theme theme;

    private Calendar selectedDate;

    private  Calendar startDate;

    private Calendar endDate;

    protected ActionBar mActionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_home);
        themetext = findViewById(R.id.theme);
        start_day = findViewById(R.id.start_day);
        start_day.setOnClickListener(this);
        selectedDate = Calendar.getInstance();
         startDate = Calendar.getInstance();
        startDate.set(2013, 0, 23);
        endDate = Calendar.getInstance();
        endDate.set(2029, 11, 28);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.save).setOnClickListener(this);
        Intent intent = getIntent();
        if (intent.hasExtra("themeId")) {
            themeId = intent.getLongExtra("themeId", -1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (themeId != null) {
            loadNote();
        }else {
            pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {//选中事件回调
                    // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                    TextView btn = (TextView) v;
                    btn.setText(getTimes(date));
                }
            })
                    .setType(new boolean[]{true, true, true, false, false, false})
                    .setLabel("年", "月", "日", "时", "", "")
                    .isCenterLabel(true)
                    .setDividerColor(Color.DKGRAY)
                    .setContentSize(21)
                    .setDate(selectedDate)
                    .setRangDate(startDate, endDate)
                    .setDecorView(null)
                    .build();
        }
    }

    private void loadNote() {
        Map<String,String> param = new HashMap<String,String>();
        param.put("id",themeId.toString());
        HttpUtil.sendOkHttpRequestText(HttpUtil.BASE_URL + "theme/findthemebyid.do", param, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final Theme note = new Gson().fromJson(response.body().string(),Theme.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setNote(note);
                    }
                });

            }
        });
    }

    private void setNote(Theme note) {
        this.theme = note;
        themetext.setText(note.getTheme());
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd ");
       start_day.setText(note.getDate());


        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                TextView btn = (TextView) v;
                btn.setText(getTimes(date));
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "", "")
                .isCenterLabel(true)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDecorView(null)
                .build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_day:
                pvTime.show(start_day);
                break;
            case R.id.save:
                saveNote();
                break;
        }
    }


    private void saveNote() {
        if (theme == null) {
            theme = new Theme();

        }else {
            theme.setId(Integer.valueOf(themeId.toString()));
        }

        Date curDate = new Date(System.currentTimeMillis());
        theme.setTheme(themetext.getText().toString());
        theme.setDate(start_day.getText().toString());
        Gson gson = new Gson();
        JSONObject jsonObject = JSONObject.parseObject(gson.toJson(theme));
        HttpUtil.sendOkHttpRequestJson(HttpUtil.BASE_URL + "theme/themesave.do", jsonObject, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final JSONObject json = JSONObject.parseObject(response.body().string());
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                if (json.getBoolean("errres")) {
                                    ToastUtil.showMsg( EditHomeActivity.this,"成功");
                                    finish();
                                }else {
                                    ToastUtil.showMsg ( EditHomeActivity.this,"失败");
                                }
                            }
                        }
                );
            }
        });
    }


    private String getTimes(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
