package encryptionOnBytes;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.file.Files;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class EncryptionFrame extends Frame implements WindowListener, ActionListener 
{
	public static JTextArea txtArea;//--text area which contains the paths of every file we are working with
	public static Label lbl_current_file;//--label that contains the name of the current file we are working with
	public static String file_txt="";//--a static String which contains the current file full path

	/* 
	 *Method Name: EncryptionFrame
	 *Method Kind: Constructor
	 *Description: A method which construct an Encryption Frame for working with files
	 */
	public EncryptionFrame() 
	{
		// TODO Auto-generated constructor stub
		
		//--Build up for the frame
		this.setTitle("Encryption Program");
		this.setLayout(new FlowLayout());
		this.setBackground(Color.blue);
		
		Button btn_open_fileChooser=new Button("Press To Open File Chooser");
		Button btn_encrypt=new Button("Encrypt");
		Button btn_deEnc=new Button("De Encrypt");
		
		lbl_current_file=new Label("No File Currently");
		
		txtArea=new JTextArea("Encryption on those files:\n",5,20);
		
		//this.add(btn_open_fileChooser);
		this.add(btn_encrypt);
		this.add(btn_deEnc);
		this.add(lbl_current_file);
		this.add(txtArea);
		
		btn_open_fileChooser.addActionListener(this);
		btn_encrypt.addActionListener(new ActionEncrypt(true));
		btn_deEnc.addActionListener(new ActionEncrypt(false));
		
		txtArea.setFont(new Font("Serif", Font.ITALIC, 16));
		txtArea.setLineWrap(true);
		txtArea.setWrapStyleWord(true);
		txtArea.setEditable(false);
		txtArea.setVisible(true);
		txtArea.setBackground(Color.CYAN);

		Button btn_help=new Button("Help");
		btn_help.addActionListener(new HelpAction(this));
		
		this.add(btn_help);
		this.setSize(500, 400);
		this.setVisible(true);
		this.addWindowListener(this);
		//--Build up for the frame
		
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
		byte[] deEnc=new byte[enc.length/2];
		
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
	    if (lastIndexOf == -1) {
	        return ""; // empty extension
	    }
	    return name.substring(lastIndexOf);
	}
	
	/*
	 *Method Name: OpenFileAndReadFromIt
	 *Method Kind: void
	 *Description: Opens file chooser
	 */
	public void OpenFileAndReadFromIt() throws Exception 
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
			    txtArea.append(file_txt+" you can work with the file now!\n");
			    lbl_current_file.setText(file_txt.substring(file_txt.lastIndexOf('\\')+1));
			    /*
			   byte[] fileContent = Files.readAllBytes(selectedFile.toPath());
			    System.out.println("----");
			    String msg=fileContent.toString();
			    //System.out.println("msg: "+msg);
			    for (int i = 0; i < 10; i++) 
			    {
					System.out.println(fileContent[i]);
				}
			    System.out.println("----");
			    byte[]enc=EncryptAllBytesByKey(fileContent);
			    msg=enc.toString();
			   //System.out.println("msg:"+msg);
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
			    	}*/
			}
			else
				System.out.println("The program can't open the current file...Try again");
	 }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			OpenFileAndReadFromIt();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void ClosingWindow(ActionEvent e)
	{
		this.dispose();
		System.exit(0);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		this.dispose();
		System.exit(0);
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
