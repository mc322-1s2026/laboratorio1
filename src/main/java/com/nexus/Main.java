package com.nexus;

import com.nexus.model.Task;
import com.nexus.model.User;
import com.nexus.exception.NexusValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<User> users = new ArrayList<>();

        System.out.println("=== Nexus System: Cadastro de Operadores ===");

        // Tentativa de cadastro manual (Exemplos variados)
        register(users, "nicolas.tesla", "tesla@nexus.com");   // Válido
        register(users, "ada.lovelace", "ada@nexus.com");     // Válido
        register(users, "", "email_sem_user@nexus.com");      // Fora de padrão (Username vazio)
        register(users, "user_sem_at", "contato.com");        // Fora de padrão (Email inválido)
        register(users, "margaret.hamilton", "margo@nexus.com"); // Válido

        printUserTable(users);

        System.out.println("\n>>> Criando Backlog de Tarefas <<<\n");

        try {
            Task t1 = new Task("Implementar Autenticação", LocalDate.now().plusDays(5));
            Task t2 = new Task("Configurar Banco de Dados", LocalDate.now().plusDays(2));
            
            System.out.printf("Tarefa criada: [%d] %s (Status: %s)%n", 
                t1.getId(), t1.getTitle(), t1.getStatus());
            System.out.printf("Tarefa criada: [%d] %s (Status: %s)%n", 
                t2.getId(), t2.getTitle(), t2.getStatus());
                
        } catch (Exception e) {
            System.err.println("Erro ao gerenciar tarefas: " + e.getMessage());
        }

        System.out.println("\n>>> Métricas de Sistema <<<");
        System.out.println("Total de Tarefas: " + Task.totalTasksCreated);
    }

    private static void register(List<User> list, String username, String email) {
        try {
            // Quando o aluno implementar a lógica no construtor, 
            // os casos "fora de padrão" cairão no catch abaixo.
            User user = new User(username, email);
            list.add(user);
            System.out.println("[OK] Usuário cadastrado: " + username);
        } catch (NexusValidationException e) {
            System.err.println("[ERRO] Falha ao cadastrar " + username + ": " + e.getMessage());
        }
    }

    private static void printUserTable(List<User> users) {
        System.out.println("\n" + "=".repeat(60));
        // %-20s -> String, alinhada à esquerda, ocupando 20 caracteres
        String headerFormat = "| %-20s | %-30s |%n";
        
        System.out.printf(headerFormat, "USERNAME", "EMAIL");
        System.out.println("-".repeat(60));

        for (User u : users) {
            System.out.printf(headerFormat, 
                truncate(u.consultUsername(), 20), 
                truncate(u.consultEmail(), 30)
            );
        }
        System.out.println("=".repeat(60));
        System.out.printf("Total de usuários ativos: %d%n", users.size());
    }

    private static String truncate(String text, int width) {
        if (text == null) return "";
        return text.length() > width ? text.substring(0, width - 3) + "..." : text;
    }
}