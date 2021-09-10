package com.example.gmaoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.gmaoapp.Models.Usages;
import com.google.android.material.navigation.NavigationView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AjoutReleve extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    EditText etDateTime, etUsage;
    Spinner spEquip, spComp;
    Button ot_enrg;
    ArrayList<String> listeEquipements, listeCompteurs;
    int var ;
    int idEmploye,idCompt,idEquip,idUsage;
    int idSo=0, idAc=0;
    String role;

    DatabaseHelper db=new DatabaseHelper(this);
    /*Web service*/
    final String NAMESPACE = ContactResult.NAMESPACE;
    final String URL = ContactResult.URL;
    /*ajout releve*/
    final  String METHOD_NAME = "AjouterUsage";
    final String SOAP_ACTION = "http://tempuri.org/AjouterUsage";
    String ReturnResult;
    /*Menu*/
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_releve);
        //*****************************************Menu**********************************/

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("");
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_drawer_open,R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.menu_ReleveUsages);
        TextView textView = (TextView) findViewById(R.id.textView9);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //*****************************************Fin Menu**********************************/

        Intent intent = getIntent();
        if (intent.hasExtra("nomDM"))
        {
            var = intent.getIntExtra("nomDM", -1);
        }
        if(intent.hasExtra("idEmploye"))
        {
            idEmploye = intent.getIntExtra("idEmploye", -1);
        }
        if(intent.hasExtra("role"))
        {
            role=intent.getStringExtra("role");
        }
        //**************************************************//
        SpecificMenu s=new SpecificMenu();
        s.afficheMenu(role,navigationView);
        //**************************************************//
        etDateTime = (EditText)findViewById(R.id.etDateTime);
        etUsage = (EditText)findViewById(R.id.etUsage);
        etUsage.setText(String.valueOf(0));
        etDateTime.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        etDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(etDateTime);
            }
        });
        spEquip = (Spinner)findViewById(R.id.spEquip);
        spComp = (Spinner)findViewById(R.id.spComp);

        //*************************Liste Equipements *************************************
        Cursor ListeEq=db.ListeMachine();
        listeEquipements=new ArrayList<>();

        while(ListeEq.moveToNext()){
            listeEquipements.add(ListeEq.getString(2));
        }
        ArrayAdapter<String> equipements=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listeEquipements);
        equipements.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEquip.setAdapter(equipements);
        spEquip.setOnItemSelectedListener(this);

        //*************************Liste Compteurs *************************************
        Cursor ListeComp=db.ListeCompteur();
        listeCompteurs=new ArrayList<>();

        while(ListeComp.moveToNext()){
            listeCompteurs.add(ListeComp.getString(2));
        }
        ArrayAdapter<String> compteurs=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listeCompteurs);
        compteurs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spComp.setAdapter(compteurs);
        spComp.setOnItemSelectedListener(this);

        //****************************Ajouter releve********************************

        ot_enrg = (Button) findViewById(R.id.ot_enregistrer);
        ot_enrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c1 = db.idDesEquip2(spEquip.getSelectedItem().toString());
                if(c1.moveToNext()){
                    idEquip = c1.getInt(c1.getColumnIndex("idMachine"));
                }
                Cursor c11=db.idDesCompteur(spComp.getSelectedItem().toString());
                if(c11.moveToNext()){
                    idCompt = c11.getInt(c11.getColumnIndex("idCompteur"));
                }
                Cursor c10 = db.cursor(String.valueOf(var));
                if(c10.moveToNext()){
                    idSo = c10.getInt(c10.getColumnIndex("idSoc"));
                    idAc = c10.getInt(c10.getColumnIndex("idAct"));
                }
                if(checkNetworkConnection())
                {
                    (new AjoutReleve.MyAsyncTask()).execute(String.valueOf(idSo), String.valueOf(idAc), String.valueOf(idEquip), String.valueOf(idCompt),
                            etUsage.getText().toString(), etDateTime.getText().toString());
                }
                else {
                    Usages u = new Usages(etDateTime.getText().toString(), Integer.parseInt(etUsage.getText().toString()), idEquip, idCompt);
                    boolean insert = db.insertUsage(u, String.valueOf(var));
                    if (insert) {
                        Cursor c12 = db.idUsage();
                        if (c12.moveToNext()) {
                            idUsage = c12.getInt(0);
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(AjoutReleve.this);
                        builder.setTitle("Releve N°"+idUsage+" ajouté avec succès");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(AjoutReleve.this, ReleveUsages.class);
                                intent.putExtra("nomDM", var);
                                intent.putExtra("role", role);
                                intent.putExtra("idEmploye", idEmploye);
                                startActivity(intent);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        Toast.makeText(AjoutReleve.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    //***********************************Check Connection*******************************************************/

    public boolean checkNetworkConnection()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }
    //***********************************ajouter releve usage en mode connecter*******************************************************/

    private class MyAsyncTask extends AsyncTask<String,Void,String>
    {
        int idUs;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request=new SoapObject(NAMESPACE,METHOD_NAME);

            PropertyInfo idSoc=new PropertyInfo();
            idSoc.setName("id_Soc");
            idSoc.setType(String.class);
            idSoc.setValue(strings[0]);
            request.addProperty(idSoc);

            PropertyInfo idAct=new PropertyInfo();
            idAct.setName("id_Act");
            idAct.setType(String.class);
            idAct.setValue(strings[1]);
            request.addProperty(idAct);

            PropertyInfo id_Machine=new PropertyInfo();
            id_Machine.setName("id_Machine");
            id_Machine.setType(String.class);
            id_Machine.setValue(strings[2]);
            request.addProperty(id_Machine);

            PropertyInfo id_Compteur=new PropertyInfo();
            id_Compteur.setName("id_Compteur");
            id_Compteur.setType(String.class);
            id_Compteur.setValue(strings[3]);
            request.addProperty(id_Compteur);

            PropertyInfo usage_rel=new PropertyInfo();
            usage_rel.setName("usage_rel");
            usage_rel.setType(String.class);
            usage_rel.setValue(strings[4]);
            request.addProperty(usage_rel);

            PropertyInfo Date=new PropertyInfo();
            Date.setName("Date");
            Date.setType(String.class);
            Date.setValue(strings[5]);
            request.addProperty(Date);


            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport=new HttpTransportSE(URL);
            try
            {
                androidHttpTransport.call(SOAP_ACTION,envelope);
                SoapObject result = (SoapObject) envelope.getResponse();
                if (result != null)
                {
                    ReturnResult=result.getProperty("result").toString();
                    idUs= Integer.parseInt(result.getProperty("NBT").toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return  e.toString();
            }
            return ReturnResult;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("true"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(AjoutReleve.this);
                builder.setTitle("Releve N°"+idUs+" ajouté avec succès");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AjoutReleve.this, ReleveUsages.class);
                        intent.putExtra("nomDM", var);
                        intent.putExtra("role", role);
                        intent.putExtra("idEmploye", idEmploye);
                        startActivity(intent);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            else
            {
                Toast.makeText(AjoutReleve.this,""+s,Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);
        }
    }

    //*********************DateHeure*******************************
    private void showDateTimeDialog(EditText editText) {
        final Calendar cldr = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cldr.set(Calendar.YEAR,year);
                cldr.set(Calendar.MONTH,month);
                cldr.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        cldr.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        cldr.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                        editText.setText(simpleDateFormat.format(cldr.getTime()));
                    }
                };
                new TimePickerDialog(AjoutReleve.this, timeSetListener, cldr.get(Calendar.HOUR_OF_DAY), cldr.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(AjoutReleve.this, dateSetListener, cldr.get(Calendar.YEAR), cldr.get(Calendar.MONTH), cldr.get(Calendar.DAY_OF_MONTH)).show();
    }
    //*****************************************Button deconnecter*************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbarmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_Dec) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //*****************************************Open and CloseMenu*************************************************/

    @Override
    public void onBackPressed()
    {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_home){
            Intent intent = new Intent(AjoutReleve.this,StatisticActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);

            startActivity(intent);
        }
        if (id == R.id.menu_listeEchUsage) {
            Intent intent = new Intent(this, EcheanceParUsage.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if(id == R.id.menu_DMInt){
            Intent intent = new Intent(AjoutReleve.this,DIActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if(id == R.id.menu_ListeDM){
            Intent intent = new Intent(AjoutReleve.this,AccueilActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);

            startActivity(intent);
        }
        if (id == R.id.menu_ReleveUsages) {
            Intent intent = new Intent(AjoutReleve.this,ReleveUsages.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_listeEchDate) {
            Intent intent = new Intent(AjoutReleve.this, EchanceParDate.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_BonSortie) {
            Intent intent = new Intent(AjoutReleve.this, BonSortie.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);

            startActivity(intent);
        }
        if (id == R.id.menu_ListeBS) {
            Intent intent = new Intent(AjoutReleve.this, ListeBonSortie.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_BonInventaire) {
            Intent intent = new Intent(AjoutReleve.this, BonInventaireActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBI) {
            Intent intent = new Intent(AjoutReleve.this, ListeBonInventaireActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeDemParTech) {
            Intent intent = new Intent(this, ListeDemandeParTech.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.consultation) {
            Intent intent = new Intent(this, ConsulterArticles.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.analyse) {
            Intent intent = new Intent(this, AnalysePanne.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.analyseSec) {
            Intent intent = new Intent(this, AnalyseSection.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_Dec) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}