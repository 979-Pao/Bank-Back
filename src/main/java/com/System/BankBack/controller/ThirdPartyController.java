package com.System.BankBack.controller;

import com.System.BankBack.dto.*;
import com.System.BankBack.model.transactions.Transaction;
import com.System.BankBack.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * End-points para operaciones de terceros autorizados (ThirdParty).
 * – POST   /thirdparty/send               → Enviar dinero desde una cuenta usando hashKey
 * – POST   /thirdparty/receive            → Recibir dinero en una cuenta usando hashKey
 * – GET    /thirdparty/transactions       → Consultar todas las transacciones asociadas al hashKey
 * – DELETE /thirdparty/{id}               → Eliminar un usuario ThirdParty (solo admin autorizado)
 * Todas las rutas (excepto DELETE) requieren el header: "hashed-key": [clave-hash-autorizada]
 */

@RestController
@RequestMapping("/thirdparty")
@RequiredArgsConstructor
public class ThirdPartyController {

    private final ThirdPartyService thirdSvc;

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void send(@RequestHeader("X-Hashed-Key") String hash,
                     @RequestBody ThirdPartyMovementDTO dto) {
        thirdSvc.sendMoney(hash, dto);
    }

    // ----- POST  /thirdparty/receive ---------------------------------
    @PostMapping("/receive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void receive(@RequestHeader("X-Hashed-Key") String hash,
                        @RequestBody ThirdPartyMovementDTO dto) {
        thirdSvc.receiveMoney(hash, dto);
    }

    // ----- GET   /thirdparty/transactions ----------------------------
    @GetMapping("/transactions")
    public List<Transaction> listTransactions(
            @RequestHeader("X-Hashed-Key") String hashedKey) {
        return thirdSvc.listTransactions(hashedKey);
    }

    // ----- DELETE /thirdparty/{id}   (solo ADMIN) --------------------
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        thirdSvc.deleteThirdParty(id);
    }
}
