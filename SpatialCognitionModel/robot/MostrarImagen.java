package robot;
import java.awt.*;

import javax.imageio.ImageIO;

import java.net.MalformedURLException;
import java.net.URL;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Vector;
import java.util.Hashtable;


public class MostrarImagen extends Frame  implements MouseListener {
	
	private BufferedImage imagen;
	
	public MostrarImagen() {
		setVisible(true);
		addMouseListener(this);
	}
	
	/**
	 * @param string
	 */
	public MostrarImagen(String string) {
		super(string);
	}

	public void paint(Graphics g) {
		g.drawImage(imagen, 0, 0, this);
	}
	
	public void setImagen(BufferedImage imagen) {
		this.imagen = imagen;
		setSize(imagen.getWidth(),imagen.getHeight());
		repaint();
	}

	public static void main(String[] args) {
		MostrarImagen imagen = new MostrarImagen();
		while (true) {
			try {
				imagen.getImage();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	
	BufferedImage getImage() {
		return imagen;
	}

	/**
	 * 
	 */
	public void clearImagen() {
		if(imagen!=null)
			setImagen(new BufferedImage(imagen.getWidth(), imagen.getHeight(), imagen.getType()));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		MostrarImagenAndFilter iaf = new MostrarImagenAndFilter();
		iaf.setImagen(imagen);
		new Thread(iaf).start();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
