package ru.shift.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.shift.ChatMessageData;
import ru.shift.Message;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatClientUI extends JFrame {
    // Константы для размеров окна
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 400;
    private static final int USER_LIST_WIDTH = 150;

    // Константы для форматирования времени
    private static final String TIME_FORMAT = "HH:mm:ss";

    // Константы для диалогов
    private static final String DIALOG_TITLE_AUTH = "Авторизация";
    private static final String DIALOG_TITLE_NAME_ERROR = "Ошибка имени";
    private static final String DIALOG_TITLE_CONFIRM = "Подтверждение";
    private static final String DIALOG_PROMPT_NAME = "Введите имя:";
    private static final String ERROR_APPEND_MESSAGE = "Ошибка при добавлении сообщения: %s";

    private static final ObjectMapper mapper = new ObjectMapper();
    private final JTextPane chatArea = new JTextPane();
    private final JTextField inputField = new JTextField();
    private final DefaultListModel<String> userListModel = new DefaultListModel<>();
    private final JList<String> userList = new JList<>(userListModel);
    private JScrollPane chatScroll;

    public ChatClientUI() {
        initializeWindow();
        createUserListPanel();
        createChatArea();
        createInputPanel();
        setVisible(true);

    }

    private void initializeWindow() {
        setTitle("Chat");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLayout(new BorderLayout());
    }

    private void createUserListPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(USER_LIST_WIDTH, 0));
        leftPanel.add(new JLabel(" Users"), BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(userList), BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);
    }

    private void createChatArea() {
        chatArea.setEditable(false);
        chatArea.setEditorKit(new WrapEditorKit());
        chatScroll = new JScrollPane(chatArea);
        add(chatScroll, BorderLayout.CENTER);
    }

    private void createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
    }

    public void addSendListener(ActionListener listener) {
        inputField.addActionListener(listener);
    }

    public String getInputText() {
        String text = inputField.getText();
        inputField.setText("");
        return text;
    }

    public void appendMessage(Message message) {
        try {
//            MVC-разделение
//            Сейчас UI знает формат сетевого Message, парсит JSON и т.д.
//                    Можно вынести это на уровень «контроллера», а UI передавать уже готовую DTO:
//
//
//            record ChatLine(String sender, String text, Instant time) {}
//            Тогда ChatClientUI становится совсем «тонким» и не зависит от Jackson.
            ChatMessageData data = mapper.convertValue(message.getData(), ChatMessageData.class);
            String sender = data.getSender();
            String text = data.getText();
            long timestamp = message.getTimestamp();

            StyledDocument doc = chatArea.getStyledDocument();
            String time = formatTimestamp(timestamp);
            String fullMessage = createMessageText(time, sender, text);
            SimpleAttributeSet style = createMessageStyle(sender);

            doc.insertString(doc.getLength(), fullMessage, style);
            scrollToBottom();
        } catch (Exception e) {
            System.err.printf((ERROR_APPEND_MESSAGE) + "%n", e.getMessage());
            e.printStackTrace();
        }
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = chatScroll.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    private String formatTimestamp(long timestamp) {
        return Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    private String createMessageText(String time, String sender, String text) {
        return String.format("[%s] %s: %s%n", time, sender, text);
    }

    private SimpleAttributeSet createMessageStyle(String sender) {
        SimpleAttributeSet style = new SimpleAttributeSet();
        if ("Server".equalsIgnoreCase(sender)) {
            StyleConstants.setForeground(style, Color.BLUE);
            StyleConstants.setBold(style, true);
        } else {
            StyleConstants.setForeground(style, Color.BLACK);
        }
        return style;
    }

    public void updateUserList(List<String> users) {
        userListModel.clear();
        users.forEach(userListModel::addElement);
    }

    public String askName() {
        return JOptionPane.showInputDialog(
                null,
                DIALOG_PROMPT_NAME,
                DIALOG_TITLE_AUTH,
                JOptionPane.QUESTION_MESSAGE
        );
    }

    public void showNameError(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                DIALOG_TITLE_NAME_ERROR,
                JOptionPane.ERROR_MESSAGE
        );
    }

    public int showConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(
                null,
                message,
                DIALOG_TITLE_CONFIRM,
                JOptionPane.YES_NO_OPTION
        );
    }

    private static class WrapEditorKit extends StyledEditorKit {
        private final ViewFactory defaultFactory = new WrapColumnFactory();

        @Override
        public ViewFactory getViewFactory() {
            return defaultFactory;
        }
    }

    private static class WrapColumnFactory implements ViewFactory {
        @Override
        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                switch (kind) {
                    case AbstractDocument.ContentElementName:
                        return new WrapLabelView(elem);
                    case AbstractDocument.ParagraphElementName:
                        return new ParagraphView(elem);
                    case AbstractDocument.SectionElementName:
                        return new BoxView(elem, View.Y_AXIS);
                    case StyleConstants.ComponentElementName:
                        return new ComponentView(elem);
                    case StyleConstants.IconElementName:
                        return new IconView(elem);
                }
            }
            return new LabelView(elem);
        }
    }

    private static class WrapLabelView extends LabelView {
        public WrapLabelView(Element elem) {
            super(elem);
        }

        @Override
        public float getMinimumSpan(int axis) {
            switch (axis) {
                case View.X_AXIS:
                    return 0;
                case View.Y_AXIS:
                    return super.getMinimumSpan(axis);
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        }
    }
}