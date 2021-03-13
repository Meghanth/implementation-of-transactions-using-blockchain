
package isa.isaaaj;

import java.math.BigInteger;
import java.security.*;
import java.util.Base64;
import java.util.ArrayList;

class CrUtils {
    static String encobase64(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    static String md5(String input) {
        try {
            
            MessageDigest md = MessageDigest.getInstance("MD5"); 
  
            byte[] messageDigest = md.digest(input.getBytes()); 
  
            BigInteger no = new BigInteger(1, messageDigest); 
  
            String hashtext = no.toString(16); 
            while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            } 
            return hashtext;
            
            /*
            MessageDigest digest = MessageDigest.getInstance("MD5");
	        
            //Applies sha256 to our input, 
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
	        
            StringBuffer hexString = new StringBuffer(); 
            for (int i = 0; i < hash.length; i++) {
                    String hex = Integer.toHexString(0xff & hash[i]);
                    if(hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
            }
            return hexString.toString();
            */
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static byte[] ECDSASig(PrivateKey privateKey, String input) {
        byte[] output = new byte[0];
        try {
            Signature sign = Signature.getInstance("ECDSA", "BC");
            sign.initSign(privateKey);
            byte[] strByte = input.getBytes();
            sign.update(strByte);
            byte[] realSig = sign.sign();
            output = realSig;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return output;
    }
    
    static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static String getDifiString(int difficulty) {
        // Replacing java null characeter `\0` with `0`
        return new String(new char[difficulty]).replace('\0', '0');
    }
    //Merkle root-hash tree,checks data integrity

    static String mklroot(ArrayList<Trans> transactions) {
        // Fill tree with previous transactions
        ArrayList<String> prevtreelayer = new ArrayList<String>();
        for (Trans transaction : transactions) {
            prevtreelayer.add(transaction.transID);
        }

        // Verifying tree with hashes
        int count = transactions.size();
        ArrayList<String> treeLayer = prevtreelayer;
        while (count > 1) {
            treeLayer = new ArrayList<String>();
            for (int i = 1; i < prevtreelayer.size(); i++) {
                treeLayer.add(md5(prevtreelayer.get(i - 1) + prevtreelayer.get(i)));
            }
            count = treeLayer.size();
            prevtreelayer = treeLayer;
        }

        String mklRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
        return mklRoot;
    }
}
