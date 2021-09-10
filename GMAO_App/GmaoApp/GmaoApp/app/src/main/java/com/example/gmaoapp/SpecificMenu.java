package com.example.gmaoapp;

import android.view.Menu;

import com.google.android.material.navigation.NavigationView;

public class SpecificMenu {
    Menu menu;
    public void afficheMenu(String var, NavigationView navigationView)
    {

        if(var.equals("Technicien"))
        {
            menu = navigationView.getMenu();
            menu.findItem(R.id.menu_home).setVisible(false);
            menu.findItem(R.id.consultation).setVisible(false);
            menu.findItem(R.id.menu_listeEchUsage).setVisible(false);
            menu.findItem(R.id.menu_ListeBI).setVisible(false);
            menu.findItem(R.id.menu_BonInventaire).setVisible(false);
            menu.findItem(R.id.menu_ReleveUsages).setVisible(false);
            menu.findItem(R.id.menu_listeEchDate).setVisible(false);
            menu.findItem(R.id.menu_ListeDM).setVisible(false);
            menu.findItem(R.id.menu_DMInt).setVisible(false);
            menu.findItem(R.id.analyseSec).setVisible(false);
            menu.findItem(R.id.analyse).setVisible(false);
            menu.findItem(R.id.menu_ListeDemParTech).setVisible(true);

        }
        if(var.equals("Responsable service"))
        {
            menu = navigationView.getMenu();
            menu.findItem(R.id.consultation).setVisible(false);
            menu.findItem(R.id.menu_BonSortie).setVisible(false);
            menu.findItem(R.id.menu_ListeBS).setVisible(false);
            menu.findItem(R.id.menu_listeEchUsage).setVisible(false);

            menu.findItem(R.id.menu_listeEchDate).setVisible(false);

        }
        if(var.equals("Responsable maintenance"))
        {
            menu = navigationView.getMenu();
            menu.findItem(R.id.consultation).setVisible(false);
            menu.findItem(R.id.menu_BonSortie).setVisible(false);
            menu.findItem(R.id.menu_ListeBS).setVisible(false);

        }
        if(var.equals("Responsable stock"))
        {
            menu = navigationView.getMenu();
            menu.findItem(R.id.menu_listeEchUsage).setVisible(false);
            menu.findItem(R.id.menu_BonSortie).setVisible(false);
            menu.findItem(R.id.menu_ListeBS).setVisible(false);
            menu.findItem(R.id.menu_listeEchUsage).setVisible(false);

            menu.findItem(R.id.menu_listeEchDate).setVisible(false);
        }

    }
}
