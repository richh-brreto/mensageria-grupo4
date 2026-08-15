package com.escola.notification.domain.entities;

/**
 * Entidade de domínio que representa um professor.
 */
public final class Teacher {
    private final String id;
    private final String name;
    private final String email;

    public Teacher(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
