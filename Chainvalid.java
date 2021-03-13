
package isa.isaaaj;

import java.util.ArrayList;
import java.util.HashMap;

class Chainvalid {
    static boolean check(ArrayList<Block> blockchain, Trans genTrans, int difficulty) {
        Block currblock;
        Block prevblock;
        HashMap<String, TransOut> tempUTXOs = new HashMap<String, TransOut>();
        tempUTXOs.put(genTrans.outputs.get(0).id, genTrans.outputs.get(0));
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for (int i = 1; i < blockchain.size(); i++) {
            prevblock = blockchain.get(i - 1);
            currblock = blockchain.get(i);

            if (!currblock.hash.equals(currblock.calculateHash())) {
                Utils.loggs("Error!Error!Error! Block hash got damaged");
                return false;
            }
            if (!prevblock.hash.equals(currblock.prevhash)) {
                Utils.loggs("Error!Error!Error! Previous block hash and current block hash are not equal");
                return false;
            }
            if (!currblock.hash.substring(0, difficulty).equals(hashTarget)) {
                Utils.loggs("Error!Error!Error! Block is not mined");
                return false;
            }

            TransOut tempOutput;
            for (int t = 0; t < currblock.transactions.size(); t++) {
                Trans currentTransaction = currblock.transactions.get(t);

                if (!currentTransaction.verifiySign()) {
                    Utils.loggs("Error!Error!Error! transaction Signature(" + t + ") is invalid");
                    return false;
                }
                if (currentTransaction.getIn() != currentTransaction.getOut()) {
                    Utils.loggs("Error!Error!Error! Inputs not equal to outputs on transaction(" + t + ")");
                    return false;
                }

                for (TransIn input : currentTransaction.inputs) {
                    tempOutput = tempUTXOs.get(input.transOutID);

                    if (tempOutput == null) {
                        Utils.loggs("Error!Error!Error! Referenced input on transaction(" + t + ") is missing");
                        return false;
                    }

                    if (input.UTXO.value != tempOutput.value) {
                        Utils.loggs("Error!Error!Error! Referenced input transaction(" + t + ") value is Invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.transOutID);
                }

                for (TransOut output : currentTransaction.outputs) {
                    tempUTXOs.put(output.id, output);
                }
                if (currentTransaction.outputs.get(0).recipient != currentTransaction.recipient) {
                    Utils.loggs("Error!Error!Error! transaction(" + t + ") output recipient is not who it should be");
                    return false;
                }
                if (currentTransaction.outputs.get(1).recipient != currentTransaction.sender) {
                    Utils.loggs("Error!Error!Error! transaction(" + t + ") output recipient is not a sender.");
                    return false;
                }
            }
        }
        return true;
    }
}
