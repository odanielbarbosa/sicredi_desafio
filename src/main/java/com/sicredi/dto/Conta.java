package com.sicredi.dto;

import com.sicredi.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Conta {

    String agencia;
    String conta;
    double saldo;
    Status status;
    boolean atualizado;
}
