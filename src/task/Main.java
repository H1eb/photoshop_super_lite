package task;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Main
{
	public static void main(String[] args) throws Exception
	{	

		switch(args[0])
		{
			case ("mem"):
				ParseRequest(args);
				break;
			case ("help"):
				ShowHelp();
				break;
			default:
				System.out.println("��� ������ �������������� help");
		}
		
		
		
	}
	
	//�������� ������� mem
	public static void ParseRequest(String[] args) throws Exception
	{
		Map<String, String> res = new HashMap<>();
		
		Pair full_path = new Pair();
		Pair pos_x = new Pair("center", 0);
		Pair pos_y = new Pair("top", 0);
		Pair family = new Pair("Monospaced", 0);
		Pair color = new Pair("white", 0);
		Pair size = new Pair("40", 0);
		Pair text = new Pair();
		
		//��������� ������� ����
		full_path = WriteToStr(0, args, ".png");
		//��������� �������� ������ ����
		if (full_path.str.startsWith("./"))
		{
			String command_path = (String)(System.getProperty("user.dir").toString()).replace('\\', '/');
			full_path.str = full_path.str.replaceFirst(".", command_path);
		}
		
		//�������� ������ ����� ����� � ����� ��� ����������
		int last_symb = full_path.str.lastIndexOf('.');
		String path = full_path.str.substring(0, last_symb);
		Pair new_name = new Pair(path +" copy.png", 0);
		
		int num = full_path.num;
		
		//��������� �������������� ���������� ��� ������
		for (int i = 0; i < args.length; i++)
		{
			switch (args[i].toString())
			{
				case ("position-x:"):
					pos_x = new Pair(args[i+1], i++);
					num += 2;
					break;
				case ("position-y:"):
					pos_y = new Pair(args[i+1], i++);
					num += 2;
					break;
				case ("font-style:"):
					family = new Pair(args[i+1], i++);
					num += 2;
					break;
				case ("color:"):
					color = new Pair(args[i+1], i++);
					num += 2;
					break;
				case ("text-size:"):
					size = new Pair(args[i+1], i++);
					num += 2;
					break;
			}
		}
		
		//��������� ������ ������
		text = WriteToStr(num, args, "'");
		
		//���� � ������� ���� ����� �������� �����, ��������� ���
		if(text.num != args.length-1)
			new_name = WriteToStr(text.num, args, ".png");
		
		res.put("full-path", full_path.str);
		res.put("pos-x", pos_x.str);
		res.put("pos-y", pos_y.str);
		res.put("font-style", family.str);
		res.put("text-size", size.str);
		res.put("color", color.str);
		res.put("text", text.str.substring(1, text.str.length()-1));
		res.put("new-name", new_name.str);

		
		ChangeImg(res);
	}
	
	//�������������� ������� � ������
	public static Pair WriteToStr(int i, String[] args, String rule)
	{
		int check = 0;
		String str = "";
			
		while (!args[i].endsWith(rule)) {
			i++;
			
			if (check == 0)
				str += args[i];
			else
				str += " " + args[i];
			check = 1;
		}
		

		Pair res = new Pair(str, i);
		
		return res;
	}
	
	//�������������� ��������
	public static void ChangeImg(Map<String, String> request) throws Exception
	{
		BufferedImage image = ImageIO.read(new File(request.get("full-path")));
		
		Graphics g = image.getGraphics();

		//�������� ����� ������
		g.setColor((Color)(Color.class.getField(request.get("color")).get(null)));
		
		//��������� ������� ������
		int text_size = Integer.parseInt(request.get("text-size"));
		
		//��������� ����� ������
		Font font = new Font(request.get("font-style")
							, Font.BOLD
							, text_size);
		g.setFont(font);
		
		int position_y = 0;
		
		//������������ ������������ ������ �� ���������
		switch (request.get("pos-y"))
		{
			case("top"):
				position_y = text_size + 10;
				break;
			case("center"):
				position_y = (int) (0.5*text_size + image.getHeight()/2);
				break;
			case("bottom"):
				position_y = image.getHeight() - 10;
				break;
			default:
				position_y = text_size;
				break;
		}
		
		//������������ ������������ ������ �� �����������
		switch (request.get("pos-x"))
		{
			case("left"):
				g.drawString(request.get("text")
						, 1
						, position_y);
				break;
			case("center"):
				g.drawString(request.get("text")
						, (int) (image.getWidth()/2 - text_size * 3)
						, position_y);
				break;
			case("right"):
				g.drawString(request.get("text")
						, (int) (image.getWidth() - text_size * 7)
						, position_y);
				break;
			default:
				g.drawString(request.get("text")
						, (int) (image.getWidth()/2 - text_size * 3)
						, position_y);
				break;
		}
		
		
		g.dispose();
		
		//���������� �����
		ImageIO.write(image, "png", new File(request.get("new-name")));
	}
	
	//������
	public static void ShowHelp()
	{
		System.out.println("help - ������ �� �������������");
		System.out.println("mem - �������� ����� � ��������");
		System.out.println("\t\tmem [path to image] [parameters] [text] [rename file]");
		System.out.println("\tposition-x - ������������ ������ �� X (left, center, right)");
		System.out.println("\t\tdefault: center");
		System.out.println("\tposition-x - ������������ ������ �� Y (left, center, right)");
		System.out.println("\t\tdefault: top");
		System.out.println("\tfont-style - ��� ������");
		System.out.println("\t\tdefault: Monospaced");
		System.out.println("\ttext-size - ������ ������");
		System.out.println("\t\tdefault: 40");
		System.out.println("\tfont-style - ���� ������");
		System.out.println("\t\tdefault: white");
		System.out.println("\ttext - ����������� �����");
		System.out.println("\trename file - �������� ������ �����");
		System.out.println("\t\tdefault: ");
	}
}
