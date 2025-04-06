package com.example.projectmodel;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Task {
    private int key;
    private String date;
    private String title;
    private String appx_time;
    private String near_activity;
    private String time_time;

    public static final MediaType JSON = MediaType.get("application/json");
    public static final  String promp="Answer must be in format of minute or hour only. You will be provided with a task in to do list, and your task is to tell how much approximate time it will take to complete the task.";
    OkHttpClient client = new OkHttpClient();

    public Task() {

    }

    public Task(int key, String title, String appx_time,String near_activity, String date, String time_time) {
        this.key = key;

        this.title = title;
        this.appx_time=appx_time;
        this.near_activity = near_activity;

        this.time_time = time_time;
        this.date = date;
    }



    /**Getter Setters **/
    //Key
    public void setKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    //Title
    public void setTitle(String taskname)
    {
        this.title=taskname;
        String prompt=promp+"User:"+taskname;
        callAPI(prompt);

    }

    public String getTitle() {
        return title;
    }


    //Approx Time

    public void setappx_time(String apprxtime){


        this.appx_time=apprxtime;

    }

    public String getappx_time(){


        return appx_time;

    }

    // Nearest Activity

    public String setNear_activity() {
        return  this.near_activity = "Sample";
    }

    public String getNear_activity() {
        return near_activity;
    }

//Date

    public void setDate(String date)
    {
        try {
        SimpleDateFormat inputFormat = new SimpleDateFormat("d/M/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date date1 = inputFormat.parse(date);

        String mysqlDate = outputFormat.format(date1);
        System.out.println("mysqlDate:"+mysqlDate);
        this.date=mysqlDate;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public String getDate() {

        return date;
    }

//Time

    public void setTime_time(String time)
    {
        this.time_time=time;
    }

    public String getTime() {
        return time_time;
    }



    public int getDay() {
        return getParsedDate().get(Calendar.DAY_OF_MONTH);
    }

    public int getMonth() {
        return getParsedDate().get(Calendar.MONTH) + 1;
    }

    public int getYear() {
        return getParsedDate().get(Calendar.YEAR);
    }



    public Calendar getParsedDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            if (date != null) {
                calendar.setTime(sdf.parse(date));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }


    @NonNull
    @Override
    public String toString() {
        return "Key:"+key+" name:"+title;
    }

    public void callAPI(String question) {
        System.out.println("callAPI Entry");
        String estimatedTime = "";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "gpt-3.5-turbo-instruct");
            jsonBody.put("prompt", question);
            jsonBody.put("max_tokens", 4000);
            jsonBody.put("temperature", 0);
        } catch (JSONException e) {
            e.printStackTrace();
            return; // Exit the method if JSON creation fails
        }

        RequestBody body = RequestBody.create(JSON, jsonBody.toString());
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer ")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Failed to load response due to " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        System.out.println("Failed to load response due to " + response.body().string());
                        return;
                    }
                    String responseData = responseBody.string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONArray jsonArray = jsonObject.getJSONArray("choices");
                    String estimatedTime = jsonArray.getJSONObject(0).getString("text");
                    setappx_time(estimatedTime.trim());


                    System.out.println("In Response time: " + estimatedTime.trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("callAPI Out");
    }

}
