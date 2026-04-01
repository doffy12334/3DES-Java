package triple_des;

import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.datatransfer.StringSelection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class MainUI extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainUI.class.getName());
    private static final int WARM_UP_ROUNDS = 100;
    private static final double NS_PER_SECOND = 1_000_000_000.0;
    private static final double BYTES_PER_MB = 1024.0 * 1024.0;

    /**
     * Creates new form MainUI
     */
    public MainUI() {
        initComponents();
        initializeUi();
    }

    private void initializeUi() {
        setSize(new Dimension(940, 660));

        // Keep a fixed-size window to avoid layout shifts after showing results.
        setResizable(false);
        setMinimumSize(getSize());
        setMaximumSize(getSize());

        ButtonGroup group = new ButtonGroup();
        group.add(jRadioButton1);
        group.add(jRadioButton2);
        jRadioButton1.setSelected(true);

        // Attach listeners here to stay robust even if GUI builder rewrites initComponents.
        for (java.awt.event.ActionListener al : jButton1.getActionListeners()) {
            jButton1.removeActionListener(al);
        }
        jButton1.addActionListener(this::jButton1ActionPerformed);

        for (java.awt.event.ActionListener al : jButton2.getActionListeners()) {
            jButton2.removeActionListener(al);
        }
        jButton2.addActionListener(this::jButton2ActionPerformed);

        for (java.awt.event.ActionListener al : jButton4.getActionListeners()) {
            jButton4.removeActionListener(al);
        }
        jButton4.addActionListener(this::jButton4ActionPerformed);

        for (java.awt.event.ActionListener al : jButton3.getActionListeners()) {
            jButton3.removeActionListener(al);
        }
        jButton3.addActionListener(this::jButton3ActionPerformed);

        for (java.awt.event.ActionListener al : jRadioButton2.getActionListeners()) {
            jRadioButton2.removeActionListener(al);
        }
        jRadioButton2.addActionListener(this::jRadioButton2ActionPerformed);

        jTextField3.setText("");
        jTextField4.setText("");
        jTextField1.setText("");
        jTextField4.setEditable(false);
        jTextField1.setEditable(false);
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jTextField3.setLineWrap(true);
        jTextField3.setWrapStyleWord(true);
        jTextField4.setLineWrap(true);
        jTextField4.setWrapStyleWord(true);
        jTextField1.setLineWrap(true);
        jTextField1.setWrapStyleWord(true);
        jPanel3.setPreferredSize(new Dimension(400, 260));
        jPanel4.setPreferredSize(new Dimension(430, 260));
        jPanel5.setPreferredSize(new Dimension(430, 110));
        jTextField4.setBackground(new java.awt.Color(245, 245, 245));
        jTextField1.setBackground(new java.awt.Color(245, 245, 245));
        jLabel13.setVisible(false);
        jLabel14.setVisible(false);
        jTextField3.setCaretPosition(0);
        jTextField4.setCaretPosition(0);
        jTextField1.setCaretPosition(0);
        jTextField3.requestFocusInWindow();
        jLabel7.setText("Mã hóa: -");
        jLabel8.setText("Giải mã: -");
        jLabel4.setText("So sánh: -");
        jLabel13.setText("-");
        updateModeUi();
    }

    private static double toSeconds(long nanos) {
        return nanos / NS_PER_SECOND;
    }

    private static String formatSeconds(double seconds) {
        return String.format("%.6f giây", seconds);
    }

    private static String formatSpeed(double dataSizeBytes, double seconds) {
        if (seconds <= 0.0) {
            return "N/A";
        }
        double mb = dataSizeBytes / BYTES_PER_MB;
        return String.format("%.2f MB/s", mb / seconds);
    }

    private static byte[] parseCipherBytesForStats(String cipherInput) {
        String normalized = cipherInput.replaceAll("\\s+", "");
        boolean looksHex = normalized.matches("(?i)[0-9a-f]+") && normalized.length() % 2 == 0;

        if (looksHex) {
            byte[] out = new byte[normalized.length() / 2];
            for (int i = 0; i < normalized.length(); i += 2) {
                int hi = Character.digit(normalized.charAt(i), 16);
                int lo = Character.digit(normalized.charAt(i + 1), 16);
                if (hi == -1 || lo == -1) {
                    throw new IllegalArgumentException("Dữ liệu vào không phải HEX hoặc Base64 hợp lệ.");
                }
                out[i / 2] = (byte) ((hi << 4) + lo);
            }
            return out;
        }

        try {
            return Base64.getDecoder().decode(normalized);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Dữ liệu vào không phải HEX hoặc Base64 hợp lệ.");
        }
    }

    private void warmUpEncryptDecrypt(String plainText, String masterKey) {
        for (int i = 0; i < WARM_UP_ROUNDS; i++) {
            String warmCipher = Main.encryptToHex(plainText, masterKey);
            Main.decryptAuto(warmCipher, masterKey);
        }
    }

    private void warmUpDecryptOnly(String masterKey) {
        String warmCipher = Main.encryptToHex("Warm up 3DES text", masterKey);
        for (int i = 0; i < WARM_UP_ROUNDS; i++) {
            Main.decryptAuto(warmCipher, masterKey);
        }
    }

    private void updateModeUi() {
        if (jRadioButton1.isSelected()) {
            jLabel5.setText("Dữ liệu vào (text)                                             >= 15 kí tự");
            jLabel6.setText("Kết quả mã hóa:");
            jLabel10.setText("Hex:");
            jLabel11.setText("Base64:");
        } else {
            jLabel5.setText("Dữ liệu vào (Hex hoặc Base64)");
            jLabel6.setText("Kết quả giải mã:");
            jLabel10.setText("Text:");
            jLabel11.setText("Base64:");
        }
    }

    private String requireMasterKey() {
        String key = jTextField2.getText();
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Khóa không được để trống.");
        }
        if (key.length() < Main.getMinMasterKeyLength() || key.length() > Main.getMaxMasterKeyLength()) {
            throw new IllegalArgumentException(
                "Khóa phải có độ dài từ " + Main.getMinMasterKeyLength() + " đến " + Main.getMaxMasterKeyLength() + " ký tự."
            );
        }
        return key;
    }

    private void doEncrypt() {
        String plainText = jTextField3.getText();
        String masterKey = requireMasterKey();

        warmUpEncryptDecrypt(plainText, masterKey);

        long startEncrypt = System.nanoTime();
        String ciphertextHex = Main.encryptToHex(plainText, masterKey);
        long encryptNs = System.nanoTime() - startEncrypt;

        String ciphertextBase64 = Main.hexToBase64(ciphertextHex);

        long startDecrypt = System.nanoTime();
        String decryptedText = Main.decryptAuto(ciphertextHex, masterKey);
        long decryptNs = System.nanoTime() - startDecrypt;
        boolean verifyOk = plainText.equals(decryptedText);

        double encTimeSec = toSeconds(encryptNs);
        double decTimeSec = toSeconds(decryptNs);
        double deltaSec = Math.abs(encTimeSec - decTimeSec);
        double ratio = (Math.min(encTimeSec, decTimeSec) > 0.0)
            ? Math.max(encTimeSec, decTimeSec) / Math.min(encTimeSec, decTimeSec)
            : 0.0;

        byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);
        int cipherByteLength = ciphertextHex.replaceAll("\\s+", "").length() / 2;
        String encSpeed = formatSpeed(plainBytes.length, encTimeSec);
        String decSpeed = formatSpeed(cipherByteLength, decTimeSec);

        String compareText;
        if (encTimeSec == decTimeSec) {
            compareText = "Mã hóa và giải mã có tốc độ tương đương.";
        } else if (encTimeSec < decTimeSec) {
            compareText = String.format("Mã hóa nhanh hơn giải mã %.2f lần.", ratio);
        } else {
            compareText = String.format("Giải mã nhanh hơn mã hóa %.2f lần.", ratio);
        }

        jTextField4.setText(ciphertextHex);
        jTextField1.setText(ciphertextBase64);
        jTextField4.setCaretPosition(0);
        jTextField1.setCaretPosition(0);
        jLabel7.setText("Mã hóa: " + formatSeconds(encTimeSec) + " | Tốc độ: " + encSpeed);
        jLabel8.setText("Giải mã: " + formatSeconds(decTimeSec) + " | Tốc độ: " + decSpeed);
        jLabel4.setText(compareText);
    }

    private void doDecrypt() {
        String cipherHex = jTextField3.getText();
        String masterKey = requireMasterKey();

        byte[] cipherBytes = parseCipherBytesForStats(cipherHex);
        warmUpDecryptOnly(masterKey);

        long startDecrypt = System.nanoTime();
        String decryptedText = Main.decryptAuto(cipherHex, masterKey);
        long decryptNs = System.nanoTime() - startDecrypt;
        double decTimeSec = toSeconds(decryptNs);
        String decSpeed = formatSpeed(cipherBytes.length, decTimeSec);

        jTextField4.setText(decryptedText);
        jTextField1.setText("");
        jTextField4.setCaretPosition(0);
        jTextField1.setCaretPosition(0);
        jLabel7.setText("Mã hóa: -");
        jLabel8.setText("Giải mã: " + formatSeconds(decTimeSec) + " | Tốc độ: " + decSpeed);
        jLabel4.setText("So sánh: Chỉ đo giải mã ở chế độ hiện tại.");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPasswordField1 = new javax.swing.JPasswordField();
        jTextField5 = new javax.swing.JTextField();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextField3 = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextField1 = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextField4 = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();

        jPasswordField1.setText("jPasswordField1");

        jTextField5.setText("jTextField5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(13, 17, 23));

        jRadioButton1.setText("Mã Hóa");
        jRadioButton1.addActionListener(this::jRadioButton1ActionPerformed);

        jRadioButton2.setText("Giải Mã");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("3DES");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel3.setText("Khóa 3DES:");

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jButton1.setText("Thực hiện");

        jButton2.setText("Sao chép KQ");

        jButton4.setText("Thông tin");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addGap(17, 17, 17))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton4))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel5.setText("Dữ liệu vào (text)                                             >= 15 kí tự");

        jTextField3.setColumns(20);
        jTextField3.setRows(5);
        jScrollPane3.setViewportView(jTextField3);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 102)));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Kết quả:");

        jTextField1.setColumns(20);
        jTextField1.setRows(5);
        jScrollPane1.setViewportView(jTextField1);

        jLabel10.setText("Hex:");

        jLabel11.setText("Base64:");

        jTextField4.setColumns(20);
        jTextField4.setRows(5);
        jScrollPane2.setViewportView(jTextField4);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(6, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 51)));

        jLabel7.setText("Mã hóa: ");

        jLabel8.setText("Giải mã:");

        jLabel4.setText("Mã hóa tốc độ:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel4))
                .addContainerGap(240, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Triple Data Encryption Standard");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel12.setText("Cơ sở bảo mật & An toàn thông tin");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Thời gian thực hiện:");

        jLabel14.setText(".");

        jTextField2.addActionListener(this::jTextField2ActionPerformed);

        jButton3.setText("Tạo khóa");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(44, 44, 44)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(307, 307, 307)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(478, 478, 478)
                        .addComponent(jLabel2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioButton1)
                            .addComponent(jRadioButton2)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel12)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3))
                        .addGap(32, 32, 32)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        String[] options = {"16 ký tự", "24 ký tự", "Hủy"};
        int choice = JOptionPane.showOptionDialog(
            this,
            "Chọn độ dài khóa ngẫu nhiên",
            "Tạo khóa 3DES",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[1]
        );

        if (choice == 0) {
            jTextField2.setText(Main.generateRandomMasterKey(16));
        } else if (choice == 1) {
            jTextField2.setText(Main.generateRandomMasterKey(24));
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        JOptionPane.showMessageDialog(
            this,
            "MÔ TẢ CHƯƠNG TRÌNH VÀ THUẬT TOÁN 3DES\n\n"
            + "1) Luồng hoạt động của chương trình:\n"
            + "- Chế độ Mã hóa: nhập bản rõ (>= 15 ký tự) và khóa K dạng plaintext.\n"
            + "- Chương trình chuyển bản rõ sang UTF-8 bytes, thêm PKCS7 padding để đủ bội số 8 byte.\n"
            + "- Kết quả hiển thị ở 2 định dạng: Hex và Base64.\n"
            + "- Chế độ Giải mã: nhập Hex hoặc Base64, chương trình tự nhận dạng, giải mã và trả về text UTF-8.\n"
            + "- Nếu dữ liệu không hợp lệ hoặc sai độ dài khối DES (8 byte), hệ thống sẽ báo lỗi.\n\n"
            + "2) Cách xử lý khóa 3DES trong chương trình (không dùng SHA-256):\n"
            + "- Khóa nhập vào được chuyển UTF-8 bytes và chuẩn hóa về đúng 24 byte.\n"
            + "- Trường hợp 16 byte: áp dụng 2-key 3DES theo mẫu K1|K2|K1.\n"
            + "- Trường hợp <24 byte: lặp lại bytes từ đầu cho đến đủ 24 byte.\n"
            + "- Trường hợp >24 byte: cắt lấy 24 byte đầu.\n"
            + "- Thiết lập odd parity cho từng byte DES và tách thành K1/K2/K3 (mỗi khóa 8 byte).\n\n"
            + "3) Mô tả thuật toán DES bên trong mỗi bước:\n"
            + "- Sinh 16 khóa con từ mỗi khóa chính (PC-1 -> dịch trái C/D theo lịch SHIFTS -> PC-2).\n"
            + "- Mỗi khối 64-bit đi qua: IP -> 16 vòng Feistel -> hoán đổi R16/L16 -> IP^-1.\n"
            + "- Trong vòng Feistel: mở rộng E(R), XOR khóa con, S-box, rồi hoán vị P.\n\n"
            + "4) Mô tả chu trình Triple DES (EDE):\n"
            + "- Mã hóa: E(K1) -> D(K2) -> E(K3).\n"
            + "- Giải mã: D(K3) -> E(K2) -> D(K1).\n"
            + "- Dữ liệu được xử lý theo từng khối 8 byte cho đến hết thông điệp.",
            "Thông tin",
            JOptionPane.INFORMATION_MESSAGE
        );
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        updateModeUi();
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField1.setText("");
        jTextField3.setCaretPosition(0);
        jTextField4.setCaretPosition(0);
        jTextField1.setCaretPosition(0);
        jTextField3.requestFocusInWindow();
        jLabel7.setText("Mã hóa: -");
        jLabel8.setText("Giải mã: -");
        jLabel4.setText("So sánh: -");

    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        updateModeUi();
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField1.setText("");
        jTextField3.setCaretPosition(0);
        jTextField4.setCaretPosition(0);
        jTextField1.setCaretPosition(0);
        jTextField3.requestFocusInWindow();
        jLabel7.setText("Mã hóa: -");
        jLabel8.setText("Giải mã: -");
        jLabel4.setText("So sánh: -");
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (jRadioButton1.isSelected()) {
                doEncrypt();
            } else {
                doDecrypt();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi xử lý", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        String textMain = jTextField4.getText();
        String textExtra = jTextField1.getText();
        if ((textMain == null || textMain.isBlank()) && (textExtra == null || textExtra.isBlank())) {
            JOptionPane.showMessageDialog(this, "Chưa có kết quả để sao chép.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] options;
        if (jRadioButton1.isSelected()) {
            options = new String[] {"Sao chép Hex", "Sao chép Base64", "Sao chép cả 2", "Hủy"};
        } else {
            options = new String[] {"Sao chép Text", "Hủy"};
        }

        int choice = JOptionPane.showOptionDialog(
            this,
            "Chọn phần kết quả cần sao chép",
            "Sao chép kết quả",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        String contentToCopy = null;
        if (jRadioButton1.isSelected()) {
            if (choice == 0) {
                contentToCopy = textMain;
            } else if (choice == 1) {
                contentToCopy = textExtra;
            } else if (choice == 2) {
                contentToCopy = "HEX:\n" + textMain + "\n\nBASE64:\n" + textExtra;
            }
        } else {
            if (choice == 0) {
                contentToCopy = textMain;
            }
        }

        if (contentToCopy != null) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(contentToCopy), null);
            JOptionPane.showMessageDialog(this, "Đã sao chép kết quả.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainUI().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextArea jTextField3;
    private javax.swing.JTextArea jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
