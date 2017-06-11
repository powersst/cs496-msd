package com.example.android.myapplication;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String LIFECYCLE_EVENTS_KEY = "lifecycleEvents";

    private static final ArrayList<String> mLifeCycleEvents;

    private TextView mLifecycleEventsTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLifecycleEventsTV = (TextView)findViewById(R.id.tv_lifecycle_events);

        if (savedInstanceState != null && savedInstanceState.containsKey(LIFECYCLE_EVENTS_KEY)) {
            mLifecycleEventsTV.setText(savedInstanceState.getString(LIFECYCLE_EVENTS_KEY));
        }

        logAndDisplayLifecycleEvent("onCreate");
    }

    private void logAndDisplayLifecycleEvent(String lifecycleEvent) {
        Log.d(TAG, "Lifecycle Event: " + lifecycleEvent);
        mLifecycleEventsTV.append(lifecycleEvent + "\n");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        logAndDisplayLifecycleEvent("onSaveInstanceState");
        String lifecycleEventsText = mLifecycleEventsTV.getText().toString();
        outState.putString(LIFECYCLE_EVENTS_KEY, lifecycleEventsText);
    }

    @Override
    protected void onStart() {
        super.onStart();
        logAndDisplayLifecycleEvent("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logAndDisplayLifecycleEvent("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logAndDisplayLifecycleEvent("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        logAndDisplayLifecycleEvent("onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logAndDisplayLifecycleEvent("onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logAndDisplayLifecycleEvent("onDestroy");
    }
}
