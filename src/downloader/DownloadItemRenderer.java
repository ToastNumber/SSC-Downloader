package downloader;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class DownloadItemRenderer implements ListCellRenderer<Download> {

	@Override
	public Component getListCellRendererComponent(JList<? extends Download> list, Download value, int index, boolean isSelected,
			boolean cellHasFocus) {
		JLabel svar = new JLabel();
		svar.setFont(new Font("courier", 0, 15));
		svar.setText(String.format("%2d. %s", index + 1, value.toString()));
		
		if (value.isRunning()) {
			svar.setBackground(Color.CYAN);
		} else if (value.isComplete()) {
			svar.setBackground(Color.GREEN);
		} 
		
		svar.setOpaque(true);
		
		return svar;
	}
	
}
