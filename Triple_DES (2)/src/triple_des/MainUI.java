package triple_des;

import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.datatransfer.StringSelection;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class MainUI extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainUI.class.getName());

    /**
     * Creates new form MainUI
     */
    public MainUI() {
        initComponents();
        initializeUi();
    }

    private void initializeUi() {
        setSize(new Dimension(860, 660));

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

        for (java.awt.event.ActionListener al : jRadioButton2.getActionListeners()) {
            jRadioButton2.removeActionListener(al);
        }
        jRadioButton2.addActionListener(this::jRadioButton2ActionPerformed);

        jTextField3.setText("");
        jTextField4.setText("");
        jTextField1.setText("");
        jTextField4.setEditable(false);
        jTextField1.setEditable(false);
        jTextField4.setBackground(new java.awt.Color(245, 245, 245));
        jTextField1.setBackground(new java.awt.Color(245, 245, 245));
        jTextField3.setLineWrap(true);
        jTextField3.setWrapStyleWord(true);
        jTextField4.setLineWrap(true);
        jTextField4.setWrapStyleWord(false);
        jTextField1.setLineWrap(true);
        jTextField1.setWrapStyleWord(false);
        jTextField3.setCaretPosition(0);
        jTextField4.setCaretPosition(0);
        jTextField1.setCaretPosition(0);
        jTextField3.requestFocusInWindow();
        jLabel7.setText("Mã hóa: -");
        jLabel8.setText("Giải mã: -");
        jLabel14.setText("Kiểm tra: -");
        updateModeUi();
    }

    private static String formatTimingNanos(long nanos) {
        double ms = nanos / 1_000_000.0;
        return String.format("%.6f ms (%d ns)", ms, nanos);
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
        String key = jTextField2.getText().trim();
        if (key.length() < Main.getMinMasterKeyLength() || key.length() > Main.getMaxMasterKeyLength()) {
            throw new IllegalArgumentException(
                "Khóa gốc phải có độ dài từ " + Main.getMinMasterKeyLength() + " đến " + Main.getMaxMasterKeyLength() + " ký tự."
            );
        }
        return key;
    }

    private void doEncrypt() {
        String plainText = jTextField3.getText();
        String masterKey = requireMasterKey();

        long startEncrypt = System.nanoTime();
        String ciphertextHex = Main.encryptToHex(plainText, masterKey);
        long encryptNs = System.nanoTime() - startEncrypt;

        String ciphertextBase64 = Main.hexToBase64(ciphertextHex);

        long startDecrypt = System.nanoTime();
        String decryptedText = Main.decryptAuto(ciphertextHex, masterKey);
        long decryptNs = System.nanoTime() - startDecrypt;
        boolean verifyOk = plainText.equals(decryptedText);

        jTextField4.setText(ciphertextHex);
        jTextField1.setText(ciphertextBase64);
        jTextField4.setCaretPosition(0);
        jTextField1.setCaretPosition(0);
        jLabel14.setText(String.format("Kiểm tra: %s (đối chiếu sau giải mã)", verifyOk ? "OK" : "FAIL"));
        jLabel7.setText("Mã hóa: " + formatTimingNanos(encryptNs));
        jLabel8.setText("Giải mã: " + formatTimingNanos(decryptNs));
    }

    private void doDecrypt() {
        String cipherHex = jTextField3.getText();
        String masterKey = requireMasterKey();

        long startDecrypt = System.nanoTime();
        String decryptedText = Main.decryptAuto(cipherHex, masterKey);
        long decryptNs = System.nanoTime() - startDecrypt;

        jTextField4.setText(decryptedText);
        jTextField1.setText("");
        jTextField4.setCaretPosition(0);
        jTextField1.setCaretPosition(0);
        jLabel7.setText("Mã hóa: -");
        jLabel8.setText("Giải mã: " + formatTimingNanos(decryptNs));
        jLabel14.setText("Kiểm tra: OK (giải mã thành công)");
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
        jPanel1 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextField3 = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextField1 = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextField4 = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();

        jPasswordField1.setText("jPasswordField1");

        jTextField5.setText("jTextField5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(13, 17, 23));

        jRadioButton1.setText("Mã Hóa");
        jRadioButton1.addActionListener(this::jRadioButton1ActionPerformed);

        jRadioButton2.setText("Giải Mã");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("3DES");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 204, 0)));

        jLabel4.setText("Khóa K (16-24 kí tự)");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel3.setText("Khóa 3DES (tự sinh K1, K2, K3)");

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jButton1.setText("Thực hiện");

        jButton2.setText("Sao chép KQ");

        jButton3.setText("Thông tin");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(30, 30, 30)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel5.setText("Dữ liệu vào (text)                                             >= 15 kí tự");

        jTextField3.setColumns(20);
        jTextField3.setRows(5);
        jScrollPane1.setViewportView(jTextField3);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 102)));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Kết quả:");

        jTextField1.setColumns(20);
        jTextField1.setRows(5);
        jScrollPane3.setViewportView(jTextField1);

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
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(19, Short.MAX_VALUE))))
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
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 51)));

        jLabel7.setText("Mã hóa: ");

        jLabel8.setText("Giải mã:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addContainerGap(219, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Triple Data Encryption Standard");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel12.setText("Cơ sở bảo mật & An toàn thông tin");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Trạng thái:");

        jLabel14.setText("Kiểm tra: -");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(66, 66, 66)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(228, 228, 228)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(255, 255, 255)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12))
                    .addComponent(jLabel1))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(13, 13, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JOptionPane.showMessageDialog(
            this,
            "MÔ TẢ CHƯƠNG TRÌNH VÀ THUẬT TOÁN 3DES\n\n"
            + "1) Luồng hoạt động của chương trình:\n"
            + "- Chế độ Mã hóa: nhập bản rõ (>= 15 ký tự) và khóa gốc (" + Main.getMinMasterKeyLength() + "-" + Main.getMaxMasterKeyLength() + " ký tự).\n"
            + "- Chương trình chuyển bản rõ sang UTF-8 bytes, thêm PKCS7 padding để đủ bội số 8 byte.\n"
            + "- Kết quả hiển thị ở 2 định dạng: Hex và Base64.\n"
            + "- Chế độ Giải mã: nhập Hex hoặc Base64, chương trình tự nhận dạng, giải mã và trả về text UTF-8.\n"
            + "- Nếu dữ liệu không hợp lệ hoặc sai độ dài khối DES (8 byte), hệ thống sẽ báo lỗi.\n\n"
            + "2) Cách sinh khóa 3DES trong chương trình:\n"
            + "- Từ khóa gốc, hệ thống tách thành 3 phần để tạo K1, K2, K3.\n"
            + "- Mỗi phần được kết hợp với khóa gốc và nhãn K1/K2/K3, sau đó băm SHA-256.\n"
            + "- Lấy 8 byte đầu của mỗi giá trị băm để tạo 3 khóa DES 64-bit.\n\n"
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
    }//GEN-LAST:event_jButton3ActionPerformed

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
        jLabel14.setText("Kiểm tra: -");
    }//GEN-LAST:event_jRadioButton1ActionPerformed

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
        jLabel14.setText("Kiểm tra: -");
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
    private javax.swing.JPanel jPanel1;
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
