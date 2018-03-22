package com.aarcoraci.bankapp.domain;

import java.util.Date;

/**
 * Created by angel on 3/22/2018.
 */

public class Transaction {
    public float amount;
    public Date date;

    public Transaction() {

    }

    public Transaction(float amount, Date date) {
        this.amount = amount;
        this.date = date;
    }
}
