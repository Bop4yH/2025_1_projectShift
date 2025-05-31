package ru.shift.common;

import java.util.List;

public record UsersData(List<String> users) implements MessageData {

}
