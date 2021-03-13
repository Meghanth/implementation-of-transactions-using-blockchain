
package isa.isaaaj;

import java.util.concurrent.TimeUnit;
import java.text.DateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Main {
    
    public static void main(String[] args) {
        
        Blockchain chain = new Blockchain();
        Wallet walletA = chain.createWallet();
        Wallet walletB = chain.createWallet();
        Wallet walletC = chain.createWallet();
        Wallet walletD = chain.createWallet();
        Wallet walletE = chain.createWallet();
        
        Utils.loggs("\nCreating first wallet and first transaction..");
        chain.prepaGenTrans(walletA, chain);
        chain.setMiningDifficulty(4);
        
        long start = System.nanoTime();
        Utils.loggs("\nTransaction-1: Sending '30' from Wallet A to Wallet B");
        Utils.logDottedLine();
        chain.transfering(walletA, walletB, 30f, chain);
        long end = System.nanoTime();
        long elapsedTime = end - start;
        System.out.println("transaction-1 time: "+elapsedTime+" nanosecs");
        
        long sta = System.nanoTime();
        Utils.loggs("\nTransaction-2: Sending '100' from Wallet A to Wallet B");
        Utils.logDottedLine();
        chain.transfering(walletA, walletB, 100f, chain);
        long ed = System.nanoTime();
        long elapsTime = ed - sta;
        System.out.println("transaction-2 time: "+elapsTime+" nanosecs");
        
        long startt = System.nanoTime();
        Utils.loggs("\nTrasnaction-3: Sending '40' from Wallet B to Wallet A");
        Utils.logDottedLine();
        chain.transfering(walletB, walletA, 40f, chain);
        long endd = System.nanoTime();
        long elapsseTime = endd - startt;
        System.out.println("transaction-3 time: "+elapsseTime+" nanosecs");
        
        long starttt = System.nanoTime();
        Utils.loggs("\nTransaction-4: Sending '50' from Wallet A to Wallet C");
        Utils.logDottedLine();
        chain.transfering(walletA, walletC, 50f, chain);
        long enddd = System.nanoTime();
        long elapssseTime = enddd - starttt;
        System.out.println("transaction-4 time: "+elapssseTime+" nanosecs");
        
        long startttt = System.nanoTime();
        Utils.loggs("\nTransaction-5: Sending '10' from Wallet C to Wallet D");
        Utils.logDottedLine();
        chain.transfering(walletC, walletD, 10f, chain);
        long endddd = System.nanoTime();
        long elapsssseTime = endddd - startttt;
        System.out.println("transaction-5 time: "+elapsssseTime+" nanosecs");
        
        long starttttt = System.nanoTime();
        Utils.loggs("\nTransaction-6: Sending '20' from Wallet C to Wallet E");
        Utils.logDottedLine();
        chain.transfering(walletC, walletE, 20f, chain);
        long enddddd = System.nanoTime();
        long elapssssseTime = enddddd - starttttt;
        System.out.println("transaction-6 time: "+elapssssseTime+" nanosecs");
    }
}
