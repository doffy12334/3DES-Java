package triple_des;

import java.util.Arrays;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.security.SecureRandom;

public class Main {
    // Bảng hoán vị khởi đầu IP (64 bit -> 64 bit)
    private static final int[] IP = {
        58, 50, 42, 34, 26, 18, 10, 2,
        60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 6,
        64, 56, 48, 40, 32, 24, 16, 8,
        57, 49, 41, 33, 25, 17, 9, 1,
        59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5,
        63, 55, 47, 39, 31, 23, 15, 7
    };
    
    // Bảng hoán vị nghịch đảo IP^-1
    private static final int[] IP_INV = {
        40, 8, 48, 16, 56, 24, 64, 32,
        39, 7, 47, 15, 55, 23, 63, 31,
        38, 6, 46, 14, 54, 22, 62, 30,
        37, 5, 45, 13, 53, 21, 61, 29,
        36, 4, 44, 12, 52, 20, 60, 28,
        35, 3, 43, 11, 51, 19, 59, 27,
        34, 2, 42, 10, 50, 18, 58, 26,
        33, 1, 41, 9, 49, 17, 57, 25
    };
    
    // Bảng mở rộng E (32 bit -> 48 bit)
    private static final int[] E = {
        32, 1, 2, 3, 4, 5,
        4, 5, 6, 7, 8, 9,
        8, 9, 10, 11, 12, 13,
        12, 13, 14, 15, 16, 17,
        16, 17, 18, 19, 20, 21,
        20, 21, 22, 23, 24, 25,
        24, 25, 26, 27, 28, 29,
        28, 29, 30, 31, 32, 1
    };
    
    // Bảng hoán vị P (32 bit -> 32 bit)
    private static final int[] P = {
        16, 7, 20, 21,
        29, 12, 28, 17,
        1, 15, 23, 26,
        5, 18, 31, 10,
        2, 8, 24, 14,
        32, 27, 3, 9,
        19, 13, 30, 6,
        22, 11, 4, 25
    };
    
    // 8 S-boxes (mỗi box: 4 hàng x 16 cột, input 6 bit -> output 4 bit)
    private static final int[][][] S_BOXES = {
        // S1
        {
            {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
            {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
            {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
            {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
        },
        // S2
        {
            {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
            {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
            {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
            {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
        },
        // S3
        {
            {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
            {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
            {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
            {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
        },
        // S4
        {
            {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
            {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
            {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
            {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
        },
        // S5
        {
            {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
            {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
            {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
            {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
        },
        // S6
        {
            {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
            {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
            {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
            {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
        },
        // S7
        {
            {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
            {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
            {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
            {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
        },
        // S8
        {
            {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
            {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
            {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
            {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
        }
    };
    
    // PC-1: Chọn 56 bit từ 64 bit khóa (bỏ 8 bit parity)
    private static final int[] PC1 = {
        57, 49, 41, 33, 25, 17, 9,
        1, 58, 50, 42, 34, 26, 18,
        10, 2, 59, 51, 43, 35, 27,
        19, 11, 3, 60, 52, 44, 36,
        63, 55, 47, 39, 31, 23, 15,
        7, 62, 54, 46, 38, 30, 22,
        14, 6, 61, 53, 45, 37, 29,
        21, 13, 5, 28, 20, 12, 4
    };
    
    // PC-2: Chọn 48 bit từ 56 bit
    private static final int[] PC2 = {
        14, 17, 11, 24, 1, 5,
        3, 28, 15, 6, 21, 10,
        23, 19, 12, 4, 26, 8,
        16, 7, 27, 20, 13, 2,
        41, 52, 31, 37, 47, 55,
        30, 40, 51, 45, 33, 48,
        44, 49, 39, 56, 34, 53,
        46, 42, 50, 36, 29, 32
    };
    
    // Số bit dịch trái cho mỗi vòng
    private static final int[] SHIFTS = {
        1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
    };
    
    // ==================== CÁC HÀM PHỤ TRỢ ====================
    
    // Lấy bit thứ i từ mảng byte (đánh số từ 1)
    private static int getBit(byte[] data, int position) {
        int byteIndex = (position - 1) / 8;
        int bitIndex = 7 - ((position - 1) % 8);
        return (data[byteIndex] >> bitIndex) & 1;
    }
    
    // Đặt bit thứ i trong mảng byte (đánh số từ 1)
    private static void setBit(byte[] data, int position, int value) {
        int byteIndex = (position - 1) / 8;
        int bitIndex = 7 - ((position - 1) % 8);
        if (value == 1) {
            data[byteIndex] |= (1 << bitIndex);
        } else {
            data[byteIndex] &= ~(1 << bitIndex);
        }
    }
    
    // Hoán vị theo bảng cho trước
    private static byte[] permute(byte[] input, int[] table, int outputBits) {
        byte[] output = new byte[(outputBits + 7) / 8];
        for (int i = 0; i < table.length; i++) {
            int bit = getBit(input, table[i]);
            setBit(output, i + 1, bit);
        }
        return output;
    }
    
    // XOR hai mảng byte
    private static byte[] xor(byte[] a, byte[] b) {
        byte[] result = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        return result;
    }
    
    // Dịch trái vòng (circular left shift) cho nửa khóa 28 bit
    private static byte[] leftShift(byte[] half, int numBits) {
        byte[] result = new byte[4]; // 28 bit cần 4 bytes
        
        // Copy dữ liệu sang result
        System.arraycopy(half, 0, result, 0, 4);
        
        for (int shift = 0; shift < numBits; shift++) {
            // Lấy bit đầu tiên (bit 1)
            int firstBit = getBit(result, 1);
            
            // Dịch tất cả các bit sang trái
            for (int i = 1; i < 28; i++) {
                int bit = getBit(result, i + 1);
                setBit(result, i, bit);
            }
            
            // Đặt bit đầu tiên vào cuối (bit 28)
            setBit(result, 28, firstBit);
        }
        
        return result;
    }
    
    // Chuyển đổi byte array thành hex string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
    
    // ==================== THUẬT TOÁN DES ====================
    
    // Sinh 16 khóa con từ khóa 64 bit
    private static byte[][] generateSubKeys(byte[] key) {
        byte[][] subKeys = new byte[16][6]; // 16 khóa con, mỗi khóa 48 bit (6 bytes)
        
        // Bước 1: PC-1 - Chọn 56 bit từ 64 bit (bỏ các bit chẵn lẻ 8, 16, ..., 64)
        byte[] key56 = permute(key, PC1, 56);
        
        // Chia thành 2 nửa, mỗi nửa 28 bit
        byte[] C = new byte[4]; // 28 bit
        byte[] D = new byte[4]; // 28 bit
        
        for (int i = 1; i <= 28; i++) {
            setBit(C, i, getBit(key56, i));
            setBit(D, i, getBit(key56, i + 28));
        }
        
        // Bước 2: Sinh 16 khóa con
        for (int round = 0; round < 16; round++) {
            // Dịch trái
            int shiftAmount = SHIFTS[round];
            C = leftShift(C, shiftAmount);
            D = leftShift(D, shiftAmount);
            
            // Ghép C và D
            byte[] CD = new byte[7]; // 56 bit
            for (int i = 1; i <= 28; i++) {
                setBit(CD, i, getBit(C, i));
                setBit(CD, i + 28, getBit(D, i));
            }
            
            // PC-2: Chọn 48 bit từ 56 bit
            subKeys[round] = permute(CD, PC2, 48);
        }
        
        return subKeys;
    }
    
    // Hàm f trong DES
    private static byte[] feistelFunction(byte[] R, byte[] subKey) {

        // Bước 1: Mở rộng R từ 32 bit -> 48 bit bằng bảng E
        byte[] expandedR = permute(R, E, 48);
        
        // Bước 2: XOR với khóa con
        byte[] xored = xor(expandedR, subKey);
        
        // Bước 3: Chia thành 8 khối 6-bit, qua 8 S-box
        byte[] sBoxOutput = new byte[4]; // 8 * 4 bit = 32 bit
        StringBuilder sboxInBinary = new StringBuilder();
        StringBuilder sboxOutBinary = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            // Lấy 6 bit
            int b1 = getBit(xored, i * 6 + 1);
            int b2 = getBit(xored, i * 6 + 2);
            int b3 = getBit(xored, i * 6 + 3);
            int b4 = getBit(xored, i * 6 + 4);
            int b5 = getBit(xored, i * 6 + 5);
            int b6 = getBit(xored, i * 6 + 6);
            String sixBits = "" + b1 + b2 + b3 + b4 + b5 + b6;
            sboxInBinary.append(sixBits).append(" ");
            
            // Tính hàng và cột
            int row = (b1 << 1) | b6;
            int col = (b2 << 3) | (b3 << 2) | (b4 << 1) | b5;
            
            // Lấy giá trị từ S-box
            int sValue = S_BOXES[i][row][col];
            String fourBits = String.format("%4s", Integer.toBinaryString(sValue)).replace(' ', '0');
            sboxOutBinary.append(fourBits).append(" ");
            

            // Ghi 4 bit vào output
            setBit(sBoxOutput, i * 4 + 1, (sValue >> 3) & 1);
            setBit(sBoxOutput, i * 4 + 2, (sValue >> 2) & 1);
            setBit(sBoxOutput, i * 4 + 3, (sValue >> 1) & 1);
            setBit(sBoxOutput, i * 4 + 4, sValue & 1);
        }
        
        // Bước 4: Hoán vị P
        byte[] pResult = permute(sBoxOutput, P, 32);
        return pResult;
    }
    
    // Mã hóa DES một khối 64 bit
    private static byte[] desEncrypt(byte[] plainBlock, byte[][] subKeys) {
        // Bước 1: Hoán vị khởi đầu IP
        byte[] permuted = permute(plainBlock, IP, 64);
        
        // Chia thành L0 và R0
        byte[] L = new byte[4]; // 32 bit
        byte[] R = new byte[4]; // 32 bit
        
        for (int i = 1; i <= 32; i++) {
            setBit(L, i, getBit(permuted, i));
            setBit(R, i, getBit(permuted, i + 32));
        }
        
        // Bước 2: 16 vòng lặp Feistel
        for (int round = 0; round < 16; round++) {
            
            byte[] newL = R;
            byte[] fResult = feistelFunction(R, subKeys[round]);
            byte[] newR = xor(L, fResult);
            
            L = newL;
            R = newR;
            
        }
        
        // Bước 3: Ghép R16L16 (đổi chỗ)
        byte[] combined = new byte[8];
        for (int i = 1; i <= 32; i++) {
            setBit(combined, i, getBit(R, i));
            setBit(combined, i + 32, getBit(L, i));
        }
        
        // Bước 4: Hoán vị IP^-1
        byte[] output = permute(combined, IP_INV, 64);
        return output;
    }
    
    // Giải mã DES một khối 64 bit (đảo ngược thứ tự khóa con)
    private static byte[] desDecrypt(byte[] cipherBlock, byte[][] subKeys) {
        // Đảo ngược thứ tự khóa con
        byte[][] reversedKeys = new byte[16][];
        for (int i = 0; i < 16; i++) {
            reversedKeys[i] = subKeys[15 - i];
        }
        
        // Giải mã = mã hóa với khóa đảo ngược
        // Toàn bộ log chi tiết sẽ được thực hiện bên trong desEncrypt
        byte[] output = desEncrypt(cipherBlock, reversedKeys);
        return output;
    }
    
    // ==================== 3DES (EDE MODE) ====================
    
    // Mã hóa 3DES (EDE): E(K1, D(K2, E(K3, plaintext)))
    // NOTE: The standard is EDE, but this implementation uses EDE with K1, K2, K3.
    // For consistency with the UI and decryption, we will implement E(K1, D(K2, E(K3, ...)))
    // A more standard implementation would be E(K1, D(K2, E(K1, ...)))
    private static byte[] tripleDesEncrypt(byte[] plainBlock, byte[][] subKeys1, byte[][] subKeys2, byte[][] subKeys3) {
        
        byte[] temp1 = desEncrypt(plainBlock, subKeys1);
        
        byte[] temp2 = desDecrypt(temp1, subKeys2);
        
        byte[] out = desEncrypt(temp2, subKeys3);
        return out;
    }
    
    // Giải mã 3DES (EDE): D(K3, E(K2, D(K1, ciphertext)))
    private static byte[] tripleDesDecrypt(byte[] cipherBlock, byte[][] subKeys1, byte[][] subKeys2, byte[][] subKeys3) {

        byte[] temp1 = desDecrypt(cipherBlock, subKeys3);

        byte[] temp2 = desEncrypt(temp1, subKeys2);

        byte[] out = desDecrypt(temp2, subKeys1);
        return out;
    }
    
    // ==================== PADDING ====================
    
    // PKCS7 padding
    private static byte[] addPadding(byte[] data) {
        int paddingLength = 8 - (data.length % 8);
        if (paddingLength == 0) paddingLength = 8;
        
        byte[] padded = new byte[data.length + paddingLength];
        System.arraycopy(data, 0, padded, 0, data.length);
        
        for (int i = data.length; i < padded.length; i++) {
            padded[i] = (byte) paddingLength;
        }
        
        return padded;
    }
    
    // Loại bỏ PKCS7 padding
    private static byte[] removePadding(byte[] data) {
        int paddingLength = data[data.length - 1] & 0xFF;
        
        // Kiểm tra padding hợp lệ
        if (paddingLength < 1 || paddingLength > 8) {
            return data;
        }
        
        for (int i = data.length - paddingLength; i < data.length; i++) {
            if ((data[i] & 0xFF) != paddingLength) {
                return data;
            }
        }
        
        return Arrays.copyOf(data, data.length - paddingLength);
    }
    
    // ==================== HÀM MAIN ====================

    private static final int BLOCK_SIZE = 8;
    private static final int DES_KEY_SIZE = 8;
    private static final int TDES_KEY_SIZE = 24;
    private static final int MIN_PLAINTEXT_LENGTH = 15;
    private static final int MIN_MASTER_KEY_LENGTH = 1;
    private static final int MAX_MASTER_KEY_LENGTH = 4096;

    private static final String RANDOM_KEY_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private static final long[] WEAK_DES_KEYS = {
        0x0101010101010101L,
        0xFEFEFEFEFEFEFEFEL,
        0xE0E0E0E0F1F1F1F1L,
        0x1F1F1F1F0E0E0E0EL
    };

    public static int getMinMasterKeyLength() {
        return MIN_MASTER_KEY_LENGTH;
    }

    public static int getMaxMasterKeyLength() {
        return MAX_MASTER_KEY_LENGTH;
    }

    public static String generateRandomMasterKey(int length) {
        if (length != 16 && length != 24) {
            throw new IllegalArgumentException("Độ dài khóa ngẫu nhiên chỉ hỗ trợ 16 hoặc 24 ký tự.");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int idx = SECURE_RANDOM.nextInt(RANDOM_KEY_ALPHABET.length());
            sb.append(RANDOM_KEY_ALPHABET.charAt(idx));
        }
        return sb.toString();
    }

    private static byte[] encrypt3DesData(byte[] paddedData, byte[][] subKeys1, byte[][] subKeys2, byte[][] subKeys3) {
        int numBlocks = paddedData.length / BLOCK_SIZE;
        byte[] ciphertext = new byte[paddedData.length];

        for (int i = 0; i < numBlocks; i++) {
            byte[] block = Arrays.copyOfRange(paddedData, i * BLOCK_SIZE, (i + 1) * BLOCK_SIZE);
            byte[] encryptedBlock = tripleDesEncrypt(block, subKeys1, subKeys2, subKeys3);
            System.arraycopy(encryptedBlock, 0, ciphertext, i * BLOCK_SIZE, BLOCK_SIZE);
        }

        return ciphertext;
    }

    private static byte[] decrypt3DesData(byte[] ciphertext, byte[][] subKeys1, byte[][] subKeys2, byte[][] subKeys3) {
        int numBlocks = ciphertext.length / BLOCK_SIZE;
        byte[] decryptedPadded = new byte[ciphertext.length];

        for (int i = 0; i < numBlocks; i++) {
            byte[] block = Arrays.copyOfRange(ciphertext, i * BLOCK_SIZE, (i + 1) * BLOCK_SIZE);
            byte[] decryptedBlock = tripleDesDecrypt(block, subKeys1, subKeys2, subKeys3);
            System.arraycopy(decryptedBlock, 0, decryptedPadded, i * BLOCK_SIZE, BLOCK_SIZE);
        }

        return decryptedPadded;
    }

    private static byte[] normalizeTo24Bytes(byte[] input) {
        if (input == null || input.length == 0) {
            throw new IllegalArgumentException("Khóa đầu vào không được rỗng.");
        }

        byte[] out = new byte[TDES_KEY_SIZE];
        if (input.length == TDES_KEY_SIZE) {
            System.arraycopy(input, 0, out, 0, TDES_KEY_SIZE);
        } else if (input.length == 16) {
            // 2-key 3DES: K1|K2|K1
            System.arraycopy(input, 0, out, 0, 16);
            System.arraycopy(input, 0, out, 16, 8);
        } else if (input.length > TDES_KEY_SIZE) {
            System.arraycopy(input, 0, out, 0, TDES_KEY_SIZE);
        } else {
            // Lặp lại byte đầu vào cho đến khi đủ 24 byte.
            for (int i = 0; i < TDES_KEY_SIZE; i++) {
                out[i] = input[i % input.length];
            }
        }

        return out;
    }

    private static void applyOddParity(byte[] key24) {
        for (int i = 0; i < key24.length; i++) {
            int sevenBits = key24[i] & 0xFE;
            int ones = Integer.bitCount(sevenBits & 0xFF);
            key24[i] = (byte) ((ones % 2 == 0) ? (sevenBits | 0x01) : sevenBits);
        }
    }

    private static boolean hasOddParity(byte b) {
        return (Integer.bitCount(b & 0xFF) % 2) == 1;
    }

    private static long toLong8(byte[] key8) {
        long v = 0L;
        for (int i = 0; i < DES_KEY_SIZE; i++) {
            v = (v << 8) | (key8[i] & 0xFFL);
        }
        return v;
    }

    private static boolean isWeakDes8Key(byte[] key8) {
        long v = toLong8(key8);
        for (long wk : WEAK_DES_KEYS) {
            if (v == wk) {
                return true;
            }
        }
        return false;
    }

    private static byte[][] generateKeyTripletFromMaster(String masterKey) {
        byte[] input = masterKey.getBytes(StandardCharsets.UTF_8);
        byte[] key24 = normalizeTo24Bytes(input);
        applyOddParity(key24);

        byte[] key1 = Arrays.copyOfRange(key24, 0, 8);
        byte[] key2 = Arrays.copyOfRange(key24, 8, 16);
        byte[] key3 = Arrays.copyOfRange(key24, 16, 24);

        if (isWeakDes8Key(key1) || isWeakDes8Key(key2) || isWeakDes8Key(key3)) {
            throw new IllegalArgumentException("Phát hiện khóa yếu DES trong K1/K2/K3. Vui lòng đổi khóa khác.");
        }

        for (byte b : key24) {
            if (!hasOddParity(b)) {
                throw new IllegalStateException("Lỗi thiết lập parity DES.");
            }
        }

        return new byte[][] { key1, key2, key3 };
    }

    private static void validateMasterKey(String masterKey) {
        if (masterKey == null || masterKey.isEmpty()) {
            throw new IllegalArgumentException("Khóa đầu vào không được để trống.");
        }
        if (masterKey.length() < MIN_MASTER_KEY_LENGTH || masterKey.length() > MAX_MASTER_KEY_LENGTH) {
            throw new IllegalArgumentException(
                "Khóa đầu vào phải có độ dài từ " + MIN_MASTER_KEY_LENGTH + " đến " + MAX_MASTER_KEY_LENGTH + " ký tự."
            );
        }
    }

    private static byte[] hexToBytes(String hex) {
        String normalized = hex.replaceAll("\\s+", "").toUpperCase();
        if (normalized.isEmpty() || (normalized.length() % 2 != 0)) {
            throw new IllegalArgumentException("Chuỗi Hex không hợp lệ.");
        }

        byte[] result = new byte[normalized.length() / 2];
        for (int i = 0; i < normalized.length(); i += 2) {
            int hi = Character.digit(normalized.charAt(i), 16);
            int lo = Character.digit(normalized.charAt(i + 1), 16);
            if (hi == -1 || lo == -1) {
                throw new IllegalArgumentException("Chuỗi Hex chứa ký tự không hợp lệ.");
            }
            result[i / 2] = (byte) ((hi << 4) + lo);
        }

        return result;
    }

    private static byte[] parseCipherInput(String cipherInput) {
        String normalized = cipherInput.replaceAll("\\s+", "");
        boolean looksHex = normalized.matches("(?i)[0-9a-f]+") && normalized.length() % 2 == 0;

        if (looksHex) {
            return hexToBytes(normalized);
        }

        try {
            return Base64.getDecoder().decode(normalized);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Dữ liệu vào phải ở dạng HEX hoặc Base64 hợp lệ.");
        }
    }

    public static String encryptToHex(String plaintext, String masterKey) {
        if (plaintext == null || plaintext.length() < MIN_PLAINTEXT_LENGTH) {
            throw new IllegalArgumentException("Dữ liệu vào phải có ít nhất " + MIN_PLAINTEXT_LENGTH + " ký tự.");
        }
        validateMasterKey(masterKey);

        byte[][] keyTriplet = generateKeyTripletFromMaster(masterKey);
        byte[][] subKeys1 = generateSubKeys(keyTriplet[0]);
        byte[][] subKeys2 = generateSubKeys(keyTriplet[1]);
        byte[][] subKeys3 = generateSubKeys(keyTriplet[2]);

        byte[] plainBytes = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] paddedData = addPadding(plainBytes);

        byte[] ciphertext = encrypt3DesData(paddedData, subKeys1, subKeys2, subKeys3);
        return bytesToHex(ciphertext);
    }

    public static String encryptToBase64(String plaintext, String masterKey) {
        String hex = encryptToHex(plaintext, masterKey);
        return Base64.getEncoder().encodeToString(hexToBytes(hex));
    }

    public static String hexToBase64(String ciphertextHex) {
        return Base64.getEncoder().encodeToString(hexToBytes(ciphertextHex));
    }

    public static String decryptFromHex(String ciphertextHex, String masterKey) {
        validateMasterKey(masterKey);

        byte[] ciphertext = hexToBytes(ciphertextHex);
        if (ciphertext.length % BLOCK_SIZE != 0) {
            throw new IllegalArgumentException("Bản mã không đúng định dạng khối DES (8 bytes).");
        }

        byte[][] keyTriplet = generateKeyTripletFromMaster(masterKey);
        byte[][] subKeys1 = generateSubKeys(keyTriplet[0]);
        byte[][] subKeys2 = generateSubKeys(keyTriplet[1]);
        byte[][] subKeys3 = generateSubKeys(keyTriplet[2]);

        byte[] decryptedPadded = decrypt3DesData(ciphertext, subKeys1, subKeys2, subKeys3);
        byte[] decrypted = removePadding(decryptedPadded);

        return new String(decrypted, StandardCharsets.UTF_8);
    }

    public static String decryptAuto(String ciphertextInput, String masterKey) {
        validateMasterKey(masterKey);

        byte[] ciphertext = parseCipherInput(ciphertextInput);
        if (ciphertext.length % BLOCK_SIZE != 0) {
            throw new IllegalArgumentException("Bản mã không đúng định dạng khối DES (8 bytes).");
        }

        byte[][] keyTriplet = generateKeyTripletFromMaster(masterKey);
        byte[][] subKeys1 = generateSubKeys(keyTriplet[0]);
        byte[][] subKeys2 = generateSubKeys(keyTriplet[1]);
        byte[][] subKeys3 = generateSubKeys(keyTriplet[2]);

        byte[] decryptedPadded = decrypt3DesData(ciphertext, subKeys1, subKeys2, subKeys3);
        byte[] decrypted = removePadding(decryptedPadded);

        return new String(decrypted, StandardCharsets.UTF_8);
    }

    public static boolean verifyRoundTrip(String plaintext, String ciphertextHex, String masterKey) {
        String decrypted = decryptAuto(ciphertextHex, masterKey);
        return plaintext.equals(decrypted);
    }
    
}