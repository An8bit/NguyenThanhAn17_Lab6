package com.example.nguyenthanhan17_lab6;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class detiles extends AppCompatActivity implements InfoAdapter.Listener  {

    ImageView imgFlag;
   TextView tvFName, tvPhone, tvMail,tvLName,tvBirthday;
    Info info;
    InfoAdapter infoAdapter;

    int position;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detiles);
        Intent intent = getIntent(); // lấy dữ liệu từ intent
        info = (Info) intent.getSerializableExtra("infos");
        imgFlag = findViewById(R.id.imgFlag);
        tvFName = findViewById(R.id.txFName);
        tvPhone = findViewById(R.id.txPhone);
        tvMail = findViewById(R.id.txEmail);
        tvLName=findViewById(R.id.txlName);
        tvBirthday=findViewById(R.id.txBirthday);
        try {
            String image = info.getImage();
            InputStream is = detiles.this.getAssets().open(image);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            imgFlag.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }

       tvFName.setText(info.getFname());
        tvLName.setText(info.getLname());
        tvPhone.setText(info.getPhone());
        tvMail.setText(info.getMail());
        tvBirthday.setText(info.getBirthday());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
    ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode()==RESULT_OK){
                        if(result.getData().getIntExtra("flag",0)==1){
                            Info info = (Info) result.getData().getSerializableExtra("contact");
                            infoAdapter.addInfo(info);
                        }else {
                            Info info=(Info) result.getData().getSerializableExtra("contact");
                            infoAdapter.editInfo(info,position);
                        }
                    }
                }
            }
    );
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClickListener(int pos, Info info) {

    }

    @Override
    public void onEditListener(int pos, Info info) {

    }

    @Override
    public void onDeleteListener(int pos, Info info) {

    }


}