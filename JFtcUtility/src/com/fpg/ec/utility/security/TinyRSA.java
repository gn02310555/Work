package com.fpg.ec.utility.security;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Hashtable;
import java.util.Random;
import java.util.StringTokenizer;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
*RSA加密程式簡易版
**/
public class TinyRSA {
	final static String BaseNumber="17";//需為奇數
	final int bitLength=64;//P,Q輸出位元長度
	final int certainty =20;
    static int BLOCKSIZE;//密碼最大長度
    static String serviceDefinedPath;
    BigInteger P, Q, N, E, D, T;
    BigInteger ZERO;
    BigInteger ONE;
    BigInteger ciphertext[];
    Random rand;    
    BASE64Encoder encode;
    BASE64Decoder decode;
/**
 * 請於此處加入方法的說明。
 * 建立日期： (2003/7/10 上午 09:36:03)
 */
private TinyRSA() {
    ciphertext= new BigInteger[100];
    ZERO= new BigInteger("0");
    ONE= new BigInteger("1");
    rand= new Random();
    encode= new BASE64Encoder();
    decode= new BASE64Decoder();
}
 /*    範例︰請於此處插入方法的範例。
 *    @param path:String 憑證路徑，包含檔名（如：D:/servlets/j2XX/CA.crypt）
 *    @param pswdSizeLimite:String  密碼最大長度，允許使用者輸入密碼的最大長度
 *    注意：此建構子會先判斷憑證是否存在於指定之憑證路徑，若不存在，程式便會
 *    自動再產生一個憑證，但是，這個憑證會之前的憑證不一樣喔！
 *    @since 2003/7/10 PC27
 */
public TinyRSA(String path,int pswdSizeLimite) throws IOException{
    this();
    this.BLOCKSIZE=pswdSizeLimite;
    this.serviceDefinedPath=path;
    java.io.File file= new java.io.File(serviceDefinedPath);
    if (!file.exists()) {
	    generateKeyPair(BaseNumber);
    }
    dispatch(readServicesFile());
}
/**
中國剩餘定理
*/   
private BigInteger ChineseRemainder(BigInteger res[]) {
        BigInteger multiplier;
        BigInteger uv[] = Euclid(P, Q);
        BigInteger e[] = new BigInteger[2];
        e[0] = uv[1].multiply(Q);
        e[1] = uv[0].multiply(P);
        return ((e[0].multiply(res[0])).add(e[1].multiply(res[1]))).mod(N);
    }
    private byte[] copyBytes(int x, byte tmp[]) {
        byte swap[] = new byte[BLOCKSIZE];
        int count = 0;
        for (int i = x * BLOCKSIZE; i < (x+1) * BLOCKSIZE; i++) {
            if (i >= tmp.length) break;
            swap[count++] = tmp[i];
        }
        return swap;
    }
//解密方法
public String Decrypt(String msg) throws TinyRSAException, IOException {
    return Decrypt(msg, this.D);
}
private String Decrypt(String msg, BigInteger key)
    throws TinyRSAException, IOException {
    String str= null;

    int i= 0;
    byte swap[]= decode.decodeBuffer(msg);
    byte segment[];
    ciphertext[0]= new BigInteger(swap);
    swap= null;
    do {
        ciphertext[i]= ciphertext[i].modPow(key, N);
        swap= ciphertext[i].toByteArray();
        if (str == null)
            str= new String(swap);
        else
            str += new String(swap);//, "ISO-8859-1"

    } while (ciphertext[++i] != null);

    return str;
}
/**
 * 請於此處加入方法的說明。
 * 建立日期： (2003/7/10 下午 12:22:43)
 * @param al java.util.ArrayList
 */
private void dispatch(Hashtable hst) {
    this.P= new BigInteger((String) hst.get("P"));
    this.E= new BigInteger((String) hst.get("E"));
    this.Q= new BigInteger((String) hst.get("Q"));
    this.T= new BigInteger((String) hst.get("T"));
    this.N= new BigInteger((String) hst.get("N"));
    this.D= new BigInteger((String) hst.get("D"));
}
//解密方法
public String Encrypt(String msg) throws TinyRSAException,IOException{
    return Encrypt(msg,this.E);
}
private String Encrypt(String msg, BigInteger key) throws TinyRSAException,IOException{
    String str= null;
    StringBuffer sb=new StringBuffer(msg);
 
	    if(msg.length()<BLOCKSIZE){		   
		    for(int i=0;i<BLOCKSIZE-msg.length();i++){
			    sb.append(" ");
		    }
		    msg=sb.toString();
	    }

        byte swap[]= msg.getBytes();
        byte segment[];
        str=new String();        
        int copies= swap.length / BLOCKSIZE;
        if (swap.length % BLOCKSIZE > 0)
            copies++;
            
        for (int i= 0; i < copies; i++) {
            segment= copyBytes(i, swap);           
            ciphertext[i]= new BigInteger(segment);
            ciphertext[i]= ciphertext[i].modPow(key, N);
            segment= ciphertext[i].toByteArray();
            
            if (str == null)
                str= encode.encode(segment);
            else
                str += encode.encode(segment);
        }
    return str;
}
/***
尤拉公式
**/ 
private BigInteger[] Euclid(BigInteger x, BigInteger y) {
        int n = 2;
        BigInteger r[] = new BigInteger[3];
        BigInteger q[] = new BigInteger[3];
        BigInteger u[] = new BigInteger[3];
        BigInteger v[] = new BigInteger[3];

        r[0] = x;
        r[1] = y;
        u[0] = new BigInteger("1");
        v[0] = new BigInteger("0");
        u[1] = new BigInteger("0");
        v[1] = new BigInteger("1");
        while (!r[(n-1)%3].equals(ZERO)) {
            q[n%3] = r[(n-2)%3].divide(r[(n-1)%3]);
            r[n%3] = r[(n-2)%3].remainder(r[(n-1)%3]);
            u[n%3] = u[(n-2)%3].subtract(q[n%3].multiply(u[(n-1)%3]));
            v[n%3] = v[(n-2)%3].subtract(q[n%3].multiply(v[(n-1)%3]));

            n++;
        }
        BigInteger result[] = new BigInteger[2];
        result[0] = u[(n-2)%3];
        result[1] = v[(n-2)%3];
        return result;
    }
/**
鑰匙產生器，這個程式只要執行一次，就可以產生公鑰跟私鑰
e請輸入奇數
**/
private boolean generateKeyPair(String e) throws IOException {
    
        E= new BigInteger(e);
        if (E.mod(new BigInteger("2")).equals(ZERO))
            return false;
        do {
            P= new BigInteger(bitLength,certainty, rand);
            Q= new BigInteger(bitLength,certainty, rand);
            T= (P.subtract(ONE)).multiply(Q.subtract(ONE));
        } while (!(E.gcd(T)).equals(ONE));
        N= P.multiply(Q);
        BigInteger tmp[]= Euclid(E, T);
        D= tmp[0];

        writeServicesFile("P", P, true);
        writeServicesFile("E", E, true);
        writeServicesFile("Q", Q, true);
        writeServicesFile("T", T, true);
        writeServicesFile("N", N, true);
        writeServicesFile("D", D, true);

    return true;
}
/**
 * 請於此處加入方法的說明。
 * 建立日期： (2003/7/2 上午 10:00:31)
 * @param args java.lang.String[]
 */
public static void main(String[] args) {
    try {
	     String CAPath="d:/xxx.crypte";
	     String msg="aAdfD4F2";
         TinyRSA rsa=new TinyRSA(CAPath,12);        
         String afterEncrytion=rsa.Encrypt(msg);
         
         String afterDecrytion=rsa.Decrypt(afterEncrytion);
         System.out.println("憑證路徑:"+CAPath);
         System.out.println("加密前明碼:"+msg);
         System.out.println("加密後密碼:"+afterEncrytion);
         System.out.println("加密後密碼長度:"+afterEncrytion.length());
         System.out.println("解碼後明碼:"+afterDecrytion);
         
    } catch (Exception e) {
        System.out.println(e);
    }
}
    private java.util.Hashtable readServicesFile() {
    Hashtable hst= null;
    try {
        BufferedReader in= new BufferedReader(new FileReader(serviceDefinedPath));

        String s= in.readLine();
        StringTokenizer st= new StringTokenizer(s, "##");
        StringTokenizer st2=null;
        hst= new Hashtable();
        while (st.hasMoreTokens()) {
            String part= (String) st.nextToken();
            //System.out.println(part);
            st2= new StringTokenizer(part, "$$");            
            if (st2.hasMoreTokens()) {
                hst.put(st2.nextElement(),st2.nextElement());                
            }
        }
    } catch (Exception e) {
        System.out.println(e);
    }
     return hst;
}
private void writeServicesFile(Object key, Object value, boolean replace)
    throws IOException {
    BufferedWriter out= new BufferedWriter(new FileWriter(serviceDefinedPath, replace));
    out.write("##"+key+ "$$"+value+"$$");
    //System.out.println(encode.encode(value.toString().getBytes()));
    out.flush();
    out.close();

}
}
