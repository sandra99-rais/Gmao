package com.example.gmaoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReleveUsages extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    TextView title2;
    EditText etDatedb, etDatefin;
    private TableLayout tableLayout;
    int var;
    String role;
    Button filtrer,affTous;
    DatabaseHelper db=new DatabaseHelper(this);
    int idEmploye;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    /*Web service*/
    final String NAMESPACE = ContactResult.NAMESPACE;
    final String URL = ContactResult.URL;
    /*Login*/
    final  String METHOD_NAME = "ListeUsage";
    final String SOAP_ACTION = "http://tempuri.org/ListeUsage";
    View tableRow1;
    ArrayList<String> LNum=new ArrayList<>();
    ArrayList<String> LEquipement=new ArrayList<>();
    ArrayList<String> LCompteur=new ArrayList<>();
    ArrayList<String> LUsage=new ArrayList<>();
    ArrayList<String> LDateUsage=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_releve_usages);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        TextView textView = (TextView) findViewById(R.id.titreEch);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        toolbar.setTitle("");

        //************************Tolbar********************************//
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_drawer_open,R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.menu_ReleveUsages);
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
        title2=(TextView)findViewById(R.id.titre1);
        etDatedb= (EditText)findViewById(R.id.etDate1);
        etDatefin= (EditText)findViewById(R.id.etDate2);
        filtrer=(Button)findViewById(R.id.btnFiltrer);
        affTous=(Button)findViewById(R.id.btnAffTous);

        title2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReleveUsages.this,AjoutReleve.class);
                intent.putExtra("nomDM",var);
                intent.putExtra("idEmploye",idEmploye);
                intent.putExtra("role", role);


                startActivity(intent);
            }
        });

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        etDatedb.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(c.getTime()));
        etDatefin.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(c1.getTime()));

        etDatedb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(etDatedb);
            }
        });
        etDatefin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(etDatefin);
            }
        });
        tableLayout = (TableLayout)findViewById(R.id.tableLayout);
//////////////////////////////////////////////////////////////////////////////////////////////
        if(checkNetworkConnection())
        {
            new ReleveUsages.MyAsyncTask().execute();
        }
        else {
            afficheTous();
        }

            filtrer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tableLayout.getChildCount()>1)
                    {
                        tableLayout.removeViews(1,tableLayout.getChildCount()-1);
                        affiche();
                    }
                    else
                    {
                        affiche();
                    }


                }
            });
        affTous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tableLayout.getChildCount()>1) {
                    tableLayout.removeViews(1,tableLayout.getChildCount()-1);
                    afficheTous();
                }
                else
                {
                    afficheTous();
                }
            }
        });

        }




   public void afficheTous()
   {
       Cursor Liste=db.ListeUsage();
       while(Liste.moveToNext())
       {
           View tableRow = LayoutInflater.from(this).inflate(R.layout.layout_table_usage, null, false);
           TextView eqp = (TextView) tableRow.findViewById(R.id.eqp);
           TextView comp = (TextView) tableRow.findViewById(R.id.comp);
           TextView usageRel = (TextView) tableRow.findViewById(R.id.usR);
           TextView dateUsage = (TextView) tableRow.findViewById(R.id.dateU);
           TextView idU = (TextView) tableRow.findViewById(R.id.idU);
           TextView modif = (TextView) tableRow.findViewById(R.id.modif);


           eqp.setText(Liste.getString(0));
           comp.setText(Liste.getString(1));
           usageRel.setText(Liste.getString(2));
           dateUsage.setText(Liste.getString(3));
           idU.setText(Liste.getString(4));



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
           modif.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(ReleveUsages.this,ModifierUsages.class);
                   intent.putExtra("idUsage",Integer.parseInt(idU.getText().toString()));
                   intent.putExtra("nomDM",var);
                   intent.putExtra("idEmploye",idEmploye);
                   intent.putExtra("role", role);

                   startActivity(intent);
               }
           });
       }

   }
    public void affiche()
    {
        Cursor c=db.filtrage(etDatedb.getText().toString(),etDatefin.getText().toString());
        while(c.moveToNext())
        {
            View tableRow = LayoutInflater.from(this).inflate(R.layout.layout_table_usage, null, false);
            TextView eqp = (TextView) tableRow.findViewById(R.id.eqp);
            TextView comp = (TextView) tableRow.findViewById(R.id.comp);
            TextView usageRel = (TextView) tableRow.findViewById(R.id.usR);
            TextView dateUsage = (TextView) tableRow.findViewById(R.id.dateU);
            TextView idU = (TextView) tableRow.findViewById(R.id.idU);
            TextView modif = (TextView) tableRow.findViewById(R.id.modif);


            eqp.setText(c.getString(0));
            comp.setText(c.getString(1));
            usageRel.setText(c.getString(2));
            dateUsage.setText(c.getString(3));
            idU.setText(c.getString(4));



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
            modif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ReleveUsages.this,ModifierUsages.class);
                    intent.putExtra("idUsage",Integer.parseInt(idU.getText().toString()));
                    intent.putExtra("nomDM",var);
                    intent.putExtra("idEmploye",idEmploye);
                    intent.putExtra("role", role);

                    startActivity(intent);
                }
            });
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
        String Num;
        String Equipement;
        String Compteur;
        String Usage;
        String Date;

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
                        Num = category_list.getProperty("num").toString();
                        Equipement = category_list.getProperty("equipement").toString();
                        Compteur = category_list.getProperty("compteur").toString();
                        Usage = category_list.getProperty("releve").toString();
                        Date = category_list.getProperty("date").toString();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

                        Date dateU= null;
                        try {
                            dateU = formatter.parse(Date);

                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }
                        formatter.applyPattern("dd/MM/yyyy HH:mm");
                        LNum.add(Num);
                        LEquipement.add(Equipement);
                        LCompteur.add(Compteur);
                        LUsage.add(Usage);
                        LDateUsage.add(formatter.format(dateU));
                    }


                }

            } catch (Exception e) {
                e.printStackTrace();
                return  e.toString();
            }
            return Num;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null)
            {


                afficheCON();


            }
            else
            {
                Toast.makeText(ReleveUsages.this, "No Date To Show", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }
    public void afficheCON()
    {
        for(int  j=0;j<LNum.size();j++)
        {
            tableRow1= LayoutInflater.from(this).inflate(R.layout.layout_table_usage, null, false);

            TextView eqp = (TextView) tableRow1.findViewById(R.id.eqp);
            TextView comp = (TextView) tableRow1.findViewById(R.id.comp);
            TextView usageRel = (TextView) tableRow1.findViewById(R.id.usR);
            TextView dateUsage = (TextView) tableRow1.findViewById(R.id.dateU);
            TextView idU = (TextView) tableRow1.findViewById(R.id.idU);
            TextView modif = (TextView) tableRow1.findViewById(R.id.modif);

            eqp.setText(LEquipement.get(j));
            comp.setText(LCompteur.get(j));
            usageRel.setText(LUsage.get(j));
            dateUsage.setText(LDateUsage.get(j));
            idU.setText(LNum.get(j));

            tableLayout.addView(tableRow1);

            for (int i = 1; i < tableLayout.getChildCount(); i++) {
                if (i % 2 == 0) {
                    tableLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#CCD0D0"));
                } else {
                    tableLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#F7F8F8"));

                }
            }
            modif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ReleveUsages.this,ModifierUsages.class);
                    intent.putExtra("idUsage",Integer.parseInt(idU.getText().toString()));
                    intent.putExtra("nomDM",var);
                    intent.putExtra("idEmploye",idEmploye);
                    intent.putExtra("role", role);

                    startActivity(intent);
                }
            });
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
                new TimePickerDialog(ReleveUsages.this, timeSetListener, cldr.get(Calendar.HOUR_OF_DAY), cldr.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(ReleveUsages.this, dateSetListener, cldr.get(Calendar.YEAR), cldr.get(Calendar.MONTH), cldr.get(Calendar.DAY_OF_MONTH)).show();
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

        if(id == R.id.menu_home){
            Intent intent = new Intent(ReleveUsages.this,StatisticActivity.class);
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
        if(id == R.id.menu_DMInt){
            Intent intent = new Intent(ReleveUsages.this,DIActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if(id == R.id.menu_ListeDM){
            Intent intent = new Intent(ReleveUsages.this,AccueilActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_ReleveUsages) {
            Toast.makeText(this,"Vous êtes déja en page de releve", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.menu_listeEchDate) {
            Intent intent = new Intent(ReleveUsages.this, EchanceParDate.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_BonSortie) {
            Intent intent = new Intent(ReleveUsages.this, BonSortie.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBS) {
            Intent intent = new Intent(ReleveUsages.this, ListeBonSortie.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_BonInventaire) {
            Intent intent = new Intent(ReleveUsages.this, BonInventaireActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBI) {
            Intent intent = new Intent(ReleveUsages.this, ListeBonInventaireActivity.class);
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