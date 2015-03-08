package org.nuaa161140225.dataclass;


import org.nuaa161140225.dataclass.SongInfo;

/**
 * 用于存放歌曲基本信息的类
 * @author 吴香礼
 * @see SongInfo
 */
public class SongInfo {

	/**
	 * mfileName 歌曲的文件名
	 * @see {@link SongInfo#mFileName}
	 */
	private String mFileName = "";

	/**
	 * mFileTitle 歌曲标题
	 * {@link SongInfo#mFileTitle}
	 */
	private String mFileTitle ="";

	/**
	 * mDuration 歌曲的时长
	 * @see {@link SongInfo#mDuration}
	 */
	private String mDuration = "";

	/**
	 *mSinger 歌手名
	 *@see {@link SongInfo#mSinger}
	 */
	private String mSinger = "";

	/**
	 * mAlbum 专辑名 
	 * @see {@link SongInfo#mAlbum}
	 */
	private String mAlbum = "";

	/**
	 * mYear 歌曲年份
	 * @see {@link SongInfo#mYear}
	 */
	private String mYear = "";

	/**
	 * mFileType 歌曲文件的类型
	 * @see {@link SongInfo#mFileTitle}
	 */
	private String mFileType = "";

	/**
	 * mFileSize 文件的大小
	 * @see {@link SongInfo#mFileSize}
	 */
	private String mFileSize = "";

	/**
	 * mFilePath 文件的路径
	 * @see {@link SongInfo#mFilePath}
	 */
	private String mFilePath = "";

	/**
	 * LrcPath 歌曲的歌词路径
	 * @see {@link SongInfo#LrcPath}
	 */
	private String LrcPath="";
 
	private boolean enable;
	/**
	 * 默认构造函数
	 * @see {@link SongInfo#SongInfo()}
	 */
	public SongInfo() {
		super();
		enable = true;
	}

	/**
	 * 构造函数，用给出的参数构造
	 * @param mFileName 文件名
	 * @param mFileTitle  文件标题
	 * @param mDuration 文件长度
	 * @param mSinger 歌手名	
	 * @param mAlbum 专辑名
	 * @param mYear 年份
	 * @param mFileType 文件类型
	 * @param mFileSize 文件大小
	 * @param mFilePath 文件路径
	 * @param mlrcPath 歌词路径
	 * @see {@link SongInfo#SongInfo(String, String, String, String, String, String, String, String, String, String)}
	 */
	public SongInfo(String mFileName, String mFileTitle, String mDuration,
			String mSinger, String mAlbum, String mYear, String mFileType,
			String mFileSize, String mFilePath, String mlrcPath) {
		super();
		this.mFileName = mFileName;
		this.mFileTitle = mFileTitle;
		this.mDuration = mDuration;
		this.mSinger = mSinger;
		this.mAlbum = mAlbum;
		this.mYear = mYear;
		this.mFileType = mFileType;
		this.mFileSize = mFileSize;
		this.mFilePath = mFilePath;
		this.LrcPath = mlrcPath;
		this.enable = true;
	}

	/**
	 * 获取文件的RUL
	 * @author 吴香礼
	 * @return 文件的URL
	 * @see {@link SongInfo#getLrcPath()}
	 */
	public String getLrcPath() {
		return LrcPath;
	}

	/**
	 * 设置文件的URL
	 * @author 吴香礼
	 * @param mFileUrl 新的URL
	 * @see {@link SongInfo#setmLrcPath(String)}
	 */
	public void setmLrcPath(String mLrcPath) {
		this.LrcPath = mLrcPath;
	}

	/**
	 * 获取文件名称
	 * @author 吴香礼
	 * @return 文件的名称
	 * @see {@link SongInfo#getmFileName()}
	 */
	public String getmFileName() {
		return mFileName;
	}

	/**
	 * 设置文件名
	 * @author 吴香礼
	 * @param mFileName 新的文件名
	 * @see {@link SongInfo#setmFileName(String)}
	 */
	public void setmFileName(String mFileName) {
		this.mFileName = mFileName;
	}

	/**
	 * 获取文件的标题
	 * @author 吴香礼
	 * @return 文件的标题
	 * @see {@link SongInfo#getmFileTitle()}
	 */
	public String getmFileTitle() {
		return mFileTitle;
	}

	/**
	 * 设置文件的标题
	 * @author 吴香礼
	 * @param mFileTitle 新的文件标题
	 * @see {@link SongInfo#setmFileTitle(String)}
	 */
	public void setmFileTitle(String mFileTitle) {
		this.mFileTitle = mFileTitle;
	}

	/**
	 * 获取文件的长度
	 * @author 吴香礼
	 * @return 文件的长度
	 * @see {@link SongInfo#getmDuration()}
	 */
	public String getmDuration() {
		return mDuration;
	}

	/**
	 * 设置文件的长度
	 * @author 吴香礼
	 * @param mDuration 新的时间长度
	 * @see {@link SongInfo#setmDuration(String)}
	 */
	public void setmDuration(String mDuration) {
		this.mDuration = mDuration;
	}

	/**
	 * 获取歌手
	 * @author 吴香礼
	 * @return 歌手
	 * @see {@link SongInfo#getmSinger()}
	 */
	public String getmSinger() {
		return mSinger;
	}

	/**
	 * 设置歌手
	 * @author 吴香礼
	 * @param mSinger  新的歌手
	 * @see {@link SongInfo#setmSinger(String)}
	 */
	public void setmSinger(String mSinger) {
		this.mSinger = mSinger;
	}

	/**
	 * 获取专辑名称
	 * @author 吴香礼
	 * @return 文件的专辑名称
	 * @see {@link SongInfo#getmAlbum()}
	 */
	public String getmAlbum() {
		return mAlbum;
	}

	/**
	 * 设置文件的专辑名称
	 * @author 吴香礼
	 * @param mAlbum 新的专辑名称
	 * @see {@link SongInfo#setmAlbum(String)}
	 */
	public void setmAlbum(String mAlbum) {
		this.mAlbum = mAlbum;
	}

	/**
	 * 获取文件的年份
	 * @author 吴香礼
	 * @return 文件的年份
	 * @see {@link SongInfo#getmYear()}
	 */
	public String getmYear() {
		return mYear;
	}

	/**
	 * 设置文件的年份
	 * @author 吴香礼
	 * @param mYear 新的年份
	 * @see {@link SongInfo#setmYear(String)}
	 */
	public void setmYear(String mYear) {
		this.mYear = mYear;
	}

	/**
	 * 获取文件类型
	 * @author 吴香礼
	 * @return 文件的类型
	 * @see {@link SongInfo#getmFileType()}
	 */
	public String getmFileType() {
		return mFileType;
	}

	/**
	 * 设置文件类型
	 * @author 吴香礼
	 * @param mFileType 新的文件类型
	 * @see {@link SongInfo#setmFileType(String)}
	 */
	public void setmFileType(String mFileType) {
		this.mFileType = mFileType;
	}

	/**
	 * 获取文件的大小
	 * @author 吴香礼
	 * @return 文件的大小
	 * @see {@link SongInfo#getmFileSize()}
	 */
	public String getmFileSize() {
		return mFileSize;
	}

	/**
	 * 设置文件的大小
	 * @author 吴香礼
	 * @param mFileSize 新的文件大小信息
	 * @see {@link SongInfo#setmFileSize(String)}}
	 */
	public void setmFileSize(String mFileSize) {
		this.mFileSize = mFileSize;
	}

	/**
	 * 获取文件的路径
	 * @author 吴香礼
	 * @return 文件的路径
	 * @see {@link SongInfo#getmFilePath()}
	 */
	public String getmFilePath() {
		return mFilePath;
	}

	/**
	 * 设置文件的路径
	 * @author 吴香礼
	 * @param mFilePath 新的文件路径
	 * @see {@link SongInfo#setmFilePath(String)}
	 */
	public void setmFilePath(String mFilePath) {
		this.mFilePath = mFilePath;
	}

	/**
	 * @return the enable
	 */
	public boolean isEnable() {
		return enable;
	}

	/**
	 * @param enable the enable to set
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

}
