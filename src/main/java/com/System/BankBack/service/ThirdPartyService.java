package com.System.BankBack.service;

import com.System.BankBack.dto.*;
import com.System.BankBack.model.accounts.Account;
import com.System.BankBack.model.embedded.Money;
import java.util.List;

public interface ThirdPartyService {
    void sendMoney(String hashedKey, ThirdPartyMovementDTO dto);
    void receiveMoney(String hashedKey, ThirdPartyMovementDTO dto);
    List<?> listTransactions(String hashedKey);
    void deleteThirdParty(Long id);
}
