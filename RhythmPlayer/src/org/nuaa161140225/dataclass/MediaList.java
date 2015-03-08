package org.nuaa161140225.dataclass;

import java.util.ArrayList;
import java.util.List;

/**
 * 模板类，用于存储一个媒体列表。包含的数据有：
 * {@link MediaList#list}
 * {@link MediaList#ListName}
 * {@link MediaList#alterNum}
 * @author 吴香礼
 * @see MediaList
 */
public class MediaList<T extends Object> {
	
	/**
	 * ListName 媒体列表名，如本地音乐,作为一组;
	 * @see {@link MediaList#ListName}
	 */
	private String ListName;
	/**
	 * list 存储媒体列表。
	 * @see {@link MediaList#list}
	 */
	private List<T> list;
	/**
	 * alterNum记录该列表新更改的媒体的数目
	 * @see {@link MediaList#alterNum}
	 */
	private int alterNum;
	
	/**
	 * 默认构造函数，给类中成员赋初值
	 * @author 吴香礼
	 * @see {@link MediaList#MediaList()}
	 */
	public MediaList()
	{
		super();
		this.ListName = "";
		this.list = new ArrayList<T>();
		this.alterNum = 0;
	}
	
	/**
	 * 使用给出的数据构造类
	 * @author 吴香礼
	 * @param listname 用于初始化媒体类型的名称。
	 * @param list	用于初始化媒体类中的成员
	 * @param num 用于初始化新添加的媒体数
	 * @see {@link MediaList#MediaList(String, int, List)}
	 */
	public MediaList(String listname,int num,List<T> list)
	{
		super();
		this.ListName = listname;
		this.alterNum = num;
		this.list = list;
	}
	
	/**
	 * 设置媒体的类型名称
	 * @author 吴香礼
	 * @param listname 媒体类型名
	 * @see {@link MediaList#setListName(String)}
	 */
	public void setListName(String listname)
	{
		this.ListName = listname;
	}
	
	/**
	 * 设置媒体类的成员,或者替换原有内容
	 * @author 吴香礼
	 * @param list 用于设置的数据
	 * @see {@link MediaList#setList(List)}
	 */
	public void setList(List<T> list)
	{
		this.list = list;
	}
	
	/**
	 * 设置最近
	 * @author 吴香礼
	 * @param num 设置最近新添的媒体数
	 * @see {@link MediaList#setNew_num(int)}
	 */
	public void setAlterNum(int num)
	{
		this.alterNum = num;
	}
	
	/**
	 * 获取最近更改的媒体数
	 * @author 吴香礼
	 * @return 最近新添加的媒体数
	 * @see {@link MediaList#getNew_num()}
	 */
	public int getAlterNum()
	{
		return alterNum;
	}

	/**
	 * 获取媒体类的名称
	 * @author 吴香礼
	 * @return 该媒体类的名称
	 * @see {@link MediaList#getListName()}
	 */
	public String getListName()
	{
		return ListName;
	}
	
	/**
	 * 获取该媒体类的所有成员
	 * @author 吴香礼
	 * @return 该类中的所有成员
	 * @see {@link MediaList#getList()}
	 */
	public List<T> getList()
	{
		return list;
	}

	/**
	 * 清空该媒体类的所有成员,alterNum记录清空的成员数，列表名不变。
	 * @author 吴香礼
	 * @see {@link MediaList#clearMediaList()}
	 */
    public void clearMediaList()
    {
    	this.alterNum = this.list.size() ;
    	this.list = new ArrayList<T>();
    }

    /**
     * 添加数据到该媒体列表中，alterNum变为参数list的长度
     * @author 吴香礼
     * @param list 欲添加的数据
     * @see {@link MediaList#addAll(List)}
     */
    public void addAll(List<T> list)
    {
    	this.list.addAll(list);
    	this.alterNum = list.size();
    }

    /**
     * 添加一个成员到列表中
     * @param item 添加的成员
     */
    public void add(T item)
    {
    	this.list.add(item);
    	this.alterNum = 1;
    }
    
}
