package encryptionOnBytes;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.file.*;
import javax.swing.JFileChooser;

public class mainProgram 
{
	public static byte rotateRight(byte bits, int shift)
	{
	     return (byte)(((bits & 0xff)  >>> shift) | ((bits & 0xff) << (8 - shift)));
	}
	public static byte rotateLeft(byte bits, int shift)
	{
	    return (byte)(((bits & 0xff) << shift) | ((bits & 0xff) >>> (8 - shift)));
	}
	
	public static byte FindKey(byte b) 
	{
		int mone=0;
		String s = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
		char[] arr=s.toCharArray();
		for (int i = 0; i < arr.length; i++)
		{
			if(arr[i]=='1')
				mone++;
		}
		return (byte)mone;
	}
	
	public static byte RotateByKey(byte b,byte key) 
	{
		return rotateRight(b,key);
	}
	
	public static String ReadTextFromFile(String file) throws Exception 
	{
		String txt="",txt_file="";
		File f=new File(file);
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		while(txt!=null)
		{
			txt=br.readLine();
			txt_file+=txt;
		}
		
		return txt_file;
	}
	
	public static byte[] EncryptAllBytesByKey(byte[] content) 
	{
		//reverse
		byte[] reverseByte=new byte[content.length];
		for (int i = 0; i < reverseByte.length; i++) 
		{
			reverseByte[i]=content[reverseByte.length-1-i];
		}
		byte[] encBytes=new byte[content.length*2];
		for (int i = 0,j=0; i < encBytes.length; i+=2,j++)
		{
			System.out.print((char)reverseByte[j]);
			encBytes[i]=reverseByte[j];
		}
		
		for (int i = 1; i < encBytes.length; i+=2) 
		{
			encBytes[i]=FindKey(encBytes[i-1]);
			encBytes[i-1]=RotateByKey(encBytes[i-1], encBytes[i]);
					}
		
		return encBytes;
	}
	
	public static byte OpenByKey(byte b,byte key) 
	{
		return rotateLeft(b,key);
	}
	
	public static byte[] DeEncBytes(byte[]enc) 
	{
		byte[] deEnc=new byte[enc.length/2];
		
		for (int i = 0,j=0; j < deEnc.length; i+=2,j++) 
		{
			deEnc[j]=OpenByKey(enc[i], enc[i+1]);
			System.out.print((char)deEnc[j]);
		}
		
		byte[] reverseByte=new byte[deEnc.length];
		for (int i = 0; i < reverseByte.length; i++)
		{
			reverseByte[i]=deEnc[reverseByte.length-1-i];
			System.out.print((char)reverseByte[i]);
		}
		
		return reverseByte;
	}
	
	public static String getFileExtension(File file)
	{
	    String name = file.getName();
	    int lastIndexOf = name.lastIndexOf(".");
	    if (lastIndexOf == -1) {
	        return ""; // empty extension
	    }
	    return name.substring(lastIndexOf);
	}
	
	public static void OpenFileAndReadFromIt() throws Exception 
	 {
		 	String file_txt="",path="",file_type="";
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			int result = fileChooser.showOpenDialog(fileChooser);
			if (result == JFileChooser.APPROVE_OPTION) 
			{
			    File selectedFile = fileChooser.getSelectedFile();
			    file_txt=selectedFile.getAbsolutePath();
			    file_type=getFileExtension(selectedFile);
			    //System.out.println(file_txt);
			    //file_txt=ReadTextFromFile(file_txt);
			    byte[] fileContent = Files.readAllBytes(selectedFile.toPath());
			    System.out.println("----");
			    String msg=fileContent.toString();
			    System.out.println("msg: "+msg);
			    for (int i = 0; i < 10; i++) 
			    {
					System.out.println(fileContent[i]);
				}
			    System.out.println("----");
			    byte[]enc=EncryptAllBytesByKey(fileContent);
			    msg=enc.toString();
			    System.out.println("msg:"+msg);
			    System.out.println("----");
			    for (int i = 0; i <20; i++)
			    {
					System.out.println(enc[i]);
				}
			    
			    System.out.println("----");
			    try (FileOutputStream fos = new FileOutputStream("encrypted"+file_type)) 
			    {
			    	   fos.write(enc);
			    	   //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
			    	}
			    try (FileOutputStream fos = new FileOutputStream("deEncrypted"+file_type)) 
			    {
			    	   fos.write(DeEncBytes(enc));
			    	   //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
			    	}
			}
			else
				System.out.println("The program can't open the current file...Try again");
	 }
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		//OpenFileAndReadFromIt();//--working without a frame
		EncryptionFrame EF=new EncryptionFrame();//--working with a frame
		
	}

}
