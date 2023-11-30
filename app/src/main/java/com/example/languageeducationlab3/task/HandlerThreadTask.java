package com.example.languageeducationlab3.task;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

public class HandlerThreadTask implements Task {

    private static final String THREAD_NAME = "DOWNLOAD_WORDS_THREAD";
    private static final HandlerThread THREAD = new HandlerThread(THREAD_NAME);
    private TaskListener taskListener;
    private boolean executed;
    private static Handler handler;
    private static final Handler handlerMain = new Handler(Looper.getMainLooper());

    static{
        THREAD.start();
        handler = new Handler(THREAD.getLooper());
    }

    private DownloadWords downloadWords;
    private Runnable runnable;

    public HandlerThreadTask(DownloadWords downloadWords){this.downloadWords = downloadWords;}

    @Override
    public void execute(TaskListener listener) {
        if(executed) throw new RuntimeException("Downloading words have been already executed");
        executed = true;
        this.taskListener = listener;

        runnable = () ->{
            try {
                downloadWords.downloadWords();
                publishResult();
            }catch (Exception e){
                Log.e(THREAD_NAME,"---",e.fillInStackTrace());
            }
        };
        handler.post(runnable);
    }

    private void publishResult(){
        runOnMainThread(()->{
            if(taskListener != null) {
                taskListener.onResult();
                taskListener = null;
            }
        });
    }

    private void runOnMainThread(Runnable action){
        if(Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()){
            action.run();
        }else{
            handlerMain.post(action);
        }
    }
}
