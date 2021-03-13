
package isa.isaaaj;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Blockchain {
    
	static HashMap<String, TransOut> UTXOs = new HashMap<String, TransOut>();
	private ArrayList<Block> blocks = new ArrayList<Block>();
	private int difficulty = 1;
	private Trans genTrans;

	Blockchain() {
		Security.addProvider(new BouncyCastleProvider()); // Cryptographic API for Java e.g. ECDSA	
	}

	void prepaGenTrans(Wallet secondWallet, Blockchain blockchain) {
		Wallet genWallet = new Wallet();
		genTrans = new Trans(genWallet.publicKey, secondWallet.publicKey, 100f, null);
		genTrans.signgen(genWallet.privateKey);
		genTrans.outputs.add(new TransOut(genTrans.recipient, genTrans.value,
				genTrans.transID));
		UTXOs.put(genTrans.outputs.get(0).id, genTrans.outputs.get(0));

		Block genesisBlock = new Block("0");
		genesisBlock.addTrans(genTrans);
		genesisBlock.mineBlock(difficulty);
		blocks.add(genesisBlock);
		validateBlockchain(blockchain);
	}

	void transfering(Wallet sender, Wallet recipient, float value, Blockchain blockchain) {
		Block block = new Block(blockchain.getHashFromLastBlock());
		printBalance(sender, "Sender");
		printBalance(recipient, "Recipient");
		block.addTrans(sender.sendingFunds(recipient.publicKey, value));
		block.mineBlock(difficulty);
		blocks.add(block);
		printBalance(sender, "Sender");
		printBalance(recipient, "Recipient");
		validateBlockchain(blockchain);
	}

	private void validateBlockchain(Blockchain blockchain) {
		System.out.print("\nBlockChain is getting validated.......");
		if (Chainvalid.check(blockchain.blocks, genTrans, difficulty)) {
			System.out.print(" Done\n");
		}
	}

	Wallet createWallet() {
		return new Wallet();
	}

	void setMiningDifficulty(int value) {
		this.difficulty = value;
	}

	ArrayList<Block> getBlocks() {
		return blocks;
	}

	private void printBalance(Wallet wallet, String wallet_name) {
		Utils.loggs(wallet_name + " wallet balance = " + wallet.getBalance());
	}

	private String getHashFromLastBlock() {
		return blocks.get(blocks.size() - 1).hash;
	}
}
