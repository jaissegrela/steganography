package app.visual;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import core.algorithm.LSBAlgorithm;
import core.message.BasicImageMessage;
import core.message.FileMessage;
import core.message.ICoverMessage;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.BorderLayout;

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

	private BasicImageMessage coverMessage;
	private FileMessage message;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField tfImage;
	private JScrollPane pImage;
	private JLabel lblImageMessage;
	private JLabel lblMessageExtracted;
	protected BasicImageMessage imageMessage;
	protected ICoverMessage stegoObject;
	private JButton btnSave;

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

		JPanel hidden_top2_panel = new JPanel();
		hiddenLayout.putConstraint(SpringLayout.NORTH, hidden_top2_panel, 5,
				SpringLayout.SOUTH, hidden_top1_panel);
		hiddenLayout.putConstraint(SpringLayout.SOUTH, hidden_top2_panel, 35,
				SpringLayout.SOUTH, hidden_top1_panel);
		hiddenLayout.putConstraint(SpringLayout.WEST, hidden_top2_panel, 10,
				SpringLayout.WEST, hiddenPanel);
		hiddenLayout.putConstraint(SpringLayout.EAST, hidden_top2_panel, -10,
				SpringLayout.EAST, hiddenPanel);
		hiddenPanel.add(hidden_top2_panel);

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
		hiddenLayout.putConstraint(SpringLayout.NORTH, hidden_splitPanel, 6,
				SpringLayout.SOUTH, hidden_top2_panel);
		hiddenLayout.putConstraint(SpringLayout.WEST, hidden_splitPanel, 10,
				SpringLayout.WEST, hiddenPanel);
		hiddenLayout.putConstraint(SpringLayout.SOUTH, hidden_splitPanel, -6,
				SpringLayout.NORTH, hidden_bottom_panel);
		hiddenLayout.putConstraint(SpringLayout.EAST, hidden_splitPanel, -10,
				SpringLayout.EAST, hiddenPanel);
		hidden_splitPanel.setResizeWeight(0.5);
		hiddenPanel.add(hidden_splitPanel);

		SpringLayout sl_hidden_top1_panel = new SpringLayout();
		hidden_top1_panel.setLayout(sl_hidden_top1_panel);

		JLabel lblLabel1 = new JLabel("Cover Message");
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
		sl_hidden_top1_panel.putConstraint(SpringLayout.WEST, tfCoverMessage,
				100, SpringLayout.WEST, hidden_top1_panel);
		sl_hidden_top1_panel.putConstraint(SpringLayout.SOUTH, tfCoverMessage,
				27, SpringLayout.NORTH, hidden_top1_panel);
		tfCoverMessage.setAlignmentX(Component.RIGHT_ALIGNMENT);
		tfCoverMessage.setHorizontalAlignment(SwingConstants.LEFT);
		hidden_top1_panel.add(tfCoverMessage);
		tfCoverMessage.setColumns(10);

		JButton btnCoverMessageBrowser = new JButton("...");
		sl_hidden_top1_panel.putConstraint(SpringLayout.EAST, tfCoverMessage,
				-10, SpringLayout.WEST, btnCoverMessageBrowser);
		btnCoverMessageBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnOpenShowDialogActionPerformed(tfCoverMessage);
			}
		});
		sl_hidden_top1_panel
				.putConstraint(SpringLayout.EAST, btnCoverMessageBrowser, 0,
						SpringLayout.EAST, hidden_top1_panel);
		hidden_top1_panel.add(btnCoverMessageBrowser);
		springLayout.putConstraint(SpringLayout.EAST, hidden_top2_panel, -10,
				SpringLayout.EAST, basePanel);
		SpringLayout sl_hidden_top2_panel = new SpringLayout();
		hidden_top2_panel.setLayout(sl_hidden_top2_panel);

		JLabel lblLabel2 = new JLabel("Message");
		sl_hidden_top2_panel.putConstraint(SpringLayout.NORTH, lblLabel2, 2,
				SpringLayout.NORTH, hidden_top2_panel);
		sl_hidden_top2_panel.putConstraint(SpringLayout.WEST, lblLabel2, 0,
				SpringLayout.WEST, hidden_top2_panel);
		sl_hidden_top2_panel.putConstraint(SpringLayout.SOUTH, lblLabel2, 27,
				SpringLayout.NORTH, hidden_top2_panel);
		hidden_top2_panel.add(lblLabel2);

		tfMessage = new JTextField();
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
				tfMessageTextChange(tfMessage.getText());
			}
		});
		sl_hidden_top2_panel.putConstraint(SpringLayout.NORTH, tfMessage, 2,
				SpringLayout.NORTH, hidden_top2_panel);
		sl_hidden_top2_panel.putConstraint(SpringLayout.WEST, tfMessage, 100,
				SpringLayout.WEST, hidden_top2_panel);
		sl_hidden_top2_panel.putConstraint(SpringLayout.SOUTH, tfMessage, 27,
				SpringLayout.NORTH, hidden_top2_panel);
		sl_hidden_top2_panel.putConstraint(SpringLayout.EAST, lblLabel2, -12,
				SpringLayout.WEST, tfMessage);
		tfMessage.setHorizontalAlignment(SwingConstants.LEFT);
		hidden_top2_panel.add(tfMessage);
		tfMessage.setColumns(10);

		JButton btnMessageBrowser = new JButton("...");
		sl_hidden_top2_panel.putConstraint(SpringLayout.EAST, tfMessage, -10,
				SpringLayout.WEST, btnMessageBrowser);
		btnMessageBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnMessageActionPerformed(arg0);
			}
		});
		sl_hidden_top2_panel.putConstraint(SpringLayout.EAST,
				btnMessageBrowser, 0, SpringLayout.EAST, hidden_top2_panel);
		hidden_top2_panel.add(btnMessageBrowser);

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
		extractLayout.putConstraint(SpringLayout.SOUTH, extract_top_panel, 35,
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
		extractLayout.putConstraint(SpringLayout.NORTH, extract_splitPanel, 6,
				SpringLayout.SOUTH, extract_top_panel);
		extractLayout.putConstraint(SpringLayout.WEST, extract_splitPanel, 10,
				SpringLayout.WEST, extractPanel);
		extractLayout.putConstraint(SpringLayout.SOUTH, extract_splitPanel, -6,
				SpringLayout.NORTH, extract_bottom_panel);
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

	private BasicImageMessage showImage(String address, JLabel lblImage) {
		File file = new File(address);
		if (file.exists() && imageFilter.accept(file)) {
			showMessage("Opening: " + file.getName());
			try {
				FileInputStream stream = new FileInputStream(file);
				if (loadImage(stream, lblImage))
					return new BasicImageMessage(file);
			} catch (IOException e) {
				showMessage("Sorry, cannot load the file");
			}
		} else {
			showMessage("It's a wrong file");
		}
		return null;
	}

	protected void tfMessageTextChange(String address) {
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
				loadImage(new FileInputStream(file), lblMessage);
			}
			else
			{
				loadTextFile(message);
				showMessage("");
			}
		}catch(IOException ex){
			showMessage("Sorry, cannot load the file");
		}
	}

	private void loadTextFile(FileMessage file) {
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
		} catch (IOException e) {
			showMessage("Sorry, cannot show the image.");
			return false;
		}
		showMessage("");
		return true;
	}

	private void btnMessageActionPerformed(ActionEvent arg0) {
		fc.setAcceptAllFileFilterUsed(true);
		int returnVal = fc.showOpenDialog(frmStegoApplication);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			tfMessage.setText(file.getPath());
		} else {
			showMessage("Open command cancelled by user.");
		}
	}

	private void btnOpenShowDialogActionPerformed(JTextField textField) {
		fc.setAcceptAllFileFilterUsed(false);

		int returnVal = fc.showOpenDialog(frmStegoApplication);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			textField.setText(file.getPath());
		} else {
			showMessage("Open command cancelled by user.");
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
		if (message == null) {
			showMessage("Please, select the message file.");
			return;
		}
		LSBAlgorithm lsb = new LSBAlgorithm(coverMessage);
		Double stegoObjectRate = lsb.getStegoObjectRate(message);
		if (stegoObjectRate > 1) {
			showMessage("Message is too big, please select other cover message.");
			return;
		}
		setStegoObject(lsb.getStegoObject(message));
		showMessage(String.format("The cover message rate is: %1$,.2f",
				stegoObjectRate));
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
		LSBAlgorithm lsb = new LSBAlgorithm(imageMessage);
		if (!lsb.hasHiddenMessage()) {
			showMessage("Imagen dosen't contain a message");
			return;
		}
		fc.setAcceptAllFileFilterUsed(false);
		int returnVal = fc.showSaveDialog(frmStegoApplication);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				byte[] embeddedData = lsb.getEmbeddedData();
				File selectedFile = fc.getSelectedFile();
				FileOutputStream file = new FileOutputStream(
						selectedFile.getPath());
				file.write(embeddedData);
				file.close();
			} catch (IOException e) {
				showMessage("Error, saving file.");
			}
		} else {
			showMessage("Open command cancelled by user.");
		}
	}
}
