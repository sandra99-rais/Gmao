


        package com.example.gmaoapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gmaoapp.Models.OrdreTravail;
import com.example.gmaoapp.Models.DetailMvt;
import com.example.gmaoapp.Models.Equipements;
import com.example.gmaoapp.Models.Mouvement;
import com.example.gmaoapp.Models.OperationOt;
import com.example.gmaoapp.Models.Usages;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="GMAO.db";
    public static final int DATABASE_VERSION=1;
    public static final String user="User";
    public static final String COL_1="idUser";
    public static final String COL_2="username";
    public static final String COL_3="password";


    public static final String OT="OT";
    public static final String idOt="idOt";
    public static final String idSoc="idSoc";
    public static final String idAct="idAct";
    public static final String numOt="NumOt";

    public static final String DateDemande="DateDemande";
    public static final String idDemandeur="idDemandeur";
    public static final String idExecuteur="idExecuteur";
    public static final String Description="Description";
    public static final String idUserSave="idUserSave";
    public static final String dateSave="dateSave";
    public static final String demandeLance="demandeLance";
    public static final String dateDB="dateDB";
    public static final String dateFin="dateFin";
    public static final String dureeTrav="dureeTrav";
    public static final String demandeCloture="demandeCloture";
    public static final String demandeApp="demandeApp";

    public static final String equipements="equipementsOT";



    public static final String typeTravaux="typeTravaux";
    public static final String idTypeTravail="idTypeTravail";
    public static final String codeTravaux="codeTravaux";
    public static final String designationTypeTravaux="designationTypeTravaux";

    public static final String clefTravaux="clefTravaux";
    public static final String idClefTravaux="idClefTravaux";
    public static final String codeClef="codeClef";
    public static final String desginationClef="desginationClef";

    public static final String degreUrgence="degreUrgence";
    public static final String idDegreUrgence="idDegreUrgence";
    public static final String desginationDegre="desginationUrgence";

    public static final String employe="employe";
    public static final String idEmploye="idEmploye";
    public static final String matriculeEmploye="matriculeEmploye";
    public static final String nomEmploye="nomEmploye";

    public static final String machine="machine";
    public static final String idMachine="idMachine";
    public static final String codeMachine="codeMachine";
    public static final String designationMachine="designationMachine";

    public static final String etatMachine="etatMachine";
    public static final String idEtat="idEtat";
    public static final String designationEtat="designationEtat";

    public static final String sousEnsemble="sousEnsemble";
    public static final String idSousEnsemble="idSousEnsemble";
    public static final String designationSousEnsemble="designationSousEnsemble";

    public static final String ensemble="ensemble";
    public static final String idEnsemble="idEnsemble";
    public static final String designationEnsemble="designationEnsemble";

    public static final String compteur="compteur";
    public static final String idCompteur="idCompteur";
    public static final String codeCompteur="codeCompteur";
    public static final String designationCompteur="designationCompteur";

    public static final String usages="usages";
    public static final String idUsage="idUsage";
    public static final String dateUsage="dateUsage";
    public static final String usageRel="usageRel";

    public static final String operationOT="operationOT";
    public static final String idOpOT="idOpOT";
    public static final String idOp="idOp";
    public static final String dateOp="dateOp";
    public static final String codeOp="codeOp";
    public static final String typeOp="typeOp";
    public static final String idGamme="idGamme";

    public static final String echeancePrev="echeancePrev";
    public static final String dateEcheance="dateEcheance";
    public static final String dureeTravEch="dureeTravEch";
    public static final String usageEc="usageEc";
    public static final String usagePrc="usagePrc";


    public static final String section="section";
    public static final String idSection="idSection";
    public static final String codeSection="codeSection";
    public static final String designationSection="designationSection";


    public static final String products="products";
    public static final String idPdt="idPdt";
    public static final String codePdt="codePdt";
    public static final String designationPdt="designationPdt";
    public static final String Qte="Qte";
    public static final String Prix="Prix";

    public static final String mouvement="mouvement";
    public static final String idMvt="idMvt";
    public static final String numMvt="numMvt";
    public static final String DateMvt="DateMvt";
    public static final String typeMvt="typeMvt";


    public static final String detailMouvement="detailMouvement";
    public static final String idDetMvt="idDetMvt";
    public static final String idPd="idPd";
    public static final String qtePdt="qtePdt";
    public static final String PU="PU";
    public static final String Total="Total";
    public static final String qteTheo="qteTheo";
    public static final String MP="MP";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+mouvement+"("+
                "idMvt integer primary key autoincrement," +
                "numMvt integer," +
                "idOt integer," +
                "DateMvt datetime," +
                "idDemandeur integer," +
                "idAct integer," +
                "idSoc integer," +
                "typeMvt text," +
                "sync boolean,"+
                "foreign key (idOt) references  OT(idOt))");
        db.execSQL("create table "+detailMouvement+"("+
                "idDetMvt integer primary key autoincrement," +
                "idMouv integer," +
                "idPd integer," +
                "qtePdt integer," +
                "PU double," +
                "Total double," +
                "qteTheo integer," +
                "MP boolean," +
                "sync boolean,"+
                "foreign key (idMouv )references  mouvement(idMvt)," +
                "foreign key (idPd) references  products(idPdt))");
        db.execSQL("create table "+operationOT+"(" +
                " idOpOT integer primary key autoincrement," +
                "idOp integer," +
                "dateOp text," +
                "codeOp text," +
                "typeOp text," +
                "codeMachine text,"+
                "codeGamme text,"+
                "idGamme integer," +
                "idSoc integer," +
                "idAct integer," +
                "idMachine integer," +
                "idOt integer," +

                "foreign key (idOt) references OT (NumOt)," +
                "foreign key (idMachine) references machine (idMachine))");
        db.execSQL("create table "+section+"(" +
                "idSection integer primary key autoincrement," +
                "codeSection text," +
                "designationSection text," +
                "idAct integer," +
                "idSoc integer)");
        db.execSQL("create table "+products+ "(" +
                "idPdt integer primary key autoincrement," +
                "codePdt text," +
                "designationPdt text," +
                "Qte integer," +
                "Prix double," +
                "codeCat text," +
                "codeSSCat text," +
                "codeUnite text," +
                "idAct integer," +
                "idSoc integer)");
        db.execSQL("create table "+echeancePrev+"(" +
                "idEcheance integer primary key autoincrement," +
                "dureeTravEch integer," +
                "idOp integer," +
                "dateEcheance text," +
                "idExecuteur integer," +
                "idTypeTravail integer," +
                "idSec integer,"+
                "idGamme integer," +
                "codeMachine text,"+
                "nomExecuteur text,"+
                "codeOperation text,"+
                "usageEc double,"+
                "usagePrc double,"+
                "codeGamme text,"+
                "typeOp integer,"+
                "idSoc integer," +
                "idAct integer," +
                "idMachine integer," +
                "idOt integer," +
                "foreign key (idOt) references OT (NumOt)," +
                "foreign key (idOp) references operationOT (idOp)," +
                "foreign key (idExecuteur) references employe (idEmploye)," +
                "foreign key (idTypeTravail) references  typeTravaux(idTypeTravail)," +
                "foreign key (idSec) references  section(idSection)," +
                "foreign key (idMachine) references machine (idMachine))");
        db.execSQL("create table "+user+"(" +
                "idUser integer primary key autoincrement," +
                "username text," +
                " password text," +
                "role text," +
                "idEmploye integer," +
                "idSoc integer," +
                "idAct integer," +
                "foreign key (idEmploye) references employe (idEmploye))");
        db.execSQL("create table "+OT+"(" +
                "idOt integer primary key autoincrement," +
                "DateDemande text, " +
                "idDemandeur integer, "+
                "idSoc integer," +
                "idAct integer," +
                "NumOt integer," +
                "idExecuteur integer," +
                "idDegreUrgence integer," +
                "idClefTravaux integer, "+
                "idTypeTravail integer, " +
                "Description text," +
                "idUserSave integer," +
                "dateSave text," +
                "dateDB text," +
                "dateFin text," +
                "dureeTrav text," +
                "demandeLance boolean," +
                "demandeCloture boolean ," +
                "demandeApp boolean,"+
                "sync boolean,"+
                "foreign key (idDemandeur) references employe (idEmploye), " +
                "foreign key (idDegreUrgence) references  degreUrgence(idDegreUrgence)," +
                "foreign key (idClefTravaux) references  clefTravaux(idClefTravaux)," +
                "foreign key (idTypeTravail) references  typeTravaux(idTypeTravail)," +
                "foreign key (idUserSave) references  User(idUser)," +
                "foreign key (idExecuteur) references employe (idEmploye))");

        db.execSQL("create table "+typeTravaux+"(" +
                "idTypeTravail integer primary key autoincrement," +
                "codeTravaux text," +
                "designationTypeTravaux text)");
        db.execSQL("create table "+clefTravaux+"(" +
                "idClefTravaux integer primary key autoincrement," +
                "codeClefTravaux text," +
                "designationClefTravaux text)");
        db.execSQL("create table "+degreUrgence+"(" +
                "idDegreUrgence integer primary key autoincrement," +
                "designationUrgence text)");
        db.execSQL("create table "+employe+"(" +
                "idEmploye integer primary key autoincrement," +
                "matriculeEmploye text," +
                "nomEmploye text)");
        db.execSQL("create table "+machine+"(" +
                "idMachine integer primary key autoincrement," +
                "codeMachine text," +
                "designationMachine text," +
                "idEtat integer," +
                "idSec integer)");
        db.execSQL("create table "+etatMachine+"(" +
                "idEtat integer primary key autoincrement," +
                "designationEtat text)");
        db.execSQL("create table "+ensemble+"(" +
                "idEnsemble integer primary key autoincrement," +
                "designationEnsemble text)");
        db.execSQL("create table "+sousEnsemble+"(" +
                "idSousEnsemble integer primary key autoincrement," +
                "designationSousEnsemble text)");
        db.execSQL("create table "+equipements+"(" +
                "idOt string," +
                "idMachine integer," +
                "idEnsemble integer," +
                "idSousEnsemble integer," +
                "idEtat integer," +
                "idSec integer,"+
                "sync boolean,"+
                "foreign key (idOt) references OT (NumOt)," +
                "foreign key (idMachine) references machine (idMachine)," +
                "foreign key (idEnsemble) references ensemble (idEnsemble)," +
                "foreign key (idSousEnsemble) references sousEnsemble (idSousEnsemble))");


        db.execSQL("create table "+compteur+"(" +
                "idCompteur integer primary key autoincrement," +
                "codeCompteur text," +
                "designationCompteur text," +
                "idSoc integer," +
                "idAct integer)");

        db.execSQL("create table "+usages+"(" +
                "idUsage integer primary key autoincrement," +
                "dateUsage text," +
                "usageRel text," +
                "idSoc integer," +
                "idAct integer," +
                "idMachine integer," +
                "idCompteur integer," +
                "sync boolean,"+
                "foreign key (idMachine) references  machine(idMachine)," +
                "foreign key (idCompteur) references  compteur(idCompteur))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+user);
        db.execSQL("drop table if exists "+OT);
        db.execSQL("drop table if exists "+typeTravaux);
        db.execSQL("drop table if exists "+degreUrgence);
        db.execSQL("drop table if exists "+clefTravaux);
        db.execSQL("drop table if exists "+machine);
        db.execSQL("drop table if exists "+ensemble);
        db.execSQL("drop table if exists "+employe);
        db.execSQL("drop table if exists "+sousEnsemble);
        db.execSQL("drop table if exists "+equipements);
        db.execSQL("drop table if exists "+etatMachine);
        db.execSQL("drop table if exists "+products);
        db.execSQL("drop table if exists "+usages);
        db.execSQL("drop table if exists "+compteur);
        db.execSQL("drop table if exists "+mouvement);
        db.execSQL("drop table if exists "+detailMouvement);
        db.execSQL("drop table if exists "+echeancePrev);
        db.execSQL("drop table if exists "+operationOT);
        db.execSQL("drop table if exists "+section);




        onCreate(db);
    }
    public boolean checkUser(String username, String password){

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " +user+ " where " +COL_2+ " =? and "+ COL_3 +" =? ",new String[] {username,password});

        int count = cursor.getCount();
        cursor.close();
        if(count>0)
            return true;
        else
            return false;

    }
    public Cursor idUserLogin(String username, String password)
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select idUser,idEmploye,role from " +user+ " where " +COL_2+ " =? and "+ COL_3 +" =? ",new String[] {username,password});
        return cursor;
    }
    public Usages UsageByid(int id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * " +
                        "from  usages,machine,compteur " +
                        "where compteur.idCompteur=usages.idCompteur and usages.idMachine=machine.idMachine and idUsage=?  "
                ,new String[] {String.valueOf(id)});
        c.moveToNext();
        Usages u=new Usages(c.getString(1),c.getInt(2),c.getInt(5),c.getInt(6));
        return u;
    }

    public boolean updateDI(OrdreTravail di, String datedb, String datefin, String dureeTr, String numOt, int lance, int cloture)
    {

        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(DateDemande,di.getDateDemande());
        cv.put(idDemandeur,di.getIdDemandeur());
        cv.put(idExecuteur,di.getIdExecuteur());
        cv.put(idClefTravaux,di.getIdPanne());
        cv.put(idTypeTravail,di.getIdTypeTrav());
        cv.put(demandeLance,lance);
        cv.put(demandeCloture,cloture);


        cv.put(dateDB,datedb);
        cv.put(dateFin,datefin);
        cv.put(dureeTrav,dureeTr);

        long res=db.update(OT,cv,"NumOt=?",new String[]{numOt});
        if(res==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public boolean updateDIApp(OrdreTravail di, String datedb, String datefin, String dureeTr, String numOt, int lance, int cloture, int app)
    {

        SQLiteDatabase db=this.getWritableDatabase();
        updateDI(di,datedb,datefin,dureeTr,numOt,lance,cloture);
        ContentValues cv=new ContentValues();

        cv.put(demandeApp,app);



        long res=db.update(OT,cv,"NumOt=?",new String[]{numOt});
        if(res==-1 )
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Cursor idUser(String nom2){
        SQLiteDatabase db=this.getReadableDatabase();
        String nom =nom2.substring(nom2.indexOf("|") + 2);
        Cursor c = db.rawQuery("select idEmploye from employe where nomEmploye=?", new String[]{nom});
        return c;
    }
    public Cursor ListeSection(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from section",null);
        return c;
    }
    public Cursor idUser2(String nom2){
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor c = db.rawQuery("select idEmploye from employe where nomEmploye=?", new String[]{nom2});
        return c;
    }
    public Cursor idDegreUrgence(String nom){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c = db.rawQuery("select idDegreUrgence from degreUrgence where designationUrgence=?", new String[]{nom});
        return c;
    }
    public Cursor idClefTravaux(String nom2){
        SQLiteDatabase db=this.getReadableDatabase();
        String nom =nom2.substring(nom2.indexOf("|") + 2);
        Cursor c = db.rawQuery("select idClefTravaux from clefTravaux where designationClefTravaux=?", new String[]{nom});
        return c;
    }
    public Cursor idTypeTravaux(String nom2){
        SQLiteDatabase db=this.getReadableDatabase();
        String nom =nom2.substring(nom2.indexOf("|") + 2);
        Cursor c = db.rawQuery("select idTypeTravail from typeTravaux where designationTypeTravaux=?", new String[]{nom});
        return c;
    }
    public Cursor idDesEquip(String nom2){
        SQLiteDatabase db=this.getReadableDatabase();
        String nom =nom2.substring(nom2.indexOf("|") + 2);
        Cursor c = db.rawQuery("select * from machine where designationMachine=?", new String[]{nom});
        return c;
    }
    public Cursor idDesSection(String nom2){
        SQLiteDatabase db=this.getReadableDatabase();
        String nom =nom2.substring(nom2.indexOf("|") + 2);
        Cursor c = db.rawQuery("select idSection from section where designationSection=?", new String[]{nom});
        return c;
    }
    public Cursor idDesEquip2(String nom2){
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor c = db.rawQuery("select idMachine,idEtat,idSec from machine where designationMachine=?", new String[]{nom2});
        return c;
    }
    public Cursor idDesCompteur(String nom){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c = db.rawQuery("select idCompteur from compteur where designationCompteur=?", new String[]{nom});
        return c;
    }
    public Cursor idEns(String nom){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c = db.rawQuery("select idEnsemble from ensemble where designationEnsemble=?", new String[]{nom});
        return c;
    }
    public Cursor idSSEns(String nom){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c = db.rawQuery("select idSousEnsemble from sousEnsemble where designationSousEnsemble=?", new String[]{nom});
        return c;
    }

    public Cursor cursor(String nom){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c = db.rawQuery("select idUser,idSoc,idAct from User where idUser=?", new String[]{nom});
        return c;
    }
    public Cursor idOt(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c = db.rawQuery("select idOt from OT order by idOt desc limit 1",null);
        return c;
    }
    public Cursor idUsage(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c = db.rawQuery("select idUsage from usages order by idUsage desc limit 1",null);
        return c;
    }
    @SuppressLint("Range")
    public Boolean insertUsage(Usages u, String name){
        SQLiteDatabase db = this.getWritableDatabase();

        int idSo=0, idAc=0;

        Cursor c19 = cursor(name);
        if(c19.moveToNext()){
            idSo =  c19.getInt(c19.getColumnIndex("idSoc"));
            idAc =  c19.getInt(c19.getColumnIndex("idAct"));
        }
        ContentValues usage=new ContentValues();
        usage.put(dateUsage,u.getDateUsage());
        usage.put(idMachine,u.getIdMachine());
        usage.put(idSoc,idSo);
        usage.put(idAct,idAc);
        usage.put(idCompteur,u.getIdCompteur());
        usage.put(usageRel,u.getUsageRel());
        usage.put("sync",0);

        long res= db.insert(usages,null,usage);

        if(res==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }




    public Cursor etatOT(String numOt){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select demandeLance, demandeCloture, demandeApp from OT where NumOt=?",new String[] {numOt});
        return c;
    }

    public Cursor etatLOT(String numOt){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select demandeLance from OT where demandeLance=0 and DateDemande<DATE() and NumOt=?",new String[] {numOt});
        return c;
    }
    public boolean updateUsage(Usages u,String id)
    {

        SQLiteDatabase db = this.getWritableDatabase();



        ContentValues usage=new ContentValues();
        usage.put(dateUsage,u.getDateUsage());
        usage.put(idMachine,u.getIdMachine());
        usage.put(idCompteur,u.getIdCompteur());
        usage.put(usageRel,u.getUsageRel());

        long res= db.update(usages,usage,"idUsage=?",new String[] {id});

        if(res==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    @SuppressLint("Range")
    public Boolean insertDI(OrdreTravail di, String name){
        SQLiteDatabase db=this.getWritableDatabase();
        int num=1;
        int     idSo=0, idAc=0, idOtc=0;





        Cursor c10 = cursor(name);
        if(c10.moveToNext()){
            idSo = c10.getInt(c10.getColumnIndex("idSoc"));
            idAc = c10.getInt(c10.getColumnIndex("idAct"));
        }
        Cursor c11 = idOt();
        if(c11.moveToNext()){
            idOtc = c11.getInt(0);
        }
        ContentValues demande=new ContentValues();
        demande.put(DateDemande,di.getDateDemande());
        demande.put(idDemandeur,di.getIdDemandeur());
        demande.put(idSoc,idSo);
        demande.put(idAct,idAc);
        demande.put(numOt,idOtc+1);
        demande.put(idExecuteur,di.getIdExecuteur());
        demande.put(idDegreUrgence,di.getIdDegreUrgence());
        demande.put(idClefTravaux,di.getIdPanne());
        demande.put(idTypeTravail,di.getIdTypeTrav());
        demande.put(Description,di.getDesc());
        demande.put(idUserSave,name);
        demande.put(dateSave,new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        demande.put(demandeLance,1);
        demande.put(demandeCloture,0);
        demande.put(demandeApp,0);
        demande.put("sync",0);
        long res= db.insert(OT,null,demande);



        if(res==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    //**************************Update sync********************************//
    public Boolean updateSync() {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues sync=new ContentValues();
        sync.put("sync",1);



        long res= db.update(OT,sync,null,null);

        if(res==-1)
        {
            return false;
        }
        else
        {
            return true;
        }

    }
    //**************************Update sync bon sortie********************************//
    public Boolean updateSyncBS() {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues sync=new ContentValues();
        sync.put("sync",1);



        long res= db.update(mouvement,sync,"typeMvt='BS'",null);

        //long res1=db.update(detailMouvement,sync,null,null);
        //long res2= db.update(detailMouvement,sync," mouvement.numMvt=detailMouvement.idMouv and mouvement.typeMvt='BS'",null);


        if(res==-1)
        {
            return false;
        }
        else
        {
            return true;
        }

    }
    //**************************Update sync Releve********************************//
    public Boolean updateSyncReleve() {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues sync=new ContentValues();
        sync.put("sync",1);

        long res= db.update(usages,sync,null,null);

        if(res==-1)
        {
            return false;
        }
        else
        {
            return true;
        }

    }
    public void SyncDetMvt(int id)
    {           SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("update detailMouvement set sync=1 where idMouv=?",new String[]{String.valueOf(id)});

    }
    public Boolean updateSyncBI() {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues sync=new ContentValues();
        sync.put("sync",1);


        long res= db.update(mouvement,sync,"typeMvt='BI'",null);

        if(res==-1)
        {
            return false;
        }
        else
        {
            return true;
        }

    }
    //--------------------statistic par statut----------------
    public Cursor countDMLStatut(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select count(demandeLance) from OT where demandeLance=1 and demandeCloture=0", null);
        return c;
    }
    public Cursor countDMCStatut(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select count(demandeCloture)from OT where demandeCloture=1 and demandeApp=0", null);
        return c;
    }
    public Cursor countDMStatut(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select count(idOt) from OT", null);
        return c;
    }
    public Cursor countDMRStatut(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select count(*)from OT where demandeLance=0 and DateDemande<DATE()", null);
        return c;
    }
    public Cursor countDMAStatut(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select count(demandeApp) from OT where demandeApp=1", null);
        return c;
    }
    public Cursor nbPrevRetard(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select count(*) from echeancePrev where dateEcheance<DATE()", null);
        return c;
    }
    public Cursor nbPrev(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select count(*) from echeancePrev", null);
        return c;
    }
    public Cursor equipArret(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select count(*) from Machine where idEtat=1", null);
        return c;
    }
    public Cursor equip(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select count(*) from Machine", null);
        return c;
    }
    //--------------------statistic par type travaux----------------
    public Cursor countDMTypeTravaux(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select count(idTypeTravail) from OT where idTypeTravail=?", new String[] {String.valueOf(id)});
        return c;
    }
    public Cursor countDMAllTypeTravaux(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select idTypeTravail,designationTypeTravaux from typeTravaux ", null);
        return c;
    }


    public Cursor ListeDemande(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select DateDemande,numOt,designationTypeTravaux,designationMachine,Description,NBTAttribue,nomEmploye  from OT,machine,typeTravaux,equipementsOT,employe " +
                "where typeTravaux.idTypeTravail=OT.idTypeTravail and OT.NumOt=equipementsOT.idOt and equipementsOT.idMachine=machine.idMachine and OT.idDemandeur=employe.idEmploye ",null);
        return c;
    }
    public Cursor ListeDemandeParId(String id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select DateDemande,numOt,designationTypeTravaux,designationMachine,Description,NBTAttribue,nomEmploye  from OT,machine,typeTravaux,equipementsOT,employe " +
                "where typeTravaux.idTypeTravail=OT.idTypeTravail and OT.NumOt=equipementsOT.idOt and equipementsOT.idMachine=machine.idMachine and OT.idDemandeur=employe.idEmploye and OT.NumOt like ?",new String[]{String.valueOf(id)});
        return c;
    }
    public Cursor OtNonSync()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from OT ,employe em where OT.sync=0 and OT.idDemandeur=em.idEmploye",null);
        return c;
    }
    public Cursor EqpNonSync()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from equipementsOT where sync=0",null);
        return c;
    }
    public Cursor ReleveNonSync()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from usages where sync=0",null);
        return c;
    }
    public Cursor BSNonSync()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from mouvement m,employe e where m.sync=0  and typeMvt='BS' and e.idEmploye=m.idDemandeur ",null);
        return c;
    }
    public Cursor BINonSync()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from mouvement m,employe e where m.sync=0  and typeMvt='BI' and e.idEmploye=m.idDemandeur",null);
        return c;
    }  public Cursor MvtNonSync()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from detailMouvement  where sync=0  ",null);
        return c;
    }
    public Cursor ListeDemandeParTech(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select DateDemande,numOt,designationTypeTravaux,designationMachine,Description,NBTAttribue,nomEmploye  from OT,machine,typeTravaux,equipementsOT,employe  " +
                "where typeTravaux.idTypeTravail=OT.idTypeTravail and OT.NumOt=equipementsOT.idOt and equipementsOT.idMachine=machine.idMachine and idDemandeur=idEmploye  and idExecuteur=?",new String[]{String.valueOf(id)});
        return c;
    }
    public Cursor ListeDemandeParTechId(int id, String numOt){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select DateDemande,numOt,designationTypeTravaux,designationMachine,Description,NBTAttribue,nomEmploye  from OT,machine,typeTravaux,equipementsOT,employe  " +
                "where typeTravaux.idTypeTravail=OT.idTypeTravail and OT.NumOt=equipementsOT.idOt and equipementsOT.idMachine=machine.idMachine and idDemandeur=idEmploye and idExecuteur=? and numOt like ?",new String[]{String.valueOf(id), String.valueOf(numOt)});
        return c;
    }
    public Cursor ListeUsage()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select designationMachine,designationCompteur,usageRel,dateUsage,idUsage " +
                "from  usages,machine,compteur " +
                "where compteur.idCompteur=usages.idCompteur and usages.idMachine=machine.idMachine  ",null);

        return c;
    }
    public Cursor ListeDegreUrgence(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from degreUrgence",null);
        return c;
    }
    public OrdreTravail DemandeByid(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select *  from OT,machine,typeTravaux,equipementsOT " +
                "where typeTravaux.idTypeTravail=OT.idTypeTravail and OT.NumOt=equipementsOT.idOt and equipementsOT.idMachine=machine.idMachine and NumOt=? ",new String[]{String.valueOf(id)});
        c.moveToNext();
        @SuppressLint("Range") OrdreTravail di=new OrdreTravail(c.getInt(6),c.getString(1),c.getInt(2),c.getInt(5),c.getInt(8),c.getInt(9),c.getInt(c.getColumnIndex("demandeCloture")),c.getInt(c.getColumnIndex("demandeLance")));
        return di;
    }



    public Cursor ListeEmploye(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from employe ",null);
        return c;
    }
    public Cursor ListeTravaux(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from typeTravaux",null);
        return c;
    }

    public Cursor ListePanne(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from clefTravaux",null);
        return c;
    }
    public Cursor ListeMachine(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from machine",null);
        return c;
    }
    public Cursor ListeEnsemble(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from ensemble",null);
        return c;
    }
    public Cursor ListeSousEnsemble(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from sousEnsemble",null);
        return c;
    }
    public Cursor ListeEtatMachine(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from etatMachine",null);
        return c;
    }
    public Cursor ListeCompteur(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from compteur",null);
        return c;
    }
    public Cursor NomEmploye(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from employe where idEmploye=?", new String[]{String.valueOf(id)});
        return c;
    }
    public Cursor NomMachine(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c = db.rawQuery("select designationMachine from machine where idMachine=?", new String[]{String.valueOf(id)});
        return c;
    }
    public Cursor NomCompteur(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c = db.rawQuery("select designationCompteur from compteur where idCompteur=?", new String[]{String.valueOf(id)});
        return c;
    }
    public Cursor typePanne(int id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from ClefTravaux where idClefTravaux=?", new String[]{String.valueOf(id)});
        return c;
    }
    public Cursor trav(int id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from typeTravaux where idTypeTravail=?", new String[]{String.valueOf(id)});
        return c;
    }
    //*********************************Debut Filtrage Usage**********************************************************//
    public Cursor filtrage(String date1,String date2)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select designationMachine,designationCompteur,usageRel,dateUsage,idUsage " +
                "from  usages,machine,compteur " +
                "where compteur.idCompteur=usages.idCompteur and usages.idMachine=machine.idMachine " +
                "and dateUsage between ? and ? " +
                "order by dateUsage asc",new String[] {date1,date2});

        return c;
    }
    //*********************************Fin Filtrage Usage**********************************************************//


    //*********************************Debut Filtrage Echeance Par Date**********************************************************//
    public Cursor filtrageEchDate(String date1,String date2,int equip)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select idEcheance, dateEcheance, codeMachine, codeOperation, nomExecuteur, codeGamme, dureeTravEch, idOt " +
                "from  echeancePrev " +
                "where idMachine = ? and dateEcheance between ? and ? and typeOp=0 " +
                "order by dateEcheance asc",new String[] {String.valueOf(equip),date1,date2});

        return c;
    }
    public Cursor filtrageEchDateWithSection(String date1,String date2,int sec)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select idEcheance, dateEcheance, codeMachine, codeOperation, nomExecuteur, codeGamme, dureeTravEch, idOt " +
                "from  echeancePrev " +
                "where idSec = ? and dateEcheance between ? and ? and typeOp=0 " +
                "order by dateEcheance asc",new String[] {String.valueOf(sec),date1,date2});

        return c;
    }
    public Cursor filtrageAllEchDate(String date1,String date2,int equip,int sec)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select idEcheance, dateEcheance, codeMachine, codeOperation, nomExecuteur, codeGamme, dureeTravEch, idOt " +
                "from  echeancePrev " +
                "where idMachine = ? and idSec=? and dateEcheance between ? and ? and typeOp=0 " +
                "order by dateEcheance asc",new String[] {String.valueOf(equip),String.valueOf(sec),date1,date2});

        return c;
    }
    public Cursor filtrageEchDateWithoutEquip(String date1,String date2)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select idEcheance, dateEcheance, codeMachine, codeOperation, nomExecuteur, codeGamme, dureeTravEch, idOt " +
                "from  echeancePrev " +
                "where  dateEcheance between ? and ? and typeOp=0 " +
                "order by dateEcheance asc",new String[] {date1,date2});

        return c;
    }
    //*********************************Fin Filtrage Echeance Par Date**********************************************************//


    //*********************************Debut Filtrage Echeance Par Usage**********************************************************//
    public Cursor filtrageEchUsage(String date1,String date2,int equip)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select idEcheance, dateEcheance, codeMachine, codeOperation, nomExecuteur, codeGamme, dureeTravEch, usageEc,usagePrc, idOt " +
                "from  echeancePrev " +
                "where idMachine = ? and dateEcheance between ? and ? and typeOp=1 " +
                "order by dateEcheance asc",new String[] {String.valueOf(equip),date1,date2});

        return c;
    }
    public Cursor filtrageEchUsageWithoutEquip(String date1,String date2)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select idEcheance, dateEcheance, codeMachine, codeOperation, nomExecuteur, codeGamme, dureeTravEch, usageEc,usagePrc, idOt " +
                "from  echeancePrev " +
                "where  dateEcheance between ? and ? and typeOp=1 " +
                "order by dateEcheance asc",new String[] {date1,date2});

        return c;
    }
    public Cursor filtrageEchUsageWithSection(String date1,String date2,int sec)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select idEcheance, dateEcheance, codeMachine, codeOperation, nomExecuteur, codeGamme, dureeTravEch, usageEc,usagePrc, idOt " +
                "from  echeancePrev " +
                "where idSec=? and dateEcheance between ? and ? and typeOp=1 " +
                "order by dateEcheance asc",new String[] {String.valueOf(sec),date1,date2});

        return c;
    }
    public Cursor filtrageAllEchUsage(String date1,String date2,int equip,int sec)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select idEcheance, dateEcheance, codeMachine, codeOperation, nomExecuteur, codeGamme, dureeTravEch, usageEc,usagePrc, idOt " +
                "from  echeancePrev " +
                "where idMachine = ? and idSec=? and dateEcheance between ? and ? and typeOp=1 " +
                "order by dateEcheance asc",new String[] {String.valueOf(equip),String.valueOf(sec),date1,date2});

        return c;
    }
    //*********************************Fin Filtrage Echeance Par Date**********************************************************//

    //*******************************Liste Echeance Par Date****************************************************************//
    public Cursor ListeEcheanceDate()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select idEcheance, dateEcheance, codeMachine, codeOperation, nomExecuteur, codeGamme, dureeTravEch, idOt " +
                "from  echeancePrev where typeOp=0",null);
        return c;

    }
    //******************************************************************************************************************//

    //*********************************ListeEcheance Par Usage**********************************************************//
    public Cursor ListeEcheanceUsage()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select idEcheance, dateEcheance, codeMachine, codeOperation, nomExecuteur, codeGamme, dureeTravEch, usageEc,usagePrc,idOt " +
                "from  echeancePrev where typeOp=1",null);
        return c;

    }
    //*****************************************************************************************************************//

    public Cursor AllIdEchDate(int id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select idTypeTravail,idMachine,idGamme,idOp,typeOp,idSec " +
                "from  echeancePrev where idEcheance=? and typeOp=0",new String[] {String.valueOf(id)});
        return c;

    }
    public Cursor AllIdEchUsage(int id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select idTypeTravail,idMachine,idGamme,idOp,typeOp,idSec " +
                "from  echeancePrev where idEcheance=? and typeOp=1",new String[] {String.valueOf(id)});
        return c;

    }

    @SuppressLint("Range")
    public boolean updateEch(int id, OrdreTravail di, String name, Equipements e, OperationOt op)
    {
        int idDem=0,  idDegUrg=0, idCT=0, idTT=0, idUtilSave=0, idSo=0, idAc=0, idOtc=0;

        SQLiteDatabase db = this.getWritableDatabase();


        Cursor c11 = idOt();
        if(c11.moveToNext()){
            idOtc = c11.getInt(0);
        }

        Cursor c10 = cursor(name);
        if(c10.moveToNext()){
            idDem = c10.getInt(c10.getColumnIndex("idUser"));
            idUtilSave = c10.getInt(c10.getColumnIndex("idUser"));
            idSo =  c10.getInt(c10.getColumnIndex("idSoc"));
            idAc =  c10.getInt(c10.getColumnIndex("idAct"));
        }


        ContentValues ot=new ContentValues();
        ot.put(idDemandeur,idDem);
        ot.put(numOt,idOtc+1);
        ot.put(DateDemande,di.getDateDemande());
        ot.put(idSoc,idSo);
        ot.put(idAct,idAc);
        ot.put(idDegreUrgence,di.getIdDegreUrgence());
        ot.put(idUserSave,idUtilSave);
        ot.put(idExecuteur,di.getIdExecuteur());
        ot.put(dateSave,new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        ot.put(demandeLance,0);
        ot.put(demandeCloture,0);
        ot.put(demandeApp,0);
        ot.put(idTypeTravail,di.getIdTypeTrav());
        ot.put(Description,"");
        ot.put(idClefTravaux,1);

        long res1= db.insert(OT,null,ot);
        ContentValues operation=new ContentValues();

        operation.put(idSoc,idSo);
        operation.put(idAct,idAc);
        operation.put(idOt,idOtc+1);
        operation.put(idOp,op.getIdOp());
        operation.put(dateOp,op.getDateOp());
        operation.put(idGamme,op.getIdGamme());
        operation.put(idMachine,op.getIdMachine());
        operation.put(codeOp,op.getCodeOp());
        operation.put(codeMachine,op.getCodeMachine());
        operation.put("codeGamme",op.getCodeGamme());
        operation.put(typeOp,op.getTypeOp());





        long res3= db.insert(operationOT,null,operation);


        ContentValues equipement=new ContentValues();
        equipement.put(idOt,idOtc+1);
        equipement.put(idMachine,e.getIdMachine());
        equipement.put(idEtat,1);
        equipement.put("idSec",e.getIdSec());



        long res2= db.insert(equipements,null,equipement);


        ContentValues ech=new ContentValues();
        ech.put(idOt,idOtc+1);



        long res= db.update(echeancePrev,ech,"idEcheance=?",new String[] {String.valueOf(id)});



        if( res1==-1 || res==-1 ||res2==-1 || res3==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public Cursor idOtById(int id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select idOt from "+echeancePrev+" where idEcheance=?",new String[] {String.valueOf(id)});
        return c;
    }
    //***************************Gestion de stock ******************************************
    public Cursor affichePdt() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select designationPdt  from products ", null);
        return c;
    }
    public Cursor idPdt(String nom)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select idPdt,codePdt  from products where designationPdt=?", new String[] {nom});
        return c;
    }
    public Cursor afficheQtePU(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select Qte,Prix  from products where  idPdt=?  ", new String[] {String.valueOf(id)});
        return c;
    }
    public Cursor idBS()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select idMvt  from mouvement order by idMvt desc limit 1", null);
        return c;
    }
    public Cursor listeIdOt() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select numOt  from OT", null);
        return c;
    }

    public Boolean insertMvt(Mouvement m, String natt)
    {        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues mvt=new ContentValues();

        mvt.put(idSoc,1);
        mvt.put(idAct,1);
        mvt.put("numMvt",m.getNumMvt());
        mvt.put("idOt",m.getIdOt());
        mvt.put("DateMvt",m.getDateMvt());
        mvt.put("idDemandeur",m.getIdDemandeur());
        mvt.put("typeMvt",m.getTypeMvt());
        mvt.put("sync",0);
        mvt.put("numAtt",natt);
        long res= db.insert("mouvement",null,mvt);
        if(res==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public Boolean insertDetMvt( DetailMvt dm,int idS,int idA)
    {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues dmvt=new ContentValues();

        dmvt.put("idMouv" ,dm.getIdMvt());
        dmvt.put("idPd",dm.getIdPdt());
        dmvt.put("qtePdt",dm.getQteMvt());
        dmvt.put("qteTheo",dm.getQteTheo());
        dmvt.put("Total",dm.getTotal());
        dmvt.put("PU",dm.getPU());
        dmvt.put("sync",0);
        dmvt.put("idSoc",idS);
        dmvt.put("idAct",idA);
        long res1= db.insert("detailMouvement",null,dmvt);




        if(res1==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public Cursor IdDEMANDEUR(String nom)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select *  from employe where nomEmploye=?", new String[] {nom});
        return c;
    }
    public Cursor ListeBonSortie()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.rawQuery("select idMvt,nomEmploye,idOt,designationPdt,qtePdt,Total,numAtt   from  mouvement m,employe e,products p,detailMouvement dm " +
                "where m.idDemandeur=e.idEmploye and m.numMvt=dm.idMouv and dm.idPd=p.idPdt and m.typeMvt='BS'",null);
        return c;
    }
    public Cursor ListeBonInventaire()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.rawQuery("select idMvt,nomEmploye,idOt,designationPdt,qtePdt,Total,PU,qteTheo,numAtt   from  mouvement m,employe e,products p,detailMouvement dm " +
                "where m.idDemandeur=e.idEmploye and m.numMvt=dm.idMouv and dm.idPd=p.idPdt and m.typeMvt='BI'",null);
        return c;
    }
    public Cursor TotalBS()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.rawQuery("select sum(Total)  from  detailMouvement dm, mouvement m where dm.idMouv=m.numMvt and m.typeMvt='BS' ",null);
        return c;
    }
    public Cursor TotalBI()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.rawQuery("select sum(Total)  from  detailMouvement dm, mouvement m where dm.idMouv=m.numMvt and m.typeMvt='BI' ",null);
        return c;
    }
    public Cursor ListeEquipementsDI(int id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from equipementsOT " +
                "where idOt=? ",new String[]{String.valueOf(id)});
        return c;


    }
    public Cursor DesEnsemble(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from ensemble where idEnsemble=?",new String[] {String.valueOf(id)});
        return c;
    }
    public Cursor DesMachine(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select designationMachine from machine where idMachine=?",new String[] {String.valueOf(id)});
        return c;
    }
    public Boolean insertEquipement(Equipements e)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        int idOtc=0;

        Cursor c11 = idOt();
        if(c11.moveToNext()){
            idOtc = c11.getInt(0);
        }
        ContentValues equipement=new ContentValues();
        equipement.put(idOt,idOtc+1);
        equipement.put(idMachine,e.getIdMachine());
        equipement.put(idEnsemble,e.getIdEns());
        equipement.put(idSousEnsemble,e.getIdSsEns());
        equipement.put(idEtat,e.getIdEtat());
        equipement.put("idSec",e.getIdSec());
        equipement.put("sync",0);


        long res2= db.insert(equipements,null,equipement);
        if(res2==-1 )
        {
            return false;
        }
        else
        {
            return true;
        }

    }

    public Cursor EtatMachine(int id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select designationEtat from etatMachine" +
                " where  idEtat=? ",new String[] {String.valueOf(id)});
        return c;
    }
    public Cursor DesSousEnsemble(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from SousEnsemble where idSousEnsemble=?",new String[] {String.valueOf(id)});
        return c;
    }
    //********************Concultation des Article************************************************//
    public Cursor ListeArticle()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from "+products
                ,null);
        return c;
    }
    //*********************Répartition par panne***********************************************//

    public Cursor countTypePanne(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select count(idClefTravaux) from OT where idClefTravaux=? and demandeApp=1", new String[] {String.valueOf(id)});
        return c;
    }
    public Cursor AlltypePanne(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select idClefTravaux,designationClefTravaux from clefTravaux ", null);
        return c;
    }


    //--------------------statistic par Section----------------
    public Cursor sectionAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select idSection, codeSection, designationSection from section", null);
        return c;
    }
    public Cursor section(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select count(e.idSec) from OT , equipementsOT as e where OT.NumOt=e.idOT and OT.DateDemande>=dateDB and OT.DateDemande<=dateFin and OT.demandeApp=1 and idSec=?", new String[]{String.valueOf(id)});
        return c;
    }

    //-----------------------------------------------------------------------
    public Boolean insertDetMvtBI( DetailMvt dm,int idA,int idS)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues dmvt=new ContentValues();

        dmvt.put("idMouv" ,dm.getIdMvt());
        dmvt.put("idPd",dm.getIdPdt());
        dmvt.put("qtePdt",dm.getQteMvt());
        dmvt.put("qteTheo",dm.getQteTheo());
        dmvt.put("MP",dm.getMP());
        dmvt.put("PU",dm.getPU());
        dmvt.put("sync",0);
        dmvt.put("idAct",idA);
        dmvt.put("idSoc",idS);
        long res1= db.insert("detailMouvement",null,dmvt);

        if(res1==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    //***************************Sync****************************************************************************************//
    //SyncMachine//
    public void deleteMachine()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + machine);
        db.close();
    }
    public long insertMachine(int id,String des,String code,int idSec,int idEtat)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues m=new ContentValues();

        m.put(idMachine,id);
        m.put(designationMachine,des);
        m.put(codeMachine,code);
        m.put("idEtat",idEtat);
        m.put("idSec",idSec);

        long res1= db.insert(machine,null,m);

        return res1;
    }
    //SyncProducts//
    public void deleteProducts()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + products);
        db.close();
    }
    public long insertProducts(int id,String code,String desc,double qte,double prix,int idActPdt,int idSocPdt,int idCat,int idSSCat,int idUnite)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues m=new ContentValues();

        m.put(idPdt,id);
        m.put(codePdt,code);
        m.put(designationPdt,desc);
        m.put(Qte,qte);
        m.put(Prix,prix);
        m.put(idAct,idActPdt);
        m.put(idSoc,idSocPdt);
        m.put("codeCat",idCat);
        m.put("codeSSCat",idSSCat);
        m.put("codeUnite",idUnite);

        long res1= db.insert(products,null,m);

        return res1;
    }
    //SyncListeMvt//
    public void deleteListeMvt()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + mouvement);
        db.close();
    }
    //SyncReleve//
    public void deleteReleve()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + usages + " where sync=1");
        db.close();
    }
    //insert releve from sql server//
    public long insertReleve(int soc,int act,int machine,int compteur,int usage,String date)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues m=new ContentValues();

        m.put(idSoc,soc);
        m.put(idAct,act);
        m.put(idMachine,machine);
        m.put(idCompteur,compteur);
        m.put(usageRel,usage);
        m.put(dateUsage,date);
        m.put("sync",1);

        long res1= db.insert(usages,null,m);

        return res1;
    }
    public long insertListeMvt(int num,String date,int idA,int idS,int idEmp, String typeM,String numAtt,int idOt)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues m=new ContentValues();

        m.put(numMvt,num);
        m.put(DateMvt,date);
        m.put(idAct,idA);
        m.put(idSoc,idS);
        m.put(idDemandeur,idEmp);
        m.put(typeMvt,typeM);
        m.put("sync",1);
        m.put("numAtt",numAtt);
        m.put("idOt",idOt);

        long res1= db.insert(mouvement,null,m);

        return res1;
    }
    //SyncListeDetailMvt//
    public void deleteDetailListeMvt()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + detailMouvement);
        db.close();
    }
    public long insertDetailListeMvt(int idMouv,int idPdt,double prixU,double qteDisp,double total, double qteMvt, boolean majCost,int idS,int idA)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues m=new ContentValues();

        m.put("idMouv",idMouv);
        m.put(idPd,idPdt);
        m.put(PU,prixU);
        m.put(qteTheo,qteDisp);
        m.put(Total,total);
        m.put(qtePdt,qteMvt);
        m.put(MP,majCost);
        m.put("sync",1);
        m.put("idSoc",idS);
        m.put("idAct",idA);

        long res1= db.insert(detailMouvement,null,m);

        return res1;
    }
    //SyncEmploye//
    public void deleteTT()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + typeTravaux);
        db.close();
    }
    public long insertTT(int id,String code,String des)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues tt=new ContentValues();

        tt.put(idTypeTravail,id);
        tt.put(codeTravaux,code);
        tt.put(designationTypeTravaux,des);


        long res1= db.insert(typeTravaux,null,tt);

        return res1;
    }    //SyncEmploye//
    public void deleteEmploye()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + employe);
        db.close();
    }
    public long insertEmploye(int id,String des,String code)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues e=new ContentValues();

        e.put(idEmploye,id);
        e.put(matriculeEmploye,code);
        e.put(nomEmploye,des);


        long res1= db.insert(employe,null,e);

        return res1;
    }
    //SyncDU//
    public void deleteDU()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + degreUrgence);
        db.close();
    }
    public long insertDU(int id,String des)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues e=new ContentValues();

        e.put(idDegreUrgence,id);
        e.put("designationUrgence",des);


        long res1= db.insert(degreUrgence,null,e);

        return res1;
    }
    //Panne//
    public void deletePanne()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + clefTravaux);
        db.close();
    }
    public long insertPanne(int id,String des,String code)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues e=new ContentValues();

        e.put("idClefTravaux",id);
        e.put("designationClefTravaux",des);
        e.put("codeClefTravaux",code);


        long res1= db.insert(clefTravaux,null,e);

        return res1;
    }
    //Etat//
    public void deleteEtat()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + etatMachine);
        db.close();
    }
    public long insertEtat(int id,String des)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues e=new ContentValues();

        e.put(idEtat,id);
        e.put(designationEtat,des);


        long res1= db.insert(etatMachine,null,e);

        return res1;
    }
    //Section//
    public void deleteSection()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + section);
        db.close();
    }
    public long insertSection(int id,String des,String code,int idAct,int idSoc)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues s=new ContentValues();

        s.put(idSection,id);
        s.put(designationSection,des);
        s.put(codeSection,code);
        s.put("idAct",idAct);
        s.put("idSoc",idSoc);


        long res1= db.insert(section,null,s);

        return res1;
    }
    //Compteur//
    public void deleteCompteur()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + compteur);
        db.close();
    }
    public long insertCompteur(int id,String des,String code,int idAct,int idSoc)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues s=new ContentValues();

        s.put(idCompteur,id);
        s.put(designationCompteur,des);
        s.put(codeCompteur,code);
        s.put("idAct",idAct);
        s.put("idSoc",idSoc);


        long res1= db.insert(compteur,null,s);

        return res1;
    }
    //users//
    public void deleteUsers()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + user);
        db.close();
    }
    public long insertUsers(int id,String username,String pass,int idAct,int idSoc,String role,int emp)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues u=new ContentValues();

        u.put("idUser",id);
        u.put("username",username);
        u.put("password",pass);
        u.put("idAct",idAct);
        u.put("idSoc",idSoc);
        u.put("role",role);
        u.put("idEmploye",emp);


        long res1= db.insert(user,null,u);

        return res1;
    }
    //ensemble//
    public void deleteEnsemble()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + ensemble);
        db.close();
    }
    public long insertEnsemble(int id,String code)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues e=new ContentValues();

        e.put(idEnsemble,id);
        e.put(designationEnsemble,code);



        long res1= db.insert(ensemble,null,e);

        return res1;
    }
    //SousEnsemble//
    public void deleteSousEnsemble()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + sousEnsemble);
        db.close();
    }
    public long insertSousEnsemble(int id,String code)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues e=new ContentValues();

        e.put(idSousEnsemble,id);
        e.put(designationSousEnsemble,code);



        long res1= db.insert(sousEnsemble,null,e);

        return res1;
    }

    public Boolean updateEqpSync(int id) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues sync=new ContentValues();
        sync.put("sync",1);

        long res= db.update(equipements,sync,"idOt=?",new String[] {String.valueOf(id)});



        if(res==-1 )
        {
            return false;
        }
        else
        {
            return true;
        }

    }
    //OT//
    public void deleteOT()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + OT +" where sync=1");
        db.close();
    } public void deleteEqp()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + equipements +" where sync=1");
        db.execSQL("DELETE FROM " + equipements +" where sync=1");
        db.close();
    }
    public long insertOT(String Date,int nomDm,int idSo,int idAc
            ,int NOT,int idEx,int DU,int CT,int TT
            ,String desc,int idUser,String DateD
            ,String DateF,int DT,String Lance,String cloture
            ,boolean App,String NBTAtt,String TravEFF,String dateR)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ot=new ContentValues();
        ot.put(DateDemande,Date);
        ot.put(dateSave,dateR);
        ot.put(idDemandeur,nomDm);
        ot.put(idSoc,idSo);
        ot.put(idAct,idAc);
        ot.put(numOt,NOT);
        ot.put(idExecuteur,idEx);
        ot.put(idDegreUrgence,DU);
        ot.put(idClefTravaux,CT);
        ot.put(idTypeTravail,TT);
        ot.put(Description,desc);
        ot.put(idUserSave,idUser);
        ot.put(dateDB,DateD);
        ot.put(dateFin,DateF);
        ot.put(dureeTrav,DT);
        ot.put(demandeLance,Lance);
        ot.put(demandeCloture,cloture);
        ot.put(demandeApp,App);
        ot.put("NBTAttribue",NBTAtt);
        ot.put("travEff",TravEFF);
        ot.put("sync",1);



        long res1= db.insert(OT,null,ot);

        return res1;


    }
    public long insertEuipementsOT(int idOT,int idM,int idEns,int idSS,int idE,int idS)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues e = new ContentValues();
        e.put("idOt", idOT);
        e.put("idMachine", idM);
        e.put("idEnsemble", idEns);
        e.put("idSousEnsemble", idSS);
        e.put("idEtat", idE);
        e.put("idSec", idS);
        e.put("sync", 1);

        long res1 = db.insert(equipements, null, e);

        return res1;
    }































}
