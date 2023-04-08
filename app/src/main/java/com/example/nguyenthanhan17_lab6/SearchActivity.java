package com.example.nguyenthanhan17_lab6;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements InfoAdapter.Listener {

    RecyclerView rvInfo;
    InfoAdapter infoAdapter;
    DBHelper dbHelper = new DBHelper(SearchActivity.this);
    ArrayList<Info> infos;
    int position;


    ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData().getIntExtra("flag", 0) == 1) {
                            Info info = (Info) result.getData().getSerializableExtra("contact");
                            dbHelper.insertInfo(info);
                            infos.clear();
                            infos.addAll(dbHelper.getInfo());
                            infoAdapter.notifyDataSetChanged();
                        } else {
                            Info info = (Info) result.getData().getSerializableExtra("contact");
                            dbHelper.updateInfo(info);
                            infos.clear();
                            infos.addAll(dbHelper.getInfo());
                            infoAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        infos = dbHelper.getInfo();
        // Khởi tạo Adapter và RecyclerView
        rvInfo = findViewById(R.id.rvInfo);
        infoAdapter = new InfoAdapter(infos, SearchActivity.this,SearchActivity.this);
        rvInfo.setAdapter(infoAdapter);
        rvInfo.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        SearchView searchView  = (SearchView) menu.findItem(R.id.menuSearchh).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

               infoAdapter.getFilter().filter(query);
                return false;
            }
            //
            @Override
            public boolean onQueryTextChange(String newText) {
                infoAdapter.getFilter().filter(newText);

                //             if(newText.isEmpty()){
//                    btAddContact.setVisibility(View.VISIBLE);
//                }else {
//                    fabAddContact.setVisibility(View.INVISIBLE);
//                }
                return false;
            }
        });


        return true;
    }



    @Override
    public void onClickListener(int pos, Info info) {
        Intent intent = new Intent(SearchActivity.this, detiles.class);
        intent.putExtra("infos",info ); // truyền dữ liệu giữa các activity: truyền dữ liệu vào intent
        startActivity(intent);
    }

    @Override
    public void onEditListener(int pos, Info info) {
        Intent intent = new Intent (SearchActivity.this, AddEditContactActivity.class);
        intent.putExtra("flag",2);
        intent.putExtra("infos",info);
        mLauncher.launch(intent);
    }

    @Override
    public void onDeleteListener(int pos, Info info) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        builder.setTitle("Information");
        builder.setMessage("Delete ".concat(info.getFname()+ " "+info.getLname()).concat(" ?"));
        builder.setNegativeButton("No", (dialogInterface, i) -> {
            dialogInterface.cancel();

        });
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            dbHelper.deleteInfo(info);
            infos.clear();
            infos.addAll(dbHelper.getInfo());
            infoAdapter.notifyDataSetChanged();
            dialogInterface.dismiss();

        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
