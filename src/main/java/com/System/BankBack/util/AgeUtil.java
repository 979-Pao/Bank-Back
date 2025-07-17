package com.System.BankBack.util;

import java.time.*;

public class AgeUtil {
    public static int years(LocalDate dob){
        return Period.between(dob, LocalDate.now()).getYears();} }
