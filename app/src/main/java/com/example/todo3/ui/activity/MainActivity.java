package com.example.todo3.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;

import com.example.todo3.R;

import com.example.todo3.ui.fragment.AddTagFragment;
import com.example.todo3.ui.fragment.AddTaskFragment;
import com.example.todo3.ui.fragment.HomeFragment;
import com.example.todo3.ui.fragment.SearchTaskFragment;
import com.example.todo3.ui.fragment.ShowByDateFragment;
import com.example.todo3.ui.fragment.ShowUncompletedTaskFragment;
import com.example.todo3.utilities.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utility.dateSortFlag = false;
        Utility.tagSortFlag = false;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.floating_btn_home_icon));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "You are now on HOME screen", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Fragment fragment;
                fragment = new HomeFragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragmentPlace, fragment);
                ft.addToBackStack(fragment.getClass().getName());//so that back button get backs to previous state
                ft.commit();

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);


        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = returnFragmentOnSelection(id);  //returns which fragment to load

        if (fragment == null) {
            return true;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentPlace, fragment);
        ft.addToBackStack(fragment.getClass().getName());//so that back button get backs to previous state
        ft.commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }

    private Fragment returnFragmentOnSelection(int id) {
        switch (id) {
            case R.id.navManageTags:
                return new AddTagFragment();

            case R.id.navAddTask:
                return new AddTaskFragment(getApplicationContext());

            case R.id.navViewByDate:
                return new ShowByDateFragment();

            case R.id.navShowUncompTasks:
                return new ShowUncompletedTaskFragment();

            case R.id.navSortByDate: {
                Switch sortDate = findViewById(R.id.switchBtnDate);
                Utility.dateSortFlag = !Utility.dateSortFlag;   //toggle its previous value
                sortDate.setChecked(Utility.dateSortFlag);
                return new HomeFragment();  //new HomeFragment will be returned with flag status changed and task list will be
                                            //sorted accordingly
            }
            case R.id.navSortByTag: {
                Switch sortTag = findViewById(R.id.switchBtnTag);
                Utility.tagSortFlag = !Utility.tagSortFlag;     //toggle its previous value
                sortTag.setChecked(Utility.tagSortFlag);
                return new HomeFragment();  //new HomeFragment will be returned with flag status changed and task list will be
                                            //sorted accordingly
            }
            default:
                return null;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {     //creating option menu
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   //which item is selected in menu option
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.actionSearchTask: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = LayoutInflater.from(this);
                View view = inflater.inflate(R.layout.search_task_dialog, null);
                final EditText editTextSearchtxt = view.findViewById(R.id.editTextSearchTask);
                builder.setView(view)
                        .setTitle("Search Task")
                        .create();
                builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Fragment fragment = new SearchTaskFragment(editTextSearchtxt.getText().toString());
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentPlace, fragment);
                        fragmentTransaction.addToBackStack(fragment.getClass().getName());//so that back button get backs to previous state
                        fragmentTransaction.commit();
                    }
                });
                builder.show();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
