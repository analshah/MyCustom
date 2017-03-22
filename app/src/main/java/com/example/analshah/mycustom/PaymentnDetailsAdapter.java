package com.example.analshah.mycustom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by analll on 20-03-2017.
 */

class PaymentnDetailsAdapter extends BaseAdapter {


    private ArrayList<PaymentModel> paymentModelsArrayList;
    private Context context;
    private LayoutInflater inflater;
    public PaymentnDetailsAdapter(Context context, ArrayList<PaymentModel> paymentModelsArrayList) {
        this.context = context;
        this.paymentModelsArrayList = paymentModelsArrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return paymentModelsArrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return paymentModelsArrayList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        PaymentnDetailsAdapter.Holder holder;
        if (v == null) {
            v = inflater.inflate(R.layout.inflate_list_item2, null);
            holder = new PaymentnDetailsAdapter.Holder();
            holder.tvPersonName = (TextView) v.findViewById(R.id.tvPersonName);
            holder.ivEditPesonDetail=(ImageView)v.findViewById(R.id.ivEditPesonDetail);
            holder.ivDeletePerson=(ImageView)v.findViewById(R.id.ivDeletePerson);
            v.setTag(holder);
        } else {
            holder = (PaymentnDetailsAdapter.Holder) v.getTag();
        }
        holder.tvPersonName.setText(paymentModelsArrayList.get(position).getName());
        holder.ivEditPesonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentModel dataToEditModel= Main2Activity.getInstance().searchPerson(paymentModelsArrayList.get(position).getId());
                Main2Activity.getInstance().addOrUpdatePersonDetailsDialog1(dataToEditModel,position);
            }
        });
        holder.ivDeletePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowConfirmDialog(context,paymentModelsArrayList.get(position).getId(), position);
            }
        });
        return v;
    }
    class Holder {
        TextView tvPersonName;
        ImageView ivDeletePerson, ivEditPesonDetail;
    }
    public static void ShowConfirmDialog(Context context,final int paymentId,final int position)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage("Are you sure you want to delete this record?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Main2Activity.getInstance().deletePerson(paymentId,position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
