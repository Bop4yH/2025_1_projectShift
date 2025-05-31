package ru.shift.view.format;

import javax.swing.text.SimpleAttributeSet;

public record ChatMessageView(String sender, String text, String formattedTime,
                              SimpleAttributeSet style) {

}

