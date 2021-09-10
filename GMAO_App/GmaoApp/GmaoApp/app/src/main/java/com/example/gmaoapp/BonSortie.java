package com.example.gmaoapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.gmaoapp.Models.DetailMvt;
import com.example.gmaoapp.Models.Mouvement;
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

public class BonSortie extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    DatabaseHelper db=new DatabaseHelper(this);
    EditText etNBS,etDateBon,etQteStock,PU,QteVoulue,etTotal;
    Spinner etNBT,spinnerDemandeur,spinnerArticle;
    int NBS,idDemandeur=0,NumBs,idPd,Qte;
    double pu;
    int idSo=0, idAc=0;
    ArrayList<Integer> listeOt;
    ArrayList<String> listeDemandeur,listeProduit;
    Button save;
    int qteVoulu,idProduit,numAtt=0;
    Double PrixU,Total;
    String codeProduit;
    TableLayout tableLayout;
    TextView txtCodeArt,txtDesArt,txtQteMvt,txtPu,txtTotal;
    ArrayList<Integer> qteArray;
    ArrayList<Double>  totalArray,puArray;
    ArrayList<String>  desginationArray;
    TextView titre1,article,QteStock,txtQte,Prix,txttotal,enregistrer,delete;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    String natt;
    String role;
    int idEmploye, var;
    boolean clicked=false;
    ScrollView scroll;
    /*Web service*/
    final String NAMESPACE = ContactResult.NAMESPACE;
    final String URL = ContactResult.URL;
    /*BS*/
    final  String METHOD_NAME = "AjouterBS";
    final String SOAP_ACTION = "http://tempuri.org/AjouterBS";
    /*Det BS*/
    final  String METHOD_NAME_Det = "AjouterDetBS";
    final String SOAP_ACTION_Det = "http://tempuri.org/AjouterDetBS";
    String ReturnResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bon_sortie);
        etNBS=(EditText)findViewById(R.id.etNBS);
        etNBS.setEnabled(false);
        etNBT=(Spinner)findViewById(R.id.etNBT);
        etDateBon=(EditText)findViewById(R.id.etDateBon);
        etQteStock=(EditText)findViewById(R.id.etQteStock);
        PU=(EditText)findViewById(R.id.PU);
        QteVoulue=(EditText) findViewById(R.id.QteVoulue);
        etTotal=(EditText) findViewById(R.id.etTotal);
        QteVoulue.setText(String.valueOf(0));
        spinnerDemandeur=(Spinner)findViewById(R.id.spinnerDemandeur);
        spinnerArticle=(Spinner)findViewById(R.id.spinnerArticle);
        save=(Button)findViewById(R.id.save);
        scroll=findViewById(R.id.scroll);
        enregistrer=(TextView) findViewById(R.id.enregistrer);
        titre1=(TextView)findViewById(R.id.titre1);
        article=(TextView)findViewById(R.id.article);
        QteStock=(TextView)findViewById(R.id.QteStock);
        txtQte=(TextView)findViewById(R.id.Qte);
        Prix=(TextView)findViewById(R.id.Prix);
        txttotal=(TextView)findViewById(R.id.Total);
        TextView textView = (TextView) findViewById(R.id.textView10);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //*****************Menu*************************************************/
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
        navigationView.setCheckedItem(R.id.menu_BonSortie);
        //********************************Fin Menu******************************************/
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

        titre1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                article.setVisibility(View.VISIBLE);
                spinnerArticle.setVisibility(View.VISIBLE);

                txtQte.setVisibility(View.VISIBLE);
                QteVoulue.setVisibility(View.VISIBLE);


                enregistrer.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);
                scroll.scrollToDescendant(save);


            }
        });

        PU.setEnabled(false);
        etQteStock.setEnabled(false);

        qteArray=new ArrayList<>();
        totalArray=new ArrayList<>();
        puArray=new ArrayList<>();
        desginationArray=new ArrayList<>();


        //*********************************ID BON DE SORTIE**************************************************************
        Cursor c=db.idBS();
        while(c.moveToNext())
        {
            NBS=c.getInt(0);
        }
        NumBs=NBS+1;
        etNBS.setText(String.valueOf(NumBs));
        //***************************************LISTE ID OT********************************************************
        Cursor ListeOt=db.listeIdOt();
        listeOt=new ArrayList<>();
        while(ListeOt.moveToNext())
        {
            int OT=ListeOt.getInt(0);
            listeOt.add(OT);
        }
        ArrayAdapter<Integer> idOt=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listeOt);
        idOt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etNBT.setAdapter(idOt);
        etNBT.setOnItemSelectedListener(this);
        //************************DATE BON DE SORTIE*********************************************************************
        etDateBon.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        etDateBon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(etDateBon);
            }
        });
        //****************************DEMANDEUR *************************************************************************
        Cursor LDemandeur=db.ListeEmploye();
        listeDemandeur=new ArrayList<>();

        while(LDemandeur.moveToNext()){
            String var = LDemandeur.getString(2);
            listeDemandeur.add(var);
        }
        ArrayAdapter<String> demandeur=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listeDemandeur);
        demandeur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDemandeur.setAdapter(demandeur);
        spinnerDemandeur.setOnItemSelectedListener(this);
        //**************************************LISTE ARTICLE*************************************************************
        Cursor LProduit=db.affichePdt();
        listeProduit=new ArrayList<>();

        while(LProduit.moveToNext()){
            String var = LProduit.getString(0);
            listeProduit.add(var);
        }
        ArrayAdapter<String> produit=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listeProduit);
        produit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArticle.setAdapter(produit);
        //***************************QTE STOCK ET PU********************************************************************

        spinnerArticle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor idPdt = db.idPdt(spinnerArticle.getSelectedItem().toString());
                while (idPdt.moveToNext()) {
                    idPd = idPdt.getInt(0);

                }
                Cursor QtePu = db.afficheQtePU(idPd);
                while (QtePu.moveToNext()) {
                    Qte = QtePu.getInt(0);
                    pu = QtePu.getInt(1);

                }
                etQteStock.setText(String.valueOf(Qte));
                PU.setText(String.valueOf(pu));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //************************************Total*************************************************************************
        QteVoulue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    qteVoulu=Integer.parseInt(QteVoulue.getText().toString());
                } catch(NumberFormatException ex){ // handle your exception
                    qteVoulu=0;
                }

                PrixU=Double.parseDouble(PU.getText().toString());
                Total=qteVoulu*PrixU;
                etTotal.setText(String.valueOf(Total));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //*********************************Table Detail Bon Sortie*****************************************************************
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    qteVoulu=Integer.parseInt(QteVoulue.getText().toString());
                } catch(NumberFormatException ex){ // handle your exception
                    qteVoulu=0;
                }
                if (qteVoulu == 0) {
                    Toast.makeText(BonSortie.this, "la quantité voulue doit etre supérieure à 0", Toast.LENGTH_SHORT).show();

                }
                else {
                    BonS();
                    article.setVisibility(View.GONE);
                    spinnerArticle.setVisibility(View.GONE);
                    QteStock.setVisibility(View.GONE);
                    etQteStock.setVisibility(View.GONE);
                    txtQte.setVisibility(View.GONE);
                    QteVoulue.setVisibility(View.GONE);
                    Prix.setVisibility(View.GONE);
                    PU.setVisibility(View.GONE);
                    txttotal.setVisibility(View.GONE);
                    etTotal.setVisibility(View.GONE);
                    enregistrer.setVisibility(View.GONE);
                    save.setVisibility(View.VISIBLE);
                    clicked=true;

                }
            }
        });


        //**********************************INSERT BON DE SORTIE************************************************************
        save.setOnClickListener(new View.OnClickListener() {

            final boolean[] insertDetail = {false};


            @Override
            public void onClick(View v) {


                natt = NumBs+""+idDemandeur;
                Cursor idDem = db.IdDEMANDEUR(spinnerDemandeur.getSelectedItem().toString());
                while (idDem.moveToNext()) {
                    idDemandeur = idDem.getInt(idDem.getColumnIndex("idEmploye"));
                }
                Cursor c19 = db.cursor(String.valueOf(var));
                if(c19.moveToNext()){
                    idSo =  c19.getInt(c19.getColumnIndex("idSoc"));
                    idAc =  c19.getInt(c19.getColumnIndex("idAct"));
                }

                if (checkNetworkConnection()) {
                    (new BonSortie.MyAsyncTask()).execute(String.valueOf(idSo), String.valueOf(idAc), etNBS.getText().toString(), String.valueOf(idDemandeur),
                            etDateBon.getText().toString(), "");
                } else {

                    if (clicked) {

                        Mouvement m = new Mouvement(Integer.parseInt(etNBS.getText().toString()),
                                Integer.parseInt(etNBT.getSelectedItem().toString()),
                                etDateBon.getText().toString(),
                                idDemandeur, "BS");
                        DetailMvt dm ;
                        for (int i = 0; i < totalArray.size(); i++) {

                            Cursor idPdt = db.idPdt(desginationArray.get(i));
                            while (idPdt.moveToNext()) {
                                idProduit = idPdt.getInt(0);
                            }

                            dm = new DetailMvt(Integer.parseInt(etNBS.getText().toString()), idProduit, qteArray.get(i)
                                    , puArray.get(i), totalArray.get(i));

                            insertDetail[0] = db.insertDetMvt(dm,idSo,idAc);
                        }


                        boolean insert = db.insertMvt(m,natt);

                        if (insertDetail[0] && insert) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(BonSortie.this);
                            builder.setTitle("Bon de sortie avec N°" +NumBs+" ajouté avec succées");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(BonSortie.this, ListeBonSortie.class);
                                    intent.putExtra("nomDM", var);
                                    intent.putExtra("role", role);
                                    intent.putExtra("idEmploye", idEmploye);
                                    startActivity(intent);
                                    NumBs = Integer.parseInt(etNBS.getText().toString()) + 1;
                                    etNBS.setText(String.valueOf(NumBs));
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        } else {
                            Toast.makeText(BonSortie.this, "erreur", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(BonSortie.this, "il faut choisir un article", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });




    }
    public void BonS()
    {

        View tableRow = LayoutInflater.from(this).inflate(R.layout.layout_item_bon_sortie, null, false);
        txtCodeArt = (TextView) tableRow.findViewById(R.id.txtCodeArt);
        txtDesArt = (TextView) tableRow.findViewById(R.id.txtDesArt);
        txtQteMvt = (TextView) tableRow.findViewById(R.id.txtQteMvt);
        txtPu = (TextView) tableRow.findViewById(R.id.txtPu);
        txtTotal = (TextView) tableRow.findViewById(R.id.txtTotal);
        delete = (TextView) tableRow.findViewById(R.id.delete);

        txtDesArt.setText(spinnerArticle.getSelectedItem().toString());
        Cursor idPdt = db.idPdt(spinnerArticle.getSelectedItem().toString());
        while (idPdt.moveToNext()) {
            codeProduit = idPdt.getString(1);
        }
        txtCodeArt.setText(String.valueOf(codeProduit));

        txtQteMvt.setText(QteVoulue.getText().toString());
        txtPu.setText(PU.getText().toString());
        txtTotal.setText(etTotal.getText().toString());
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
        qteArray.add(Integer.parseInt(txtQteMvt.getText().toString()));
        totalArray.add(Double.parseDouble(txtTotal.getText().toString()));
        puArray.add(Double.parseDouble(txtPu.getText().toString()));
        desginationArray.add(txtDesArt.getText().toString());



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

            PropertyInfo numbt=new PropertyInfo();
            numbt.setName("numbt");
            numbt.setType(String.class);
            numbt.setValue(strings[2]);
            request.addProperty(numbt);

            PropertyInfo Id_Employee=new PropertyInfo();
            Id_Employee.setName("Id_Employee");
            Id_Employee.setType(String.class);
            Id_Employee.setValue(strings[3]);
            request.addProperty(Id_Employee);

            PropertyInfo Date_Mvt=new PropertyInfo();
            Date_Mvt.setName("Date_Mvt");
            Date_Mvt.setType(String.class);
            Date_Mvt.setValue(strings[4]);
            request.addProperty(Date_Mvt);

            PropertyInfo Num_Att=new PropertyInfo();
            Num_Att.setName("Num_Att");
            Num_Att.setType(String.class);
            Num_Att.setValue(strings[5]);
            request.addProperty(Num_Att);



            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport=new HttpTransportSE(URL);
            try
            {
                androidHttpTransport.call(SOAP_ACTION,envelope);
                SoapObject result= (SoapObject) envelope.getResponse();
                if (result!=null)
                {
                    ReturnResult= result.getProperty("result").toString();
                    numAtt= Integer.parseInt(result.getProperty("NBT").toString());
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
                for(int i=0;i<totalArray.size();i++)
                {

                    Cursor idPdt = db.idPdt(desginationArray.get(i));
                    while (idPdt.moveToNext()) {
                        idProduit = idPdt.getInt(0);
                    }

                    (new BonSortie.DetAsyncTask()).execute(String.valueOf(idSo),String.valueOf(idAc),
                            String.valueOf(numAtt), String.valueOf(idProduit), String.valueOf(qteArray.get(i)));
                }

            }
            else
            {
                Toast.makeText(BonSortie.this,""+s,Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);
        }
    }
    private class DetAsyncTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request=new SoapObject(NAMESPACE,METHOD_NAME_Det);

            PropertyInfo id_Soc=new PropertyInfo();
            id_Soc.setName("id_Soc");
            id_Soc.setType(String.class);
            id_Soc.setValue(strings[0]);
            request.addProperty(id_Soc);

            PropertyInfo idAct=new PropertyInfo();
            idAct.setName("id_Act");
            idAct.setType(String.class);
            idAct.setValue(strings[1]);
            request.addProperty(idAct);

            PropertyInfo Id_Mvt=new PropertyInfo();
            Id_Mvt.setName("Id_Mvt");
            Id_Mvt.setType(String.class);
            Id_Mvt.setValue(strings[2]);
            request.addProperty(Id_Mvt);


            PropertyInfo Id_Pdt=new PropertyInfo();
            Id_Pdt.setName("Id_Pdt");
            Id_Pdt.setType(String.class);
            Id_Pdt.setValue(strings[3]);
            request.addProperty(Id_Pdt);

            PropertyInfo Qty_Mvt_Ua=new PropertyInfo();
            Qty_Mvt_Ua.setName("Qty_Mvt_Ua");
            Qty_Mvt_Ua.setType(String.class);
            Qty_Mvt_Ua.setValue(strings[4]);
            request.addProperty(Qty_Mvt_Ua);


            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport=new HttpTransportSE(URL);
            try
            {
                androidHttpTransport.call(SOAP_ACTION_Det,envelope);
                SoapObject result= (SoapObject) envelope.getResponse();
                if (result!=null)
                {
                    ReturnResult= result.getProperty("result").toString();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(BonSortie.this);
                builder.setTitle("Bon de sortie avec N°"+numAtt+" ajouté avec succées");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(BonSortie.this, ListeBonSortie.class);
                        intent.putExtra("nomDM", var);
                        intent.putExtra("role", role);
                        intent.putExtra("idEmploye", idEmploye);
                        startActivity(intent);
                        NumBs = Integer.parseInt(etNBS.getText().toString()) + 1;
                        etNBS.setText(String.valueOf(NumBs));
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
            else
            {
                Toast.makeText(BonSortie.this,""+s,Toast.LENGTH_SHORT).show();

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
                new TimePickerDialog(BonSortie.this, timeSetListener, cldr.get(Calendar.HOUR_OF_DAY), cldr.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(BonSortie.this, dateSetListener, cldr.get(Calendar.YEAR), cldr.get(Calendar.MONTH), cldr.get(Calendar.DAY_OF_MONTH)).show();
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_home) {
            Intent intent = new Intent(BonSortie.this, StatisticActivity.class);
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
        if (id == R.id.menu_DMInt) {
            Intent intent = new Intent(BonSortie.this, DIActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeDM) {
            Intent intent = new Intent(BonSortie.this, AccueilActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ReleveUsages) {
            Intent intent = new Intent(BonSortie.this, ReleveUsages.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_listeEchDate) {
            Intent intent = new Intent(BonSortie.this, EchanceParDate.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_BonSortie) {
            Toast.makeText(this, "Vous êtes déja en page de bon sortie", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.menu_ListeBS) {
            Intent intent = new Intent(BonSortie.this, ListeBonSortie.class);
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
        if (id == R.id.menu_BonInventaire) {
            Intent intent = new Intent(BonSortie.this, BonInventaireActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBI) {
            Intent intent = new Intent(BonSortie.this, ListeBonInventaireActivity.class);
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