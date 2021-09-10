package com.example.gmaoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.gmaoapp.Models.OrdreTravail;
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

public class ModifierActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    EditText etNOT,etDateDM,etDateDebut,etDateFin,travEff,etDureeTrav;
    Spinner spinnerDU,spinnerTypeTravail,spinnerCloture,spinnerPanneConstate,spNomDM,spExecuteur,spinnerLance;
    Button acuse1,acuse2,btnEnregistrer;
    ArrayList<String> listePanne,listTravaux,listeEmploye;
    DatabaseHelper db=new DatabaseHelper(this);
    long diff,seconds,minutes = 0;
    Date dateDB,dateFin;
    int id;
    int var;
    String role,Demande,Cloture;
    Boolean Approuver;
    boolean isSelected=false,clotu=false;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    int idEmploye,idExec,idCT,idTT;
    ConstraintLayout scroll;
    TextView txtpanne;
    /*Web service*/
    final String NAMESPACE = ContactResult.NAMESPACE;
    final String URL = ContactResult.URL;
    /*update*/
    /*OT*/
    final  String METHOD_NAME = "UpdateOt";
    final String SOAP_ACTION = "http://tempuri.org/UpdateOt";
    /*OT BY ID*/
    final  String METHOD_NAME_BYID = "ListeOTById";
    final String SOAP_ACTION_BYID = "http://tempuri.org/ListeOTById";
    String ReturnResult;
    ArrayAdapter<String> travaux,panne,employe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        txtpanne=findViewById(R.id.demandeur);
        scroll =  findViewById(R.id.scroll);
        TextView textView = (TextView) findViewById(R.id.modif);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        toolbar.setTitle("");
        //************************Toolbar********************************//
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

        spNomDM = findViewById(R.id.SpNomDM);
        etNOT = findViewById(R.id.etNOT);
        etDateDebut = findViewById(R.id.etDateDebut);
        etDateFin = findViewById(R.id.etDateFin);
        spExecuteur = findViewById(R.id.spExecuteur);
        travEff = findViewById(R.id.etTravEff);
        etNOT.setEnabled(false);
        etDateDM = findViewById(R.id.etNBS);
        spinnerTypeTravail = findViewById(R.id.spinnerTypeTravail);
        spinnerPanneConstate = findViewById(R.id.spinnerDemandeur);
        spinnerDU = findViewById(R.id.spinnerDemandeur);
        spinnerCloture = findViewById(R.id.spinnerCloture);
        spinnerCloture.setEnabled(false);
        spinnerLance = findViewById(R.id.spinnerLance);

        acuse1 = findViewById(R.id.acuse1);
        acuse2 =  findViewById(R.id.acuse2);
        acuse1.setEnabled(false);
        acuse2.setEnabled(false);
        etDureeTrav=findViewById(R.id.etDureeTravail);
        btnEnregistrer=findViewById(R.id.btnEnregistrer);





        ////////////////////////////////////////////
        etDateDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(etDateDM);
            }
        });
        etDateFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(etDateFin);
            }
        });
        etDateDebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(etDateDebut);
            }
        });




        //**************************Liste Employe***************************************************************
        Cursor lEmploye=db.ListeEmploye();
        listeEmploye=new ArrayList<>();
        while(lEmploye.moveToNext())
        {
            String var = (lEmploye.getString(1)+" | "+lEmploye.getString(2));
            listeEmploye.add(var);

        }
         employe=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listeEmploye);
        employe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExecuteur.setAdapter(employe);
        spExecuteur.setOnItemSelectedListener(this);

        spNomDM.setAdapter(employe);
        spNomDM.setOnItemSelectedListener(this);


        //**********************************Liste Type Travaux**********************************************
        Cursor Liste=db.ListeTravaux();
        listTravaux=new ArrayList<>();

        while(Liste.moveToNext()){
            String var = (Liste.getString(1)+" | "+Liste.getString(2));
            listTravaux.add(var);
        }
        travaux=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listTravaux);
        travaux.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeTravail.setAdapter(travaux);
        spinnerTypeTravail.setOnItemSelectedListener(this);


        //***************************************Liste Panne*******************************************************************
        Cursor LPanne=db.ListePanne();
        listePanne=new ArrayList<>();
        while(LPanne.moveToNext()){
            String var = (LPanne.getString(1)+" | "+LPanne.getString(2));
            listePanne.add(var);
        }
        panne=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listePanne);
        panne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDU.setAdapter(panne);
        spinnerDU.setOnItemSelectedListener(this);
        id = intent.getIntExtra("idDI",-1);
        if(checkNetworkConnection())
        {
            new ListByIdAsyncTask().execute(String.valueOf(id));
        }
        else
        {
            OrdreTravail di=db.DemandeByid(id);
            etDateDebut.setText(di.getDateDemande());
            etDateFin.setText(di.getDateDemande());
            etDateDM.setText(di.getDateDemande());
            etNOT.setText(String.valueOf(id));
            //************************************Date fin**********************************************************

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            Date dateObject = null;
            try
            {
                String dateF=(etDateDM.getText().toString());
                dateObject = formatter.parse(dateF);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateObject);
            cal.add(Calendar.MINUTE, 1);
            String newTime = formatter.format(cal.getTime());
            etDateFin.setText(newTime);

            //************************Cloture*************************************************
            if(di.getCloture()==0)
            {
                spinnerCloture.setSelection(1);
            }
            else
            {
                spinnerCloture.setSelection(0);
            }
            //***********************Lance****************************************************
            if(di.getLance()==0)
            {
                spinnerLance.setSelection(1);
            }
            else
            {
                spinnerLance.setSelection(0);
            }
            //*************************Select Nom Executeur*******************************************************************
            Cursor Executeur=db.NomEmploye(di.getIdExecuteur());
            Executeur.moveToNext();
            String nomEX=Executeur.getString(1)+" | "+Executeur.getString(2);
            spExecuteur.setSelection(employe.getPosition(nomEX));
            //*************************Select Nom Demandeur*******************************************************************
            Cursor Demandeur=db.NomEmploye(di.getIdDemandeur());
            Demandeur.moveToNext();
            String nomDm=Demandeur.getString(1)+" | "+Demandeur.getString(2);
            spNomDM.setSelection(employe.getPosition(nomDm));
            //***************************Select type Travail*****************************************************
            Cursor t=db.trav(di.getIdTypeTrav());
            t.moveToNext();
            String tTravaux=t.getString(1)+" | "+t.getString(2);
            spinnerTypeTravail.setSelection(travaux.getPosition(tTravaux));
            //***************************Select type Panne*****************************************************
            if(di.getIdPanne()==0)
            {
                spinnerDU.setVisibility(View.GONE);
                txtpanne.setVisibility(View.GONE);


            }
            else {
                Cursor typeP = db.typePanne(di.getIdPanne());
                typeP.moveToNext();
                String tPanne = typeP.getString(1) + " | " + typeP.getString(2);
                spinnerDU.setSelection(panne.getPosition(tPanne));
            }
        }
        //*********************dateDebut**************

        etDateDebut.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s)
            {
                calculDate();
            }
        });
        //*********************datefin****************

        etDateFin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s)
            {
                calculDate();
            }
        });

        //////////////////////////////////////////////////////////////////////////
        spinnerCloture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long i) {
                if(clotu=true){
                    if(spinnerCloture.getSelectedItem().toString().equals("Oui")){
                        Cursor c=db.etatOT(String.valueOf(id));
                        while(c.moveToNext())
                        {
                            if(checkNetworkConnection())
                            {
                                disabledLayout();
                            }
                            else{
                            if(c.getInt(0)==1 && c.getInt(1)==1 && c.getInt(2)==1)
                            {

                                for (int j = 0; j < scroll.getChildCount(); j++) {
                                    View child = scroll.getChildAt(j);
                                    child.setEnabled(false);
                                }
                            }
                            else
                            {
                                acuse1.setEnabled(true);

                            }
                            }

                        }
                    }
                    if(spinnerCloture.getSelectedItem().toString().equals("Non")){
                        acuse1.setEnabled(false);
                        acuse2.setEnabled(false);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        if(clotu=false){
            acuse1.setEnabled(false);
            acuse2.setEnabled(false);
        }
        //////////////////////////////////////////////////////////////////////////
        spinnerLance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(spinnerLance.getSelectedItem().toString().equals("Oui")){
                    spinnerCloture.setEnabled(true);
                    clotu=true;
                    if(spinnerCloture.getSelectedItem().toString().equals("Oui")){
                        acuse1.setEnabled(true);
                    }
                }
                if(spinnerLance.getSelectedItem().toString().equals("Non")){
                    spinnerCloture.setEnabled(false);
                    clotu=false;
                    acuse1.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        acuse1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acuse1.setEnabled(false);
                acuse2.setEnabled(true);
            }
        });
        acuse2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                acuse2.setEnabled(false);

                isSelected=true;

            }
        });

        //*******************Calcul Date *********************************************
        calculDate();
        acuse2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(minutes<=0)
                {
                    Toast.makeText(ModifierActivity.this,"Veuillez entrer une date valide !",Toast.LENGTH_SHORT).show();
                }

                else if(Integer.parseInt(etDureeTrav.getText().toString())>minutes)
                {
                    Toast.makeText(ModifierActivity.this,"Veuillez entrer un durée valide !",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(spinnerCloture.getSelectedItem().toString().equals("Oui"))
                    { if(checkNetworkConnection())
                    {
                        updateApp("oui","oui","1");
                    }
                    else {
                        updateApp("1", "1", "1");
                    }
                    }

                }
            }
        });
        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(minutes<=0)
                {
                    Toast.makeText(ModifierActivity.this,"Veuillez entrer une date valide !",Toast.LENGTH_SHORT).show();
                }

                else if(Integer.parseInt(etDureeTrav.getText().toString())>minutes)
                {
                    Toast.makeText(ModifierActivity.this,"Veuillez entrer un durée valide !",Toast.LENGTH_SHORT).show();
                }
                else if(spinnerLance.getSelectedItem().toString().equals("Oui")){
                    if(spinnerCloture.getSelectedItem().toString().equals("Oui"))
                    {
                        if(checkNetworkConnection())
                        {
                            update("oui","oui");
                        }
                        else {
                            update("1", "1");
                        }
                    }
                    else if(spinnerCloture.getSelectedItem().toString().equals("Non")){
                        if(checkNetworkConnection())
                        {
                            update("oui","non");
                        }
                        else {
                            update("1", "0");
                        }
                    }
                }
                else
                {   if(checkNetworkConnection())
                {
                    updateApp("non","non","0");
                }
                else {
                    updateApp("0", "0", "0");
                }
                }
            }
        });
    }
    public void updateApp(String lance,String cloture,String app)
    {
        boolean insert;
        int idDem=0;
        Cursor c = db.idUser(spNomDM.getSelectedItem().toString());
        if(c.moveToNext()){
            idDem = c.getInt(c.getColumnIndex("idEmploye"));
        }
        Cursor c2 = db.idUser(spExecuteur.getSelectedItem().toString());
        while(c2.moveToNext()){
            idExec = c2.getInt(0);
        }

        Cursor c4 = db.idClefTravaux(spinnerPanneConstate.getSelectedItem().toString());
        if(c4.moveToNext()){
            idCT = c4.getInt(c4.getColumnIndex("idClefTravaux"));
        }
        Cursor c5 = db.idTypeTravaux(spinnerTypeTravail.getSelectedItem().toString());
        if(c5.moveToNext()){
            idTT = c5.getInt(c5.getColumnIndex("idTypeTravail"));
        }
        if(checkNetworkConnection()) {

            (new ModifierActivity.MyAsyncTask()).execute(String.valueOf(id),
                    etDateDM.getText().toString(),
                    String.valueOf(idDem),
                    String.valueOf(idCT),
                    String.valueOf(idTT)
                    ,etDateDebut.getText().toString(),
                    etDateFin.getText().toString(),
                    etDureeTrav.getText().toString(),
                    String.valueOf(idExec),
                    lance,
                    cloture,
                    app,
                    travEff.getText().toString());
        }
        else {
            OrdreTravail di = new OrdreTravail(etDateDM.getText().toString(), idExec, idDem
                    , idCT, idTT, travEff.getText().toString());
             insert = db.updateDIApp(di, etDateDebut.getText().toString(), etDateFin.getText().toString(), etDureeTrav.getText().toString(), String.valueOf(id), Integer.parseInt(lance), Integer.parseInt(cloture), Integer.parseInt(app));

        if(insert)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(ModifierActivity.this);
            builder.setTitle("OT N°"+id+" est modifié");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent(ModifierActivity.this,AccueilActivity.class);
                    intent.putExtra("nomDM",var);
                    intent.putExtra("idEmploye",idEmploye);
                    intent.putExtra("role", role);

                    startActivity(intent);
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }
        else
        {
            Toast.makeText(ModifierActivity.this,"failed",Toast.LENGTH_SHORT).show();
        }
        }
    }
    public void update(String lance,String cloture)
    {
        int idDem=0;
        Cursor c = db.idUser(spNomDM.getSelectedItem().toString());
        if(c.moveToNext()){
            idDem = c.getInt(c.getColumnIndex("idEmploye"));
        }
        Cursor c2 = db.idUser(spExecuteur.getSelectedItem().toString());
        while(c2.moveToNext()){
            idExec = c2.getInt(0);
        }

        Cursor c4 = db.idClefTravaux(spinnerPanneConstate.getSelectedItem().toString());
        if(c4.moveToNext()){
            idCT = c4.getInt(c4.getColumnIndex("idClefTravaux"));
        }
        Cursor c5 = db.idTypeTravaux(spinnerTypeTravail.getSelectedItem().toString());
        if(c5.moveToNext()){
            idTT = c5.getInt(c5.getColumnIndex("idTypeTravail"));
        }
        if(checkNetworkConnection()) {
            (new ModifierActivity.MyAsyncTask()).execute(String.valueOf(id),
                    etDateDM.getText().toString(),
                    String.valueOf(idDem),
                    String.valueOf(idCT),
                    String.valueOf(idTT)
                    ,etDateDebut.getText().toString(),
                    etDateFin.getText().toString(),
                    etDureeTrav.getText().toString(),
                    String.valueOf(idExec),
                    lance,
                    cloture,
                    String.valueOf(0),
                    travEff.getText().toString());
        }
else{        OrdreTravail di=new OrdreTravail(etDateDM.getText().toString(),idExec,idDem
                ,idCT,idTT,travEff.getText().toString());
        boolean insert= db.updateDI(di,etDateDebut.getText().toString(),etDateFin.getText().toString(),etDureeTrav.getText().toString(), String.valueOf(id),Integer.parseInt(lance),Integer.parseInt(cloture));
        if(insert)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(ModifierActivity.this);
            builder.setTitle("OT N°"+id+" est modifié");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent(ModifierActivity.this,AccueilActivity.class);
                    intent.putExtra("nomDM",var);
                    intent.putExtra("idEmploye",idEmploye);
                    intent.putExtra("role", role);

                    startActivity(intent);
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }
        else
        {
            Toast.makeText(ModifierActivity.this,"failed",Toast.LENGTH_SHORT).show();
        }}
    }
    public void calculDate(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try {
            dateDB = format.parse(etDateDebut.getText().toString());
            dateFin = format.parse(etDateFin.getText().toString());
            diff = dateFin.getTime() - dateDB.getTime();
            seconds = diff / 1000;
            minutes = seconds / 60;


            if (minutes<=0){
                etDureeTrav.setEnabled(false);
            }
            else {
                etDureeTrav.setEnabled(true);
                etDureeTrav.setText(String.valueOf(minutes));
            }
        } catch (ParseException e) {
            e.printStackTrace();
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
                new TimePickerDialog(ModifierActivity.this, timeSetListener, cldr.get(Calendar.HOUR_OF_DAY), cldr.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(ModifierActivity.this, dateSetListener, cldr.get(Calendar.YEAR), cldr.get(Calendar.MONTH), cldr.get(Calendar.DAY_OF_MONTH)).show();
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
    private class ListByIdAsyncTask extends AsyncTask<String, Void, String> {
         String Date_Demande,Date_Debut,Date_Fin,Trv_Effectue;
         int Nom_Emetteur,Nom_Demandeur,Id_Clef_trv,Id_Type_Trv,Duree_Travail;
        Date dateDM= null;
        Date dateDB= null;
        Date dateF= null;
        String newTime;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_BYID);

            PropertyInfo Id_Bt=new PropertyInfo();
            Id_Bt.setName("Id_Bt");
            Id_Bt.setType(String.class);
            Id_Bt.setValue(strings[0]);
            request.addProperty(Id_Bt);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(SOAP_ACTION_BYID, envelope);
                SoapObject result = (SoapObject) envelope.getResponse();
                if (result != null) {
                    Demande = result.getProperty("Demande").toString();
                    Cloture = result.getProperty("Cloture").toString();
                    Date_Demande = result.getProperty("Date_Demande").toString();
                    Date_Fin = result.getProperty("Date_Fin").toString();
                    Date_Debut = result.getProperty("Date_Debut").toString();
                    Trv_Effectue = result.getProperty("Trv_Effectue").toString();
                    Nom_Emetteur =Integer.parseInt( result.getProperty("Nom_Emetteur").toString());
                    Nom_Demandeur =Integer.parseInt( result.getProperty("Nom_Demandeur").toString());
                    Id_Clef_trv =Integer.parseInt( result.getProperty("Id_Clef_trv").toString());
                    Id_Type_Trv =Integer.parseInt( result.getProperty("Id_Type_Trv").toString());
                    Duree_Travail =Integer.parseInt( result.getProperty("Duree_Travail").toString());
                    Approuver = Boolean.valueOf(result.getProperty("Approuver").toString());



                    try {
                        dateDM = formatter.parse(Date_Demande);
                        dateDB = formatter.parse(Date_Debut);
                        dateF = formatter.parse(Date_Fin);

                    }
                    catch (ParseException e)
                    {
                        e.printStackTrace();
                    }
                    formatter.applyPattern("dd/MM/yyyy HH:mm");
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateF);
                    cal.add(Calendar.MINUTE, 1);
                     newTime = formatter.format(cal.getTime());


                }

            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return Demande;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s!=null) {
                etNOT.setText(String.valueOf(id));
                etDateDM.setText(formatter.format(dateDM));
                etDateDebut.setText(formatter.format(dateDB));
                etDateFin.setText(newTime);
                travEff.setText(Trv_Effectue);
//************************Cloture*************************************************
                if(Cloture.equals("oui"))
                {
                    spinnerCloture.setSelection(0);
                }
                else
                {
                    spinnerCloture.setSelection(1);
                }
                //***********************Lance****************************************************
                if(Demande.equals("oui"))
                {
                    spinnerLance.setSelection(0);
                }
                else
                {
                    spinnerLance.setSelection(1);
                }

                //*************************Select Nom Executeur*******************************************************************
                Cursor Executeur=db.NomEmploye(Nom_Emetteur);
                Executeur.moveToNext();
                String nomEX=Executeur.getString(1)+" | "+Executeur.getString(2);
                spExecuteur.setSelection(employe.getPosition(nomEX));
               //*************************Select Nom Demandeur*******************************************************************
                Cursor Demandeur=db.NomEmploye(Nom_Demandeur);
                Demandeur.moveToNext();
                String nomDm=Demandeur.getString(1)+" | "+Demandeur.getString(2);
                spNomDM.setSelection(employe.getPosition(nomDm));
                //***************************Select type Travail*****************************************************
                Cursor t=db.trav(Id_Type_Trv);
                t.moveToNext();
                String tTravaux=t.getString(1)+" | "+t.getString(2);
                spinnerTypeTravail.setSelection(travaux.getPosition(tTravaux));
                //***************************Select type Panne*****************************************************
                if(Id_Clef_trv==0)
                {
                    spinnerDU.setVisibility(View.GONE);
                    txtpanne.setVisibility(View.GONE);


                }
                else {
                    Cursor typeP = db.typePanne(Id_Clef_trv);
                    typeP.moveToNext();
                    String tPanne = typeP.getString(1) + " | " + typeP.getString(2);
                    spinnerDU.setSelection(panne.getPosition(tPanne));
                }


            }
            super.onPostExecute(s);
        }
    }
    public void disabledLayout()
    {
        if(Demande.equals("oui") && Cloture.equals("oui") && Approuver==true)
        {

            for (int j = 0; j < scroll.getChildCount(); j++) {
                View child = scroll.getChildAt(j);
                child.setEnabled(false);
            }
        }
        else
        {
            acuse1.setEnabled(true);
        }
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

            PropertyInfo date=new PropertyInfo();
            date.setName("date");
            date.setType(String.class);
            date.setValue(strings[1]);
            request.addProperty(date);


            PropertyInfo Nom_Demandeur=new PropertyInfo();
            Nom_Demandeur.setName("Nom_Demandeur");
            Nom_Demandeur.setType(String.class);
            Nom_Demandeur.setValue(strings[2]);
            request.addProperty(Nom_Demandeur);

            PropertyInfo Id_Clef_trv=new PropertyInfo();
            Id_Clef_trv.setName("Id_Clef_trv");
            Id_Clef_trv.setType(String.class);
            Id_Clef_trv.setValue(strings[3]);
            request.addProperty(Id_Clef_trv);

            PropertyInfo Id_Type_Trv=new PropertyInfo();
            Id_Type_Trv.setName("Id_Type_Trv");
            Id_Type_Trv.setType(String.class);
            Id_Type_Trv.setValue(strings[4]);
            request.addProperty(Id_Type_Trv);

            PropertyInfo Date_Debut=new PropertyInfo();
            Date_Debut.setName("Date_Debut");
            Date_Debut.setType(String.class);
            Date_Debut.setValue(strings[5]);
            request.addProperty(Date_Debut);

            PropertyInfo Date_Fin=new PropertyInfo();
            Date_Fin.setName("Date_Fin");
            Date_Fin.setType(String.class);
            Date_Fin.setValue(strings[6]);
            request.addProperty(Date_Fin);

            PropertyInfo Duree_Travail=new PropertyInfo();
            Duree_Travail.setName("Duree_Travail");
            Duree_Travail.setType(String.class);
            Duree_Travail.setValue(strings[7]);
            request.addProperty(Duree_Travail);

            PropertyInfo Nom_Emetteur=new PropertyInfo();
            Nom_Emetteur.setName("Nom_Emetteur");
            Nom_Emetteur.setType(String.class);
            Nom_Emetteur.setValue(strings[8]);
            request.addProperty(Nom_Emetteur);

            PropertyInfo Demande=new PropertyInfo();
            Demande.setName("Demande");
            Demande.setType(String.class);
            Demande.setValue(strings[9]);
            request.addProperty(Demande);

            PropertyInfo Cloture=new PropertyInfo();
            Cloture.setName("Cloture");
            Cloture.setType(String.class);
            Cloture.setValue(strings[10]);
            request.addProperty(Cloture);


            PropertyInfo Approuver=new PropertyInfo();
            Approuver.setName("Approuver");
            Approuver.setType(String.class);
            Approuver.setValue(strings[11]);
            request.addProperty(Approuver);


            PropertyInfo Trv_Effectue=new PropertyInfo();
            Trv_Effectue.setName("Trv_Effectue");
            Trv_Effectue.setType(String.class);
            Trv_Effectue.setValue(strings[12]);
            request.addProperty(Trv_Effectue);


            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport=new HttpTransportSE(URL);
            try
            {
                androidHttpTransport.call(SOAP_ACTION,envelope);
                SoapObject result= (SoapObject) envelope.bodyIn;
                if (result!=null)
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
               AlertDialog.Builder builder=new AlertDialog.Builder(ModifierActivity.this);
               builder.setTitle("OT N°"+id+" est modifié");
               builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       Intent intent=new Intent(ModifierActivity.this,AccueilActivity.class);
                       intent.putExtra("nomDM",var);
                       intent.putExtra("idEmploye",idEmploye);
                       intent.putExtra("role", role);
                       startActivity(intent);
                   }
               });
               AlertDialog alertDialog=builder.create();
               alertDialog.show();
           }
           else
           {
               Toast.makeText(ModifierActivity.this,"false  "+s,Toast.LENGTH_SHORT).show();

           }


            super.onPostExecute(s);
        }
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_home) {
            Intent intent = new Intent(ModifierActivity.this, StatisticActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);

            startActivity(intent);
        }
        if (id == R.id.menu_DMInt) {
            Intent intent = new Intent(ModifierActivity.this, DIActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);

            startActivity(intent);
        }
        if (id == R.id.menu_ListeDM) {
            Intent intent = new Intent(ModifierActivity.this, AccueilActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);

            startActivity(intent);
        }
        if (id == R.id.menu_ReleveUsages) {
            Intent intent = new Intent(ModifierActivity.this, ReleveUsages.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);

            startActivity(intent);
        }
        if (id == R.id.menu_listeEchDate) {
            Intent intent = new Intent(ModifierActivity.this, EchanceParDate.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);

            startActivity(intent);
        }
        if (id == R.id.menu_BonSortie) {
            Intent intent = new Intent(ModifierActivity.this, BonSortie.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);

            startActivity(intent);
        }
        if (id == R.id.menu_ListeBS) {
            Intent intent = new Intent(ModifierActivity.this, ListeBonSortie.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);

            startActivity(intent);
        }
        if (id == R.id.menu_BonInventaire) {
            Intent intent = new Intent(ModifierActivity.this, BonInventaireActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBI) {
            Intent intent = new Intent(ModifierActivity.this, ListeBonInventaireActivity.class);
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
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
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