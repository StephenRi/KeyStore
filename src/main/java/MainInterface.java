import com.alibaba.fastjson.JSONObject;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.apache.commons.io.FileUtils;
import utils.AESUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Vector;

public class MainInterface {
    private String lastLogged = "";
    private String userName = "";
    private String password = "";

    private JFrame frame;
    private JPanel panel1;
    private JList resultList;
    private JTextField searchTextField;
    private JButton clearButton;
    private JButton searchButton;
    private JButton viewAllButton;

    private JMenuBar menuBar;
    private JMenu logMenu;
    private JMenu newMenu;
    private JMenu viewMenu;
    private JMenuItem loginMenuItem;
    private JMenuItem logupMenuItem;
    private JMenuItem newWebMenuItem;
    private JMenuItem newCardMenuItem;
    private JMenuItem viewAllMenuItem;
    private JMenuItem viewWebMenuItem;
    private JMenuItem viewCardMenuItem;

    private MainInterfaceActionListener actionListener;
    private LoginInterface loginInterface;
    private LogupInterface logupInterface;
    private NewWebInterface newWebInterface;
    private ViewWebInterface viewWebInterface;
    private NewCardInterface newCardInterface;
    private ViewCardInterface viewCardInterface;
    private JSONObject allWebJson;
    private JSONObject allCardJson;

    public MainInterface() {
        actionListener = new MainInterfaceActionListener();

        $$$setupUI$$$();
        viewAllButton.addActionListener(actionListener);
        clearButton.addActionListener(actionListener);
        searchButton.addActionListener(actionListener);
        resultList.setFixedCellHeight(50);
        resultList.setCellRenderer(new MyListCellRenderer());
        resultList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList list = (JList) e.getSource();
                if (e.getClickCount() == 2) {
                    JSONObject json = (JSONObject) list.getSelectedValue();
                    if (json.getString("kind").equals("Web")) {
                        viewWebInterface = new ViewWebInterface(MainInterface.this, json);
                        viewWebInterface.init();
                        return;
                    }
                    if (json.getString("kind").equals("Card")) {
                        viewCardInterface = new ViewCardInterface(MainInterface.this, json);
                        viewCardInterface.init();
                        return;
                    }
                }
            }
        });
    }

    public void init() {
        frame = new JFrame("Key Store");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //主界面关闭时，把数据写入文件中
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                //未登录用户不能修改文件
                if (userName.length() == 0) {
                    return;
                } else {
                    try {
                        saveJsonToFile();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "write data file failed", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
        });

        //菜单
        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        logMenu = new JMenu("Log");
        newMenu = new JMenu("New");
        viewMenu = new JMenu("View");

        menuBar.add(logMenu);
        menuBar.add(newMenu);
        menuBar.add(viewMenu);

        loginMenuItem = new JMenuItem("Log In");
        logupMenuItem = new JMenuItem("Log Up");
        newWebMenuItem = new JMenuItem("New Web");
        newCardMenuItem = new JMenuItem("New Card");
        viewAllMenuItem = new JMenuItem("View All");
        viewWebMenuItem = new JMenuItem("View Web");
        viewCardMenuItem = new JMenuItem("View Card");

        logMenu.add(loginMenuItem);
        logMenu.add(logupMenuItem);
        newMenu.add(newWebMenuItem);
        newMenu.addSeparator();
        newMenu.add(newCardMenuItem);
        viewMenu.add(viewAllMenuItem);
        viewMenu.addSeparator();
        viewMenu.add(viewWebMenuItem);
        viewMenu.add(viewCardMenuItem);

        //为菜单添加事件监听
        loginMenuItem.addActionListener(actionListener);
        logupMenuItem.addActionListener(actionListener);
        newWebMenuItem.addActionListener(actionListener);
        newCardMenuItem.addActionListener(actionListener);
        viewAllMenuItem.addActionListener(actionListener);
        viewWebMenuItem.addActionListener(actionListener);
        viewCardMenuItem.addActionListener(actionListener);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //全局数据
        allWebJson = new JSONObject();
        allCardJson = new JSONObject();
    }

    public void reStart() throws Exception {
        saveJsonToFile();
        userName = "";
        password = "";
        allWebJson = new JSONObject();
        allCardJson = new JSONObject();
        resultList.setListData(new Vector());
    }

    public void saveJsonToFile() throws Exception {
        File webFile = getFile("web");
        File cardFile = getFile("card");
        writeJsonToFile(webFile, allWebJson);
        writeJsonToFile(cardFile, allCardJson);
    }

    public JButton getViewAllButton() {
        return viewAllButton;
    }

    public JMenuItem getViewWebMenuItem() {
        return viewWebMenuItem;
    }

    public JMenuItem getViewCardMenuItem() {
        return viewCardMenuItem;
    }

    public String getLastLogged() {
        return lastLogged;
    }

    public void setLastLogged(String lastLogged) {
        this.lastLogged = lastLogged;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public JSONObject getAllWebJson() {
        return allWebJson;
    }

    public void setAllWebJson(JSONObject json) {
        this.allWebJson = json;
    }

    public JSONObject getAllCardJson() {
        return allCardJson;
    }

    public void setAllCardJson(JSONObject json) {
        this.allCardJson = json;
    }

    public String getJarDir() {
        return System.getProperty("user.dir");
    }

    public File getMetaDataFile() {
        String metaDataFileName = getJarDir() + File.separator + "user" + File.separator + "metadata";
        File metaDataFile = new File(metaDataFileName);
        return metaDataFile;
    }

    public boolean writeLastLoggedToFile() {
        File metaDataFile = getMetaDataFile();
        JSONObject metaDataJson = new JSONObject();
        try {

            if (!metaDataFile.exists()) {
                metaDataFile.createNewFile();
            } else {
                String metaDataJsonStr = FileUtils.readFileToString(metaDataFile);
                metaDataJson = JSONObject.parseObject(metaDataJsonStr);
            }
            metaDataJson.put("lastLogged", lastLogged);
            FileUtils.writeStringToFile(metaDataFile, metaDataJson.toString());
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "write metadata file failed", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public String getUserDirName() {
        String currentDir = getJarDir();
        String userDirName = currentDir + File.separator + "user" + File.separator + getUserName();
        return userDirName;
    }

    /**
     * 得到数据文件
     *
     * @param kind: web / card
     * @return
     */
    public File getFile(String kind) {
        String fileName = getUserDirName() + File.separator + kind.toLowerCase();
        File file = new File(fileName);
        return file;
    }

    /**
     * 从数据文件中读取，解密，转换成json
     *
     * @param kind: web / card
     * @return
     */
    public JSONObject getJsonFromFile(String kind) throws Exception {
        File file = getFile(kind);
        String aesKey = AESUtil.generateKey(password);

        String encryptedJsonStr = FileUtils.readFileToString(file);
        String jsonStr = AESUtil.decrypt(aesKey, encryptedJsonStr);
        JSONObject json = JSONObject.parseObject(jsonStr);
        return json;
    }

    /**
     * 把json加密写入到数据文件
     *
     * @param file
     * @param json
     * @return
     */
    public void writeJsonToFile(File file, JSONObject json) throws Exception {
        String aesKey = AESUtil.generateKey(password);
        String jsonStr = json.toString();

        String encryptedJsonStr = AESUtil.encrypt(aesKey, jsonStr);
        FileUtils.writeStringToFile(file, encryptedJsonStr);
        return;
    }

    public class MyListCellRenderer extends JLabel implements ListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JSONObject jsonObject = (JSONObject) value;
            String kind = (jsonObject).getString("kind");

            String beginHtml = "<html>";
            String endHtml = "</html>";
            String wrapHtml = "<br>";
            String indentHtml = "&emsp;";
            String masterFont = "<font size=\"5\" color=\"black\">";
            String selectedMasterFont = "<font size=\"5\" color=\"blue\">";
            String slaveFont = "<font size=\"3\" color=\"gray\">";
            String selectedSlaveFont = "<font size=\"3\" color=\"blue\">";
            String endFont = "</font>";
            String content = "";

            //title
            if (isSelected) {
                content = beginHtml + selectedMasterFont + jsonObject.getString("title") + endFont + wrapHtml;
            } else {
                content = beginHtml + masterFont + jsonObject.getString("title") + endFont + wrapHtml;
            }

            //如果是web，显示UserName和MailBox
            if (kind.equals("Web")) {
                if (isSelected) {
                    if (jsonObject.containsKey("userName")) {
                        content += selectedSlaveFont + indentHtml + jsonObject.getString("userName") + endFont;
                    }
                    if (jsonObject.containsKey("mailBox")) {
                        content += selectedSlaveFont + indentHtml + jsonObject.getString("mailBox") + endFont;
                    }
                } else {
                    if (jsonObject.containsKey("userName")) {
                        content += slaveFont + indentHtml + jsonObject.getString("userName") + endFont;
                    }
                    if (jsonObject.containsKey("mailBox")) {
                        content += slaveFont + indentHtml + jsonObject.getString("mailBox") + endFont;
                    }
                }
            }

            //如果是card，显示No.和Bank
            if (kind.equals("Card")) {
                if (isSelected) {
                    content += selectedSlaveFont + indentHtml + jsonObject.getString("no") + endFont;
                    content += selectedSlaveFont + indentHtml + jsonObject.getString("bank") + endFont;
                } else {
                    content += slaveFont + indentHtml + jsonObject.getString("no") + endFont;
                    content += slaveFont + indentHtml + jsonObject.getString("bank") + endFont;
                }
            }

            content += endHtml;
            this.setText(content);
            return this;
        }
    }

    public class MainInterfaceActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Log In")) {
                loginInterface = new LoginInterface(MainInterface.this);
                loginInterface.init();
                return;
            }
            if (e.getActionCommand().equals("Log Up")) {
                logupInterface = new LogupInterface(MainInterface.this);
                logupInterface.init();
                return;
            }

            //if not logged in, stop
            if (MainInterface.this.getUserName().length() == 0) {
                JOptionPane.showMessageDialog(null, "You need to log in to continue", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (e.getActionCommand().equals("New Web")) {
                MainInterface.this.newWebInterface = new NewWebInterface(MainInterface.this);
                MainInterface.this.newWebInterface.init();
                return;
            }
            if (e.getActionCommand().equals("New Card")) {
                MainInterface.this.newCardInterface = new NewCardInterface(MainInterface.this);
                MainInterface.this.newCardInterface.init();
                return;
            }
            if (e.getActionCommand().equals("View All")) {
                if (MainInterface.this.allWebJson.isEmpty() && MainInterface.this.allCardJson.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "no data exists", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                } else {
                    Vector<JSONObject> jsonList = new Vector<JSONObject>();
                    for (Map.Entry<String, Object> entry : MainInterface.this.allWebJson.entrySet()) {
                        JSONObject webJson = (JSONObject) entry.getValue();
                        jsonList.add(webJson);
                    }
                    for (Map.Entry<String, Object> entry : MainInterface.this.allCardJson.entrySet()) {
                        JSONObject cardJson = (JSONObject) entry.getValue();
                        jsonList.add(cardJson);
                    }
                    Collections.sort(jsonList, new JsonComparator());
                    MainInterface.this.resultList.setListData(jsonList);
                }
                return;
            }
            if (e.getActionCommand().equals("View Web")) {
                if (MainInterface.this.allWebJson.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "no web exists", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                } else {
                    Vector<JSONObject> webJsonList = new Vector<JSONObject>();
                    for (Map.Entry<String, Object> entry : MainInterface.this.allWebJson.entrySet()) {
                        JSONObject webJson = (JSONObject) entry.getValue();
                        webJsonList.add(webJson);
                    }
                    Collections.sort(webJsonList, new JsonComparator());
                    MainInterface.this.resultList.setListData(webJsonList);
                }
                return;
            }
            if (e.getActionCommand().equals("View Card")) {
                if (MainInterface.this.allCardJson.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "no card exists", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                } else {
                    Vector<JSONObject> cardJsonList = new Vector<JSONObject>();
                    for (Map.Entry<String, Object> entry : MainInterface.this.allCardJson.entrySet()) {
                        JSONObject cardJson = (JSONObject) entry.getValue();
                        cardJsonList.add(cardJson);
                    }
                    Collections.sort(cardJsonList, new JsonComparator());
                    MainInterface.this.resultList.setListData(cardJsonList);
                }
                return;
            }
            if (e.getActionCommand().equals("Clear")) {
                MainInterface.this.searchTextField.setText("");
                return;
            }
            if (e.getActionCommand().equals("Search")) {
                String searchStr = MainInterface.this.searchTextField.getText();
                Vector<JSONObject> jsonList = new Vector<JSONObject>();
                for (Map.Entry<String, Object> entry : MainInterface.this.allWebJson.entrySet()) {
                    JSONObject webJson = (JSONObject) entry.getValue();
                    if (webJson.toString().contains(searchStr)) {
                        jsonList.add(webJson);
                    }
                }
                for (Map.Entry<String, Object> entry : MainInterface.this.allCardJson.entrySet()) {
                    JSONObject cardJson = (JSONObject) entry.getValue();
                    if (cardJson.toString().contains(searchStr)) {
                        jsonList.add(cardJson);
                    }
                }
                Collections.sort(jsonList, new JsonComparator());
                MainInterface.this.resultList.setListData(jsonList);
            }
        }
    }

    public class JsonComparator implements Comparator<JSONObject> {
        public int compare(JSONObject o1, JSONObject o2) {
            String title1 = o1.getString("title");
            String title2 = o2.getString("title");
            return title1.compareTo(title2);
        }
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
        panel1.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(600, 400), null, 0, false));
        resultList = new JList();
        scrollPane1.setViewportView(resultList);
        searchTextField = new JTextField();
        searchTextField.setText("");
        searchTextField.setToolTipText("Search Everything");
        panel1.add(searchTextField, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        clearButton = new JButton();
        clearButton.setText("Clear");
        panel1.add(clearButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        searchButton = new JButton();
        searchButton.setSelected(false);
        searchButton.setText("Search");
        panel1.add(searchButton, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        viewAllButton = new JButton();
        viewAllButton.setText("View All");
        panel1.add(viewAllButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
