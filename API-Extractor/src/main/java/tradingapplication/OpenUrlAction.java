package tradingapplication;

import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class OpenUrlAction implements MouseListener {

	static public OpenUrlAction instance;

	private OpenUrlAction() {

	}

	public static OpenUrlAction getInstance() {

		if (instance == null) {
			instance = new OpenUrlAction();
		}

		return instance;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if ( // get image logo coordinates
		e.getPoint().getX() >= 40 && e.getPoint().getX() <= 170 && e.getPoint().getY() >= 195
				&& e.getPoint().getY() <= 325) {
			open(TradingApplication.urlLogo);
			TradingApplication.urlAction = null;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	private void open(URL url) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(url.toURI());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}
}