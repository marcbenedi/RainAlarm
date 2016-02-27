package com.rainalarm.marcb.rainalarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;

public class Utility {

    static Context context;
    MainActivity a = new MainActivity();
    String name = a.PREFS_NAME;
    SharedPreferences preferences;
    static boolean NotificationCheck;

    public Utility(Context c)
    {
        context = c;
        preferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        NotificationCheck = preferences.getBoolean("notification",false);
    }

    public static void updateWeatherId
            (NotificationManager notificationManeger,View v)
    {

        String temp = new String("NULL");
        BackgroundUpdate a = new BackgroundUpdate(context);
        try {
            temp = a.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        int weatherId = -1;
        String weatherDescription = "ERROR";

        //Get the wheater id
        try {
            JSONObject apiObj = new JSONObject(temp);
            JSONArray listArr = apiObj.getJSONArray("list");
            JSONObject dayObj = (JSONObject)listArr.get(1);
            //Take the 2nd day of the forecast (tomorrow)
            JSONArray weatherArr = dayObj.getJSONArray("weather");
            JSONObject idObject = (JSONObject) weatherArr.get(0);
            weatherId = idObject.getInt("id");
            weatherDescription = idObject.getString("description");

        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage());
        }

        //Process the weatherId and do what is requested
        Utility.processWeatherId(weatherId,weatherDescription,v,notificationManeger);
    }

    public static void makeToast(View v, String s)
    {
        Toast.makeText(v.getContext(), s, Toast.LENGTH_SHORT).show();
    }

    public static Notification makeNotification(View v, String s)
    {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification noti = new Notification.Builder(v.getContext())
                .setContentTitle("Rain Alarm")
                .setContentText(s)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(soundUri)
                .build();
        return noti;
    }

    public static void processWeatherId(int weatherId,String weatherDescription,
                                        View v,NotificationManager notificationManager)
    {
        //Notification for rain alert
        int notificationId = 1;
        Boolean alarm = false;
        //NotificationID is necessary for the OS
        if (weatherId == -1 && NotificationCheck){
            //Error, means that we didnt get any resposne from the api
            Notification noti = Utility.makeNotification(v, "Problem with your settings, check city name.");
            notificationManager.notify(notificationId, noti);
        }
        else if (weatherId >= 200 && weatherId < 300 && NotificationCheck){
            //Means thunderstorm expected
            Notification noti = Utility.makeNotification(v, weatherDescription);
            notificationManager.notify(notificationId,noti);
        }
        else if (weatherId >= 300 && weatherId < 400 && NotificationCheck){
            //Means drizzle expected
            Notification noti = Utility.makeNotification(v, weatherDescription);
            notificationManager.notify(notificationId,noti);
        }
        else if (weatherId >= 500 && weatherId < 600 && NotificationCheck){
            //Means rain expected
            Notification noti = Utility.makeNotification(v, weatherDescription);
            notificationManager.notify(notificationId, noti);
        }
        else{
            //Another weather
            Notification noti = Utility.makeNotification(v, "No rain tomorrow");
            notificationManager.notify(notificationId, noti);
        }
    }
}
