package com.example.gmaoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    EditText et_username, et_password;
    Button btn_login;
    TextView erreur;
    DatabaseHelper db;
    int numAtt=0;
    String user, pass, ReturnResult;
    /*Web service*/
    final String NAMESPACE = ContactResult.NAMESPACE;
    final String URL = ContactResult.URL;
    /*Det BI*/
    final  String METHOD_NAME_Det = "AjouterDetBI";
    final String SOAP_ACTION_Det = "http://tempuri.org/AjouterDetBI";
    /*Login*/
    final String METHOD_NAME = "Connect";
    final String SOAP_ACTION = "http://tempuri.org/Connect";
    /*Machine*/
    final String METHOD_NAME1 = "ListeMachine";
    final String SOAP_ACTION1 = "http://tempuri.org/ListeMachine";
    /*Employe*/
    final String METHOD_NAME2 = "ListeEmploye";
    final String SOAP_ACTION2 = "http://tempuri.org/ListeEmploye";
    /*Degre Urgence*/
    final String METHOD_NAME0 = "ListeDegreUrgence";
    final String SOAP_ACTION0 = "http://tempuri.org/ListeDegreUrgence";
    /*Type Travaux*/
    final String METHOD_NAME4 = "ListeTypeTravaux";
    final String SOAP_ACTION4 = "http://tempuri.org/ListeTypeTravaux";
    /*clef Travaux*/
    final String METHOD_NAME_CF = "ListeClefTravaux";
    final String SOAP_ACTION_CF = "http://tempuri.org/ListeClefTravaux";
    /*Etat*/
    final String METHOD_NAME_Etat = "ListeEtat";
    final String SOAP_ACTION_Etat = "http://tempuri.org/ListeEtat";
    /*Section*/
    final String METHOD_NAME_sec = "ListeSection";
    final String SOAP_ACTION_sec = "http://tempuri.org/ListeSection";
    /*Compteur*/
    final String METHOD_NAME_Comp = "ListeCompteur";
    final String SOAP_ACTION_Comp = "http://tempuri.org/ListeCompteur";
    /*Det BS*/
    final  String METHOD_NAME_DetBS = "AjouterDetBS";
    final String SOAP_ACTION_DetBS = "http://tempuri.org/AjouterDetBS";
    /*User*/
    final String METHOD_NAME_User = "ListeUsers";
    final String SOAP_ACTION_User = "http://tempuri.org/ListeUsers";
    /*ensemble*/
    final String METHOD_NAME_Ens = "ListeEnsemble";
    final String SOAP_ACTION_Ens = "http://tempuri.org/ListeEnsemble";
    /*Sousensemble*/
    final String METHOD_NAME_SousEns = "ListeSousEnsemble";
    final String SOAP_ACTION_SousEns = "http://tempuri.org/ListeSousEnsemble";
    /*ListeMvt*/
    final String METHOD_NAME_ListeMvt = "ListeMvt";
    final String SOAP_ACTION_ListeMvt = "http://tempuri.org/ListeMvt";
    /*ListeDetailMvt*/
    final String METHOD_NAME_ListeDetailMvt = "ListeDetailMvt";
    final String SOAP_ACTION_ListeDetailMvt = "http://tempuri.org/ListeDetailMvt";
    /*Product*/
    final String METHOD_NAME_Pdt = "ListeProducts";
    final String SOAP_ACTION_Pdt = "http://tempuri.org/ListeProducts";
    /*ListeReleve*/
    final String METHOD_NAME_ListRel = "ListeUsageAll";
    final String SOAP_ACTION_ListRel = "http://tempuri.org/ListeUsageAll";
    /*BS*/
    final  String METHOD_NAME_BS = "AjouterBS";
    final String SOAP_ACTION_BS = "http://tempuri.org/AjouterBS";
    /*BI*/
    final  String METHOD_NAME_BI = "AjouterBI";
    final String SOAP_ACTION_BI = "http://tempuri.org/AjouterBI";
    /*ajout releve*/
    final  String METHOD_NAME_Rel = "AjouterUsage";
    final String SOAP_ACTION_Rel = "http://tempuri.org/AjouterUsage";
    /*Eqp*/
    final  String METHOD_NAME_EQ = "AjouterEqp";
    final String SOAP_ACTION_EQ = "http://tempuri.org/AjouterEqp";
    /*OT/EquipementsOT*/
    final  String METHOD_NAME_OT = "AjouterDI";
    final String SOAP_ACTION_OT = "http://tempuri.org/AjouterDI";
    /*ListeOT*/
    final  String METHOD_NAME_LOT = "ListeOT";
    final String SOAP_ACTION_LOT = "http://tempuri.org/ListeOT";
    /*ListeEquipementsOT*/
    final  String METHOD_NAME_Leq = "ListeEquipement";
    final String SOAP_ACTION_Leq = "http://tempuri.org/ListeEquipement";
    //Sync OT EquipementsOT//
    ArrayList<Integer> ListeidSoc,ListeidAct,NumOt,app,idMachine,idDemandeur,idExec,idUser,idDegreUrgence,idTypeTravail,idClefTravaux,idEnsemble,idSousEnsemble,idEtat,idSection;
    ArrayList<String>  Description,DateDemande,DateDebut,DateFin;
    ArrayList<String> lance,cloture;
    //Sync OT from SQLserver//
    ArrayList<Integer> LId_Bt = new ArrayList<>();
    ArrayList<String> LDateDM = new ArrayList<>();
    ArrayList<Integer> LIdDm = new ArrayList<>();
    ArrayList<Integer> LSoc = new ArrayList<>();
    ArrayList<Integer> LAct = new ArrayList<>();
    ArrayList<Integer> LNOT = new ArrayList<>();
    ArrayList<Integer> LIdEx = new ArrayList<>();
    ArrayList<Integer> LDU = new ArrayList<>();
    ArrayList<Integer> LTT = new ArrayList<>();
    ArrayList<Integer> LCF = new ArrayList<>();
    ArrayList<Integer> LUser = new ArrayList<>();
    ArrayList<String> LDesc = new ArrayList<>();
    ArrayList<String> LDDB = new ArrayList<>();
    ArrayList<String> LDF = new ArrayList<>();
    ArrayList<Integer> LLance = new ArrayList<>();
    ArrayList<Integer> LCloture = new ArrayList<>();
    ArrayList<Boolean> LApp = new ArrayList<>();
    ArrayList<Integer> LDT = new ArrayList<>();
    ArrayList<String> LOTAtt = new ArrayList<>();
    ArrayList<String> LDateRealisation = new ArrayList<>();
    ArrayList<String> LTrav = new ArrayList<>();
    ArrayList<String> nomEmpEqp = new ArrayList<>();
    ArrayList<String> travEff = new ArrayList<>();
    ArrayList<String> dateSave = new ArrayList<>();
    ArrayList<Integer> dureeTrav = new ArrayList<>();
    //Sync Equipement//
    ArrayList<Integer> LIdEqp = new ArrayList<>();
    ArrayList<Integer> LIdOtEqp = new ArrayList<>();
    ArrayList<Integer> LMachineEqp = new ArrayList<>();
    ArrayList<Integer> LEnsembleEqp = new ArrayList<>();
    ArrayList<Integer> LSSEnsEqp = new ArrayList<>();
    ArrayList<Integer> LEtatEqp = new ArrayList<>();
    ArrayList<Integer> LSecEqp = new ArrayList<>();
    //Sync Machine//
    ArrayList<String> LDesignation = new ArrayList<>();
    ArrayList<Integer> LMachine = new ArrayList<>();
    ArrayList<Integer> LEtat = new ArrayList<>();
    ArrayList<Integer> LSection = new ArrayList<>();
    ArrayList<String> LCode_Machine = new ArrayList<>();
    //Sync Employe//
    ArrayList<String> LName = new ArrayList<>();
    ArrayList<String> LCode = new ArrayList<>();
    ArrayList<Integer> LIdEmploye = new ArrayList<>();
    //Sync TT//
    ArrayList<String> LDesTT = new ArrayList<>();
    ArrayList<String> LCodeTT = new ArrayList<>();
    ArrayList<Integer> LIdTT = new ArrayList<>();
    //Sync CT//
    ArrayList<String> LDesCT = new ArrayList<>();
    ArrayList<String> LCodeCT = new ArrayList<>();
    ArrayList<Integer> LIdCT = new ArrayList<>();
    //Sync DU//
    ArrayList<String> LDesDU = new ArrayList<>();
    ArrayList<Integer> LIdDU = new ArrayList<>();
    //Sync Ens//
    ArrayList<String> LDesEns = new ArrayList<>();
    ArrayList<Integer> LIdEns = new ArrayList<>();
    //Sync Sous Ens//
    ArrayList<String> LDesSousEns = new ArrayList<>();
    ArrayList<Integer> LIdSousEns = new ArrayList<>();
    //Sync Etat//
    ArrayList<String> LDesEtat = new ArrayList<>();
    ArrayList<Integer> LIdEtat = new ArrayList<>();
    //Sync Section//
    ArrayList<String> LDesSec = new ArrayList<>();
    ArrayList<String> LCodeSec = new ArrayList<>();
    ArrayList<Integer> LIdSec = new ArrayList<>();
    ArrayList<Integer> LIdAct = new ArrayList<>();
    ArrayList<Integer> LIdSoc = new ArrayList<>();
    //Sync Compteur//
    ArrayList<String> LDesComp = new ArrayList<>();
    ArrayList<String> LCodeComp = new ArrayList<>();
    ArrayList<Integer> LIdComp = new ArrayList<>();
    ArrayList<Integer> LIdActComp = new ArrayList<>();
    ArrayList<Integer> LIdSocComp = new ArrayList<>();
    //Sync User//
    ArrayList<String> LLogin = new ArrayList<>();
    ArrayList<String> LPass = new ArrayList<>();
    ArrayList<String> role = new ArrayList<>();
    ArrayList<Integer> LId_Act = new ArrayList<>();
    ArrayList<Integer> LId_Soc = new ArrayList<>();
    ArrayList<Integer> LId_User = new ArrayList<>();
    ArrayList<Integer> LIdEmp = new ArrayList<>();
    //Sync Liste Mvt//
    ArrayList<Integer> LNum = new ArrayList<>();
    ArrayList<String> LDate_Mvt = new ArrayList<>();
    ArrayList<Integer> LId_Employee = new ArrayList<>();
    ArrayList<Integer> LId_SocMvt = new ArrayList<>();
    ArrayList<Integer> LId_ActMvt = new ArrayList<>();
    ArrayList<String> LType_Mvt = new ArrayList<>();
    ArrayList<String> LNumAtt = new ArrayList<>();
    ArrayList<String> LidOtMvt = new ArrayList<>();
    //Sync Liste DetailMvt//
    ArrayList<Integer> LId_Mvt = new ArrayList<>();
    ArrayList<Integer> LId_SocM = new ArrayList<>();
    ArrayList<Integer> LId_ActM = new ArrayList<>();
    ArrayList<Double> LPu_Ua = new ArrayList<>();
    ArrayList<Integer> LId_Pdt = new ArrayList<>();
    ArrayList<Double> LQtot_Disp = new ArrayList<>();
    ArrayList<Double> LTotal_TTC = new ArrayList<>();
    ArrayList<Double> LQty_Mvt_Ua = new ArrayList<>();
    ArrayList<Boolean> LMaj_Cost = new ArrayList<>();
    //Sync Bon Sortie et Bon inventaire//
    ArrayList<Integer> idSocBS= new ArrayList<>();
    ArrayList<Integer> idActBS= new ArrayList<>();
    ArrayList<Integer> idSocMvt= new ArrayList<>();
    ArrayList<Integer> idActMvt= new ArrayList<>();
    ArrayList<Integer> NumOtBS= new ArrayList<>();
    ArrayList<Integer> Id_Employee= new ArrayList<>();
    ArrayList<String> Date_Mvt= new ArrayList<>();
    ArrayList<Integer> Id_PdtBS= new ArrayList<>();
    ArrayList<Integer> Qty_Mvt_Ua= new ArrayList<>();
    ArrayList<String> nomEmp= new ArrayList<>();
    ArrayList<Integer> LnumAtt= new ArrayList<>();
    //Bon inventaire//
    ArrayList<Integer> idSocBI= new ArrayList<>();
    ArrayList<Integer> idActBI= new ArrayList<>();
    ArrayList<Integer> Id_EmployeeBI= new ArrayList<>();
    ArrayList<String> Date_MvtBI= new ArrayList<>();

    ArrayList<Integer> Maj_CostBI= new ArrayList<>();
    ArrayList<String> nomEmpBI= new ArrayList<>();

    //Sync Rel//
    ArrayList<Integer> idSocRel= new ArrayList<>();
    ArrayList<Integer> idActRel= new ArrayList<>();
    ArrayList<Integer> machineRel= new ArrayList<>();
    ArrayList<Integer> compteurRel= new ArrayList<>();
    ArrayList<Integer> usageRel= new ArrayList<>();
    ArrayList<String> dateRel= new ArrayList<>();

    ArrayList<Integer> idSocRelSync= new ArrayList<>();
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
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        et_username =  findViewById(R.id.et_username);
        et_username = findViewById(R.id.et_username);
        et_password =  findViewById(R.id.et_password);
        btn_login =  findViewById(R.id.btn_login);
        erreur = findViewById(R.id.erreur);




        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = et_username.getText().toString();
                pass = et_password.getText().toString();
                Boolean res = db.checkUser(user, pass);

                if (!validateUser() | !validatePassword()) {
                    return;
                } else {

                    if (checkNetworkConnection()) {
                        (new MyAsyncTask()).execute(user, pass);

                    } else {
                        if (res == true) {
                            Cursor c = db.idUserLogin(user, pass);
                            Intent intent;
                            while (c.moveToNext()) {
                                if(c.getString((2)).equals("Technicien")){
                                    intent = new Intent(MainActivity.this, ListeDemandeParTech.class);
                                }
                                else {
                                    intent = new Intent(MainActivity.this, StatisticActivity.class);
                                }
                                intent.putExtra("nomDM", c.getInt(0));
                                intent.putExtra("idEmploye", c.getInt(1));
                                intent.putExtra("role", c.getString(2));
                                startActivity(intent);
                            }
                            erreur.setText("");

                        } else if ((res == false) & (!user.isEmpty()) & (!pass.isEmpty())) {
                            erreur.setText("Verifier les champs !");
                        }
                    }
                }
            }


        });
///////////////////////////////Sync/////////////////////////////////////////
        ListeidSoc= new ArrayList<>();
        ListeidAct= new ArrayList<>();
        NumOt= new ArrayList<>();
        idMachine= new ArrayList<>();
        idDemandeur= new ArrayList<>();
        nomEmp= new ArrayList<>();
        lance= new ArrayList<>();
        cloture= new ArrayList<>();
        cloture= new ArrayList<>();
        app= new ArrayList<>();
        DateDebut= new ArrayList<>();
        DateFin= new ArrayList<>();
        idSection= new ArrayList<>();
        idExec= new ArrayList<>();idUser= new ArrayList<>();
        idDegreUrgence= new ArrayList<>();
        idTypeTravail= new ArrayList<>();
        idClefTravaux= new ArrayList<>();
        idEnsemble= new ArrayList<>();
        idSousEnsemble= new ArrayList<>();
        idEtat= new ArrayList<>();
        Description= new ArrayList<>();
        DateDemande= new ArrayList<>();
        Cursor e=db.EqpNonSync();
        while(e.moveToNext())
        {
            idMachine.add(e.getInt(e.getColumnIndex("idMachine")));
            idEnsemble.add(e.getInt(e.getColumnIndex("idEnsemble")));
            idSousEnsemble.add(e.getInt(e.getColumnIndex("idSousEnsemble")));
            idEtat.add(e.getInt(e.getColumnIndex("idEtat")));
            idSection.add(e.getInt(e.getColumnIndex("idSec")));

        }
        Cursor ot=db.OtNonSync();
        while(ot.moveToNext())
        {
            ListeidSoc.add(ot.getInt(ot.getColumnIndex("idSoc")));
            ListeidAct.add(ot.getInt(ot.getColumnIndex("idAct")));
            NumOt.add(ot.getInt(ot.getColumnIndex("NumOt")));
            idDemandeur.add(ot.getInt(ot.getColumnIndex("idDemandeur")));
            idExec.add(ot.getInt(ot.getColumnIndex("idExecuteur")));
            if(ot.getInt(ot.getColumnIndex("demandeLance"))==1)
            {
                lance.add("oui");

            }
            else
            {
                lance.add("non");

            }
            if(ot.getInt(ot.getColumnIndex("demandeCloture"))==1)
            {
                cloture.add("oui");

            }
            else
            {
                cloture.add("non");

            }

            app.add(ot.getInt(ot.getColumnIndex("demandeApp")));
            idUser.add(ot.getInt(ot.getColumnIndex("idUserSave")));
            Description.add(ot.getString(ot.getColumnIndex("Description")));
            idDegreUrgence.add(ot.getInt(ot.getColumnIndex("idDegreUrgence")));
            idTypeTravail.add(ot.getInt(ot.getColumnIndex("idTypeTravail")));
            idClefTravaux.add(ot.getInt(ot.getColumnIndex("idClefTravaux")));
            DateDemande.add(ot.getString(ot.getColumnIndex("DateDemande")));
            DateDebut.add(ot.getString(ot.getColumnIndex("dateDB")));
            DateFin.add(ot.getString(ot.getColumnIndex("dateFin")));
            nomEmpEqp.add(ot.getString(ot.getColumnIndex("nomEmploye")));
            travEff.add(ot.getString(ot.getColumnIndex("travEff")));
            dateSave.add(ot.getString(ot.getColumnIndex("dateSave")));
            dureeTrav.add(ot.getInt(ot.getColumnIndex("dureeTrav")));




        }


        //****************************Ajouter bon sortie********************************
        Cursor cursor=db.BSNonSync();
        while(cursor.moveToNext())
        {

            idSocBS.add(cursor.getInt(cursor.getColumnIndex("idSoc")));
            idActBS.add(cursor.getInt(cursor.getColumnIndex("idAct")));
            NumOtBS.add(cursor.getInt(cursor.getColumnIndex("idOt")));
            Id_Employee.add(cursor.getInt(cursor.getColumnIndex("idDemandeur")));
            Date_Mvt.add(cursor.getString(cursor.getColumnIndex("DateMvt")));

            nomEmp.add(cursor.getString(cursor.getColumnIndex("nomEmploye")));


        }

        //****************************Ajouter bon inventaire********************************
        Cursor cursor2=db.BINonSync();
        while(cursor2.moveToNext())
        {

            idSocBI.add(cursor2.getInt(cursor2.getColumnIndex("idSoc")));
            idActBI.add(cursor2.getInt(cursor2.getColumnIndex("idAct")));
            Id_EmployeeBI.add(cursor2.getInt(cursor2.getColumnIndex("idDemandeur")));
            Date_MvtBI.add(cursor2.getString(cursor2.getColumnIndex("DateMvt")));

            nomEmpBI.add(cursor2.getString(cursor2.getColumnIndex("nomEmploye")));


        }
        Cursor mvt=db.MvtNonSync();
        while(mvt.moveToNext())
        {
            idSocMvt.add(mvt.getInt(mvt.getColumnIndex("idSoc")));
            idActMvt.add(mvt.getInt(mvt.getColumnIndex("idAct")));
            Maj_CostBI.add(mvt.getInt(mvt.getColumnIndex("MP")));
            Id_PdtBS.add(mvt.getInt(mvt.getColumnIndex("idPd")));
            Qty_Mvt_Ua.add(mvt.getInt(mvt.getColumnIndex("qtePdt")));
            LnumAtt.add(mvt.getInt(mvt.getColumnIndex("idMouv")));

        }

        //****************************Ajouter releve********************************
        Cursor cursor3=db.ReleveNonSync();
        while(cursor3.moveToNext())
        {

            idSocRel.add(cursor3.getInt(cursor3.getColumnIndex("idSoc")));
            idActRel.add(cursor3.getInt(cursor3.getColumnIndex("idAct")));
            machineRel.add(cursor3.getInt(cursor3.getColumnIndex("idMachine")));
            compteurRel.add(cursor3.getInt(cursor3.getColumnIndex("idCompteur")));
            usageRel.add(cursor3.getInt(cursor3.getColumnIndex("usageRel")));
            dateRel.add(cursor3.getString(cursor3.getColumnIndex("dateUsage")));


        }


    }


    private boolean validateUser() {
        String user = et_username.getText().toString();
        if (user.isEmpty()) {
            et_username.setError("Le champ pseudo est vide!");
            return false;
        } else {
            et_username.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String pass = et_password.getText().toString();
        if (pass.isEmpty()) {
            et_password.setError("Le champ mot de passe est vide");
            return false;
        } else {
            et_password.setError(null);
            return true;
        }
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            PropertyInfo infoEmail = new PropertyInfo();
            infoEmail.setName("Login");
            infoEmail.setType(String.class);
            infoEmail.setValue(strings[0]);
            request.addProperty(infoEmail);

            PropertyInfo infoPass = new PropertyInfo();
            infoPass.setName("Mdp");
            infoPass.setType(String.class);
            infoPass.setValue(strings[1]);
            request.addProperty(infoPass);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapObject result = (SoapObject) envelope.getResponse();
                if (result != null) {
                    ContactResult.result = Boolean.valueOf(result.getProperty("result").toString());
                    ContactResult.userID = Integer.parseInt(result.getProperty("UserID").toString());
                    ContactResult.employeID = Integer.parseInt(result.getProperty("EmployeeID").toString());
                    ContactResult.role = result.getProperty("Role").toString();

                }
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return String.valueOf(ContactResult.result);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("true")) {
                erreur.setText("");

                Intent intent;
                if(ContactResult.role.equals("Technicien")){
                    intent = new Intent(MainActivity.this, ListeDemandeParTech.class);
                }
                else {
                    intent = new Intent(MainActivity.this, StatisticActivity.class);
                }
                intent.putExtra("nomDM", ContactResult.userID);
                intent.putExtra("idEmploye", ContactResult.employeID);
                intent.putExtra("role", ContactResult.role);
                startActivity(intent);

                (new MachineAsyncTask()).execute();
                (new TTAsyncTask()).execute();
                (new ProductAsyncTask()).execute();
                (new EmployeAsyncTask()).execute();
                (new DUAsyncTask()).execute();
                (new CTAsyncTask()).execute();
                (new EtatAsyncTask()).execute();
                (new SectionAsyncTask()).execute();
                (new UserAsyncTask()).execute();
                (new CompteurAsyncTask()).execute();
                (new EnsAsyncTask()).execute();
                (new SousEnsAsyncTask()).execute();
                for (int i = 0; i < ListeidAct.size(); i++) {
                    (new OTAsyncTask()).execute(String.valueOf(ListeidSoc.get(i)), String.valueOf(ListeidAct.get(i)), String.valueOf(idDemandeur.get(i))
                            ,String.valueOf(idExec.get(i)),String.valueOf(idUser.get(i)),
                            String.valueOf( idDegreUrgence.get(i)),lance.get(i),cloture.get(i),String.valueOf(app.get(i))
                            ,String.valueOf(idTypeTravail.get(i)), String.valueOf(idClefTravaux.get(i))
                            , Description.get(i), DateDemande.get(i), NumOt.get(i)+" "+nomEmpEqp.get(i),dateSave.get(i),DateDemande.get(i),DateDemande.get(i),
                            String.valueOf(dureeTrav.get(i)),travEff.get(i));
                }
                (new ListeEqpAsyncTask()).execute();
                (new ListeOTAsyncTask()).execute();

                for (int i = 0; i < idActBS.size(); i++) {
                    (new BSAsyncTask()).execute(String.valueOf(idSocBS.get(i)), String.valueOf(idActBS.get(i)),String.valueOf( NumOtBS.get(i))
                            ,String.valueOf( Id_Employee.get(i)), Date_Mvt.get(i),  LnumAtt.get(i)+" "+nomEmp.get(i));
                }

                for (int i = 0; i < idActBI.size(); i++) {
                    (new BIAsyncTask()).execute(String.valueOf(idSocBI.get(i)), String.valueOf(idActBI.get(i))
                            ,String.valueOf(Id_EmployeeBI.get(i)), Date_MvtBI.get(i),  LnumAtt.get(i)+" "+nomEmpBI.get(i));
                }

                (new ListeMvtAsyncTask()).execute();
                (new ListeDetailMvtAsyncTask()).execute();
                for (int i = 0; i < machineRel.size(); i++) {
                    (new ReleveAsyncTask()).execute(String.valueOf(idSocRel.get(i)), String.valueOf(idActRel.get(i)), String.valueOf(machineRel.get(i)), String.valueOf(compteurRel.get(i))
                            , String.valueOf(usageRel.get(i)),dateRel.get(i));
                }


                (new ListReleveAsyncTask()).execute();
            } else {
                erreur.setText("nom d'utilisateur ou mot de passe incorrect");
            }
            super.onPostExecute(s);
        }
    }

    private class MachineAsyncTask extends AsyncTask<String, Void, String> {
        String Designation;
        int Id_Machine;
        int Id_Etat;
        int Id_Section;
        String Code_Machine;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAP_ACTION1, envelope);

                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject res = (SoapObject) result.getProperty(0);

                for (int i = 0; i < res.getPropertyCount(); i++) {
                    Object property = res.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject category_list = (SoapObject) property;
                        Designation = category_list.getProperty("Designation").toString();
                        Id_Machine = Integer.parseInt(category_list.getProperty("Id_Machine").toString());
                        Id_Etat = Integer.parseInt(category_list.getProperty("Id_Etat").toString());
                        Id_Section = Integer.parseInt(category_list.getProperty("Id_Section").toString());
                        Code_Machine = category_list.getProperty("Code_Machine").toString();

                        LDesignation.add(Designation);
                        LMachine.add(Id_Machine);
                        LEtat.add(Id_Etat);
                        LSection.add(Id_Section);
                        LCode_Machine.add(Code_Machine);


                    }


                }


            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return Designation;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                db.deleteMachine();
                for (int i = 0; i < LDesignation.size(); i++) {
                    db.insertMachine(LMachine.get(i), LDesignation.get(i), LCode_Machine.get(i), LSection.get(i), LEtat.get(i));

                }
            } else {
                Toast.makeText(MainActivity.this, "No Date To Show", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }
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
                db.deleteProducts();
                for (int i = 0; i < LCode_Pdt.size(); i++) {
                    db.insertProducts(LId_Product.get(i), LCode_Pdt.get(i), LDescription.get(i), LQtyOnHand.get(i), LDprix.get(i), LId_ActPdt.get(i), LId_SocPdt.get(i), LId_Cat_Art.get(i), LId_SS_Cat.get(i), LId_Unite.get(i));

                }
            } else {
                Toast.makeText(MainActivity.this, "No Date To Show", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }
    private class ReleveAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_Rel);

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

            PropertyInfo id_Machine=new PropertyInfo();
            id_Machine.setName("id_Machine");
            id_Machine.setType(String.class);
            id_Machine.setValue(strings[2]);
            request.addProperty(id_Machine);

            PropertyInfo id_Compteur=new PropertyInfo();
            id_Compteur.setName("id_Compteur");
            id_Compteur.setType(String.class);
            id_Compteur.setValue(strings[3]);
            request.addProperty(id_Compteur);

            PropertyInfo usage_rel=new PropertyInfo();
            usage_rel.setName("usage_rel");
            usage_rel.setType(String.class);
            usage_rel.setValue(strings[4]);
            request.addProperty(usage_rel);

            PropertyInfo Date=new PropertyInfo();
            Date.setName("Date");
            Date.setType(String.class);
            Date.setValue(strings[5]);
            request.addProperty(Date);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);



            try {

                androidHttpTransport.call(SOAP_ACTION_Rel, envelope);

                SoapObject result = (SoapObject) envelope.getResponse();
                if (result != null)
                {
                    ReturnResult=result.getProperty("result").toString();
                }


            } catch (Exception e) {
                e.printStackTrace();
                return  e.toString();
            }
            return ReturnResult;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("true")) {
                db.updateSyncReleve();

            } else {
                Toast.makeText(MainActivity.this, "No Date To Show"+s, Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }
    private class ListReleveAsyncTask extends AsyncTask<String, Void, String> {
        String dateUsage;
        int usage,machine,compteur,soc,act;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_ListRel);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);



            try {

                androidHttpTransport.call(SOAP_ACTION_ListRel, envelope);

                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject res = (SoapObject) result.getProperty(0);


                for (int i = 0; i < res.getPropertyCount(); i++) {
                    Object property = res.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject category_list = (SoapObject) property;
                        soc = Integer.parseInt(category_list.getProperty("Id_Soc").toString());
                        act = Integer.parseInt(category_list.getProperty("Id_Act").toString());
                        machine = Integer.parseInt(category_list.getProperty("Id_Machine").toString());
                        compteur = Integer.parseInt(category_list.getProperty("Id_Compteur").toString());
                        usage = Integer.parseInt(category_list.getProperty("Usage_En_Cours").toString());
                        dateUsage = category_list.getProperty("Date_Usage").toString();

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

                        Date dateU= null;
                        try {
                            dateU = formatter.parse(dateUsage);

                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }
                        formatter.applyPattern("dd/MM/yyyy HH:mm");

                        idSocRelSync.add(soc);
                        idActRel.add(act);
                        machineRel.add(machine);
                        compteurRel.add(compteur);
                        usageRel.add(usage);
                        dateRel.add(formatter.format(dateU));


                    }



                }
            } catch (Exception e) {
                e.printStackTrace();
                return  e.toString();
            }
            return String.valueOf(machine);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                db.deleteReleve();
                for (int i = 0; i < idSocRelSync.size(); i++) {
                    db.insertReleve(idSocRelSync.get(i),idActRel.get(i),machineRel.get(i), compteurRel.get(i), usageRel.get(i), dateRel.get(i));}
                //db.insertReleve(1,1,1, 1,1, "15/02/2011");


            } else {
                Toast.makeText(MainActivity.this, "No Date To Show", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }

    private class OTAsyncTask extends AsyncTask<String, Void, String> {
        int nbt;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_OT);

            PropertyInfo idSoc = new PropertyInfo();
            idSoc.setName("id_Soc");
            idSoc.setType(String.class);
            idSoc.setValue(strings[0]);
            request.addProperty(idSoc);

            PropertyInfo idAct = new PropertyInfo();
            idAct.setName("id_Act");
            idAct.setType(String.class);
            idAct.setValue(strings[1]);
            request.addProperty(idAct);


            PropertyInfo Nom_Demande = new PropertyInfo();
            Nom_Demande.setName("Nom_Demande");
            Nom_Demande.setType(String.class);
            Nom_Demande.setValue(strings[2]);
            request.addProperty(Nom_Demande);

            PropertyInfo Nom_Emetteur = new PropertyInfo();
            Nom_Emetteur.setName("Nom_Emetteur");
            Nom_Emetteur.setType(String.class);
            Nom_Emetteur.setValue(strings[3]);
            request.addProperty(Nom_Emetteur);

            PropertyInfo Nom_User = new PropertyInfo();
            Nom_User.setName("Nom_User");
            Nom_User.setType(String.class);
            Nom_User.setValue(strings[4]);
            request.addProperty(Nom_User);


            PropertyInfo id_DU = new PropertyInfo();
            id_DU.setName("id_DU");
            id_DU.setType(String.class);
            id_DU.setValue(strings[5]);
            request.addProperty(id_DU);

            PropertyInfo Demande = new PropertyInfo();
            Demande.setName("Demande");
            Demande.setType(String.class);
            Demande.setValue(strings[6]);
            request.addProperty(Demande);

            PropertyInfo Cloture = new PropertyInfo();
            Cloture.setName("Cloture");
            Cloture.setType(String.class);
            Cloture.setValue(strings[7]);
            request.addProperty(Cloture);

            PropertyInfo Approuver = new PropertyInfo();
            Approuver.setName("Approuver");
            Approuver.setType(String.class);
            Approuver.setValue(strings[8]);
            request.addProperty(Approuver);


            PropertyInfo id_TT = new PropertyInfo();
            id_TT.setName("id_TT");
            id_TT.setType(String.class);
            id_TT.setValue(strings[9]);
            request.addProperty(id_TT);

            PropertyInfo id_Panne = new PropertyInfo();
            id_Panne.setName("id_Panne");
            id_Panne.setType(String.class);
            id_Panne.setValue(strings[10]);
            request.addProperty(id_Panne);

            PropertyInfo desc = new PropertyInfo();
            desc.setName("desc");
            desc.setType(String.class);
            desc.setValue(strings[11]);
            request.addProperty(desc);

            PropertyInfo Date = new PropertyInfo();
            Date.setName("Date");
            Date.setType(String.class);
            Date.setValue(strings[12]);
            request.addProperty(Date);

            PropertyInfo NBTAttiribue = new PropertyInfo();
            NBTAttiribue.setName("NBTAttiribue");
            NBTAttiribue.setType(String.class);
            NBTAttiribue.setValue(strings[13]);
            request.addProperty(NBTAttiribue);

            PropertyInfo Date_Realisation = new PropertyInfo();
            Date_Realisation.setName("Date_Realisation");
            Date_Realisation.setType(String.class);
            Date_Realisation.setValue(strings[14]);
            request.addProperty(Date_Realisation);

            PropertyInfo Date_Debut = new PropertyInfo();
            Date_Debut.setName("Date_Debut");
            Date_Debut.setType(String.class);
            Date_Debut.setValue(strings[15]);
            request.addProperty(Date_Debut);

            PropertyInfo Date_Fin = new PropertyInfo();
            Date_Fin.setName("Date_Fin");
            Date_Fin.setType(String.class);
            Date_Fin.setValue(strings[16]);
            request.addProperty(Date_Fin);

            PropertyInfo Duree_Travail=new PropertyInfo();
            Duree_Travail.setName("Duree_Travail");
            Duree_Travail.setType(String.class);
            Duree_Travail.setValue(strings[17]);
            request.addProperty(Duree_Travail);

            PropertyInfo Trv_Effectue=new PropertyInfo();
            Trv_Effectue.setName("Trv_Effectue");
            Trv_Effectue.setType(String.class);
            Trv_Effectue.setValue(strings[18]);
            request.addProperty(Trv_Effectue);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(SOAP_ACTION_OT, envelope);
                SoapObject result = (SoapObject) envelope.getResponse();
                if (result != null)
                {
                    ReturnResult=result.getProperty("result").toString();
                    nbt= Integer.parseInt(result.getProperty("NBT").toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return ReturnResult;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("true"))
            {

                for (int i = 0; i < idMachine.size(); i++) {
                    (new MainActivity.MyEqpAsyncTask()).execute(String.valueOf(nbt),String.valueOf( idMachine.get(i)),String.valueOf(idEnsemble.get(i)),String.valueOf( idSousEnsemble.get(i))
                            , String.valueOf(idEtat.get(i)),String.valueOf(idSection.get(i))) ;}
                db.updateSync();
                for(int i=0;i<NumOt.size();i++)
                {
                    db.updateEqpSync(NumOt.get(i));
                }


            }
            else
            {
                Toast.makeText(MainActivity.this, ""+s, Toast.LENGTH_SHORT).show();

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
            super.onPostExecute(s);
        }
    }

    private class ListeOTAsyncTask extends AsyncTask<String, Void, String> {
        int Id_Bt,Nom_Demandeur,Id_Soc,Id_Act,NBT,Nom_Emetteur,Id_D_Urgence,Id_Clef_trv,
                Id_Type_Trv,Nom_User,Duree_Travail;
        Boolean Approuver;
        String Date_Demande,Description_Dmd,Date_Debut,Date_Fin
                ,Date_Realisation,Demande,Cloture,NBTAttiribue,Trv_Effectue;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_LOT);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAP_ACTION_LOT, envelope);

                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject res = (SoapObject) result.getProperty(0);

                for (int i = 0; i < res.getPropertyCount(); i++) {
                    Object property = res.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject category_list = (SoapObject) property;
                        Id_Bt = Integer.parseInt(category_list.getProperty("Id_Bt").toString());
                        Nom_Demandeur = Integer.parseInt(category_list.getProperty("Nom_Demandeur").toString());
                        Id_Soc = Integer.parseInt(category_list.getProperty("Id_Soc").toString());
                        Id_Act = Integer.parseInt(category_list.getProperty("Id_Act").toString());
                        NBT = Integer.parseInt(category_list.getProperty("NBT").toString());
                        Nom_Emetteur = Integer.parseInt(category_list.getProperty("Nom_Emetteur").toString());
                        Id_D_Urgence = Integer.parseInt(category_list.getProperty("Id_D_Urgence").toString());
                        Id_Clef_trv = Integer.parseInt(category_list.getProperty("Id_Clef_trv").toString());
                        Id_Type_Trv = Integer.parseInt(category_list.getProperty("Id_Type_Trv").toString());
                        Nom_User = Integer.parseInt(category_list.getProperty("Nom_User").toString());
                        Duree_Travail = Integer.parseInt(category_list.getProperty("Duree_Travail").toString());
                        Approuver = Boolean.valueOf(category_list.getProperty("Approuver").toString());
                        Date_Demande = category_list.getProperty("Date_Demande").toString();
                        Description_Dmd = category_list.getProperty("Description_Dmd").toString();
                        Date_Debut = category_list.getProperty("Date_Debut").toString();
                        Date_Fin = category_list.getProperty("Date_Fin").toString();
                        Demande = category_list.getProperty("Demande").toString();
                        Cloture = category_list.getProperty("Cloture").toString();
                        NBTAttiribue = category_list.getProperty("NBTAttiribue").toString();
                        Trv_Effectue = category_list.getProperty("Trv_Effectue").toString();
                        Date_Realisation = category_list.getProperty("Date_Realisation").toString();

                        LUser.add(Nom_User);
                        LDT.add(Duree_Travail);
                        LId_Bt.add(Id_Bt);
                        LApp.add(Approuver);


                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

                        Date dateDM = null;
                        Date dateD = null;
                        Date dateF = null;
                        Date dateR = null;
                        try
                        {
                            dateDM = formatter.parse(Date_Demande);
                            dateD = formatter.parse(Date_Debut);
                            dateF = formatter.parse(Date_Fin);
                            dateR = formatter.parse(Date_Realisation);
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }
                        formatter.applyPattern("dd/MM/yyyy HH:mm");
                        LDateDM.add(formatter.format(dateDM));
                        LDDB.add(formatter.format(dateD));
                        LDF.add(formatter.format(dateF));
                        LDateRealisation.add(formatter.format(dateR));

                        if(Demande.equals("oui"))
                        {
                            LLance.add(1);

                        }
                        else
                        {
                            LLance.add(0);

                        }
                        if(Cloture.equals("oui"))
                        {
                            LCloture.add(1);

                        }
                        else
                        {
                            LCloture.add(0);

                        }

                        LOTAtt.add(NBTAttiribue);
                        LSoc.add(Id_Soc);
                        LAct.add(Id_Act);
                        LIdDm.add(Nom_Demandeur);
                        LIdEx.add(Nom_Emetteur);
                        LNOT.add(NBT);
                        LDU.add(Id_D_Urgence);
                        LCF.add(Id_Clef_trv);
                        LTT.add(Id_Type_Trv);
                        if(Description_Dmd.equals("anyType{}"))
                        {
                            LDesc.add("");
                        }
                        else {
                            LDesc.add(Description_Dmd);
                        }
                        if(Trv_Effectue.equals("anyType{}"))
                        {
                            LTrav.add("");
                        }
                        else {
                            LTrav.add(Trv_Effectue);

                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return Date_Demande;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                db.deleteOT();

                for (int i = 0; i < LDateDM.size(); i++) {
                    db.insertOT(LDateDM.get(i),LIdDm.get(i), LSoc.get(i),LAct.get(i)
                            ,LNOT.get(i),LIdEx.get(i),LDU.get(i),LCF.get(i),LTT.get(i)
                            ,LDesc.get(i),LUser.get(i),LDDB.get(i)
                            ,LDF.get(i),LDT.get(i),String.valueOf(LLance.get(i)),String.valueOf(LCloture.get(i))
                            ,LApp.get(i),LOTAtt.get(i),LTrav.get(i),LDateRealisation.get(i));

                }


            } else {
                Toast.makeText(MainActivity.this, "No Date To Show", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }
    private class ListeEqpAsyncTask extends AsyncTask<String, Void, String> {
        int Id,Id_BT,Id_Machine,Id_Ensemble,Id_SSEnsemble,Etat_D,Id_Section;


        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_Leq);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAP_ACTION_Leq, envelope);

                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject res = (SoapObject) result.getProperty(0);

                for (int i = 0; i < res.getPropertyCount(); i++) {
                    Object property = res.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject category_list = (SoapObject) property;
                        Id = Integer.parseInt(category_list.getProperty("Id").toString());
                        Id_BT = Integer.parseInt(category_list.getProperty("Id_BT").toString());
                        Id_Machine = Integer.parseInt(category_list.getProperty("Id_Machine").toString());
                        Id_Ensemble = Integer.parseInt(category_list.getProperty("Id_Ensemble").toString());
                        Id_SSEnsemble = Integer.parseInt(category_list.getProperty("Id_SSEnsemble").toString());
                        Etat_D = Integer.parseInt(category_list.getProperty("Etat_D").toString());
                        Id_Section = Integer.parseInt(category_list.getProperty("Id_Section").toString());

                        LIdEqp.add(Id);
                        LIdOtEqp.add(Id_BT);
                        LMachineEqp.add(Id_Machine);
                        LEnsembleEqp.add(Id_Ensemble);
                        LSSEnsEqp.add(Id_SSEnsemble);
                        LEtatEqp.add(Etat_D);
                        LSecEqp.add(Id_Section);

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return String.valueOf(Id_Machine);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {

                db.deleteEqp();
                for (int i = 0; i < LIdEqp.size(); i++) {
                    db.insertEuipementsOT(LIdOtEqp.get(i),LMachineEqp.get(i),LEnsembleEqp.get(i),LSSEnsEqp.get(i),LEtatEqp.get(i),LSecEqp.get(i));
                }




            } else {
                Toast.makeText(MainActivity.this, "No Date To Show"+s, Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }
    private class ListeMvtAsyncTask extends AsyncTask<String, Void, String> {
        int Num;
        String Date_Mvt;
        int Id_Employee;
        int Id_SocMvt;
        int Id_ActMvt;
        String Type_Mvt,numAtt,idOtMvt;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_ListeMvt);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAP_ACTION_ListeMvt, envelope);

                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject res = (SoapObject) result.getProperty(0);

                for (int i = 0; i < res.getPropertyCount(); i++) {
                    Object property = res.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject category_list = (SoapObject) property;
                        Date_Mvt = category_list.getProperty("Date_Mvt").toString();
                        Num = Integer.parseInt(category_list.getProperty("Num").toString());
                        Id_Employee = Integer.parseInt(category_list.getProperty("Id_Employee").toString());
                        Id_SocMvt = Integer.parseInt(category_list.getProperty("Id_Soc").toString());
                        Id_ActMvt = Integer.parseInt(category_list.getProperty("Id_Act").toString());
                        Type_Mvt = category_list.getProperty("Type_Mvt").toString();
                        numAtt = category_list.getProperty("Num_Att").toString();
                        idOtMvt = category_list.getProperty("Nbt").toString();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

                        Date dateM= null;
                        try {
                            dateM = formatter.parse(Date_Mvt);

                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }
                        formatter.applyPattern("dd/MM/yyyy HH:mm");

                        LDate_Mvt.add(formatter.format(dateM));
                        LNum.add(Num);
                        LId_Employee.add(Id_Employee);
                        LId_SocMvt.add(Id_SocMvt);
                        LId_ActMvt.add(Id_ActMvt);
                        LType_Mvt.add(Type_Mvt);
                        LNumAtt.add(numAtt);
                        LidOtMvt.add(idOtMvt);

                    }


                }


            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return Type_Mvt;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                db.deleteListeMvt();
                for (int i = 0; i < LNum.size(); i++) {
                    db.insertListeMvt(LNum.get(i), LDate_Mvt.get(i), LId_ActMvt.get(i), LId_SocMvt.get(i), LId_Employee.get(i), LType_Mvt.get(i), LNumAtt.get(i), Integer.parseInt(LidOtMvt.get(i)));

                }
            } else {
                Toast.makeText(MainActivity.this, "No Date To Show", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }

    private class ListeDetailMvtAsyncTask extends AsyncTask<String, Void, String> {
        int Id_Mvt;
        int Id_Pdt;
        int id_Soc,id_Act;
        double Pu_Ua;
        double Qtot_Disp;
        double Total_TTC;
        double Qty_Mvt_Ua;
        boolean Maj_Cost;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_ListeDetailMvt);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAP_ACTION_ListeDetailMvt, envelope);

                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject res = (SoapObject) result.getProperty(0);

                for (int i = 0; i < res.getPropertyCount(); i++) {
                    Object property = res.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject category_list = (SoapObject) property;
                        Id_Mvt = Integer.parseInt(category_list.getProperty("Id_Mvt").toString());
                        Id_Pdt = Integer.parseInt(category_list.getProperty("Id_Pdt").toString());
                        id_Soc = Integer.parseInt(category_list.getProperty("id_Soc").toString());
                        id_Act = Integer.parseInt(category_list.getProperty("id_Act").toString());
                        Pu_Ua = Double.parseDouble(category_list.getProperty("Pu_Ua").toString());
                        Qtot_Disp = Double.parseDouble(category_list.getProperty("Qtot_Disp").toString());
                        Total_TTC = Double.parseDouble(category_list.getProperty("Total_TTC").toString());
                        Qty_Mvt_Ua = Double.parseDouble(category_list.getProperty("Qty_Mvt_Ua").toString());
                        Maj_Cost = Boolean.parseBoolean(category_list.getProperty("Maj_Cost").toString());

                        LId_Mvt.add(Id_Mvt);
                        LId_Pdt.add(Id_Pdt);
                        LPu_Ua.add(Pu_Ua);
                        LQtot_Disp.add(Qtot_Disp);
                        LTotal_TTC.add(Total_TTC);
                        LQty_Mvt_Ua.add(Qty_Mvt_Ua);
                        LMaj_Cost.add(Maj_Cost);
                        LId_ActM.add(id_Act);
                        LId_SocM.add(id_Soc);

                    }


                }


            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return String.valueOf(Id_Mvt);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                db.deleteDetailListeMvt();
                for (int i = 0; i < LId_Mvt.size(); i++) {
                    db.insertDetailListeMvt(LId_Mvt.get(i), LId_Pdt.get(i), LPu_Ua.get(i), LQtot_Disp.get(i), LTotal_TTC.get(i), LQty_Mvt_Ua.get(i), LMaj_Cost.get(i), LId_SocM.get(i),LId_ActM.get(i));

                }
            } else {
                Toast.makeText(MainActivity.this, "No Date To Show", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }

    private class EmployeAsyncTask extends AsyncTask<String, Void, String> {
        String Code_Employee;
        int Id_Employee;
        String Name;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAP_ACTION2, envelope);

                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject res = (SoapObject) result.getProperty(0);

                for (int i = 0; i < res.getPropertyCount(); i++) {
                    Object property = res.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject category_list = (SoapObject) property;
                        Code_Employee = category_list.getProperty("Code_Employee").toString();
                        Id_Employee = Integer.parseInt(category_list.getProperty("Id_Employee").toString());
                        Name = category_list.getProperty("Name").toString();

                        LIdEmploye.add(Id_Employee);
                        LCode.add(Code_Employee);
                        LName.add(Name);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return Name;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                db.deleteEmploye();
                for (int i = 0; i < LName.size(); i++) {
                    db.insertEmploye(LIdEmploye.get(i), LName.get(i), LCode.get(i));

                }


            } else {
                Toast.makeText(MainActivity.this, "No Date To Show", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }

    private class TTAsyncTask extends AsyncTask<String, Void, String> {
        String Code_Classe;
        int Id_Type_Trv;
        String Designation;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME4);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAP_ACTION4, envelope);

                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject res = (SoapObject) result.getProperty(0);

                for (int i = 0; i < res.getPropertyCount(); i++) {
                    Object property = res.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject category_list = (SoapObject) property;
                        Code_Classe = category_list.getProperty("Code_Classe").toString();
                        Id_Type_Trv = Integer.parseInt(category_list.getProperty("Id_Type_Trv").toString());
                        Designation = category_list.getProperty("Designation").toString();

                        LDesTT.add(Designation);
                        LCodeTT.add(Code_Classe);
                        LIdTT.add(Id_Type_Trv);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return Designation;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                db.deleteTT();
                for (int i = 0; i < LDesTT.size(); i++) {
                    db.insertTT(LIdTT.get(i), LCodeTT.get(i), LDesTT.get(i));

                }


            } else {
                Toast.makeText(MainActivity.this, "No Date To Show", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }

    private class DUAsyncTask extends AsyncTask<String, Void, String> {
        String Designation;
        int Id_DegUrgence;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME0);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAP_ACTION0, envelope);

                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject res = (SoapObject) result.getProperty(0);

                for (int i = 0; i < res.getPropertyCount(); i++) {
                    Object property = res.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject category_list = (SoapObject) property;
                        Designation = category_list.getProperty("Designation").toString();
                        Id_DegUrgence = Integer.parseInt(category_list.getProperty("Id_DegUrgence").toString());


                        LIdDU.add(Id_DegUrgence);
                        LDesDU.add(Designation);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return Designation;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                db.deleteDU();
                for (int i = 0; i < LDesDU.size(); i++) {
                    db.insertDU(LIdDU.get(i), LDesDU.get(i));

                }
            } else {
                Toast.makeText(MainActivity.this, "No Date To Show", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }

    private class CTAsyncTask extends AsyncTask<String, Void, String> {
        String Designation;
        String Code_clef;
        int Id_Clef;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_CF);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAP_ACTION_CF, envelope);

                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject res = (SoapObject) result.getProperty(0);

                for (int i = 0; i < res.getPropertyCount(); i++) {
                    Object property = res.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject category_list = (SoapObject) property;
                        Designation = category_list.getProperty("Designation").toString();
                        Code_clef = category_list.getProperty("Code_clef").toString();
                        Id_Clef = Integer.parseInt(category_list.getProperty("Id_Clef").toString());


                        LIdCT.add(Id_Clef);
                        LDesCT.add(Designation);
                        LCodeCT.add(Code_clef);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return Designation;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                db.deletePanne();
                for (int i = 0; i < LDesCT.size(); i++) {
                    db.insertPanne(LIdCT.get(i), LDesCT.get(i), LCodeCT.get(i));

                }
            } else {
                Toast.makeText(MainActivity.this, "No Date To Show", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }

    private class EtatAsyncTask extends AsyncTask<String, Void, String> {
        String Designation;
        int Id_Etat;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_Etat);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAP_ACTION_Etat, envelope);

                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject res = (SoapObject) result.getProperty(0);

                for (int i = 0; i < res.getPropertyCount(); i++) {
                    Object property = res.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject category_list = (SoapObject) property;
                        Designation = category_list.getProperty("designation").toString();
                        Id_Etat = Integer.parseInt(category_list.getProperty("Id_Etat").toString());


                        LIdEtat.add(Id_Etat);
                        LDesEtat.add(Designation);


                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return Designation;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                db.deleteEtat();
                for (int i = 0; i < LDesEtat.size(); i++) {
                    db.insertEtat(LIdEtat.get(i), LDesEtat.get(i));

                }
            } else {
                Toast.makeText(MainActivity.this, "No Date To Show", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }

    private class SectionAsyncTask extends AsyncTask<String, Void, String> {
        String Designation;
        String Code_Section;
        int Id_Section;
        int Id_Act;
        int Id_Soc;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_sec);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAP_ACTION_sec, envelope);

                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject res = (SoapObject) result.getProperty(0);

                for (int i = 0; i < res.getPropertyCount(); i++) {
                    Object property = res.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject category_list = (SoapObject) property;
                        Designation = category_list.getProperty("Designation").toString();
                        Code_Section = category_list.getProperty("Code_Section").toString();
                        Id_Section = Integer.parseInt(category_list.getProperty("Id_Section").toString());
                        Id_Act = Integer.parseInt(category_list.getProperty("Id_Act").toString());
                        Id_Soc = Integer.parseInt(category_list.getProperty("Id_Soc").toString());


                        LIdSec.add(Id_Section);
                        LIdAct.add(Id_Act);
                        LIdSoc.add(Id_Soc);
                        LDesSec.add(Designation);
                        LCodeSec.add(Code_Section);


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return Designation;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                db.deleteSection();
                for (int i = 0; i < LDesSec.size(); i++) {
                    db.insertSection(LIdSec.get(i), LDesSec.get(i), LCodeSec.get(i), LIdAct.get(i), LIdSoc.get(i));

                }
            } else {
                Toast.makeText(MainActivity.this, "No Date To Show", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }

    private class SousEnsAsyncTask extends AsyncTask<String, Void, String> {
        String Designation;
        int Id_SS_Ens;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SousEns);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAP_ACTION_SousEns, envelope);

                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject res = (SoapObject) result.getProperty(0);

                for (int i = 0; i < res.getPropertyCount(); i++) {
                    Object property = res.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject category_list = (SoapObject) property;
                        Designation = category_list.getProperty("Designation").toString();
                        Id_SS_Ens = Integer.parseInt(category_list.getProperty("Id_SS_Ens").toString());


                        LIdSousEns.add(Id_SS_Ens);
                        LDesSousEns.add(Designation);


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return Designation;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                db.deleteSousEnsemble();
                for (int i = 0; i < LDesSousEns.size(); i++) {
                    db.insertSousEnsemble(LIdSousEns.get(i), LDesSousEns.get(i));

                }
            } else {
                Toast.makeText(MainActivity.this, "No Date To Show", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }

    private class EnsAsyncTask extends AsyncTask<String, Void, String> {
        String Designation;
        int Id__Ens;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_Ens);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAP_ACTION_Ens, envelope);

                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject res = (SoapObject) result.getProperty(0);

                for (int i = 0; i < res.getPropertyCount(); i++) {
                    Object property = res.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject category_list = (SoapObject) property;
                        Designation = category_list.getProperty("Designation").toString();
                        Id__Ens = Integer.parseInt(category_list.getProperty("Id__Ens").toString());


                        LIdEns.add(Id__Ens);
                        LDesEns.add(Designation);


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return Designation;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                db.deleteEnsemble();
                for (int i = 0; i < LDesEns.size(); i++) {
                    db.insertEnsemble(LIdEns.get(i), LDesEns.get(i));

                }
            } else {
                Toast.makeText(MainActivity.this, "No Date To Show", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }

    private class CompteurAsyncTask extends AsyncTask<String, Void, String> {
        String designation;
        String codeComp;
        int idComp;
        int idSoc;
        int idAct;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_Comp);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAP_ACTION_Comp, envelope);

                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject res = (SoapObject) result.getProperty(0);

                for (int i = 0; i < res.getPropertyCount(); i++) {
                    Object property = res.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject category_list = (SoapObject) property;
                        designation = category_list.getProperty("designation").toString();
                        codeComp = category_list.getProperty("codeComp").toString();
                        idComp = Integer.parseInt(category_list.getProperty("idComp").toString());
                        idSoc = Integer.parseInt(category_list.getProperty("idSoc").toString());
                        idAct = Integer.parseInt(category_list.getProperty("idAct").toString());


                        LDesComp.add(designation);
                        LCodeComp.add(codeComp);
                        LIdComp.add(idComp);
                        LIdSocComp.add(idSoc);
                        LIdActComp.add(idAct);


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return designation;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                db.deleteCompteur();
                for (int i = 0; i < LDesComp.size(); i++) {
                    db.insertCompteur(LIdComp.get(i), LDesComp.get(i), LCodeComp.get(i), LIdSocComp.get(i), LIdActComp.get(i));

                }
            } else {
                Toast.makeText(MainActivity.this, "No Date To Show", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }

    private class UserAsyncTask extends AsyncTask<String, Void, String> {
        String Login;
        String Mdp;
        String Id_Role;
        int Id_User;
        int Id_Soc;
        int Id_Act;
        int Id_Employee;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_User);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAP_ACTION_User, envelope);

                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject res = (SoapObject) result.getProperty(0);

                for (int i = 0; i < res.getPropertyCount(); i++) {
                    Object property = res.getProperty(i);
                    if (property instanceof SoapObject) {
                        SoapObject category_list = (SoapObject) property;
                        Login = category_list.getProperty("Login").toString();
                        Mdp = category_list.getProperty("Mdp").toString();
                        Id_Role = category_list.getProperty("Id_Role").toString();
                        Id_User = Integer.parseInt(category_list.getProperty("Id_User").toString());
                        Id_Soc = Integer.parseInt(category_list.getProperty("Id_Soc").toString());
                        Id_Act = Integer.parseInt(category_list.getProperty("Id_Act").toString());
                        Id_Employee = Integer.parseInt(category_list.getProperty("Id_Employee").toString());


                        LLogin.add(Login);
                        LPass.add(Mdp);
                        role.add(Id_Role);
                        LId_User.add(Id_User);
                        LId_Soc.add(Id_Soc);
                        LId_Act.add(Id_Act);
                        LIdEmp.add(Id_Employee);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return Login;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                db.deleteUsers();
                for (int i = 0; i < LLogin.size(); i++) {
                    db.insertUsers(LId_User.get(i), LLogin.get(i), LPass.get(i), LId_Act.get(i), LId_Soc.get(i),role.get(i),LIdEmp.get(i));

                }
            } else {
                Toast.makeText(MainActivity.this, "No Date To Show", Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);

        }
    }
    private class BSAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_BS);

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



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {

                androidHttpTransport.call(SOAP_ACTION_BS,envelope);
                SoapObject result= (SoapObject) envelope.getResponse();
                if (result!=null)
                {
                    ReturnResult= result.getProperty("result").toString();
                    numAtt= Integer.parseInt(result.getProperty("NBT").toString());
                }
                System.out.println(idSocBS);
                System.out.println(Id_PdtBS);
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
            return ReturnResult;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("true")) {
                for (int i = 0; i < Id_PdtBS.size(); i++) {
                    (new DetBSAsyncTask()).execute(String.valueOf(idSocMvt.get(i)),String.valueOf(idActMvt.get(i)), String.valueOf(numAtt)
                            , String.valueOf(Id_PdtBS.get(i)), String.valueOf(Qty_Mvt_Ua.get(i)));

                }
                db.updateSyncBS();

            } else {
                Toast.makeText(MainActivity.this, "No Date To Show"+s, Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);

        }
    }

    private class DetBSAsyncTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request=new SoapObject(NAMESPACE,METHOD_NAME_DetBS);

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
                androidHttpTransport.call(SOAP_ACTION_DetBS,envelope);
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

                for (int i = 0; i < LnumAtt.size(); i++) {
                    db.SyncDetMvt(LnumAtt.get(i));
                }
            }
            else
            {
                Toast.makeText(MainActivity.this,""+s,Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);
        }
    }

    private class BIAsyncTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request=new SoapObject(NAMESPACE,METHOD_NAME_BI);

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

            PropertyInfo Id_Employee=new PropertyInfo();
            Id_Employee.setName("Id_Employee");
            Id_Employee.setType(String.class);
            Id_Employee.setValue(strings[2]);
            request.addProperty(Id_Employee);

            PropertyInfo Date_Mvt=new PropertyInfo();
            Date_Mvt.setName("Date_Mvt");
            Date_Mvt.setType(String.class);
            Date_Mvt.setValue(strings[3]);
            request.addProperty(Date_Mvt);


            PropertyInfo Num_Att=new PropertyInfo();
            Num_Att.setName("Num_Att");
            Num_Att.setType(String.class);
            Num_Att.setValue(strings[4]);
            request.addProperty(Num_Att);


            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport=new HttpTransportSE(URL);
            try
            {
                androidHttpTransport.call(SOAP_ACTION_BI,envelope);
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
            if (s.equals("true")) {
                for (int i = 0; i < Id_PdtBS.size(); i++) {
                    (new DetAsyncTask()).execute(String.valueOf(idSocMvt.get(i)),String.valueOf(idActMvt.get(i)), String.valueOf(numAtt)
                            , String.valueOf( Id_PdtBS.get(i)), String.valueOf(Qty_Mvt_Ua.get(i)), String.valueOf(Maj_CostBI.get(i)));
                }
                db.updateSyncBI();

            } else {
                Toast.makeText(MainActivity.this, "No Date To Show"+s, Toast.LENGTH_SHORT).show();
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

            PropertyInfo Maj_Cost=new PropertyInfo();
            Maj_Cost.setName("Maj_Cost");
            Maj_Cost.setType(String.class);
            Maj_Cost.setValue(strings[5]);
            request.addProperty(Maj_Cost);


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
                for (int i = 0; i < LnumAtt.size(); i++) {
                    db.SyncDetMvt(LnumAtt.get(i));
                }
            }
            else
            {
                Toast.makeText(MainActivity.this,""+s,Toast.LENGTH_SHORT).show();

            }
            super.onPostExecute(s);
        }
    }
}
