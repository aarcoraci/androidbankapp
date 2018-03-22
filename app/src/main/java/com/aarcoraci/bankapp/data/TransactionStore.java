package com.aarcoraci.bankapp.data;

import com.aarcoraci.bankapp.domain.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by angel on 3/22/2018.
 * <p>
 * simulates a source of data
 */

public class TransactionStore {

    private static TransactionStore instance = new TransactionStore();

    public static TransactionStore getInstance() {
        return instance;
    }

    private List<Transaction> transactionList = new ArrayList<>();

    private TransactionStore() {
        generateTransactionList();
    }

    /**
     * Generates some sample transactions to be used as source of data
     */
    private void generateTransactionList() {

        transactionList.clear();

        Random randomGenerator = new Random();
        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date();

        for (int i = 0; i < 12; i++) {
            int totalTransactions = 5 + randomGenerator.nextInt(4);
            int daysBetweenTransactions = (30 / totalTransactions); // to create a smooth curve :)

            for (int j = 1; j <= totalTransactions; j++) {
                // transaction date
                calendar.setTime(currentDate);  // init default year
                calendar.set(Calendar.MONTH, i);
                calendar.set(Calendar.DAY_OF_MONTH, j * daysBetweenTransactions);
                // some amount
                float amount = 200 + randomGenerator.nextInt(500);
                amount = randomGenerator.nextBoolean() ? amount : amount * -1;

                // add element to data collection
                transactionList.add(new Transaction(amount, calendar.getTime()));

            }
        }

        // sort by date
        Collections.sort(transactionList, (transaction, t1) -> transaction.date.compareTo(t1.date));
    }

    public Observable<Transaction> getTransactions() {
        return Observable.create(emitter -> {
            for (Transaction current : transactionList) {
                emitter.onNext(current);
            }
            emitter.onComplete();
        });
    }
}
