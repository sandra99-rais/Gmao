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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AccueilActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TableLayout tableLayout;
    DatabaseHelper db=new DatabaseHelper(this);
    EditText search;
    /*intent*/
    int var ;
    int idEmploye;
    String role;
    /*Menu*/
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    /*Web service*/
    final String NAMESPACE = ContactResult.NAMESPACE;
    final String URL = ContactResult.URL;
    /*Liste*/
    final  String METHOD_NAME ="ListeDM";
    final String SOAP_ACTION = "http://tempuri.org/ListeDM ";
   /*En mode Connecté*/
    Date dateU= null;
    String NBT,NBTAttiribue,Name;
    String Id_Machine;
    String Id_Type_Trv;
    String desc;
    String Date_Demande;
    String demande,cloture;
    Boolean approuve;
    View tableRow1;
    ArrayList<String> LNBT=new ArrayList<>();
    ArrayList<String> LMachine=new ArrayList<>();
    ArrayList<String> LTypeTrav=new ArrayList<>();
    ArrayList<String> LDesc=new ArrayList<>();
    ArrayList<String> LDate=new ArrayList<>();
    ArrayList<String> LLance=new ArrayList<>();
    ArrayList<String> LCloture=new ArrayList<>();
    ArrayList<Boolean> LApprouve=new ArrayList<>();
    ArrayList<String> LNBTAttiribue=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        //*****************************************Menu**********************************/
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
        navigationView.setCheckedItem(R.id.menu_ListeDM);

        TextView textView = (TextView) findViewById(R.id.titreListe);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //*****************************************Fin Menu**********************************/
        search = findViewById(R.id.search);
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
        tableLayout = (TableLayout)findViewById(R.id.tableLayout);
        if (checkNetworkConnection()) {
            (new AccueilActivity.MyAsyncTask()).execute();
        } else {
            if(search.getText().toString().equals("")) {
                if (tableLayout.getChildCount() > 1) {
                    tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
                    Liste();
                } else {
                    Liste();
                }
            }
            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(search.getText().toString().equals("")) {
                        if (tableLayout.getChildCount() > 1) {
                            tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
                            Liste();
                        } else {
                            Liste();
                        }
                    }
                    else {
                        if (tableLayout.getChildCount() > 1) {
                            tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
                            affiche();
                        } else {
                            ListeId();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        }
    }
    public void Liste(){
        Cursor Liste = db.ListeDemande();
        while (Liste.moveToNext()) {
            View tableRow = LayoutInflater.from(AccueilActivity.this).inflate(R.layout.layout_item, null, false);

            TextView dateD = (TextView) tableRow.findViewById(R.id.textView1);
            TextView numOT = (TextView) tableRow.findViewById(R.id.textView2);
            TextView typeTravail = (TextView) tableRow.findViewById(R.id.textView3);
            TextView equipement = (TextView) tableRow.findViewById(R.id.textView4);
            TextView detail = (TextView) tableRow.findViewById(R.id.textView5);
            TextView desc = (TextView) tableRow.findViewById(R.id.textView7);
            TextView modif = (TextView) tableRow.findViewById(R.id.textView6);
            TextView nbtAtt = (TextView) tableRow.findViewById(R.id.nbtAtt);


            dateD.setText(Liste.getString(0));
            numOT.setText(Liste.getString(1));
            typeTravail.setText(Liste.getString(2));
            equipement.setText(Liste.getString(3));
            desc.setText(Liste.getString(4));
            nbtAtt.setText(Liste.getString(5));

            if(nbtAtt.getText().toString().equals("anyType{}"))
            {
                nbtAtt.setText(numOT.getText().toString()+" "+Liste.getString(6));
            }



            tableLayout.addView(tableRow);

            Cursor c = db.etatOT(numOT.getText().toString());
            while (c.moveToNext()) {
                if (c.getInt(0) == 1 && c.getInt(1) == 0) {
                    dateD.setBackgroundColor(Color.parseColor("#04448c"));
                    dateD.setTextColor(Color.WHITE);
                }
                if (c.getInt(0) == 1 && c.getInt(1) == 1) {
                    dateD.setBackgroundColor(Color.parseColor("#f4941c"));
                    dateD.setTextColor(Color.WHITE);
                }
                if (c.getInt(0) == 1 && c.getInt(1) == 1 && c.getInt(2) == 1) {
                    dateD.setBackgroundColor(Color.parseColor("#f4941c"));
                    dateD.setTextColor(Color.WHITE);
                }
            }

            Cursor cNL = db.etatLOT(numOT.getText().toString());
            while (cNL.moveToNext()) {
                dateD.setBackgroundColor(Color.parseColor("#a8b1af"));
                dateD.setTextColor(Color.WHITE);
            }

            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AccueilActivity.this, DetailDIActivity.class);
                    intent.putExtra("idDI", Integer.parseInt(numOT.getText().toString()));
                    intent.putExtra("nomDM", var);
                    intent.putExtra("role", role);
                    intent.putExtra("idEmploye", idEmploye);
                    startActivity(intent);
                }

            });
            modif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AccueilActivity.this, ModifierActivity.class);
                    intent.putExtra("idDI", Integer.parseInt(numOT.getText().toString()));
                    intent.putExtra("nomDM", var);
                    intent.putExtra("role", role);
                    intent.putExtra("idEmploye", idEmploye);
                    startActivity(intent);
                }
            });

            for (int i = 1; i < tableLayout.getChildCount(); i++) {
                if (i % 2 == 0) {
                    tableLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#CCD0D0"));
                } else {
                    tableLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#F7F8F8"));

                }
            }
        }
    }
    public void ListeId(){
        Cursor Liste = db.ListeDemandeParId(search.getText().toString());
        while (Liste.moveToNext()) {
            View tableRow = LayoutInflater.from(AccueilActivity.this).inflate(R.layout.layout_item, null, false);

            TextView dateD = (TextView) tableRow.findViewById(R.id.textView1);
            TextView numOT = (TextView) tableRow.findViewById(R.id.textView2);
            TextView typeTravail = (TextView) tableRow.findViewById(R.id.textView3);
            TextView equipement = (TextView) tableRow.findViewById(R.id.textView4);
            TextView detail = (TextView) tableRow.findViewById(R.id.textView5);
            TextView desc = (TextView) tableRow.findViewById(R.id.textView7);
            TextView modif = (TextView) tableRow.findViewById(R.id.textView6);
            TextView nbtAtt = (TextView) tableRow.findViewById(R.id.nbtAtt);


            dateD.setText(Liste.getString(0));
            numOT.setText(Liste.getString(1));
            typeTravail.setText(Liste.getString(2));
            equipement.setText(Liste.getString(3));
            desc.setText(Liste.getString(4));

            if(Liste.getString(5).equals("anyType{}"))
            {
                nbtAtt.setText(numOT.getText().toString()+" "+Liste.getString(6));
            }
            else
            {
                nbtAtt.setText(Liste.getString(5));


            }
            tableLayout.addView(tableRow);

            Cursor c = db.etatOT(numOT.getText().toString());
            while (c.moveToNext()) {
                if (c.getInt(0) == 1 && c.getInt(1) == 0) {
                    dateD.setBackgroundColor(Color.parseColor("#04448c"));
                    dateD.setTextColor(Color.WHITE);
                }
                if (c.getInt(0) == 1 && c.getInt(1) == 1) {
                    dateD.setBackgroundColor(Color.parseColor("#f4941c"));
                    dateD.setTextColor(Color.WHITE);
                }
                if (c.getInt(0) == 1 && c.getInt(1) == 1 && c.getInt(2) == 1) {
                    dateD.setBackgroundColor(Color.parseColor("#f4941c"));
                    dateD.setTextColor(Color.WHITE);
                }
            }

            Cursor cNL = db.etatLOT(numOT.getText().toString());
            while (cNL.moveToNext()) {
                dateD.setBackgroundColor(Color.parseColor("#a8b1af"));
                dateD.setTextColor(Color.WHITE);
            }

            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AccueilActivity.this, DetailDIActivity.class);
                    intent.putExtra("idDI", Integer.parseInt(numOT.getText().toString()));
                    intent.putExtra("nomDM", var);
                    intent.putExtra("role", role);
                    intent.putExtra("idEmploye", idEmploye);
                    startActivity(intent);
                }

            });
            modif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AccueilActivity.this, ModifierActivity.class);
                    intent.putExtra("idDI", Integer.parseInt(numOT.getText().toString()));
                    intent.putExtra("nomDM", var);
                    intent.putExtra("role", role);
                    intent.putExtra("idEmploye", idEmploye);
                    startActivity(intent);
                }
            });

            for (int i = 1; i < tableLayout.getChildCount(); i++) {
                if (i % 2 == 0) {
                    tableLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#CCD0D0"));
                } else {
                    tableLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#F7F8F8"));

                }
            }
        }
    }
    //*****************************************Button deconnecter*************************************************/
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
    //************************************Open and CloseMenu******************************************************/
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
    //***********************************Check Connection*******************************************************/
    public boolean checkNetworkConnection()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }
    //********************************Liste OT en mode connecter**********************************************************/
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

                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject res = (SoapObject) result.getProperty(0);

                for (int i = 0; i < res.getPropertyCount(); i++) {
                    Object property = res.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject category_list = (SoapObject) property;
                        NBT = category_list.getProperty("NBT").toString();
                        Id_Machine = category_list.getProperty("Id_Machine").toString();
                        Id_Type_Trv = category_list.getProperty("Id_Type_Trv").toString();
                        desc = category_list.getProperty("desc").toString();
                        Date_Demande = category_list.getProperty("Date_Demande").toString();
                        demande = category_list.getProperty("demande").toString();
                        cloture = category_list.getProperty("cloture").toString();
                        NBTAttiribue = category_list.getProperty("NBTAttiribue").toString();
                        Name = category_list.getProperty("Name").toString();
                        approuve = Boolean.valueOf(category_list.getProperty("approuve").toString());
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");


                        try
                        {
                            dateU = formatter.parse(Date_Demande);
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }
                        formatter.applyPattern("dd/MM/yyyy HH:mm");
                        LNBT.add(NBT);
                        LMachine.add(Id_Machine);
                        LTypeTrav.add(Id_Type_Trv);
                        LDesc.add(desc);
                        LDate.add(formatter.format(dateU));
                        LLance.add(demande);
                        LCloture.add(cloture);
                        LApprouve.add(approuve);
                        if(NBTAttiribue.equals("anyType{}"))
                        {
                            LNBTAttiribue.add(NBT+" "+Name);
                        }
                        else
                        {
                            LNBTAttiribue.add(NBTAttiribue);

                        }

                    }


                }

            } catch (Exception e) {
                e.printStackTrace();
                return  e.toString();
            }
            return NBT;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null) {
                if (search.getText().toString().equals("")) {
                    if (tableLayout.getChildCount() > 1) {
                        tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
                        affiche();
                    } else {
                        affiche();
                    }
                }
                search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (search.getText().toString().equals("")) {
                            if (tableLayout.getChildCount() > 1) {
                                tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
                                affiche();
                            } else {
                                affiche();
                            }
                        } else {
                            if (tableLayout.getChildCount() > 1) {
                                tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
                                afficheId(search.getText().toString());
                            } else {
                                afficheId(search.getText().toString());
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }
            else
            {
                Toast.makeText(AccueilActivity.this, "No Date To Show", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }
    public void affiche()
    {
        for(int  j=0;j<LNBT.size();j++)
        {
            tableRow1= LayoutInflater.from(this).inflate(R.layout.layout_item, null, false);

            TextView dateD = (TextView) tableRow1.findViewById(R.id.textView1);
            TextView numOT = (TextView) tableRow1.findViewById(R.id.textView2);
            TextView typeTravail = (TextView) tableRow1.findViewById(R.id.textView3);
            TextView equipement = (TextView) tableRow1.findViewById(R.id.textView4);
            TextView actions = (TextView) tableRow1.findViewById(R.id.textView5);
            TextView desc = (TextView) tableRow1.findViewById(R.id.textView7);
            TextView modif = (TextView) tableRow1.findViewById(R.id.textView6);
            TextView nbtAtt = (TextView) tableRow1.findViewById(R.id.nbtAtt);


            numOT.setText(LNBT.get(j));
            dateD.setText(LDate.get(j));
            typeTravail.setText(LTypeTrav.get(j));
            equipement.setText(LMachine.get(j));
            nbtAtt.setText(LNBTAttiribue.get(j));
            if(LDesc.get(j).equals("anyType{}"))
            {
                desc.setText("");
            }
            else {
                desc.setText(LDesc.get(j));
            }
            tableLayout.addView(tableRow1);
            actions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AccueilActivity.this, DetailDIActivity.class);
                    intent.putExtra("idDI", Integer.parseInt(numOT.getText().toString()));
                    intent.putExtra("nomDM", var);
                    intent.putExtra("role", role);
                    intent.putExtra("idEmploye", idEmploye);
                    startActivity(intent);
                }

            });
            modif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AccueilActivity.this, ModifierActivity.class);
                    intent.putExtra("idDI", Integer.parseInt(numOT.getText().toString()));
                    intent.putExtra("nomDM", var);
                    intent.putExtra("role", role);
                    intent.putExtra("idEmploye", idEmploye);
                    startActivity(intent);
                }
            });
            if ((LLance.get(j)).equals("oui") && (LCloture.get(j)).equals("non")) {
                dateD.setBackgroundColor(Color.parseColor("#04448c"));
                dateD.setTextColor(Color.WHITE);
            }
            if ((LLance.get(j)).equals("oui") && (LCloture.get(j)).equals("oui")) {
                dateD.setBackgroundColor(Color.parseColor("#f4941c"));
                dateD.setTextColor(Color.WHITE);
            }
            if ((LLance.get(j)).equals("oui") && (LCloture.get(j)).equals("oui") && (LApprouve.get(j))) {
                dateD.setBackgroundColor(Color.parseColor("#f4941c"));
                dateD.setTextColor(Color.WHITE);
            }
            if((LLance.get(j)).equals("non") && (LCloture.get(j)).equals("non") && !(LApprouve.get(j)))
            {
                dateD.setBackgroundColor(Color.parseColor("#a8b1af"));
                    dateD.setTextColor(Color.WHITE);
            }
            for (int i = 1; i < tableLayout.getChildCount(); i++) {
                if (i % 2 == 0) {
                    tableLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#CCD0D0"));
                } else {
                    tableLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#F7F8F8"));

                }
            }

        }
    }
    public void afficheId(String id)
    {
        for(int  j=0;j<LNBT.size();j++)
        {
            if(LNBT.get(j).equals(id)) {
                tableRow1 = LayoutInflater.from(this).inflate(R.layout.layout_item, null, false);

                TextView dateD = (TextView) tableRow1.findViewById(R.id.textView1);
                TextView numOT = (TextView) tableRow1.findViewById(R.id.textView2);
                TextView typeTravail = (TextView) tableRow1.findViewById(R.id.textView3);
                TextView equipement = (TextView) tableRow1.findViewById(R.id.textView4);
                TextView actions = (TextView) tableRow1.findViewById(R.id.textView5);
                TextView desc = (TextView) tableRow1.findViewById(R.id.textView7);
                TextView modif = (TextView) tableRow1.findViewById(R.id.textView6);
                TextView nbtAtt = (TextView) tableRow1.findViewById(R.id.nbtAtt);


                numOT.setText(LNBT.get(j));
                dateD.setText(LDate.get(j));
                typeTravail.setText(LTypeTrav.get(j));
                equipement.setText(LMachine.get(j));
                nbtAtt.setText(LNBTAttiribue.get(j));

                if (LDesc.get(j).equals("anyType{}")) {
                    desc.setText("");
                } else {
                    desc.setText(LDesc.get(j));
                }
                tableLayout.addView(tableRow1);
                actions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AccueilActivity.this, DetailDIActivity.class);
                        intent.putExtra("idDI", Integer.parseInt(numOT.getText().toString()));
                        intent.putExtra("nomDM", var);
                        intent.putExtra("role", role);
                        intent.putExtra("idEmploye", idEmploye);
                        startActivity(intent);
                    }

                });
                modif.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AccueilActivity.this, ModifierActivity.class);
                        intent.putExtra("idDI", Integer.parseInt(numOT.getText().toString()));
                        intent.putExtra("nomDM", var);
                        intent.putExtra("role", role);
                        intent.putExtra("idEmploye", idEmploye);
                        startActivity(intent);
                    }
                });
                if ((LLance.get(j)).equals("oui") && (LCloture.get(j)).equals("non")) {
                    dateD.setBackgroundColor(Color.parseColor("#04448c"));
                    dateD.setTextColor(Color.WHITE);
                }
                if ((LLance.get(j)).equals("oui") && (LCloture.get(j)).equals("oui")) {
                    dateD.setBackgroundColor(Color.parseColor("#f4941c"));
                    dateD.setTextColor(Color.WHITE);
                }
                if ((LLance.get(j)).equals("oui") && (LCloture.get(j)).equals("oui") && (LApprouve.get(j))) {
                    dateD.setBackgroundColor(Color.parseColor("#f4941c"));
                    dateD.setTextColor(Color.WHITE);
                }
                if ((LLance.get(j)).equals("non") && (LCloture.get(j)).equals("non") && !(LApprouve.get(j))) {
                    dateD.setBackgroundColor(Color.parseColor("#a8b1af"));
                    dateD.setTextColor(Color.WHITE);
                }
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_home) {
            Intent intent = new Intent(AccueilActivity.this, StatisticActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_listeEchUsage) {
            Intent intent = new Intent(AccueilActivity.this, EcheanceParUsage.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_DMInt) {
            Intent intent = new Intent(AccueilActivity.this, DIActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeDM) {
            Toast.makeText(this, "Vous êtes déja en page de liste des demandes d'intervention", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.menu_ReleveUsages) {
            Intent intent = new Intent(AccueilActivity.this, ReleveUsages.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_listeEchDate) {
            Intent intent = new Intent(AccueilActivity.this, EchanceParDate.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_BonSortie) {
            Intent intent = new Intent(AccueilActivity.this, BonSortie.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBS) {
            Intent intent = new Intent(AccueilActivity.this, ListeBonSortie.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_BonInventaire) {
            Intent intent = new Intent(AccueilActivity.this, BonInventaireActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("role", role);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBI) {
            Intent intent = new Intent(AccueilActivity.this, ListeBonInventaireActivity.class);
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