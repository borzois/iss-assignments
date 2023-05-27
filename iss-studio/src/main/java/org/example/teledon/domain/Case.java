package org.example.teledon.domain;

public class Case implements Entity<Long> {
    private Long id;
    private String name;
    private String description;

    public Case(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Case() {
    }

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "case " + name + " - " + description;
    }
}
