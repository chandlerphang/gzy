package com.cactus.guozy.common;
//1.将商品零售的信息打印成小票的形式。

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cactus.guozy.core.domain.Goods;
import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.domain.OrderItem;

public class PrintOrder implements Printable {
	private String title;
	private Order mOrder;
	private final int lineGoodsNameCount = 10;

	public PrintOrder() {

	}

	/* 对应于商品零售的构造方法 */
	public PrintOrder(String title, Order order) {
		this.title = title;
		this.mOrder=order;
	}

	public PrintOrder(Order message, String printType) {
		this.mOrder = message;
	}

	@Override
	public int print(Graphics gra, PageFormat pf, int pageIndex) throws PrinterException {
		try {
			/**
			 * * @param Graphic指明打印的图形环境 * @param
			 * PageFormat指明打印页格式（页面大小以点为计量单位，1
			 * 点为1英寸的1/72，1英寸为25.4毫米。A4纸大致为595×842点） * @param pageIndex指明页号
			 **/

			// 转换成Graphics2D
			Graphics2D g2_1 = (Graphics2D) gra;
			// Graphics2D g2_2 = (Graphics2D) gra;

			// 设置打印颜色为黑色
			g2_1.setColor(Color.black);
			// g2_2.setColor(Color.black);
			// 打印起点坐标
			switch (pageIndex) {
			case 0:
				// 设置打印字体（字体名称、样式和点大小）（字体名称可以是物理或者逻辑名称）
				// Java平台所定义的五种字体系列：Serif、SansSerif、Monospaced、Dialog 和
				// DialogInput
				Font font = new Font("宋体", Font.PLAIN, 14);
				Font font2 = new Font("宋体", Font.PLAIN, 9);

				g2_1.setFont(font2); // 设置字体
				// BasicStroke bs_3=new BasicStroke(0.5f);
				float[] dash1 = { 2.0f };
				// 设置打印线的属性。
				// 1.线宽 2、3、不知道，4、空白的宽度，5、虚线的宽度，6、偏移量
				g2_1.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash1, 0.0f));

				double x = pf.getImageableX();
				double y = pf.getImageableY();
				float heigth1 = font.getSize2D();
				float heigth = font2.getSize2D() + 2; // 字体高度
				float space_1 = 90;
				float space_2 = 120;
					
				String shipAddress=mOrder.getShipAddr().getAddrLine1()+mOrder.getShipAddr().getAddrLine2();
				int lineAddressNum=11;
				//1、 【商家小票】
				/* 如果调用该方法的是要打印"商品零售"的小票的信息，则调用该 方法 */
				Date date = new Date();
				DateFormat df = DateFormat.getDateTimeInstance();
				g2_1.setFont(font); // 设置字体
				g2_1.drawString(title + "（商家留）", (float) x + 20, (float) y + 1 * heigth1);
				g2_1.setFont(font2);
				g2_1.drawString("订单号："+mOrder.getOrderNumber(), (float) x, (float) y + 2 * heigth1+5);
				g2_1.drawLine((int) x, (int) (y + 3 * heigth1), (int) x + 144, (int) (y + 3 * heigth1));
				g2_1.setFont(font2);
				g2_1.drawString("名称", (float) x, (float) y + 4 * heigth1);
				g2_1.setFont(font2); // 设置字体
				g2_1.setFont(font2); // 设置字体
				g2_1.drawString("数量", (float) x + space_1, (float) y + 4 * heigth1);
				g2_1.setFont(font2); // 设置字体
				g2_1.drawString("金额", (float) x + space_2, (float) y + 4 * heigth1);
				g2_1.setFont(font2); // 设置字体
				g2_1.drawLine((int) x - 2, (int) (y + 6 * heigth- 7), (int) x + 144, (int) (y + 6 * heigth- 7));

				int y1 = 0;
				g2_1.setFont(font2);
				
				for (OrderItem orderItem : mOrder.getOrderItems()) {
					if (orderItem.getName().length() > lineGoodsNameCount) {
						g2_1.drawString(orderItem.getName().substring(0, lineGoodsNameCount), (float) x,
								(float) y + ((float) 6.5 + y1) * heigth);
					} else {
						g2_1.drawString(orderItem.getName().substring(0, orderItem.getName().length()), (float) x,
								(float) y + ((float) 6.5 + y1) * heigth);
					}

					g2_1.drawString("*" + orderItem.getQuantity(), (float) x + space_1,
							(float) y + ((float) 6.5 + y1) * heigth);
					g2_1.drawString((orderItem.getPrice().floatValue()*orderItem.getQuantity().floatValue())+ "", (float) x + space_2,
							(float) y + ((float) 6.5 + y1) * heigth);

					y1++;
					if (orderItem.getName().length() / lineGoodsNameCount > 0) {
						if (orderItem.getName().length() < (lineGoodsNameCount * 2)) {
							g2_1.drawString(
									orderItem.getName().substring(lineGoodsNameCount, orderItem.getName().length()),
									(float) x, (float) y + ((float) 6.5 + y1) * heigth);
						} else {
							g2_1.drawString(
									orderItem.getName().substring(lineGoodsNameCount, lineGoodsNameCount * 2 - 2) + "...",
									(float) x, (float) y + ((float) 6.5 + y1) * heigth);
						}
						y1++;
					}
				}
				// 配送费
				g2_1.drawLine((int) x - 2, (int) (y + ((float) 6.5 + y1) * heigth), (int) x + 144,
						(int) (y + ((float) 6.5 + y1) * heigth));
				y1++;
				g2_1.drawString("配送费：", (float) x, (float) y + ((float) 6.5 + y1) * heigth);
				g2_1.drawString(mOrder.getShipPrice()+"", (float) x + space_2, (float) y + ((float) 6.5 + y1) * heigth);
				y1++;
				g2_1.drawLine((int) x - 2, (int) (y + ((float) 6.5 + y1) * heigth-7), (int) x + 144,
						(int) (y + ((float) 6.5 + y1) * heigth-7));
				y1++;
				g2_1.drawString("总金额：" + mOrder.getTotal()+"元", (float) x, (float) y + ((float) 6.5 + y1) * heigth);
				y1++;
				g2_1.drawString("收货人：" + mOrder.getShipAddr().getName(), (float) x, (float) y + ((float) 6.5 + y1) * heigth);
				y1++;
				if(shipAddress.length()<lineAddressNum){
					g2_1.drawString("收货地址：" +shipAddress, (float) x, (float) y + ((float) 6.5 + y1) * heigth);
				}else{
					g2_1.drawString("收货地址：" +shipAddress.substring(0, lineAddressNum-1), (float) x, (float) y + ((float) 6.5 + y1) * heigth);
					y1++;
					g2_1.drawString(shipAddress.substring(lineAddressNum, shipAddress.length()), (float) x, (float) y + ((float) 6.5 + y1) * heigth);
				}
				y1++;
				g2_1.drawString("联系电话：" + mOrder.getShipAddr().getPhone(), (float) x, (float) y + ((float) 6.5 + y1) * heigth);
				y1++;
				
				g2_1.drawLine((int) x - 2, (int) (y + ((float) 6.5 + y1) * heigth), (int) x + 144,
						(int) (y + ((float) 6.5 + y1) * heigth));
				y1++;
				g2_1.drawString("谢谢惠顾，欢迎再次光临！", (float) x, (float) y + ((float) 6.5 + y1) * heigth);
				y1++;
				g2_1.drawString("日期：" + df.format(date), (float) x, (float) y + ((float) 6.5 + y1) * heigth);

			// 【給顾客小票】
			{
				float baseHeight = printSize(mOrder.getOrderItems()) / 2;
				y += baseHeight;
				g2_1.setFont(font); // 设置字体
				g2_1.drawString(title + "（顾客留）", (float) x + 20, (float) y + 1 * heigth1);
				g2_1.setFont(font2);
				g2_1.drawString("订单号："+mOrder.getOrderNumber(), (float) x, (float) y + 2 * heigth1+5);
				g2_1.drawLine((int) x, (int) (y + 3 * heigth1), (int) x + 144, (int) (y + 3 * heigth1));
				g2_1.setFont(font2);
				g2_1.drawString("名称", (float) x, (float) y + 4 * heigth1);
				g2_1.setFont(font2); // 设置字体
				g2_1.setFont(font2); // 设置字体
				g2_1.drawString("数量", (float) x + space_1, (float) y + 4 * heigth1);
				g2_1.setFont(font2); // 设置字体
				g2_1.drawString("金额", (float) x + space_2, (float) y + 4 * heigth1);
				g2_1.setFont(font2); // 设置字体
				g2_1.drawLine((int) x - 2, (int) (y + 6 * (heigth )- 7), (int) x + 144, (int) (y + 6 * (heigth)- 7));

				y1 = 0;
				g2_1.setFont(font2);
				for (OrderItem orderItem : mOrder.getOrderItems()) {
					if (orderItem.getName().length() > lineGoodsNameCount) {
						g2_1.drawString(orderItem.getName().substring(0, lineGoodsNameCount), (float) x,
								(float) y + ((float) 6.5 + y1) * heigth);
					} else {
						g2_1.drawString(orderItem.getName().substring(0, orderItem.getName().length()), (float) x,
								(float) y + ((float) 6.5 + y1) * heigth);
					}

					g2_1.drawString("*" + orderItem.getQuantity(), (float) x + space_1,
							(float) y + ((float) 6.5 + y1) * heigth);
					g2_1.drawString((orderItem.getPrice().floatValue()*orderItem.getQuantity().floatValue())+ "", (float) x + space_2,
							(float) y + ((float) 6.5 + y1) * heigth);

					y1++;
					if (orderItem.getName().length() / lineGoodsNameCount > 0) {
						if (orderItem.getName().length() < (lineGoodsNameCount * 2)) {
							g2_1.drawString(
									orderItem.getName().substring(lineGoodsNameCount, orderItem.getName().length()),
									(float) x, (float) y + ((float) 6.5 + y1) * heigth);
						} else {
							g2_1.drawString(
									orderItem.getName().substring(lineGoodsNameCount, lineGoodsNameCount * 2 - 2) + "...",
									(float) x, (float) y + ((float) 6.5 + y1) * heigth);
						}
						y1++;
					}
				}
				// 配送费
				g2_1.drawLine((int) x - 2, (int) (y + ((float) 6.5 + y1) * heigth), (int) x + 144,
						(int) (y + ((float) 6.5 + y1) * heigth));
				y1++;
				g2_1.drawString("配送费：", (float) x, (float) y + ((float) 6.5 + y1) * heigth);
				g2_1.drawString(mOrder.getShipPrice()+"", (float) x + space_2, (float) y + ((float) 6.5 + y1) * heigth);
				y1++;
				g2_1.drawLine((int) x - 2, (int) (y + ((float) 6.5 + y1) * heigth)-7, (int) x + 144,
						(int) (y + ((float) 6.5 + y1) * heigth-7));
				y1++;				
				g2_1.drawString("总金额：" + mOrder.getTotal()+"元", (float) x, (float) y + ((float) 6.5 + y1) * heigth);
				y1++;
				g2_1.drawString("收货人：" + mOrder.getShipAddr().getName(), (float) x, (float) y + ((float) 6.5 + y1) * heigth);
				y1++;
				if(shipAddress.length()<lineAddressNum){
					g2_1.drawString("收货地址：" +shipAddress, (float) x, (float) y + ((float) 6.5 + y1) * heigth);
				}else{
					g2_1.drawString("收货地址：" +shipAddress.substring(0, lineAddressNum-1), (float) x, (float) y + ((float) 6.5 + y1) * heigth);
					y1++;
					g2_1.drawString(shipAddress.substring(lineAddressNum, shipAddress.length()), (float) x, (float) y + ((float) 6.5 + y1) * heigth);
				}
				y1++;
				g2_1.drawString("联系电话：" + mOrder.getShipAddr().getPhone(), (float) x, (float) y + ((float) 6.5 + y1) * heigth);
				y1++;
				g2_1.drawLine((int) x - 2, (int) (y + ((float) 6.5 + y1) * heigth), (int) x + 144,
						(int) (y + ((float) 6.5 + y1) * heigth));
				y1++;
				g2_1.drawString("谢谢惠顾，欢迎再次光临！", (float) x, (float) y + ((float) 6.5 + y1) * heigth);
				y1++;
				g2_1.drawString("日期：" + df.format(date), (float) x, (float) y + ((float) 6.5 + y1) * heigth);

			}
				return PAGE_EXISTS;
			default:
				return NO_SUCH_PAGE;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 用于将商品零售进行进行打印
	 * 
	 * @param title
	 * @param orderItem
	 * @param admin
	 * @param danjuh
	 * @param zongjie
	 * @param type
	 */
	public void print(String title, Order order) {
		// 通俗理解就是书、文档
		Book book = new Book();
		// 设置成竖打
		PageFormat pf = new PageFormat();
		pf.setOrientation(PageFormat.PORTRAIT); // LANDSCAPE表示竖打;PORTRAIT表示横打;REVERSE_LANDSCAPE表示打印空白
		// 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
		Paper p = new Paper();

		int length = printSize(order.getOrderItems());
		p.setSize(164, length); // 纸张大小(590, 840)表示A4纸
		p.setImageableArea(10, 10, 144, length); // A4(595 X
		// 842)设置打印区域，其实0，0应该是72，72
		// ，因为A4纸的默认X,Y边距是72

		pf.setPaper(p);
		// // 把 PageFormat 和 Printable 添加到书中，组成一个页面
		book.append(new PrintOrder(title, order), pf);
		// // 获取打印服务对象
		PrinterJob job = PrinterJob.getPrinterJob();
		// // 设置打印类
		job.setPageable(book);
		try {
			// // 可以用printDialog显示打印对话框，在用户确认后打印；也可以直接打印
			// boolean a = job.printDialog();
			// if (a) {
			job.print();
		} catch (PrinterException e) {
			e.printStackTrace();
		}
	}

	public Integer printSize(List<OrderItem> orderItemList) {
		int k = 11;
		int lines = 0;
		// if (orderItemList.size() != 0) {
		// k = k * orderItemList.size();
		// }
		for (OrderItem item : orderItemList) {
			if (item.getName().length() > lineGoodsNameCount)
				lines += 2;
			else {
				lines++;
			}
		}
		return (220 + k * lines) * 2;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	public static void main(String[] args) {
		PrintOrder printer = new PrintOrder();
		Order order=new Order();
		printer.print("果之源", order);
		// printer.print("果之源", Goods, "小美", "20113168494", "500", type);
	}
}
