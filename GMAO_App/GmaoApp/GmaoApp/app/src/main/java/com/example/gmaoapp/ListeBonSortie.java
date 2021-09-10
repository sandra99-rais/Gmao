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

public class ListeBonSortie extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TableLayout tableLayout;
    DatabaseHelper db=new DatabaseHelper(this);
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    int var ;
    int idEmploye;
    String role;
    /*Web service*/
    final String NAMESPACE = ContactResult.NAMESPACE;
    final String URL = ContactResult.URL;
    /*Liste*/
    final  String METHOD_NAME = "ListeBS";
    final String SOAP_ACTION = "http://tempuri.org/ListeBS";
    View tableRow1;
    ArrayList<String> LNBS=new ArrayList<>();
    ArrayList<String> LNOT=new ArrayList<>();
    ArrayList<String> LDemandeur=new ArrayList<>();
    ArrayList<String> LArticle=new ArrayList<>();
    ArrayList<String> LQtePhy=new ArrayList<>();
    ArrayList<String> LTotal=new ArrayList<>();
    ArrayList<String> LNumAtt=new ArrayList<>();
    double TTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_bon_sortie);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        toolbar.setTitle("");


        //************************Tolbar********************************//
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_drawer_open,R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.menu_ListeBS);
        tableLayout = (TableLayout)findViewById(R.id.tableLayout);

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
            Cursor c = db.ListeBonSortie();
            while (c.moveToNext()) {
                View tableRow = LayoutInflater.from(this).inflate(R.layout.activity_item_liste_bon_sortie, null, false);
                TextView BS = (TextView) tableRow.findViewById(R.id.BS);
                TextView Demandeur = (TextView) tableRow.findViewById(R.id.Demandeur);
                TextView OT = (TextView) tableRow.findViewById(R.id.OT);
                TextView Article = (TextView) tableRow.findViewById(R.id.Article);
                TextView qte = (TextView) tableRow.findViewById(R.id.qte);
                TextView total = (TextView) tableRow.findViewById(R.id.total);
                TextView bsAtt = (TextView) tableRow.findViewById(R.id.bsAtt);


                BS.setText(String.valueOf(c.getInt(0)));
                Demandeur.setText(c.getString(1));
                OT.setText(String.valueOf(c.getInt(2)));
                Article.setText(c.getString(3));
                qte.setText(String.valueOf(c.getInt(4)));
                total.setText(String.valueOf(c.getDouble(5)));
                bsAtt.setText(c.getString(6));
                if(bsAtt.getText().toString().equals("anyType{}"))
                {
                    bsAtt.setText(BS.getText().toString()+" "+Demandeur.getText().toString());
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
            Cursor c1 = db.TotalBS();
            while (c1.moveToNext()) {
                View tableRow2 = LayoutInflater.from(this).inflate(R.layout.total_bs_item, null, false);
                TextView prixTotal = (TextView) tableRow2.findViewById(R.id.prixTotal);


                prixTotal.setText(String.valueOf(c1.getDouble(0)));


                tableLayout.addView(tableRow2);

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
        String NBS;
        String NOT;
        String demandeur;
        String article;
        String qtePhy;
        String prixTotal,Num_Att;

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
                        NBS = category_list.getProperty("NBS").toString();
                        NOT = category_list.getProperty("numbt").toString();
                        demandeur = category_list.getProperty("demandeur").toString();
                        article = category_list.getProperty("article").toString();
                        qtePhy = category_list.getProperty("qtePhy").toString();
                        prixTotal = category_list.getProperty("prixTotal").toString();
                        Num_Att = category_list.getProperty("Num_Att").toString();
                        LNBS.add(NBS);
                        LNOT.add(NOT);
                        LDemandeur.add(demandeur);
                        LArticle.add(article);
                        LQtePhy.add(qtePhy);
                        LTotal.add(prixTotal);
                        TTotal+= Double.parseDouble(LTotal.get(i));
                        if(Num_Att.equals("anyType{}"))
                        {
                            LNumAtt.add(NBS+" "+demandeur);
                        }
                        else
                        {
                            LNumAtt.add(Num_Att);

                        }
                    }


                }


                System.out.println(res.getPropertyCount());
                System.out.println(LNBS);
                System.out.println(LNOT);
                System.out.println(LDemandeur);
                System.out.println(LArticle);
                System.out.println(LQtePhy);
                System.out.println(LTotal);




            } catch (Exception e) {
                e.printStackTrace();
                return  e.toString();
            }
            return NBS;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null)
            {


                affiche();


            }
            else
            {
                Toast.makeText(ListeBonSortie.this, "No Date To Show", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }
    public void affiche()
    {
        for(int  j=0;j<LNBS.size();j++)
        {
            tableRow1= LayoutInflater.from(this).inflate(R.layout.activity_item_liste_bon_sortie, null, false);

            TextView BS = (TextView) tableRow1.findViewById(R.id.BS);
            TextView Demandeur = (TextView) tableRow1.findViewById(R.id.Demandeur);
            TextView OT = (TextView) tableRow1.findViewById(R.id.OT);
            TextView Article = (TextView) tableRow1.findViewById(R.id.Article);
            TextView qte = (TextView) tableRow1.findViewById(R.id.qte);
            TextView total = (TextView) tableRow1.findViewById(R.id.total);
            TextView bsAtt = (TextView) tableRow1.findViewById(R.id.bsAtt);

            BS.setText(LNBS.get(j));
            Demandeur.setText(LDemandeur.get(j));
            OT.setText(LNOT.get(j));
            Article.setText(LArticle.get(j));
            qte.setText(LQtePhy.get(j));
            total.setText(LTotal.get(j));
            bsAtt.setText(LNumAtt.get(j));
            tableLayout.addView(tableRow1);

            for (int i = 1; i < tableLayout.getChildCount(); i++) {
                if (i % 2 == 0) {
                    tableLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#CCD0D0"));
                } else {
                    tableLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#F7F8F8"));

                }
            }
        }
        View tableRow2 = LayoutInflater.from(this).inflate(R.layout.total_bs_item, null, false);
        TextView prixTotal = (TextView) tableRow2.findViewById(R.id.prixTotal);


        prixTotal.setText(String.valueOf(TTotal));


        tableLayout.addView(tableRow2);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_home){
            Intent intent = new Intent(ListeBonSortie.this,StatisticActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if(id == R.id.menu_DMInt){
            Intent intent = new Intent(ListeBonSortie.this,DIActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if(id == R.id.menu_ListeDM){
            Intent intent = new Intent(ListeBonSortie.this,AccueilActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_listeEchDate) {
            Intent intent = new Intent(ListeBonSortie.this,EchanceParDate.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBS)
        {
            Toast.makeText(this,"Vous êtes déja en page liste bon sortie", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.menu_ListeBI) {
            Intent intent = new Intent(ListeBonSortie.this,ListeBonInventaireActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ReleveUsages) {
            Intent intent = new Intent(ListeBonSortie.this, ReleveUsages.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_listeEchUsage) {
            Intent intent = new Intent(this, EcheanceParUsage.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_BonSortie) {
            Intent intent = new Intent(ListeBonSortie.this, BonSortie.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_BonInventaire) {
            Intent intent = new Intent(ListeBonSortie.this, BonInventaireActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBI) {
            Intent intent = new Intent(ListeBonSortie.this, ListeBonInventaireActivity.class);
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