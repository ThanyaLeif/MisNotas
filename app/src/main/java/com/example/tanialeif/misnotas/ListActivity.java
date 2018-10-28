package com.example.tanialeif.misnotas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tanialeif.misnotas.Adapters.ListNoteAdapter;
import com.example.tanialeif.misnotas.DB.DAONote;
import com.example.tanialeif.misnotas.Model.Note;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    FloatingActionButton fab;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    RecyclerView list;
    ListNoteAdapter adapter;

    int REQ_NEW_NOTE = 1;
    int REQ_MOD_NOTE = 2;

    private ArrayList<Note> temporalStaticListExample() {
        ArrayList<Note> listNotes = new ArrayList<>();

        listNotes.add(new Note(
                45,
                "Terminar la app",
                "Completar la app para movil I.",
                Note.TypeNote.Note
        ));

        listNotes.add(new Note(
                5,
                "Hacer el seguidor de lineas",
                "Jeziel.",
                Note.TypeNote.Note
        ));

        listNotes.add(new Note(
                45,
                "Terminar la app",
                "Completar la app para movil I.",
                Note.TypeNote.Note
        ));

        listNotes.add(new Note(
                5,
                "Hacer el seguidor de lineas",
                "Jeziel.",
                Note.TypeNote.Note
        ));

        listNotes.add(new Note(
                7,
                "Proyecto final",
                "Jeziel.",
                Note.TypeNote.Task
        ));

        listNotes.add(new Note(
                5,
                "Proyecto Sistema Chispa",
                "GPS",
                Note.TypeNote.Task
        ));

        listNotes.add(new Note(
                7,
                "Proyecto final",
                "Jeziel.",
                Note.TypeNote.Task
        ));

        listNotes.add(new Note(
                5,
                "Proyecto Sistema Chispa",
                "GPS",
                Note.TypeNote.Task
        ));

        listNotes.add(new Note(
                9,
                "Alguna otra cosa",
                "...",
                Note.TypeNote.Note
        ));

        return listNotes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        final Context self = this;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detail = new Intent(self, VistaDetalle.class);

                startActivityForResult(detail, REQ_NEW_NOTE);
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final DAONote daoNote = new DAONote(this);

        list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListNoteAdapter(this, daoNote.getAll());
        list.setAdapter(adapter);

        adapter.setOnItemLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int vid = list.getChildAdapterPosition(v);
                final Note note = adapter.getItem(vid);

                CharSequence[] options;

                if (note.getType() == Note.TypeNote.Note) {
                    options = new CharSequence[]  {
                            "Editar",
                            "Eliminar"
                    };
                } else {
                    options = new CharSequence[]  {
                            "Editar",
                            "Eliminar",
                            "Marcar como hecha"
                    };
                }

                AlertDialog.Builder menu = new AlertDialog.Builder(self);

                menu.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: {
                                Intent detail = new Intent(self, VistaDetalle.class);
                                detail.putExtra("id", note.getId());
                                startActivityForResult(detail, REQ_MOD_NOTE);
                                break;
                            }
                            case 1: {
                                daoNote.delete(note.getId());
                                adapter.updateData(daoNote.getAll());
                                break;
                            }
                            case 2: {
                                // TODO: MARCAR COMO HECHA
                                Log.i("ATSARATSA", "Marcar como hecha");
                                break;
                            }
                        }
                    }
                });

                menu.show();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list, menu);

        MenuItem searchItem  = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.setFilterText(s);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_all) {
            adapter.setFilter(ListNoteAdapter.TypeFilter.All);
            adapter.notifyDataSetChanged();
        } else if (id == R.id.nav_notes) {
            adapter.setFilter(ListNoteAdapter.TypeFilter.Notes);
            adapter.notifyDataSetChanged();
        } else if (id == R.id.nav_tasks) {
            adapter.setFilter(ListNoteAdapter.TypeFilter.Tasks);
            adapter.notifyDataSetChanged();
        } else if (id == R.id.nav_settings) {
            // TODO: CREATE SETTINGS SCREEN
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
