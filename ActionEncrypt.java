package encryptionOnBytes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.Spring;

public class ActionEncrypt implements WindowListener, ActionListener {
	boolean isEncrypt=true;
	String file_txt="";
	public static String fileType="";
	
	public ActionEncrypt(boolean isEncrypt) 
	{
		// TODO Auto-generated constructor stub
		this.isEncrypt=isEncrypt;	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		file_txt=EncryptionFrame.file_txt;
		
		if(isEncrypt) 
		{
			String path="",file_type="";
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			int result = fileChooser.showOpenDialog(fileChooser);
			if (result == JFileChooser.APPROVE_OPTION) 
			{
				File selectedFile = fileChooser.getSelectedFile();
				file_txt=selectedFile.getAbsolutePath();
				file_type=getFileExtension(selectedFile);
				EncryptionFrame.txtArea.append(file_txt+" is ");
				EncryptionFrame.lbl_current_file.setText(file_txt.substring(file_txt.lastIndexOf('\\')+1));
				
				try
				{
					byte[] content=Files.readAllBytes(selectedFile.toPath());
					byte [] enc=EncryptAllBytesByKey(content);
					ActionEncrypt.fileType=getFileExtension(selectedFile);
					 try (FileOutputStream fos = new FileOutputStream("encrypted"+getFileExtension(selectedFile))) 
					    {
					    	   fos.write(enc);
					    	   //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
					    }
				} 
				catch (IOException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
			else
				System.out.println("Cant open any file");
			
			
			}
		
		
		else
		{
			EncryptionFrame.txtArea.append("de-encrypted! \n");
			File selectedFile = new File("encrypted"+ActionEncrypt.fileType);
			try 
			{
				byte[] content=Files.readAllBytes(selectedFile.toPath());
				try (FileOutputStream fos = new FileOutputStream("deEncrypted"+getFileExtension(selectedFile))) 
			    {
			    	   fos.write(DeEncBytes(content));
			    	   //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
			    	}
			}
			catch (IOException e1)
			{
			// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
			
	}
	

	/*
	 *Method Name: rotateRight
	 *Method Kind: byte
	 *Input: byte,int
	 *Output: byte
	 *Description: Rotates right a byte(bits) the number of times as the shift says
	 */
	public byte rotateRight(byte bits, int shift)
	{
	     return (byte)(((bits & 0xff)  >>> shift) | ((bits & 0xff) << (8 - shift)));
	}
	
	/*
	 *Method Name: rotateLeft
	 *Method Kind: byte
	 *Input: byte,int
	 *Output: byte
	 *Description: Rotates left a byte(bits) the number of times as the shift says
	 */
	public byte rotateLeft(byte bits, int shift)
	{
	    return (byte)(((bits & 0xff) << shift) | ((bits & 0xff) >>> (8 - shift)));
	}
	
	/*
	 *Method Name: FindKey
	 *Method Kind: byte
	 *Input: byte
	 *Output: byte
	 *Description: Checking how many bytes are turned on in the current byte b
	 */
	public byte FindKey(byte b) 
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
	
	/*
	 *Method Name: RotateByKey
	 *Method Kind: byte
	 *Input: byte, byte
	 *Output: byte
	 *Description: Rotates right the byte b by the number of times contains in byte key
	 */
	public byte RotateByKey(byte b,byte key) 
	{
		return rotateRight(b,key);
	}
	
	
	public String ReadTextFromFile(String file) throws Exception 
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
	
	/*
	 *Method Name: EncryptAllBytesByKey
	 *Method Kind: byte[]
	 *Input: byte array
	 *Output: byte array
	 *Description: Encrypts the byte array content
	 */
	public byte[] EncryptAllBytesByKey(byte[] content) 
	{
		//reverse
		byte[] reverseByte=new byte[content.length];
		for (int i = 0; i < reverseByte.length; i++) 
		{
			reverseByte[i]=content[reverseByte.length-1-i];
		}
		byte[] encBytes=new byte[content.length*2];
		//encBytes[0]=(byte)20182;
		for (int i = 0,j=0; i < encBytes.length; i+=2,j++)
		{
			//System.out.print((char)reverseByte[j]);
			encBytes[i]=reverseByte[j];
		}
		
		for (int i = 1; i < encBytes.length; i+=2) 
		{
			encBytes[i]=FindKey(encBytes[i-1]);
			encBytes[i-1]=RotateByKey(encBytes[i-1], encBytes[i]);
		}
		
		return encBytes;
	}
	
	/*
	 *Method Name: OpenByKey
	 *Method Kind: byte
	 *Input: byte, byte
	 *Output: byte
	 *Description: Rotates left the byte b by the number of times contains in byte key
	 */
	public byte OpenByKey(byte b,byte key) 
	{
		return rotateLeft(b,key);
	}
	
	/*
	 *Method Name: DeEncBytes
	 *Method Kind: byte[]
	 *Input: byte array
	 *Output: byte array
	 *Description: De-encrypt the byte array enc
	 */
	public byte[] DeEncBytes(byte[]enc) 
	{
		byte[] deEnc=new byte[(enc.length)/2];
		
		for (int i = 0,j=0; j < deEnc.length; i+=2,j++) 
		{
			deEnc[j]=OpenByKey(enc[i], enc[i+1]);
			//System.out.print((char)deEnc[j]);
		}
		
		byte[] reverseByte=new byte[deEnc.length];
		for (int i = 0; i < reverseByte.length; i++)
		{
			reverseByte[i]=deEnc[reverseByte.length-1-i];
			//System.out.print((char)reverseByte[i]);
		}
		
		return reverseByte;
		
		//EncryptionFrame.txtArea.append("The current file is not encrypted, please choose another file!\n");
		//return enc;
		
	}
	
	/*
	 *Method Name: getFileExtension
	 *Method Kind: String
	 *Input: File
	 *Output: String
	 *Description: Returns the file extension
	 */
	public String getFileExtension(File file)
	{
	    String name = file.getName();
	    int lastIndexOf = name.lastIndexOf(".");
	    if (lastIndexOf == -1) 
	    {
	        return ""; // empty extension
	    }
	    return name.substring(lastIndexOf);
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
