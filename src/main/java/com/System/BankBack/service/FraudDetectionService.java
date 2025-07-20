package com.System.BankBack.service;

import com.System.BankBack.model.accounts.Account;
import com.System.BankBack.model.transactions.Transaction;

public interface FraudDetectionService {

    /** Analiza el movimiento recién creado y, si detecta fraude,
     *  congela la cuenta y devuelve **true**.  */
    boolean evaluate(Transaction tx);
}
