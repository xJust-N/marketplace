package ru.itis.marketplace.models;

public interface Entity<ID> {
    ID getId();
    void setId(ID id);
}
