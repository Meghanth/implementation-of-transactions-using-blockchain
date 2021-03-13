
package isa.isaaaj;

import java.security.*;
import java.util.ArrayList;

class Trans {
    private byte[] sign;
    private float minTrans = 0.1f;
    private int counter = 0;
    String transID;float value;
    PublicKey sender, recipient;
    
    ArrayList<TransIn> inputs = new ArrayList<TransIn>();
    ArrayList<TransOut> outputs = new ArrayList<TransOut>();

    Trans(PublicKey from, PublicKey to, float value, ArrayList<TransIn> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    boolean transproces() {
        Utils.loggs("Processing transaction..");
        if (verifiySign() == false) {
            return false;
        }

        for (TransIn input : inputs) {
            input.UTXO = Blockchain.UTXOs.get(input.transOutID); // Input is always an output of previously generated transaction
        }
        if (getIn() < minTrans) {
            return false;
        }
 
        float leftOver = getIn() - value;  // subtracting current transaction value from all inputs
        transID = calHash();

        outputs.add(new TransOut(this.recipient, value, transID)); // Generating outputs with sender and recipient values
        outputs.add(new TransOut(this.sender, leftOver, transID));

        // Add transaction outputs to Blockchain UTXOs
        for (TransOut o : outputs) {
            Blockchain.UTXOs.put(o.id, o);
        }

        // Removing finished transactions from UTXO
        for (TransIn i : inputs) {
            if (i.UTXO == null)
                continue;
            Blockchain.UTXOs.remove(i.UTXO.id);
        }
        return true;
    } 
    
    void signgen(PrivateKey privateKey) {
        String data = CrUtils.encobase64(sender) + CrUtils.encobase64(recipient) //Encoding String: "sender + recipient + value" and encode this with privateKey by ECDSA
                + Float.toString(value);
        sign = CrUtils.ECDSASig(privateKey, data);
    }
    
    boolean verifiySign() {
        String data = CrUtils.encobase64(sender) + CrUtils.encobase64(recipient)
                + Float.toString(value);
        return CrUtils.verifyECDSASig(sender, data, sign);  //String: "sender + recipient + value" verified by ECDSA and transaction sign
    }

    float getIn() {
        float total = 0;
        for (TransIn i : inputs) {
            if (i.UTXO == null)
                continue;
            total += i.UTXO.value;
        }
        return total;
    }

    float getOut() {
        float total = 0;
        for (TransOut o : outputs) {
            total += o.value;
        }
        return total;
    }

    // String: "sender + recipient + value + counter" encoded by MD5
    private String calHash() {
        counter++;
        return CrUtils.md5(CrUtils.encobase64(sender) + CrUtils.encobase64(recipient)
                + Float.toString(value) + counter);
    }
}
