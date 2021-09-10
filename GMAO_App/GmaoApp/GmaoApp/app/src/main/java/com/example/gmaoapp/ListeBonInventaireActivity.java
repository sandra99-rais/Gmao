package com.example.gmaoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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

import com.google.android.material.navigation.NavigationView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class ListeBonInventaireActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TableLayout tableLayout;
    DatabaseHelper db=new DatabaseHelper(this);
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    String role;
    int idEmploye,var;
    /*Web service*/
    final String NAMESPACE = ContactResult.NAMESPACE;
    final String URL = ContactResult.URL;
    /*Liste*/
    final  String METHOD_NAME = "ListeBI";
    final String SOAP_ACTION = "http://tempuri.org/ListeBI";
    ArrayList<String> LNBI=new ArrayList<>();
    ArrayList<String> LDemandeur=new ArrayList<>();
    ArrayList<String> LArticle=new ArrayList<>();
    ArrayList<String> LQtePhy=new ArrayList<>();
    ArrayList<String> LQteTheo=new ArrayList<>();
    ArrayList<String> Lprix=new ArrayList<>();
    ArrayList<String> LNumAtt=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_bon_inventaire);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("");

        //************************Tolbar********************************//
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_drawer_open,R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.menu_ListeBI);
        tableLayout = (TableLayout)findViewById(R.id.tableLayout);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Intent intent = getIntent();
        if (intent.hasExtra("nomDM")){
            var = intent.getIntExtra("nomDM",-1);
            idEmploye=intent.getIntExtra("idEmploye",-1);
            role=intent.getStringExtra("role");

        }


        //**************************************************//
        SpecificMenu s=new SpecificMenu();
        s.afficheMenu(role,navigationView);
        //**************************************************//

        if(checkNetworkConnection())
        {
            new MyAsyncTask().execute();
        }
        else {
            Cursor c = db.ListeBonInventaire();
            while (c.moveToNext()) {
                View tableRow = LayoutInflater.from(this).inflate(R.layout.activity_item_liste_bon_inventaire, null, false);
                TextView BI = (TextView) tableRow.findViewById(R.id.BI);
                TextView Demandeur = (TextView) tableRow.findViewById(R.id.Demandeur);
                TextView Article = (TextView) tableRow.findViewById(R.id.Article);
                TextView qte = (TextView) tableRow.findViewById(R.id.qte);
                TextView qteTheo = (TextView) tableRow.findViewById(R.id.qteTheo);
                TextView prixU = (TextView) tableRow.findViewById(R.id.prixU);
                TextView biAtt = (TextView) tableRow.findViewById(R.id.biAtt);


                BI.setText(String.valueOf(c.getInt(0)));
                Demandeur.setText(c.getString(1));
                Article.setText(c.getString(3));
                qte.setText(String.valueOf(c.getInt(4)));
                qteTheo.setText(String.valueOf(c.getInt(7)));
                prixU.setText(String.valueOf(c.getInt(6)));
                biAtt.setText(c.getString(8));
                if(biAtt.getText().toString().equals("anyType{}"))
                {
                    biAtt.setText(BI.getText().toString()+" "+Demandeur.getText().toString());
                }

                tableLayout.addView(tableRow);


                for (int i = 1; i < tableLayout.getChildCount(); i++) {
                    if (i % 2 == 0) {
                        tableLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#CCD0D0"));
                    } else {
                        tableLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#F7F8F8"));

                    }
                }
            }
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

    public boolean checkNetworkConnection()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }

    private class MyAsyncTask extends AsyncTask<String,Void,String>
    {
        String NBI;
        String qteTheo;
        String demandeur;
        String article;
        String qtePhy;
        String prix,Num_Att;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request=new SoapObject(NAMESPACE,METHOD_NAME);



            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport=new HttpTransportSE(URL);

            try
            {

                androidHttpTransport.call(SOAP_ACTION,envelope);

                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject res = (SoapObject) result.getProperty(0);

                for (int i = 0; i < res.getPropertyCount(); i++) {
                    Object property = res.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject category_list = (SoapObject) property;
                        NBI = category_list.getProperty("NBI").toString();
                        qteTheo = category_list.getProperty("qteTheo").toString();
                        demandeur = category_list.getProperty("demandeur").toString();
                        article = category_list.getProperty("article").toString();
                        qtePhy = category_list.getProperty("qtePhy").toString();
                        prix = category_list.getProperty("prixU").toString();
                        Num_Att = category_list.getProperty("Num_Att").toString();

                        LNBI.add(NBI);
                        LQteTheo.add(qteTheo);
                        LDemandeur.add(demandeur);
                        LArticle.add(article);
                        LQtePhy.add(qtePhy);
                        Lprix.add(prix);
                        if(Num_Att.equals("anyType{}"))
                        {
                            LNumAtt.add(NBI+" "+demandeur);
                        }
                        else
                        {
                            LNumAtt.add(Num_Att);

                        }
                    }


                }



            } catch (Exception e) {
                e.printStackTrace();
                return  e.toString();
            }
            return NBI;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null)
            {


                affiche();


            }
            else
            {
                Toast.makeText(ListeBonInventaireActivity.this, "No Date To Show", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }
    public void affiche()
    {
        for(int  j=0;j<LNBI.size();j++)
        {
            View tableRow1 = LayoutInflater.from(this).inflate(R.layout.activity_item_liste_bon_inventaire, null, false);
            TextView BI = (TextView) tableRow1.findViewById(R.id.BI);
            TextView Demandeur = (TextView) tableRow1.findViewById(R.id.Demandeur);
            TextView Article = (TextView) tableRow1.findViewById(R.id.Article);
            TextView qte = (TextView) tableRow1.findViewById(R.id.qte);
            TextView qteTheo = (TextView) tableRow1.findViewById(R.id.qteTheo);
            TextView prixU = (TextView) tableRow1.findViewById(R.id.prixU);
            TextView biAtt = (TextView) tableRow1.findViewById(R.id.biAtt);


            BI.setText(LNBI.get(j));
            Demandeur.setText(LDemandeur.get(j));
            qteTheo.setText(LQteTheo.get(j));
            Article.setText(LArticle.get(j));
            qte.setText(LQtePhy.get(j));
            prixU.setText(Lprix.get(j));
            biAtt.setText(LNumAtt.get(j));

            tableLayout.addView(tableRow1);

            for (int i = 1; i < tableLayout.getChildCount(); i++) {
                if (i % 2 == 0) {
                    tableLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#CCD0D0"));
                } else {
                    tableLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#F7F8F8"));

                }
            }
        }
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_home){
            Intent intent = new Intent(ListeBonInventaireActivity.this,StatisticActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if(id == R.id.menu_DMInt){
            Intent intent = new Intent(ListeBonInventaireActivity.this,DIActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if(id == R.id.menu_ListeDM){
            Intent intent = new Intent(ListeBonInventaireActivity.this,AccueilActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_listeEchDate) {
            Intent intent = new Intent(ListeBonInventaireActivity.this,EchanceParDate.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBS) {
            Intent intent = new Intent(ListeBonInventaireActivity.this,ListeBonSortie.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_ReleveUsages) {
            Intent intent = new Intent(ListeBonInventaireActivity.this, ReleveUsages.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_listeEchUsage) {
            Intent intent = new Intent(ListeBonInventaireActivity.this, EcheanceParUsage.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_BonSortie) {
            Intent intent = new Intent(ListeBonInventaireActivity.this, BonSortie.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_BonInventaire) {
            Intent intent = new Intent(ListeBonInventaireActivity.this, BonInventaireActivity.class);
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