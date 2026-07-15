package monitor;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class App{
	public static void main(String args[]) {
		DashboardFrame frame=new DashboardFrame("CPU and Memory monitor");
		frame.setGUI();
		StatsPoller sp=new StatsPoller();
		sp.polling(frame);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				sp.stop();
			}
		});
	}
}