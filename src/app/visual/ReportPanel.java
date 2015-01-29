package app.visual;

import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
//
//import core.attack.ChiSquare;
//import core.attack.IStatisticsSteganalysis;

@SuppressWarnings("serial")
public class ReportPanel extends JPanel {

	private final JFileChooser fc = new JFileChooser();
	private ImageFilter imageFilter;
//	private IStatisticsSteganalysis[] attacks;
	private JTextArea taReport;

	/**
	 * Create the panel.
	 */
	public ReportPanel() {
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);

		final JPanel extractPanel = new JPanel();
		SpringLayout extractLayout = new SpringLayout();
		extractPanel.setLayout(extractLayout);
		springLayout.putConstraint(SpringLayout.WEST, extractPanel, 0,
				SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.NORTH, extractPanel, 0,
				SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.SOUTH, extractPanel, 0,
				SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, extractPanel, 0,
				SpringLayout.EAST, this);
		add(extractPanel);

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
		extract_top_panel.add(lblExtratedLabel1);

		final JTextField tfPath = new JTextField();
		sl_extract_top_panel.putConstraint(SpringLayout.WEST, tfPath, 10,
				SpringLayout.EAST, lblExtratedLabel1);
		final JButton btn_extract_Apply = new JButton("Apply");
		btn_extract_Apply.setEnabled(false);

		sl_extract_top_panel.putConstraint(SpringLayout.SOUTH, tfPath, 0,
				SpringLayout.SOUTH, lblExtratedLabel1);
		tfPath.getDocument().addDocumentListener(new DocumentListener() {
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
				File f = new File(tfPath.getText());
				btn_extract_Apply.setEnabled(f.exists()
						&& imageFilter.accept(f));
			}
		});
		extract_top_panel.add(tfPath);
		tfPath.setColumns(10);

		sl_extract_top_panel.putConstraint(SpringLayout.WEST,
				btn_extract_Apply, -82, SpringLayout.EAST, extract_top_panel);
		sl_extract_top_panel.putConstraint(SpringLayout.NORTH,
				btn_extract_Apply, 2, SpringLayout.NORTH, extract_top_panel);
		sl_extract_top_panel.putConstraint(SpringLayout.EAST,
				btn_extract_Apply, 0, SpringLayout.EAST, extract_top_panel);
		extract_top_panel.add(btn_extract_Apply);
		btn_extract_Apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnApply(tfPath.getText());
			}
		});

		JButton btnImageBrowser = new JButton("...");
		sl_extract_top_panel.putConstraint(SpringLayout.EAST, tfPath, -10,
				SpringLayout.EAST, btnImageBrowser);
		sl_extract_top_panel.putConstraint(SpringLayout.NORTH, btnImageBrowser,
				2, SpringLayout.NORTH, extract_top_panel);
		sl_extract_top_panel.putConstraint(SpringLayout.WEST, btnImageBrowser,
				-62, SpringLayout.WEST, btn_extract_Apply);
		sl_extract_top_panel.putConstraint(SpringLayout.EAST, btnImageBrowser,
				-6, SpringLayout.WEST, btn_extract_Apply);
		btnImageBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnOpenShowDialogActionPerformed(tfPath);
			}
		});
		extract_top_panel.add(btnImageBrowser);

		sl_extract_top_panel.putConstraint(SpringLayout.NORTH, tfPath, 2,
				SpringLayout.NORTH, extract_top_panel);
		sl_extract_top_panel.putConstraint(SpringLayout.EAST, tfPath, -2,
				SpringLayout.WEST, btnImageBrowser);

		taReport = new JTextArea();
		taReport.setDisabledTextColor(Color.BLACK);
		taReport.setBackground(SystemColor.controlHighlight);
		extractLayout.putConstraint(SpringLayout.NORTH, taReport, 6,
				SpringLayout.SOUTH, extract_top_panel);
		extractLayout.putConstraint(SpringLayout.SOUTH, taReport, -6,
				SpringLayout.SOUTH, extractPanel);
		extractLayout.putConstraint(SpringLayout.EAST, taReport, -10,
				SpringLayout.EAST, extractPanel);
		taReport.setEditable(false);
		taReport.setEnabled(false);

		extractLayout.putConstraint(SpringLayout.WEST, taReport, 10,
				SpringLayout.WEST, extractPanel);
		taReport.setLineWrap(true);
		extractPanel.add(taReport);

		imageFilter = new ImageFilter();
		fc.addChoosableFileFilter(imageFilter);

//		attacks = new IStatisticsSteganalysis[1];
//		attacks[0] = new ChiSquare();
	}

	protected void btnApply(String path) {
//		try {
//			BasicImageMessage file = new BasicImageMessage(path);
//			StringBuilder str = new StringBuilder();
//			str.append("Methods: ").append(attacks.length).append("\n");
//			str.append("----------------").append("\n");
//			for (IStatisticsSteganalysis attack : attacks) {
//				double value = attack.calculate(file);
//				str.append(String
//						.format("Method: %s, value: %f", attack, value));
//			}
//			taReport.setText(str.toString());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	private void btnOpenShowDialogActionPerformed(JTextField textField) {
		fc.setAcceptAllFileFilterUsed(false);

		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			textField.setText(file.getPath());
		}
	}
}
