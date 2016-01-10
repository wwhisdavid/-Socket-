package com.wwhisdavid.chart;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;

/**
 * Servlet implementation class ChartServlet
 */
@WebServlet("/ChartServlet")
public class ChartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChartServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 获取数据
		
		// 1. 当前时间
		long currentTime = System.currentTimeMillis();
		long fromTime = System.currentTimeMillis() - 24*60*60*1000; // 前24小时
		long toTime = System.currentTimeMillis() + 24*60*60*1000;  // 后24小时
		
		
		
		JFreeChart chart = this.creatChart();
		ChartUtilities.writeChartAsJPEG(response.getOutputStream(), chart, 500, 500);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	private JFreeChart creatChart(){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		 String[] time = new String[15];
        String[] timeValue = { "6-1日", "6-2日", "6-3日", "6-4日", "6-5日", "6-6日",
                      "6-7日", "6-8日", "6-9日", "6-10日", "6-11日", "6-12日", "6-13日",
                      "6-14日", "6-15日" };
        for (int i = 0; i < 15; i++) {
               time[i] = timeValue[i];
        }
//随机添加数据值
        for (int i = 0; i < 15; i++) {
               Random r = new Random();
               dataset.addValue(i + i * 9.34 + r.nextLong() % 100,"test1", time[i]); // 对应的横轴
        }
		JFreeChart chart = ChartFactory.createLineChart(
				"test",
				"时间", 
				"温度", 
				dataset, 
				PlotOrientation.VERTICAL, 
				true, 
				true, 
				false);
		
		// 字体是否模糊边界
       chart.setTextAntiAlias(false);
       chart.setBackgroundPaint(Color.BLACK);
       // 设置图标题的字体重新设置title
       Font font = new Font("隶书", Font.BOLD, 25);
       TextTitle chartTitle = new TextTitle("test");
       chartTitle.setFont(font);
       chart.setTitle(chartTitle);
      
       // 背景色
       chart.setBackgroundPaint(Color.WHITE);
       // 配置字体（解决中文乱码的通用方法）
       Font xfont = new Font("宋体", Font.PLAIN, 16); // X轴
       Font yfont = new Font("宋体", Font.PLAIN, 16); // Y轴
       Font kfont = new Font("宋体", Font.PLAIN, 14); // 底部
       Font titleFont = new Font("隶书", Font.BOLD, 20); // 图片标题
       CategoryPlot categoryPlot = chart.getCategoryPlot();
       categoryPlot.getDomainAxis().setLabelFont(xfont);
       categoryPlot.getDomainAxis().setLabelFont(xfont);
       categoryPlot.getRangeAxis().setLabelFont(yfont);
       chart.getLegend().setItemFont(kfont);
       chart.getTitle().setFont(titleFont);
       categoryPlot.setBackgroundPaint(new Color(101, 171, 17));
       // 设置网格显示
       // plot.setDomainGridlinesVisible(true);
       // x轴 // 分类轴网格是否可见
       categoryPlot.setDomainGridlinesVisible(true);
       // y轴 //数据轴网格是否可见
       categoryPlot.setRangeGridlinesVisible(true);
       // 设置网格竖线颜色
       categoryPlot.setDomainGridlinePaint(Color.LIGHT_GRAY);
       // 设置网格横线颜色
       categoryPlot.setRangeGridlinePaint(Color.LIGHT_GRAY);
       // 没有数据时显示的文字说明
       categoryPlot.setNoDataMessage("没有数据显示");
       // 设置曲线图与xy轴的距离
       categoryPlot.setAxisOffset(new RectangleInsets(0d, 0d, 0d, 0d));
       // 设置面板字体
       Font labelFont = new Font("SansSerif", Font.TRUETYPE_FONT, 12);
       // 取得Y轴
       NumberAxis rangeAxis = (NumberAxis)categoryPlot.getRangeAxis();
       rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
       rangeAxis.setAutoRangeIncludesZero(true);
       // rangeAxis.setUpperMargin(0.20);
       // rangeAxis.setLabelAngle(Math.PI / 2.0);
       // 取得X轴
       CategoryAxis categoryAxis = (CategoryAxis)categoryPlot.getDomainAxis();
       // 设置X轴坐标上的文字
       categoryAxis.setTickLabelFont(labelFont);
       // 设置X轴的标题文字
       categoryAxis.setLabelFont(labelFont);
       // 横轴上的 Lable 45度倾斜
       categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
       // 设置距离图片左端距离
       categoryAxis.setLowerMargin(0.0);
       // 设置距离图片右端距离
       categoryAxis.setUpperMargin(0.0);
		
    // 获得renderer 注意这里是下嗍造型到lineandshaperenderer！！
       LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer)categoryPlot.getRenderer();
       lineandshaperenderer.setBaseShapesVisible(true);
       // series 点（即数据点）可见
       lineandshaperenderer.setBaseLinesVisible(true);
       // series 点（即数据点）间有连线可见 显示折点数据
       lineandshaperenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
       lineandshaperenderer.setBaseItemLabelsVisible(true);
       return chart;
       
	}
}
