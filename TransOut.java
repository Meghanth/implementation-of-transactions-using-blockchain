
package isa.isaaaj;

import java.security.PublicKey;

class TransOut {
    String id,partransID;float value;
    PublicKey recipient;

    TransOut(PublicKey recipient, float value, String partransactionid) {
        this.recipient = recipient;
        this.value = value;
        this.partransID = partransactionid;
        // id -> String: "recipient + value + partransID" encoded by md5
        this.id = CrUtils
                .md5(CrUtils.encobase64(recipient) + Float.toString(value) + partransactionid);
    }

    boolean mining(PublicKey publicKey) {
        return (publicKey == recipient);
    }
}
