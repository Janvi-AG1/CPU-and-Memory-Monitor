package monitor;

import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.SystemInfo;

public class SystemStats{
	private SystemInfo si = new SystemInfo();
	private HardwareAbstractionLayer hal=si.getHardware();
	private CentralProcessor cp=hal.getProcessor();
	private GlobalMemory gMem=hal.getMemory();
	private long[][] prevTicks=cp.getProcessorCpuLoadTicks();
	private final double memConversionFactor=1024*1024;
	
	public double[] getCpuLoad() {
		long[][] currentTicks=cp.getProcessorCpuLoadTicks();
		double[] cpuLoad=cp.getProcessorCpuLoadBetweenTicks(prevTicks, currentTicks);
		prevTicks=currentTicks;
		return cpuLoad;
	}
	
	public double getAvgCpuLoad(double[] cpuLoad) {
		double avg=0;
		double sum=0;
		for(double val:cpuLoad) {
			sum+=val;
		}
		avg=sum/cpuLoad.length;
		return avg;
	}
	
	public double getTotalMemory() {
		return (gMem.getTotal()/memConversionFactor);
	}
	
	public double getAvailableMemory() {
		return (gMem.getAvailable()/memConversionFactor);
	}
	
	public double getUsedMemory() {
		return (getTotalMemory() - getAvailableMemory());
	}
}