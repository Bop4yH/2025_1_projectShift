package ru.shift;

public enum MessageType {
    TEXT,
    USER_NAME,       // обычное сообщение
    USERS,       // список пользователей
    ERROR,       // сообщение об ошибке
    LOGIN,       // логин пользователя
    NAME_OK,    // логин успешен
    NAME_TAKEN,
    NAME_EMPTY,
    ENTER_NAME// имя пустое (можно объединить с ERROR)
}
