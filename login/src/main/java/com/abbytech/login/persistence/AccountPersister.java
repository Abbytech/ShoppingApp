package com.abbytech.login.persistence;

public interface AccountPersister<T> {
    void deleteAccount();

    T getAccount();

    void setAccount(T account);
}
