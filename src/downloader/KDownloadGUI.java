package downloader;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class KDownloadGUI extends JFrame implements Observer {

	private JPanel contentPane;
	private final JProgressBar progressBar;
	private BatchDownload batchDownload;
	private JPanel pnlControl;
	private JPanel pnlOptions;
	private JCheckBox chckbxJPG;
	private JCheckBox chckbxPNG;
	private JCheckBox chckbxTXT;
	private JTextField textField;
	private JLabel lblWebpage;
	private JTextField textField_1;
	private JPanel pnlExtensions;
	private JPanel pnlDest;
	private JPanel pnlWebpage;
	private JLabel lblDestinationFolder;
	private JButton btnOpenFolder;

	/**
	 * Create the frame.
	 */
	public KDownloadGUI(BatchDownload batchDownload) {
		this.batchDownload = batchDownload;
		this.batchDownload.addObserver(this);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 692, 393);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		pnlOptions = new JPanel();
		contentPane.add(pnlOptions, BorderLayout.NORTH);
		pnlOptions.setLayout(new GridLayout(3, 1, 0, 0));
		
		pnlExtensions = new JPanel();
		pnlOptions.add(pnlExtensions);
		pnlExtensions.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		chckbxJPG = new JCheckBox(".jpg");
		pnlExtensions.add(chckbxJPG);
		
		chckbxPNG = new JCheckBox(".png");
		pnlExtensions.add(chckbxPNG);
		
		chckbxTXT = new JCheckBox(".txt");
		pnlExtensions.add(chckbxTXT);
		
		pnlDest = new JPanel();
		pnlOptions.add(pnlDest);
		pnlDest.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		lblDestinationFolder =new JLabel("Destination Folder:");
		pnlDest.add(lblDestinationFolder);
		
		textField = new JTextField();
		pnlDest.add(textField);
		textField.setColumns(10);
		
		btnOpenFolder = new JButton("Open folder");
		pnlDest.add(btnOpenFolder);
		
		pnlWebpage = new JPanel();
		pnlOptions.add(pnlWebpage);
		pnlWebpage.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblWebpage = new JLabel("Webpage:");
		pnlWebpage.add(lblWebpage);
		
		textField_1 = new JTextField();
		pnlWebpage.add(textField_1);
		textField_1.setColumns(10);
		
		pnlControl = new JPanel();
		contentPane.add(pnlControl, BorderLayout.SOUTH);
		
		JButton btnStartDownload = new JButton("Start Download");
		pnlControl.add(btnStartDownload);
		btnStartDownload.setFocusable(false);
		
		progressBar = new JProgressBar(0, this.batchDownload.getNumDownloads());
		pnlControl.add(progressBar);
		progressBar.setStringPainted(true);
		btnStartDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!batchDownload.isComplete()) {
					progressBar.setMinimum(0);
					progressBar.setMaximum(batchDownload.getNumDownloads());
					progressBar.setValue(0);
					
					(new Thread(batchDownload)).start();
				}
			}
		});
		
		setVisible(true);
	}

	@Override
	public void update(Observable o, Object arg) {
		progressBar.setValue(batchDownload.getNumCompletedDownloads());
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		final File destDir = new File("images");
		System.out.println("Started");
		
		List<URL> urls = Download.getDownloadLinks("http://www.reddit.com", Arrays.asList("jpg"));
		List<Download> downloads = new ArrayList<>();
		for (URL url : urls) {
			downloads.add(new Download(url, destDir));
		}
		
		BatchDownload bd = new BatchDownload(downloads);
		new KDownloadGUI(bd);
		
		System.out.println("Finished");
	}

}
