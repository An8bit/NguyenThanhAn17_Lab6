package com.example.nguyenthanhan17_lab6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Random;


public class AddEditContactActivity extends AppCompatActivity {


    ImageView imgAvata;
    TextInputLayout tifName, tilName, tiEmail, tiPhone, tiBirthday;
    TextInputEditText edfName, edlName, edEmail, edPhone, edBirthday;
    int mYear, mMonth, mDay;
    int flag;
    Info InfoEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tifName = findViewById(R.id.tilFname);
        tilName = findViewById(R.id.tilLname);
        tiEmail = findViewById(R.id.tilEmail);
        tiPhone = findViewById(R.id.tilPhone);
        tiBirthday = findViewById(R.id.tilBirthday);
        edfName = findViewById(R.id.edFirstName);
        edlName = findViewById(R.id.edLastName);
        edEmail = findViewById(R.id.edEmail);
        edPhone = findViewById(R.id.edPhone);
        imgAvata=findViewById(R.id.imgAvata);
        edBirthday = findViewById(R.id.edBirthday);
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", 0);
        if(flag == 1){
            getSupportActionBar().setTitle("Add Contact");
        }else {
            getSupportActionBar().setTitle("Edit Contact");
            InfoEdit = (Info) intent.getSerializableExtra("infos");
            try {
                String image = InfoEdit.getImage();
                InputStream is = AddEditContactActivity.this.getAssets().open(image);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                imgAvata.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            edfName.setText(InfoEdit.getFname());
            edlName.setText(InfoEdit.getLname());
            edEmail.setText(InfoEdit.getMail());
            edPhone.setText(InfoEdit.getPhone());
            edBirthday.setText(InfoEdit.getBirthday());

        }


        edBirthday.setOnClickListener(view -> {
            if (view == edBirthday) {
                final Calendar calendar = Calendar.getInstance ();
                mYear = calendar.get ( Calendar.YEAR );
                mMonth = calendar.get ( Calendar.MONTH );
                mDay = calendar.get ( Calendar.DAY_OF_MONTH );

                //show dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog ( AddEditContactActivity.this, new DatePickerDialog.OnDateSetListener () {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        edBirthday.setText ( dayOfMonth + "/" + String.format("%02d",month+1) + "/" + year );
                    }
                }, mYear, mMonth, mDay );
                datePickerDialog.show ();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       MenuInflater menuInflater = getMenuInflater();
       menuInflater.inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mnuadd){
            if(edfName.getText().toString().isEmpty()
                    ||edlName.getText().toString().isEmpty()
                    ||edEmail.getText().toString().isEmpty()
                    ||edPhone.getText().toString().isEmpty()
                    ||edBirthday.getText().toString().isEmpty()){
                tifName.setError("Not null");
                tilName.setError("Not null");
                tiEmail.setError("Not null");
                tiPhone.setError("Not null");
                tiBirthday.setError("Not null");
                return false;
            }else {
                if(flag == 1){
                    Info info = new Info(new Random().nextInt(9999),
                            edfName.getText().toString(),
                            edlName.getText().toString(),
                            "ava.jpg",
                            edPhone.getText().toString(),
                            edEmail.getText().toString(),
                            edBirthday.getText().toString());


                    Intent intent = new Intent();
                    intent.putExtra("contact", info);
                    intent.putExtra("flag", 1);
                    setResult(RESULT_OK, intent);
                    finish();
                }else {
                    Info info = new Info(InfoEdit.getId(),
                            edfName.getText().toString(),
                            edlName.getText().toString(),
                            InfoEdit.getImage(),
                            edPhone.getText().toString(),
                            edEmail.getText().toString(),
                            edBirthday.getText().toString());

                    Intent intent = new Intent();
                    intent.putExtra("contact", info);
                    intent.putExtra("flag", 2);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
