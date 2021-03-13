
package isa.isaaaj;

import java.util.Date;
import java.util.ArrayList;

class Block {
    String hash, prevhash, mklroot;
    
    ArrayList<Trans> transactions = new ArrayList<Trans>();
    long timeStamp;
    int nonce;                                          // miners starting iterate point

    Block(String previousHash) {
        this.prevhash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    String calculateHash() {
        String calculatedhash = CrUtils
                .md5(prevhash + Long.toString(timeStamp) + Integer.toString(nonce) + mklroot);
        return calculatedhash;
    }

    boolean addTrans(Trans transaction) {
        if (transaction == null)
            return false;
        if (prevhash != "0") {
            if (transaction.transproces() != true) {
                Utils.loggs("Warning: Transaction discarded");
                return false;
            }
        }
        transactions.add(transaction);
        return true;
    }

    void mineBlock(int difficulty) {
        Utils.loggs("Mining block: " + hash);
        mklroot = CrUtils.mklroot(transactions);
        String target = CrUtils.getDifiString(difficulty);
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
    }
}
