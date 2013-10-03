/**
 * This is an application built to check the URL additions to the JAF
 */
package	test;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.beans.*;
import java.io.IOException;
import javax.activation.*;

public class UrlCheck extends Frame implements WindowListener {
    private URL url;
    private Object command = null;

    ////////////////////////////////////////////////////////////
    //-- ctor
    public UrlCheck(URL url) {
	super("UrlCheck");
	this.url = url;
	
	initMe();
	
	if(checkURLData()) { // if we can get a DS
	    System.out.println("\n\n...Looking for a bean to display the content");

	    CommandInfo ci[] = url.getPreferredCommands(url.getContentType());
	    if(ci != null && (ci.length > 0)) {
		System.out.println("\tfound a bean");
		// i'm going to make an assumption about the semantics
		// of the default cmd map for now, --bhc
		// I'm also assuming all beans implement cmdobject
		System.out.print("\tadding bean to frame...");
		command =((DataHandler) url).getBean(ci[0]);
		if(command instanceof Component) {
		    this.add((Component)command);
		    System.out.println("done.");
		    this.setSize(((Container)command).getPreferredSize());
		    this.doLayout();
		}
		else {
		    System.out.println("*failed* not a Component!");
		}
		show();
		
	    }
	    else {
		System.out.println("\tcouldn't find a bean");
	    }
	}
	
    }
    ////////////////////////////////////////////////////////////
    // init me
    private void initMe() {
	setSize(400,400);
	setLayout(new BorderLayout());
	this.addWindowListener(this);
    }
    ////////////////////////////////////////////////////////////
    // get url data
    // Print all the DH stuff we know about the URL
    private boolean checkURLData()
	{
	    DataSource ds = null;

	    // Printout the content type
	    System.out.println( "URL = " + url.toString() );
	    System.out.println( "\tContent Type: " +  url.getContentType());
	    System.out.println( "\tName: " + url.getName());
	    System.out.print("\n...getting the DataSource...");
	    
	    try {
		ds = url.getDataSource();
	    } catch (IOException e) {
		System.out.println("IOException caught in checkURL Data");
		System.exit(1);
	    }
		
	    if(ds == null) {
		System.out.println("<failed>\n");
		return false;
	    }
	    else {
		System.out.println("done.\n");
	    
		System.out.println("\tDataSource.getName(): " + 
				   ds.getName());
		System.out.println("\tDataSource.getContentType(): " +
				   ds.getContentType());
		return true;
	    }
	    
	}
    ////////////////////////////////////////////////////////////
    // mainline
    public static void main(String args[]) {
	URL new_url = null;
	UrlCheck appFrame = null;

	if(args.length == 0) {
	    System.out.println("usage: UrlCheck <url>");
	    System.exit(1);
	}

	try {
	    new_url = new URL(args[0]);
	} catch (MalformedURLException e) {
	    System.out.println("ERROR: The syntax of the URL was incorrect!");
	    System.out.println("Check it and try again!");
	    System.exit(1);
	}
	    
	appFrame = new UrlCheck( new_url );    
	    
    }

    /////////////****WindowListener****//////////////////
    public void windowOpened(WindowEvent e) {}
    public void windowClosing(WindowEvent e) {
	System.exit(0);
    }
    public void windowClosed(WindowEvent e) { }
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
}
