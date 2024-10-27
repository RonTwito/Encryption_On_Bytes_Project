package encryptionOnBytes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelpAction implements ActionListener {

	EncryptionFrame ef;
	public HelpAction(EncryptionFrame ef) 
	{
		// TODO Auto-generated constructor stub
		this.ef=ef;
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		this.ef.dispose();
		HelpFrame hf=new HelpFrame();
	}

}
