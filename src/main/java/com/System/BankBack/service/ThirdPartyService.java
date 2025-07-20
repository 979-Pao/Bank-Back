package com.System.BankBack.service;

import com.System.BankBack.dto.*;
import com.System.BankBack.model.transactions.Transaction;

import java.util.List;

public interface ThirdPartyService {
    void sendMoney(String hashKey, ThirdPartyMovementDTO dto);
    void receiveMoney(String hashKey, ThirdPartyMovementDTO dto);
    List<Transaction> listTransactions(String hashKey);
    void deleteThirdParty(Long id);
}
