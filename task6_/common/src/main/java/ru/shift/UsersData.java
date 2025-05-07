package ru.shift;

import java.util.List;

public class UsersData {
    private List<String> users;

    public UsersData() {}

    public UsersData(List<String> users) {
        this.users = users;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
