package com.management.admin.modules.sys.dao;

public interface UserDao {
    String queryPassword(String id);

    int changePassword(String id, String password);
}
