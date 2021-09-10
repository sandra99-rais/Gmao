package com.example.gmaoapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.gmaoapp.Models.OrdreTravail;
import com.example.gmaoapp.Models.Equipements;
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

public class DIActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener,NavigationView.OnNavigationItemSelectedListener{
    EditText etDateDM,etDesc;
    Button btn_enrg;
    Spinner spinnerSousEns,spinnerDU,spinnerTypeTravail,spinnerPanneConstate,spinnerDesignation,spinnerEns,etNomDM,etExecuteur,etat;
    ArrayList<String> listTravaux,listPanne,listeEquipements,Ensemble,ssEnsemble,listDegreUrgence,listEmploye,lEtat;
    ArrayList<Integer> listeDes,listeEtat,listeEns,listeSousEns,listeIdSec;
    int var,idEmploye,idExec,idDem,idDegUrg,idTT,idCT,idSo, idAc,nbt=0;
    String nomEX="",role;
    TextView titre1,designation,ensemble,sousEnsemble,txtetat,save;
    TableLayout tableLayout;
    Boolean clicked=false;
    ArrayAdapter<String> etatM;
    ScrollView scroll;
    /*Menu*/
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    DatabaseHelper db=new DatabaseHelper(this);
    /*Web service*/
    final String NAMESPACE = ContactResult.NAMESPACE;
    final String URL = ContactResult.URL;
    /*OT*/
    final  String METHOD_NAME = "AjouterDI";
    final String SOAP_ACTION = "http://tempuri.org/AjouterDI";
    /*Eqp*/
    final  String METHOD_NAME_EQ = "AjouterEqp";
    final String SOAP_ACTION_EQ = "http://tempuri.org/AjouterEqp";
    String ReturnResult;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_i);
        tableLayout=findViewById(R.id.tableLayout);
        scroll=findViewById(R.id.scroll);
        //****************ArrayList pour l'ajout d'equipements*************************
        listeDes= new ArrayList<>();
        listeEtat=new ArrayList<>();
        listeEns= new ArrayList<>();
        listeSousEns=new ArrayList<>();
        listeIdSec=new ArrayList<>();



///////////////////////////////////////////////////////////////////////////////////////////////////////
        //**************************************************************************
        titre1 =  findViewById(R.id.titre1);
        designation =  findViewById(R.id.designation);
        ensemble =  findViewById(R.id.ensemble);
        sousEnsemble =  findViewById(R.id.sousEnsemble);
        txtetat =  findViewById(R.id.etat);
        save =  findViewById(R.id.saveEqp);
        titre1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                designation.setVisibility(View.VISIBLE);
                ensemble.setVisibility(View.VISIBLE);
                sousEnsemble.setVisibility(View.VISIBLE);
                txtetat.setVisibility(View.VISIBLE);
                spinnerDesignation.setVisibility(View.VISIBLE);
                spinnerEns.setVisibility(View.VISIBLE);
                spinnerSousEns.setVisibility(View.VISIBLE);
                etat.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);
                btn_enrg.setVisibility(View.VISIBLE);
                scroll.scrollToDescendant(save);

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate(spinnerDesignation,"Veuillez choisir une machine"))
                    return;
                if(!validate(spinnerEns,"Veuillez choisir un ensemble"))
                    return;
                if(!validate(spinnerSousEns,"Veuillez choisir un sous ensemble"))
                    return;
                else {
                    AddEquipements();
                    designation.setVisibility(View.GONE);
                    ensemble.setVisibility(View.GONE);
                    sousEnsemble.setVisibility(View.GONE);
                    txtetat.setVisibility(View.GONE);
                    spinnerDesignation.setVisibility(View.GONE);
                    spinnerEns.setVisibility(View.GONE);
                    spinnerSousEns.setVisibility(View.GONE);
                    etat.setVisibility(View.GONE);
                    save.setVisibility(View.GONE);
                    clicked = true;
                }
            }
        });
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




        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("");

        //************************Toolbar********************************//
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_drawer_open,R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.menu_DMInt);
        btn_enrg =  findViewById(R.id.btnEnregistrer);
//**************************************************//
        SpecificMenu s=new SpecificMenu();
        s.afficheMenu(role,navigationView);
        //**************************************************//
        etNomDM =  findViewById(R.id.etNomDM);
        etExecuteur =  findViewById(R.id.etExecuteur);
        etDateDM = findViewById(R.id.etDateDM);
        etDesc = findViewById(R.id.etDesc);
        etat =  findViewById(R.id.etEtat);
        spinnerSousEns = findViewById(R.id.spinnerSousEns);
        spinnerDU = findViewById(R.id.spinnerDU);
        spinnerTypeTravail = findViewById(R.id.spinnerTypeTravail);
        spinnerPanneConstate = findViewById(R.id.spinnerPanneConstate);
        spinnerDesignation = findViewById(R.id.spinnerDesignation);
        spinnerEns = findViewById(R.id.spinnerEns);

        etDateDM.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        etDateDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(etDateDM);
            }
        });



        Cursor LDUrg=db.ListeDegreUrgence();
        listDegreUrgence=new ArrayList<>();

        while(LDUrg.moveToNext()){
            listDegreUrgence.add(LDUrg.getString(1));
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listDegreUrgence);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDU.setAdapter(adapter);
        spinnerDU.setOnItemSelectedListener(this);


        //*************************Liste Equipements *************************************
        Cursor ListeEq=db.ListeMachine();
        listeEquipements=new ArrayList<>();

        listeEquipements.add("");
        while(ListeEq.moveToNext()){
            String var = (ListeEq.getString(1)+" | "+ListeEq.getString(2));
            listeEquipements.add(var);

        }
        ArrayAdapter<String> equipements=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listeEquipements);
        equipements.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDesignation.setAdapter(equipements);
        spinnerDesignation.setOnItemSelectedListener(this);



        //*******************************Liste Ensemble***********************************************
        Cursor ListeEn = db.ListeEnsemble();
        Ensemble=new ArrayList< >();
        Ensemble.add("");
        while(ListeEn.moveToNext()){
            Ensemble.add(ListeEn.getString(1));

        }
        ArrayAdapter<String> ensemble=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,Ensemble);
        ensemble.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEns.setAdapter(ensemble);
        spinnerEns.setOnItemSelectedListener(this);


        //***************************************Liste sous ensemble**********************************************
        Cursor ListeSousEns=db.ListeSousEnsemble();
        ssEnsemble=new ArrayList<>();
        ssEnsemble.add("");

        while(ListeSousEns.moveToNext()){
            ssEnsemble.add(ListeSousEns.getString(1));
        }
        ArrayAdapter<String> ssensemble=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,ssEnsemble);
        ssensemble.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSousEns.setAdapter(ssensemble);
        spinnerSousEns.setOnItemSelectedListener(this);

        //*******************************************Liste etat machine******************************************************
        Cursor ListeEtat=db.ListeEtatMachine();
        lEtat=new ArrayList<>();
        lEtat.add("");
        while(ListeEtat.moveToNext()) {
            lEtat.add(ListeEtat.getString(1));
        }
        etatM=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,lEtat);
        etatM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etat.setAdapter(etatM);
        etat.setOnItemSelectedListener(this);

        spinnerDesignation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int desEtat=0;
                if(!spinnerDesignation.getSelectedItem().toString().equals(""))
                {
                    Cursor c=db.idDesEquip(spinnerDesignation.getSelectedItem().toString());
                    while(c.moveToNext())
                    {
                        desEtat =c.getInt(3);

                    }
                    Cursor c1=db.EtatMachine(desEtat);
                    while(c1.moveToNext())
                    {
                        etat.setSelection(etatM.getPosition(c1.getString(0)));
                    }

                }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //*************************Liste Travaux *************************************
        Cursor Liste=db.ListeTravaux();
        listTravaux=new ArrayList<>();

        while(Liste.moveToNext()){
            String var = (Liste.getString(1)+" | "+Liste.getString(2));
            listTravaux.add(var);
        }
        ArrayAdapter<String> travaux=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listTravaux);
        travaux.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeTravail.setAdapter(travaux);
        spinnerTypeTravail.setOnItemSelectedListener(this);
        //*********************************************Liste Employe******************************
        Cursor LEmploye=db.ListeEmploye();
        listEmploye=new ArrayList<>();

        while(LEmploye.moveToNext()){
            String var = (LEmploye.getString(1)+" | "+LEmploye.getString(2));
            listEmploye.add(var);
        }
        ArrayAdapter<String> employe=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listEmploye);
        employe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etNomDM.setAdapter(employe);
        etNomDM.setOnItemSelectedListener(this);
        //***************************Set Selection NomDM******************************************************
        Cursor Executeur=db.NomEmploye(idEmploye);
        while(Executeur.moveToNext())
        {
            nomEX=Executeur.getString(1)+" | "+Executeur.getString(2);
        }
        etNomDM.setSelection(employe.getPosition(nomEX));

        //******************************************************************************************************

        etExecuteur.setAdapter(employe);
        etExecuteur.setOnItemSelectedListener(this);

        //*************************Liste Panne *************************************
        Cursor LPanne=db.ListePanne();
        listPanne=new ArrayList<>();

        while(LPanne.moveToNext()){
            String var = (LPanne.getString(1)+" | "+LPanne.getString(2));
            listPanne.add(var);
        }
        ArrayAdapter<String> panne=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listPanne);
        panne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPanneConstate.setAdapter(panne);
        spinnerPanneConstate.setOnItemSelectedListener(this);


        //****************************Ajouter demande intervention********************************

        btn_enrg.setOnClickListener(new View.OnClickListener() {
            final boolean[] insertEqp = {false};
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                Cursor c2 = db.idUser(etExecuteur.getSelectedItem().toString());
                while (c2.moveToNext()) {
                    idExec = c2.getInt(0);
                }
                Cursor c = db.idUser(etNomDM.getSelectedItem().toString());
                if (c.moveToNext()) {
                    idDem = c.getInt(c.getColumnIndex("idEmploye"));
                }
                Cursor c3 = db.idDegreUrgence(spinnerDU.getSelectedItem().toString());
                if (c3.moveToNext()) {
                    idDegUrg = c3.getInt(c3.getColumnIndex("idDegreUrgence"));
                }
                Cursor c5 = db.idTypeTravaux(spinnerTypeTravail.getSelectedItem().toString());
                if (c5.moveToNext()) {
                    idTT = c5.getInt(c5.getColumnIndex("idTypeTravail"));
                }
                Cursor c4 = db.idClefTravaux(spinnerPanneConstate.getSelectedItem().toString());
                if (c4.moveToNext()) {
                    idCT = c4.getInt(c4.getColumnIndex("idClefTravaux"));
                }


                Cursor c10 = db.cursor(String.valueOf(var));
                if (c10.moveToNext()) {
                    idSo = c10.getInt(c10.getColumnIndex("idSoc"));
                    idAc = c10.getInt(c10.getColumnIndex("idAct"));
                }
                System.out.println(var);


                if (clicked) {
                    if (checkNetworkConnection()) {


                        (new DIActivity.MyAsyncTask()).execute(String.valueOf(idSo),
                                String.valueOf(idAc), String.valueOf(idDem),
                                String.valueOf(idExec), String.valueOf(var),
                                String.valueOf(idDegUrg), "oui", "non", "0"
                                , String.valueOf(idTT), String.valueOf(idCT),
                                etDesc.getText().toString()
                                , etDateDM.getText().toString(),
                                "", etDateDM.getText().toString(),
                                etDateDM.getText().toString(), etDateDM.getText().toString());



                    } else {
                        OrdreTravail di = new OrdreTravail(etDateDM.getText().toString(), idExec, idDem, idDegUrg
                                , idCT, idTT, etDesc.getText().toString());
                        for (int i = 0; i < listeDes.size(); i++) {
                            Equipements e = new Equipements(listeDes.get(i), listeEns.get(i), listeSousEns.get(i), listeEtat.get(i), listeIdSec.get(i));
                            insertEqp[0] = db.insertEquipement(e);

                        }
                        boolean insert = db.insertDI(di, String.valueOf(var));

                        int idOtc = 0;
                        if (insertEqp[0] && insert) {
                            Cursor c11 = db.idOt();
                            if (c11.moveToNext()) {
                                idOtc = c11.getInt(0);
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(DIActivity.this);
                            builder.setTitle("OT N°" + idOtc);
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(DIActivity.this, AccueilActivity.class);
                                    intent.putExtra("nomDM", var);
                                    intent.putExtra("idEmploye", idEmploye);
                                    intent.putExtra("role", role);

                                    startActivity(intent);
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        } else {
                            Toast.makeText(DIActivity.this, "choisir un equipements", Toast.LENGTH_SHORT).show();
                        }


                    }
                } else {
                    Toast.makeText(DIActivity.this, "choisir un equipements", Toast.LENGTH_SHORT).show();
                }
            }
        });

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


            PropertyInfo Nom_Demande=new PropertyInfo();
            Nom_Demande.setName("Nom_Demande");
            Nom_Demande.setType(String.class);
            Nom_Demande.setValue(strings[2]);
            request.addProperty(Nom_Demande);

            PropertyInfo Nom_Emetteur=new PropertyInfo();
            Nom_Emetteur.setName("Nom_Emetteur");
            Nom_Emetteur.setType(String.class);
            Nom_Emetteur.setValue(strings[3]);
            request.addProperty(Nom_Emetteur);

            PropertyInfo Nom_User=new PropertyInfo();
            Nom_User.setName("Nom_User");
            Nom_User.setType(String.class);
            Nom_User.setValue(strings[4]);
            request.addProperty(Nom_User);


            PropertyInfo id_DU=new PropertyInfo();
            id_DU.setName("id_DU");
            id_DU.setType(String.class);
            id_DU.setValue(strings[5]);
            request.addProperty(id_DU);

            PropertyInfo Demande=new PropertyInfo();
            Demande.setName("Demande");
            Demande.setType(String.class);
            Demande.setValue(strings[6]);
            request.addProperty(Demande);

            PropertyInfo Cloture=new PropertyInfo();
            Cloture.setName("Cloture");
            Cloture.setType(String.class);
            Cloture.setValue(strings[7]);
            request.addProperty(Cloture);

            PropertyInfo Approuver=new PropertyInfo();
            Approuver.setName("Approuver");
            Approuver.setType(String.class);
            Approuver.setValue(strings[8]);
            request.addProperty(Approuver);


            PropertyInfo id_TT=new PropertyInfo();
            id_TT.setName("id_TT");
            id_TT.setType(String.class);
            id_TT.setValue(strings[9]);
            request.addProperty(id_TT);

            PropertyInfo id_Panne=new PropertyInfo();
            id_Panne.setName("id_Panne");
            id_Panne.setType(Integer.class);
            id_Panne.setValue(strings[10]);
            request.addProperty(id_Panne);

            PropertyInfo desc=new PropertyInfo();
            desc.setName("desc");
            desc.setType(String.class);
            desc.setValue(strings[11]);
            request.addProperty(desc);

            PropertyInfo Date=new PropertyInfo();
            Date.setName("Date");
            Date.setType(String.class);
            Date.setValue(strings[12]);
            request.addProperty(Date);

            PropertyInfo NBTAttiribue=new PropertyInfo();
            NBTAttiribue.setName("NBTAttiribue");
            NBTAttiribue.setType(String.class);
            NBTAttiribue.setValue(strings[13]);
            request.addProperty(NBTAttiribue);

            PropertyInfo Date_Realisation=new PropertyInfo();
            Date_Realisation.setName("Date_Realisation");
            Date_Realisation.setType(String.class);
            Date_Realisation.setValue(strings[14]);
            request.addProperty(Date_Realisation);

            PropertyInfo Date_Debut=new PropertyInfo();
            Date_Debut.setName("Date_Debut");
            Date_Debut.setType(String.class);
            Date_Debut.setValue(strings[15]);
            request.addProperty(Date_Debut);

            PropertyInfo Date_Fin=new PropertyInfo();
            Date_Fin.setName("Date_Fin");
            Date_Fin.setType(String.class);
            Date_Fin.setValue(strings[16]);
            request.addProperty(Date_Fin);

            PropertyInfo Duree_Travail=new PropertyInfo();
            Duree_Travail.setName("Duree_Travail");
            Duree_Travail.setType(String.class);
            Duree_Travail.setValue(0);
            request.addProperty(Duree_Travail);

            PropertyInfo Trv_Effectue=new PropertyInfo();
            Trv_Effectue.setName("Trv_Effectue");
            Trv_Effectue.setType(String.class);
            Trv_Effectue.setValue("");
            request.addProperty(Trv_Effectue);


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
                    nbt= Integer.parseInt(result.getProperty("NBT").toString());
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
                for (int i = 0; i < listeDes.size(); i++) {
                    (new DIActivity.MyEqpAsyncTask()).execute(String.valueOf(nbt),String.valueOf(listeDes.get(i)),
                            String.valueOf(listeEns.get(i)), String.valueOf(listeSousEns.get(i))
                            , String.valueOf(listeEtat.get(i))
                            ,String.valueOf(listeIdSec.get(i)));}
            }
            else
            {
                Toast.makeText(DIActivity.this,"test"+s,Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);
        }
    }
    private class MyEqpAsyncTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request=new SoapObject(NAMESPACE,METHOD_NAME_EQ);




            PropertyInfo Id_BT=new PropertyInfo();
            Id_BT.setName("Id_BT");
            Id_BT.setType(String.class);
            Id_BT.setValue(strings[0]);
            request.addProperty(Id_BT);

            PropertyInfo Id_Machine=new PropertyInfo();
            Id_Machine.setName("Id_Machine");
            Id_Machine.setType(String.class);
            Id_Machine.setValue(strings[1]);
            request.addProperty(Id_Machine);

            PropertyInfo id_Ens=new PropertyInfo();
            id_Ens.setName("Id_Ensemble");
            id_Ens.setType(String.class);
            id_Ens.setValue(strings[2]);
            request.addProperty(id_Ens);

            PropertyInfo id_SSEns=new PropertyInfo();
            id_SSEns.setName("Id_SSEnsemble");
            id_SSEns.setType(String.class);
            id_SSEns.setValue(strings[3]);
            request.addProperty(id_SSEns);

            PropertyInfo id_Etat=new PropertyInfo();
            id_Etat.setName("Etat_D");
            id_Etat.setType(String.class);
            id_Etat.setValue(strings[4]);
            request.addProperty(id_Etat);

            PropertyInfo Id_Section=new PropertyInfo();
            Id_Section.setName("Id_Section");
            Id_Section.setType(String.class);
            Id_Section.setValue(strings[5]);
            request.addProperty(Id_Section);



            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport=new HttpTransportSE(URL);
            try
            {
                androidHttpTransport.call(SOAP_ACTION_EQ,envelope);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(DIActivity.this);
                builder.setTitle("OT N°"+nbt+" ajouté avec succées");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(DIActivity.this, AccueilActivity.class);
                        intent.putExtra("role", role);
                        intent.putExtra("nomDM", var);
                        intent.putExtra("idEmploye", idEmploye);
                        startActivity(intent);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            else
            {
                Toast.makeText(DIActivity.this,"test"+s,Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);
        }
    }

    @SuppressLint("Range")
    public void AddEquipements()
    {

        View tableRow = LayoutInflater.from(this).inflate(R.layout.layout_table_equipements, null, false);
        TextView machine = (TextView) tableRow.findViewById(R.id.machine);
        TextView etatTxt = (TextView) tableRow.findViewById(R.id.etatM);
        TextView ens = (TextView) tableRow.findViewById(R.id.ens);
        TextView sousEns = (TextView) tableRow.findViewById(R.id.sousEns);
        TextView delete = (TextView) tableRow.findViewById(R.id.delete);
        String nom =spinnerDesignation.getSelectedItem().toString().substring(spinnerDesignation.getSelectedItem().toString().indexOf("|") + 2);

        int idEquip=0, idEns=0, idSSEns=0, idEt=0,idSec=0;
        Cursor c6 = db.idDesEquip2(nom);
        if(c6.moveToNext()){
            idEquip = c6.getInt(c6.getColumnIndex("idMachine"));
            idEt= c6.getInt(c6.getColumnIndex("idEtat"));
            idSec= c6.getInt(c6.getColumnIndex("idSec"));
        }
        Cursor c7 = db.idEns(spinnerEns.getSelectedItem().toString());
        if(c7.moveToNext()){
            idEns = c7.getInt(c7.getColumnIndex("idEnsemble"));
        }
        Cursor c8 = db.idSSEns(spinnerSousEns.getSelectedItem().toString());
        if(c8.moveToNext()){
            idSSEns = c8.getInt(c8.getColumnIndex("idSousEnsemble"));
        }
        machine.setText(nom);
        etatTxt.setText(etat.getSelectedItem().toString());
        ens.setText(spinnerEns.getSelectedItem().toString());
        sousEns.setText(spinnerSousEns.getSelectedItem().toString());
        listeDes.add(idEquip);
        listeEtat.add(idEt);
        listeEns.add(idEns);
        listeSousEns.add(idSSEns);
        listeIdSec.add(idSec);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=1;i<tableLayout.getChildCount();i++) {
                    tableLayout.removeView(tableRow);
                }
            }
        });
        tableLayout.addView(tableRow);
        for(int i=1;i<tableLayout.getChildCount();i++)
        {
            if(i%2==0)
            {
                tableLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#CCD0D0"));
            }
            else
            {
                tableLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#F7F8F8"));

            }
        }




    }





    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
                new TimePickerDialog(DIActivity.this, timeSetListener, cldr.get(Calendar.HOUR_OF_DAY), cldr.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(DIActivity.this, dateSetListener, cldr.get(Calendar.YEAR), cldr.get(Calendar.MONTH), cldr.get(Calendar.DAY_OF_MONTH)).show();
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


    private boolean validate(Spinner s, String message) {
        String var = s.getSelectedItem().toString();
        TextView errorText = (TextView)s.getSelectedView();
        if (var.isEmpty()) {
            errorText.setTextColor(Color.RED);
            errorText.setTextSize(12);
            errorText.setText(message);
            return false;
        } else {
            errorText.setError(null);
            return true;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_home) {
            Intent intent = new Intent(DIActivity.this, StatisticActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_DMInt) {
            Toast.makeText(this, "Vous êtes déja en page de demande d'intervention", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.menu_ListeDM) {
            Intent intent = new Intent(DIActivity.this, AccueilActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_ReleveUsages) {
            Intent intent = new Intent(DIActivity.this, ReleveUsages.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_listeEchDate) {
            Intent intent = new Intent(DIActivity.this, EchanceParDate.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_listeEchUsage) {
            Intent intent = new Intent(this, EcheanceParUsage.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_BonSortie) {
            Intent intent = new Intent(DIActivity.this, BonSortie.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBS) {
            Intent intent = new Intent(DIActivity.this, ListeBonSortie.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_BonInventaire) {
            Intent intent = new Intent(this, BonInventaireActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBI) {
            Intent intent = new Intent(this, ListeBonInventaireActivity.class);
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
        if (id == R.id.menu_ListeBS) {
            Intent intent = new Intent(this, ListeBonSortie.class);
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
            intent.putExtra("role", role);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}