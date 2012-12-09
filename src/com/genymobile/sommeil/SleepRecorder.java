
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
        private float mMouvement = 0;
        SleepPoint(long time, float mouvement) {
            mTime = time;
            mMouvement = mouvement;
        }
        
        public long getTime() {
            return mTime;
        }
        
        public float getMouvement() {
            return mMouvement;
        }
    }
    
    private static final String SD_DIR = "SleepPillow";
    private static final long FREQ = 1000 ;
    private OutputStreamWriter mStreamWriter;
    private List<SleepPoint> mData = new ArrayList<SleepPoint>();
    private List<SleepPoint> mDataWindow = new ArrayList<SleepPoint>();
    private long mLastPoint = 0;
    
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
        
        mDataWindow.add(new SleepPoint(now, value));
        if( now > mLastPoint + FREQ ) {
            mLastPoint = now;
            int nb = mDataWindow.size();
            float total = 0;
            for(SleepPoint pt : mDataWindow) {
                total += pt.getMouvement();
            }
            float average = total / nb;            
            mDataWindow.clear();
            
            synchronized (mData) {
                mData.add(new SleepPoint(now, average));
            }
        
            try {
                mStreamWriter.write(String.format("%d %f\n", now, average));
                mStreamWriter.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
