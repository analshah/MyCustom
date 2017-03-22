package com.example.analshah.mycustom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import static com.example.analshah.mycustom.R.id.tvPersonDetailAddress;
import static com.example.analshah.mycustom.R.id.tvPersonDetailEmail;

/**
 * Created by analll on 20-03-2017.
 */

class PaymentDetailsActivity extends AppCompatActivity {
    private TextView tvPersonDetailId,tvPersonDetailName,tvamount,tvmethod;

    private PaymentModel paymentModel=new PaymentModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getAllWidgets();
        getDataFromPreviousActivity();
        setDataInWidgets();
    }

    private void getAllWidgets()
    {
        tvPersonDetailId= (TextView) findViewById(R.id.tvPaymentid);
        tvPersonDetailName= (TextView) findViewById(R.id.tvname);
        tvamount= (TextView) findViewById(R.id.tvamount);
        tvmethod= (TextView) findViewById(R.id.tvmethod);
    }
    private void getDataFromPreviousActivity()
    {
        int personID = getIntent().getIntExtra("PersonID", -1);
        paymentModel=Main2Activity.getInstance().searchPerson(personID);
    }
    private void setDataInWidgets()
    {
       /* tvPersonDetailId.setText(getString(R.string.payment_id,String.valueOf(paymentModel.getId())));
        tvPersonDetailName.setText(getString(R.string.paymentname,paymentModel.getName()));
        tvPersonDetailEmail.setText(getString(R.string.person_email,personDetailsModel.getEmail()));
       // tvPersonDetailAddress.setText(getString(R.string.person_address,personDetailsModel.getAddress()));
        tvamount.setText(getString(R.string.person_age, String.valueOf(personDetailsModel.getAge())));*/
        tvPersonDetailId.setText(getString(R.string.payment_id,String.valueOf(paymentModel.getId())));
        tvPersonDetailName.setText(getString(R.string.personname,paymentModel.getName()));
        tvamount.setText(getString(R.string.payamount, String.valueOf(paymentModel.getAmount())));
        tvmethod.setText(getString(R.string.method,paymentModel.getMethod()));

    }


}
