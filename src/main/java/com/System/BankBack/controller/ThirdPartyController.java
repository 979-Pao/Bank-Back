package com.System.BankBack.controller;

import com.System.BankBack.dto.*;
import com.System.BankBack.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

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

    /* POST enviar dinero */
    @PostMapping("/send")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void send(HttpServletRequest request, @RequestBody ThirdPartyMovementDTO dto){
        thirdSvc.sendMoney(request.getHeader("hashed-key"), dto);
    }

    /* POST recibir dinero */
    @PostMapping("/receive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void receive(HttpServletRequest request, @RequestBody ThirdPartyMovementDTO dto){
        thirdSvc.receiveMoney(request.getHeader("hashed-key"), dto);
    }

    /* GET auditoría */
    @GetMapping("/transactions")
    public List<?> listTx(HttpServletRequest request){
        return thirdSvc.listTransactions(request.getHeader("hashed-key"));
    }

    /* DELETE (admin) */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){ thirdSvc.deleteThirdParty(id);}
}
