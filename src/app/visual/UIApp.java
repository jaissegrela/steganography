package app.visual;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_highgui;

import core.algorithm.KeyPointRaw_HH_Algorithm;
import core.algorithm.KeyPointRaw_Parameter;
import core.message.CacheMessage;
import core.message.ICoverMessage;
import core.message.MatImage;
import core.utils.AccuracyEvaluator;
import core.utils.Utils;

public class UIApp {

	private JFrame frmStegoApplication;
	private JTextField tfCoverMessage;
	// Create a file chooser
	final JFileChooser fc = new JFileChooser();
	private JLabel lblMessageInfo;
	private JScrollPane pCoverMessage;
	private ImageFilter imageFilter;
	private JLabel lblCoverMessage;
	private JLabel lblMessage;

	private MatImage coverMessage1;
	private MatImage image;
	private JTextField tfImage;
	private MatImage coverMessage2;
	protected ICoverMessage stegoObject;
	private JButton btnSave;

	private JTextField tfOriginalImage;
	private JComboBox cBox;

	private String[] names = { "User 1", "User 2", "User 3" };
	private byte[][] signatures = { { -101, 65, 78 }, { 78, -101, 65 }, { 65, 78, 101 } };
	private JButton btn_hidden_Apply;
	private JButton btn_extract_Apply;
	private JTextPane txtInfo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIApp window = new UIApp();
					window.frmStegoApplication.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UIApp() {
		initialize();

		Loader.load(opencv_core.class);
		imageFilter = new ImageFilter();
		fc.addChoosableFileFilter(imageFilter);
		fc.setAcceptAllFileFilterUsed(false);

		for (int i = 0; i < names.length; i++)
			cBox.addItem(names[i]);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmStegoApplication = new JFrame();
		final String title = "Stegano Application 0.5.0";
		frmStegoApplication.setTitle(title);
		frmStegoApplication.setBounds(100, 100, 654, 489);
		frmStegoApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStegoApplication.setMinimumSize(new Dimension(500, 400));
		SpringLayout springLayout = new SpringLayout();
		frmStegoApplication.getContentPane().setLayout(springLayout);

		JPanel basePanel = new JPanel();
		SpringLayout baseLayout = new SpringLayout();
		basePanel.setLayout(baseLayout);
		springLayout.putConstraint(SpringLayout.NORTH, basePanel, 0, SpringLayout.NORTH,
				frmStegoApplication.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, basePanel, 0, SpringLayout.SOUTH,
				frmStegoApplication.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, basePanel, 0, SpringLayout.WEST,
				frmStegoApplication.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, basePanel, 0, SpringLayout.EAST,
				frmStegoApplication.getContentPane());
		frmStegoApplication.getContentPane().add(basePanel);

		final JPanel statusPanel = new JPanel();
		SpringLayout statusLayout = new SpringLayout();
		statusPanel.setLayout(statusLayout);
		baseLayout.putConstraint(SpringLayout.WEST, statusPanel, 0, SpringLayout.WEST, basePanel);
		baseLayout.putConstraint(SpringLayout.NORTH, statusPanel, -20, SpringLayout.SOUTH, basePanel);
		baseLayout.putConstraint(SpringLayout.SOUTH, statusPanel, 0, SpringLayout.SOUTH, basePanel);
		baseLayout.putConstraint(SpringLayout.EAST, statusPanel, 0, SpringLayout.EAST, basePanel);
		basePanel.add(statusPanel);

		final JPanel hiddenPanel = new JPanel();
		baseLayout.putConstraint(SpringLayout.NORTH, hiddenPanel, 0, SpringLayout.NORTH, basePanel);
		baseLayout.putConstraint(SpringLayout.WEST, hiddenPanel, 0, SpringLayout.WEST, basePanel);
		baseLayout.putConstraint(SpringLayout.SOUTH, hiddenPanel, 0, SpringLayout.NORTH, statusPanel);
		baseLayout.putConstraint(SpringLayout.EAST, hiddenPanel, 0, SpringLayout.EAST, basePanel);
		SpringLayout hiddenLayout = new SpringLayout();
		hiddenPanel.setLayout(hiddenLayout);
		basePanel.add(hiddenPanel);
		hiddenPanel.setVisible(false);

		JPanel hidden_top1_panel = new JPanel();
		hiddenLayout.putConstraint(SpringLayout.NORTH, hidden_top1_panel, 5, SpringLayout.NORTH, hiddenPanel);
		hiddenLayout.putConstraint(SpringLayout.WEST, hidden_top1_panel, 10, SpringLayout.WEST, hiddenPanel);
		hiddenLayout.putConstraint(SpringLayout.SOUTH, hidden_top1_panel, 35, SpringLayout.NORTH, hiddenPanel);
		hiddenLayout.putConstraint(SpringLayout.EAST, hidden_top1_panel, -10, SpringLayout.EAST, hiddenPanel);
		hiddenPanel.add(hidden_top1_panel);

		JPanel hidden_bottom_panel = new JPanel();
		hiddenLayout.putConstraint(SpringLayout.NORTH, hidden_bottom_panel, -31, SpringLayout.SOUTH, hiddenPanel);
		hiddenLayout.putConstraint(SpringLayout.WEST, hidden_bottom_panel, 10, SpringLayout.WEST, hiddenPanel);
		hiddenLayout.putConstraint(SpringLayout.SOUTH, hidden_bottom_panel, -5, SpringLayout.SOUTH, hiddenPanel);
		hiddenLayout.putConstraint(SpringLayout.EAST, hidden_bottom_panel, -10, SpringLayout.EAST, hiddenPanel);
		hiddenPanel.add(hidden_bottom_panel);

		JSplitPane hidden_splitPanel = new JSplitPane();
		hiddenLayout.putConstraint(SpringLayout.NORTH, hidden_splitPanel, 6, SpringLayout.SOUTH, hidden_top1_panel);
		hiddenLayout.putConstraint(SpringLayout.WEST, hidden_splitPanel, 10, SpringLayout.WEST, hiddenPanel);
		hiddenLayout.putConstraint(SpringLayout.SOUTH, hidden_splitPanel, -6, SpringLayout.NORTH, hidden_bottom_panel);
		hiddenLayout.putConstraint(SpringLayout.EAST, hidden_splitPanel, -10, SpringLayout.EAST, hiddenPanel);
		hidden_splitPanel.setResizeWeight(0.5);
		hiddenPanel.add(hidden_splitPanel);

		SpringLayout sl_hidden_top1_panel = new SpringLayout();
		hidden_top1_panel.setLayout(sl_hidden_top1_panel);

		JLabel lblLabel1 = new JLabel("Image:");
		sl_hidden_top1_panel.putConstraint(SpringLayout.NORTH, lblLabel1, 2, SpringLayout.NORTH, hidden_top1_panel);
		sl_hidden_top1_panel.putConstraint(SpringLayout.WEST, lblLabel1, 0, SpringLayout.WEST, hidden_top1_panel);
		sl_hidden_top1_panel.putConstraint(SpringLayout.SOUTH, lblLabel1, 27, SpringLayout.NORTH, hidden_top1_panel);
		lblLabel1.setAlignmentX(1.0f);
		lblLabel1.setHorizontalAlignment(SwingConstants.LEFT);
		hidden_top1_panel.add(lblLabel1);

		tfCoverMessage = new JTextField();
		sl_hidden_top1_panel.putConstraint(SpringLayout.WEST, tfCoverMessage, 6, SpringLayout.EAST, lblLabel1);
		sl_hidden_top1_panel.putConstraint(SpringLayout.EAST, tfCoverMessage, -219, SpringLayout.EAST,
				hidden_top1_panel);
		tfCoverMessage.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				warn();
			}

			public void removeUpdate(DocumentEvent e) {
				warn();
			}

			public void insertUpdate(DocumentEvent e) {
				warn();
			}

			public void warn() {
				frmStegoApplication.setCursor (Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				setCoverMessage1(null);
				setCoverMessage1(showImage(tfCoverMessage.getText(), lblCoverMessage));
				frmStegoApplication.setCursor (Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		sl_hidden_top1_panel
				.putConstraint(SpringLayout.NORTH, tfCoverMessage, 2, SpringLayout.NORTH, hidden_top1_panel);
		sl_hidden_top1_panel.putConstraint(SpringLayout.SOUTH, tfCoverMessage, 27, SpringLayout.NORTH,
				hidden_top1_panel);
		tfCoverMessage.setAlignmentX(Component.RIGHT_ALIGNMENT);
		tfCoverMessage.setHorizontalAlignment(SwingConstants.LEFT);
		hidden_top1_panel.add(tfCoverMessage);
		tfCoverMessage.setColumns(10);

		JButton btnCoverMessageBrowser = new JButton("...");
		sl_hidden_top1_panel
				.putConstraint(SpringLayout.NORTH, btnCoverMessageBrowser, 1, SpringLayout.NORTH, lblLabel1);
		sl_hidden_top1_panel.putConstraint(SpringLayout.WEST, btnCoverMessageBrowser, 6, SpringLayout.EAST,
				tfCoverMessage);
		btnCoverMessageBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnOpenShowDialogActionPerformed(tfCoverMessage);
			}
		});
		hidden_top1_panel.add(btnCoverMessageBrowser);

		JLabel lblLabel2 = new JLabel("Identifier:");
		sl_hidden_top1_panel.putConstraint(SpringLayout.NORTH, lblLabel2, 0, SpringLayout.NORTH, lblLabel1);
		sl_hidden_top1_panel.putConstraint(SpringLayout.WEST, lblLabel2, 6, SpringLayout.EAST, btnCoverMessageBrowser);
		sl_hidden_top1_panel.putConstraint(SpringLayout.SOUTH, lblLabel2, 27, SpringLayout.NORTH, hidden_top1_panel);
		hidden_top1_panel.add(lblLabel2);

		cBox = new JComboBox();
		sl_hidden_top1_panel.putConstraint(SpringLayout.NORTH, cBox, 0, SpringLayout.NORTH, lblLabel1);
		sl_hidden_top1_panel.putConstraint(SpringLayout.WEST, cBox, 6, SpringLayout.EAST, lblLabel2);
		sl_hidden_top1_panel.putConstraint(SpringLayout.SOUTH, cBox, 0, SpringLayout.SOUTH, lblLabel1);
		sl_hidden_top1_panel.putConstraint(SpringLayout.EAST, cBox, -10, SpringLayout.EAST, hidden_top1_panel);
		hidden_top1_panel.add(cBox);

		btn_hidden_Apply = new JButton("Apply");
		btn_hidden_Apply.setEnabled(false);
		btn_hidden_Apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applySteganographyMethod();
			}
		});
		hidden_bottom_panel.setLayout(new BorderLayout(0, 0));
		hidden_bottom_panel.add(btn_hidden_Apply, BorderLayout.WEST);

		btnSave = new JButton("Save");
		btnSave.setEnabled(false);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveStegoObject();
			}
		});
		hidden_bottom_panel.add(btnSave, BorderLayout.EAST);

		pCoverMessage = new JScrollPane();
		hidden_splitPanel.setLeftComponent(pCoverMessage);

		lblCoverMessage = new JLabel("");
		pCoverMessage.setViewportView(lblCoverMessage);

		JScrollPane pMessage = new JScrollPane();
		hidden_splitPanel.setRightComponent(pMessage);

		lblMessage = new JLabel("");
		pMessage.setViewportView(lblMessage);

		hiddenPanel.setVisible(true);

		final JPanel extractPanel = new JPanel();
		SpringLayout extractLayout = new SpringLayout();
		extractPanel.setLayout(extractLayout);
		baseLayout.putConstraint(SpringLayout.WEST, extractPanel, 0, SpringLayout.WEST, basePanel);
		baseLayout.putConstraint(SpringLayout.NORTH, extractPanel, 0, SpringLayout.NORTH, basePanel);
		baseLayout.putConstraint(SpringLayout.SOUTH, extractPanel, 0, SpringLayout.NORTH, statusPanel);
		baseLayout.putConstraint(SpringLayout.EAST, extractPanel, 0, SpringLayout.EAST, basePanel);
		basePanel.add(extractPanel);
		extractPanel.setVisible(false);

		lblMessageInfo = new JLabel("");
		statusLayout.putConstraint(SpringLayout.NORTH, lblMessageInfo, -0, SpringLayout.NORTH, statusPanel);
		statusLayout.putConstraint(SpringLayout.WEST, lblMessageInfo, 0, SpringLayout.WEST, statusPanel);
		statusLayout.putConstraint(SpringLayout.SOUTH, lblMessageInfo, 0, SpringLayout.SOUTH, statusPanel);
		statusLayout.putConstraint(SpringLayout.EAST, lblMessageInfo, 0, SpringLayout.EAST, statusPanel);
		lblMessageInfo.setVerticalAlignment(SwingConstants.BOTTOM);
		statusPanel.add(lblMessageInfo);

		JMenuBar menuBar = new JMenuBar();
		frmStegoApplication.setJMenuBar(menuBar);

		JMenu mnSteganogrphy = new JMenu("Hidden Message");
		mnSteganogrphy.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				frmStegoApplication.setTitle(title + " - Hidden Message");
				extractPanel.setVisible(false);
				hiddenPanel.setVisible(true);
			}
		});

		menuBar.add(mnSteganogrphy);

		JMenu mnSteganoanalisys = new JMenu("Extract Message");
		mnSteganoanalisys.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frmStegoApplication.setTitle(title + " - Extract Message");
				hiddenPanel.setVisible(false);
				extractPanel.setVisible(true);
			}
		});

		menuBar.add(mnSteganoanalisys);

		JPanel extract_top_panel = new JPanel();
		extractLayout.putConstraint(SpringLayout.NORTH, extract_top_panel, 5, SpringLayout.NORTH, extractPanel);
		extractLayout.putConstraint(SpringLayout.SOUTH, extract_top_panel, 61, SpringLayout.NORTH, extractPanel);
		extractLayout.putConstraint(SpringLayout.WEST, extract_top_panel, 10, SpringLayout.WEST, extractPanel);
		extractLayout.putConstraint(SpringLayout.EAST, extract_top_panel, -10, SpringLayout.EAST, extractPanel);
		extractPanel.add(extract_top_panel);

		SpringLayout sl_extract_top_panel = new SpringLayout();
		extract_top_panel.setLayout(sl_extract_top_panel);

		JLabel lblExtratedLabel1 = new JLabel("Image:");
		sl_extract_top_panel.putConstraint(SpringLayout.NORTH, lblExtratedLabel1, 2, SpringLayout.NORTH,
				extract_top_panel);
		sl_extract_top_panel.putConstraint(SpringLayout.WEST, lblExtratedLabel1, 0, SpringLayout.WEST,
				extract_top_panel);
		sl_extract_top_panel.putConstraint(SpringLayout.SOUTH, lblExtratedLabel1, 27, SpringLayout.NORTH,
				extract_top_panel);
		lblExtratedLabel1.setHorizontalAlignment(SwingConstants.LEFT);
		extract_top_panel.add(lblExtratedLabel1);

		tfImage = new JTextField();
		sl_extract_top_panel.putConstraint(SpringLayout.WEST, tfImage, 10, SpringLayout.EAST, lblExtratedLabel1);
		tfImage.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				warn();
			}

			public void removeUpdate(DocumentEvent e) {
				warn();
			}

			public void insertUpdate(DocumentEvent e) {
				warn();
			}

			public void warn() {
				frmStegoApplication.setCursor (Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				setCoverMessage2(getImage(tfImage.getText()));
				frmStegoApplication.setCursor (Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		sl_extract_top_panel.putConstraint(SpringLayout.NORTH, tfImage, 2, SpringLayout.NORTH, extract_top_panel);
		sl_extract_top_panel.putConstraint(SpringLayout.SOUTH, tfImage, 27, SpringLayout.NORTH, extract_top_panel);
		tfImage.setHorizontalAlignment(SwingConstants.LEFT);
		extract_top_panel.add(tfImage);
		tfImage.setColumns(10);

		JButton btnImageBrowser = new JButton("...");
		sl_extract_top_panel.putConstraint(SpringLayout.EAST, tfImage, -10, SpringLayout.WEST, btnImageBrowser);
		btnImageBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnOpenShowDialogActionPerformed(tfImage);
			}
		});
		sl_extract_top_panel.putConstraint(SpringLayout.EAST, btnImageBrowser, 0, SpringLayout.EAST, extract_top_panel);
		extract_top_panel.add(btnImageBrowser);

		JLabel lblOriginal = new JLabel("Original:");
		sl_extract_top_panel.putConstraint(SpringLayout.NORTH, lblOriginal, 2, SpringLayout.SOUTH, lblExtratedLabel1);
		sl_extract_top_panel.putConstraint(SpringLayout.WEST, lblOriginal, 0, SpringLayout.WEST, lblExtratedLabel1);
		sl_extract_top_panel.putConstraint(SpringLayout.SOUTH, lblOriginal, 27, SpringLayout.SOUTH, lblExtratedLabel1);
		lblOriginal.setHorizontalAlignment(SwingConstants.LEFT);
		extract_top_panel.add(lblOriginal);

		tfOriginalImage = new JTextField();
		sl_extract_top_panel.putConstraint(SpringLayout.NORTH, tfOriginalImage, 0, SpringLayout.NORTH, lblOriginal);
		sl_extract_top_panel.putConstraint(SpringLayout.WEST, tfOriginalImage, 0, SpringLayout.WEST, tfImage);
		sl_extract_top_panel.putConstraint(SpringLayout.SOUTH, tfOriginalImage, 0, SpringLayout.SOUTH, lblOriginal);
		sl_extract_top_panel.putConstraint(SpringLayout.EAST, tfOriginalImage, 0, SpringLayout.EAST, tfImage);
		extract_top_panel.add(tfOriginalImage);

		tfOriginalImage.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				warn();
			}

			public void removeUpdate(DocumentEvent e) {
				warn();
			}

			public void insertUpdate(DocumentEvent e) {
				warn();
			}

			public void warn() {
				frmStegoApplication.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				setImage(getImage(tfOriginalImage.getText()));
				frmStegoApplication.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});

		JButton btnOriginalImage = new JButton("...");
		btnOriginalImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnOpenShowDialogActionPerformed(tfOriginalImage);
			}
		});
		sl_extract_top_panel.putConstraint(SpringLayout.WEST, btnOriginalImage, 0, SpringLayout.WEST, btnImageBrowser);
		sl_extract_top_panel.putConstraint(SpringLayout.SOUTH, btnOriginalImage, 0, SpringLayout.SOUTH, lblOriginal);
		extract_top_panel.add(btnOriginalImage);

		JPanel extract_bottom_panel = new JPanel();
		extractLayout.putConstraint(SpringLayout.NORTH, extract_bottom_panel, -31, SpringLayout.SOUTH, extractPanel);
		extractLayout.putConstraint(SpringLayout.WEST, extract_bottom_panel, 10, SpringLayout.WEST, extractPanel);
		extractLayout.putConstraint(SpringLayout.SOUTH, extract_bottom_panel, -5, SpringLayout.SOUTH, extractPanel);
		extractLayout.putConstraint(SpringLayout.EAST, extract_bottom_panel, -10, SpringLayout.EAST, extractPanel);
		extractPanel.add(extract_bottom_panel);
		SpringLayout sl_extract_bottom_panel = new SpringLayout();
		extract_bottom_panel.setLayout(sl_extract_bottom_panel);

		btn_extract_Apply = new JButton("Apply");
		btn_extract_Apply.setEnabled(false);
		sl_extract_bottom_panel.putConstraint(SpringLayout.NORTH, btn_extract_Apply, 0, SpringLayout.NORTH,
				extract_bottom_panel);
		sl_extract_bottom_panel.putConstraint(SpringLayout.WEST, btn_extract_Apply, 140, SpringLayout.WEST,
				extract_bottom_panel);
		sl_extract_bottom_panel.putConstraint(SpringLayout.EAST, btn_extract_Apply, -140, SpringLayout.EAST,
				extract_bottom_panel);
		btn_extract_Apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applySteganoAnalisysMethod();
			}
		});
		extract_bottom_panel.add(btn_extract_Apply);

		JScrollPane scrollPane = new JScrollPane();
		extractLayout.putConstraint(SpringLayout.NORTH, scrollPane, 15, SpringLayout.SOUTH, extract_top_panel);
		extractLayout.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, extractPanel);
		extractLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -6, SpringLayout.NORTH, extract_bottom_panel);
		extractLayout.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.EAST, extractPanel);
		extractPanel.add(scrollPane);

		txtInfo = new JTextPane();
		txtInfo.setEditable(false);
		scrollPane.setViewportView(txtInfo);
		extractPanel.setVisible(false);

	}

	private MatImage showImage(String address, JLabel lblImage) {
		File file = new File(address);
		showMessage("Opening: " + file.getName());
		MatImage result = getImage(address);
		if (result != null) {
			loadImage(file, lblImage);
		} else {
			showMessage("It's a wrong file");
		}
		return result;
	}

	private MatImage getImage(String address) {
		File file = new File(address);
		if (file.exists() && imageFilter.accept(file)) {
			showMessage("Reading: " + file.getName());
			Mat original = opencv_highgui.imread(address, opencv_highgui.CV_LOAD_IMAGE_UNCHANGED);
			if (original.size().width() == 0)
				showMessage("Sorry, the image couldn't be loaded");
			else{
				showMessage("");
				return new MatImage(original, Utils.getExtension(address));
			}
		} else {
			showMessage("It's a wrong file");
		}
		return null;
	}

	private boolean loadImage(File stream, JLabel canvas) {
		try {
			Image image = ImageIO.read(stream);
			ImageIcon imageIcon = new ImageIcon(image);
			canvas.setText("");
			canvas.setIcon(imageIcon);
			showMessage("");
		} catch (Exception e) {
			canvas.setIcon(null);
			showMessage("Sorry, the image cannot be shown. Some image type like .TIFF cannot be shown.");
		}
		return true;
	}

	private void btnOpenShowDialogActionPerformed(JTextField textField) {
		try {
			int returnVal = fc.showOpenDialog(frmStegoApplication);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				textField.setText(file.getPath());
			} else {
				showMessage("Open command cancelled by user.");
			}
		} catch (Exception e) {
			showMessage("Error showing the image");
		}
	}

	private void showMessage(String message) {
		lblMessageInfo.setText(message);
	}

	private void setStegoObject(MatImage object) {
		stegoObject = object;
		btnSave.setEnabled(stegoObject != null);
		if(stegoObject == null)
		{
			lblMessage.setIcon(null);
		}
	}

	private void setImage(MatImage object) {
		image = object;
		btn_extract_Apply.setEnabled(image != null && coverMessage2 != null);
	}

	private void setCoverMessage1(MatImage object) {
		coverMessage1 = object;
		btn_hidden_Apply.setEnabled(coverMessage1 != null);
		if(coverMessage1 == null)
		{
			lblCoverMessage.setIcon(null);
		}
	}

	private void setCoverMessage2(MatImage object) {
		coverMessage2 = object;
		btn_extract_Apply.setEnabled(image != null && coverMessage2 != null);
	}

	protected void saveStegoObject() {
		if (stegoObject == null) {
			showMessage("There is nothing to save");
			return;
		}
		int returnVal = fc.showSaveDialog(frmStegoApplication);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String path = fc.getSelectedFile().getPath();
			frmStegoApplication.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			opencv_highgui.cvSaveImage(path, stegoObject.getMat().asIplImage());
			frmStegoApplication.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			showMessage("File was saved successfully.");
		} else {
			showMessage("Open command cancelled by user.");
		}
	}
	
	private void applySteganographyMethod() {
		frmStegoApplication.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setStegoObject(null);

		if (coverMessage1 == null) {
			showMessage("Please, select the cover message file.");
			return;
		}

		CacheMessage message = new CacheMessage(signatures[cBox.getSelectedIndex()]);

		KeyPointRaw_Parameter params = new KeyPointRaw_Parameter(coverMessage1);

		int keyPointSize = params.getKeyPointSize();
		int pointsByBit = params.getPointsByBit();
		int visibilityfactor = (int) params.getVisibilityfactor();
		
		System.out.println(String.format("Paramaters: M = %s, Q = %s, F = %s", keyPointSize, pointsByBit,
				visibilityfactor));
		
		KeyPointRaw_HH_Algorithm algorithm = new KeyPointRaw_HH_Algorithm(coverMessage1, keyPointSize,
				pointsByBit * 24, pointsByBit, visibilityfactor, null);

		setStegoObject((MatImage) algorithm.getStegoObject(message));
		String name = "_temp." + coverMessage1.getExtension();
		opencv_highgui.cvSaveImage(name, stegoObject.getMat().asIplImage());
		showImage(name, lblMessage);
		frmStegoApplication.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	private void applySteganoAnalisysMethod() {

		frmStegoApplication.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		txtInfo.setText("");

		if (image == null) {
			showMessage("Please, select the image message file.");
			return;
		}

		if (coverMessage2 == null) {
			showMessage("Please, select the original image file.");
			return;
		}

		KeyPointRaw_Parameter params = new KeyPointRaw_Parameter(coverMessage2);
		
		int keyPointSize = params.getKeyPointSize();
		int pointsByBit = params.getPointsByBit();
		int visibilityfactor = (int) params.getVisibilityfactor();
		
		System.out.println(String.format("Paramaters: M = %s, Q = %s, F = %s", keyPointSize, pointsByBit,
				visibilityfactor));

		KeyPointRaw_HH_Algorithm algorithm = new KeyPointRaw_HH_Algorithm(image, keyPointSize,
				pointsByBit * 24, pointsByBit, visibilityfactor, coverMessage2);
		AccuracyEvaluator evaluator = new AccuracyEvaluator(null, params.getPointsByBit());

		algorithm.setEvaluator(evaluator);
		algorithm.getEmbeddedData();

		double max = 0;
		String user = "";

		StringBuilder text = new StringBuilder();
		text.append(String.format("Filename: %s\n\n", tfImage.getText()));
		text.append("Name\tADR\tBER\n");
		for (int i = 0; i < names.length; i++) {
			evaluator.setMessage(new CacheMessage(signatures[i]));
			double accurancy = evaluator.sincronizationResultAccurancy();
			if (accurancy > max) {
				max = accurancy;
				user = names[i];
			}
			text.append(String.format("%s\t%6.2f\t%6.3f\n", names[i], accurancy,
					evaluator.minorStepResultErrorAccurancy()));
		}
		if (max > 85)
			text.append(String.format("\nThe owner of the file is %s", user));
		else
			text.append("\nThe results are not conclusive.");
		txtInfo.setText(text.toString());
		frmStegoApplication.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
}
