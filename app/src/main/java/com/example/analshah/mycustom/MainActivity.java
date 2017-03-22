package com.example.analshah.mycustom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static int id = 1;
    private FloatingActionButton fabAddPerson;
    private Realm myRealm;
    private ListView lvPersonNameList;
    private static ArrayList<PersonDetailsModel> personDetailsModelArrayList = new ArrayList<>();
    private PersonDetailsAdapter personDetailsAdapter;
    private static MainActivity instance;
    private AlertDialog.Builder subDialog;
    private Button button12;

    private View.OnClickListener buttonListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this,Main2Activity.class);
            startActivity(i);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button12 = (Button) findViewById(R.id.payment);
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);
        Realm.init(this);
        myRealm = Realm.getDefaultInstance();
        instance = this;
        getAllWidgets();
        bindWidgetsWithEvents();
        setPersonDetailsAdapter();
        getAllUsers();
        button12.setOnClickListener(buttonListner);

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    public static MainActivity getInstance() {
        return instance;
    }
    private void getAllWidgets() {
        fabAddPerson = (FloatingActionButton) findViewById(R.id.fab);
        lvPersonNameList = (ListView) findViewById(R.id.lvPersonNameList);
    }
    private void bindWidgetsWithEvents() {
        fabAddPerson.setOnClickListener(this);
        lvPersonNameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,PersonDetailsActivity.class);
                intent.putExtra("PersonID", personDetailsModelArrayList.get(position).getId());
                startActivity(intent);
            }
        });
    }
    private void setPersonDetailsAdapter() {
        personDetailsAdapter = new PersonDetailsAdapter(MainActivity.this, personDetailsModelArrayList);
        lvPersonNameList.setAdapter(personDetailsAdapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                addOrUpdatePersonDetailsDialog(null,-1);
                break;
        }
    }
    public void addOrUpdatePersonDetailsDialog(final PersonDetailsModel model,final int position) {
        //subdialog
        subDialog =  new AlertDialog.Builder(MainActivity.this)
                .setMessage("Please enter all the details!!!")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg2, int which) {
                        dlg2.cancel();
                    }
                });
        //maindialog
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.prompt_dialog, null);
        AlertDialog.Builder mainDialog = new AlertDialog.Builder(MainActivity.this);
        mainDialog.setView(promptsView);
        final EditText etAddPersonName = (EditText) promptsView.findViewById(R.id.etAddPersonName);
        final EditText etAddPersonEmail = (EditText) promptsView.findViewById(R.id.etAddPersonEmail);
        final EditText etAddPersonAddress = (EditText) promptsView.findViewById(R.id.etAddPersonAddress);
        final EditText etAddPersonAge = (EditText) promptsView.findViewById(R.id.etAddPersonAge);
        if (model != null) {
            etAddPersonName.setText(model.getName());
            etAddPersonEmail.setText(model.getEmail());
            etAddPersonAddress.setText(model.getAddress());
            etAddPersonAge.setText(String.valueOf(model.getAge()));
        }
        mainDialog.setCancelable(false)
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog dialog = mainDialog.create();
        dialog.show();
        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utility.isBlankField(etAddPersonName) && !Utility.isBlankField(etAddPersonEmail) && !Utility.isBlankField(etAddPersonAddress) && !Utility.isBlankField(etAddPersonAge)) {
                    PersonDetailsModel personDetailsModel = new PersonDetailsModel();
                    personDetailsModel.setName(etAddPersonName.getText().toString());
                    personDetailsModel.setEmail(etAddPersonEmail.getText().toString());
                    personDetailsModel.setAddress(etAddPersonAddress.getText().toString());
                    personDetailsModel.setAge(Integer.parseInt(etAddPersonAge.getText().toString()));
                    if (model == null)
                        addDataToRealm(personDetailsModel);
                    else
                        updatePersonDetails(personDetailsModel, position, model.getId());
                    dialog.cancel();
                } else {
                    subDialog.show();
                }
            }
        });
    }
    private void addDataToRealm(PersonDetailsModel model) {
        myRealm.beginTransaction();
        PersonDetailsModel personDetailsModel = myRealm.createObject(PersonDetailsModel.class,id);
       // personDetailsModel.setId(id);
        personDetailsModel.setName(model.getName());
        personDetailsModel.setEmail(model.getEmail());
        personDetailsModel.setAddress(model.getAddress());
        personDetailsModel.setAge(model.getAge());
        personDetailsModelArrayList.add(personDetailsModel);
        myRealm.commitTransaction();
        personDetailsAdapter.notifyDataSetChanged();
        id++;
    }
    public void deletePerson(int personId, int position) {
        RealmResults<PersonDetailsModel> results = myRealm.where(PersonDetailsModel.class).equalTo("id", personId).findAll();
        myRealm.beginTransaction();
        results.deleteAllFromRealm();
        myRealm.commitTransaction();
        personDetailsModelArrayList.remove(position);
        personDetailsAdapter.notifyDataSetChanged();
    }
    public PersonDetailsModel searchPerson(int personId) {
        RealmResults<PersonDetailsModel> results = myRealm.where(PersonDetailsModel.class).equalTo("id", personId).findAll();
        myRealm.beginTransaction();
        myRealm.commitTransaction();
        return results.get(0);
    }
    public void updatePersonDetails(PersonDetailsModel model,int position,int personID) {
        PersonDetailsModel editPersonDetails = myRealm.where(PersonDetailsModel.class).equalTo("id", personID).findFirst();
        myRealm.beginTransaction();
        editPersonDetails.setName(model.getName());
        editPersonDetails.setEmail(model.getEmail());
        editPersonDetails.setAddress(model.getAddress());
        editPersonDetails.setAge(model.getAge());
        myRealm.commitTransaction();
        personDetailsModelArrayList.set(position, editPersonDetails);
        personDetailsAdapter.notifyDataSetChanged();
    }
    private void getAllUsers() {
        RealmResults<PersonDetailsModel> results = myRealm.where(PersonDetailsModel.class).findAll();
        myRealm.beginTransaction();
        for (int i = 0; i < results.size(); i++) {
            personDetailsModelArrayList.add(results.get(i));
        }
        if(results.size()>0)
            id = myRealm.where(PersonDetailsModel.class).max("id").intValue() + 1;
        myRealm.commitTransaction();
        personDetailsAdapter.notifyDataSetChanged();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        personDetailsModelArrayList.clear();
        myRealm.close();
    }


}
