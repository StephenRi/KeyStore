import com.alibaba.fastjson.JSONObject;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import constant.FileConstant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewWebInterface {
    private JFrame frame;
    private JPanel panel1;
    private JTextField titleTextField;
    private JTextField userNameTextField;
    private JTextField mailBoxTextField;
    private JTextField passwordTextField;
    private JButton newButton;
    private JTextArea remarkTextArea;
    private MainInterface mainInterface;
    private NewWebInterfaceActionListener actionListener;

    public NewWebInterface(MainInterface mainInterface) {
        this.mainInterface = mainInterface;

        actionListener = new NewWebInterfaceActionListener();
        newButton.addActionListener(actionListener);
    }

    public void init() {
        frame = new JFrame("New Web");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public class NewWebInterfaceActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("New")) {
                String title = NewWebInterface.this.titleTextField.getText();
                String userName = NewWebInterface.this.userNameTextField.getText();
                String mailBox = NewWebInterface.this.mailBoxTextField.getText();
                String password = NewWebInterface.this.passwordTextField.getText();
                String remark = NewWebInterface.this.remarkTextArea.getText();

                if (title.length() == 0 || password.length() == 0) {
                    JOptionPane.showMessageDialog(null, "Title and Password are necessary", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                JSONObject webJson = new JSONObject();
                webJson.put(FileConstant.JSON_ALL_KEY_NAME_TITLE, title);
                webJson.put(FileConstant.JSON_WEB_KEY_NAME_USER_NAME, userName);
                webJson.put(FileConstant.JSON_WEB_KEY_NAME_MAIL_BOX, mailBox);
                webJson.put(FileConstant.JSON_WEB_KEY_NAME_PASSWORD, password);
                webJson.put(FileConstant.JSON_WEB_KEY_NAME_REMARK, remark);
                webJson.put(FileConstant.JSON_ALL_KEY_NAME_KIND, FileConstant.JSON_WEB_VALUE_KIND);

                JSONObject allWebJson = NewWebInterface.this.mainInterface.getAllWebJson();
                if (allWebJson.containsKey(title)) {
                    JOptionPane.showMessageDialog(null, title + " has existed", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                } else {
                    allWebJson.put(title, webJson);
                    NewWebInterface.this.mainInterface.getViewWebMenuItem().doClick();
                    NewWebInterface.this.frame.dispose();
                }
                return;
            }
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Title.");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        titleTextField = new JTextField();
        titleTextField.setEditable(true);
        titleTextField.setEnabled(true);
        titleTextField.setFocusCycleRoot(false);
        panel1.add(titleTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("UserName");
        panel1.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JLabel label3 = new JLabel();
        label3.setText("MailBox");
        panel1.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JLabel label4 = new JLabel();
        label4.setText("Password.");
        panel1.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        userNameTextField = new JTextField();
        panel1.add(userNameTextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mailBoxTextField = new JTextField();
        panel1.add(mailBoxTextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordTextField = new JTextField();
        panel1.add(passwordTextField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Remark");
        panel1.add(label5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        newButton = new JButton();
        newButton.setText("New");
        panel1.add(newButton, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(250, 150), null, 0, false));
        remarkTextArea = new JTextArea();
        scrollPane1.setViewportView(remarkTextArea);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
