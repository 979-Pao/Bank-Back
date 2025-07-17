package com.System.BankBack.service;

import com.System.BankBack.dto.*;
import com.System.BankBack.model.accounts.Account;
import com.System.BankBack.model.embedded.Money;
import java.util.List;

public interface ThirdPartyService {
    void sendMoney(String hashKey, ThirdPartyMovementDTO dto);
    void receiveMoney(String hashKey, ThirdPartyMovementDTO dto);
    List<?> listTransactions(String hashKey);
    void deleteThirdParty(Long id);
}
