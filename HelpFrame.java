package encryptionOnBytes;

import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JTextArea;

public class HelpFrame extends Frame implements WindowListener
{

	public HelpFrame() throws HeadlessException 
	{
		// TODO Auto-generated constructor stub
		this.setTitle("Help Screen");
		JTextArea txtArea=new JTextArea("HELP:\n"
									  + "This programe purpose\n"
				                      + "is to encrypt your files\n" 
									  + "and to de-encrypt the encrypted files.\n"
				                      + "At first you need to open the file\n"
									  + "chooser and choose a file to encrypt.\n"
				                      + "Then you need to press the 'Encrypt'\n"
				                      + "button and it will create a new\n"
				                      + "encrypted file.\n"
				                      + "After that you need to open the file\n"
				                      + "chooser again and to locate the\n"
				                      + "encrypted file and to pick it, and\n"
				                      + "then you can de-encrypt it.");
		this.add(txtArea);
		this.setSize(500, 400);
		this.setVisible(true);
		this.addWindowListener(this);
	}

	public HelpFrame(GraphicsConfiguration gc) {
		super(gc);
		// TODO Auto-generated constructor stub
	}

	public HelpFrame(String title) throws HeadlessException {
		super(title);
		// TODO Auto-generated constructor stub
	}

	public HelpFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) 
	{
		// TODO Auto-generated method stub
		EncryptionFrame ef=new EncryptionFrame();
		this.dispose();
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
