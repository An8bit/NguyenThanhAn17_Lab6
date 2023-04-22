package com.example.nguyenthanhan17_lab6;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements InfoAdapter.Listener {

    RecyclerView rvInfo;
    InfoAdapter infoAdapter;
    ArrayList<Info> infos;
    int position;
    DBHelper dbHelper;
    FloatingActionButton btAddContact;


       ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode()==RESULT_OK){
                        if(result.getData().getIntExtra("flag",0)==1){
                            Info info = (Info) result.getData().getSerializableExtra("contact");
                           dbHelper.insertInfo(info);
                           infos.clear();
                           infos.addAll(dbHelper.getInfo());
                            infoAdapter.notifyDataSetChanged();
                        }else {
                            Info info=(Info) result.getData().getSerializableExtra("contact");
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
        setContentView(R.layout.activity_main);
       dbHelper=new DBHelper(MainActivity.this);
       infos = dbHelper.getInfo();
        rvInfo = findViewById(R.id.rvInfo);
        getSupportActionBar().setTitle("Contact");
        infoAdapter = new InfoAdapter(infos,MainActivity.this,MainActivity.this);
        rvInfo.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        rvInfo.setAdapter(infoAdapter);
        btAddContact = findViewById(R.id.btAddContact);
        btAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, AddEditContactActivity.class);
                intent.putExtra("flag",1);
               mLauncher.launch(intent);
            }
        });


    }

    @Override
    //hiển thị chi tiết
    public void onClickListener(Info info) {
        Intent intent = new Intent(MainActivity.this, detiles.class);
        Info info1 = dbHelper.getdetail(info.getId());
        intent.putExtra("infos",info1 ); // truyền dữ liệu giữa các activity: truyền dữ liệu vào intent
        startActivity(intent);
    }

    @Override
    //chỗ này chỉnh sửa
    public void onEditListener(int pos, Info info) {
        Intent intent = new Intent (MainActivity.this, AddEditContactActivity.class);
        intent.putExtra("flag",2);
        intent.putExtra("infos",info);
        mLauncher.launch(intent);
    }

    @Override
    //để xóa
    public void onDeleteListener(int pos, Info info) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
        alertDialog.show();}




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
      return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuSort){
            infos.clear();
            infos.addAll(dbHelper.Sort());
            infoAdapter.notifyDataSetChanged();
        }
        if (item.getItemId() == R.id.menuSearch) {
            infos.clear();
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }



}



