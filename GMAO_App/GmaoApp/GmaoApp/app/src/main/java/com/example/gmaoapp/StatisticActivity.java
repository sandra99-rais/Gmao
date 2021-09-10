package com.example.gmaoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import com.google.android.material.navigation.NavigationView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import java.util.ArrayList;
import java.util.List;

public class StatisticActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseHelper db=new DatabaseHelper(this);
    int var;
    int idEmploye;
    TextView tx1, tx2;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    AnyChartView anyChartView;
    ArrayList<PieEntry> status;
    ArrayList<Integer> colors;
    Cartesian cartesian;
    //Sync TT//
    ArrayList<String> LDesTT = new ArrayList<>();
    ArrayList<Integer> LIdTT = new ArrayList<>();
    int countNL,countL,countC,countAp,countAl, countMachine, countMachineArret, countPr, countPrRetard, countTT, Id_Type_Trv;
    String role, Designation;
    /*Web service*/
    final String NAMESPACE = ContactResult.NAMESPACE;
    final String URL = ContactResult.URL;
    /*Statistic*/
    final  String METHOD_NAME = "ListeStatistiques";
    final String SOAP_ACTION = "http://tempuri.org/ListeStatistiques";

    PieChart pieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        setSupportActionBar(toolbar);
        toolbar.setTitle("");


        //************************Toolbar********************************//
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_drawer_open,R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.menu_home);


        pieChart = findViewById(R.id.pieChart);
        tx1 = (TextView)findViewById(R.id.text1) ;
        tx2 = (TextView)findViewById(R.id.text2) ;

        Cursor cprev = db.nbPrev();
        Cursor cprevRetard = db.nbPrevRetard();

        Cursor cmachine = db.equip();
        Cursor cmachineArret = db.equipArret();


        Cursor cLance = db.countDMLStatut();
        Cursor cCloture = db.countDMCStatut();
        Cursor cApp = db.countDMAStatut();
        Cursor cAll = db.countDMStatut();
        Cursor cRetard = db.countDMRStatut();

        status = new ArrayList<>();
        colors = new ArrayList<Integer>();

        anyChartView =findViewById(R.id.anyChartView);
        cartesian = AnyChart.column();

        List<DataEntry> data= new ArrayList<>();

        if (checkNetworkConnection()) {
            (new StatisticActivity.MyAsyncTask()).execute();


        } else {

            while (cprev.moveToNext() && cprev.getInt(0)!=0) {
                while (cprevRetard.moveToNext()) {
                    tx1.setText(" Pourcentage Preventif En Retard : " + cprevRetard.getInt(0) * 100 /cprev.getInt(0)+ "%");
                }
            }

            while (cmachine.moveToNext() && cmachine.getInt(0)!=0){
                while (cmachineArret.moveToNext()){
                    tx2.setText("Pourcentage Equipement En Arrêt : "+cmachineArret.getInt(0)*100/cmachine.getInt(0) +"%");
                }
            }

            while (cAll.moveToNext() && cAll.getInt(0) != 0) {


                while (cLance.moveToNext() && cLance.getInt(0) != 0) {
                    status.add(new PieEntry(cLance.getInt(0) * 100 / cAll.getInt(0), "OT Lancé"));
                    colors.add(Color.parseColor("#04448c"));
                }
                cLance.close();
                while (cCloture.moveToNext() && cCloture.getInt(0) != 0) {
                    status.add(new PieEntry(cCloture.getInt(0) * 100 / cAll.getInt(0), "OT Cloturé"));
                    colors.add(Color.parseColor("#f4941c"));

                }
                cCloture.close();

                while (cApp.moveToNext() && cApp.getInt(0) != 0) {
                    status.add(new PieEntry(cApp.getInt(0) * 100 / cAll.getInt(0), "OT Approuvé"));
                    colors.add(Color.parseColor("#f4941c"));

                }
                cApp.close();

                while (cRetard.moveToNext() && cRetard.getInt(0) != 0) {
                    status.add(new PieEntry(cRetard.getInt(0) * 100 / cAll.getInt(0), "OT En Retard"));
                    colors.add(Color.parseColor("#a8b1af"));

                }
                cRetard.close();

            }
            cAll.close();
            PieDataSet pieDataSet = new PieDataSet(status, "Statut des OT");

            pieDataSet.setColors(colors);
            pieDataSet.setValueTextColor(Color.WHITE);
            pieDataSet.setValueTextSize(16f);
            PieData pieData = new PieData(pieDataSet);
            pieData.setValueFormatter(new PercentFormatter(pieChart));
            pieChart.getDescription().setEnabled(false);
            pieChart.setCenterText("Répartition des OTs par états");
            pieChart.animateY(2000, Easing.EaseInOutCubic);
            pieChart.animate();
            pieChart.setData(pieData);
            pieChart.setUsePercentValues(true);


        }
        //*******************************************************************/
        Cursor ctt = db.countDMAllTypeTravaux();

        while (ctt.moveToNext()) {
            Cursor idtt = db.countDMTypeTravaux(ctt.getInt(0));
            while(idtt.moveToNext()) {
                data.add(new ValueDataEntry(ctt.getString(1), idtt.getInt(0)));
            }
        }


        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Histogramme des OTs par type des travaux");


        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.xAxis(0).title("Type des travaux");
        cartesian.yAxis(0).title("Nombre OT");
        anyChartView.setChart(cartesian);








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



            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport=new HttpTransportSE(URL);

            try
            {
                androidHttpTransport.call(SOAP_ACTION,envelope);

                SoapObject res = (SoapObject) envelope.getResponse();

                if (res != null) {
                    countNL = Integer.parseInt(res.getProperty("countNonLa").toString());
                    countL = Integer.parseInt(res.getProperty("countLa").toString());
                    countC = Integer.parseInt(res.getProperty("countCl").toString());
                    countAp = Integer.parseInt(res.getProperty("countApp").toString());
                    countAl = Integer.parseInt(res.getProperty("countAll").toString());
                    countPr = Integer.parseInt(res.getProperty("countPrev").toString());
                    countPrRetard = Integer.parseInt(res.getProperty("countPrevRetard").toString());
                    countMachine = Integer.parseInt(res.getProperty("countEquip").toString());
                    countMachineArret = Integer.parseInt(res.getProperty("countEquipArret").toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
                return  e.toString();
            }
            return String.valueOf(countAl);
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null)
            {
                affiche();
            }
            else
            {
                Toast.makeText(StatisticActivity.this, "Pas de date a affiché", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }

    public void affiche()
    {

        if (countPr==0) {
            tx1.setText("Pourcentage Preventif En Retard : 0%");
        }
        if (countPr!=0) {
            if (countPrRetard!=0) {
                tx1.setText("Pourcentage Preventif En Retard : " + countPrRetard * 100 /countPr+ "%");
            }
        }

        if (countMachine!=0){
            if (countMachineArret!=0){
                tx2.setText("Pourcentage Equipement En Arrêt : "+countMachineArret*100 /countMachine +"%");
            }
        }

        if (countAl!=0) {

            if (countL != 0) {
                status.add(new PieEntry(countL * 100 / countAl, "OT Lancé"));
                colors.add(Color.parseColor("#04448c"));

            }

            if (countC != 0) {
                status.add(new PieEntry(countC * 100 / countAl, "OT Cloturé"));
                colors.add(Color.parseColor("#f4941c"));

            }
            if (countAp != 0) {
                status.add(new PieEntry(countAp * 100 / countAl, "OT Approuvé"));
                colors.add(Color.parseColor("#f4941c"));

            }
            if (countNL != 0) {
                status.add(new PieEntry(countNL * 100 / countAl, "OT En Retard"));
                colors.add(Color.parseColor("#a8b1af"));

            }
        }
        PieDataSet pieDataSet = new PieDataSet(status, "Statut des OT");

        pieDataSet.setColors(colors);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(16f);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Répartition des OTs par états");
        pieChart.animateY(2000, Easing.EaseInOutCubic);
        pieChart.animate();
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
    }

    public void afficheTT()
    {
        List<DataEntry> data= new ArrayList<>();

       // if (countTT!=0) {
        if (LIdTT.size()!=0) {
            for (int i = 0; i < LIdTT.size(); i++) {
                data.add(new ValueDataEntry(LDesTT.get(i), countTT));

            }
        }
            Column column = cartesian.column(data);

            column.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }");

            cartesian.animation(true);
            cartesian.title("Histogramme des OTs par type des travaux");


            cartesian.yScale().minimum(0d);

            cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
            cartesian.interactivity().hoverMode(HoverMode.BY_X);
            cartesian.xAxis(0).title("Type des travaux");
            cartesian.yAxis(0).title("Nombre OT");
            anyChartView.setChart(cartesian);





    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_DMInt){
            Intent intent = new Intent(StatisticActivity.this,DIActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if(id == R.id.menu_ListeDM){
            Intent intent = new Intent(StatisticActivity.this,AccueilActivity.class);
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
        if (id == R.id.menu_ReleveUsages) {
            Intent intent = new Intent(StatisticActivity.this, ReleveUsages.class);
            intent.putExtra("role", role);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_listeEchDate) {
            Intent intent = new Intent(StatisticActivity.this, EchanceParDate.class);
            intent.putExtra("role", role);
            intent.putExtra("nomDM",var);
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
        if (id == R.id.menu_BonSortie) {
            Intent intent = new Intent(StatisticActivity.this, BonSortie.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBS) {
            Intent intent = new Intent(StatisticActivity.this, ListeBonSortie.class);
            intent.putExtra("role", role);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_BonInventaire) {
            Intent intent = new Intent(StatisticActivity.this, BonInventaireActivity.class);
            intent.putExtra("role", role);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBI) {
            Intent intent = new Intent(this, ListeBonInventaireActivity.class);
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