package com.msd.finalproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.msd.finalproject.fragment.CameraFragment;

public class CameraActivity extends AppCompatActivity {
    private String loggedInUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        loggedInUser = getIntent().getStringExtra("loggedInUser");
        loadFragment(new CameraFragment(), null, false);
    }

    public void loadFragment(Fragment fragment, String activityId, Boolean bool) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("loggedInUserId", loggedInUser);
        if (activityId != null)
            bundle.putString("userActivityId", activityId);
        fragment.setArguments(bundle);
        transaction.replace(R.id.frameLayout, fragment);
        if (bool)
            transaction.addToBackStack(null);
        transaction.commit();
    }
}
