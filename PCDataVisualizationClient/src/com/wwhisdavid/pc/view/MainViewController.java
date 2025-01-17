package com.wwhisdavid.pc.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
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

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.*;

import com.wwhisdavid.send.NettyClient;
import com.wwhisdavid.pc.entity.ANodeDetailEntity;
import com.wwhisdavid.pc.util.DateUtil;

public class MainViewController extends ApplicationFrame {
	private ServerSocket ss = null;
	private Socket socket = null;
	private static Set<String> params = new HashSet<>();
	private static String host = "127.0.0.1";
	private String user;
	private String node_id;
	ChartPanel chartpanel = null;
	private JTextArea jTextArea = null;
	private JButton jButtonForBoldLine = null;
	private JButton jButtonForSpline = null;
	private JButton jButtonForSegment = null;
	private JButton jButtonNextData = null;
	private JButton jButtonPreData = null;
	private JButton jButtonShowAll = null;
	private JButton jButtonForHour = null;
	private JButton jButtonNextHour = null;
	private JButton jButtonPreHour = null;

	private static int page = -1;
	private static int totalPage = 0;
	private static int totalCount = 0;
	
	private static int hourPage = -1;
	private static int totalHourPage = 0;
	private static int totalHourCount = 0;
	private static int hourIndexStart = -1;
	private static int hourIndexEnd = -1;

	private static List<ANodeDetailEntity> totalList = new ArrayList<>();

	public MainViewController(String title) {
		super(title);
		
		JMenuBar jMenuBar = new JMenuBar();

		JMenu jMenu = new JMenu();
		jMenu.setText("选项");

		JMenuItem jMenuItemCommand = new JMenuItem("发送命令");
		jMenuItemCommand.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] options = { "1h", "1d", "1w", "stop" };
				JOptionPane jOptionPane = new JOptionPane();
				if (isShowing()) { // fix : component must be showing on the
									// screen to determine its location
					getLocationOnScreen();
				}
				String command = (String) jOptionPane.showInputDialog(null, "注: 1h-1小时一次、1d-1天一次、1w-1周一次、stop-停止采样",
						"请选择采样频率", jOptionPane.PLAIN_MESSAGE, null, options, "1h");
				if (command != null && command.length() > 0) {
					// 1DCCADFED7BCBB036C56A4AFB97E906F#node_id&command_time&user&command
//					System.out.println(user + "-" + node_id + "-" + System.currentTimeMillis() / 1000 +"-"+ command);
					String message = "1DCCADFED7BCBB036C56A4AFB97E906F#"
									+node_id
									+"&"
									+System.currentTimeMillis() / 1000
									+"&"
									+user
									+"&"
									+command
									+"$";
					try {
						NettyClient.send(message);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					try {
//						NettyClient.testConcurrent();
//					} catch (InterruptedException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
				}

			}
		});

		JMenuItem jMenuItemShowAll = new JMenuItem("显示全部数据");
		jMenuItemShowAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				jButtonShowAll.doClick();
			}
		});
		
		JMenuItem jMenuItemShowForSegment = new JMenuItem("显示分段数据（20个/组）");
		jMenuItemShowForSegment.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				jButtonForSegment.doClick();
			}
		});
		
		JMenuItem jMenuItemShowHour = new JMenuItem("按小时显示");
		jMenuItemShowHour.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				jButtonForHour.doClick();
			}
		});
		
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
				String fileName = selectedFile.getName();
				String[] info = fileName.split("_");
				user = info[0];
				node_id = info[1];
				
				jMenuItemCommand.setEnabled(true);
				jMenuItemShowAll.setEnabled(true);
				jMenuItemShowForSegment.setEnabled(true);
				jMenuItemShowHour.setEnabled(true);
				
				List<ANodeDetailEntity> list = openXMLFile(selectedFile);
				totalList = list;
//				System.out.println(params.toString());
				try {
					resetPanel(list, XYLineAndShapeRenderer.class);
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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

		

		if (user == null || user.length() == 0) {
			jMenuItemCommand.setEnabled(false);
			jMenuItemShowAll.setEnabled(false);
			jMenuItemShowForSegment.setEnabled(false);
			jMenuItemShowHour.setEnabled(false);
		}

		jMenu.add(jMenuItem1);
		jMenu.add(jMenuItem2);
		jMenu.add(jMenuItemCommand);
		jMenu.add(jMenuItemShowAll);
		jMenu.add(jMenuItemShowForSegment);
		jMenu.add(jMenuItemShowHour);
		

		jMenuBar.add(jMenu);
		setJMenuBar(jMenuBar);

		setPreferredSize(new Dimension(1200, 600));

	}

	private static <T extends XYItemRenderer> JFreeChart createChart(XYDataset xydataset, Class<T> clazz)
			throws InstantiationException, IllegalAccessException {
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
		resetRender(clazz, xyplot);
		DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
		dateaxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss"));
		return jfreechart;
	}

	private static XYDataset createDataset(List<ANodeDetailEntity> list) {
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		for (String param : params) {
			TimeSeries timeseries = new TimeSeries(param, org.jfree.data.time.Second.class);
			for (ANodeDetailEntity entity : list) {
//				System.out.println("time:" + entity.getRecord_time());
//				System.out.println(entity.getStress_y());
				switch (param) {
				case "humidity":
					timeseries.addOrUpdate(new Second(new Date(entity.getRecord_time() * 1000)), entity.getHumidity());
					break;
				case "temperature":
					timeseries.addOrUpdate(new Second(new Date(entity.getRecord_time() * 1000)), entity.getTemperature());
					break;
				case "stress-x":
					timeseries.addOrUpdate(new Second(new Date(entity.getRecord_time() * 1000)), entity.getStress_x());
					break;
				case "stress-y":
					timeseries.addOrUpdate(new Second(new Date(entity.getRecord_time() * 1000)), entity.getStress_y());
					break;
				case "stress-z":
					timeseries.addOrUpdate(new Second(new Date(entity.getRecord_time() * 1000)), entity.getStress_z());
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
//			System.out.println(data);

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
			totalCount = recordList.size();
			totalPage = totalCount / 20;
			if (totalCount % 20 > 0) {
				totalPage++;
			}
		} catch (DocumentException ex) {
			Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
		if (totalCount > 0) {
			int time = (int)(recordList.get(totalCount - 1).getRecord_time() - recordList.get(0).getRecord_time());
			totalHourCount = time / 3600 + (time % 3600 > 0 ? 1 : 0);
			totalHourPage = totalHourCount;
		}
		System.out.println(totalHourCount+"~~~~~~~~~");
		return recordList;
	}

	private static <T extends XYItemRenderer> void resetRender(Class<T> clazz, XYPlot xyplot)
			throws InstantiationException, IllegalAccessException {
		XYItemRenderer xyitemrenderer = clazz.newInstance();
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
		} else if (xyitemrenderer instanceof XYSplineRenderer) {
			XYSplineRenderer renderer = (XYSplineRenderer) xyitemrenderer;
			renderer.setBaseShapesVisible(false); // 绘制的线条上不显示图例，如果显示的话，会使图片变得很丑陋
			renderer.setPrecision(5); // 设置精度，大概意思是在源数据两个点之间插入5个点以拟合出一条平滑曲线
			xyplot.setRenderer(renderer);
		}
	}

	private <T extends XYItemRenderer> void resetPanel(List<ANodeDetailEntity> list, Class<T> clazz)
			throws InstantiationException, IllegalAccessException {
//		System.out.println(list.size()+"````````````");
		XYDataset xydataset = createDataset(list);
		JFreeChart jfreechart = createChart(xydataset, clazz);
		
		chartpanel = new ChartPanel(jfreechart, false);
		chartpanel.setPreferredSize(new Dimension(1000, 500));
		chartpanel.setMouseZoomable(false);
		
		chartpanel = new ChartPanel(jfreechart, false);
		

		chartpanel.addChartMouseListener(new ChartMouseListener() {

			@Override
			public void chartMouseMoved(ChartMouseEvent cme) {
				report(cme);

			}

			@Override
			public void chartMouseClicked(ChartMouseEvent cme) {
				tip(cme);
			}

			private void tip(ChartMouseEvent cme) {
				jTextArea.setText("将鼠标移至需要查看点处！");
			}

			private void report(ChartMouseEvent cme) {
				ChartEntity ce = cme.getEntity();
				if (ce instanceof XYItemEntity) {
					XYItemEntity e = (XYItemEntity) ce;
					XYDataset d = e.getDataset();
					int s = e.getSeriesIndex();
					int i = e.getItem();
//					System.out.println("X:" + d.getX(s, i) + ", Y:" + d.getY(s, i));
					String date = DateUtil.unixTime2StringSecond(d.getX(s, i).longValue() + "");
					jTextArea.setText("日期:" + date + "-" + "数值:" + d.getY(s, i));
				}
			}
		});
		if (jTextArea == null) {
			jTextArea = new JTextArea();
		}

		jTextArea.setText("将鼠标移至需要查看点处！");
		jTextArea.setFont(new Font("Default", Font.PLAIN, 10));
		jTextArea.setEditable(false);
		jTextArea.setPreferredSize(new Dimension(180, 15));
		jTextArea.setBackground(Color.gray);
		jTextArea.setBounds(0, 0, 180, 15);
		
		
		
		jButtonForBoldLine = new JButton("显示折线图");
		
		jButtonForBoldLine.setBounds(0, 15, 90, 15);
		jButtonForBoldLine.setFont(new Font("Default", Font.PLAIN, 10));
		jButtonForBoldLine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					resetPanel(list, XYLineAndShapeRenderer.class);
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

//		if (jButtonForSpline == null) { // 这样会一直捕获一个list
			jButtonForSpline = new JButton("显示曲线图");
//		}
		
		jButtonForSpline.setBounds(95, 15, 90, 15);
		jButtonForSpline.setFont(new Font("Default", Font.PLAIN, 10));
		jButtonForSpline.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println(list.size());
					resetPanel(list, XYSplineRenderer.class);
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

//		if (jButtonForSegment == null) {
			jButtonForSegment = new JButton("显示分段数据(20)");
//		}
		
		jButtonForSegment.setBounds(190, 15, 90, 15);
		jButtonForSegment.setFont(new Font("Default", Font.PLAIN, 9));
		jButtonForSegment.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				hourPage = -1;
				List<ANodeDetailEntity> pageList = new ArrayList<>();
				if (totalCount > 20) {
					jButtonNextData.setEnabled(true);
				}
				jButtonNextHour.setEnabled(false);
				for (int num = 0; num < 20; num++) {
					pageList.add(totalList.get(num));
				}
				try {
					page = 0;
					resetPanel(pageList, XYLineAndShapeRenderer.class);
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

//		if (jButtonNextData == null) {
			jButtonNextData = new JButton("下一组数据");
//		}
		
		jButtonNextData.setBounds(380, 15, 90, 15);
		jButtonNextData.setFont(new Font("Default", Font.PLAIN, 9));
		if (page < 0 || page == totalPage - 1) {
			jButtonNextData.setEnabled(false);
		}
		jButtonNextData.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				page++;
				if (page > 0) {
					jButtonPreData.setEnabled(true);
				}
				List<ANodeDetailEntity> pageList = new ArrayList<>();
				for (int num = page * 20; num < (page + 1) * 20 && num < totalCount; num++) {
					pageList.add(totalList.get(num));
				}
				try {
					resetPanel(pageList, XYLineAndShapeRenderer.class);
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

//		if (jButtonPreData == null) {
			jButtonPreData = new JButton("上一组数据");
//		}
		
		jButtonPreData.setBounds(285, 15, 90, 15);
		jButtonPreData.setFont(new Font("Default", Font.PLAIN, 9));
		if (page == -1 || page == 0) {
			jButtonPreData.setEnabled(false);
		}
		jButtonPreData.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				page--;
				if (page == 0) {
					jButtonPreData.setEnabled(false);
				}
				List<ANodeDetailEntity> pageList = new ArrayList<>();
				for (int num = page * 20; num < (page + 1) * 20 && num < totalCount; num++) {
					pageList.add(totalList.get(num));
				}
				try {
					resetPanel(pageList, XYLineAndShapeRenderer.class);
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

//		if (jButtonShowAll == null) {
			jButtonShowAll = new JButton("展示所有");
//		}
		
		jButtonShowAll.setBounds(475, 15, 80, 15);
		jButtonShowAll.setFont(new Font("Default", Font.PLAIN, 9));
		jButtonShowAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					page = -1;
					hourPage = -1;
					resetPanel(totalList, XYLineAndShapeRenderer.class);
				} catch (InstantiationException | IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		
		
//		if (jButtonPreHour == null) {
			jButtonPreHour = new JButton("上小时数据");
//		}
		
		jButtonPreHour.setBounds(645, 15, 90, 15);
		jButtonPreHour.setFont(new Font("Default", Font.PLAIN, 9));
		if (hourPage == -1 || hourPage == 0) {
			jButtonPreHour.setEnabled(false);
		}
		jButtonPreHour.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hourPage --;
				System.out.println("Pre hourPage:"+ hourPage);
				System.out.println("hourIndexStart:"+ hourIndexStart + "   hourIndexEnd:" + hourIndexStart);
				if (hourPage == 0) {
					jButtonPreHour.setEnabled(false);
				}
				List<ANodeDetailEntity> pageHourList = new ArrayList<>();
				hourIndexEnd = hourIndexStart;
				for(int i = hourIndexStart - 1; i >= 0; i --){
					ANodeDetailEntity entity = totalList.get(i);
					long time = totalList.get(hourIndexEnd - 1).getRecord_time() - entity.getRecord_time();
					
					if (time >= 3600) {
						hourIndexStart = i + 1;
						break;
					}else {
						hourIndexStart = i;
						pageHourList.add(entity);
					}
				}
				try {
					resetPanel(pageHourList, XYLineAndShapeRenderer.class);
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

//		if (jButtonNextHour == null) {
			jButtonNextHour = new JButton("下小时数据");
//		}
		
		jButtonNextHour.setBounds(740, 15, 90, 15);
		jButtonNextHour.setFont(new Font("Default", Font.PLAIN, 9));
		if (hourPage < 0 || hourPage == totalHourPage - 1) {
			jButtonNextHour.setEnabled(false);
		}
		
		jButtonNextHour.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hourPage++;
				System.out.println("Next hourPage:"+ hourPage);
				System.out.println("hourIndexStart:"+ hourIndexStart + "   hourIndexEnd:" + hourIndexStart);
				if (hourPage > 0) {
					jButtonPreHour.setEnabled(true);
				}
				List<ANodeDetailEntity> pageHourList = new ArrayList<>();
				hourIndexStart = hourIndexEnd;
				
				for(int i = hourIndexEnd; i < totalList.size() - 1; i ++){
					ANodeDetailEntity entity = totalList.get(i);
					long time = entity.getRecord_time() - totalList.get(hourIndexStart).getRecord_time();
					hourIndexEnd = i;
					if (time >= 3600) {
						break;
					}else {
						pageHourList.add(entity);
						
					}
				}
				try {
					resetPanel(pageHourList, XYLineAndShapeRenderer.class);
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
//		if (jButtonForHour == null) {
			jButtonForHour = new JButton("按小时显示");// 只显示第一个小时的先
//		}
		
		jButtonForHour.setBounds(560, 15, 80, 15);
		jButtonForHour.setFont(new Font("Default", Font.PLAIN, 9));
		jButtonForHour.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					page = -1;
					jButtonNextData.setEnabled(false);
					List<ANodeDetailEntity> pageHourList = new ArrayList<>();
					for(int i = 0; i < totalList.size(); i ++){
						ANodeDetailEntity entity = totalList.get(i);
						long time = entity.getRecord_time() - totalList.get(0).getRecord_time();
						hourIndexStart = 0;
						hourIndexEnd = i;
						System.out.println("hourIndexStart:"+ hourIndexStart + "   hourIndexEnd:" + hourIndexStart);
						hourPage = 0;
						if (time >= 3600) {
							jButtonNextHour.setEnabled(true);
							break;
						}
						else {
							pageHourList.add(entity);
						}
					}			
					resetPanel(pageHourList, XYLineAndShapeRenderer.class);
				} catch (InstantiationException | IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		chartpanel.setLayout(null);
		chartpanel.add(jTextArea);
		chartpanel.add(jButtonForBoldLine);
		chartpanel.add(jButtonForSpline);
//		chartpanel.add(jButtonForSegment);
		chartpanel.add(jButtonNextData);
		chartpanel.add(jButtonPreData);
//		chartpanel.add(jButtonShowAll);
//		chartpanel.add(jButtonForHour);
		chartpanel.add(jButtonNextHour);
		chartpanel.add(jButtonPreHour);
		
		setContentPane(chartpanel);
		revalidate();
	}

	private void sendMsg(String command) throws UnknownHostException, IOException{
		socket = new Socket(host, 12345);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		bw.write(command);
		bw.flush();
		
		socket.shutdownOutput();
				
		
		BufferedReader brClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String client = brClient.readLine();
//		System.out.println(client+"*********");
		brClient.close();
		socket.close();
		socket = null;
	}
	
	private void sendMsg_netty(String command){
		new Thread(new Runnable() {
			public void run() {
				try {
					new NettyClient().connect(12345, host, command);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
	public static void main(String[] args) {
		MainViewController mainViewController = new MainViewController("WHU-EIS");

		mainViewController.pack();
		RefineryUtilities.centerFrameOnScreen(mainViewController);
		mainViewController.setVisible(true);
	}

}
