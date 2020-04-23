package com.msd.finalproject.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.msd.finalproject.CameraActivity;
import com.msd.finalproject.LoginActivity;
import com.msd.finalproject.R;
import com.msd.finalproject.helper.DataBaseHelper;

import java.io.ByteArrayOutputStream;

/**
 * CameraFragment to allow user
 * to manage activities after
 * logging into the application
 */
public class CameraFragment extends Fragment {
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    Button btnSaveImg, btnViewImg, btnStartTrack, btnStopTrack, btnLogout, btnShowRecent;
    //Bitmap photo;
    String photo;
    DataBaseHelper databaseHandler;
    Bitmap theImage;
    SharedPreferences sp;
    private String loggedInUser = null;

    Long activityId = null;
    Boolean isActivityStarted = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        loggedInUser = getArguments().getString("loggedInUserId");
        // imageView =view. findViewById(R.id.imageView1);
        btnSaveImg = view.findViewById(R.id.btnSaveImg);
        btnViewImg = view.findViewById(R.id.btnViewImg);
        btnStartTrack = view.findViewById(R.id.btnStartTrack);
        btnStopTrack = view.findViewById(R.id.btnStopTrack);
        btnShowRecent = view.findViewById(R.id.btnShowRecent);
        btnLogout = view.findViewById(R.id.btnLogout);
        databaseHandler = new DataBaseHelper(getContext());

        /**
         * Ask for permission and when persmissions are granted
         * allow user to take pictures
         */
        btnSaveImg.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {

                        if (activityId == null) {

                            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                            } else {
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);

                            }

                        } else {
                            showActivityIdExistsAlert();
                        }
                    }
                });


        /**
         * Initialise LocalFragment
         * to diplay user's recently captured photo
         */
        btnViewImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityId != null) {

                    ((CameraActivity) getActivity()).loadFragment(new LocalFragment(), String.valueOf(activityId), true);

                } else {

                    showActivityIdNotExistsAlert();
                }
            }
        });

        /**
         * Initialise FragmentTrackLocation
         * to store starting coordinates
         */
        btnStartTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityId != null && !isActivityStarted) {
                    isActivityStarted = true;
                    ((CameraActivity) getActivity()).loadFragment(new FragmentTrackLocation(), String.valueOf(activityId), true);

                } else {

                    if (isActivityStarted) {

                        showActivityStartedsAlert();

                    } else {

                        showActivityIdNotExistsAlert();

                    }
                }
            }
        });

        /**
         * Initialise MapFragment
         * shows user's starting and destination places
         * by means of plyline on google map
         */
        btnShowRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityId != null && !isActivityStarted) {

                    ((CameraActivity) getActivity()).loadFragment(new MapFragment(), String.valueOf(activityId), true);

                } else {

                    if (isActivityStarted) {

                        showActivityStartedsAlert();

                    } else {

                        showActivityIdNotExistsAlert();

                    }

                }
            }
        });

        /**
         * Initialise FragmentTrackLocation
         * to store destination coordinates
         */
        btnStopTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityId != null && isActivityStarted) {
                    isActivityStarted = false;
                    ((CameraActivity) getActivity()).loadFragment(new FragmentTrackLocation(), String.valueOf(activityId), true);

                } else {
                    showActivityIdNotExistsAlert();
                }
            }
        });

        /**
         *Code to perfrom logout functionality
         */
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp = getActivity().getSharedPreferences("login", getContext().MODE_PRIVATE);
                sp.edit().putBoolean("logged", false).apply();
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    /**
     * Initialize user's new activity records to Activity table
     */
    private void setDataToDataBase() {
        activityId = databaseHandler.storeActivityDetails(loggedInUser, getEncodedString(theImage));

        if (activityId < 0) {
            Toast.makeText(getContext(), "Something went wrong. Please try again later...", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Add successful", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Show Alert if Activity Id already exists
     * before starting new activity
     * used to validate user's operations
     * to reduce conflicts between modules
     */
    private void showActivityIdExistsAlert() {
        new AlertDialog.Builder(this.getContext())
                .setTitle("Activity Started")
                .setMessage("Activity Has already been started. Please Initiate Tracking")
                .setPositiveButton("OK", null)
                .create()
                .show();
    }

    /**
     * Show Alert if Activity Is already running
     * before starting new activity
     * used to validate user's operations
     * to reduce conflicts between modules
     */
    private void showActivityStartedsAlert() {
        new AlertDialog.Builder(this.getContext())
                .setTitle("Activity Started")
                .setMessage("Please wait!! Activity is in Progress")
                .setPositiveButton("OK", null)
                .create()
                .show();
    }

    /**
     * Show Alert if Activity Id doesn't exists
     * or if the activity hasn't been started, yet
     */
    private void showActivityIdNotExistsAlert() {
        new AlertDialog.Builder(this.getContext())
                .setTitle("Activity Not Created")
                .setMessage("Please Capture your Photo before initiating Tracking or Viewing Image.")
                .setPositiveButton("OK", null)
                .create()
                .show();
    }

    /**
     * Reuqesting for premissons
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getActivity(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Start an activity for result
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            theImage = (Bitmap) data.getExtras().get("data");
            photo = getEncodedString(theImage);
            setDataToDataBase();
        }
    }

    /**
     * Convert Bitmap to String
     *
     * @param bitmap
     * @return base64 format string
     */
    private String getEncodedString(Bitmap bitmap) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);

       /* or use below if you want 32 bit images

        bitmap.compress(Bitmap.CompressFormat.PNG, (0–100 compression), os);*/
        byte[] imageArr = os.toByteArray();

        return Base64.encodeToString(imageArr, Base64.URL_SAFE);

    }


}
