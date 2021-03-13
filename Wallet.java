
package isa.isaaaj;

import java.security.spec.ECGenParameterSpec;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Wallet {
    PrivateKey privateKey;
    PublicKey publicKey;
    private HashMap<String, TransOut> UTXOs = new HashMap<String, TransOut>();

    Wallet() {
        genPriPub();
    }

    private void genPriPub() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } 
        catch (Exception e) {
            throw new RuntimeException("Wallet: generating KeyPair failed. Its because of : " + e);
        }
    }

    float getBalance() {
        float total = 0;
        for (Map.Entry<String, TransOut> item : Blockchain.UTXOs.entrySet()) {
            TransOut UTXO = item.getValue();
            if (UTXO.mining(publicKey)) {
                UTXOs.put(UTXO.id, UTXO);
                total += UTXO.value;
            }
        }
        return total;
    }

    Trans sendingFunds(PublicKey recipient, float value) {
        if (getBalance() < value) {
            return null;
        }

        ArrayList<TransIn> inputs = new ArrayList<TransIn>();
        float total = 0;
        for (Map.Entry<String, TransOut> item : UTXOs.entrySet()) {
            TransOut UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransIn(UTXO.id));
            if (total > value)
                break;
        }

        Trans newTransaction = new Trans(publicKey, recipient, value, inputs);
        newTransaction.signgen(privateKey);
        for (TransIn input : inputs) {
            UTXOs.remove(input.transOutID);
        }
        return newTransaction;
    }

    String priget() {
        return privateKey.getEncoded().toString();
    }

    String pubget() {
        return publicKey.getEncoded().toString();
    }
}
