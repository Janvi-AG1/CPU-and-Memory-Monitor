package monitor;

import java.util.concurrent.*;

public class StatsPoller {
	ScheduledExecutorService scheduler;
	SystemStats ss;
	public StatsPoller() {
		ss=new SystemStats();
		scheduler=Executors.newScheduledThreadPool(1);
	}
	
	public void polling(DashboardFrame frame) {
		scheduler.scheduleAtFixedRate(()-> {
			double[] coreLoads=ss.getCpuLoad();
			double avgCpuLoad=ss.getAvgCpuLoad(coreLoads);
			double totalMemory=ss.getTotalMemory();
			double availableMemory=ss.getAvailableMemory();
			double usedMemory=ss.getUsedMemory();
			frame.updateBars(avgCpuLoad, coreLoads, usedMemory, availableMemory, totalMemory);
			
		}, 1, 1, TimeUnit.SECONDS);
		
	}
	
	public void stop() {
		scheduler.shutdown();
	}
	
}
