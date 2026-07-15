package monitor;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JProgressBar[] pbars=new JProgressBar[12];
	JProgressBar mainBar;
	JLabel usedMemory=new JLabel();
	JLabel availableMemory=new JLabel();
	int maxGap=15;
	Font mainFont=new Font("SansSerif", Font.BOLD, 16);
	Font smallFont=new Font("SansSerif", Font.BOLD, 14);
	
	public DashboardFrame(String name) {
		super(name);
		setSize(900, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void FillFrame() {
		setLayout(new BorderLayout());
		
		JPanel bigBar=new JPanel();
		mainBar=new JProgressBar();
		JLabel mainBarLabel= new JLabel("Average Processor Use");
		mainBarLabel.setFont(mainFont);
		mainBar.setPreferredSize(new Dimension(500, 50));
		bigBar.add(mainBarLabel);
		bigBar.add(mainBar);
		add(bigBar, BorderLayout.NORTH);
				
		JPanel smallBars=new JPanel();
		smallBars.setLayout(new GridLayout(6, 2, 6, 6));
		
		JPanel[] each12Bars=FillSmallBars();
		
		for(JPanel barLabel:each12Bars) {
			smallBars.add(barLabel);
		}
		
		add(smallBars, BorderLayout.CENTER);
		
		bigBar.setBorder(BorderFactory.createEmptyBorder(10, 10,10, 10));
		smallBars.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JPanel memoryStats = new JPanel(new BorderLayout());
		memoryStats.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		usedMemory.setFont(mainFont);
		availableMemory.setFont(mainFont);
		memoryStats.add(usedMemory, BorderLayout.NORTH);
		memoryStats.add(availableMemory, BorderLayout.SOUTH);
		
		
		add(memoryStats, BorderLayout.SOUTH);
		}
	
	private JPanel[] FillSmallBars() {
		JPanel[] each12Panels=new JPanel[12];
		for(int i=0; i<12; i++) {
			JPanel barLabel = new JPanel();
			barLabel.setLayout(new GridLayout(2, 1));
			JLabel label=new JLabel("core "+(i+1));
			label.setFont(smallFont);
			pbars[i]=new JProgressBar();
			barLabel.add(label);
			barLabel.add(pbars[i]);
			each12Panels[i]=barLabel;
		}
		return each12Panels;
	}
	
	public void updateBars(double avgCpuLoad, double[] coreLoads, double usedMem, double availMem, double totalMem) {
		SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
						mainBar.setValue((int)(avgCpuLoad*100));	
						for(int i=0; i<12; i++) {
							pbars[i].setValue((int)(coreLoads[i]*100));
						}
						usedMemory.setText("Used memory: "+String.format("%.1f", usedMem)+"/"+String.format("%.1f", totalMem)+" MB");
						availableMemory.setText("Available memory: "+String.format("%.1f", availMem)+"/"+String.format("%.1f", totalMem)+" MB");
				}
				
		});
	}
	
	public void setGUI() {
		this.FillFrame();
		setVisible(true);
	}
}
