package com.example.gmaoapp;

import androidx.annotation.NonNull;
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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.gmaoapp.Models.OrdreTravail;
import com.example.gmaoapp.Models.Equipements;
import com.example.gmaoapp.Models.OperationOt;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EchanceParDate extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener , NavigationView.OnNavigationItemSelectedListener {
    EditText etDatedb, etDatefin;
    private TableLayout tableLayout;
    DatabaseHelper db = new DatabaseHelper(this);
    Button bt, btnFiltrer, btnAffTousEch;
    ArrayList<Integer> array;
    CheckBox chkAll;
    int var;
    Spinner spinnerDesignation, etSection;
    ArrayList<String> listeEquipements, listeSection;
    String   typeOp;
    int idGamme, idOp,idSec,idExec,idTT,idMachine;
    String idOt,role;
    int idEmploye;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echance_par_date);
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
        navigationView.setCheckedItem(R.id.menu_listeEchDate);
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


        etDatedb = (EditText) findViewById(R.id.etDate1);
        etDatefin = (EditText) findViewById(R.id.etDate2);
        spinnerDesignation = (Spinner) findViewById(R.id.editTextTextPersonName);
        etSection = (Spinner) findViewById(R.id.etSection);

        bt = (Button) findViewById(R.id.generer);
        btnFiltrer = (Button) findViewById(R.id.btnFiltrer);
        btnAffTousEch = (Button) findViewById(R.id.AffTousEch);
        TextView textView = (TextView) findViewById(R.id.titreEch);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        //**************************************************************************
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        etDatedb.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(c.getTime()));
        etDatefin.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(c1.getTime()));
        //*************************Liste Machine *************************************

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

        //*************************Liste Equipements *************************************
        Cursor ListeEq = db.ListeMachine();
        listeEquipements = new ArrayList<>();
        listeEquipements.add("");

        while (ListeEq.moveToNext()) {
            String var = (ListeEq.getString(1) + " | " + ListeEq.getString(2));
            listeEquipements.add(var);

        }
        ArrayAdapter<String> equipements = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listeEquipements);
        equipements.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDesignation.setAdapter(equipements);
        spinnerDesignation.setOnItemSelectedListener(this);
        //***********************************Liste Section***************************************
        Cursor ListeSec = db.ListeSection();
        listeSection = new ArrayList<>();
        listeSection.add("");

        while (ListeSec.moveToNext()) {
            String var = (ListeSec.getString(1) + " | " + ListeSec.getString(2));

            listeSection.add(var);

        }
        ArrayAdapter<String> sections = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listeSection);
        sections.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etSection.setAdapter(sections);
        etSection.setOnItemSelectedListener(this);
        //******************************************************************************

        chkAll = (CheckBox) findViewById(R.id.titre8);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        array = new ArrayList<>();



        afficheTous();
        btnAffTousEch.setOnClickListener(new View.OnClickListener() {
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



        btnFiltrer.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                if (tableLayout.getChildCount() > 1) {
                    tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
                    affiche();
                } else {
                    affiche();
                }
                if (array.size() > 0) {
                    array.clear();
                }
                if (chkAll.isChecked()) {
                    chkAll.setChecked(false);
                }


            }
        });

    }



    public void cursor(Cursor c)
    {
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();

        while (c.moveToNext()) {
            View tableRow = LayoutInflater.from(this).inflate(R.layout.layout_table_ech_date, null, false);
            TextView date = (TextView) tableRow.findViewById(R.id.dateEch);
            TextView txtEqp = (TextView) tableRow.findViewById(R.id.txtEqp);
            TextView operation = (TextView) tableRow.findViewById(R.id.operation);
            TextView txtEx = (TextView) tableRow.findViewById(R.id.txtEx);
            TextView txtGamme = (TextView) tableRow.findViewById(R.id.txtGamme);
            TextView duree = (TextView) tableRow.findViewById(R.id.duree);
            TextView idEch = (TextView) tableRow.findViewById(R.id.idEch);

            TextView btGenerer = (TextView) tableRow.findViewById(R.id.btGenerer);

            CheckBox chk = (CheckBox) tableRow.findViewById(R.id.chk);

            idEch.setText(c.getString(0));
            date.setText(c.getString(1));
            txtEqp.setText(c.getString(2));
            operation.setText(c.getString(3));
            txtEx.setText(c.getString(4));
            txtGamme.setText(c.getString(5));
            duree.setText(c.getString(6));
            btGenerer.setText(c.getString(7));


           if(!btGenerer.getText().toString().equals(""))
           {
               chk.setEnabled(false);
           }


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
            if(!checkNetworkConnection()) {
                btGenerer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!btGenerer.getText().toString().equals("")) {
                            Intent intent = new Intent(EchanceParDate.this, ModifierActivity.class);
                            intent.putExtra("idDI", Integer.parseInt(btGenerer.getText().toString()));
                            intent.putExtra("nomDM", var);
                            intent.putExtra("idEmploye", idEmploye);
                            intent.putExtra("role", role);

                            startActivity(intent);
                        } else {
                            Toast.makeText(EchanceParDate.this, "il faut generé un Ot", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }

            checkBoxes.add(chk);
            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    final boolean[] update = {false};

                    if ((isChecked)) {
                        array.add(Integer.parseInt(idEch.getText().toString()));

                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                for (int i = 0; i < array.size(); i++) {
                                    Cursor c = db.idOtById(array.get(i));
                                    Cursor c2 = db.AllIdEchDate(array.get(i));
                                    while (c2.moveToNext()) {
                                        idTT = c2.getInt(0);
                                        idGamme = c2.getInt(2);
                                        idOp = c2.getInt(3);
                                        idMachine = c2.getInt(1);
                                        typeOp = c2.getString(4);
                                        idSec=c2.getInt(5);
                                    }
                                    Cursor E = db.idUser2(txtEx.getText().toString());
                                    while(E.moveToNext()){
                                        idExec = E.getInt(0);
                                    }
                                    while (c.moveToNext()) {
                                        idOt = c.getString(0);

                                    }
                                    if (idOt == null) {
                                        Equipements e = new Equipements(idMachine,idSec);
                                        OperationOt op = new OperationOt(idOp, date.getText().toString(), idGamme, idMachine,
                                                operation.getText().toString(), txtEqp.getText().toString(), txtGamme.getText().toString(), typeOp);
                                        OrdreTravail di = new OrdreTravail(date.getText().toString(),idExec , 1, idTT);
                                        update[0] = db.updateEch(array.get(i), di, String.valueOf(var), e, op);
                                    }

                                }

                                if (update[0]) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EchanceParDate.this);
                                    builder.setTitle("OT generé avec succes");
                                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                            startActivity(getIntent());
                                        }
                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }
                                if (idOt != null) {

                                    Toast.makeText(EchanceParDate.this, "Ot deja generé", Toast.LENGTH_SHORT).show();


                                }
                                if (update[0] = false) {
                                    Toast.makeText(EchanceParDate.this, "not updated !", Toast.LENGTH_SHORT).show();
                                }

                            }

                        });

                    } else {
                        array.remove(idEch.getText().toString());

                    }

                }

            });
            chkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    for (int i = 0; i < checkBoxes.size(); i++) {
                        if (chkAll.isChecked()) {
                            CheckBox c = checkBoxes.get(i);
                            c.setChecked(true);

                        } else {
                            CheckBox c = checkBoxes.get(i);
                            c.setChecked(false);

                        }


                    }
                }
            });
        }


    }
    public void afficheTous()
    {


        Cursor Liste=db.ListeEcheanceDate();

        cursor(Liste);



    }
    public void affiche()
    {        int idEq=0,idSec=0;

        Cursor c ;
        if(!spinnerDesignation.getSelectedItem().toString().equals("")) {
            Cursor equipement=db.idDesEquip(spinnerDesignation.getSelectedItem().toString());
            while(equipement.moveToNext())
            {
                idEq=equipement.getInt(0);
            }
            c = db.filtrageEchDate(etDatedb.getText().toString(), etDatefin.getText().toString(), idEq);
            if(!etSection.getSelectedItem().toString().equals(""))
            {
                Cursor section=db.idDesSection(etSection.getSelectedItem().toString()) ;
                while(section.moveToNext())
                {
                    idSec=section.getInt(0);
                }

                c = db.filtrageAllEchDate(etDatedb.getText().toString(), etDatefin.getText().toString(), idEq,idSec);

            }
        }
        else if(!etSection.getSelectedItem().toString().equals("") && spinnerDesignation.getSelectedItem().toString().equals(""))
        {
            Cursor section=db.idDesSection(etSection.getSelectedItem().toString()) ;
            while(section.moveToNext())
            {
                idSec=section.getInt(0);
            }
            c = db.filtrageEchDateWithSection(etDatedb.getText().toString(), etDatefin.getText().toString(),idSec);

        }


        else {
            c = db.filtrageEchDateWithoutEquip(etDatedb.getText().toString(), etDatefin.getText().toString());
        }
        cursor(c);

    }
    public boolean checkNetworkConnection()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
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
                new TimePickerDialog(EchanceParDate.this, timeSetListener, cldr.get(Calendar.HOUR_OF_DAY), cldr.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(EchanceParDate.this, dateSetListener, cldr.get(Calendar.YEAR), cldr.get(Calendar.MONTH), cldr.get(Calendar.DAY_OF_MONTH)).show();
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
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_home){
            Intent intent = new Intent(EchanceParDate.this,StatisticActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);

            startActivity(intent);
        }
        if(id == R.id.menu_DMInt){
            Intent intent = new Intent(EchanceParDate.this,DIActivity.class);
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
        if(id == R.id.menu_ListeDM){
            Intent intent = new Intent(EchanceParDate.this,AccueilActivity.class);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_listeEchDate) {
            Toast.makeText(this,"Vous êtes déja en page d'echeance par date", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.menu_ReleveUsages) {
            Intent intent = new Intent(EchanceParDate.this, ReleveUsages.class);
            intent.putExtra("role", role);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_BonSortie) {
            Intent intent = new Intent(EchanceParDate.this, BonSortie.class);
            intent.putExtra("role", role);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_BonInventaire) {
            Intent intent = new Intent(EchanceParDate.this, BonInventaireActivity.class);
            intent.putExtra("role", role);
            intent.putExtra("nomDM",var);
            intent.putExtra("idEmploye",idEmploye);
            startActivity(intent);
        }
        if (id == R.id.menu_ListeBI) {
            Intent intent = new Intent(EchanceParDate.this, ListeBonInventaireActivity.class);
            intent.putExtra("role", role);
            intent.putExtra("nomDM",var);
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
        if (id == R.id.menu_ListeBS) {
            Intent intent = new Intent(this, ListeBonSortie.class);
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