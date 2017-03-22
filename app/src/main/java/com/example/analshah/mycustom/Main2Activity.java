package com.example.analshah.mycustom;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class Main2Activity extends AppCompatActivity implements View.OnClickListener{


    private static int id = 1;
    private FloatingActionButton fabAddPerson1;
    private Realm myRealm;
    private ListView ivpaymentlist;
    private static ArrayList<PaymentModel> paymentModelArrayList = new ArrayList<>();
    private PaymentnDetailsAdapter paymentDetailsAdapter;
    private static Main2Activity instance;
    private AlertDialog.Builder subDialog;
    int pos;
    String value="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Realm.init(this);
        myRealm = Realm.getDefaultInstance();
        instance = this;
        getAllWidgets();
        bindWidgetsWithEvents();
        setPaymentDetailsAdapter();
       getAllUsers();
    }

    public static Main2Activity getInstance() {
        return instance;
    }

    private void getAllWidgets() {
        fabAddPerson1 = (FloatingActionButton) findViewById(R.id.fab2);
        ivpaymentlist = (ListView) findViewById(R.id.ivpaymentlist);
    }

    private void bindWidgetsWithEvents() {
        fabAddPerson1.setOnClickListener(this);
        ivpaymentlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(Main2Activity.this,PaymentDetailsActivity.class);
                intent.putExtra("PaymentID", paymentModelArrayList.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void setPaymentDetailsAdapter() {
        paymentDetailsAdapter = new PaymentnDetailsAdapter(Main2Activity.this, paymentModelArrayList);
        ivpaymentlist.setAdapter(paymentDetailsAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab2:
                addOrUpdatePersonDetailsDialog1(null,-1);
                break;
        }
    }

    public void addOrUpdatePersonDetailsDialog1(final PaymentModel model, final int position) {


        subDialog =  new AlertDialog.Builder(Main2Activity.this)
                .setMessage("Please enter all the details!!!")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg2, int which) {
                        dlg2.cancel();
                    }
                });
        LayoutInflater li = LayoutInflater.from(Main2Activity.this);
        View promptsView = li.inflate(R.layout.prompt2_dialog, null);
        AlertDialog.Builder mainDialog = new AlertDialog.Builder(Main2Activity.this);
        mainDialog.setView(promptsView);
        final EditText etAddPersonName = (EditText) promptsView.findViewById(R.id.etAddPersonName);
        final EditText etAmount = (EditText) promptsView.findViewById(R.id.etAmount);
        final RadioGroup radiopayment=(RadioGroup) promptsView.findViewById(R.id.radiopayment);
        final RadioButton radiocredit=(RadioButton)promptsView.findViewById(R.id.radiocredit);
        final RadioButton radiodebit=(RadioButton)promptsView.findViewById(R.id.radiodebit);
        if(value != null) {
            if (value.equals("") || value.equals("Credit")) {
                value = radiocredit.getText().toString();
                radiocredit.setChecked(true);
                radiodebit.setChecked(false);
            } else {
                value = radiodebit.getText().toString();
                radiodebit.setChecked(true);
                radiocredit.setChecked(false);
            }
        }
        Log.d("chiii12", "=oncreat="+value);

        radiopayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId){
                    case R.id.radiocredit:
                           value="Credit";
                        Log.d("chiii12","=Credit="+value);
                        break;
                    case R.id.radiodebit:
                         value="Debit";
                        Log.d("chiiii12","=Debit="+value);
                        break;

                }
            }
        });


        if (model != null) {

            etAddPersonName.setText(model.getName());
            etAmount.setText(String.valueOf(model.getAmount()));
            Log.d("method","=="+model.getMethod());

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
                if (!Utility2.isBlankField(etAddPersonName) && !Utility2.isBlankField(etAmount)) {
                    PaymentModel paymentModel = new PaymentModel();
                    paymentModel.setName(etAddPersonName.getText().toString());
                    Log.d("chill12","=okclick="+value);
                    paymentModel.setMethod(value);
                    paymentModel.setAmount(Integer.parseInt(etAmount.getText().toString()));
                    if (model == null)
                        addDataToRealm(paymentModel);
                    else
                        updatePaymentDetails(paymentModel, position, model.getId());
                    dialog.cancel();
                } else {
                    subDialog.show();
                }
            }
        });

    }

    private void addDataToRealm(PaymentModel model) {

        myRealm.beginTransaction();
        PaymentModel paymentDetailModel = myRealm.createObject(PaymentModel.class,id);
       // paymentDetailModel.setId(id);
        paymentDetailModel.setName(model.getName());
        Log.d("method45","=="+model.getMethod());
        paymentDetailModel.setMethod(model.getMethod());
      paymentDetailModel.setAmount(model.getAmount());
        paymentModelArrayList.add(paymentDetailModel);
        myRealm.commitTransaction();
        paymentDetailsAdapter.notifyDataSetChanged();
        id++;
    }


    public PaymentModel searchPerson(int paymentId) {
        RealmResults<PaymentModel> results = myRealm.where(PaymentModel.class).equalTo("id", paymentId).findAll();
        myRealm.beginTransaction();
        myRealm.commitTransaction();
        return results.get(0);
    }

    public void deletePerson(int paymentId, int position) {
        RealmResults<PaymentModel> results = myRealm.where(PaymentModel.class).equalTo("id", paymentId).findAll();
        myRealm.beginTransaction();
        results.deleteAllFromRealm();
        myRealm.commitTransaction();
        paymentModelArrayList.remove(position);
        paymentDetailsAdapter.notifyDataSetChanged();
    }

    private void updatePaymentDetails(PaymentModel model, int position, int paymentid) {

        PaymentModel editPayment = myRealm.where(PaymentModel.class).equalTo("id",paymentid).findFirst();
        myRealm.beginTransaction();
        editPayment.setName(model.getName());
        editPayment.setAmount(model.getAmount());
        myRealm.commitTransaction();
        paymentModelArrayList.set(position, editPayment);
        paymentDetailsAdapter.notifyDataSetChanged();
    }

    private void getAllUsers() {
        RealmResults<PaymentModel> results = myRealm.where(PaymentModel.class).findAll();
        myRealm.beginTransaction();
        for (int i = 0; i < results.size(); i++) {
            paymentModelArrayList.add(results.get(i));
        }
        if(results.size()>0)
            id = myRealm.where(PaymentModel.class).max("id").intValue() + 1;
        myRealm.commitTransaction();
        paymentDetailsAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        paymentModelArrayList.clear();
        myRealm.close();
    }

}
