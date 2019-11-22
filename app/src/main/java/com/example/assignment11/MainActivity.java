package com.example.assignment11;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS_1 = 1;
    private static final int RESULT_PICK_CONTACT =1;
    ListView listaView;
    Button addBTN;
    Button clearBTN;
    OmaAdapteri adapteri;
    ArrayList<String> lista;
    // HaVeN't cHecKed iF tHe NuMbeR iS alReAdY uSed
    ArrayList<String> kuitenkinValittaa = new ArrayList<>();
    Context context;
    Activity thisActivity = MainActivity.this;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        /*ActivityCompat.requestPermissions(thisActivity,
                new String[]{Manifest.permission.READ_CONTACTS},
                MY_PERMISSIONS_REQUEST_READ_CONTACTS_1);*/


        // kysytään täällä
        // grantResult: antaako luvan

        if(requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS_1){
            Toast.makeText(context, "We got permission!", Toast.LENGTH_SHORT)
                    .show();
        }
        else{
            ActivityCompat.requestPermissions(thisActivity,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS_1);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        }
        else{
            Toast.makeText(this,"Failed to pick contact", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;

        try {
            String phoneName = null;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneName = cursor.getString(phoneIndex);

            cursor.moveToFirst();
            phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String phoneNumber = cursor.getString(phoneIndex);



            // shouldn't add to the list if there is:
            //    same named contact lists  and  same numbered contact lists
            if(!(lista.contains(phoneName) && kuitenkinValittaa.contains(phoneNumber))){
                lista.add(phoneName);
                kuitenkinValittaa.add(phoneNumber);
            }

            adapteri.notifyDataSetChanged();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(context, "New Runnable", Toast.LENGTH_SHORT)
                //        .show();

                Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(pickContact, RESULT_PICK_CONTACT);
            }
        };

        listaView = (ListView) findViewById(R.id.ViewList);
        addBTN = (Button) findViewById(R.id.addBTN);
        clearBTN = (Button) findViewById(R.id.clearBTN);

        lista = new ArrayList<>();
        adapteri = new OmaAdapteri(this, R.layout.adapter_layout, lista);
        listaView.setAdapter(adapteri);


        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* check permission or something
                if (ContextCompat.checkSelfPermission(thisActivity, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted

                    //Toast.makeText(context, "No permission", Toast.LENGTH_SHORT)
                    //        .show();

                    ActivityCompat.requestPermissions(thisActivity,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS_1);
                            // request permission
                }

                else{
                    Toast.makeText(context, "Permission", Toast.LENGTH_SHORT)
                            .show();
                }*/




                Handler handler = new Handler();
                handler.post(runnable);


                //Yhteistieto uusi = new Yhteistieto("Kalle", false);

                //lista.add(uusi);


            }
        });

        clearBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lista.clear();
                kuitenkinValittaa.clear();
                adapteri.notifyDataSetChanged();
            }
        });
    }
}
