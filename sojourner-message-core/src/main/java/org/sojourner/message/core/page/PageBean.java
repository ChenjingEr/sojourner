package org.sojourner.message.core.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PageBean<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int currentPage;
	private int numPerPage;

	private int totalCount;
	private List<T> recordList = new ArrayList<T>(0);

	private int totalPage;
	private int beginPageIndex;
	private int endPageIndex;

	private Map<String, Object> countResultMap;

	public PageBean() {
	}

	/**
	 * @param totalCount
	 * @param numPerPage
	 * @return 计算总页数
	 */
	public static int countTotalPage(int totalCount, int numPerPage) {
		if (totalCount % numPerPage == 0) {
			// 刚好整除
			return totalCount / numPerPage;
		} else {
			// 不能整除则总页数为：商 + 1
			return totalCount / numPerPage + 1;
		}
	}

	public static int checkCurrentPage(int totalCount, int numPerPage, int currentPage) {
		int totalPage = PageBean.countTotalPage(totalCount, numPerPage); // 最大页数
		if (currentPage > totalPage) {
			// 如果页面提交过来的页数大于总页数，则将当前页设为总页数
			// 此时要求totalPage要大于获等于1
			if (totalPage < 1) {
				return 1;
			}
			return totalPage;
		} else if (currentPage < 1) {
			return 1; // 当前页不能小于1（避免页面输入不正确值）
		} else {
			return currentPage;
		}
	}

	public PageBean(int currentPage, int numPerPage, int totalCount, List<T> recordList) {
		this.currentPage = currentPage;
		this.numPerPage = numPerPage;
		this.totalCount = totalCount;
		this.recordList = recordList;

		// 计算总页码
		totalPage = (totalCount + numPerPage - 1) / numPerPage;

		// 计算 beginPageIndex 和 endPageIndex
		if (totalPage <= 10) {
			// 如果总页数不多于10页，则全部显示
			beginPageIndex = 1;
			endPageIndex = totalPage;
		} else {
			// 如果总页数多于10页，则显示当前页附近的共10个页码
			// 当前页附近的共10个页码（前4个 + 当前页 + 后5个）
			beginPageIndex = currentPage - 4;
			endPageIndex = currentPage + 5;
			// 当前面的页码不足4个时，则显示前10个页码
			if (beginPageIndex < 1) {
				beginPageIndex = 1;
				endPageIndex = 10;
			}
			// 当后面的页码不足5个时，则显示后10个页码
			if (endPageIndex > totalPage) {
				endPageIndex = totalPage;
				beginPageIndex = totalPage - 10 + 1;
			}
		}
	}

	/**
	 * 只接受前5个必要的属性，会自动的计算出其他3个属生的值
	 * 
	 * @param currentPage
	 * @param numPerPage
	 * @param totalCount
	 * @param recordList
	 */
	public PageBean(int currentPage, int numPerPage, int totalCount, List<T> recordList,
			Map<String, Object> countResultMap) {
		this.currentPage = currentPage;
		this.numPerPage = numPerPage;
		this.totalCount = totalCount;
		this.recordList = recordList;
		this.countResultMap = countResultMap;

		// 计算总页码
		totalPage = (totalCount + numPerPage - 1) / numPerPage;

		// 计算 beginPageIndex 和 endPageIndex
		if (totalPage <= 10) {
			// 如果总页数不多于10页，则全部显示
			beginPageIndex = 1;
			endPageIndex = totalPage;
		} else {
			// 如果总页数多于10页，则显示当前页附近的共10个页码
			// 当前页附近的共10个页码（前4个 + 当前页 + 后5个）
			beginPageIndex = currentPage - 4;
			endPageIndex = currentPage + 5;
			// 当前面的页码不足4个时，则显示前10个页码
			if (beginPageIndex < 1) {
				beginPageIndex = 1;
				endPageIndex = 10;
			}
			// 当后面的页码不足5个时，则显示后10个页码
			if (endPageIndex > totalPage) {
				endPageIndex = totalPage;
				beginPageIndex = totalPage - 10 + 1;
			}
		}
	}
	
	public static int checkNumPerPage(int numPerPage) {
        if (numPerPage > PageParam.MAX_PAGE_SIZE) {
            return PageParam.MAX_PAGE_SIZE;
        } else if (numPerPage < 1) {
            return PageParam.DEFAULT_NUM_PER_PAGE;
        } else {
            return numPerPage;
        }
    }

	public List<T> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<T> recordList) {
		this.recordList = recordList;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getBeginPageIndex() {
		return beginPageIndex;
	}

	public void setBeginPageIndex(int beginPageIndex) {
		this.beginPageIndex = beginPageIndex;
	}

	public int getEndPageIndex() {
		return endPageIndex;
	}

	public void setEndPageIndex(int endPageIndex) {
		this.endPageIndex = endPageIndex;
	}

	public Map<String, Object> getCountResultMap() {
		return countResultMap;
	}

	public void setCountResultMap(Map<String, Object> countResultMap) {
		this.countResultMap = countResultMap;
	}

}
