
package com.genymobile.sommeil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;



public class SleepRecorder {
    public class SleepPoint {
        private long mTime = 0;
        private int mMouvement = 0;
        SleepPoint(long time, int mouvement) {
            mTime = time;
            mMouvement = mouvement;
        }
        
        public long getTime() {
            return mTime;
        }
        
        public int getMouvement() {
            return mMouvement;
        }
    }
    
    private final String SD_DIR = "SleepPillow";
    private OutputStreamWriter mStreamWriter;
    private List<SleepPoint> mData = new ArrayList<SleepPoint>();

    public SleepRecorder(Context context, String name) {
        File parent = new File(context.getExternalCacheDir(), SD_DIR);
        parent.mkdirs();
        File file = new File(parent, name);
        try {
            mStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void addValue(int value) {
        long now = System.currentTimeMillis();
        try {
            mStreamWriter.write(String.format("%d %d\n", now, value));
            mStreamWriter.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        synchronized (mData) {
            mData.add(new SleepPoint(now, value));
        }
    }

    public boolean hasValue() {
        synchronized (mData) {
            return ! mData.isEmpty();
        }
    }

    public SleepPoint getValue() {
        synchronized (mData) {
            SleepPoint ret = mData.get(0);
            mData.remove(0);
            return ret;
        }
    }
}
