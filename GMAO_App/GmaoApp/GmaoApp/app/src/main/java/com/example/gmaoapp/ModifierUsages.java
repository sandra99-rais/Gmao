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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ModifierUsages extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    int idUsage;
    EditText etUsage,date;
    ArrayList<String> listeEquipements, listeCompteurs;
    Spinner spEquip, spComp;
    Button btn;
    int var, idEquip=0, idCompt=0;
    int idEmploye;
    DatabaseHelper db=new DatabaseHelper(this);
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    /*Web service*/
    final String NAMESPACE = ContactResult.NAMESPACE;
    final String URL = ContactResult.URL;
    /*modifier usage*/
    final  String METHOD_NAME = "UpdateUsages";
    final String SOAP_ACTION = "http://tempuri.org/UpdateUsages";
    /*afficher usage by id*/
    final  String METHOD_NAMEUsage = "ListeUsageById";
    final String SOAP_ACTIONUsage = "http://tempuri.org/ListeUsageById";
    String ReturnResult,role;
    ArrayAdapter<String> equipements,compteurs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_usages);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        TextView textView = (TextView) findViewById(R.id.textView9);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        toolbar.setTitle("");

        //************************Tolbar********************************//
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_drawer_open,R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        Intent intent = getIntent();

        if (intent.hasExtra("nomDM"))
        {
            var = intent.getIntExtra("nomDM", -1);
        }
        if (intent.hasExtra("idUsage"))
        {
            idUsage= intent.getIntExtra("idUsage",-1);

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

        etUsage=(EditText)findViewById(R.id.etUsage) ;
        date=(EditText)findViewById(R.id.etDateTime);
        spEquip = (Spinner)findViewById(R.id.spEquip);
        btn=(Button)findViewById(R.id.btn_modif);

        spComp = (Spinner)findViewById(R.id.spComp);






        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(date);
            }
        });


        //*************************Liste Equipements *************************************
        Cursor ListeEq=db.ListeMachine();
        listeEquipements=new ArrayList<>();

        while(ListeEq.moveToNext()){
            listeEquipements.add(ListeEq.getString(2));
        }
         equipements=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listeEquipements);
        equipements.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEquip.setAdapter(equipements);
        spEquip.setOnItemSelectedListener(this);


        //*************************Liste Compteurs *************************************
        Cursor ListeComp=db.ListeCompteur();
        listeCompteurs=new ArrayList<>();

        while(ListeComp.moveToNext()){
            listeCompteurs.add(ListeComp.getString(2));
        }
        compteurs=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listeCompteurs);
        compteurs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spComp.setAdapter(compteurs);
        spComp.setOnItemSelectedListener(this);
        if(checkNetworkConnection())
        {
            new MyAsyncTaskListById().execute(String.valueOf(idUsage));
        }
        else{
            Usages u=db.UsageByid(idUsage);
            date.setText(u.getDateUsage());
            etUsage.setText(String.valueOf(u.getUsageRel()));
            Cursor machine=db.NomMachine(u.getIdMachine());
            machine.moveToNext();
            String nomMachine=machine.getString(0);
            spEquip.setSelection(equipements.getPosition(nomMachine));
            Cursor compteur=db.NomCompteur(u.getIdCompteur());
            compteur.moveToNext();
            String nomCompteur=compteur.getString(0);
            spComp.setSelection(compteurs.getPosition(nomCompteur));
        }


        btn.setOnClickListener(new View.OnClickListener() {
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
                if(checkNetworkConnection())
                {
                    (new ModifierUsages.MyAsyncTask()).execute(String.valueOf(idUsage),String.valueOf(idEquip),String.valueOf(idCompt),etUsage.getText().toString(), date.getText().toString());
                }
                else {
                    Usages u = new Usages(date.getText().toString(), Integer.parseInt(etUsage.getText().toString()), idEquip, idCompt);
                    boolean insert = db.updateUsage(u, String.valueOf(idUsage));
                    if (insert) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ModifierUsages.this);
                        builder.setTitle("Usage N°" + idUsage + " est modifié");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ModifierUsages.this, ReleveUsages.class);
                                intent.putExtra("nomDM", var);
                                intent.putExtra("idEmploye", idEmploye);
                                intent.putExtra("role", role);

                                startActivity(intent);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        Toast.makeText(ModifierUsages.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
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

    public boolean checkNetworkConnection()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }

    private class MyAsyncTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request=new SoapObject(NAMESPACE,METHOD_NAME);


            PropertyInfo id=new PropertyInfo();
            id.setName("id");
            id.setType(String.class);
            id.setValue(strings[0]);
            request.addProperty(id);

            PropertyInfo equipement=new PropertyInfo();
            equipement.setName("equipement");
            equipement.setType(String.class);
            equipement.setValue(strings[1]);
            request.addProperty(equipement);


            PropertyInfo compteur=new PropertyInfo();
            compteur.setName("compteur");
            compteur.setType(String.class);
            compteur.setValue(strings[2]);
            request.addProperty(compteur);

            PropertyInfo releve=new PropertyInfo();
            releve.setName("releve");
            releve.setType(String.class);
            releve.setValue(strings[3]);
            request.addProperty(releve);

            PropertyInfo date=new PropertyInfo();
            date.setName("date");
            date.setType(String.class);
            date.setValue(strings[4]);
            request.addProperty(date);






            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport=new HttpTransportSE(URL);
            try
            {
                androidHttpTransport.call(SOAP_ACTION,envelope);
                SoapObject result = (SoapObject) envelope.bodyIn;
                if (result != null)
                {
                    ReturnResult=result.getProperty(0).toString();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ModifierUsages.this);
                builder.setTitle("Usage N°" + idUsage + " est modifié");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ModifierUsages.this, ReleveUsages.class);
                        intent.putExtra("nomDM", var);
                        intent.putExtra("idEmploye", idEmploye);
                        intent.putExtra("role", role);
                        startActivity(intent);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            else
            {
                Toast.makeText(ModifierUsages.this,"test"+s,Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);
        }
    }

    private class MyAsyncTaskListById extends AsyncTask<String,Void,String>
    {
        int num;
        int releve;
        String equipement,compteur,dateRel;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        Date dateU= null;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request=new SoapObject(NAMESPACE,METHOD_NAMEUsage);


            PropertyInfo id=new PropertyInfo();
            id.setName("Id_Usage");
            id.setType(String.class);
            id.setValue(strings[0]);
            request.addProperty(id);




            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport=new HttpTransportSE(URL);
            try
            {

                    androidHttpTransport.call(SOAP_ACTIONUsage, envelope);
                    SoapObject result = (SoapObject) envelope.getResponse();
                    if (result != null) {
                        num = Integer.parseInt(result.getProperty("num").toString());
                        equipement = result.getProperty("equipement").toString();
                        compteur = result.getProperty("compteur").toString();
                        releve = Integer.parseInt(result.getProperty("releve").toString());
                        dateRel = result.getProperty("date").toString();

                        try {
                            dateU = formatter.parse(dateRel);

                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }
                        formatter.applyPattern("dd/MM/yyyy HH:mm");

                    }
            } catch (Exception e) {
                e.printStackTrace();
                return  e.toString();
            }
            return equipement;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null)
            {
               date.setText(formatter.format(dateU));
               etUsage.setText(String.valueOf(releve));
                spEquip.setSelection(equipements.getPosition(equipement));
                spComp.setSelection(compteurs.getPosition(compteur));

            }
            else
            {
                Toast.makeText(ModifierUsages.this,"test"+s,Toast.LENGTH_SHORT).show();

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
                new TimePickerDialog(ModifierUsages.this, timeSetListener, cldr.get(Calendar.HOUR_OF_DAY), cldr.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(ModifierUsages.this, dateSetListener, cldr.get(Calendar.YEAR), cldr.get(Calendar.MONTH), cldr.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
   if (id == R.id.menu_home) {
            Intent intent = new Intent(ModifierUsages.this, StatisticActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
       intent.putExtra("role", role);
       startActivity(intent);
        }
        if (id == R.id.menu_DMInt) {
            Intent intent = new Intent(ModifierUsages.this, DIActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeDM) {
            Intent intent = new Intent(ModifierUsages.this, AccueilActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_ReleveUsages) {
            Intent intent = new Intent(ModifierUsages.this, ReleveUsages.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_listeEchDate) {
            Intent intent = new Intent(ModifierUsages.this, EchanceParDate.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_BonSortie) {
            Intent intent = new Intent(ModifierUsages.this, BonSortie.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBS) {
            Intent intent = new Intent(ModifierUsages.this, ListeBonSortie.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_listeEchUsage) {
            Intent intent = new Intent(this, EcheanceParUsage.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_BonInventaire) {
            Intent intent = new Intent(ModifierUsages.this, BonInventaireActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBI) {
            Intent intent = new Intent(ModifierUsages.this, ListeBonInventaireActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeDemParTech) {
            Intent intent = new Intent(this, ListeDemandeParTech.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.consultation) {
            Intent intent = new Intent(this, ConsulterArticles.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.analyse) {
            Intent intent = new Intent(this, AnalysePanne.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.analyseSec) {
            Intent intent = new Intent(this, AnalyseSection.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
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