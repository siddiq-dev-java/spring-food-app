package com.springBoot.saravana_bhavan.project_help;


	
	
	
	
	import java.io.UnsupportedEncodingException;
	import java.security.InvalidAlgorithmParameterException;
	import java.security.InvalidKeyException;
	import java.security.NoSuchAlgorithmException;
	import java.util.Base64;
	import javax.crypto.BadPaddingException;
	import javax.crypto.Cipher;
	import javax.crypto.IllegalBlockSizeException;
	import javax.crypto.NoSuchPaddingException;
	import javax.crypto.spec.IvParameterSpec;
	import javax.crypto.spec.SecretKeySpec;

	
	public class AES { 
	    private static final String KEY = "mysecretkey12345";
	    private static final byte[] IV = new byte[16];
	    
	    public static String Encrypt(String plain_pass){
	        try{
	            SecretKeySpec sk = new SecretKeySpec(KEY.getBytes("UTF-8"),"AES");
	            IvParameterSpec iv =new IvParameterSpec(IV);
	            
	            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5padding");
	            cipher.init(Cipher.ENCRYPT_MODE,sk,iv);
	            
	            byte[] encrypted = cipher.doFinal(plain_pass.getBytes("UTF-8"));
	            return Base64.getEncoder().encodeToString(encrypted);
	        }
	        catch(Exception e){
	            System.err.println(e.getMessage());
	        }
	        return null;
	    }
	    
	    
	      public static String Decrypt(String cipher_text){
	        try{
	            SecretKeySpec sk = new SecretKeySpec(KEY.getBytes("UTF-8"),"AES");
	            IvParameterSpec iv =new IvParameterSpec(IV);
	            
	            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5padding");
	              cipher.init(Cipher.DECRYPT_MODE, sk, iv); 
	            
	            byte[] decodedBytes = Base64.getDecoder().decode(cipher_text);
	             byte[] original = cipher.doFinal(decodedBytes);
	             return new String(original,"UTF-8");
	        }
	        catch(UnsupportedEncodingException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e){
	            System.err.println(e.getMessage());
	        }
	        return null;
	    }
	    
	    
	    
	}



