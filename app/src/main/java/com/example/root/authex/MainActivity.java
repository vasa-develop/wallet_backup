package com.example.root.authex;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.signers.ECDSASigner;
import org.spongycastle.crypto.signers.HMacDSAKCalculator;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.util.concurrent.ExecutionException;

import jnr.ffi.annotations.In;

public class MainActivity extends AppCompatActivity {


    private Web3j web3j;
    private Credentials credentials;
    private String WalletFile,transactionHash;

    private String NETWORK_URL;
    private String RINKEBY = "https://rinkeby.infura.io/MIY5d592BKY8caiAK2TJ";
    private String ROPSTEN = "https://ropsten.infura.io/MIY5d592BKY8caiAK2TJ";
    private String KOVAN = "https://kovan.infura.io/MIY5d592BKY8caiAK2TJ";
    private String IPFS = "https://ipfs.infura.io:5001";

    String filepath = "/data/data/com.example.root.authex/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NETWORK_URL = RINKEBY;
        //System.out.println("Raw_TRX_ARR: "+ create_rawtx("344bea059c4ba36961d55ce6e9d500cb4a8033b8d009e4791f02edd0a7bc0530"));
        new ConnectTestnet().execute();
        //int b = hexStringToByteArray();
        //System.out.println(b);



    }

    public int[] bytearray2intarray(byte[] barray)
    {
        int[] iarray = new int[barray.length];
        int i = 0;
        for (byte b : barray)
            iarray[i++] = b & 0xff;
        return iarray;
    }

    public static String hexStringToByteArray(String trx_hash) {
        String str = trx_hash.substring(2);
        int[] arrayOfValues = new int[str.length() / 2];
        int counter = 0;
        for (int i = 0; i < str.length(); i += 2) {
            String s = str.substring(i, i + 2);
            arrayOfValues[counter] = Integer.parseInt(s, 16);
            counter++;
        }
        System.out.println("Counter: "+counter);
        String arr_txt = "{";
        for (int i=0;i<arrayOfValues.length;i++){
            arr_txt = arr_txt + arrayOfValues[i] + ",";
        }
        arr_txt = arr_txt + "}";
        return arr_txt;
    }

    public static String hexStringToHexArray(String trx_hash){
        String str = trx_hash;
        String[] arrayOfValues = new String[str.length() / 2];
        int counter = 0;
        for (int i = 0; i < str.length(); i += 2) {
            String s = str.substring(i, i + 2);
            arrayOfValues[counter] = Integer.toHexString(Integer.parseInt(s, 16));
            counter++;
        }
        String arr_txt = "{";
        for (int i=0;i<arrayOfValues.length;i++){
            arr_txt = arr_txt + arrayOfValues[i] + ",";
        }
        arr_txt = arr_txt + "}";
        return arr_txt;
    }

    public void CreateWallet() throws Exception{
        File file = new File(filepath);
        if (!file.isDirectory()) {
            Log.e("uploadFile", "Source File not exist :" + filepath);
        }else{
            Log.d("uploadFile","file exist");
            System.out.println("Wallet generation started...");
            //WalletUtils.generateLightNewWalletFile("borntochange", new File(filepath));
            System.out.println("Wallet created.");
            String[] d = file.list();
            WalletFile = d[3];
            System.out.println("Wallet: "+WalletFile);
            new InitiateWallet().execute();
        }

    }



    class Generate_tx extends AsyncTask<String, Void, Credentials> {
        @Override
        protected Credentials doInBackground(String... strings) {
            RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(
                    BigInteger.valueOf(400000000),ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT, "0x1158F15E74DCEc06AEAeEbA5b0EaA8461c73dB36",BigInteger.ONE);



            System.out.println("RaW_TRX: "+Numeric.toHexString(Hash.sha3(TransactionEncoder.encode(rawTransaction))));

            ECKeyPair_crack keyPair = new ECKeyPair_crack(BigInteger.valueOf(credentials.getEcKeyPair().getPrivateKey().longValue()),BigInteger.valueOf(credentials.getEcKeyPair().getPublicKey().longValue()));



            BigInteger publicKey = keyPair.getPublicKey();

            byte[] messageHash = Hash.sha3(TransactionEncoder.encode(rawTransaction));

            BigInteger sig = keyPair.sign(messageHash);





            System.out.println("ARR: "+hexStringToByteArray(Numeric.toHexString(sig.toByteArray())));

            /*ECDSASignature sig = keyPair.sign(Hash.sha3(TransactionEncoder.encode(rawTransaction)));

            System.out.println("Signature: "+sig);

            System.out.println("Byte_arr_RAW_trx: "+create_rawtx(Numeric.toHexString(Hash.sha3(TransactionEncoder.encode(rawTransaction)))));


            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,credentials);


            String hexValue = Numeric.toHexString(signedMessage);


            System.out.println("hexValue: "+hexValue);
            //System.out.println("Signed_Byte_arr: "+hexStringToByteArray(hexValue));

            Sign.SignatureData signatureData = Sign.signMessage(
                    TransactionEncoder.encode(rawTransaction), credentials.getEcKeyPair());

            System.out.println("signatureData: "+(Sign.signMessage(TransactionEncoder.encode(rawTransaction),credentials.getEcKeyPair())));

            String res = "{";
            int c = 0;
            for(int i=0; i<signedMessage.length;i++){
                res = res + signedMessage[i];
                c = i;
            }

            res = res + "}";
            System.out.println("Signed_Byte_arr" + res);
            System.out.println("len: "+c);


            EthSendTransaction ethSendTransaction = null;
            try {
                ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            transactionHash = ethSendTransaction.getTransactionHash();
            System.out.println("transaction_hash: "+transactionHash);
*/








            return null;
        }

        @Override
        protected void onPostExecute(Credentials credentials) {

            //hexStringToByteArray(transactionHash);
        }
    }

    class InitiateWallet extends AsyncTask<String, Void, Credentials> {

        @Override
        protected Credentials doInBackground(String... strings) {
            try {
                credentials = WalletUtils.loadCredentials("borntochange",filepath+WalletFile);

            } catch (Exception e) {
                Log.d("wallet_init_err: ", String.valueOf(e));
            }
            Log.d("wallet_initiation: ","SUCCESS");
            Log.d("wallet_Pubkey: ", String.valueOf(credentials.getEcKeyPair().getPublicKey()));
            Log.d("wallet_Prikey: ", String.valueOf(credentials.getEcKeyPair().getPrivateKey()));
            Log.d("wallet_addr: ",credentials.getAddress());

            String hex =Numeric.toHexString(credentials.getEcKeyPair().getPrivateKey().toByteArray());


            System.out.println("Arr_priv_key: "+hexStringToByteArray(hex));



            return credentials;
        }

        @Override
        protected void onPostExecute(Credentials credentials) {

            new Generate_tx().execute();
        }
    }


    class ConnectTestnet extends AsyncTask<String, Void, Web3j> {
        String transfer_to;
        BigInteger val;
        @Override
        protected Web3j doInBackground(String... strings) {

            web3j = Web3jFactory.build(new HttpService(
                    NETWORK_URL));
            try {
                Log.d("wallet_network_log: ","Connected to Ethereum client version: "
                        + web3j.web3ClientVersion().send().getWeb3ClientVersion());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return web3j;
        }

        @Override
        protected void onPostExecute(Web3j web3j) {
            try {
                CreateWallet();
            } catch (Exception e) {
                System.out.println("Wallet_err :"+e);
            }
        }
    }



    public String create_rawtx(String rawtx_hexhash){



        int [] rawtx_arr = new int[32];

        int c = 0;

        String s = "{";

        for (int i=0; i<32 ; i++){


            s = s+"0x"+rawtx_hexhash.substring(c,c+2);
            if(i!=31){
                s = s+",";
            }
            c = c+2;
        }

        s = s+"}";
        //System.out.println("res: "+s);
        return s;
    }

}
