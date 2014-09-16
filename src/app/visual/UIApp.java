package app.visual;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import core.algorithm.DWT2D_HL_LH_Algorithm;
import core.algorithm.KeyPointImageAlgorithm;
import core.message.CacheMessage;
import core.message.FileMessage;
import core.message.ICoverMessage;
import core.message.IMessage;
import core.message.MatImage;
import core.transform.FastDiscreteBiorthogonal_CDF_9_7;
import core.transform.Transform2d;
import core.transform.Transform2dBasic;
import core.utils.ImageFactory;
import core.utils.Utils;

public class UIApp {

	private JFrame frmStegoApplication;
	private JTextField tfCoverMessage;
	private JTextField tfMessage;
	// Create a file chooser
	final JFileChooser fc = new JFileChooser();
	private JLabel lblMessageInfo;
	private JScrollPane pCoverMessage;
	private ImageFilter imageFilter;
	private JLabel lblCoverMessage;
	private JLabel lblMessage;

	private ICoverMessage coverMessage;
	private FileMessage message;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField tfImage;
	private JScrollPane pImage;
	private JLabel lblImageMessage;
	private JLabel lblMessageExtracted;
	protected ICoverMessage imageMessage;
	protected ICoverMessage stegoObject;
	protected ICoverMessage originalMessage;
	private JButton btnSave;
	
	protected final double visibilityfactor = 7;
	protected final int keyPointSize = 128;
	protected final int howManyPoints = 2;
	private JTextField tfOriginalImage;

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

		System.loadLibrary("opencv_java249");
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		imageFilter = new ImageFilter();
		fc.addChoosableFileFilter(imageFilter);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmStegoApplication = new JFrame();
		final String title = "Stegano Application 0.0.5";
		frmStegoApplication.setTitle(title);
		frmStegoApplication.setBounds(100, 100, 654, 489);
		frmStegoApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStegoApplication.setMinimumSize(new Dimension(500, 400));
		SpringLayout springLayout = new SpringLayout();
		frmStegoApplication.getContentPane().setLayout(springLayout);

		JPanel basePanel = new JPanel();
		SpringLayout baseLayout = new SpringLayout();
		basePanel.setLayout(baseLayout);
		springLayout.putConstraint(SpringLayout.NORTH, basePanel, 0,
				SpringLayout.NORTH, frmStegoApplication.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, basePanel, 0,
				SpringLayout.SOUTH, frmStegoApplication.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, basePanel, 0,
				SpringLayout.WEST, frmStegoApplication.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, basePanel, 0,
				SpringLayout.EAST, frmStegoApplication.getContentPane());
		frmStegoApplication.getContentPane().add(basePanel);

		final JPanel statusPanel = new JPanel();
		SpringLayout statusLayout = new SpringLayout();
		statusPanel.setLayout(statusLayout);
		baseLayout.putConstraint(SpringLayout.WEST, statusPanel, 0,
				SpringLayout.WEST, basePanel);
		baseLayout.putConstraint(SpringLayout.NORTH, statusPanel, -20,
				SpringLayout.SOUTH, basePanel);
		baseLayout.putConstraint(SpringLayout.SOUTH, statusPanel, 0,
				SpringLayout.SOUTH, basePanel);
		baseLayout.putConstraint(SpringLayout.EAST, statusPanel, 0,
				SpringLayout.EAST, basePanel);
		basePanel.add(statusPanel);
		
		final JPanel extractPanel = new JPanel();
		SpringLayout extractLayout = new SpringLayout();
		extractPanel.setLayout(extractLayout);
		baseLayout.putConstraint(SpringLayout.WEST, extractPanel, 0,
				SpringLayout.WEST, basePanel);
		baseLayout.putConstraint(SpringLayout.NORTH, extractPanel, 0,
				SpringLayout.NORTH, basePanel);
		baseLayout.putConstraint(SpringLayout.SOUTH, extractPanel, 0,
				SpringLayout.NORTH, statusPanel);
		baseLayout.putConstraint(SpringLayout.EAST, extractPanel, 0,
				SpringLayout.EAST, basePanel);
		basePanel.add(extractPanel);
		extractPanel.setVisible(false);

		final JPanel hiddenPanel = new JPanel();
		SpringLayout hiddenLayout = new SpringLayout();
		hiddenPanel.setLayout(hiddenLayout);
		baseLayout.putConstraint(SpringLayout.WEST, hiddenPanel, 0,
				SpringLayout.WEST, basePanel);
		baseLayout.putConstraint(SpringLayout.NORTH, hiddenPanel, 0,
				SpringLayout.NORTH, basePanel);
		baseLayout.putConstraint(SpringLayout.SOUTH, hiddenPanel, 0,
				SpringLayout.NORTH, statusPanel);
		baseLayout.putConstraint(SpringLayout.EAST, hiddenPanel, 0,
				SpringLayout.EAST, basePanel);
		basePanel.add(hiddenPanel);
		hiddenPanel.setVisible(false);

		final JPanel attackPanel = new ReportPanel();
		baseLayout.putConstraint(SpringLayout.WEST, attackPanel, 0,
				SpringLayout.WEST, basePanel);
		baseLayout.putConstraint(SpringLayout.NORTH, attackPanel, 0,
				SpringLayout.NORTH, basePanel);
		baseLayout.putConstraint(SpringLayout.SOUTH, attackPanel, 0,
				SpringLayout.NORTH, statusPanel);
		baseLayout.putConstraint(SpringLayout.EAST, attackPanel, 0,
				SpringLayout.EAST, basePanel);
		basePanel.add(attackPanel);
		attackPanel.setVisible(false);

		JPanel hidden_top1_panel = new JPanel();
		hiddenLayout.putConstraint(SpringLayout.NORTH, hidden_top1_panel, 5,
				SpringLayout.NORTH, hiddenPanel);
		hiddenLayout.putConstraint(SpringLayout.WEST, hidden_top1_panel, 10,
				SpringLayout.WEST, hiddenPanel);
		hiddenLayout.putConstraint(SpringLayout.SOUTH, hidden_top1_panel, 35,
				SpringLayout.NORTH, hiddenPanel);
		hiddenLayout.putConstraint(SpringLayout.EAST, hidden_top1_panel, -10,
				SpringLayout.EAST, hiddenPanel);
		hiddenPanel.add(hidden_top1_panel);

		JPanel hidden_bottom_panel = new JPanel();
		hiddenLayout.putConstraint(SpringLayout.NORTH, hidden_bottom_panel,
				-31, SpringLayout.SOUTH, hiddenPanel);
		hiddenLayout.putConstraint(SpringLayout.WEST, hidden_bottom_panel, 10,
				SpringLayout.WEST, hiddenPanel);
		hiddenLayout.putConstraint(SpringLayout.SOUTH, hidden_bottom_panel, -5,
				SpringLayout.SOUTH, hiddenPanel);
		hiddenLayout.putConstraint(SpringLayout.EAST, hidden_bottom_panel, -10,
				SpringLayout.EAST, hiddenPanel);
		hiddenPanel.add(hidden_bottom_panel);

		JSplitPane hidden_splitPanel = new JSplitPane();
		hiddenLayout.putConstraint(SpringLayout.NORTH, hidden_splitPanel, 6, SpringLayout.SOUTH, hidden_top1_panel);
		hiddenLayout.putConstraint(SpringLayout.WEST, hidden_splitPanel, 10,
				SpringLayout.WEST, hiddenPanel);
		hiddenLayout.putConstraint(SpringLayout.SOUTH, hidden_splitPanel, -6, SpringLayout.NORTH, hidden_bottom_panel);
		hiddenLayout.putConstraint(SpringLayout.EAST, hidden_splitPanel, -10,
				SpringLayout.EAST, hiddenPanel);
		hidden_splitPanel.setResizeWeight(0.5);
		hiddenPanel.add(hidden_splitPanel);

		SpringLayout sl_hidden_top1_panel = new SpringLayout();
		hidden_top1_panel.setLayout(sl_hidden_top1_panel);

		JLabel lblLabel1 = new JLabel("Cover Message:");
		sl_hidden_top1_panel.putConstraint(SpringLayout.NORTH, lblLabel1, 2,
				SpringLayout.NORTH, hidden_top1_panel);
		sl_hidden_top1_panel.putConstraint(SpringLayout.WEST, lblLabel1, 0,
				SpringLayout.WEST, hidden_top1_panel);
		sl_hidden_top1_panel.putConstraint(SpringLayout.SOUTH, lblLabel1, 27,
				SpringLayout.NORTH, hidden_top1_panel);
		lblLabel1.setAlignmentX(1.0f);
		lblLabel1.setHorizontalAlignment(SwingConstants.LEFT);
		hidden_top1_panel.add(lblLabel1);

		tfCoverMessage = new JTextField();
		sl_hidden_top1_panel.putConstraint(SpringLayout.WEST, tfCoverMessage, 26, SpringLayout.EAST, lblLabel1);
		tfCoverMessage.getDocument().addDocumentListener(
				new DocumentListener() {
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
						coverMessage = showImage(tfCoverMessage.getText(),
								lblCoverMessage);
					}
				});
		sl_hidden_top1_panel.putConstraint(SpringLayout.NORTH, tfCoverMessage,
				2, SpringLayout.NORTH, hidden_top1_panel);
		sl_hidden_top1_panel.putConstraint(SpringLayout.SOUTH, tfCoverMessage,
				27, SpringLayout.NORTH, hidden_top1_panel);
		tfCoverMessage.setAlignmentX(Component.RIGHT_ALIGNMENT);
		tfCoverMessage.setHorizontalAlignment(SwingConstants.LEFT);
		hidden_top1_panel.add(tfCoverMessage);
		tfCoverMessage.setColumns(10);

		JButton btnCoverMessageBrowser = new JButton("...");
		sl_hidden_top1_panel.putConstraint(SpringLayout.EAST, tfCoverMessage, -6, SpringLayout.WEST, btnCoverMessageBrowser);
		sl_hidden_top1_panel.putConstraint(SpringLayout.NORTH, btnCoverMessageBrowser, 1, SpringLayout.NORTH, lblLabel1);
		btnCoverMessageBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnOpenShowDialogActionPerformed(tfCoverMessage);
			}
		});
		hidden_top1_panel.add(btnCoverMessageBrowser);
		
				JLabel lblLabel2 = new JLabel("Identifier:");
				sl_hidden_top1_panel.putConstraint(SpringLayout.EAST, btnCoverMessageBrowser, -6, SpringLayout.WEST, lblLabel2);
				sl_hidden_top1_panel.putConstraint(SpringLayout.NORTH, lblLabel2, 2, SpringLayout.NORTH, hidden_top1_panel);
				sl_hidden_top1_panel.putConstraint(SpringLayout.WEST, lblLabel2, -100, SpringLayout.EAST, hidden_top1_panel);
				sl_hidden_top1_panel.putConstraint(SpringLayout.SOUTH, lblLabel2, 27, SpringLayout.NORTH, hidden_top1_panel);
				hidden_top1_panel.add(lblLabel2);
		
				tfMessage = new JTextField();
				sl_hidden_top1_panel.putConstraint(SpringLayout.NORTH, tfMessage, 2, SpringLayout.NORTH, hidden_top1_panel);
				sl_hidden_top1_panel.putConstraint(SpringLayout.WEST, tfMessage, -40, SpringLayout.EAST, hidden_top1_panel);
				sl_hidden_top1_panel.putConstraint(SpringLayout.SOUTH, tfMessage, 27, SpringLayout.NORTH, hidden_top1_panel);
				hidden_top1_panel.add(tfMessage);
		tfMessage.getDocument().addDocumentListener(new DocumentListener() {
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
				String text = tfMessage.getText();
				if(text.length() > 3)
					tfMessage.setText(text.substring(0, 3));
			}
		});
		tfMessage.setHorizontalAlignment(SwingConstants.LEFT);
		tfMessage.setColumns(3);

		lblMessageInfo = new JLabel("");
		statusLayout.putConstraint(SpringLayout.NORTH, lblMessageInfo, -0,
				SpringLayout.NORTH, statusPanel);
		statusLayout.putConstraint(SpringLayout.WEST, lblMessageInfo, 0,
				SpringLayout.WEST, statusPanel);
		statusLayout.putConstraint(SpringLayout.SOUTH, lblMessageInfo, 0,
				SpringLayout.SOUTH, statusPanel);
		statusLayout.putConstraint(SpringLayout.EAST, lblMessageInfo, 0,
				SpringLayout.EAST, statusPanel);
		lblMessageInfo.setVerticalAlignment(SwingConstants.BOTTOM);
		statusPanel.add(lblMessageInfo);

		JButton btn_hidden_Apply = new JButton("Apply");
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

		JMenuBar menuBar = new JMenuBar();
		frmStegoApplication.setJMenuBar(menuBar);

		JMenu mnSteganogrphy = new JMenu("Steganography");
		menuBar.add(mnSteganogrphy);

		JRadioButtonMenuItem rdbtnmntmHiddenMessage = new JRadioButtonMenuItem(
				"Hide Message");
		rdbtnmntmHiddenMessage.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				JRadioButtonMenuItem source = (JRadioButtonMenuItem) arg0
						.getSource();
				boolean selected = source.isSelected();
				if (selected) {
					frmStegoApplication.setTitle(title + " - "
							+ source.getText());
				}
				hiddenPanel.setVisible(selected);
			}
		});
		buttonGroup.add(rdbtnmntmHiddenMessage);
		mnSteganogrphy.add(rdbtnmntmHiddenMessage);

		JRadioButtonMenuItem rdbtnmntmExtractMessage = new JRadioButtonMenuItem(
				"Extract Message");
		rdbtnmntmExtractMessage.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				JRadioButtonMenuItem source = (JRadioButtonMenuItem) arg0
						.getSource();
				boolean selected = source.isSelected();
				if (selected) {
					frmStegoApplication.setTitle(title + " - "
							+ source.getText());
				}
				extractPanel.setVisible(selected);
			}
		});
		buttonGroup.add(rdbtnmntmExtractMessage);
		mnSteganogrphy.add(rdbtnmntmExtractMessage);

		JMenu mnSteganoanalisys = new JMenu("Steganalysis");
		menuBar.add(mnSteganoanalisys);

		JRadioButtonMenuItem rdbtnmntmNewRadioItem = new JRadioButtonMenuItem(
				"Analysis");
		rdbtnmntmNewRadioItem.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				JRadioButtonMenuItem source = (JRadioButtonMenuItem) arg0
						.getSource();
				boolean selected = source.isSelected();
				if (selected) {
					frmStegoApplication.setTitle(title + " - "
							+ source.getText());
				}
				attackPanel.setVisible(source.isSelected());
			}
		});
		buttonGroup.add(rdbtnmntmNewRadioItem);
		mnSteganoanalisys.add(rdbtnmntmNewRadioItem);

		JPanel extract_top_panel = new JPanel();
		extractLayout.putConstraint(SpringLayout.NORTH, extract_top_panel, 5,
				SpringLayout.NORTH, extractPanel);
		extractLayout.putConstraint(SpringLayout.SOUTH, extract_top_panel, 70,
				SpringLayout.NORTH, extractPanel);
		extractLayout.putConstraint(SpringLayout.WEST, extract_top_panel, 10,
				SpringLayout.WEST, extractPanel);
		extractLayout.putConstraint(SpringLayout.EAST, extract_top_panel, -10,
				SpringLayout.EAST, extractPanel);
		extractPanel.add(extract_top_panel);

		SpringLayout sl_extract_top_panel = new SpringLayout();
		extract_top_panel.setLayout(sl_extract_top_panel);

		JLabel lblExtratedLabel1 = new JLabel("Image");
		sl_extract_top_panel.putConstraint(SpringLayout.NORTH,
				lblExtratedLabel1, 2, SpringLayout.NORTH, extract_top_panel);
		sl_extract_top_panel.putConstraint(SpringLayout.WEST,
				lblExtratedLabel1, 0, SpringLayout.WEST, extract_top_panel);
		sl_extract_top_panel.putConstraint(SpringLayout.SOUTH,
				lblExtratedLabel1, 27, SpringLayout.NORTH, extract_top_panel);
		lblExtratedLabel1.setHorizontalAlignment(SwingConstants.LEFT);
		extract_top_panel.add(lblExtratedLabel1);

		tfImage = new JTextField();
		sl_extract_top_panel.putConstraint(SpringLayout.WEST, tfImage, 10,
				SpringLayout.EAST, lblExtratedLabel1);
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
				imageMessage = showImage(tfImage.getText(), lblImageMessage);
			}
		});
		sl_extract_top_panel.putConstraint(SpringLayout.NORTH, tfImage, 2,
				SpringLayout.NORTH, extract_top_panel);
		sl_extract_top_panel.putConstraint(SpringLayout.SOUTH, tfImage, 27,
				SpringLayout.NORTH, extract_top_panel);
		tfImage.setHorizontalAlignment(SwingConstants.LEFT);
		extract_top_panel.add(tfImage);
		tfImage.setColumns(10);

		JButton btnImageBrowser = new JButton("...");
		sl_extract_top_panel.putConstraint(SpringLayout.EAST, tfImage, -10,
				SpringLayout.WEST, btnImageBrowser);
		btnImageBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnOpenShowDialogActionPerformed(tfImage);
			}
		});
		sl_extract_top_panel.putConstraint(SpringLayout.EAST, btnImageBrowser,
				0, SpringLayout.EAST, extract_top_panel);
		extract_top_panel.add(btnImageBrowser);
		
		JLabel lblOriginal = new JLabel("Original");
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
				originalMessage = getOriginalImage(tfOriginalImage.getText());
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
		extractLayout.putConstraint(SpringLayout.NORTH, extract_bottom_panel,
				-31, SpringLayout.SOUTH, extractPanel);
		extractLayout.putConstraint(SpringLayout.WEST, extract_bottom_panel,
				10, SpringLayout.WEST, extractPanel);
		extractLayout.putConstraint(SpringLayout.SOUTH, extract_bottom_panel,
				-5, SpringLayout.SOUTH, extractPanel);
		extractLayout.putConstraint(SpringLayout.EAST, extract_bottom_panel,
				-10, SpringLayout.EAST, extractPanel);
		extractPanel.add(extract_bottom_panel);
		SpringLayout sl_extract_bottom_panel = new SpringLayout();
		extract_bottom_panel.setLayout(sl_extract_bottom_panel);

		JButton btn_extract_Apply = new JButton("Apply");
		sl_extract_bottom_panel.putConstraint(SpringLayout.NORTH,
				btn_extract_Apply, 0, SpringLayout.NORTH, extract_bottom_panel);
		sl_extract_bottom_panel
				.putConstraint(SpringLayout.WEST, btn_extract_Apply, 140,
						SpringLayout.WEST, extract_bottom_panel);
		sl_extract_bottom_panel.putConstraint(SpringLayout.EAST,
				btn_extract_Apply, -140, SpringLayout.EAST,
				extract_bottom_panel);
		btn_extract_Apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applySteganoAnalisysMethod();
			}
		});
		extract_bottom_panel.add(btn_extract_Apply);

		JSplitPane extract_splitPanel = new JSplitPane();
		extractLayout.putConstraint(SpringLayout.NORTH, extract_splitPanel, 70, SpringLayout.NORTH, extractPanel);
		extractLayout.putConstraint(SpringLayout.WEST, extract_splitPanel, 10,
				SpringLayout.WEST, extractPanel);
		extractLayout.putConstraint(SpringLayout.SOUTH, extract_splitPanel, -6, SpringLayout.NORTH, extract_bottom_panel);
		extractLayout.putConstraint(SpringLayout.EAST, extract_splitPanel, -10,
				SpringLayout.EAST, extractPanel);
		extract_splitPanel.setResizeWeight(0.5);
		extractPanel.add(extract_splitPanel);

		pImage = new JScrollPane();
		extract_splitPanel.setLeftComponent(pImage);

		lblImageMessage = new JLabel("");
		pImage.setViewportView(lblImageMessage);

		JScrollPane pMessageExtracted = new JScrollPane();
		extract_splitPanel.setRightComponent(pMessageExtracted);

		lblMessageExtracted = new JLabel("");
		pMessageExtracted.setViewportView(lblMessageExtracted);

	}

	private ICoverMessage showImage(String address, JLabel lblImage) {
		File file = new File(address);
		if (file.exists() && imageFilter.accept(file)) {
			showMessage("Opening: " + file.getName());
			try {
				FileInputStream stream = new FileInputStream(file);
				if (loadImage(stream, lblImage)){
					Mat original = Highgui.imread(address);
					return new MatImage(original, Utils.getExtension(address));
				}
			} catch (IOException e) {
				showMessage("Sorry, cannot load the file");
			}
		} else {
			showMessage("It's a wrong file");
		}
		return null;
	}
	
	private ICoverMessage getOriginalImage(String address) {
		File file = new File(address);
		if (file.exists() && imageFilter.accept(file)) {
			showMessage("Reading: " + file.getName());
			Mat original = Highgui.imread(address);
			if(original.size().width == 0)
				showMessage("Sorry, cannot load the file");
			else
				return new MatImage(original, Utils.getExtension(address));
		} else {
			showMessage("It's a wrong file");
		}
		return null;
	}

	protected void tfMessageTextChange(String address, JLabel elementToShow) {
		File file = new File(address);
		if (!file.exists()) {
			showMessage("It's a wrong file");
			return;
		}
		showMessage("Opening: " + file.getName());
		try{
			message = new FileMessage(file);
			if (imageFilter.accept(file))
			{
				loadImage(new FileInputStream(file), elementToShow);
			}
			else
			{
				loadTextFile(message, elementToShow);
				showMessage("");
			}
		}catch(IOException ex){
			showMessage("Sorry, cannot load the file");
		}
	}

	private void loadTextFile(FileMessage file, JLabel elementToShow) {
		lblMessage.setText("<html>" + new String(message.getAllBytes()) + "</html>");
		lblMessage.setIcon(null);
		lblMessage.revalidate();
	}

	private boolean loadImage(InputStream stream, JLabel canvas) {
		try {
			Image image = ImageIO.read(stream);
			ImageIcon imageIcon = new ImageIcon(image);
			canvas.setText("");
			canvas.setIcon(imageIcon);
			showMessage("");
		} catch (Exception e) {
			showMessage("Sorry, cannot show the image.");
		}
		return true;
	}

	private void btnOpenShowDialogActionPerformed(JTextField textField) {
		fc.setAcceptAllFileFilterUsed(false);

		try{
			int returnVal = fc.showOpenDialog(frmStegoApplication);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				textField.setText(file.getPath());
			} else {
				showMessage("Open command cancelled by user.");
			}
		}catch(Exception e){
			showMessage("Error showing the image");
		}
	}

	private void showMessage(String message) {
		lblMessageInfo.setText(message);
	}

	private void applySteganographyMethod() {
		stegoObject = null;
		
		if (coverMessage == null) {
			showMessage("Please, select the cover message file.");
			return;
		}
		String identifier = tfMessage.getText();
		if (identifier.length() == 0) {
			showMessage("Please, enter the identifier.");
			return;
		}
		
		Transform2d alg = new Transform2dBasic(new FastDiscreteBiorthogonal_CDF_9_7());
		DWT2D_HL_LH_Algorithm steganoAlgorithm = new DWT2D_HL_LH_Algorithm(null, alg, visibilityfactor, 1);
		KeyPointImageAlgorithm algorithm = new KeyPointImageAlgorithm(coverMessage,
				steganoAlgorithm, visibilityfactor, keyPointSize, howManyPoints, null);
		
		IMessage embeddedData = new CacheMessage(identifier.getBytes());
		MatImage stegoObject = (MatImage)algorithm.getStegoObject(embeddedData);
		
		setStegoObject(stegoObject);
	}

	private void setStegoObject(ICoverMessage stego) {
		stegoObject = stego;
		btnSave.setEnabled(stego != null);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			stegoObject.save(stream);
			InputStream in = new ByteArrayInputStream(stream.toByteArray());
			loadImage(in, lblMessage);
		} catch (IOException e) {
			showMessage("Error, showing stego-object");
		}
	}

	protected void saveStegoObject() {
		if(stegoObject == null)
		{
			showMessage("There is nothing to save");
			return;
		}
		fc.setAcceptAllFileFilterUsed(true);
		int returnVal = fc.showSaveDialog(frmStegoApplication);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				String path = fc.getSelectedFile().getPath();
				stegoObject.save(new FileOutputStream(path));
				showMessage("File was saved successfully.");
			} catch (IOException e) {
				showMessage("Error, saving file.");
			}
		} else {
			showMessage("Open command cancelled by user.");
		}
	}

	private void applySteganoAnalisysMethod() {
		if (imageMessage == null) {
			showMessage("Please, select the image message file.");
			return;
		}
		
		if (originalMessage == null) {
			showMessage("Please, select the original image file.");
			return;
		}
		
		Transform2d alg = new Transform2dBasic(new FastDiscreteBiorthogonal_CDF_9_7());
		DWT2D_HL_LH_Algorithm steganoAlgorithm = new DWT2D_HL_LH_Algorithm(null, alg, visibilityfactor, 1);
		KeyPointImageAlgorithm algorithm = new KeyPointImageAlgorithm(imageMessage,
				steganoAlgorithm, visibilityfactor, keyPointSize, howManyPoints, originalMessage);

		try {
			byte[] embeddedData = algorithm.getEmbeddedData();
			ImageFactory factory = new ImageFactory();
			
			BufferedImage image = factory.createImage(keyPointSize >> 1, keyPointSize >> 1, embeddedData);
			String outfile = "message.bmp";
			FileOutputStream file = new FileOutputStream(outfile);
			ImageIO.write(image, "bmp", file);
			file.close();
			
			tfMessageTextChange(outfile, lblMessageExtracted);
		} catch (IOException e) {
			showMessage("Error, saving file.");
		}
	}
}
