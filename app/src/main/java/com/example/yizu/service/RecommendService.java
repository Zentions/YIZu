package com.example.yizu.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.Toast;

import com.example.yizu.SearchActivity;
import com.example.yizu.db.HistoryRecord;
import com.example.yizu.tool.ShareStorage;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RecommendService extends Service {
    public RecommendService() {

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       updateRecommed();
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int hour = 4*60*60*1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + hour;
        Intent i = new Intent(this,RecommendService.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }
    private void updateRecommed(){
        String objectId = ShareStorage.getShareString(this,"ObjectId");
        List<HistoryRecord> Clist = DataSupport.where("objectId = ?",objectId).order("date desc").limit(100).find(HistoryRecord.class);
        int i;
        Map<String,Double> map = new HashMap<String, Double>();
        for (i=0 ;i<Clist.size();i++){
            map.put(Clist.get(i).getRecord(),0.0);
        }
        Date now =new Date();
        for (i=0 ;i<Clist.size();i++){
            map.put(Clist.get(i).getRecord(),map.get(Clist.get(i).getRecord())+1.0-(now.getTime()-Clist.get(i).getDate().getTime())/24/3600/100000);
        }
        List<Map.Entry<String,Double>> list = new ArrayList<Map.Entry<String,Double>>(map.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<String,Double>>() {
            public int compare(Map.Entry<String, Double> o1,Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        String[] strings = new String[3];
        for(i = 0; i<3; i++){
            if(i>=list.size()) strings[i] = "null";
            else  strings[i] = list.get(i).getKey();
        }
        ShareStorage.setStrings(this,"recommed",strings);
    }
}