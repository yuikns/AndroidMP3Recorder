
package com.czt.mp3recorder.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.czt.mp3recorder.MP3Recorder;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class MainActivity extends Activity {
    //    private MP3Recorder mRecorder = new MP3Recorder(new File(Environment.getExternalStorageDirectory(), "test.mp3"));

    private MP3Recorder mRecorder = null;
    private Logger logger = Logger.getLogger(MainActivity.class.getName());

    public MP3Recorder GetMP3Recorder() {
        if (mRecorder == null) {
            String cacheDir = this.getCacheDir().getAbsolutePath();
            logger.info("mRecorder using cache dir ===== " + cacheDir);
            mRecorder = new MP3Recorder(new File(cacheDir, "test.mp3"));
            logger.info("mRecorder initialized....");
        }
        return mRecorder;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mRecorder = GetMP3Recorder();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        Button startButton = (Button) findViewById(R.id.StartButton);
        startButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    logger.info("mRecorder starting...");
                    mRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Button stopButton = (Button) findViewById(R.id.StopButton);
        stopButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecorder.stop();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logger.info("mRecorder stopping...");
        if (mRecorder != null) {
            logger.info("mRecorder real");
            mRecorder.stop();
        }
    }
}
