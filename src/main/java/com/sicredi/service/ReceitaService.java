package com.sicredi.service;

import com.sicredi.enums.Status;

import java.util.Arrays;

/**
 * @author gabriel_stabel<gabriel_stabel@sicredi.com.br>
 */
public class ReceitaService {

    public static boolean atualizarConta(String agencia, String conta, double saldo, String status) throws RuntimeException, InterruptedException {

        validate(agencia, conta, status);

        long wait = Math.round(Math.random() * 4000) + 1000;
        Thread.sleep(wait);

        long randomError = Math.round(Math.random() * 1000);
        if (randomError == 500) throw new RuntimeException("Error");

        return true;
    }

    public static void validate(String agencia, String conta, String status) {
        // Formato agencia: 0000
        if (agencia == null || agencia.length() != 4) {
            throw new RuntimeException("Agencia inválida!");
        }

        // Formato conta: 000000
        if (conta == null || conta.length() != 6) {
            throw new RuntimeException("Conta inválida!");
        }

        if (!Arrays.asList(Status.values()).contains(Status.valueOf(status))) {
            throw new RuntimeException("Status inválido!");
        }
    }
}
