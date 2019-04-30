import com.alibaba.fastjson.JSONObject;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import constant.FileConstant;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LogupInterface {
    private JFrame frame;
    private JPanel panel1;
    private JTextField userNameTextField;
    private JTextField passwordTextField;
    private JTextField confirmPassTextField;
    private JButton logupButton;

    private MainInterface mainInterface;
    private LogupInterfaceActionListener logupInterfaceActionListener;

    public LogupInterface(MainInterface mainInterface) {
        this.mainInterface = mainInterface;

        logupInterfaceActionListener = new LogupInterfaceActionListener();
        logupButton.addActionListener(logupInterfaceActionListener);
    }

    public void init() {
        frame = new JFrame("Log Up");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public class LogupInterfaceActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Logup")) {
                if (LogupInterface.this.mainInterface.getUserName().length() != 0) {
                    try {
                        LogupInterface.this.mainInterface.reStart();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "write file failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                String userName = userNameTextField.getText();
                String password = passwordTextField.getText();
                String confirmPass = confirmPassTextField.getText();

                String currentDir = System.getProperty("user.dir");
                String userDirName = currentDir + File.separator + FileConstant.FILE_NAME_USER + File.separator + userName;

                File userDir = new File(userDirName);
                if (userDir.exists()) {
                    JOptionPane.showMessageDialog(null, userName + " already logup", "Warning", JOptionPane.WARNING_MESSAGE);
                    userNameTextField.setText("");
                    return;
                }
                if (!password.equals(confirmPass)) {
                    JOptionPane.showMessageDialog(null, "Passwords not same", "Warning", JOptionPane.WARNING_MESSAGE);
                    passwordTextField.setText("");
                    confirmPassTextField.setText("");
                    return;
                }

                if (userDir.mkdirs()) {
                    JSONObject userJson = new JSONObject();
                    userJson.put("userName", userName);
                    String passwordHash = "";
                    try {
                        passwordHash = new String(MessageDigest.getInstance("MD5").digest(password.getBytes()));
                    } catch (NoSuchAlgorithmException ex) {
                        JOptionPane.showMessageDialog(null, "NoSuchAlgorithm: MD5", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    userJson.put("password", passwordHash);
                    String userFileName = userDirName + File.separator + FileConstant.FILE_NAME_USERDATA;
                    File userFile = new File(userFileName);
                    try {
                        if (userFile.createNewFile()) {
                            FileUtils.writeStringToFile(userFile, userJson.toString());
                            LogupInterface.this.mainInterface.setUserName(userName);
                            LogupInterface.this.mainInterface.setPassword(password);
                            LogupInterface.this.mainInterface.setLastLogged(userName);

                            File webFile = LogupInterface.this.mainInterface.getFile(FileConstant.FILE_NAME_WEB);
                            File cardFile = LogupInterface.this.mainInterface.getFile(FileConstant.FILE_NAME_CARD);
                            if (!webFile.createNewFile() || !cardFile.createNewFile()) {
                                JOptionPane.showMessageDialog(null, "Create data file failed", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            if (!LogupInterface.this.mainInterface.writeLastLoggedToFile()) {
                                return;
                            }

                            JOptionPane.showMessageDialog(null, userName + " logged in", "Welcome", JOptionPane.INFORMATION_MESSAGE);
                            LogupInterface.this.frame.dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "Create userData Failed", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Create userData Failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Logup Failed", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
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
        panel1.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("UserName");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        userNameTextField = new JTextField();
        userNameTextField.setText("");
        panel1.add(userNameTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Password");
        panel1.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        passwordTextField = new JTextField();
        passwordTextField.setText("");
        panel1.add(passwordTextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Confirm Pass");
        panel1.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        confirmPassTextField = new JTextField();
        confirmPassTextField.setText("");
        panel1.add(confirmPassTextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        logupButton = new JButton();
        logupButton.setText("Logup");
        panel1.add(logupButton, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
