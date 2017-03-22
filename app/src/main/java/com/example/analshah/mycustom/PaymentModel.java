package com.example.analshah.mycustom;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by analll on 20-03-2017.
 */

public class PaymentModel extends RealmObject {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @PrimaryKey

    private int id;
    private String name;
    private String method;
    private int amount;
}
