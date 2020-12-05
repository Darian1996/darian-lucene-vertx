
package com.darian.darianLuceneVertx.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.StringTokenizer;

public class CpuUsageUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CpuUsageUtils.class);

    private static final int CPUTIME = 30;

    private static final int PERCENT = 100;

    private static final int FAULTLENGTH = 10;

    private static String linuxVersion = "3.10.0-514.26.2.el7.x86_64";

    public static double getCpuRatio() {
        // 操作系统
        String osName = System.getProperty("os.name");
        double cpuRatio = 0;
        if (osName.toLowerCase().startsWith("windows")) {
            cpuRatio = getCpuRatioForWindows();

        } else {
            cpuRatio = getCpuRateForLinux();
        }

        LOGGER.info(String.format("CPU：USE: [%s]", cpuRatio));
        return cpuRatio;
    }

    /**
     * 获得当前的监控对象.
     *
     * @return 返回构造好的监控对象
     */
    public MonitorInfoBean getMonitorInfoBean() throws Exception {

        // 操作系统
        String osName = System.getProperty("os.name");

        // 获得线程总数
        ThreadGroup parentThread;
        for (parentThread = Thread.currentThread().getThreadGroup();
             Objects.nonNull(parentThread.getParent());
             parentThread = parentThread.getParent()) {
            ;
        }
        int totalThread = parentThread.activeCount();

        double cpuRatio = 0;
        if (osName.toLowerCase().startsWith("windows")) {
            cpuRatio = getCpuRatioForWindows();
        } else {
            cpuRatio = getCpuRateForLinux();
        }

        // 构造返回对象
        MonitorInfoBean infoBean = new MonitorInfoBean();
        infoBean.setTotalThread(totalThread);
        infoBean.setCpuRatio(cpuRatio);
        return infoBean;
    }

    private static double getCpuRateForLinux() {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader brStat = null;
        StringTokenizer tokenStat = null;
        try {
            LOGGER.debug("Get usage rate of CUP , linux version: " + linuxVersion);

            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "top -b -n 1"});
            is = process.getInputStream();
            isr = new InputStreamReader(is);
            brStat = new BufferedReader(isr);

            if ("2.4".equals(linuxVersion)) {
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();

                tokenStat = new StringTokenizer(brStat.readLine());
                tokenStat.nextToken();
                tokenStat.nextToken();
                String user = tokenStat.nextToken();
                tokenStat.nextToken();
                String system = tokenStat.nextToken();
                tokenStat.nextToken();
                String nice = tokenStat.nextToken();

                LOGGER.info(user + " , " + system + " , " + nice);

                user = user.substring(0, user.indexOf("%"));
                system = system.substring(0, system.indexOf("%"));
                nice = nice.substring(0, nice.indexOf("%"));

                float userUsage = new Float(user).floatValue();
                float systemUsage = new Float(system).floatValue();
                float niceUsage = new Float(nice).floatValue();

                return (userUsage + systemUsage + niceUsage) / 100;
            } else {
                brStat.readLine();
                brStat.readLine();

                tokenStat = new StringTokenizer(brStat.readLine());
                tokenStat.nextToken();
                tokenStat.nextToken();
                tokenStat.nextToken();
                tokenStat.nextToken();
                tokenStat.nextToken();
                tokenStat.nextToken();
                tokenStat.nextToken();
                String cpuUsage = tokenStat.nextToken();

                LOGGER.info("CPU idle : " + cpuUsage);

                Float usage = Float.valueOf(cpuUsage);

                return 100 - usage.floatValue();
            }

        } catch (IOException e) {
            LOGGER.error("getCpuRateForLinux error: ", e);
            MonitorInfoBean.freeResource(is, isr, brStat);
            return 1;
        } finally {
            MonitorInfoBean.freeResource(is, isr, brStat);
        }

    }

    static class Bytes {
        public static String substring(String src, int start_idx, int end_idx) {
            byte[] b = src.getBytes();
            String tgt = "";
            for (int i = start_idx; i <= end_idx; i++) {
                tgt += (char) b[i];
            }
            return tgt;
        }
    }

    public static class MonitorInfoBean {

        /**
         * 线程总数.
         */
        private int totalThread;

        /**
         * cpu使用率.
         */
        private double cpuRatio;

        public int getTotalThread() {
            return totalThread;
        }

        public void setTotalThread(int totalThread) {
            this.totalThread = totalThread;
        }

        public double getCpuRatio() {
            return cpuRatio;
        }

        public void setCpuRatio(double cpuRatio) {
            this.cpuRatio = cpuRatio;
        }

        private static void freeResource(InputStream is, InputStreamReader isr,
                                         BufferedReader br) {
            try {
                if (is != null) {
                    is.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                LOGGER.error("getCpuRateForLinux error: ", e);
            }
        }
    }

    /**
     * 获得CPU使用率.
     *
     * @return 返回cpu使用率
     */
    private static double getCpuRatioForWindows() {
        try {
            String procCmd = System.getenv("windir")
                    + "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,"
                    + "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
            // 取进程信息
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
            Thread.sleep(CPUTIME);
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
            if (c0 != null && c1 != null) {
                long idletime = c1[0] - c0[0];
                long busytime = c1[1] - c0[1];
                return Double.valueOf(
                        PERCENT * (busytime) / (busytime + idletime))
                        .doubleValue();
            } else {
                return 0.0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }

    /**
     * 读取CPU信息.
     *
     * @param proc
     */
    private static long[] readCpu(final Process proc) {
        long[] retn = new long[2];
        try {
            proc.getOutputStream().close();
            InputStreamReader ir = new InputStreamReader(proc.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line = input.readLine();
            if (line == null || line.length() < FAULTLENGTH) {
                return null;
            }
            int capidx = line.indexOf("Caption");
            int cmdidx = line.indexOf("CommandLine");
            int rocidx = line.indexOf("ReadOperationCount");
            int umtidx = line.indexOf("UserModeTime");
            int kmtidx = line.indexOf("KernelModeTime");
            int wocidx = line.indexOf("WriteOperationCount");
            long idletime = 0;
            long kneltime = 0;
            long usertime = 0;
            while ((line = input.readLine()) != null) {
                if (line.length() < wocidx) {
                    continue;
                }
                String caption = Bytes.substring(line, capidx, cmdidx - 1)
                        .trim();
                String cmd = Bytes.substring(line, cmdidx, kmtidx - 1).trim();
                if (cmd.indexOf("wmic.exe") >= 0) {
                    continue;
                }
                if (caption.equals("System Idle Process")
                        || caption.equals("System")) {
                    idletime += Long.valueOf(
                            Bytes.substring(line, kmtidx, rocidx - 1).trim())
                            .longValue();
                    idletime += Long.valueOf(
                            Bytes.substring(line, umtidx, wocidx - 1).trim())
                            .longValue();
                    continue;
                }

                kneltime += Long.valueOf(
                        Bytes.substring(line, kmtidx, rocidx - 1).trim())
                        .longValue();
                usertime += Long.valueOf(
                        Bytes.substring(line, umtidx, wocidx - 1).trim())
                        .longValue();
            }
            retn[0] = idletime;
            retn[1] = kneltime + usertime;
            return retn;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                proc.getInputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        testOne();

        System.out.println(testLinux());
    }

    private static float testLinux() throws IOException {

        InputStream inputStream = new ByteArrayInputStream(top_result_string.getBytes());

        InputStreamReader isr = null;
        BufferedReader brStat = null;
        StringTokenizer tokenStat = null;

        isr = new InputStreamReader(inputStream);
        brStat = new BufferedReader(isr);

        brStat.readLine();
        brStat.readLine();

        tokenStat = new StringTokenizer(brStat.readLine());
        tokenStat.nextToken();
        tokenStat.nextToken();
        tokenStat.nextToken();
        tokenStat.nextToken();
        tokenStat.nextToken();
        tokenStat.nextToken();
        tokenStat.nextToken();
        String cpuUsage = tokenStat.nextToken();

        System.out.println("CPU idle : " + cpuUsage);

        Float usage = Float.valueOf(cpuUsage);

        return (1 - usage.floatValue() / 100);

    }

    private static void testOne() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataString = sdf.format(new Date());
        LOGGER.info(dataString + ": cpu占有率 = [" + CpuUsageUtils.getCpuRatio() + "]");

    }

    static String top_result_string = "top - 15:32:28 up 465 days, 11:11,  1 user,  load average: 0.00, 0.01, 0.05\n"
            + "Tasks:  75 total,   2 running,  73 sleeping,   0 stopped,   0 zombie\n"
            + "%Cpu(s):  1.0 us,  0.6 sy,  0.0 ni, 98.4 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st\n"
            + "KiB Mem :  1883724 total,   232180 free,  1185220 used,   466324 buff/cache\n"
            + "KiB Swap:   999992 total,   309992 free,   690000 used.   453228 avail Mem \n";

}