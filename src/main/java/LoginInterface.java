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

public class LoginInterface {
    private JFrame frame;
    private JPanel panel1;
    private JTextField userNameTextField;
    private JPasswordField passwordPasswordField;
    private JButton loginButton;

    private MainInterface mainInterface;
    private LoginInterfaceActionListener loginInterfaceActionListener;

    public LoginInterface(MainInterface mainInterface) {
        this.mainInterface = mainInterface;

        loginInterfaceActionListener = new LoginInterfaceActionListener();
        loginButton.addActionListener(loginInterfaceActionListener);
    }

    public void init() {
        frame = new JFrame("Log In");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        setLastLogged();
    }

    public void setLastLogged() {
        File metaDataFile = this.mainInterface.getMetaDataFile();
        if (metaDataFile.exists()) {
            try {
                String metaDataJsonStr = FileUtils.readFileToString(metaDataFile);
                JSONObject metaDataJson = JSONObject.parseObject(metaDataJsonStr);
                if (metaDataJson.containsKey("lastLogged")) {
                    userNameTextField.setText(metaDataJson.getString("lastLogged"));
                }
                return;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "read metadata file failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public class LoginInterfaceActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Login")) {
                //如果是已登陆状态，刷新
                if (LoginInterface.this.mainInterface.getUserName().length() != 0) {
                    try {
                        LoginInterface.this.mainInterface.reStart();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "write file failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                String userName = userNameTextField.getText();
                String password = new String(passwordPasswordField.getPassword());

                String currentDir = System.getProperty("user.dir");
                String userDirName = currentDir + File.separator + FileConstant.FILE_NAME_USER + File.separator + userName;
                File userDir = new File(userDirName);
                if (!userDir.exists()) {
                    JOptionPane.showMessageDialog(null, userName + " not logged up", "Warning", JOptionPane.WARNING_MESSAGE);
                    userNameTextField.setText("");
                    return;
                }

                String userFileName = userDirName + File.separator + FileConstant.FILE_NAME_USERDATA;
                File userFile = new File(userFileName);
                String userJsonStr = "";
                JSONObject userJson = new JSONObject();
                if (!userFile.exists()) {
                    JOptionPane.showMessageDialog(null, "userData not found", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        userJsonStr = FileUtils.readFileToString(userFile);
                        userJson = JSONObject.parseObject(userJsonStr);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Open userData Failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                String savedPasswordHash = userJson.getString("password");

                String passwordHash = "";
                try {
                    passwordHash = new String(MessageDigest.getInstance("MD5").digest(password.getBytes()));
                } catch (NoSuchAlgorithmException ex) {
                    JOptionPane.showMessageDialog(null, "NoSuchAlgorithm: MD5", "Error", JOptionPane.ERROR_MESSAGE);
                }
                if (passwordHash.equals(savedPasswordHash)) {
                    LoginInterface.this.mainInterface.setUserName(userName);
                    LoginInterface.this.mainInterface.setPassword(password);
                    LoginInterface.this.mainInterface.setLastLogged(userName);

                    //记录登陆用户
                    if (!LoginInterface.this.mainInterface.writeLastLoggedToFile()) {
                        return;
                    }

                    //读取用户数据文件到allWebJson等
                    File webFile = LoginInterface.this.mainInterface.getFile(FileConstant.FILE_NAME_WEB);
                    File cardFile = LoginInterface.this.mainInterface.getFile(FileConstant.FILE_NAME_CARD);
                    try {
                        if (webFile.exists()) {
                            JSONObject allWebJson = LoginInterface.this.mainInterface.getJsonFromFile(FileConstant.FILE_NAME_WEB);
                            LoginInterface.this.mainInterface.setAllWebJson(allWebJson);
                        } else {
                            webFile.createNewFile();
                        }
                        if (cardFile.exists()) {
                            JSONObject allCardJson = LoginInterface.this.mainInterface.getJsonFromFile(FileConstant.FILE_NAME_CARD);
                            LoginInterface.this.mainInterface.setAllCardJson(allCardJson);
                        } else {
                            cardFile.createNewFile();
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "read user data failed", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    JOptionPane.showMessageDialog(null, userName + " logged in", "Welcome", JOptionPane.INFORMATION_MESSAGE);
                    LoginInterface.this.frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Password not right", "Warning", JOptionPane.WARNING_MESSAGE);
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
        panel1.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("UserName");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        userNameTextField = new JTextField();
        userNameTextField.setText("");
        panel1.add(userNameTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Password");
        panel1.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        passwordPasswordField = new JPasswordField();
        passwordPasswordField.setText("");
        panel1.add(passwordPasswordField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        loginButton = new JButton();
        loginButton.setText("Login");
        panel1.add(loginButton, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
