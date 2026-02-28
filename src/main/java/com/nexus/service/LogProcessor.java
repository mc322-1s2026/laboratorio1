package com.nexus.service;

import com.nexus.model.*;
import com.nexus.exception.NexusValidationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class LogProcessor {

    public void processLog(String fileName, Workspace workspace, List<User> users) {
        try {
            // Busca o arquivo dentro da pasta de recursos do projeto (target/classes)
            var resource = getClass().getClassLoader().getResourceAsStream(fileName);
            
            if (resource == null) {
                throw new IOException("Arquivo não encontrado no classpath: " + fileName);
            }

            try (java.util.Scanner s = new java.util.Scanner(resource).useDelimiter("\\A")) {
                String content = s.hasNext() ? s.next() : "";
                List<String> lines = List.of(content.split("\\R"));
                
                for (String line : lines) {
                    if (line.isBlank() || line.startsWith("#")) continue;

                    String[] p = line.split(";");
                    String action = p[0];

                    try {
                        switch (action) {
                            case "CREATE_USER" -> {
                                users.add(new User(p[1], p[2]));
                                System.out.println("[LOG] Usuário criado: " + p[1]);
                            }
                            case "CREATE_TASK" -> {
                                Task t = new Task(p[1], LocalDate.parse(p[2]));
                                workspace.addTask(t);
                                System.out.println("[LOG] Tarefa criada: " + p[1]);
                            }
                            case "MOVE_TO_PROGRESS" -> {
                                // Aluno implementará o findTask e findUser
                                Task task = findTask(workspace, Integer.parseInt(p[1]));
                                User user = findUser(users, p[2]);
                                task.moveToInProgress(user);
                                System.out.println("[LOG] Task " + p[1] + " movida para IN_PROGRESS por " + p[2]);
                            }
                            case "SET_BLOCKED" -> {
                                Task task = findTask(workspace, Integer.parseInt(p[1]));
                                task.setBlocked(Boolean.parseBoolean(p[2]));
                                System.out.println("[LOG] Task " + p[1] + " bloqueio definido como: " + p[2]);
                            }
                            case "SET_DONE" -> {
                                Task task = findTask(workspace, Integer.parseInt(p[1]));
                                task.markAsDone();
                                System.out.println("[LOG] Task " + p[1] + " finalizada.");
                            }
                            default -> System.err.println("[WARN] Ação desconhecida: " + action);
                        }
                    } catch (NexusValidationException e) {
                        System.err.println("[ERRO DE REGRAS] Falha no comando '" + line + "': " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[ERRO FATAL] " + e.getMessage());
        }
    }

    // Métodos auxiliares que os alunos podem implementar ou você pode fornecer
    private Task findTask(Workspace ws, int id) {
        return ws.getTasks().stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Task ID " + id + " não encontrada."));
    }

    private User findUser(List<User> users, String username) {
        return users.stream()
                .filter(u -> u.consultUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User " + username + " não encontrado."));
    }
}