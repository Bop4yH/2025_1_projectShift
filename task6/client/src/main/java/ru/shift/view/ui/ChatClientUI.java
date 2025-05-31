package ru.shift.view.ui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shift.view.format.ChatMessageView;
import ru.shift.view.text.WrapEditorKit;


public class ChatClientUI extends JFrame {
   private static final Logger log = LoggerFactory.getLogger(ChatClientUI.class);
   private static final int WINDOW_WIDTH = 600;
   private static final int WINDOW_HEIGHT = 400;
   private static final int USER_LIST_WIDTH = 150;

   private static final String DIALOG_TITLE_AUTH = "Авторизация";
   private static final String DIALOG_TITLE_ERROR = "Ошибка";
   private static final String DIALOG_PROMPT_NAME = "Введите имя:";

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

   public String pollInputText() {
      String text = inputField.getText();
      inputField.setText("");
      return text;
   }

   public void appendChatLine(ChatMessageView chatLine) {
      try {
         StyledDocument doc = chatArea.getStyledDocument();
         String fullMessage = String.format("[%s] %s: %s%n", chatLine.formattedTime(),
             chatLine.sender(), chatLine.text());
         doc.insertString(doc.getLength(), fullMessage, chatLine.style());
         scrollToBottom();
      } catch (BadLocationException e) {
         log.error("Error during append line", e);
      }
   }

   private void scrollToBottom() {
      SwingUtilities.invokeLater(() -> {
         JScrollBar vertical = chatScroll.getVerticalScrollBar();
         vertical.setValue(vertical.getMaximum());
      });
   }

   public void updateUserList(List<String> users) {
      userListModel.clear();
      users.forEach(userListModel::addElement);
   }

   public String askName() {

      return JOptionPane.showInputDialog(null, DIALOG_PROMPT_NAME, DIALOG_TITLE_AUTH,
          JOptionPane.QUESTION_MESSAGE);
   }

   public void shutdown() {
      dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
   }

   public void showError(String message) {
      JOptionPane.showMessageDialog(null, message, DIALOG_TITLE_ERROR,
          JOptionPane.ERROR_MESSAGE);
   }

   public void onClose(Runnable action) {
      this.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            action.run();
         }
      });
   }
}
