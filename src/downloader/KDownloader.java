package downloader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker.StateValue;
import javax.swing.border.EmptyBorder;

/**
 * A GUI which allows the user to download files from a specified website to a
 * specified folder.
 * 
 * @author Kelsey McKenna
 *
 */
public class KDownloader extends JFrame {

	private JPanel contentPane;
	private final JProgressBar progressBar;
	private JPanel pnlControl;
	private JPanel pnlOptions;
	private JCheckBox chckbxJPG;
	private JCheckBox chckbxPNG;
	private JCheckBox chckbxTXT;
	private JTextField fldDest;
	private JLabel lblWebpage;
	private JTextField fldWebpage;
	private JPanel pnlExtensions;
	private JPanel pnlDest;
	private JPanel pnlWebpage;
	private JLabel lblDestinationFolder;
	private JButton btnOpenFolder;
	private JButton btnStartDownload;

	private List<JCheckBox> extensionBoxes = new ArrayList<>();
	private JScrollPane scrlQueue;
	private JList<Download> downloadList;
	private DefaultListModel<Download> model;
	private JSlider sldrNumThreads;

	private BatchDownload batchDownload;
	private JPanel panel;
	private JLabel lblInfo;
	private JLabel lblNoThreads;
	private JPanel pnlExtensionControl;
	private JLabel lblExtensions;
	private JPanel pnlUserControl;
	private JCheckBox chckbxZIP;
	private JCheckBox chckbxPDF;
	private JButton btnSelectAll;

	/**
	 * Create the frame.
	 */
	public KDownloader() {
		super("KDownload");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 734, 555);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		pnlOptions = new JPanel();
		contentPane.add(pnlOptions, BorderLayout.NORTH);
		pnlOptions.setLayout(new GridLayout(3, 1, 0, 0));

		pnlExtensionControl = new JPanel();
		pnlOptions.add(pnlExtensionControl);
		pnlExtensionControl.setLayout(new BorderLayout(0, 0));

		lblExtensions = new JLabel("Extensions: ");
		pnlExtensionControl.add(lblExtensions, BorderLayout.WEST);

		pnlExtensions = new JPanel();
		pnlExtensionControl.add(pnlExtensions, BorderLayout.CENTER);
		pnlExtensions.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		chckbxJPG = new JCheckBox(".jpg");
		chckbxJPG.setFocusable(false);
		extensionBoxes.add(chckbxJPG);
		pnlExtensions.add(chckbxJPG);

		chckbxPNG = new JCheckBox(".png");
		chckbxPNG.setFocusable(false);
		pnlExtensions.add(chckbxPNG);
		extensionBoxes.add(chckbxPNG);

		chckbxTXT = new JCheckBox(".txt");
		chckbxTXT.setFocusable(false);
		pnlExtensions.add(chckbxTXT);
		extensionBoxes.add(chckbxTXT);

		chckbxPDF = new JCheckBox("pdf");
		chckbxPDF.setFocusable(false);
		pnlExtensions.add(chckbxPDF);
		extensionBoxes.add(chckbxPDF);

		chckbxZIP = new JCheckBox(".zip");
		chckbxZIP.setFocusable(false);
		pnlExtensions.add(chckbxZIP);
		extensionBoxes.add(chckbxZIP);

		btnSelectAll = new JButton("Select All");
		btnSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < extensionBoxes.size(); ++i) {
					extensionBoxes.get(i).setSelected(true);
				}
			}
		});
		btnSelectAll.setFocusable(false);
		pnlExtensions.add(btnSelectAll);

		pnlDest = new JPanel();
		pnlOptions.add(pnlDest);
		pnlDest.setLayout(new BorderLayout(0, 0));

		lblDestinationFolder = new JLabel("Destination Folder: ");
		pnlDest.add(lblDestinationFolder, BorderLayout.WEST);

		fldDest = new JTextField();
		pnlDest.add(fldDest, BorderLayout.CENTER);
		fldDest.setColumns(10);

		btnOpenFolder = new JButton("Open folder");
		btnOpenFolder.setFocusable(false);
		btnOpenFolder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Set up a thread so that the program doesn't freeze when 'Open
				// Folder' button is pressed.
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						final JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
						// Only allow a single directory to be selected.
						fc.setMultiSelectionEnabled(false);
						fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

						// Show the dialog
						int option = fc.showOpenDialog(null);

						// If the user selected a directory and clicked 'Open'
						if (option == JFileChooser.APPROVE_OPTION) {
							// Then set the text of the field to the selected
							// directory.
							File file = fc.getSelectedFile();
							fldDest.setText(file.getAbsolutePath());
						}
					}
				});

				t.start();
			}
		});
		pnlDest.add(btnOpenFolder, BorderLayout.EAST);

		pnlWebpage = new JPanel();
		pnlOptions.add(pnlWebpage);
		pnlWebpage.setLayout(new BorderLayout(0, 0));

		lblWebpage = new JLabel("Webpage: ");
		pnlWebpage.add(lblWebpage, BorderLayout.WEST);

		fldWebpage = new JTextField();
		fldWebpage.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		pnlWebpage.add(fldWebpage);
		fldWebpage.setColumns(10);
		fldWebpage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startDownload();
			}
		});

		pnlControl = new JPanel();
		contentPane.add(pnlControl, BorderLayout.SOUTH);
		pnlControl.setLayout(new BorderLayout(0, 0));

		pnlUserControl = new JPanel();
		pnlControl.add(pnlUserControl, BorderLayout.WEST);

		btnStartDownload = new JButton("Start Download");
		pnlUserControl.add(btnStartDownload);
		btnStartDownload.setFocusable(false);

		lblNoThreads = new JLabel("No. threads");
		pnlUserControl.add(lblNoThreads);

		sldrNumThreads = new JSlider();
		pnlUserControl.add(sldrNumThreads);
		sldrNumThreads.setMajorTickSpacing(1);
		sldrNumThreads.setSnapToTicks(true);
		sldrNumThreads.setPaintTicks(true);
		sldrNumThreads.setPaintLabels(true);
		sldrNumThreads.setMinimum(1);
		sldrNumThreads.setMaximum(10);
		sldrNumThreads.setValue(3);

		btnStartDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startDownload();
			}
		});

		progressBar = new JProgressBar(0, 100);
		progressBar.setStringPainted(true);
		pnlControl.add(progressBar);

		model = new DefaultListModel<Download>();

		panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		lblInfo = new JLabel("Idle");
		lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblInfo, BorderLayout.NORTH);
		downloadList = new JList<>(model);
		downloadList.setCellRenderer(new DownloadItemRenderer());

		scrlQueue = new JScrollPane(downloadList);
		panel.add(scrlQueue);

		setMinimumSize(new Dimension(400, 400));
		setVisible(true);
	}

	/**
	 * @param downloads the downloads to be performed in this batch download
	 * @param numThreads the number of threads to be used when downloading
	 * @return a freshly generated batch downloader.
	 */
	private BatchDownload genBatchDownload(List<Download> downloads, int numThreads) {
		BatchDownload svar = new BatchDownload(downloads, numThreads);

		// Check when the batch download completes a new download
		svar.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// If the batch downloader has progressed
				if (evt.getPropertyName().equals("progress")) {
					int val = (Integer) evt.getNewValue();

					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							// Set the progress bar to the associated amount of
							// progress.
							progressBar.setValue(val);

							if (val >= 100) {
								lblInfo.setText("Complete");
							}

							// Refresh the list of downloads
							refreshList();
						}
					});
				} else if (evt.getPropertyName().equals("state")) {
					StateValue state = (StateValue) evt.getNewValue();
					if (state.equals(StateValue.DONE)) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								btnStartDownload.setEnabled(true);
							}
						});
					}
				}
			}
		});

		return svar;
	}

	/**
	 * Clear the list of downloads, then add them all again.
	 */
	private void refreshList() {
		model.clear();
		for (Download download : batchDownload.getDownloads()) {
			model.addElement(download);
		}
	}

	/**
	 * Start downloading the files specified by the user's input
	 */
	private void startDownload() {
		// Disable the start download button
		btnStartDownload.setEnabled(false);

		// Create a thread to run the downloads and send updates for the GUI.
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				List<URL> downloadURLs;

				try {
					// Change the info label to show "Loading web page"
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							lblInfo.setForeground(Color.BLUE);
							lblInfo.setText("Loading web page ...");
						}
					});

					// Get the download URLs.
					downloadURLs = Download.getDownloadLinks(fldWebpage.getText(), getExtensions());
					// Create a list of download objects from these URLs
					List<Download> downloads = Download.getDownloadList(downloadURLs, new File(fldDest.getText()));

					// Use the specified number of threads for the batch
					// downloader, and the specified downloads
					batchDownload = genBatchDownload(downloads, sldrNumThreads.getValue());
					batchDownload.execute();

					// Update the info label
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							if (downloads.size() > 0) {
								lblInfo.setText("Downloading files ...");
							} else {
								lblInfo.setText("No files found with specified extensions.");
							}
						}
					});
				} catch (SocketTimeoutException e) {
					// Update the info label to indicate a connection timeout.
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							lblInfo.setForeground(Color.BLUE);
							lblInfo.setText("Connection timeout. Trying again ...");
						}
					});

					// Restart the download
					startDownload();
				} catch (Exception e) {
					// Update the info label to indicate that the operation
					// failed.
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							lblInfo.setForeground(Color.RED);
							lblInfo.setText("Failed. Please try again.");
						}
					});
					e.printStackTrace();
				}

			}
		});

		// Wipe the visible list of downloads
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				model.clear();
			}
		});

		t1.start();
	}

	/**
	 * @return the list of selected extensions
	 */
	private List<String> getExtensions() {
		List<String> svar = new ArrayList<>();
		for (JCheckBox checkBox : extensionBoxes) {
			String text = checkBox.getText();

			// If the checkbox is selected, get the name of the extension, e.g.
			// if the extension shows ".jpg" then grab "jpg"
			if (checkBox.isSelected()) svar.add(text.substring(text.lastIndexOf(".") + 1));
		}

		return svar;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new KDownloader();
			}
		});
	}

}
