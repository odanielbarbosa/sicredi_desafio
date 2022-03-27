package com.sicredi;

import com.sicredi.service.ContaService;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
* Funcionalidade:
* 0. Criar uma aplicação SprintBoot standalone. Exemplo: java -jar SincronizacaoReceita <input-file>
* 1. Processa um arquivo CSV de entrada com o formato abaixo.
* 2. Envia a atualização para a Receita através do serviço (SIMULADO pela classe ReceitaService).
* 3. Retorna um arquivo com o resultado do envio da atualização da Receita. Mesmo formato adicionando o resultado em uma nova coluna.
*/

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        ContaService.enviarContasReceita();
    }
}