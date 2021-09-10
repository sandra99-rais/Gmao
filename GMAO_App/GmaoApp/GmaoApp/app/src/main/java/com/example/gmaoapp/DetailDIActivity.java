package com.example.gmaoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.app.ActivityCompat;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
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
import java.util.Date;

public class DetailDIActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DatabaseHelper db=new DatabaseHelper(this);
    TextView detail, date, demandeur, executeur, panne, typetravail, desc;
    int var ;
    int id;
    String role;
    int idEmploye;
    /*Menu*/
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TableLayout tableLayout;
    /*Web service*/
    final String NAMESPACE = ContactResult.NAMESPACE;
    final String URL = ContactResult.URL;
    /*Liste Eqp*/
    final  String METHOD_NAME = "ListeEqpDMByID";
    final String SOAP_ACTION = "http://tempuri.org/ListeEqpDMByID";
    /*OT*/
    final  String METHOD_NAME_OT = "ListeDMById";
    final String SOAP_ACTION_OT = "http://tempuri.org/ListeDMById";


    ArrayList<String> LMachineEqp = new ArrayList<>();
    ArrayList<String> LEnsembleEqp = new ArrayList<>();
    ArrayList<String> LSSEnsEqp = new ArrayList<>();
    ArrayList<String> LEtatEqp = new ArrayList<>();
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_d_i);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        //******************Menu**********************/
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
        //********************Fin Menu************************************************
        detail=(TextView)findViewById(R.id.detail);
        date=(TextView)findViewById(R.id.etdatedm);
        demandeur=(TextView)findViewById(R.id.etNomDM);
        executeur=(TextView)findViewById(R.id.etExecuteur);
        panne=(TextView)findViewById(R.id.etpanne);
        typetravail=(TextView)findViewById(R.id.ettypetrav);
        desc=(TextView)findViewById(R.id.etDesc);
        tableLayout=(TableLayout)findViewById(R.id.tableLayout);
        TextView textView = (TextView) findViewById(R.id.detail);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);



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
        Intent intentID = getIntent();
        id = intentID.getIntExtra("idDI",-1);
        detail.setText("Demande d'intervention N°"+id);
        if(checkNetworkConnection())
        {
            new ListByIdAsyncTask().execute(String.valueOf(id));
            new ListeEqpAsyncTask().execute(String.valueOf(id));
        }
        else{
        OrdreTravail di=db.DemandeByid(id);
        date.setText(di.getDateDemande());
        desc.setText(di.getDesc());
        if(di.getIdDemandeur()!=0)
        {
            Cursor Demandeur=db.NomEmploye(di.getIdDemandeur());
            Demandeur.moveToNext();
            demandeur.setText(Demandeur.getString(Demandeur.getColumnIndex("nomEmploye")));

        }

        Cursor Executeur=db.NomEmploye(di.getIdExecuteur());
        Executeur.moveToNext();
        executeur.setText(Executeur.getString(2));
        if(di.getIdPanne()!=0)
        {
        Cursor typeP = db.typePanne(di.getIdPanne());
        typeP.moveToNext();
        panne.setText(typeP.getString(2));
        }
        Cursor t=db.trav(di.getIdTypeTrav());
        t.moveToNext();
        typetravail.setText(t.getString(2));

        Cursor equipements=db.ListeEquipementsDI(id);
        while (equipements.moveToNext()){
            View tableRow = LayoutInflater.from(this).inflate(R.layout.layout_table_detail_di, null, false);
            TextView machine = (TextView) tableRow.findViewById(R.id.machine);
            TextView etatM = (TextView) tableRow.findViewById(R.id.etatM);
            TextView ens = (TextView) tableRow.findViewById(R.id.ens);
            TextView sousEns = (TextView) tableRow.findViewById(R.id.sousEns);


            Cursor m=db.DesMachine(equipements.getInt(2));
            while(m.moveToNext()) {
                machine.setText(m.getString(0));

            }
            if(equipements.getInt(2)==0 )
            {
                ens.setText("");

            }
            if(equipements.getInt(3)==0)
            {
                sousEns.setText("");
            }
            if(equipements.getInt(4)==0)
            {
                etatM.setText("");
            }
            else
            {
                Cursor ensemble=db.DesEnsemble(equipements.getInt(2));
                while(ensemble.moveToNext())
                {
                    ens.setText(ensemble.getString(1));

                }

                Cursor sousens=db.DesSousEnsemble(equipements.getInt(3));
                while(sousens.moveToNext())
                {
                    sousEns.setText(sousens.getString(1));

                }
                @SuppressLint("Range") Cursor etat=db.EtatMachine(equipements.getInt(equipements.getColumnIndex("idEtat")));
                while(etat.moveToNext())
                {
                    etatM.setText(etat.getString(0));

                }
            }
            tableLayout.addView(tableRow);
        }


    }}
    public boolean checkNetworkConnection()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }
    private class ListByIdAsyncTask extends AsyncTask<String, Void, String> {
        String Id_Type_Trv,Id_Clef_trv,Nom_Emetteur,Nom_Demandeur,desc,Date_Demande;
        Date dateDM= null;


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_OT);

            PropertyInfo NBT=new PropertyInfo();
            NBT.setName("NBT");
            NBT.setType(String.class);
            NBT.setValue(strings[0]);
            request.addProperty(NBT);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(SOAP_ACTION_OT, envelope);
                SoapObject result = (SoapObject) envelope.getResponse();
                if (result != null) {
                    Date_Demande = result.getProperty("Date_Demande").toString();
                    Nom_Emetteur = result.getProperty("Nom_Emetteur").toString();
                    Nom_Demandeur = result.getProperty("Nom_Demandeur").toString();
                    Id_Clef_trv =result.getProperty("Id_Clef_trv").toString();
                    Id_Type_Trv = result.getProperty("Id_Type_Trv").toString();



                    try {
                        dateDM = formatter.parse(Date_Demande);

                    }
                    catch (ParseException e)
                    {
                        e.printStackTrace();
                    }
                    formatter.applyPattern("dd/MM/yyyy HH:mm");


                }


            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return Nom_Emetteur;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s!=null) {
                    date.setText(formatter.format(dateDM));
                    executeur.setText(Nom_Emetteur);
                    demandeur.setText(Nom_Demandeur);
                    typetravail.setText(Id_Type_Trv);
                    panne.setText(Id_Clef_trv);
                    DetailDIActivity.this.desc.setText(desc);
            }
            super.onPostExecute(s);
        }
    }
    private class ListeEqpAsyncTask extends AsyncTask<String, Void, String> {
        String Id_Machine,Id_Ensemble,Id_SSEnsemble,Etat_D;


        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            PropertyInfo NBT=new PropertyInfo();
            NBT.setName("NBT");
            NBT.setType(String.class);
            NBT.setValue(strings[0]);
            request.addProperty(NBT);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAP_ACTION, envelope);

                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject res = (SoapObject) result.getProperty(0);

                for (int i = 0; i < res.getPropertyCount(); i++) {
                    Object property = res.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject category_list = (SoapObject) property;
                        Id_Machine = category_list.getProperty("Id_Machine").toString();
                        Id_Ensemble = category_list.getProperty("Id_Ensemble").toString();
                        Id_SSEnsemble =category_list.getProperty("Id_SSEnsemble").toString();
                        Etat_D = category_list.getProperty("Id_Etat").toString();


                        LMachineEqp.add(Id_Machine);
                        LEnsembleEqp.add(Id_Ensemble);
                        LSSEnsEqp.add(Id_SSEnsemble);
                        LEtatEqp.add(Etat_D);

                    }
                }
                System.out.println(LMachineEqp);
                System.out.println(LEnsembleEqp);
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return String.valueOf(Id_Machine);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {



                for(int i=0;i<LMachineEqp.size();i++) {
                    View tableRow = LayoutInflater.from(DetailDIActivity.this).inflate(R.layout.layout_table_detail_di, null, false);
                    TextView machine = (TextView) tableRow.findViewById(R.id.machine);
                    TextView etatM = (TextView) tableRow.findViewById(R.id.etatM);
                    TextView ens = (TextView) tableRow.findViewById(R.id.ens);
                    TextView sousEns = (TextView) tableRow.findViewById(R.id.sousEns);
                    machine.setText(LMachineEqp.get(i));
                    etatM.setText(LEtatEqp.get(i));
                    ens.setText(LEnsembleEqp.get(i));
                    sousEns.setText(LSSEnsEqp.get(i));
                    tableLayout.addView(tableRow);

                }


            } else {

                Toast.makeText(DetailDIActivity.this, "No Date To Show"+s, Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
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
            Intent intent = new Intent(DetailDIActivity.this, StatisticActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_DMInt) {
            Intent intent = new Intent(DetailDIActivity.this, DIActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeDM) {
            Intent intent = new Intent(DetailDIActivity.this, AccueilActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ReleveUsages) {
            Intent intent = new Intent(DetailDIActivity.this, ReleveUsages.class);
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
        if (id == R.id.consultation) {
            Intent intent = new Intent(this, ConsulterArticles.class);
            intent.putExtra("role", role);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_listeEchDate) {
            Intent intent = new Intent(DetailDIActivity.this, EchanceParDate.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_BonSortie) {
            Intent intent = new Intent(DetailDIActivity.this, BonSortie.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBS) {
            Intent intent = new Intent(DetailDIActivity.this, ListeBonSortie.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_BonInventaire) {
            Intent intent = new Intent(DetailDIActivity.this, BonInventaireActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBI) {
            Intent intent = new Intent(DetailDIActivity.this, ListeBonInventaireActivity.class);
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