package com.msd.finalproject.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraFragment extends Fragment {
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    Button btnSaveImg, btnViewImg, btnStartTrack, btnStopTrack, btnLogout;
    //Bitmap photo;
    String photo;
    DataBaseHelper databaseHandler;
    Bitmap theImage;
    private String loggedInUser = null;
    private SQLiteDatabase db;
    SharedPreferences sp;


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
        btnLogout = view.findViewById(R.id.btnLogout);
        databaseHandler = new DataBaseHelper(getContext());

        btnSaveImg.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                        } else {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);

                        }
                    }
                });

        btnViewImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CameraActivity) getActivity()).loadFragment(new LocalFragment(), true);
            }
        });

        btnStartTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CameraActivity) getActivity()).loadFragment(new FragmentTrackLocation(), true);
            }
        });

        btnStopTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CameraActivity) getActivity()).loadFragment(new FragmentTrackLocation(), true);
            }
        });

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

    private void setDataToDataBase() {
        db = databaseHandler.getWritableDatabase();
        Intent intent = getActivity().getIntent();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        ContentValues cv = new ContentValues();
        cv.put(DataBaseHelper.ACTIVITY_COL3, getEncodedString(theImage));
        cv.put(DataBaseHelper.ACTIVITY_COL2, loggedInUser);
        cv.put(DataBaseHelper.ACTIVITY_COL4, date);

        long id = db.insert(DataBaseHelper.ACTIVITY_TABLE_NAME, null, cv);
        if (id < 0) {
            Toast.makeText(getContext(), "Something went wrong. Please try again later...", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Add successful", Toast.LENGTH_LONG).show();
        }
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


    private String getEncodedString(Bitmap bitmap) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);

       /* or use below if you want 32 bit images

        bitmap.compress(Bitmap.CompressFormat.PNG, (0–100 compression), os);*/
        byte[] imageArr = os.toByteArray();

        return Base64.encodeToString(imageArr, Base64.URL_SAFE);

    }


}
