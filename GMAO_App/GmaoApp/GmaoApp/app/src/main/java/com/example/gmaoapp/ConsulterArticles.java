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

public class ConsulterArticles extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    TableLayout tableLayout;
    DatabaseHelper db = new DatabaseHelper(this);
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    String role;
    int idEmploye,var;
    /*Web service*/
    final String NAMESPACE = ContactResult.NAMESPACE;
    final String URL = ContactResult.URL;
    /*Product*/
    final String METHOD_NAME_Pdt = "ListeProducts";
    final String SOAP_ACTION_Pdt = "http://tempuri.org/ListeProducts";
    //Sync Products//
    ArrayList<Integer> LId_Product = new ArrayList<>();
    ArrayList<String> LCode_Pdt = new ArrayList<>();
    ArrayList<String> LDescription = new ArrayList<>();
    ArrayList<Double> LQtyOnHand = new ArrayList<>();
    ArrayList<Double> LDprix = new ArrayList<>();
    ArrayList<Integer> LId_ActPdt = new ArrayList<>();
    ArrayList<Integer> LId_SocPdt = new ArrayList<>();
    ArrayList<Integer> LId_Cat_Art = new ArrayList<>();
    ArrayList<Integer> LId_SS_Cat = new ArrayList<>();
    ArrayList<Integer> LId_Unite = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulter_articles);
        //********************Menu******************************************************/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        toolbar.setTitle("");
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.consultation);
        //**************************Fin Menu*********************************************/


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
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        if(checkNetworkConnection())
        {
            new ProductAsyncTask().execute();
        }
        else
        {
        Cursor c = db.ListeArticle();
        while (c.moveToNext()) {
            View tableRow = LayoutInflater.from(this).inflate(R.layout.item_liste_article, null, false);
            TextView code = (TextView) tableRow.findViewById(R.id.code);
            TextView des = (TextView) tableRow.findViewById(R.id.des);
            TextView cat = (TextView) tableRow.findViewById(R.id.cat);
            TextView scat = (TextView) tableRow.findViewById(R.id.ssCat);
            TextView unite = (TextView) tableRow.findViewById(R.id.u);
            TextView prix = (TextView) tableRow.findViewById(R.id.prix);
            TextView qte = (TextView) tableRow.findViewById(R.id.qte);


            code.setText(c.getString(c.getColumnIndex("codePdt")));
            des.setText(c.getString(c.getColumnIndex("designationPdt")));
            cat.setText(c.getString(c.getColumnIndex("codeCat")));
            scat.setText(c.getString(c.getColumnIndex("codeSSCat")));
            unite.setText(c.getString(c.getColumnIndex("codeUnite")));
            prix.setText(String.valueOf(c.getDouble(c.getColumnIndex("Prix"))));
            qte.setText(String.valueOf(c.getInt(c.getColumnIndex("Qte"))));

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
    //***********************************Check Connection*******************************************************/
    public boolean checkNetworkConnection()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }
    //****************************************************************************************************************//
    private class ProductAsyncTask extends AsyncTask<String, Void, String> {
        String Code_Pdt,Description;
        int Id_Pdt,Id_Act,Id_Soc,Id_Cat_Art,Id_SS_Cat,Id_Unite;
        double QtyOnHand,Dprix;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_Pdt);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAP_ACTION_Pdt, envelope);

                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject res = (SoapObject) result.getProperty(0);

                for (int i = 0; i < res.getPropertyCount(); i++) {
                    Object property = res.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject category_list = (SoapObject) property;
                        Code_Pdt = category_list.getProperty("Code_Pdt").toString();
                        Description = category_list.getProperty("Description").toString();
                        QtyOnHand = Double.parseDouble(category_list.getProperty("QtyOnHand").toString());
                        Dprix = Double.parseDouble(category_list.getProperty("Dprix").toString());
                        Id_Pdt = Integer.parseInt(category_list.getProperty("Id_Pdt").toString());
                        Id_Act = Integer.parseInt(category_list.getProperty("Id_Act").toString());
                        Id_Soc = Integer.parseInt(category_list.getProperty("Id_Soc").toString());
                        Id_Cat_Art = Integer.parseInt(category_list.getProperty("Id_Cat_Art").toString());
                        Id_SS_Cat = Integer.parseInt(category_list.getProperty("Id_SS_Cat").toString());
                        Id_Unite = Integer.parseInt(category_list.getProperty("Id_Unite").toString());

                        LCode_Pdt.add(Code_Pdt);
                        LDescription.add(Description);
                        LQtyOnHand.add(QtyOnHand);
                        LDprix.add(Dprix);
                        LId_Product.add(Id_Pdt);
                        LId_ActPdt.add(Id_Act);
                        LId_SocPdt.add(Id_Soc);
                        LId_Cat_Art.add(Id_Cat_Art);
                        LId_SS_Cat.add(Id_SS_Cat);
                        LId_Unite.add(Id_Unite);


                    }


                }


            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return Code_Pdt;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {

                for (int i = 0; i < LCode_Pdt.size(); i++) {
                    View tableRow = LayoutInflater.from(ConsulterArticles.this).inflate(R.layout.item_liste_article, null, false);
                    TextView code = (TextView) tableRow.findViewById(R.id.code);
                    TextView des = (TextView) tableRow.findViewById(R.id.des);
                    TextView cat = (TextView) tableRow.findViewById(R.id.cat);
                    TextView scat = (TextView) tableRow.findViewById(R.id.ssCat);
                    TextView unite = (TextView) tableRow.findViewById(R.id.u);
                    TextView prix = (TextView) tableRow.findViewById(R.id.prix);
                    TextView qte = (TextView) tableRow.findViewById(R.id.qte);

                    code.setText(LCode_Pdt.get(i));
                    des.setText(LDescription.get(i));
                    cat.setText( String.valueOf(LId_Cat_Art.get(i)));
                    scat.setText(String.valueOf(LId_SS_Cat.get(i)));
                    unite.setText(String.valueOf(LId_Unite.get(i)));
                    prix.setText(String.valueOf(LDprix.get(i)));
                    qte.setText(String.valueOf(LQtyOnHand.get(i)));

                    tableLayout.addView(tableRow);

                    for (int j = 1; j < tableLayout.getChildCount(); j++) {
                        if (j % 2 == 0) {
                            tableLayout.getChildAt(j).setBackgroundColor(Color.parseColor("#CCD0D0"));
                        } else {
                            tableLayout.getChildAt(j).setBackgroundColor(Color.parseColor("#F7F8F8"));

                        }
                    }
                }
            } else {
                Toast.makeText(ConsulterArticles.this, "No Date To Show", Toast.LENGTH_SHORT).show();

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
            Intent intent = new Intent(this, StatisticActivity.class);
            intent.putExtra("nomDM", var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye", idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_DMInt) {
            Intent intent = new Intent(this, DIActivity.class);
            intent.putExtra("nomDM", var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye", idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeDM) {
            Intent intent = new Intent(this, AccueilActivity.class);
            intent.putExtra("nomDM", var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye", idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_listeEchDate) {
            Intent intent = new Intent(this, EchanceParDate.class);
            intent.putExtra("nomDM", var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye", idEmploye);
            startActivity(intent);
        }

        if (id == R.id.menu_ListeBS) {
            Intent intent = new Intent(this, ListeBonSortie.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBI) {
            Intent intent = new Intent(this, ListeBonInventaireActivity.class);
            intent.putExtra("nomDM", var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye", idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ReleveUsages) {
            Intent intent = new Intent(this, ReleveUsages.class);
            intent.putExtra("nomDM", var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye", idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_listeEchUsage) {
            Intent intent = new Intent(this, EcheanceParUsage.class);
            intent.putExtra("nomDM", var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye", idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_BonSortie) {
            Intent intent = new Intent(this, BonSortie.class);
            intent.putExtra("nomDM", var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye", idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_BonInventaire) {
            Intent intent = new Intent(this, BonInventaireActivity.class);
            intent.putExtra("nomDM", var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye", idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBI) {
            Intent intent = new Intent(this, ListeBonInventaireActivity.class);
            intent.putExtra("nomDM", var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye", idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeDemParTech) {
            Intent intent = new Intent(this, ListeDemandeParTech.class);
            intent.putExtra("nomDM", var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye", idEmploye);
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
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}