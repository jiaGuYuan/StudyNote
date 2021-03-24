第一步：需要加jdom.jar。

1.	创建一个XML文档：
	/*
		<?xml version="1.0" encoding="gbk"?>
		<resume>
			<name>李世民</name>
			<preName>秦王</preName>
			<ocucupation dynasty="唐朝" >皇帝</occupation>
			<preOccupation>将军</preOccupation>
		</resume>
	*/
	public static void createXML() throws FileNotFoundException, IOException {
		
		//构建元素节点和相关属性
		Element root = new Element("resume");  //<resume></resume>
		Element name = new Element("name");    //<name></name>
		Element preName = new Element("preName"); //<preName></preName>

		// <ocucupation dynasty="唐朝" ></occupation>
		Element occupation = new Element("occupation");
		Attribute attr = new Attribute("dynasty","唐朝");
		occupation.setAttribute(attr);   
		
		Element preOccupation = new Element("preOccupation");

		name.setText("李世民");   //setText只用于给元素增加文本。
		preName.addContent("秦王");  //addContent：不光可以给元素增加文本，也可以增加元素，注释等等。
		preOccupation.addContent("将军");  //<preOccupation>将军</preOccupation>
		occupation.addContent("皇帝");   
 		
		root.addContent(name);
		root.addContent(preName);
		root.addContent(occupation);
		root.addContent(preOccupation);
 		
		Document doc = new Document(root);
		
		Format f = Format.getPrettyFormat();
		//Format f = Format.getCompactFormat();
		f.setEncoding("gbk");   //设置生成的xml文档的编码方式：<?xml version="1.0" encoding="gbk"?>
		
		XMLOutputter xmlOut = new XMLOutputter(f);
		xmlOut.output(doc, new FileOutputStream("d:/2.xml"));
	}

2.	修改XML文档
	public static void updateXML(String  filePath) throws FileNotFoundException, JDOMException, IOException {
		
		SAXBuilder sb = new  SAXBuilder();
		Document doc = sb.build(new FileInputStream(filePath));  //将某一个xml文件读入到doc中。
		//对内存中的doc进行操作。
		Element root = doc.getRootElement();  //一旦获得了根元素，就可以对所有XML数据进行处理。
		
		Element e = root.getChild("occupation");
		System.out.println(e.getText());    //返回元素的文本内容
		System.out.println(e.getName());	//返回元素的标记名
		System.out.println(e.getAttributeValue("dynasty"));     //返回元素的属性值
		e.setText("lishimin");  //通过setText,getText直接设置元素的文本

		List li = root.getContent();   //所有内容：子元素、注释、文本等等
		List li1 = root.getChildren();  //只有标记内容
		System.out.println(li.size());
		System.out.println(li1.size());
		
		List list = root.getChildren();
		Element ele = (Element) list.get(0);
//		System.out.println(ele.getText());
		Attribute attr =new Attribute("国家","中国");
		ele.setAttribute(attr);
		
		XMLOutputter xmlOut = new XMLOutputter();   //讲doc写入到某一个xml文件中，从而更新硬盘中的文件
		xmlOut.output(doc, new FileOutputStream(filePath));
	}


3.	使用XPATH解析XML文档（需加入jar包：saxpath.jar，jaxen-core.jar，jaxen-jdom.jar）
	public static void testXPath(String  filePath) throws FileNotFoundException, JDOMException, IOException {
		// TODO Auto-generated method stub
		SAXBuilder sb = new  SAXBuilder();
		Document doc = sb.build(new FileInputStream(filePath));  //将某一个xml文件读入到doc中。
		//对内存中的doc进行操作。
		Element root = doc.getRootElement();  //一旦获得了根标记，就可以对所有DOM中的数据进行处理。
		
		XPath xpath = XPath.newInstance("/resume/*"); //返回resume节点下面所有的子节点。
//		XPath xpath = XPath.newInstance("//preOccupation"); //返回文档中所有preOccupation节点。
//		XPath xpath = XPath.newInstance("/resume/wife//preOccupation"); //返回/resume/wife所有preOccupation节点。
//		XPath xpath = XPath.newInstance("/resume/wife/preOccupation");   //返回/resume/wife/preOccupation这个节点。
//		XPath xpath = XPath.newInstance("/resume/wife/preOccupation[@period]");  //返回名字为preOccupation有属性peroid的节点。
//		XPath xpath = XPath.newInstance("//preOccupation[@period]");  //返回文档中所有preOccupation节点，并且有属性period
//		XPath xpath = XPath.newInstance("/resume/wife/preOccupation[@period='5-18']");  //返回名字为preOccupation属性peroid='5-18'的节点。
//		XPath xpath = XPath.newInstance("//preOccupation[text()='皇子']");  //返回文档中所有preOccupation节点，并且文本内容为皇子。
		
		List list = xpath.selectNodes(root);
		for (int i = 0; i < list.size(); i++) {
			Element e = (Element)list.get(i);
			System.out.println(e.getText());
		}
		
//		Element e2 =(Element) xpath.selectSingleNode(root);
//		System.out.println(e2.getText());
		
	}
