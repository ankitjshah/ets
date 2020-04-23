package com.msd.finalproject.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.msd.finalproject.R;
import com.msd.finalproject.helper.DataBaseHelper;
import com.msd.finalproject.utils.LocalDataBaseAdapter;
import com.msd.finalproject.utils.LocalResponse;

import java.util.ArrayList;

/**
 * LocalFragment is used to display
 * captured images in a recycler view
 */
public class LocalFragment extends Fragment {
    RecyclerView recyclerView;
    String image;
    int uid;
    Cursor cursor;
    private DataBaseHelper myDatabase;
    private SQLiteDatabase db;
    private ArrayList<LocalResponse> singleRowArrayList;
    private LocalResponse singleRow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.local_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        myDatabase = new DataBaseHelper(getContext());
        db = myDatabase.getWritableDatabase();
        setData();
        return view;
    }

    private void setData() {
        db = myDatabase.getWritableDatabase();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        singleRowArrayList = new ArrayList<>();
        String[] columns = {DataBaseHelper.ACTIVITY_COL1, DataBaseHelper.ACTIVITY_COL3};

        // Get captured images from the database
        cursor = db.query(DataBaseHelper.ACTIVITY_TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {

            int index1 = cursor.getColumnIndex(DataBaseHelper.ACTIVITY_COL1);
            int index2 = cursor.getColumnIndex(DataBaseHelper.ACTIVITY_COL3);
            uid = cursor.getInt(index1);
            image = cursor.getString(index2);
            singleRow = new LocalResponse(image, uid);
            singleRowArrayList.add(singleRow);
        }
        // If records doesn't exist, then do not show recycerview
        if (singleRowArrayList.size() == 0) {
            //empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            // populating recyclerview from a database's records
            LocalDataBaseAdapter localDataBaseResponse = new LocalDataBaseAdapter(getContext(), singleRowArrayList, db, myDatabase);
            recyclerView.setAdapter(localDataBaseResponse);
        }


    }
}
