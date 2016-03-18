package com.wwhisdavid.pc.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.*;

import com.wwhisdavid.pc.entity.ANodeDetailEntity;
import com.wwhisdavid.pc.util.DateUtil;

public class MainViewController extends ApplicationFrame {
	private ServerSocket ss = null;
	private Socket socket = null;
	private static Set<String> params = new HashSet<>();
	ChartPanel chartpanel = null;
	private JTextArea jTextArea = null;
	
	public MainViewController(String title) {
		super(title);
		JMenuBar jMenuBar = new JMenuBar();

		JMenu jMenu = new JMenu();
		jMenu.setText("选项");

		JMenuItem jMenuItem1 = new JMenuItem("打开本地文件");
		jMenuItem1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jf = new JFileChooser(); // 打开文件控件
				jf.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
				FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("请选择xml文件", "xml");
				jf.setFileFilter(fileNameExtensionFilter);
				jf.showDialog(null, null);

				File selectedFile = jf.getSelectedFile();
				if (selectedFile == null) {
					return;
				}
				List<ANodeDetailEntity> list = openXMLFile(selectedFile);

				System.out.println(params.toString());
				resetPanel(list);
			}
		});

		JMenuItem jMenuItem2 = new JMenuItem("等待接收");
		jMenuItem2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				waitReceive(); // 打开socket 等待web数据传输,软件阻塞。
				try {
					handleData(); // 处理数据并封装
				} catch (IOException ex) {
					Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});

		jMenu.add(jMenuItem1);
		jMenu.add(jMenuItem2);

		jMenuBar.add(jMenu);
		setJMenuBar(jMenuBar);

		setPreferredSize(new Dimension(500, 270));

	}

	private static JFreeChart createChart(XYDataset xydataset) {
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("水库监测", "记录日期", "温度/湿度/压强", xydataset, true, true,
				true);
		jfreechart.setBackgroundPaint(Color.white);
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setBackgroundPaint(Color.lightGray);
		xyplot.setDomainGridlinePaint(Color.white);
		xyplot.setRangeGridlinePaint(Color.white);
		xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
		xyplot.setDomainCrosshairVisible(true);
		xyplot.setRangeCrosshairVisible(true);
		org.jfree.chart.renderer.xy.XYItemRenderer xyitemrenderer = xyplot.getRenderer();

		if (xyitemrenderer instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyitemrenderer;
			xylineandshaperenderer.setBaseShapesVisible(true);
			xylineandshaperenderer.setBaseShapesFilled(true);

			xylineandshaperenderer.setBaseItemLabelsVisible(true);
			xylineandshaperenderer.setBasePositiveItemLabelPosition(
					new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
			xylineandshaperenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
			xylineandshaperenderer.setBaseItemLabelFont(new Font("Dialog", 1, 8));
			xyplot.setRenderer(xylineandshaperenderer);
		}
		DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
		dateaxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd:HH"));
		return jfreechart;
	}
	
	private static XYDataset createDataset(List<ANodeDetailEntity> list) {
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		for (String param : params) {
			TimeSeries timeseries = new TimeSeries(param, org.jfree.data.time.Hour.class);
			for (ANodeDetailEntity entity : list) {
				System.out.println("time:" + entity.getRecord_time());
				System.out.println(entity.getStress_y());
				switch (param) {
				case "humidity":
					timeseries.add(new Hour(new Date(entity.getRecord_time() * 1000)), entity.getHumidity());
					break;
				case "temperature":
					timeseries.addOrUpdate(new Hour(new Date(entity.getRecord_time() * 1000)), entity.getTemperature());
					break;
				case "stress-x":
					timeseries.addOrUpdate(new Hour(new Date(entity.getRecord_time() * 1000)), 1);
					break;
				case "stress-y":
					timeseries.addOrUpdate(new Hour(new Date(entity.getRecord_time() * 1000)), entity.getStress_y());
					break;
				case "stress-z":
					timeseries.addOrUpdate(new Hour(new Date(entity.getRecord_time() * 1000)), entity.getStress_z());
					break;
				default:
					break;
				}

			}
			timeseriescollection.addSeries(timeseries);
		}
		return timeseriescollection;
	}

	private void waitReceive() {
		try {
			if (ss == null) {
				ss = new ServerSocket(9999);
				socket = ss.accept();
			}
		} catch (IOException ex) {
			Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private List handleData() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedWriter bw = null;
		String data = null;
		String fileName = new String();
		FileWriter fileWriter = null;
		File file = null;

		while ((data = br.readLine()) != null) { // 阻塞
			System.out.println(data);

			if (data.startsWith("whu&")) {
				fileName = data.substring(4);
				file = new File("./" + fileName);
				if (file.exists()) {
					break;
				} else {
					file.createNewFile();
					continue;
				}
			}
			if (fileName.length() == 0) {
				break;
			}

			if (fileWriter == null) {
				fileWriter = new FileWriter(file);
			}
			if (bw == null) {
				bw = new BufferedWriter(fileWriter);
			}

			bw.write(data);
			bw.flush();
			bw.newLine();
		}
		bw.close();
		// 给出反馈
		BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		bw2.write("received!");
		bw2.newLine();
		bw2.flush();
		bw2.close();

		socket.close();

		return openXMLFile(file);
	}

	private List openXMLFile(File file) {
		ArrayList<ANodeDetailEntity> recordList = new ArrayList<ANodeDetailEntity>();
		try {
			Document doc = new SAXReader().read(file);
			Iterator<Element> iterator = doc.getRootElement().elementIterator("record");
			while (iterator.hasNext()) {
				Element next = iterator.next();
				ANodeDetailEntity entity = new ANodeDetailEntity();
				entity.setRecord_time(Integer.valueOf(next.attributeValue("record_time")));
				if (next.elementText("humidity") != null) {
					entity.setHumidity(Float.valueOf(next.elementText("humidity")));
					params.add("humidity");
				}
				if (next.elementText("temperature") != null) {
					entity.setTemperature(Float.valueOf(next.elementText("temperature")));
					params.add("temperature");
				}
				if (next.elementText("stress-x") != null) {
					entity.setStress_x(Float.valueOf(next.elementText("stress-x")));
					params.add("stress-x");
				}
				if (next.elementText("stress-y") != null) {
					entity.setStress_y(Float.valueOf(next.elementText("stress-y")));
					params.add("stress-y");
				}
				if (next.elementText("stress-z") != null) {
					entity.setStress_z(Float.valueOf(next.elementText("stress-z")));
					params.add("stress-z");
				}
				recordList.add(entity);
				System.out.println(entity.toString());
			}
		} catch (DocumentException ex) {
			Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
		return recordList;
	}

	private void resetPanel(List<ANodeDetailEntity> list) {
		XYDataset xydataset = createDataset(list);
		JFreeChart jfreechart = createChart(xydataset);
		ChartPanel chartpanel = new ChartPanel(jfreechart, false);
		chartpanel.setPreferredSize(new Dimension(500, 270));
		chartpanel.setMouseZoomable(false);

		chartpanel.addChartMouseListener(new ChartMouseListener() {

			@Override
			public void chartMouseMoved(ChartMouseEvent cme) {
				report(cme);

			}

			@Override
			public void chartMouseClicked(ChartMouseEvent cme) {
				tip(cme);
			}
			
			private void tip(ChartMouseEvent cme){
				jTextArea.setText("将鼠标移至需要查看点处！");
			}
			
			private void report(ChartMouseEvent cme) {
				ChartEntity ce = cme.getEntity();
				if (ce instanceof XYItemEntity) {
					XYItemEntity e = (XYItemEntity) ce;
					XYDataset d = e.getDataset();
					int s = e.getSeriesIndex();
					int i = e.getItem();
					System.out.println("X:" + d.getX(s, i) + ", Y:" + d.getY(s, i));
					String date = DateUtil.unixTime2StringSecond(d.getX(s, i).longValue()+"");
					jTextArea.setText("日期:" + date + "-" + "数值:" + d.getY(s, i));
				}
			}
		});
		jTextArea = new JTextArea();

		jTextArea.setText("将鼠标移至需要查看点处！");
		jTextArea.setFont(new Font("Default", Font.PLAIN, 10));
		jTextArea.setEditable(false);
		jTextArea.setPreferredSize(new Dimension(180, 15));
		jTextArea.setBackground(Color.gray);
		jTextArea.setBounds(0, 0, 180, 15);
		chartpanel.setLayout(null);
		chartpanel.add(jTextArea);
		setContentPane(chartpanel);
		revalidate();
	}

	
	public static void main(String[] args) {
		MainViewController mainViewController = new MainViewController("WHU-EIS");
		mainViewController.pack();
		RefineryUtilities.centerFrameOnScreen(mainViewController);
		mainViewController.setVisible(true);
	}

}
