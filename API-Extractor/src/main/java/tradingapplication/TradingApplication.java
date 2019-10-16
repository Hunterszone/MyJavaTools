package tradingapplication;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.json.JSONException;

public class TradingApplication extends javax.swing.JFrame {

	private JButton selectButton;
	private JButton exportButton;
	private JButton changeLogoButton;
	private JProgressBar jProgressBar1;
	private JPanel mainPanel = new javax.swing.JPanel();
	private JTextField inputField;
	private JLabel appTitle;
	private JLabel selectField;
	private JLabel outputText;
	private String linkLogo = "";
	private String linkLogoSymbol = "";
	private static Image image = null;
	private ConnectionToAPI connectionToAPI;
	static OpenUrlAction urlAction;
	static JLabel labelLogo = new JLabel();
	static URL urlLogo;
	public static String path2 = "";
	public static String[] companyNames = { "AAPL", "GOOGL", "BAM", "XOM", "BUD", "INTC", "C", "FB", "ORCL", "BAC",
			"MSFT", "HD", "PFE", "PG", "JPM", "AMZN", "UNH", "T", "VZ", "WMT", "WFC", "CHL", "NVS", "JNJ", "TSM", "CVX",
			"BABA", "V" }; // input from file is TBD
	CustomLogger log = new CustomLogger();

	public TradingApplication() {
		this.setLocationRelativeTo(null);
		setTitle("API Extractor");
		setResizable(false);
		initComponents();
		mainPanel.setLayout(new BorderLayout());
		loadStartLogo();
	}

	private void loadStartLogo() {

//        BufferedImage img = null;
		try {
			linkLogoSymbol = companyNames[0];
			linkLogo = "https://storage.googleapis.com/iex/api/logos/";
			urlLogo = new URL(linkLogo + linkLogoSymbol + ".png");
			image = ImageIO.read(urlLogo);
		} catch (IOException e) {
			e.printStackTrace();
		}
//        try {
//            img = ImageIO.read(new File("startImage/AAPL.png"));
//        } catch (IOException e) {
//        }

//        labelLogo = new JLabel(new ImageIcon(img));
		labelLogo = new JLabel(new ImageIcon(image));
		labelLogo.setText("Logo URL:  " + linkLogo + linkLogoSymbol + ".png");
		labelLogo.setFont(labelLogo.getFont().deriveFont(13f));
		labelLogo.addMouseListener(urlAction);
		mainPanel.add(labelLogo, BorderLayout.CENTER);
		repaint();
		revalidate();
	}

	// importing id symbols and putting it to list of strings
	private ArrayList<String> listOfStockSymbols() {
		ArrayList<String> listOfStockSymbols = new Thread("Importing symbols") {
			ArrayList<String> run2() {
				System.out.println("Thread: " + getName() + " is running");
				return ImportExcel.importSymbolsFromExcel(path2);
			}
		}.run2();

		path2 = inputField.getText();

		return listOfStockSymbols;
	}

	// Creating 2D data array
	private Object[][] addingValuesToArrays(ArrayList<String> listOfStockSymbols)
			throws JSONException, IOException, SQLException {
		connectionToAPI = new ConnectionToAPI();
		for (String symbol : listOfStockSymbols)
			if (!symbol.equals("")) {
				connectionToAPI.extractPrices(symbol);
			}
		log.writeLogToDisk("\\apiLog.json");
		Object[][] mainDataArrays = new Object[listOfStockSymbols.size()][listOfStockSymbols.size()];
		final AtomicBoolean terminate = new AtomicBoolean(false);
		new Thread("Adding values") {
			@Override
			public void run() {
				jProgressBar1.setValue(100);
				System.out.println("Thread: " + getName() + " is running");
				while (!terminate.get()) {
					appTitle.setText("Extracting...");
					terminate.set(true);
					try {
						jProgressBar1.setStringPainted(true);
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (terminate.get())
						appTitle.setText("API Extractor");
					jProgressBar1.setValue(0);
				}
			}
		}.start();
		int br = 1;

		// adding values to 2d array
		for (String s : listOfStockSymbols) {
			Object[] helpArray = connectionToAPI.connectToAPIAndParseValues(s);
			if (helpArray[4] != "NO DATA") {
				System.arraycopy(helpArray, 0, mainDataArrays[br], 0, 5);
				br++;
			}
		}
		return mainDataArrays;
	}

	private void loadValues() throws FileNotFoundException {
		try {
			for (String companyName : companyNames) {
				if (companyName.equalsIgnoreCase(ImportExcel.importSymbolsFromExcel(TradingApplication.path2).get(0))) {
					// instantiate 2d array for values which size is equal to a size of symbols list
					// plus rows header
					Object[][] mainDataArrays = addingValuesToArrays(listOfStockSymbols());
					log.addToLog("Values have been successfully loaded from IEXTrading API." + "\n");
				}
			}
			log.addToLog("Supported symbols are: " + Arrays.toString(companyNames) + "\n"
					+ "--------------------------------------------------------------" + "\n");
		} catch (JSONException | IOException ex) {
			log.addToLog("Exception caught:" + ex);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void initComponents() {

		appTitle = new javax.swing.JLabel();
		selectField = new javax.swing.JLabel();
		inputField = new javax.swing.JTextField();
		selectButton = new javax.swing.JButton();
		exportButton = new javax.swing.JButton();
		jProgressBar1 = new JProgressBar();
		changeLogoButton = new javax.swing.JButton();
		outputText = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jProgressBar1.setValue(jProgressBar1.getValue());
		jProgressBar1.setStringPainted(true);
		Border border = BorderFactory.createTitledBorder("Stream progress");
		jProgressBar1.setBorder(border);

		mainPanel.setForeground(new java.awt.Color(51, 51, 51));

		appTitle.setText("API Extractor");
		appTitle.setFont(appTitle.getFont().deriveFont(16f));

		selectField.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
		selectField.setText("Select Symbol.xlsx path:");
		selectField.setToolTipText("");

		inputField.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
		inputField.setText("Choose file path");

		selectButton.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
		selectButton.setText("Choose");
		selectButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				chooseButton(evt);
			}
		});

		exportButton.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
		exportButton.setText("Get report");
		exportButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				getReportButton(evt);
			}
		});

		changeLogoButton.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
		changeLogoButton.setText("Get partner logo");
		changeLogoButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				showPartnerButton(evt);
			}
		});

		outputText.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
		outputText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(mainPanel);
		mainPanel.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel1Layout.createSequentialGroup()
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
						.addGroup(jPanel1Layout.createSequentialGroup().addComponent(inputField)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(selectButton))
						.addGroup(jPanel1Layout.createSequentialGroup()
								.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(jPanel1Layout.createSequentialGroup().addGap(284, 284, 284)
												.addComponent(appTitle))
										.addComponent(selectField, javax.swing.GroupLayout.PREFERRED_SIZE, 399,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGap(0, 0, Short.MAX_VALUE)))
						.addContainerGap())
				.addGroup(jPanel1Layout.createSequentialGroup().addGap(235, 235, 235)
						.addComponent(exportButton, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
						.addGap(249, 249, 249))
				.addComponent(changeLogoButton, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
				.addComponent(jProgressBar1)
				.addGroup(jPanel1Layout.createSequentialGroup().addComponent(outputText,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
						.addContainerGap().addGap(50, 50, 50).addComponent(appTitle).addGap(40, 40, 40)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE))
						.addGap(24, 24, 24).addComponent(selectField)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(inputField, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(selectButton))
						.addGap(18, 18, 18)
						.addComponent(outputText, javax.swing.GroupLayout.PREFERRED_SIZE, 182,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addComponent(exportButton, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
						.addGap(18, 18, 18)
						.addComponent(changeLogoButton, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
						.addContainerGap())
				.addComponent(jProgressBar1)

		);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(mainPanel,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(mainPanel,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));

		pack();
		setLocationRelativeTo(null);
	}

	private void chooseButton(java.awt.event.ActionEvent evt) {
		JFileChooser jfc = new JFileChooser();
		jfc.showDialog(null, "Select path");
		jfc.setVisible(true);
		try {
			File filename = jfc.getSelectedFile();
			if (filename.getName().equalsIgnoreCase("Symbol.xlsx")) {
				path2 = filename.getPath();
				inputField.setText(path2);
//                log.addToLog("File loaded: " + filename.getName() + " under " + filename.getPath()
//                        + "\n");
				// error pane if Symbol.xlsx is opened inside the project dir
				if (CustomLogger.checkIfFileIsOpened(path2)) {
					JOptionPane.showMessageDialog(null, "Symbol.xlsx is opened - please close it!",
							"Symbol.xlsx opened", JOptionPane.ERROR_MESSAGE);
				} else {
					// Symbol.xlsx was selected info
					JOptionPane.showMessageDialog(null, "Symbol.xlsx was selected", "Success!",
							JOptionPane.INFORMATION_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Please find Symbol.xlsx", "Wrong file selected",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (NullPointerException e) {
//            log.addToLog("Nothing is selected");
		}
	}

	private void getReportButton(java.awt.event.ActionEvent evt) {
		if (!path2.equals("")) {
			try {
				loadValues();
				DbConnectivity.main(null);
			} catch (FileNotFoundException ex) {
				Logger.getLogger(TradingApplication.class.getName()).log(Level.SEVERE, null, ex);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "File saved under " + System.getProperty("user.home") + "\\Desktop",
					"File saved", JOptionPane.INFORMATION_MESSAGE);
//            outputText.setText("File saved under " + System.getProperty("user.home") + "\\Desktop");
		} else {
			// outputText.setText("Please select correct files");
			JOptionPane.showMessageDialog(null, "Please select filepath", "Path is not selected",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void showPartnerButton(java.awt.event.ActionEvent evt) {
		Random r = new Random();
		final AtomicBoolean terminate = new AtomicBoolean(false);
		new Thread("Adding values") {
			@Override
			public void run() {
				for (String companyName : TradingApplication.companyNames) {
					jProgressBar1.setValue(100);
					while (!terminate.get()) {
						appTitle.setText("Extracting...");
						terminate.set(true);
						try {
							urlLogo = new URL(linkLogo + companyNames[r.nextInt(companyNames.length)] + ".png");
							URLConnection uc = urlLogo.openConnection();
							uc.setRequestProperty("User-Agent",
									"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_5_8; en-US) AppleWebKit/532.5 (KHTML, like Gecko) Chrome/4.0.249.0 Safari/532.5");
							uc.connect();
							image = ImageIO.read(urlLogo.openStream());
							jProgressBar1.setStringPainted(true);
							Thread.sleep(2000);
						} catch (IOException | InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (terminate.get()) {
						appTitle.setText("API Extractor");
					}
					jProgressBar1.setValue(0);
					labelLogo.setIcon(new ImageIcon(image));
					labelLogo.setText("Logo URL:  " + urlLogo);
					labelLogo.setFont(labelLogo.getFont().deriveFont(13f));
				}
			}
		}.start();
		urlAction = null;
		labelLogo.addMouseListener(urlAction);
	}

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(() -> new TradingApplication().setVisible(true));
		urlAction = OpenUrlAction.getInstance();
	}
}